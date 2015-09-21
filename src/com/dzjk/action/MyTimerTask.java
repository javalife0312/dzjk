package com.dzjk.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dzjk.model.DZJK_Device;
import com.dzjk.model.DZJK_DeviceTask;
import com.template.service.Service;

@org.springframework.stereotype.Service  
public class MyTimerTask{
	private Service service;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	/*************************************************************************
	 * 定时生成 巡检任务
	 ************************************************************************/
	//秒 分 时 天 周 年
	public void doTask() {
		System.out.println("我的调度器 。。。。。。。。。。。。。。。。");
		Map<Integer, String> tasks = new HashMap<Integer, String>();
		List<Object> devices = new ArrayList<Object>();

		tasks = getLastTask();

		String hql = "From DZJK_Device";
		devices = service.listByHql(hql, 0, Integer.MAX_VALUE);
		for (Object obj : devices) {
			DZJK_Device device = (DZJK_Device) obj;
			String taskDate = "";
			if (tasks.containsKey(device.getId())) {
				taskDate = tasks.get(device.getId());
			}
			checkAndCreateTask(device, taskDate);
		}
	}

	private String dateAdd(String srcDate, Integer diff) {
		String result = "";
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(dateFormat.parse(srcDate));
			calendar.add(Calendar.DATE, diff);
			result = dateFormat.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		MyTimerTask myTimerTask = new MyTimerTask();
		System.out.println(myTimerTask.dateAdd("20150601", 1));
	}

	/**
	 * @param dzjk_Device
	 * 检测是否需要生成新的任务，如果需要生成新的任务
	 */
	private void checkAndCreateTask(DZJK_Device device, String taskDate) {
		String nowDate = dateFormat.format(new Date());

		DZJK_DeviceTask deviceTask = new DZJK_DeviceTask();
		deviceTask.setDeviceId(device.getId());
		deviceTask.setDeviceName(device.getName());
		deviceTask.setWeijianId(device.getExecutorId());
		deviceTask.setWeijianName(device.getExecutorName());
		deviceTask.setTipDays(device.getTipDays());
		deviceTask.setPositionId(device.getFid());
		deviceTask.setPositionName(device.getPosition());
		
		// 如果taskDate是""说明是第一次生成任务，直接生成task
		if (taskDate.equals("")) {
			deviceTask.setStatus(0);
			deviceTask.setTaskDate(nowDate);
			service.saveOrUpdate(deviceTask);
		} else {
			String tmpDate = dateAdd(taskDate,device.getTipDays());
			//如果时间一致生成当天任务
			if(tmpDate.equals(nowDate)){
				deviceTask.setStatus(0);
				deviceTask.setTaskDate(nowDate);
				service.saveOrUpdate(deviceTask);
			}
//			//如果时间小于今天需要补全历史 不需要补生成
//			if(tmpDate.compareTo(nowDate)<0){
//				deviceTask.setStatus(2);
//				deviceTask.setTaskDate(tmpDate);
//				service.saveOrUpdate(deviceTask);
//				//递归，补全任务
//				checkAndCreateTask(device,tmpDate);
//			}
		}
	}

	/**
	 * @return 检索出任务列表中每条任务的最后生成时间
	 */
	private Object[] trimNull(Object[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == null) {
				arr[i] = "";
			}
		}
		return arr;
	}

	private Map<Integer, String> getLastTask() {
		Map<Integer, String> tasks = new HashMap<Integer, String>();
		String sql = "select deviceId,max(taskDate) from dzjk_device_task group by deviceId;";
		List<Object> list = service.listBySql(sql);
		for (Object tmp : list) {
			Object[] obj_arr = (Object[]) tmp;
			obj_arr = trimNull(obj_arr);
			Integer deviceId = Integer.valueOf(obj_arr[0].toString());
			String taskDate = obj_arr[1].toString();
			tasks.put(deviceId, taskDate);
		}
		return tasks;
	}
}
