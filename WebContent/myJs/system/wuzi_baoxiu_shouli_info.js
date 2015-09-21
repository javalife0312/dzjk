Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	var qx_nodeId = '0';
	var userid = '0';
	var wuziTypeId = '-1';
	
	/***********************************************************************
	 *维修记录填写
	 ************************************************************************/
	var weixiuJiLuForm = new Ext.FormPanel({
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
                    fieldLabel: '记录Id',
                    allowBlank:false,
                    disabled : true,
                    name: 'id',
                    id: 'id',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '申请人',
                    allowBlank:false,
                    disabled : true,
                    name: 'persion_id',
                    id: 'persion_id',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '物资类别名称',
                    disabled : true,
                    name: 'wuziTypeName',
                    id: 'wuziTypeName',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '物资名称',
                    disabled : true,
                    name: 'wuziName',
                    id: 'wuziName',
                    anchor:'95%'
                },{
                    xtype:'textarea',
                    fieldLabel: '维修记录',
                    disabled : false,
                    name: 'deal_note',
                    id: 'deal_note',
                    allowBlank:false,
                    blankText:'维修过程必须填写',
                    anchor:'95%'
                }]
            }]
        }]
    });
	 var weixiuJiLuWindow = new Ext.Window({
	        id : 'weixiuJiLuWindow',
	        title: '故障维修过程记录',
	        width: width*.4,
	        height: height*.5,
	        layout: 'fit',
	        plain:true,
	        bodyStyle:'padding:5px;',
	        buttonAlign:'right',
	        closable:false,
	        items: weixiuJiLuForm,
	        buttons: [ts,{
	            text: '确认',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	var shenqing_records = grid.getSelectionModel().getSelections();
	            	if(shenqing_records.length!=1){
	            		Ext.MessageBox.alert('提示','单次只能填写一条维修记录');
	            		return;
	            	}else{
	                	if(weixiuJiLuForm.form.isValid()){
	                		var records = grid.getSelectionModel().getSelections();
	                		var baoxiuId = records[0].get('id');
	    					Ext.Ajax.request({
	    						url: 'dZJK_PersionWuZiAction!wuziBaoWeiXiu',
	    					   	success: function(response){
	    					   		var responseArray = Ext.util.JSON.decode(response.responseText);
	    							if (responseArray.success == 'true') {
							   			store.proxy = new Ext.data.HttpProxy({
											url: 'dZJK_PersionWuZiAction!listBaoXiuAll',
								            method: 'POST'
										});
										store.load({
											params : {
												start : 0,
												limit : page
											}
										});
										store.reload();
										weixiuJiLuWindow.hide();
										Ext.MessageBox.alert('提示','维修记录填写成功');
	    							}
	    					   	},
	    					   	params: { 
	    					   		baoxiuId:baoxiuId,
	    					   		deal_note:Ext.getCmp('deal_note').getValue()
	    					   	}
	    					});
	            		
	                	}
	    			
	            	}
	            }
	        },ts,{
	            text: '取消',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	weixiuJiLuWindow.hide();
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
             url: 'dZJK_PersionWuZiAction!listBaoXiuAll',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'受理报修记录明细',
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
		tbar : [
		        /**ts,{
            text: '驳回',
            tooltip: '审批人驳回',
            //hidden : false,
            //disabled:true,
            id : 'bt_sprbohui',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要驳回的记录');
            		return;
            	}else {
            		Ext.MessageBox.confirm('请确认', '确认驳回申请，同时驳回子集',function(btn){
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
												url: 'dZJK_PersionWuZiAction!listBaoXiuAll',
									            method: 'POST'
											});
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											Ext.MessageBox.alert('提示','物资申请驳回成功');
										}
									}
							   	},
							   	params: { 
							   		ids:ids,
							   		status : -2
							   	}
							});
            			}
            		});
				}
			}
        },ts,{
            text: '受理',
            tooltip: '资产报修受理',
            id : 'bt_sprtongguo',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要受理的记录');
            	}else if(records.length>1){
            		Ext.MessageBox.alert('提示','仅支持单条记录处理');
            	}else if(records[0].get('status')!=0){
            		Ext.MessageBox.alert('提示','只有申请状态的记录可以处理');
            	}else{
            		org_usr_grid.proxy = new Ext.data.HttpProxy({
            			url: 'dZJK_WuZiAction!listBaoXiuWeiHuAll',
                        method: 'POST'
            		});
            		org_usr_store.load({
            			params : {
            				start : 0,
            				limit : page
            			}
            		});
            		weihuPersonWindow.show();
            	}
			}
        },*/ts,{
            text: '维修记录',
            tooltip: '维修记录填写',
            id : 'bt_sprzichanfafang',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要填写的记录');
            	}else if(records.length>1){
            		Ext.MessageBox.alert('提示','仅支持单维修记录填写');
            	}else if(records[0].get('status')!=2){
            		Ext.MessageBox.alert('提示','只有已受理状态的记录可以处理');
            	}else{
            		weixiuJiLuForm.form.reset();
            		weixiuJiLuForm.form.loadRecord(records[0]);
            		weixiuJiLuWindow.show();
            	}
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

