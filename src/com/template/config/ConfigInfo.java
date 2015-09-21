package com.template.config;


public interface ConfigInfo {
	
	public int KKD_STATUS_HUITUI = 0;					//快递单状态-传达室,退回
	public int KKD_STATUS_BAOCUN = 1;					//快递单状态-保存
	public int KKD_STATUS_BAOCUNDAYIN = 2;				//快递单状态-未打印
	public int KKD_STATUS_YIDAYIN = 3;					//快递单状态-已打印
	public int KKD_STATUS_YIFAHUO = 4;					//快递单状态-已发货
	
	public int KKD_HHUIZHI_BENRENQIANSHOU = 51;			//本人签收
	public int KKD_HHUIZHI_JUSHOU = 52;					//拒收 
	public int KKD_HHUIZHI_DAISHOU = 53 ;				//代收
	public int KKD_HHUIZHI_DIZHIBUXIANG = 54;			//地址不详
	public int KKD_HHUIZHI_QITA = 55;					//其他（可填写）
	
	public int KKD_FNW_QT = 0;							//其他
	public int KKD_FNW_NEI = 1;							//阜内
	public int KKD_FNW_WAI = 2;							//阜外
}
