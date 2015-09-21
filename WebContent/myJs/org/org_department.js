Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	var nodePath = '0';
	
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
	var checkbox1 = new Ext.form.Checkbox({
		name: 'isleaf123',
		xtype: 'checkbox',
		boxLabel:'子节点',
		checked : true 
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
                    fieldLabel: '名字',
                    allowBlank:false,
                    blankText:'名字不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '描述',
                    allowBlank:false,
                    blankText:'描述不能为空',
                    name: 'descr',
                    id: 'descr',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '组织路径',
                    name: 'path',
                    id: 'path',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
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
                },checkbox1]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '组织机构信息',
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
            	var tid = Ext.getCmp('id').getValue();
            	if(tid == null || tid == ''){
            		tid = '';
            	}
            	Ext.Ajax.request({
					url: 'ogrDepartmentAction!saveOrUpdate',
				   	success: function(response){
				   		var responseArray = Ext.util.JSON.decode(response.responseText);
						if (responseArray.success == 'true') {
				   			store.proxy = new Ext.data.HttpProxy({
								url: 'ogrDepartmentAction!listAllDeparts',
					            method: 'POST'
							});
							store.baseParams = {
								id : nodeId,
								path : nodePath
							};
							store.load({
								params : {
									start : 0,
									limit : page
								}
							});
							store.reload();
							addOrUpdateForm.form.reset();
							addOrUpdateWindow.hide();
							reloadUpdateNode();
							Ext.MessageBox.alert('提示','添加/修改组织结构成功');
						}
				   	},
				   	params: { 
				   		id:tid,
				   		name:Ext.getCmp('name').getValue(),
				   		descr:Ext.getCmp('descr').getValue(),
				   		isleaf:checkbox1.getValue(),
				   		path:Ext.getCmp('path').getValue(),
				   		fid:Ext.getCmp('fid').getValue()
				   	}
				});
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
            {name: 'name', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'path', type: 'string'},
            {name: 'isleaf', type: 'int'},
            {name: 'fid', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'ogrDepartmentAction!listAllDeparts?id='+nodeId + '&path='+nodePath,
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'组织结构信息',
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
            header: "名字",
            dataIndex: 'name',
            sortable: true
        },{
            header: "描述信息",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "部门路径",
            dataIndex: 'path',
            sortable: true,
            hidden : true
        },{
            header: "Leaf",
            dataIndex: 'isleaf',
            sortable: true,
            hidden : true,
            renderer : function(value) {
            	if(value=='0') {
            		return "叶子节点";
            	}
            	if(value=='1') {
            		return "父节点";
            	}
            }
        },{
            header: "父节点ID",
            dataIndex: 'fid',
            hidden : true,
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加部门',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				var node = tree.getSelectionModel().getSelectedNode();
//				debugger;
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择部门的父节点');
					return;
				}else {
					checkbox1.checked = true;
					Ext.getCmp('fid').setValue(node.id);
					Ext.getCmp('path').setValue(node.attributes.path+','+node.id);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改部门信息',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要修改的记录');
            		return;
            	}else if(records.length > 1){
            		Ext.MessageBox.alert('提示','仅支持单条部门修改');
					return;
				}else {
					addOrUpdateForm.form.reset();
					addOrUpdateForm.form.loadRecord(records[0]);
					var leaf = records[0].data.isleaf;
					if(leaf == 0){
						checkbox1.checked = true;
						checkbox1.disabled = true;
					}
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '删除',
            tooltip: '删除部门',
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
								url: 'ogrDepartmentAction!deleteDeparts',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'ogrDepartmentAction!listAllDeparts',
									            method: 'POST'
											});
											store.baseParams = {
												id : nodeId,
												path : nodePath
											};
											store.load({
												params : {
													start : 0,
													limit : page
												}
											});
											store.reload();
											reloadUpdateNode();
											Ext.MessageBox.alert('提示','删除部门成功');
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
		text : '组织结构',
		draggable : false,
		id : '0',
		path : '0',
		url : 'isleaf',
		icon : "img/tree/system.png"
	});
	var tree = new Ext.tree.TreePanel({
		renderTo : 'tree',
		autoScroll : true,
		width : width*0.15,
		height : height,
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
		nodePath = node.attributes.path;
		store.proxy = new Ext.data.HttpProxy({
			url: 'ogrDepartmentAction!listAllDeparts',
            method: 'POST'
		});
		store.baseParams = {
			id : nodeId,
			path : nodePath
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
					width : width*0.85,
					items : grid
				}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
