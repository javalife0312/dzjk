Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	var groupId = '0';
	
    /***********************************************************************
	*自定义函数相关
	************************************************************************/
	
	/***********************************************************************
	*Tree相关
	************************************************************************/
	var root = new Ext.tree.AsyncTreeNode({
		text : '权 限',
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
		checkModel : 'cascade',
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!sysCheckBoxTree?id='+nodeId+'&userid='+groupId,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					}
				})
	});
	tree.render();
	tree.expandAll();
	
	tree.on('beforeload',function(node){     
		nodeId = node.id;
		tree.loader.dataUrl = 'treeAction!sysCheckBoxTree?id='+nodeId+'&userid='+groupId;      
	});  
	tree.addListener('checkchange', function(node, checked) {
		if(node.attributes.url=='isleaf'){
			if(checked){
				node.eachChild(function(child) {
					child.ui.toggleCheck(checked);
					child.attributes.checked = true;
					child.fireEvent('checkchange', child, checked);
				});
			}else{
				node.eachChild(function(child) {
					child.ui.toggleCheck(checked);
					child.attributes.checked = false;
					child.fireEvent('checkchange', child, checked);
				});
			}
		}
	});
	/***********************************************************************
	*Tree相关
	************************************************************************/
	
	
	
	/***********************************************************************
	*Window,Form相关
	************************************************************************/
	var addOrUpdateForm = new Ext.FormPanel({
        labelAlign: 'left',
        frame:true,
        title: '',
        bodyStyle:'padding:5px 5px 0',
        width: 450,
        items: [{
            layout:'column',
            items:[{
                columnWidth:1,
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
                    fieldLabel: '名称',
                    allowBlank:false,
                    blankText:'名称不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                },{
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
                    disabled : true,
                    anchor:'95%'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '权限组信息',
        width: 450,
        height:190,
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
            		var authId = Ext.getCmp('id').getValue();
            		if(authId != null && authId != '') {
            			//修改不用验证编号
						Ext.Ajax.request({
							url: 'authorizationGroupAction!saveOrUpdateRole',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'authorizationGroupAction!listAllAuthorizationGroups',
							            method: 'POST'
									});
									store.load({
										params : {
											start : 0,
											limit : page
										}
									});
									store.reload();
									addOrUpdateWindow.hide();
									Ext.MessageBox.alert('提示','添加权限成功');
								}
						   	},
						   	params: { 
						   		id:Ext.getCmp('id').getValue(),
						   		code:Ext.getCmp('code').getValue(),
						   		name:Ext.getCmp('name').getValue(),
						   		descr:Ext.getCmp('descr').getValue()
						   	}
						});
            		}else {
	            		//新增验证编号重复
	            		Ext.Ajax.request({
							url: 'authorizationGroupAction!isCodeExit',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			if (responseArray.msg == 'true') {
						   				Ext.MessageBox.alert('提示','编号重复');
						   				return;
						   			}else{
							            Ext.Ajax.request({
											url: 'authorizationGroupAction!saveOrUpdateRole',
										   	success: function(response){
										   		var responseArray = Ext.util.JSON.decode(response.responseText);
												if (responseArray.success == 'true') {
										   			store.proxy = new Ext.data.HttpProxy({
														url: 'authorizationGroupAction!listAllAuthorizationGroups',
											            method: 'POST'
													});
													store.load({
														params : {
															start : 0,
															limit : page
														}
													});
													store.reload();
													addOrUpdateWindow.hide();
													Ext.MessageBox.alert('提示','添加权限成功');
												}
										   	},
										   	params: { 
										   		id:Ext.getCmp('id').getValue(),
										   		code:Ext.getCmp('code').getValue(),
										   		name:Ext.getCmp('name').getValue(),
										   		descr:Ext.getCmp('descr').getValue()
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
    
    var authFPWindow = new Ext.Window({
        id : 'authFPWindow',
        title: '权限分配',
        width: 300,
        height:450,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: tree,
        buttons: [{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	var nodeIds = '';
            	var checkedNodes = tree.getChecked();
            	if(checkedNodes.length == 0) {
            		Ext.MessageBox.alert('提示','请选择权限');
            		return;
            	}else{
	            	var authId = grid.getSelectionModel().getSelected().get('id');
	            	for(var i=0;i<checkedNodes.length;i++) {
						nodeIds += checkedNodes[i].id + ',';            		
	            	}
	            	Ext.Ajax.request({
						url: 'authorizationGroupAction!authorizationGroupFPRoles',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								authFPWindow.hide();
								Ext.MessageBox.alert('提示','分配权限成功');
							}
					   	},
					   	params: { 
					   		nodeIds:nodeIds,
					   		authId:authId
					   	}
					});
            	}
            }
        },{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	authFPWindow.hide();
            }
        }]
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
            {name: 'name', type: 'string'},
            {name: 'descr', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'authorizationGroupAction!listAllAuthorizationGroups',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
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
            header: "名字",
            dataIndex: 'name',
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
            tooltip: '添加权限组',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				Ext.getCmp("code").enable();
				addOrUpdateWindow.show();
			}
        },ts,{
            text: '修改',
            tooltip: '修改权限组',
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
            tooltip: '删除权限组',
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
								url: 'authorizationGroupAction!deleteAuthorizationGroups',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'authorizationGroupAction!listAllAuthorizationGroups',
									            method: 'POST'
											});
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
        },ts,{
            text: '权限',
            tooltip: '分配权限',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要分配权限的权限组');
            		return;
            	}else if(records.length > 1) {
            		Ext.MessageBox.alert('提示','仅支持单组权限分配');
            		return;
            	}else{
            		groupId = records[0].get('id');
            		root.reload();
            		tree.expandAll();
            		tree.show();
            		authFPWindow.show();
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
	*布局相关
	************************************************************************/
	 var viewport = new Ext.Viewport({
         layout:'border',
         items:[{
             region:'center',
             margins:'0 0 1 0',//上->右->下->左
             layout:'fit',
             autoScroll:true,
             items:[grid]
         }]
     });
	/***********************************************************************
	*布局相关
	************************************************************************/
});
