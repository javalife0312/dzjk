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
                    fieldLabel: '名称',
                    allowBlank:false,
                    blankText:'名称不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: 'URL',
                    allowBlank:false,
                    blankText:'URL不能为空',
                    name: 'url',
                    id: 'url',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '父节点ID',
                    name: 'fid',
                    id: 'fid',
                    hidden : false,
                    disabled : true,
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
        title: '权限信息',
        width: 600,
        height:170,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: addOrUpdateForm,
        buttons: [ts,{
			id: 'isleaf',
			xtype: 'checkbox',
			boxLabel:'子节点',
			checked:true,
			handler : function(){
				var isleaf = this.getValue();
				if(isleaf){
					Ext.getCmp("url").enable();
					Ext.getCmp("url").setValue('');
				}else{
					Ext.getCmp("url").disable();
					Ext.getCmp("url").setValue('isleaf');
				}
			}
        },ts,{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	if(addOrUpdateForm.form.isValid()){
            		var roleId = Ext.getCmp('id').getValue();
            		if(roleId != null && roleId != '') {
            			//修改不用验证编号
						Ext.Ajax.request({
							url: 'roleAction!saveOrUpdateRole',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'roleAction!listAllRoles',
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
									reloadUpdateNode();
									Ext.MessageBox.alert('提示','添加权限成功');
								}
						   	},
						   	params: { 
						   		id:Ext.getCmp('id').getValue(),
						   		code:Ext.getCmp('code').getValue(),
						   		name:Ext.getCmp('name').getValue(),
						   		url:Ext.getCmp('url').getValue(),
						   		fid:Ext.getCmp('fid').getValue()
						   	}
						});
            		}else {
	            		//新增验证编号重复
	            		Ext.Ajax.request({
							url: 'roleAction!isCodeExit',
						   	success: function(response){
						   		var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
						   			if (responseArray.msg == 'true') {
						   				Ext.MessageBox.alert('提示','编号重复');
						   				return;
						   			}else{
							            Ext.Ajax.request({
											url: 'roleAction!saveOrUpdateRole',
										   	success: function(response){
										   		var responseArray = Ext.util.JSON.decode(response.responseText);
												if (responseArray.success == 'true') {
										   			store.proxy = new Ext.data.HttpProxy({
														url: 'roleAction!listAllRoles',
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
													reloadAddNode(nodeId);
													Ext.MessageBox.alert('提示','添加权限成功');
												}
										   	},
										   	params: { 
										   		id:Ext.getCmp('id').getValue(),
										   		code:Ext.getCmp('code').getValue(),
										   		name:Ext.getCmp('name').getValue(),
										   		url:Ext.getCmp('url').getValue(),
										   		fid:Ext.getCmp('fid').getValue()
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
            {name: 'name', type: 'string'},
            {name: 'url', type: 'string'},
            {name: 'fid', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'roleAction!listAllRoles',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'权限信息',
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
            header: "URL",
            dataIndex: 'url',
            sortable: true
        },{
            header: "父节点ID",
            dataIndex: 'fid',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加权限',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				Ext.getCmp("code").enable();
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择权限的父节点');
					return;
				}if(node.attributes.url != 'isleaf') {
					Ext.MessageBox.alert('提示','不能在叶子节点添子权限');
					return;
				}else {
					nodeId = node.id;
					Ext.getCmp('fid').setValue(nodeId);
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
								url: 'roleAction!deleteRoles',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'roleAction!listAllRoles',
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
											reloadUpdateNode();
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
		text : '权 限',
		draggable : false,
		id : '0',
		url : 'isleaf',
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
					dataUrl : 'treeAction!sysTree?id='+nodeId
				})
	});
	tree.render();
	root.expand();
	
	tree.on('beforeload',function(node){     
		nodeId = node.id;
		tree.loader.dataUrl = 'treeAction!sysTree?id='+nodeId;          
	}); 
	
	tree.on('click',function(node){
		nodeId = node.id;
		store.proxy = new Ext.data.HttpProxy({
			url: 'roleAction!listAllRoles',
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
