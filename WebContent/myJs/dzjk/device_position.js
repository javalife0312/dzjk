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
                    fieldLabel: '定检点类别名称',
                    allowBlank:false,
                    blankText:'定检点类别名称不能为空',
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
                },{
                    xtype:'textfield',
                    fieldLabel: '是否是叶子节点',
                    name: 'isleaf',
                    id: 'isleaf',
                    emptyText:'0',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '定检点类别描述',
                    allowBlank:false,
                    blankText:'定检点类别描述不能为空',
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
        title: '定检点类别',
        width: 600,
        height:170,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: addOrUpdateForm,
        buttons: [ts,{
			id: 'isleafcheck',
			xtype: 'checkbox',
			boxLabel:'子节点',
			checked:true,
			handler : function(){
				var isleaf = this.getValue();
				if(isleaf){
					Ext.getCmp("isleaf").setValue('0');
				}else{
					Ext.getCmp("isleaf").setValue('-1');
				}
			}
        },ts,{
            text: '保存',
            icon : "img/button/add.gif",
            handler: function(){
            	if(addOrUpdateForm.form.isValid()){
					Ext.Ajax.request({
						url: 'dZJK_DevicePositionManageAction!saveOrUpdate',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_DevicePositionManageAction!listAll',
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
								Ext.MessageBox.alert('提示','添加定检点类别成功');
							}
					   	},
					   	params: { 
					   		id:Ext.getCmp('id').getValue(),
					   		fid:Ext.getCmp('fid').getValue(),
					   		name:Ext.getCmp('name').getValue(),
					   		descr:Ext.getCmp('descr').getValue(),
					   		isleaf:Ext.getCmp('isleaf').getValue()
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
            {name: 'isleaf', type: 'string'},
            {name: 'fid', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_DevicePositionManageAction!listAll?fid='+nodeId,
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*0.85,
        title:'定检点信息',
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
            header: "定检点名称",
            dataIndex: 'name',
            sortable: true
        },{
            header: "定检点描述",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "定检点类别路径",
            dataIndex: 'fid',
            sortable: true,
            hidden:true
        },{
            header: "是否是叶子节点",
            dataIndex: 'isleaf',
            sortable: true,
            renderer : function(value) {
            	if(value=='-1') {
            		return "否";
            	}
            	if(value=='0') {
            		return "是";
            	}
            }
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '添加',
            tooltip: '添加定检点类别',
            icon : "img/button/add.gif",
            handler: function(){
				addOrUpdateForm.form.reset();
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择定检点类别的父节点');
					return;
				}if(node.attributes.isleaf != -1) {
					Ext.MessageBox.alert('提示','不能在叶子节点添子权限');
					return;
				}else {
					Ext.getCmp('fid').setValue(node.id);
					addOrUpdateWindow.show();
					
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改定检点类别',
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
								url: 'dZJK_DevicePositionManageAction!delete',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_DevicePositionManageAction!listAll',
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
											Ext.MessageBox.alert('提示','删除巡检点成功');
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
		width : width*0.15,
		height : height,
		root : root,
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
			url: 'dZJK_DevicePositionManageAction!listAll',
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
					items : [grid]
				}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
