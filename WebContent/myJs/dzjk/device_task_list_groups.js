Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	
	
	/***************************************************************************
	 * Window,Form相关
	 **************************************************************************/ 
	 var diviceStatusCombo = new Ext.form.ComboBox({
     	xtype:'combo',
        fieldLabel: '任务状态',
        store: new Ext.data.ArrayStore({
        	fields: ['id', 'name'],
        	data: [[-1, '--'],[0, '未巡检'], [1, '正常'], [1, '异常']]
        }),
        displayField: 'name',
        valueField: 'id',
        name: 'deviceStatus',
        emptyText: '请选择...',
        triggerAction: 'all',
        blankText: '请选择用户类型',
        mode: 'local'
    });
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
                    xtype:'textfield',    
                    fieldLabel: '任务人',
                    name: 'weijianName',
                    id: 'weijianName',
                    anchor:'95%'
                },{
					xtype : 'datefield',
					fieldLabel : '任务日期',
					value: Ext.util.Format.date(new Date(),'Ymd'),
			        name: 'taskDate',
			        id: 'taskDate',
					format : 'Ymd'
                },diviceStatusCombo]
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
					url: 'dZJK_DeviceManageAction!listDeviceTaskAllgroup',
		            method: 'POST'
				});
            	var taskDate = Ext.util.Format.date(searchForm.getForm().findField("taskDate").getValue(), 'Ymd');
            	var weijianName = searchForm.getForm().findField("weijianName").getValue();
            	var deviceStatus =diviceStatusCombo.getValue();
            	if(deviceStatus==-1){deviceStatus='';}
            	
            	
            	
            	var fid = nodeId;
				store.baseParams = {
						taskDate:taskDate,
						weijianName:weijianName,
						deviceStatus:deviceStatus,
						fid:fid
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
    /***************************************************************************
	 * Window,Form相关
	 **************************************************************************/
    
  	/***************************************************************************
	 * Grid相关
	 **************************************************************************/
    var reader = new Ext.data.JsonReader({
        root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        fields: [
                 {name: 'id', type: 'int'},
                 {name: 'taskDate', type: 'string'},
                 {name: 'deviceId', type: 'int'},
                 {name: 'status', type: 'int'},
                 {name: 'weijianId', type: 'int'},
                 {name: 'tipDays', type: 'int'},
                 {name: 'deviceName', type: 'string'},
                 {name: 'weijianName', type: 'string'},
                 {name: 'updateTime', type: 'string'},
                 {name: 'positionId', type: 'string'},
                 {name: 'positionName', type: 'string'},
                 {name: 'deviceStatus', type: 'string'},
                 {name: 'deal_note', type: 'string'}
             ]

    });
    var store = new Ext.data.GroupingStore({
        reader:reader,
        remoteSort: false,
        groupField:'weijianName',
        sortInfo:{field: 'deviceStatus', direction: "ASC"},
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_DeviceManageAction!listDeviceTaskAllgroup?fid='+nodeId,// 根据部门ID查询此部门下的人员任务？？？
             method: 'POST'
         })
    });
   store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'设备分配的任务列表',
        store: store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : sm,
        view: new Ext.grid.GroupingView({
            forceFit:true,
            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "项" : "项"]})'
        }),
        columns:[sm,{
            id: 'id',
            header: "ID",
            dataIndex: 'id',
            sortable: true
        },{
            header: "任务日期",
            dataIndex: 'taskDate',
            sortable: true
        },{
            header: "设备ID",
            dataIndex: 'deviceId',
            sortable: true,
            hidden:true
        },{
        	header: "巡检点名字",
        	dataIndex: 'positionName',
        	sortable: true
        },{
        	header: "设备",
        	dataIndex: 'deviceName',
        	sortable: true
        },{
        	header: "巡检点ID",
        	dataIndex: 'positionId',
        	sortable: true,
        	hidden:true
        },{
            header: "任务人ID",
            dataIndex: 'weijianId',
            sortable: true,
            hidden:true
        },{
        	header: "任务人",
        	dataIndex: 'weijianName',
        	sortable: true
        },{
            header: "周期（天）",
            dataIndex: 'tipDays',
            sortable: true
        },{
            header: "状态",
            dataIndex: 'status',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "生成任务";
            	}
            	if(value=='1') {
            		return "任务完成";
            	}
            	if(value=='2') {
            		return "补生成任务";
            	}
            }
        },{
            header: "最后修改时间",
            width:200,
            dataIndex: 'updateTime',
            sortable: true
        },{
            header: "设备状态",
            dataIndex: 'deviceStatus',
            sortable: true,
            renderer : function(value) {
            	if(value=='0') {
            		return "未处理";
            	}
            	if(value=='1') {
            		return "正常";
            	}
            	if(value=='2') {
            		return "异常";
            	}
            }
        },{
            header: "处理过程",
            width:200,
            dataIndex: 'deal_note',
            sortable: true
        }],
		tbar : [ts,{
            text: '查询',
            tooltip: '输入条件查询',
            icon : "img/button/add.gif",
            handler: function(){
            	//searchForm.form.reset();
				searchWindow.show();
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
    /***************************************************************************
	 * Grid相关
	 * ***********************************************************************
	 * /***********************************************************************
	 * Tree相关
	 **************************************************************************/
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
		store.proxy = new Ext.data.HttpProxy({
			url: 'dZJK_DeviceManageAction!listDeviceTaskAllgroup?fid='+nodeId,// 根据部门ID查询此部门下的人员任务？？？
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
	/***************************************************************************
	 * Tree相关
	 **************************************************************************/
	
	
	/***************************************************************************
	 * 布局相关
	 **************************************************************************/
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
					width : width,
					items : grid
				}]
	});
	/***************************************************************************
	 * 布局相关
	 **************************************************************************/
});
