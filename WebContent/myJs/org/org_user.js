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
	*Window,Form 上传导入
	************************************************************************/
	var uploadForm = new Ext.form.FormPanel({    
		labelAlign: 'left',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
	    labelWidth: 80,     
	    fileUpload:true,    
	    defaultType: 'form',    
	    items: [{    
	        xtype: 'textfield',    
	        fieldLabel: '上传文件',    
	        name: 'urlName',    
	        inputType: 'file',    
	        allowBlank: false,    
	        anchor: '90%'
	   }]    
	});  
    var uploadWindow = new Ext.Window({
        id : 'uploadWindow',
        title: '用户信息',
        width: 600,
        height:120,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: uploadForm,
        buttons: [ts,{
            text: '上传文件',
            icon : "img/button/add.gif",
            handler: function(){
            	uploadForm.getForm().submit({  
            	    url : 'myUpload!upload_usr',  
            	    method : 'POST',  
            	    success : function(form, action) {  
            	    	store.proxy = new Ext.data.HttpProxy({
			   				url: 'ogrUserAction!listAll?id='+nodeId,
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
						
            	        Ext.Msg.alert('成功','恭喜！文件上传成功！');  
            	        uploadWindow.hide();  
            	    },  
            	    failure : function(form, action) {  
            	        Ext.Msg.alert('錯誤',"文件上传失败，请重新操作！");  
            	    }  
            	}) ; 
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	uploadForm.getForm().reset();;
            	uploadWindow.hide();
            }
        },ts]
    });
	/***********************************************************************
	*Window,Form 上传导入
	************************************************************************/
	
	
	/***********************************************************************
	*Window,Form相关
	************************************************************************/
	var root_qx = new Ext.tree.AsyncTreeNode({
		text : '权 限',
		draggable : false,
		id : '0',
		icon : "img/tree/system.png"
	});
	var tree_qx = new Ext.tree.TreePanel({
		renderTo : '',
		autoScroll : true,
		root : root_qx,
		animate : true,
		enableDD : false,
		checkModel : 'cascade',
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!sysCheckBoxTree?id='+qx_nodeId+'&userid='+userid,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					}
				})
	});
	tree_qx.on('beforeload',function(node){     
		qx_nodeId = node.id;
		tree_qx.loader.dataUrl = 'treeAction!sysCheckBoxTree?id='+node.id+'&userid='+userid;      
	});  
	tree_qx.addListener('checkchange', function(node, checked) {
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
        items: tree_qx,
        buttons: [{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	var nodeIds = '';
            	var checkedNodes = tree_qx.getChecked();
            	debugger;
            	if(checkedNodes.length == 0) {
            		Ext.MessageBox.alert('提示','请选择权限');
            		return;
            	}else{
	            	var uid = grid.getSelectionModel().getSelected().get('id');
	            	for(var i=0;i<checkedNodes.length;i++) {
						nodeIds += checkedNodes[i].id + ',';            		
	            	}
	            	Ext.Ajax.request({
						url: 'ogrUserAction!quanxianFP',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								authFPWindow.hide();
								Ext.MessageBox.alert('提示','分配权限成功');
							}
					   	},
					   	params: { 
					   		nodeIds:nodeIds,
					   		uid:uid
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
    
    
    var utype_combox_tore = new Ext.data.ArrayStore({
    	fields: ['id', 'name'],
    	data: [[0, '超级管理员'], [1, '物资资产管理员'], [2, '报修审批管理员'], [3, '报修维护人员'], [100, '普通用户']]
    });
    var level_combox_tore = new Ext.data.JsonStore({
        autoLoad : true,
        fields: ['id', 'name'],
        method: 'POST',
        url : "zhiWeiAction!listAllLevel"
    });
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
                    fieldLabel: '用户名',
                    allowBlank:false,
                    blankText:'名字不能为空',
                    name: 'username',
                    id: 'username',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '描述信息',
                    allowBlank:false,
                    blankText:'描述不能为空',
                    name: 'descr',
                    id: 'descr',
                    anchor:'95%'
                },{
                	xtype:'combo',
                    fieldLabel: '职位信息',
                    store: level_combox_tore,
                    displayField: 'name',
                    valueField: 'id',
                    name: 'level',
                    id: 'level',
                    emptyText: '请选择...',
                    allowBlank: false,
                    triggerAction: 'all',
                    blankText: '请选择职位信息',
                    mode: 'local'
                },{
                	xtype:'combo',
                    fieldLabel: '用户类型',
                    store: utype_combox_tore,
                    triggerAction: 'all',
                    displayField: 'name',
                    valueField: 'id',
                    name: 'type',
                    id: 'type',
                    emptyText: '请选择...',
                    allowBlank: false,
                    blankText: '请选择用户类型',
                    mode: 'local'
                }, {
                    xtype:'textfield',
                    fieldLabel: '联系电话',
                    allowBlank:false,
                    blankText:'联系电话不能为空',
                    name: 'phoneNum',
                    id: 'phoneNum',
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '密码',
                    allowBlank:false,
                    name: 'password',
                    id: 'password',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '部门ID',
                    name: 'orgId',
                    id: 'orgId',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'IP',
                    allowBlank:false,
                    blankText:'IP不能为空',
                    name: 'ip',
                    id: 'ip',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: 'ID',
                    name: 'id',
                    id: 'id',
                    hidden : true,
                    disabled : true,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '工位',
                    name: 'gongwei',
                    id: 'gongwei',
                    hidden : false,
                    disabled : false,
                    anchor:'95%'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '用户信息',
        width: 600,
        height:300,
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
            	var tid = Ext.getCmp('id').getValue();
            	if(tid == null || tid == ''){
            		tid = '';
            	}
            	if(!addOrUpdateForm.form.isValid()){
            		return;
            	}
            	Ext.Ajax.request({
					url: 'ogrUserAction!checkUsername?username='+Ext.getCmp('username').getValue(),
				   	success: function(response){
				   		var responseArray = Ext.util.JSON.decode(response.responseText);
						if (responseArray.success == 'true') {
				   			if(responseArray.msg == 'false' || tid != ''){
				   				Ext.Ajax.request({
									url: 'ogrUserAction!saveOrUpdate',
								   	success: function(response){
								   		var responseArray = Ext.util.JSON.decode(response.responseText);
										if (responseArray.success == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
								   				url: 'ogrUserAction!listAll?id='+nodeId,
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
											addOrUpdateForm.form.reset();
											Ext.MessageBox.alert('提示','添加/修改用户成功');
										}
								   	},
								   	params: { 
								   		id:tid,
								   		username:Ext.getCmp('username').getValue(),
								   		password:Ext.getCmp('password').getValue(),
								   		descr:Ext.getCmp('descr').getValue(),
								   		orgId:Ext.getCmp('orgId').getValue(),
								   		level:Ext.getCmp('level').getValue(),
								   		type:Ext.getCmp('type').getValue(),								   	
								   		gongwei:Ext.getCmp('gongwei').getValue(),								   	
								   		ip:Ext.getCmp('ip').getValue(),
								   		phoneNum:Ext.getCmp('phoneNum').getValue()
								   	}
								});
				   			}else{
				   				Ext.MessageBox.alert("提示","用户名已经存在");
				   				Ext.getCmp('username').setValue("");
				   			}
						}
				   	}
				});
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	addOrUpdateForm.getForm().reset();;
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
            {name: 'username', type: 'string'},
            {name: 'password', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'orgId', type: 'int'},
            {name: 'orgName', type: 'string'},
            {name: 'level', type: 'int'},
            {name: 'levelName', type: 'string'},
            {name: 'type', type: 'int'},
            {name: 'ip', type: 'string'},
            {name: 'gongwei', type: 'string'},
            {name: 'phoneNum', type: 'string'},
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'ogrUserAction!listAll?id='+nodeId,
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'用户信息',
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
            header: "用户名",
            dataIndex: 'username',
            sortable: true
        },{
            header: "联系电话",
            dataIndex: 'phoneNum',
            sortable: true
        },{
            header: "密码",
            dataIndex: 'password',
            sortable: true
        },{
            header: "描述信息",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "组织部门",
            dataIndex: 'orgName',
            sortable: true
        },{
            header: "职级信息",
            dataIndex: 'levelName',
            sortable: true
        },{
            header: "用户类型",
            dataIndex: 'type',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "超级管理员";
            	}
            	if(value=='1') {
            		return "物资资产管理员";
            	}
            	if(value=='2') {
            		return "报修审批管理员";
            	}
            	if(value=='3') {
            		return "报修维护人员";
            	}
            	if(value=='100') {
            		return "普通用户";
            	}
            }
        },{
            header: "IP",
            dataIndex: 'ip',
            sortable: true
        },{
            header: "工位",
            dataIndex: 'gongwei',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加用户',
            icon : "img/button/add.gif",
            handler: function(){
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择部门');
					return;
				}else if(node.attributes.leaf != true) {
					Ext.MessageBox.alert('提示','只能在叶子节点添加信息');
					return;
				}else {
					addOrUpdateForm.form.reset();
					Ext.getCmp('orgId').setValue(nodeId);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改用户',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要修改的记录');
            		return;
            	}else if(records.length > 1){
            		Ext.MessageBox.alert('提示','仅支持单条修改');
					return;
				}else {
					addOrUpdateForm.getForm().reset();
					addOrUpdateForm.form.loadRecord(records[0]);
					Ext.getCmp('username').disabled = true;
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '删除',
            tooltip: '删除用户',
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
								url: 'ogrUserAction!delete',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
								   				url: 'ogrUserAction!listAll?id='+nodeId,
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
											Ext.MessageBox.alert('提示','删除用户成功');
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
            text: '权限分配',
            tooltip: '权限分配',
            icon : "img/button/add.gif",
            handler: function(){
            	var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要分配权限');
            	}else if(records.length > 1) {
            		Ext.MessageBox.alert('提示','每次只能给单个人分配权限');
            	}else{
            		userid = records[0].get('id');
            		debugger;
            		root_qx.reload();
            		tree_qx.expandAll();
            		tree_qx.show();
            		authFPWindow.show();
            	}
			}
        },ts,{
            text: '上传导入',
            tooltip: '上传导入',
            icon : "img/button/add.gif",
            handler: function(){
            	uploadWindow.show();
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
		text : '组织结构',
		draggable : false,
		id : '0',
		url : 'isleaf',
		icon : "img/tree/system.png"
	});
	var tree = new Ext.tree.TreePanel({
		renderTo : 'tree',
		autoScroll : true,
		height : height,
		width : width*0.15,
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!orgTree?id='+nodeId
				})
	});
	tree.render();
	root.expand();
	
	tree.on('beforeload',function(node){     
		nodeId = node.id;
		tree.loader.dataUrl = 'treeAction!orgTree?id='+nodeId;          
	}); 
	
	tree.on('click',function(node){
		nodeId = node.id;
		store.proxy = new Ext.data.HttpProxy({
			url: 'ogrUserAction!listAll',
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
					split : true,
					width : width*0.15,
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
