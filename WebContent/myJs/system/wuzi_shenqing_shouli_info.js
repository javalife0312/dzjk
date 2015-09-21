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
	        width: width*.30,
	        height: 150,
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
	            		if(record.get('status')==4){
	            			ids += ","+record.get('id');
	            		}
	            	}
	            	Ext.Ajax.request({
						url: 'dZJK_PersionWuZiAction!wuziShenqingManyidu',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_PersionWuZiAction!listShenqingAll',
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
								Ext.MessageBox.alert('提示','物资申请发放成功');
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
	 *物资发放Form Window
	 ************************************************************************/
	var wuziFaFangForm = new Ext.FormPanel({
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
                    xtype:'textfield',
                    fieldLabel: '物资有效期',
                    name: 'youxiaoqi',
                    id: 'youxiaoqi',
                    disabled : true,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '数量',
                    disabled : true,
                    name: 'tcount',
                    id: 'tcount',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '资产编号',
                    disabled : false,
                    name: 'wuziCode',
                    id: 'wuziCode',
                    allowBlank:false,
                    blankText:'资产编号必须填写',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '申请类型',
                    disabled : false,
                    name: 'applytype',
                    id: 'applytype',
                    allowBlank:false,
                    blankText:'申请类型',
                    hidden:true,
                    anchor:'95%'
                },{
                    xtype:'radiogroup',
                    fieldLabel:'更换/退回处理',
                    name:'dealtype',
                    id:'dealtype',
                    items:[
                        {boxLabel:'回库重用',name:'dealtype',inputValue:1,checked:true},
                        {boxLabel:'直接报废',name:'dealtype',inputValue:2}
                    ]
                }]
            }]
        }]
    });
	 var wuziFaFangWindow = new Ext.Window({
	        id : 'wuziFaFangWindow',
	        title: '物资发放明细对照',
	        width: width*.30,
	        height: height*.45,
	        layout: 'fit',
	        plain:true,
	        bodyStyle:'padding:5px;',
	        buttonAlign:'right',
	        closable:false,
	        items: wuziFaFangForm,
	        buttons: [ts,{
	            text: '处理',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	var shenqing_records = grid.getSelectionModel().getSelections();
	            	if(shenqing_records.length!=1){
	            		Ext.MessageBox.alert('提示','审批的时候只能并且只能分配一条资产');
	            		return;
	            	}else{
	                	if(wuziFaFangForm.form.isValid()){
	                		var records = grid.getSelectionModel().getSelections();
	                		var shenqingId = records[0].get('id');
	    					Ext.Ajax.request({
	    						url: 'dZJK_PersionWuZiAction!wuziShenqingFaFang',
	    					   	success: function(response){
	    					   		var responseArray = Ext.util.JSON.decode(response.responseText);
	    							if (responseArray.success == 'true') {
							   			store.proxy = new Ext.data.HttpProxy({
											url: 'dZJK_PersionWuZiAction!listShenqingAll',
								            method: 'POST'
										});
										store.load({
											params : {
												start : 0,
												limit : page
											}
										});
										store.reload();
										wuziFaFangWindow.hide();
										Ext.MessageBox.alert('提示','物资申请发放成功');
										
										if(responseArray.tipsNum != 0){
											Ext.MessageBox.alert('提示','该类物资仅剩余'+responseArray.tipsNum+"!请注意采购");
										}
	    							}
	    					   	},
	    					   	params: { 
	    					   		shenqingId:shenqingId,
	    					   		wuziCode:Ext.getCmp('wuziCode').getValue(),
	    					   		dealtype:Ext.getCmp('dealtype').getValue().inputValue,
	    					   		applytype:Ext.getCmp('applytype').getValue()
	    					   	}
	    					});
	            		
	                	}
	    			
	            	}
	            }
	        },ts,{
	            text: '取消',
	            icon : "img/button/add.gif",
	            handler: function(){
	            	wuziFaFangWindow.hide();
	            }
	        },ts]
	    });

	
	/***********************************************************************
	 *物资类型数
	 ************************************************************************/
	var root_wuzitype = new Ext.tree.AsyncTreeNode({
		text : '物资类别',
		draggable : false,
		id : '0',
		icon : "img/tree/system.png"
	});
	var tree_wuzitype = new Ext.tree.TreePanel({
		renderTo : '',
		autoScroll : true,
		root : root_wuzitype,
		animate : true,
		enableDD : false,
		checkModel : 'cascade',
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!wuziTypeCheckBoxTree?id='+nodeId,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					}
				})
	});
	tree_wuzitype.on('beforeload',function(node){     
		nodeId = node.id;
		tree_wuzitype.loader.dataUrl = 'treeAction!wuziTypeCheckBoxTree?id='+nodeId;      
	});  
	
    var wuzitypeWindow = new Ext.Window({
        id : 'wuzitypeWindow',
        title: '资产类别目录',
        width: 300,
        height:450,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: tree_wuzitype,
        buttons: [{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	var nodeIds = '-1';
            	var checkedNodes = tree_wuzitype.getChecked();
            	if(checkedNodes.length == 0) {
            		Ext.MessageBox.alert('提示','请选择物资');
            		return;
            	}else{
	            	for(var i=0;i<checkedNodes.length;i++) {
	            		if(checkedNodes[i].leaf){
	            			nodeIds += ',' + checkedNodes[i].id;            			            			
	            		}
	            	}
	            	Ext.Ajax.request({
						url: 'dZJK_PersionWuZiAction!wuziShenqing',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								wuzitypeWindow.hide();
								store.reload();
								Ext.MessageBox.alert('提示','资产申请成功');
							}
					   	},
					   	params: { 
					   		ids:nodeIds
					   	}
					});
            	}
            }
        },{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	wuzitypeWindow.hide();
            }
        }]
    });
    
    
    
	
    /***********************************************************************
	*自定义函数相关
	************************************************************************/
	function reloadAddNode(node){
		if(node == '0'){
			tree.getRootNode().reload();
		}else{
			tree.getNodeById(node).parentNode.reload();
		}
	}
	
	function reloadUpdateNode(node){
		tree.getRootNode().reload();
	}
    
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
            {name: 'persion_id', type: 'string'},
            {name: 'persion_name', type: 'string'},
            {name: 'wuziTypeId', type: 'string'},
            {name: 'wuziTypeName', type: 'string'},
            {name: 'wuziId', type: 'string'},
            {name: 'wuziName', type: 'string'},
            {name: 'wuziDescr', type: 'string'},
            {name: 'shenqingliyou', type: 'string'},
            {name: 'youxiaoqi', type: 'string'},
            {name: 'manyidu', type: 'string'},
            {name: 'manyidu_note', type: 'string'},
            {name: 'tcount', type: 'string'},
            {name: 'status', type: 'string'},
            {name: 'persion_phone', type: 'string'},
            {name: 'applytype', type: 'string'},
            {name: 'bghid', type: 'string'},
            {name: 'wuziCode', type: 'string'},
            {name: 'persion_orgName', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_PersionWuZiAction!listShenqingAll',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'个人资产申请列表',
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
            header: "申请类型",
            dataIndex: 'applytype',
            sortable: true,
            renderer : function(value) {
            	if(value=='1') {
            		return "资产申请";
            	}
            	if(value=='2') {
            		return "更换申请";
            	}
            	if(value=='3') {
            		return "退还申请";
            	}
            }
        },{
            header: "申请人",
            dataIndex: 'persion_id',
            sortable: true,
            hidden: true
        },{
        	header: "申请人名字",
        	dataIndex: 'persion_name',
        	sortable: true
        },{
        	header: "申请人组织结构",
        	dataIndex: 'persion_orgName',
        	sortable: true,
        	hidden:true
        },{
        	header: "申请人联系电话",
        	dataIndex: 'persion_phone',
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
            header: "申请理由",
            dataIndex: 'shenqingliyou',
            sortable: true
        },{
            header: "申请数量",
            dataIndex: 'tcount',
            sortable: true
        },{
            header: "物资有效期",
            dataIndex: 'youxiaoqi',
            sortable: true
        },{
            header: "状态",
            dataIndex: 'status',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "提交申请,等待接受";
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
            		return "申请已接受,等待分配资产";
            	}
            	if(value=='3') {
            		return "资产已分配,等待发放";
            	}
            	if(value=='4') {
            		return "资产已发放,等待评论";
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
        },{
            header: "被处理的资产Id",
            dataIndex: 'bghid',
            sortable: true,
            hidden:true
        },{
        	header: "被处理的资产Code",
        	dataIndex: 'wuziCode',
        	sortable: true,
        	hidden:true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [/**ts,{
            text: '撤销',
            tooltip: '申请撤销',
            //hidden : false,
            //disabled:true,
            //id : 'bt_update',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要修改的记录');
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
            				if(ids=='-1'){
            					Ext.MessageBox.alert('提示','您没有选中可撤销的记录');
                        		return;
            				}
            				Ext.Ajax.request({
								url: 'dZJK_PersionWuZiAction!wuziShenqingCheXiao',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_PersionWuZiAction!listShenqingAll',
									            method: 'POST'
											});
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											Ext.MessageBox.alert('提示','物资申请撤销成功');
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
        },*/ts,{
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
            				if(ids=='-1'){
            					Ext.MessageBox.alert('提示','您没有选中可驳回的记录');
                        		return;
            				}
            				Ext.Ajax.request({
								url: 'dZJK_PersionWuZiAction!wuziShenqingCheXiao',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_PersionWuZiAction!listShenqingAll',
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
            text: '分配资产',
            tooltip: '分配资产',
            id : 'bt_sprtongguo',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要分配资产的记录');
            	}else if(records.length>1){
            		Ext.MessageBox.alert('提示','仅支持单条记录处理');
            	}else if(records[0].get('status')!=2){
            		Ext.MessageBox.alert('提示','只有申请已受理状态的记录可以处理');
            	}else{
            		if(records[0].get('applytype')!=3){
            			wuziTypeId = records[0].get('wuziTypeId');
                		wuzi_store.proxy = new Ext.data.HttpProxy({
                			url: 'dZJK_WuZiAction!listAll?fid='+wuziTypeId,
                            method: 'POST'
                		});
                		wuzi_store.load({
                			params : {
                				start : 0,
                				limit : page
                			}
                		});
                		wuziListWindow.show();
            		}else{
            			var records = grid.getSelectionModel().getSelections();
                    	if(records.length != 1) {
                    		Ext.MessageBox.alert('提示','请选择要审批,仅支持单条申请处理');
                    		return;
                    	}
            			Ext.Ajax.request({
							url: 'dZJK_PersionWuZiAction!wuziShenqingShenPi',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
									if (responseArray.msg == 'true') {
							   			store.proxy = new Ext.data.HttpProxy({
											url: 'dZJK_PersionWuZiAction!listShenqingAll',
								            method: 'POST'
										});
										store.load({
											params : {
												start : 0,
												limit : page
											}
										});
										store.reload();
										Ext.MessageBox.alert('提示','物资退还申请审批成功');
									}
								}
						   	},
						   	params: { 
						   		shenqingId:records[0].data.id,
						   		wuziId : records[0].data.wuziId
						   	}
						});
            		}
            	}
			}
        },ts,{
            text: '发放/更换/回收',
            tooltip: '资产发放',
            id : 'bt_sprzichanfafang',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要发放资产的记录');
            	}else if(records.length>1){
            		Ext.MessageBox.alert('提示','仅支持单条记录处理');
            	}else if(records[0].get('status')!=3){
            		Ext.MessageBox.alert('提示','只有已分配状态的记录可以处理');
            	}else{
            		wuziFaFangForm.form.reset();
            		wuziFaFangForm.form.loadRecord(records[0]);
            		wuziFaFangWindow.show();
            	}
			}
        },/**ts,{
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
        },*/ts,{
            text: '打印',
            tooltip: '打印申请单',
            id : '打印',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length!=1) {
            		Ext.MessageBox.alert('提示','每次可以打印一张单子');
            	}else{
            		var apply_person = records[0].get("persion_name");
                    var apply_wuziName = records[0].get("wuziName");
                    var apply_org_name = records[0].get("persion_orgName");
            		var showUrl = "../../myWeb/print/printType1_view.jsp?apply_person="+apply_person+"&apply_wuziName="+apply_wuziName + "&apply_org_name="+apply_org_name;
            		window.open(showUrl,'newwindow','height='+height+',width='+width+',top=0,left=0,titlebar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,directories=yes,status=no');
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
	*Window,Form相关
	************************************************************************/
	var wuzi_store = new Ext.data.JsonStore({
        root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        remoteSort: false,
        fields: [
            {name: 'id', type: 'int'},
            {name: 'name', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'wuziTypeId', type: 'string'},
            {name: 'tcount', type: 'string'},
            {name: 'youxiaoqi', type: 'string'},
            {name: 'tipDays', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_WuZiAction!listAll?fid=-1',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
	wuzi_store.load({params:{start:0, limit:page}});
    
    var wuzi_sm = new Ext.grid.CheckboxSelectionModel();
    var wuzi_grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'资产明细列表',
        store: wuzi_store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : wuzi_sm,
        columns:[wuzi_sm,{
            id: 'id',
            header: "ID",
            dataIndex: 'id',
            sortable: true
        },{
            header: "资产类别",
            dataIndex: 'wuziTypeId',
            sortable: true
        },{
            header: "资产名字",
            dataIndex: 'name',
            sortable: true
        },{
            header: "资产描述",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "剩余数量",
            dataIndex: 'tcount',
            sortable: true
        },{
            header: "有效期",
            dataIndex: 'youxiaoqi',
            sortable: true
        },{
            header: "过期前提醒天",
            dataIndex: 'tipDays',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
        bbar: new Ext.PagingToolbar({
            store: wuzi_store,
            displayInfo: true,
            displayMsg: '第 {0} - {1} 条, 总共 {2} 条',
            emptyMsg: "没有数据"
        })
    });
    var wuziListWindow = new Ext.Window({
        id : 'wuziListWindow',
        title: '资产明细列表',
        width: width*.55,
        height: height*.45,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: wuzi_grid,
        buttons: [ts,{
            text: '审批',
            icon : "img/button/add.gif",
            handler: function(){
            	var shenqing_records = grid.getSelectionModel().getSelections();
            	var shenpi_records = wuzi_grid.getSelectionModel().getSelections();
            	if(shenpi_records.length!=1){
            		Ext.MessageBox.alert('提示','审批的时候只能并且只能分配一条资产');
            		return;
            	}else{
            		var shenqingId = shenqing_records[0].get('id');
            		var wuziId = shenpi_records[0].get('id');
    				Ext.Ajax.request({
						url: 'dZJK_PersionWuZiAction!wuziShenqingShenPi',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								if (responseArray.msg == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'dZJK_PersionWuZiAction!listShenqingAll',
							            method: 'POST'
									});
									store.load({
										params : {
											start : 0,
											limit : page
										}
									});
									store.reload();
									wuziListWindow.hide();
									Ext.MessageBox.alert('提示','申请审批通过');
								}
							}
					   	},
					   	params: { 
					   		shenqingId:shenqingId,
					   		wuziId : wuziId
					   	}
					});
            	}
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	wuziListWindow.hide();
            }
        },ts]
    });
    /***********************************************************************
	*Window,Form相关
	************************************************************************/
    
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
    
    /***********************************************************************
     *预先加载
     ************************************************************************/
    Ext.Ajax.request({
    	url: 'dZJK_PersionWuZiAction!shnqingUserInfo',
    	success: function(response){
    		var responseArray = Ext.util.JSON.decode(response.responseText);
    		if (responseArray.success == 'true') {
    			//utype 0超级管理员 1申请审批人  其他在此页面都是普通用户
    			var utype = responseArray.utype;
    			if(utype!=0 && utype!=1){
    				Ext.getCmp('bt_sprbohui').disable();
    				Ext.getCmp('bt_sprtongguo').disable();
    				Ext.getCmp('bt_sprzichanfafang').disable();
    			}
    		}
    	}
    });
    /***********************************************************************
     *预先加载
     ************************************************************************/
});

