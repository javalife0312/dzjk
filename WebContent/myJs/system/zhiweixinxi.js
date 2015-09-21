Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	
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
	var level_combox_tore = new Ext.data.ArrayStore({
        fields: ['id', 'name'],
        data: [[1, '高优先级'], [2, '中等优先级'], [3, '低优先级']]
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
                    fieldLabel: '职级名称',
                    allowBlank:false,
                    blankText:'职位不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                },{
                	xtype:'combo',
                    fieldLabel: '报修优先级',
                    store: level_combox_tore,
                    triggerAction: 'all',
                    displayField: 'name',
                    valueField: 'id',
                    name: 'level',
                    id: 'level',
                    emptyText: '请选择...',
                    allowBlank: false,
                    blankText: '请选择优先级信息',
                    mode: 'local'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '职级描述',
                    allowBlank:false,
                    blankText:'URL不能为空',
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
        title: '职级管理',
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
						url: 'zhiWeiAction!saveOrUpdate',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'zhiWeiAction!listAll',
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
								reloadUpdateNode();
								Ext.MessageBox.alert('提示','添加权限成功');
							}
					   	},
					   	params: { 
					   		id:Ext.getCmp('id').getValue(),
					   		name:Ext.getCmp('name').getValue(),
					   		descr:Ext.getCmp('descr').getValue(),
					   		level:Ext.getCmp('level').getValue()
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
            {name: 'level', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'zhiWeiAction!listAll',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'职级信息',
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
            header: "职级名称",
            dataIndex: 'name',
            sortable: true
        },{
            header: "优先级",
            dataIndex: 'level',
            sortable: true,
            renderer : function(value) {
            	if(value=='1') {
            		return "高优先级";
            	}
            	if(value=='2') {
            		return "中等优先级";
            	}
            	if(value=='3') {
            		return "低优先级";
            	}
            }
        },{
            header: "职级描述",
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
            tooltip: '添加职级',
            icon : "img/button/add.gif",
            handler: function(){
            	addOrUpdateForm.form.reset();
				addOrUpdateWindow.show();
			}
        },ts,{
            text: '修改',
            tooltip: '修改职级',
            icon : "img/button/add.gif",
            handler: function(){
				var records = grid.getSelectionModel().getSelections();
            	if(records.length == 0) {
            		Ext.MessageBox.alert('提示','请选择要修改的记录');
            		return;
            	}else if(records.length > 1){
            		Ext.MessageBox.alert('提示','仅支持单条职级修改');
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
								url: 'zhiWeiAction!delete',
							   	success: function(response){
							   		var responseArray = Ext.util.JSON.decode(response.responseText);
									if (responseArray.success == 'true') {
										if (responseArray.msg == 'true') {
								   			store.proxy = new Ext.data.HttpProxy({
												url: 'zhiWeiAction!listAll',
									            method: 'POST'
											});
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
