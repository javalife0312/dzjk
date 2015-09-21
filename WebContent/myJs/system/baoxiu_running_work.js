Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
    
  	/***********************************************************************
	*Grid相关
	************************************************************************/
    var store = new Ext.data.JsonStore({
        root: 'root',
        totalProperty: 'totalProperty',
        remoteSort: false,
        fields: [
            {name: 'weixiu_person_id', type: 'string'},
            {name: 'weixiu_persion_name', type: 'string'},
            {name: 'weixiu_persion_phone', type: 'string'},
            {name: 'start_time', type: 'string'},
            
            {name: 'baoxiu_person_id', type: 'string'},
            {name: 'baoxiu_persion_name', type: 'string'},
            {name: 'baoxiu_persion_gongwei', type: 'string'},
            {name: 'baoxiu_persion_wuziName', type: 'string'},
            {name: 'status', type: 'string'}

            
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_PersionWuZiAction!listRunningWorks',
             method: 'POST'
         })
    });
    //store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        title:'Running Works',
        store: store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        columns:[{
            id: 'weixiu_person_id',
            header: "维修人员ID",
            dataIndex: 'weixiu_person_id',
            hidden:true,
            sortable: true
        },{
            header: "维修人员名字",
            dataIndex: 'weixiu_persion_name',
            sortable: true,
            hidden:false
        },{
            header: "维修人员电话",
            dataIndex: 'weixiu_persion_phone',
            sortable: true,
            hidden:false
        },{
        	header: "开始时间",
        	dataIndex: 'start_time',
        	sortable: true
        },{
        	header: "报修人ID",
        	dataIndex: 'baoxiu_person_id',
        	sortable: true,
        	hidden:true
        },{
            header: "报修人名字",
            dataIndex: 'baoxiu_persion_name',
            sortable: true,
            hidden: false
        },{
            header: "报修人工位",
            dataIndex: 'baoxiu_persion_gongwei',
            sortable: true
        },{
            header: "物资名称",
            dataIndex: 'baoxiu_persion_wuziName',
            sortable: true
        },{
            header: "状态",
            dataIndex: 'status',
            sortable: true,
            renderer : function(value) {
            	if(value=='2') {
            		return "已受理,处理中...";
            	}
            }
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
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
});

