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
	function reloadUpdateNode(){
		tree.getRootNode().reload();
	}
	
	/***********************************************************************
	*Window,Form相关
	************************************************************************/
	var type_tore = new Ext.data.ArrayStore({
    	fields: ['id', 'name'],
    	data: [[1, '软件'], [2, '硬件']]
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
                    fieldLabel: '物资类别名称',
                    allowBlank:false,
                    blankText:'物资类别名称不能为空',
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
                    fieldLabel: '物资类别描述',
                    allowBlank:false,
                    blankText:'物资类别描述不能为空',
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
                },{
                	xtype:'combo',
                    fieldLabel: '软硬件标志',
                    store: type_tore,
                    displayField: 'name',
                    valueField: 'id',
                    name: 'type',
                    id: 'type',
                    value:1,
                    allowBlank: false,
                    triggerAction: 'all',
                    mode: 'local'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '物资类别',
        width: 650,
        height:180,
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
						url: 'dZJK_WuZiTypeAction!saveOrUpdate',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_WuZiTypeAction!listAll',
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
								Ext.MessageBox.alert('提示','添加物资类别成功');
							}
					   	},
					   	params: { 
					   		id:Ext.getCmp('id').getValue(),
					   		fid:Ext.getCmp('fid').getValue(),
					   		name:Ext.getCmp('name').getValue(),
					   		descr:Ext.getCmp('descr').getValue(),
					   		isleaf:Ext.getCmp('isleaf').getValue(),
					   		type:Ext.getCmp('type').getValue()
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
            {name: 'fid', type: 'int'},
            {name: 'type', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_WuZiTypeAction!listAll?fid='+nodeId,
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        title:'物资类别信息',
        store: store,
        height : height,
        width:width*.85, 
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
            header: "物资名称",
            dataIndex: 'name',
            sortable: true
        },{
            header: "物资描述",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "物资类别路径",
            dataIndex: 'fid',
            sortable: true,
            hidden : true
        },{
            header: "是否是叶子节点",
            dataIndex: 'isleaf',
            sortable: true,
            hidden : true
        },{
            header: "软硬件标志",
            dataIndex: 'type',
            sortable: true,
            hidden : false,
            renderer : function(value) {
            	if(value=='1') {
            		return "软件";
            	}
            	if(value=='2') {
            		return "硬件";
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
            tooltip: '添加物资类别',
            icon : "img/button/add.gif",
            handler: function(){
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择物资类别的父节点');
					return;
				}if(node.attributes.isleaf != -1) {
					Ext.MessageBox.alert('提示','不能在叶子节点添子节点');
					return;
				}else {
					addOrUpdateForm.form.reset();
					Ext.getCmp("type").setValue(1); 
					Ext.getCmp('fid').setValue(node.id);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '修改',
            tooltip: '修改物资类别',
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
								url: 'dZJK_WuZiTypeAction!delete',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'dZJK_WuZiTypeAction!listAll',
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
		text : '物资类别',
		draggable : false,
		id : '0',
		isleaf : '-1',
		url : 'isleaf',
		icon : "img/tree/system.png"
	});
	var tree = new Ext.tree.TreePanel({
		renderTo : 'tree',
		autoScroll : true,
		height : height,
		width:width*.15, 
		root : root,
		animate : true,
		enableDD : false,
		border : false,
		rootVisible : true,
		containerScroll : true,
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!wuziTypeTree?id='+nodeId
				})
	});
	tree.render();
	root.expand();
	
	tree.on('beforeload',function(node){     
		nodeId = node.id;
		tree.loader.dataUrl = 'treeAction!wuziTypeTree?id='+nodeId;          
	}); 
	
	tree.on('click',function(node){
		nodeId = node.id;
		store.proxy = new Ext.data.HttpProxy({
			url: 'dZJK_WuZiTypeAction!listAll',
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
