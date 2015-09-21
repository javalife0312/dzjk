Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	//0 islead  -1 notleaf
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	
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
	*Window,Form相关
	************************************************************************/
	var addOrUpdateForm = new Ext.FormPanel({
        labelAlign: 'left',
        frame:true,
        title: '',
        bodyStyle:'padding:5px 5px 0',
        width: 600,
        items: [{
            layout:'column',
            items:[{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '设备名称',
                    allowBlank:false,
                    blankText:'设备类称不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '父级ID',
                    allowBlank:false,
                    disabled : true,
                    blankText:'父级ID不能为空',
                    name: 'fid',
                    id: 'fid',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '定检周期（天）',
                    allowBlank:false,
                    blankText:'定检周期不能为空',
                    name: 'tipDays',
                    id: 'tipDays',
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '设备描述',
                    allowBlank:false,
                    blankText:'设备描述不能为空',
                    name: 'descr',
                    id: 'descr',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'ID',
                    name: 'id',
                    id: 'id',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '设备',
        width: 600,
        height:170,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: addOrUpdateForm,
        buttons: [ts,{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	if(addOrUpdateForm.form.isValid()){
					Ext.Ajax.request({
						url: 'dZJK_DeviceManageAction!saveOrUpdate',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_DeviceManageAction!listAll',
						            method: 'POST'
								});
								store.baseParams = {
									fid : nodeId
								};
								store.load({
									params : {
										start : 0,
										limit : page
									}
								});
								store.reload();
								addOrUpdateWindow.hide();
								reloadUpdateNode();
								Ext.MessageBox.alert('提示','添加设备成功');
							}
					   	},
					   	params: { 
					   		id:Ext.getCmp('id').getValue(),
					   		fid:Ext.getCmp('fid').getValue(),
					   		name:Ext.getCmp('name').getValue(),
					   		descr:Ext.getCmp('descr').getValue(),
					   		tipDays:Ext.getCmp('tipDays').getValue()
					   	}
					});
        		
            	}
			}
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	addOrUpdateForm.form.reset();
            	addOrUpdateWindow.hide();
            }
        },ts]
    });
    
  //---win start
	var org_usr_store = new Ext.data.JsonStore({
		root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        remoteSort: false,
        fields: [
            {name: 'id', type: 'int'},
            {name: 'username', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'orgId', type: 'int'},
            {name: 'ip', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_PersionWuZiAction!listBaoXiuWeiHuAll',
             method: 'POST'
         })
    });
	org_usr_store.load({params:{start:0, limit:page}});
    
    var org_usr_sm = new Ext.grid.CheckboxSelectionModel({singleSelect: true});
    var org_usr_grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        store: org_usr_store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : org_usr_sm,
        columns:[org_usr_sm,{
            id: 'id',
            header: "ID",
            dataIndex: 'id',
            sortable: true
        },{
            header: "用户名",
            dataIndex: 'username',
            sortable: true
        },{
            header: "描述信息",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "所属组织ID",
            dataIndex: 'orgId',
            sortable: true
        },{
            header: "用户IP",
            dataIndex: 'ip',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
        bbar: new Ext.PagingToolbar({
            store: org_usr_store,
            displayInfo: true,
            displayMsg: '第 {0} - {1} 条, 总共 {2} 条',
            emptyMsg: "没有数据"
        })
    });
    var weihuPersonWindow = new Ext.Window({
        id : 'weihuPersonWindow',
        title: '维检人员名单',
        width: width*.55,
        height: height*.45,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: org_usr_grid,
        buttons: [ts,{
            text: '任务分配',
            icon : "img/button/add.gif",
            handler: function(){
            	var device_records = grid.getSelectionModel().getSelections();
            	var weijian_records = org_usr_grid.getSelectionModel().getSelections();
            	if(weijian_records.length!=1){
            		Ext.MessageBox.alert('提示','只能分配给一个维检人员');
            		return;
            	}else{
            		var deviceId = device_records[0].get('id');
            		var tipDays = device_records[0].get('tipDays');
            		var weijianId = weijian_records[0].get('id');
            		
            		var deviceName = device_records[0].get('name');
            		var weijianName = weijian_records[0].get('username');
    				Ext.Ajax.request({
						url: 'dZJK_DeviceManageAction!deviceTaskHandle',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								if (responseArray.msg == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'dZJK_DeviceManageAction!listAll',
							            method: 'POST'
									});
						   			store.baseParams = {
											fid : nodeId
										};
									store.load({
										params : {
											start : 0,
											limit : page
										}
									});
									store.reload();
									weihuPersonWindow.hide();
									Ext.MessageBox.alert('提示','申请审批通过');
								}
							}
					   	},
					   	params: { 
					   		weijianId:weijianId,
					   		deviceId : deviceId,
					   		tipDays:tipDays,
					   		deviceName:deviceName,
					   		weijianName:weijianName
					   	}
					});
            	}
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	weihuPersonWindow.hide();
            }
        },ts]
    });

  //---win stop
    
    /***********************************************************************
	*Window,Form相关
	************************************************************************/
    
    
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
            {name: 'name', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'tipDays', type: 'int'},
            {name: 'executorId', type: 'int'},
            {name: 'executorName', type: 'string'},
            {name: 'fid', type: 'int'},
            {name: 'position', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_DeviceManageAction!listAll?fid='+nodeId,
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'设备信息',
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
        	header: "巡检点",
        	dataIndex: 'position',
        	sortable: true
        },{
            header: "设备名称",
            dataIndex: 'name',
            sortable: true
        },{
            header: "设备描述",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "设备路径",
            dataIndex: 'fid',
            sortable: true,
            hidden:true
        },{
            header: "周期（天）",
            dataIndex: 'tipDays',
            sortable: true
        },{
        	header: "任务人ID",
        	dataIndex: 'executorId',
        	sortable: true,
        	hidden:true
        },{
        	header: "任务人",
        	dataIndex: 'executorName',
        	sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加设备',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择设备的父节点');
					return;
				}if(!node.attributes.leaf) {
					Ext.MessageBox.alert('提示','不能在叶子节点添子权限');
					return;
				}else {
					Ext.getCmp('fid').setValue(node.id);
					addOrUpdateWindow.show();
					
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改设备',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要修改的记录');
            		return;
            	}else if(records.length > 1){
            		Ext.MessageBox.alert('提示','仅支持单条记录修改');
					return;
				}else {
					addOrUpdateForm.form.reset();
					addOrUpdateForm.form.loadRecord(records[0]);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '删除',
            tooltip: '删除设备',
            icon : "img/button/add.gif",
            handler: function(){
            	
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要删除的记录');
            	}else{
            		Ext.MessageBox.confirm('请确认', '确认删除，同时删除任务',function(btn){
            			if(btn == 'yes'){
            				var ids = '';
            				for(var i=0;i<records.length;i++){
            					ids += records[i].get('id')+',';
            				}
            				Ext.Ajax.request({
								url: 'dZJK_DeviceManageAction!delete',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_DeviceManageAction!listAll',
									            method: 'POST'
											});
											store.baseParams = {
												fid : nodeId
											};
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											reloadUpdateNode();
											Ext.MessageBox.alert('提示','删除设备成功');
										}
									}
							   	},
							   	params: { 
							   		ids:ids
							   	}
							});
            			}
            		});
            	}
			}
        },ts,{
            text: '分配责任人',
            tooltip: '设备分配责任人',
            text: '任务分配',
            tooltip: '定检任务分配',
            id : 'bt_sprtongguo',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要受理的记录');
            	}else if(records.length>1){
            		Ext.MessageBox.alert('提示','仅支持单条记录处理');
            	}else{
            		org_usr_grid.proxy = new Ext.data.HttpProxy({
            			url: 'dZJK_WuZiAction!listBaoXiuWeiHuAll',
                        method: 'POST'
            		});
            		org_usr_store.load({
            			params : {
            				start : 0,
            				limit : page,
            				msg_type : 1
            			}
            		});
            		weihuPersonWindow.show();
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
	************************************************************************/
	
    
    /***********************************************************************
	*Tree相关
	************************************************************************/
    <!--树形系统菜单-->
	var root = new Ext.tree.AsyncTreeNode({
		text : '定检点信息',
		draggable : false,
		id : '0',
		isleaf : '-1',
		url : 'isleaf',
		icon : "img/tree/system.png"
	});

	var tree = new Ext.tree.TreePanel({
		renderTo : 'tree',
		autoScroll : true,
		root : root,
		height : height,
		width : width*0.15,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!devicePositionTree?id='+nodeId
				})
	});
	tree.render();
	root.expand();
	
	tree.on('beforeload',function(node){     
		nodeId = node.id;
		tree.loader.dataUrl = 'treeAction!devicePositionTree?id='+nodeId;          
	}); 
	
	tree.on('click',function(node){
		nodeId = node.id;
		store.proxy = new Ext.data.HttpProxy({
			url: 'dZJK_DeviceManageAction!listAll',
            method: 'POST'
		});
		store.baseParams = {
			fid : nodeId
		};
		store.load({
			params : {
				start : 0,
				limit : page
			}
		});
		store.reload();
	});
	/***********************************************************************
	*Tree相关
	************************************************************************/
	
	
	/***********************************************************************
	*布局相关
	************************************************************************/
	new Ext.Viewport({
		layout : 'border',
		items : [{
					region : 'west',
					id : 'tree',
					split : true,
					width : width*0.15,
					collapsible : false,
					layoutConfig : {
						animate : true
					},
					items : [tree]
				},{
					region : 'center',
					width : width*0.85,
					items : grid
				}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
