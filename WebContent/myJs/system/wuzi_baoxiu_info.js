Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	
	/***********************************************************************
	 *满意度Form Window
	 ************************************************************************/
	var manyiduForm = new Ext.FormPanel({
        labelAlign: 'left',
        frame:true,
        title: '',
        bodyStyle:'padding:5px 5px 0',
        items: [{
            layout:'column',
            items:[{
                columnWidth:1,
                layout: 'form',
                items: [{
                    xtype:'radiogroup',
                    fieldLabel:'是否满意',
                    name:'manyidu',
                    id:'manyidu',
                    items:[
                        {boxLabel:'非常满意',name:'manyidu',inputValue:1,checked:true},
                        {boxLabel:'基本满意',name:'manyidu',inputValue:2},
                        {boxLabel:'非常不满意',name:'manyidu',inputValue:3}
                    ]
                },{
                    xtype:'textfield',
                    fieldLabel: '满意度备注',
                    allowBlank:true,
                    disabled : false,
                    name: 'manyidu_note',
                    id: 'manyidu_note',
                    anchor:'95%'
                }]
            }]
        }]
    });
	 var manyiduWindow = new Ext.Window({
	        id : 'manyiduWindow',
	        title: '满意度调查信息',
	        width: width*.4,
	        height: height*.3,
	        layout: 'fit',
	        plain:true,
	        bodyStyle:'padding:5px;',
	        buttonAlign:'right',
	        closable:false,
	        items: manyiduForm,
	        buttons: [ts,{
	            text: '提交',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	var ids = "-1";
	            	var shenqing_records = grid.getSelectionModel().getSelections();
	            	for(var i=0;i<shenqing_records.length;i++){
	            		var record = shenqing_records[i];
	            		if(record.get('status')==3){
	            			ids += ","+record.get('id');
	            		}
	            	}
	            	Ext.Ajax.request({
						url: 'dZJK_PersionWuZiAction!wuziBaoXiuManyidu',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_PersionWuZiAction!listBaoXiuAll_Person',
						            method: 'POST'
								});
								store.load({
									params : {
										start : 0,
										limit : page
									}
								});
								store.reload();
								manyiduWindow.hide();
								Ext.MessageBox.alert('提示','感谢您的评价，我们保证提供一如既往优秀的服务');
							}
					   	},
					   	params: { 
					   		ids:ids,
					   		manyidu:Ext.getCmp('manyidu').getValue().inputValue,
					   		manyidu_note:Ext.getCmp('manyidu_note').getValue()
					   	}
					});
	            }
	        },ts,{
	            text: '取消',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	manyiduWindow.hide();
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
            {name: 'persion_zichan_id', type: 'int'},
            {name: 'persion_id', type: 'int'},
            {name: 'persion_name', type: 'string'},
            {name: 'persion_gongwei', type: 'string'},

            {name: 'wuziId', type: 'string'},
            {name: 'wuziName', type: 'string'},
            {name: 'wuziDescr', type: 'string'},
            {name: 'youxiaoqi', type: 'string'},
            {name: 'type', type: 'string'},
            
            {name: 'wuziTypeId', type: 'string'},
            {name: 'wuziTypeName', type: 'string'},
            
            {name: 'tcount', type: 'string'},
            {name: 'baoyou_yuanyin', type: 'string'},
            {name: 'status', type: 'string'},
            
            {name: 'manyidu', type: 'string'},
            {name: 'manyidu_note', type: 'string'},
            
            {name: 'deal_note', type: 'string'},
            {name: 'deal_personId', type: 'string'},
            {name: 'deal_personName', type: 'string'},
            {name: 'date_baoxiao', type: 'string'},
            {name: 'date_shouli', type: 'string'},
            {name: 'date_wancheng', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_PersionWuZiAction!listBaoXiuAll_Person',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'报修记录明细',
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
            header: "报修人资产ID",
            dataIndex: 'persion_zichan_id',
            sortable: true,
            hidden:true
        },{
            header: "报修人ID",
            dataIndex: 'persion_id',
            sortable: true,
            hidden:true
        },{
        	header: "报修人名字",
        	dataIndex: 'persion_name',
        	sortable: true
        },{
        	header: "报修人工位",
        	dataIndex: 'persion_gongwei',
        	sortable: true
        },{
            header: "物资类别ID",
            dataIndex: 'wuziTypeId',
            sortable: true,
            hidden: true
        },{
            header: "物资类别名称",
            dataIndex: 'wuziTypeName',
            sortable: true
        },{
            header: "物资ID",
            dataIndex: 'wuziId',
            sortable: true,
            hidden:true
        },{
            header: "物资名称",
            dataIndex: 'wuziName',
            sortable: true
        },{
            header: "物资描述",
            dataIndex: 'wuziDescr',
            sortable: true
        },{
            header: "物资有效期",
            dataIndex: 'youxiaoqi',
            sortable: true
        },{
            header: "物资类型",
            dataIndex: 'type',
            sortable: true,
            hidden:true
        },{
            header: "报修数量",
            dataIndex: 'tcount',
            sortable: true
        },{
            header: "申请理由",
            dataIndex: 'baoyou_yuanyin',
            sortable: true
        },{
            header: "处理结论",
            dataIndex: 'deal_note',
            sortable: true
        },{
            header: "处理人ID",
            dataIndex: 'deal_personId',
            sortable: true,
            hidden:true
        },{
        	header: "处理人名字",
        	dataIndex: 'deal_personName',
        	sortable: true
        },{
            header: "报修时间",
            dataIndex: 'date_baoxiao',
            sortable: true
        },{
            header: "受理时间",
            dataIndex: 'date_shouli',
            sortable: true
        },{
            header: "处理完成时间",
            dataIndex: 'date_wancheng',
            sortable: true
        },{
            header: "状态",
            dataIndex: 'status',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "提交报修申请";
            	}
            	if(value=='-1') {
            		return "申请人撤销";
            	}
            	if(value=='-2') {
            		return "审批人驳回";
            	}
            	if(value=='1') {
            		return "已经完成";
            	}
            	if(value=='2') {
            		return "已受理";
            	}
            	if(value=='3') {
            		return "已经维修-等待评论";
            	}
            }
        },{
            header: "满意度",
            dataIndex: 'manyidu',
            sortable: true,
            renderer : function(value) {
            	if(value=='1') {
            		return "非常满意";
            	}
            	if(value=='2') {
            		return "基本满意";
            	}
            	if(value=='3') {
            		return "非常不满意";
            	}
            }
        },{
            header: "满意度备注",
            dataIndex: 'manyidu_note',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '撤销',
            tooltip: '撤销报修',
            //hidden : false,
            //disabled:true,
            //id : 'bt_update',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要撤销的记录');
            		return;
            	}else {
            		Ext.MessageBox.confirm('请确认', '确认撤销申请，同时撤销子集',function(btn){
            			if(btn == 'yes'){
            				var ids = '-1';
            				for(var i=0;i<records.length;i++){
            					if(records[i].get('status')=='0'){
            						ids += ',' + records[i].get('id');            						
            					}
            				}
            				Ext.Ajax.request({
								url: 'dZJK_PersionWuZiAction!wuziBaoXiuCheXiao',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_PersionWuZiAction!listBaoXiuAll_Person',
									            method: 'POST'
											});
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											Ext.MessageBox.alert('提示','物资报修撤销成功');
										}
									}
							   	},
							   	params: { 
							   		ids:ids,
							   		status : -1
							   	}
							});
            			}
            		});
				}
			}
        },ts,{
            text: '满意度反馈',
            tooltip: '满意度反馈',
            id : 'bt_sprmanyidu',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要反馈满意度的记录');
            	}else{
            		manyiduForm.form.reset();
            		manyiduWindow.show();
            	}
			}
        }],
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

