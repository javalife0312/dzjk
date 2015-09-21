Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	
	
	/***********************************************************************
	 *物资发放Form Window
	 ************************************************************************/
	var xunjianForm = new Ext.FormPanel({
        labelAlign: 'left',
        frame:true,
        title: '',
        bodyStyle:'padding:5px 5px 0',
        width: 600,
        items: [{
            layout:'column',
            items:[{
                columnWidth:1,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'ID',
                    allowBlank:false,
                    disabled : true,
                    name: 'id',
                    id: 'id',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '设备',
                    allowBlank:false,
                    disabled : true,
                    name: 'deviceName',
                    id: 'deviceName',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '巡检点',
                    disabled : true,
                    name: 'positionName',
                    id: 'positionName',
                    anchor:'95%'
                },{
                    xtype:'radiogroup',
                    fieldLabel:'设备状态',
                    name:'deviceStatus',
                    id:'deviceStatus',
                    items:[
                        {boxLabel:'正常',name:'deviceStatus',inputValue:1},
                        {boxLabel:'异常',name:'deviceStatus',inputValue:2,checked:true}
                    ]
                },{
                    xtype:'textfield',
                    fieldLabel: '处理过程',
                    disabled : false,
                    name: 'deal_note',
                    id: 'deal_note',
                    anchor:'95%'
                }]
            }]
        }]
    });
	 var xunjianWindow = new Ext.Window({
        id : 'xunjianWindow',
        title: '巡检设备回执信息',
        width: width*.30,
        height: height*.3,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: xunjianForm,
        buttons: [ts,{
            text: '回执',
            icon : "img/button/add.gif",
            handler: function(){
            	Ext.Ajax.request({
					url: 'dZJK_DeviceManageAction!complateXJ',
				   	success: function(response){
				   		var responseArray = Ext.util.JSON.decode(response.responseText);
						if (responseArray.success == 'true') {
				   			store.proxy = new Ext.data.HttpProxy({
								url: 'dZJK_DeviceManageAction!listDeviceTaskAll',
					            method: 'POST'
							});
							store.load({
								params : {
									start : 0,
									limit : page
								}
							});
							xunjianWindow.hide();
							Ext.MessageBox.alert('提示','设备巡检确定完成');
						}
				   	},
				   	params: { 
				   		ids:Ext.getCmp('id').getValue(),
				   		deviceStatus:Ext.getCmp('deviceStatus').getValue().inputValue,
				   		deal_note:Ext.getCmp('deal_note').getValue()
				   	}
            	});
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	xunjianWindow.hide();
            }
        },ts]
    });
  	/***********************************************************************
	*Grid相关
	************************************************************************/
    var store = new Ext.data.JsonStore({
        root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        remoteSort: false,
        fields: [
            {name: 'id', type: 'int'},
            {name: 'taskDate', type: 'string'},
            {name: 'deviceId', type: 'int'},
            {name: 'status', type: 'int'},
            {name: 'weijianId', type: 'int'},
            {name: 'tipDays', type: 'int'},
            {name: 'deviceName', type: 'string'},
            {name: 'weijianName', type: 'string'},
            {name: 'updateTime', type: 'string'},
            {name: 'positionId', type: 'string'},
            {name: 'positionName', type: 'string'},
            {name: 'deviceStatus', type: 'string'},
            {name: 'deal_note', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_DeviceManageAction!listDeviceTaskAll',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'设备分配的任务列表',
        store: store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : sm,
        columns:[sm,{
            id: 'id',
            header: "ID",
            dataIndex: 'id',
            sortable: true
        },{
            header: "任务日期",
            dataIndex: 'taskDate',
            sortable: true
        },{
            header: "设备ID",
            dataIndex: 'deviceId',
            sortable: true,
            hidden:true
        },{
        	header: "巡检点名字",
        	dataIndex: 'positionName',
        	sortable: true
        },{
        	header: "设备",
        	dataIndex: 'deviceName',
        	sortable: true
        },{
        	header: "巡检点ID",
        	dataIndex: 'positionId',
        	sortable: true,
        	hidden:true
        },{
            header: "任务人ID",
            dataIndex: 'weijianId',
            sortable: true,
            hidden:true
        },{
        	header: "任务人",
        	dataIndex: 'weijianName',
        	sortable: true
        },{
            header: "周期（天）",
            dataIndex: 'tipDays',
            sortable: true
        },{
            header: "状态",
            dataIndex: 'status',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "未巡检";
            	}
            	if(value=='1') {
            		return "已巡检";
            	}
            }
        },{
            header: "最后修改时间",
            width:200,
            dataIndex: 'updateTime',
            sortable: true
        },{
            header: "设备状态",
            dataIndex: 'deviceStatus',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "未处理";
            	}
            	if(value=='1') {
            		return "正常";
            	}
            	if(value=='2') {
            		return "异常";
            	}
            }
        },{
            header: "处理过程",
            width:200,
            dataIndex: 'deal_note',
            sortable: true
        }],
		tbar : [ts,{
            text: '回执结果',
            tooltip: '回执结果',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	var ids = "-1";
            	for(var i=0;i<records.length;i++){
            		ids += ","+records[i].get('id');
            	}
            	if(records.length!=1){
            		Ext.MessageBox.alert('提示','回执时只能单条记录处理');
            		return;
            	}
            	xunjianForm.getForm().reset();
				xunjianForm.form.loadRecord(records[0]);
            	xunjianWindow.show();
			}
        },ts],
        bbar: new Ext.PagingToolbar({
            store: store,
            displayInfo: true,
            displayMsg: '第 {0} - {1} 条, 总共 {2} 条',
            emptyMsg: "没有数据"
        })
    });
    grid.render('center');
    /***********************************************************************
	*Grid相关
	************************************************************************
	
	/***********************************************************************
	*布局相关
	************************************************************************/
	new Ext.Viewport({
		layout : 'border',
		items : [{
			region : 'center',
			items : grid
		}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
