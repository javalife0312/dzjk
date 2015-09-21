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
	var searchForm = new Ext.FormPanel({
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
					xtype : 'datefield',
					fieldLabel : '开始日期',
					allowBlank : false,
					value: Ext.util.Format.date(new Date(),'Ymd'),
			        name: 'startDate',
					format : 'Ymd'
				},{
					xtype : 'datefield',
					fieldLabel : '结束日期',
					allowBlank : false,
					value: Ext.util.Format.date(new Date(),'Ymd'),
			        name: 'endDate',
					format : 'Ymd'
				},{
                    xtype:'textfield',    
                    fieldLabel: '用户名',
                    allowBlank:true,
                    blankText:'名字不能为空',
                    name: 'username',
                    id: 'username',
                    anchor:'95%'
                }]
            }]
        }]
    });
    var searchWindow = new Ext.Window({
        id : 'searchWindow',
        title: '输入条件查询',
        width: 350,
        height:200,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: searchForm,
        buttons: [ts,{
            text: '查询',
            icon : "img/button/add.gif",
            handler: function(){
            	if(!searchForm.form.isValid()){
            		return;
            	}
            	store.proxy = new Ext.data.HttpProxy({
					url: 'dzjk_ReportAction!xunjianTJ',
		            method: 'POST'
				});
            	
            	var startDate = Ext.util.Format.date(searchForm.getForm().findField("startDate").getValue(), 'Ymd');
            	var endDate = Ext.util.Format.date(searchForm.getForm().findField("endDate").getValue(), 'Ymd');
            	var username = searchForm.getForm().findField("username").getValue();
            	var orgIg = nodeId;
				store.baseParams = {
						startDate:startDate,
				   		endDate:endDate,
				   		username:username,
				   		orgIg:nodeId
				};
				store.load({
					params : {
						start : 0,
						limit : page
					}
				});
				
				searchForm.form.reset();
            	searchWindow.hide();
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	searchForm.form.reset();
            	searchWindow.hide();
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
            {name: 'username', type: 'string'},
            {name: 'userdesc', type: 'string'},
            {name: 'orgName', type: 'string'},
            {name: 'zongji', type: 'int'},
            {name: 'weixunjian', type: 'int'},
            {name: 'zhengchang', type: 'int'},
            {name: 'yichang', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dzjk_ReportAction!xunjianTJ',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    //store.load();
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'巡检任务统计',
        store: store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : sm,
        columns:[sm,{
            header: "维修员代号",
            dataIndex: 'username',
            sortable: true
        },{
            header: "用户名",
            dataIndex: 'userdesc',
            sortable: true
        },{
            header: "部门名称",
            dataIndex: 'orgName',
            sortable: true
        },{
            header: "总任务量",
            dataIndex: 'zongji',
            sortable: true
        },{
            header: "未巡检设备",
            dataIndex: 'weixunjian',
            sortable: true
        },{
            header: "正常设备",
            dataIndex: 'zhengchang',
            sortable: true
        },{
            header: "异常设备",
            dataIndex: 'yichang',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '查询',
            tooltip: '输入条件查询',
            icon : "img/button/add.gif",
            handler: function(){
            	//searchForm.form.reset();
				searchWindow.show();
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
	});
	/***********************************************************************
	*Tree相关
	************************************************************************/
	
	
	/***********************************************************************
	*布局相关
	************************************************************************/
	new Ext.Viewport({
		layout : 'border',
		items : [/**{
					region : 'west',
					split : true,
					width : width*0.15,
					collapsible : false,
					layoutConfig : {
						animate : true
					},
					items : [tree]
				},*/{
					region : 'center',
					width : width*0.85,
					items : grid
				}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
