//---win start
	var org_usr_store = new Ext.data.JsonStore({
		root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        remoteSort: false,
        fields: [
            {name: 'id', type: 'int'},
            {name: 'username', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'orgId', type: 'int'},
            {name: 'ip', type: 'string'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_PersionWuZiAction!listBaoXiuWeiHuAll',
             method: 'POST'
         })
    });
	org_usr_store.load({params:{start:0, limit:page}});
    
    var org_usr_sm = new Ext.grid.CheckboxSelectionModel();
    var org_usr_grid = new Ext.grid.GridPanel({
        height : height,
        width : width,
        store: org_usr_store,
        trackMouseOver:true,
        disableSelection:false,
        loadMask: true,
        sm : org_usr_sm,
        columns:[org_usr_sm,{
            id: 'id',
            header: "ID",
            dataIndex: 'id',
            sortable: true
        },{
            header: "用户名",
            dataIndex: 'username',
            sortable: true
        },{
            header: "描述信息",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "所属组织ID",
            dataIndex: 'orgId',
            sortable: true
        },{
            header: "用户IP",
            dataIndex: 'ip',
            sortable: true
        }],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
        bbar: new Ext.PagingToolbar({
            store: org_usr_store,
            displayInfo: true,
            displayMsg: '第 {0} - {1} 条, 总共 {2} 条',
            emptyMsg: "没有数据"
        })
    });
    var weihuPersonWindow = new Ext.Window({
        id : 'weihuPersonWindow',
        title: '维检人员名单',
        width: width*.55,
        height: height*.45,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: org_usr_grid,
        buttons: [ts,{
            text: '受理',
            icon : "img/button/add.gif",
            handler: function(){
            	var baoxiu_records = grid.getSelectionModel().getSelections();
            	var weijian_records = org_usr_grid.getSelectionModel().getSelections();
            	if(weijian_records.length!=1){
            		Ext.MessageBox.alert('提示','单报修信息只能分配给一个维检人员');
            		return;
            	}else{
            		var baoxiuId = baoxiu_records[0].get('id');
            		var weijianId = weijian_records[0].get('id');
    				Ext.Ajax.request({
						url: 'dZJK_PersionWuZiAction!wuziBaoXiuShouLi',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								if (responseArray.msg == 'true') {
						   			store.proxy = new Ext.data.HttpProxy({
										url: 'dZJK_PersionWuZiAction!listBaoXiuAll',
							            method: 'POST'
									});
									store.load({
										params : {
											start : 0,
											limit : page
										}
									});
									store.reload();
									weihuPersonWindow.hide();
									Ext.MessageBox.alert('提示','申请审批通过');
								}
							}
					   	},
					   	params: { 
					   		weijianId:weijianId,
					   		baoxiuId : baoxiuId
					   	}
					});
            	}
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	weihuPersonWindow.hide();
            }
        },ts]
    });

  //---win stop