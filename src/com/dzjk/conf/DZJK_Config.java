package com.dzjk.conf;

/**
 * @author Administrator
 * 类型对应关系
 *
 */
public interface DZJK_Config {
	public static Integer dzjk_WUZI = 1;
	public static Integer dzjk_BAOXIU = 2;
	
	//User Type
	public static Integer user_type_SUPER = 0; //超级管理员
	public static Integer user_type_WUZI_审批 = 1; //物资审批的管理
	public static Integer user_type_BAOXIU_审批 = 2; //报修_受理_任务分配者
	public static Integer user_type_BAOXIU_维护 =3; //报修_受理_维修者
	public static Integer user_type_普通 =100; //普通用户
	
	
	//消息信息
	public static Integer msg_type_BaoXiu = 1;
	public static String msg_type_BaoXiu_Desc = "报修申请";
	public static Integer msg_status_BaoXiu_Create = 0;
	
	public static Integer msg_type_WuZiShenQing = 2;
	public static String msg_type_WuZiShenQing_Desc = "资产申请";
	public static Integer msg_status_WuZiShenQing_Create = 0;
}
