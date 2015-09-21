package com.template.util;

public class ToExcel {
//	
//	private static List<Kdd_Info> ExportList = new ArrayList<Kdd_Info>();
//	private static List<Kdd_Report> ExportReportList = new ArrayList<Kdd_Report>();
//	
//	public static void setExportList(List<Kdd_Info> list){
//		if(ExportList != null){
//			ExportList.clear();
//		}
//		ExportList = list;
//	}
//	
//	public static List<Kdd_Info> getExportList(){
//		return ExportList;
//	}
//	public static void setExportReiprtList(List<Kdd_Report> list){
//		if(ExportReportList != null){
//			ExportReportList.clear();
//		}
//		ExportReportList = list;
//	}
//	
//	public static List<Kdd_Report> getExportReportList(){
//		return ExportReportList;
//	}
//	
//
//	/**
//	 * @param request
//	 * @param response
//	 * @param title
//	 * @param header data-index-中文名字
//	 * @param data
//	 */
//	public static void exportExcel(HttpServletRequest request, HttpServletResponse response,String title,Integer[] headerIndex,List<Kdd_Info> list) {
//		String name = new Date().getTime()+"";
//		
//		//处理时间签字的问题
//		boolean dateQianming = false;
//		Integer[] tmpArr = null;
//		for (Integer val : headerIndex) {
//			if(val == 11){
//				dateQianming = true;
//			}
//		}
//		if(dateQianming){
//			tmpArr = new Integer[headerIndex.length-1];
//			int index = 0;
//			for (Integer val : headerIndex) {
//				if(val == 11){
//					continue;
//				}
//				tmpArr[index] = val;
//				index++;
//			}
//			headerIndex = tmpArr;
//		}
//		//处理时间签字的问题
//
//		String url = request.getSession().getServletContext().getRealPath("/");
//		String filename = "" + name + ".xls";
//		try {
//			FileOutputStream fileOutputStream = new FileOutputStream(url + filename);
//			
//
//			Workbook workbook = new HSSFWorkbook();
//			Sheet sheet = workbook.createSheet();
//			Cell cell = null;
//			
//			//创建一个sheet
//			workbook.setSheetName(0, "Sheet");
//			
//			sheet.addMergedRegion(new CellRangeAddress(
//	                0, 						//first row (0-based)
//	                0, 						//last row  (0-based)
//	                0, 						//first column (0-based)
//	                (headerIndex.length-1)  //last column  (0-based)
//	        ));//设置合并的区域
//			
////			//创建表头
////			DataFormat dataFormat = workbook.createDataFormat();
//			
//			sheet.setDisplayGridlines(true);
//			sheet.setPrintGridlines(true);
//			
//			CellStyle titleStyle = workbook.createCellStyle();
//			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//			
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(CellStyle.ALIGN_RIGHT);
//			
//			int rowIndex = 0;
//			String[] header = getHeader(headerIndex);
//			Row row_title = sheet.createRow(rowIndex);
//			cell = row_title.createCell(0);
//			cell.setCellStyle(titleStyle);
//			cell.setCellValue(title);
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);    
//			rowIndex++;
//			
//			Row row_header = sheet.createRow(rowIndex);
//			Map<Integer, Integer> headWidthMap = exportHeaderWidth();
//			for (short cellnum = 0; cellnum < header.length; cellnum++) {
//				cell = row_header.createCell(cellnum);
//				cell.setCellValue(header[cellnum]);
//				cell.setCellStyle(titleStyle);
//				sheet.setColumnWidth(cellnum, headWidthMap.get(headerIndex[cellnum]));
//			}
//			rowIndex++;
//			
//			//填充数据
//			List<String[]> table_data = getData(headerIndex, list);
//			Row row_data = null;
//			for (short i = 0; i<table_data.size(); i++) {
//				String[] tmpData = table_data.get(i);
//				row_data = sheet.createRow(rowIndex);
//				for (short cellnum = 0; cellnum < header.length; cellnum++) {
//					cell = row_data.createCell(cellnum);
//					cell.setCellValue(tmpData[cellnum]);
//					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					cell.setCellStyle(dataStyle);
//				}
//				rowIndex++;
//				
//			}
//			
//			//设置时间和签名
//			if(dateQianming){
//				Row row_data1 = null;
//				
//				row_data1 = sheet.createRow(rowIndex);
//				//第0列显示日期
//				cell = row_data1.createCell(0);
//				cell.setCellValue("日期");
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				sheet.addMergedRegion(new CellRangeAddress(
//		                rowIndex, 						//first row (0-based)
//		                rowIndex, 						//last row  (0-based)
//		                1, 						//first column (0-based)
//		                2  //last column  (0-based)
//		        ));
//				rowIndex++;
//				
//				row_data1 = sheet.createRow(rowIndex);
//				//第3列显示签名
//				cell = row_data1.createCell(0);
//				cell.setCellValue("签名");
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				sheet.addMergedRegion(new CellRangeAddress(
//		                rowIndex, 						//first row (0-based)
//		                rowIndex, 						//last row  (0-based)
//		                1, 						//first column (0-based)
//		                2
//		        ));
//				
//			}
//			workbook.write(fileOutputStream);
//			fileOutputStream.flush();
//			fileOutputStream.close();
//			
//			
//			
//			String filePath = url + name + ".xls";
//			String fname = name + ".xls";
//			File file = new File(filePath);
//			downLoadData(response, file, fname);
//			file.delete();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	/**
//	 * @param request
//	 * @param response
//	 * @param title
//	 * @param header data-index-中文名字
//	 * @param data
//	 */
//	public static void exportReportExcel(HttpServletRequest request, HttpServletResponse response,String title,List<Kdd_Report> list) {
//		long name = new Date().getTime();
//		
//		String url = request.getSession().getServletContext().getRealPath("/");
//		String filename = "" + name + ".xls";
//		try {
//			FileOutputStream fileOutputStream = new FileOutputStream(url + filename);
//			
//			
//			Workbook workbook = new HSSFWorkbook();
//			Sheet sheet = workbook.createSheet();
//			Cell cell = null;
//			
//			//创建一个sheet
//			workbook.setSheetName(0, "Sheet");
//			
//			sheet.addMergedRegion(new CellRangeAddress(
//					0, 						//first row (0-based)
//					0, 						//last row  (0-based)
//					0, 						//first column (0-based)
//					12  	//last column  (0-based)
//					));//设置合并的区域
//			
//			//创建表头
////			DataFormat dataFormat = workbook.createDataFormat();
//			
//			sheet.setDisplayGridlines(true);
//			sheet.setPrintGridlines(true);
//			
//			CellStyle titleStyle = workbook.createCellStyle();
//			titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//			
//			CellStyle dataStyle = workbook.createCellStyle();
//			dataStyle.setAlignment(CellStyle.ALIGN_RIGHT);
//			
//			//title
//			int rowIndex = 0;
//			Row row_title = sheet.createRow(rowIndex);
//			cell = row_title.createCell(0);
//			cell.setCellStyle(titleStyle);
//			cell.setCellValue(title);
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);    
//			rowIndex++;
//			
//			//head
//			Row row_header = sheet.createRow(rowIndex);
//			rowIndex++;
//			cell = row_header.createCell(0);
//			cell.setCellValue("快递单类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(1);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(2);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(3);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(4);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(5);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(6);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(7);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(8);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(9);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(10);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(11);
//			cell.setCellValue("类型");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			cell = row_header.createCell(12);
//			cell.setCellValue("数量");
//			cell.setCellStyle(titleStyle);
//			sheet.setColumnWidth(0,3000);
//			
//			
//			//填充数据
//			Row row_data = null;
//			for (short i = 0; i<list.size(); i++) {
//				row_data = sheet.createRow(rowIndex+i);
//				Kdd_Report kdd_Report = list.get(i);
//				cell = row_data.createCell(0);
//				cell.setCellValue(kdd_Report.getKddType());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(1);
//				cell.setCellValue(kdd_Report.getType1());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(2);
//				cell.setCellValue(kdd_Report.getSum1());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(3);
//				cell.setCellValue(kdd_Report.getType2());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(4);
//				cell.setCellValue(kdd_Report.getSum2());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(5);
//				cell.setCellValue(kdd_Report.getType3());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(6);
//				cell.setCellValue(kdd_Report.getSum3());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(7);
//				cell.setCellValue(kdd_Report.getType4());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(8);
//				cell.setCellValue(kdd_Report.getSum4());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(9);
//				cell.setCellValue(kdd_Report.getType5());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(10);
//				cell.setCellValue(kdd_Report.getSum5());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(11);
//				cell.setCellValue(kdd_Report.getType6());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//				cell = row_data.createCell(12);
//				cell.setCellValue(kdd_Report.getSum6());
//				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//				cell.setCellStyle(dataStyle);
//				
//			}
//			workbook.write(fileOutputStream);
//			fileOutputStream.flush();
//			fileOutputStream.close();
//			
//			
//			
//			String filePath = url + name + ".xls";
//			String fname = name + ".xls";
//			File file = new File(filePath);
//			downLoadData(response, file, fname);
//			file.delete();
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//	}
//	
//	/**
//	 * @param response
//	 * @param downlaodFile File类型的
//	 * @param fileName 下载的文件名字,比如报表.xls
//	 */
//	private static void downLoadData(HttpServletResponse response,File downlaodFile, String fileName) {
//		response.setHeader("Content-disposition", "attachment; filename="+ fileName);
//		response.setHeader("Content-Type", "application/octet-stream");
//
//		BufferedInputStream bis = null;// 读excel
//		BufferedOutputStream bos = null;// 输出
//
//		try {
//			// 读取excel文件
//			bis = new BufferedInputStream(new FileInputStream(downlaodFile));
//			// 写入response的输出流中
//			bos = new java.io.BufferedOutputStream(response.getOutputStream());
//			byte[] buff = new byte[2048];/* 设置缓存 */
//			int bytesRead;
//			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
//				bos.write(buff, 0, bytesRead);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (bis != null)
//				try {
//					bis.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			if (bos != null)
//				try {
//					bos.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//	}
//	
//	/**
//	 * @return
//	 * 获取导出的配置Map信息
//	 */
//	private static Map<Integer, String> exportMap(){
//		Map<Integer,String> ExportMap = new HashMap<Integer, String>();
//		ExportMap.put(0, "行号");
//		ExportMap.put(1, "编号");
//		ExportMap.put(2, "发件人名字");
//		ExportMap.put(3, "发件人手机");
//		ExportMap.put(4, "收件人名字");
//		ExportMap.put(5, "收件人手机");
//		ExportMap.put(6, "快递编号");
//		ExportMap.put(7, "案号");
//		ExportMap.put(8, "部门名称");
//		ExportMap.put(9, "快递单状态");
//		ExportMap.put(10, "阜内阜外");
//		ExportMap.put(12, "寄件人地址");
//		ExportMap.put(13, "收件人地址");
//		return ExportMap;
//	}
//	
//	/**
//	 * @return
//	 * 获取导出的配置Map信息
//	 */
//	private static Map<Integer, Integer> exportHeaderWidth(){
//		Map<Integer,Integer> ExportHeaderWidth = new HashMap<Integer, Integer>();
//		ExportHeaderWidth.put(0, 2000);
//		ExportHeaderWidth.put(1, 2000);
//		ExportHeaderWidth.put(2, 5000);
//		ExportHeaderWidth.put(3, 4500);
//		ExportHeaderWidth.put(4, 5000);
//		ExportHeaderWidth.put(5, 4500);
//		ExportHeaderWidth.put(6, 6000);
//		ExportHeaderWidth.put(7, 5000);
//		ExportHeaderWidth.put(8, 7000);
//		ExportHeaderWidth.put(9, 7000);
//		ExportHeaderWidth.put(10, 7000);
//		ExportHeaderWidth.put(12, 7000);
//		ExportHeaderWidth.put(13, 7000);
//		return ExportHeaderWidth;
//	}
//	
//	/**
//	 * @param header
//	 * @return
//	 * 根据要导出的列,获取表头信息
//	 */
//	private static String[] getHeader(Integer[] header){
//		Map<Integer, String> ExportMap = exportMap();
//		String[] table_header = new String[header.length];
//		try {
//			for(int i=0;i<header.length;i++){
//				table_header[i] = Type.getString(ExportMap.get(header[i]));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return table_header;
//	}
//	
//	/**
//	 * @param header
//	 * @param list
//	 * @return
//	 * 根据要导出的列,获取数据信息
//	 */
//	private static List<String[]> getData(Integer[] header,List<Kdd_Info> list){
//		List<String[]> data = new ArrayList<String[]>();
//		if(list != null && list.size() > 0){
//			for (int i = 0; i < list.size(); i++) {
//				Kdd_Info kdd_Info = list.get(i);
//				String[] tmpData = new String[header.length];
//				for(int h=0;h<header.length;h++){
//					if(header[h] == 0){//行号
//						tmpData[h] = Type.getString(i+1);
//					}
//					if(header[h] == 1){//编号
//						tmpData[h] = Type.getString(kdd_Info.getId());
//					}
//					if(header[h] == 2){//发件人名字
//						tmpData[h] = Type.getString(kdd_Info.getJijianren_xingming());
//					}
//					if(header[h] == 3){//发件人手机
//						tmpData[h] = Type.getString(kdd_Info.getJijianren_shouji());
//					}
//					if(header[h] == 4){//收件人名字
//						tmpData[h] = Type.getString(kdd_Info.getShoujianren_xingming());
//					}
//					if(header[h] == 5){//收件人手机
//						tmpData[h] = Type.getString(kdd_Info.getShoujianren_shouji());
//					}
//					if(header[h] == 6){//快递编号
//						tmpData[h] = Type.getString(kdd_Info.getTiaoxingma());
//					}
//					if(header[h] == 7){//案号
//						tmpData[h] = Type.getString(kdd_Info.getAnhao());
//					}
//					if(header[h] == 8){//部门名称
//						tmpData[h] = Type.getString(kdd_Info.getLururenDeptName());
//					}
//					if(header[h] == 9){//状态
//						int status = kdd_Info.getStatus();
//						if(status == 0){
//							tmpData[h] = Type.getString("退回");
//						}
//						if(status == 1){
//							tmpData[h] = Type.getString("已保存");
//						}
//						if(status == 2){
//							tmpData[h] = Type.getString("已提交");
//						}
//						if(status == 3){
//							tmpData[h] = Type.getString("已打印");
//						}
//						if(status == 4){
//							tmpData[h] = Type.getString("已发送");
//						}
//						if(status == 51){
//							tmpData[h] = Type.getString("本人签收");
//						}
//						if(status == 52){
//							tmpData[h] = Type.getString("拒收");
//						}
//						if(status == 53){
//							tmpData[h] = Type.getString("代收");
//						}
//						if(status == 54){
//							tmpData[h] = Type.getString("地址不详");
//						}
//						if(status == 55){
//							tmpData[h] = Type.getString("其他");
//						}
//					}
//					if(header[h] == 10){//阜内阜外
//						int status = kdd_Info.getFunei_fuwai();
//						if(status == 1){
//							tmpData[h] = Type.getString("阜内");
//						}else if(status == 2){
//							tmpData[h] = Type.getString("阜外");
//						}else{
//							tmpData[h] = Type.getString("");
//						}
//					}
//					if(header[h] == 12){//寄件人地址
//						tmpData[h] = Type.getString(kdd_Info.getJijianren_dizhi());
//					}
//					if(header[h] == 13){//收件人地址
//						tmpData[h] = Type.getString(kdd_Info.getShoujianren_dizhi());
//					}
//					
//				}
//				data.add(tmpData);
//			}
//		}
//		return data;
//	}
//
}
