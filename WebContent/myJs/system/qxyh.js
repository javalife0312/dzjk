Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
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
                    fieldLabel: '编号',
                    allowBlank:false,
                    blankText:'编号不能为空',
                    name: 'code',
                    id: 'code',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '用户名',
                    allowBlank:false,
                    blankText:'用户名不能为空',
                    name: 'username',
                    id: 'username',
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '密码',
                    allowBlank:false,
                    blankText:'密码不能为空',
                    name: 'password',
                    id: 'password',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '权限组ID',
                    name: 'groupId',
                    id: 'groupId',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '描述',
                    name: 'descr',
                    id: 'descr',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'ID',
                    name: 'id',
                    id: 'id',
                    hidden : true,
                    anchor:'95%'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '权限用户信息',
        width: 600,
        height:185,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: addOrUpdateForm,
        buttons: [{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	if(addOrUpdateForm.form.isValid()){
            		var userId = Ext.getCmp('id').getValue();
            		if(userId != null && userId != '') {
            			//修改不用验证编号
						Ext.Ajax.request({
							url: 'authorizationUserAction!saveOrUpdate',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'authorizationUserAction!listAllAuthorizationUsers',
							            method: 'POST'
									});
									store.baseParams = {
										id : nodeId
									};
									store.load({
										params : {
											start : 0,
											limit : page
										}
									});
									store.reload();
									addOrUpdateWindow.hide();
									Ext.MessageBox.alert('提示','修改权限用户成功');
								}
						   	},
						   	params: { 
						   		id:Ext.getCmp('id').getValue(),
						   		code:Ext.getCmp('code').getValue(),
						   		username:Ext.getCmp('username').getValue(),
						   		password:Ext.getCmp('password').getValue(),
						   		descr:Ext.getCmp('descr').getValue(),
						   		groupId:Ext.getCmp('groupId').getValue()
						   	}
						});
            		}else {
	            		//新增验证编号重复
	            		Ext.Ajax.request({
							url: 'authorizationUserAction!isCodeExit',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			if (responseArray.msg == 'true') {
						   				Ext.MessageBox.alert('提示','编号重复');
						   				return;
						   			}else{
							            Ext.Ajax.request({
											url: 'authorizationUserAction!saveOrUpdate',
										   	success: function(response){
										   		var responseArray = Ext.util.JSON.decode(response.responseText);
												if (responseArray.success == 'true') {
										   			store.proxy = new Ext.data.HttpProxy({
														url: 'authorizationUserAction!listAllAuthorizationUsers',
											            method: 'POST'
													});
													store.baseParams = {
														id : nodeId
													};
													store.load({
														params : {
															start : 0,
															limit : page
														}
													});
													store.reload();
													addOrUpdateWindow.hide();
													Ext.MessageBox.alert('提示','添加权限用户成功');
												}
										   	},
										   	params: { 
										   		id:Ext.getCmp('id').getValue(),
										   		code:Ext.getCmp('code').getValue(),
										   		username:Ext.getCmp('username').getValue(),
										   		password:Ext.getCmp('password').getValue(),
										   		descr:Ext.getCmp('descr').getValue(),
										   		groupId:Ext.getCmp('groupId').getValue()
										   	}
										});
						   			}
								}
						   	},
						   	params: { 
						   		code:Ext.getCmp('code').getValue()
						   	}
						});
            		}
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
            {name: 'code', type: 'string'},
            {name: 'username', type: 'string'},
            {name: 'password', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'groupId', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'authorizationUserAction!listAllAuthorizationUsers',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'权限组信息',
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
            header: "编码",
            dataIndex: 'code',
            sortable: true
        },{
            header: "用户名",
            dataIndex: 'username',
            sortable: true
        },{
            header: "密码",
            dataIndex: 'password',
            sortable: true
        },{
            header: "权限组ID",
            dataIndex: 'groupId',
            sortable: true
        },{
            header: "描述",
            dataIndex: 'descr',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加权限用户',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				Ext.getCmp("code").enable();
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '' || node.id == '0') {
					Ext.MessageBox.alert('提示','请选择权限组');
					return;
				}else {
					nodeId = node.id;
					Ext.getCmp('groupId').setValue(nodeId);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改权限',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要删除的记录');
            		return;
            	}else if(records.length > 1){
            		Ext.MessageBox.alert('提示','仅支持单条权限修改');
					return;
				}else {
					addOrUpdateForm.form.reset();
					addOrUpdateForm.form.loadRecord(records[0]);
					Ext.getCmp("code").disable();
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '删除',
            tooltip: '删除权限',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要删除的记录');
            	}else{
            		Ext.MessageBox.confirm('请确认', '确认删除，同时删除子集',function(btn){
            			if(btn == 'yes'){
            				var ids = '';
            				for(var i=0;i<records.length;i++){
            					ids += records[i].get('id')+',';
            				}
            				Ext.Ajax.request({
								url: 'authorizationUserAction!deleteAuthorizationUsers',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'authorizationUserAction!listAllAuthorizationUsers',
									            method: 'POST'
											});
											store.baseParams = {
												id : nodeId
											};
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											Ext.MessageBox.alert('提示','删除权限成功');
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
	************************************************************************/
	
    
    /***********************************************************************
	*Tree相关
	************************************************************************/
    <!--树形系统菜单-->
	var root = new Ext.tree.AsyncTreeNode({
		text : '权限组',
		draggable : false,
		id : '0',
		icon : "img/tree/system.png"
	});
	var tree = new Ext.tree.TreePanel({
		renderTo : 'tree',
		autoScroll : true,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'authorizationUserAction!sysAuthTree'
				})
	});
	tree.render();
	root.expand();
	
	tree.on('click',function(node){
		nodeId = node.id;
		store.proxy = new Ext.data.HttpProxy({
			url: 'authorizationUserAction!listAllAuthorizationUsers',
            method: 'POST'
		});
		store.baseParams = {
			id : nodeId
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
					minSize : 175,
					maxSize : 400,
					margins : '0 0 0 0',
					collapsible : false,
					layoutConfig : {
						animate : true
					},
					items : [tree]
				},{
			region : 'center',
			items : grid
		}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
