Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'qtip';
	
	// 0 islead -1 notleaf
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var nodeId = '0';
	
    /***************************************************************************
	 * 自定义函数相关
	 **************************************************************************/
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
	
	/***************************************************************************
	 * Window,Form相关
	 **************************************************************************/
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
                    fieldLabel: '资产名称',
                    allowBlank:false,
                    blankText:'资产名称不能为空',
                    name: 'name',
                    id: 'name',
                    anchor:'95%'
                }, {
                    xtype:'textfield',
                    fieldLabel: '物资类别ID',
                    allowBlank:false,
                    disabled : true,
                    blankText:'物资类别不能为空',
                    name: 'wuziTypeId',
                    id: 'wuziTypeId',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '资产有效期',
                    name: 'youxiaoqi',
                    id: 'youxiaoqi',
                    hidden : false,
                    disabled : false,
                    allowBlank:false,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '资产ID',
                    name: 'id',
                    id: 'id',
                    hidden : false,
                    disabled : true,
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '资产描述',
                    allowBlank:false,
                    blankText:'资产描述不能为空',
                    name: 'descr',
                    id: 'descr',
                    anchor:'95%'
                },{
                    xtype:'numberfield',
                    fieldLabel: '(入库/追加)数量',
                    name: 'tcount',
                    id: 'tcount',
                    hidden : false,
                    allowBlank:false,
                    blankText:'数量必须填写',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '提醒天数',
                    name: 'tipDays',
                    id: 'tipDays',
                    hidden : false,
                    disabled : false,
                    allowBlank:false,
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '剩余阀值',
                    name: 'tipNum',
                    id: 'tipNum',
                    hidden : false,
                    disabled : false,
                    allowBlank:false,
                    anchor:'95%'
                }]
            }]
        }]
    });
    var addOrUpdateWindow = new Ext.Window({
        id : 'addOrUpdateWindow',
        title: '资产信息',
        width: 600,
        height:200,
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
            		var ttcount = Ext.getCmp('tcount').getValue()*1;
            		var records = grid.getSelectionModel().getSelections();
            		if(records.length!=0){
            			var sysl = records[0].get('tcount')*1;
            			ttcount += sysl;
            		}
					Ext.Ajax.request({
						url: 'dZJK_WuZiAction!saveOrUpdate',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
					   			store.proxy = new Ext.data.HttpProxy({
									url: 'dZJK_WuZiAction!listAll',
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
					   		name:Ext.getCmp('name').getValue(),
					   		descr:Ext.getCmp('descr').getValue(),
					   		wuziTypeId:Ext.getCmp('wuziTypeId').getValue(),
					   		tcount:ttcount,
					   		youxiaoqi:Ext.getCmp('youxiaoqi').getValue(),
					   		tipDays:Ext.getCmp('tipDays').getValue(),
					   		tipNum:Ext.getCmp('tipNum').getValue()
					   	}
					});
        		
            	}
			}
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	addOrUpdateWindow.hide();
            	addOrUpdateForm.form.reset();
            }
        },ts]
    });
    /***************************************************************************
	 * Window,Form相关
	 **************************************************************************/
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
        title: '物资上传',
        width: 600,
        height:130,
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
            	    url : 'myUpload!upload_wuzi',  
            	    method : 'POST',  
            	    success : function(form, action) {
            	    	
            	    	store.proxy = new Ext.data.HttpProxy({
							url: 'dZJK_WuZiAction!listAll',
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
	
    
  	/***************************************************************************
	 * Grid相关
	 **************************************************************************/
    var store = new Ext.data.JsonStore({
        root: 'root',
        totalProperty: 'totalProperty',
        idProperty: 'id',
        remoteSort: false,
        fields: [
            {name: 'id', type: 'int'},
            {name: 'name', type: 'string'},
            {name: 'descr', type: 'string'},
            {name: 'wuziTypeId', type: 'string'},
            {name: 'wuziTypeName', type: 'string'},
            {name: 'youxiaoqi', type: 'string'},
            {name: 'tipDays', type: 'int'},
            {name: 'tipNum', type: 'int'},
        	{name: 'firstRecordDate', type: 'string'},
            {name: 'tcount', type: 'int'},
            {name: 't_all_count', type: 'int'},
            {name: 't_feiqi_count', type: 'int'},
            {name: 't_fafang_count', type: 'int'}
        ],
        proxy : new Ext.data.HttpProxy({
             url: 'dZJK_WuZiAction!listAll?fid='+nodeId,
             method: 'POST'
         })
    });
    // store.setDefaultSort('code', 'desc');
    store.load({params:{start:0, limit:page}});
    
    var sm = new Ext.grid.CheckboxSelectionModel();
    var grid = new Ext.grid.GridPanel({
        height : height,
        width : width*.85,
        title:'资产明细',
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
            header: "资产类别ID",
            dataIndex: 'wuziTypeId',
            sortable: true,
            hidden:true
        },{
            header: "资产类别",
            dataIndex: 'wuziTypeName',
            sortable: true
        },{
            header: "资产名字",
            dataIndex: 'name',
            sortable: true
        },{
            header: "资产描述",
            dataIndex: 'descr',
            sortable: true
        },{
            header: "剩余数量",
            dataIndex: 'tcount',
            sortable: true
        },{
        	header: "总量",
        	dataIndex: 't_all_count',
        	sortable: true
        },{
        	header: "已发放量",
        	dataIndex: 't_fafang_count',
        	sortable: true
        },{
        	header: "报废数量",
        	dataIndex: 't_feiqi_count',
        	sortable: true
        },{
        	header: "第一次录入时间",
        	dataIndex: 'firstRecordDate',
        	sortable: true
        },{
            header: "有效期",
            dataIndex: 'youxiaoqi',
            sortable: true
        },{
            header: "过期前提醒天",
            dataIndex: 'tipDays',
            sortable: true,
            renderer:function(value,cellmeta,record,rowIndex,columnIndex,store){
            	var yxq = record.data["youxiaoqi"];
            	var yxq = yxq.substr(0,4)+"/"+yxq.substr(4,2)+"/"+yxq.substr(6,2);
            	var diffday = Date.parse(yxq) - new Date();
            	diffday = Math.ceil(diffday/(24*60*60*1000));
            	if(diffday<=value){
            		var tips = value-diffday;
            		if(tips>=0){
            			return "<font color='red'>还有"+diffday+"过期</font>";
            		}else{
            			return "<font color='red'>已经过期"+Math.abs(tips)+"天</font>";
            		}
            	}else{
            		return value;
            	}
            }
        },{
            header: "剩余阀值",
            dataIndex: 'tipNum',
            sortable: true
        }, {
			xtype : "actioncolumn",
			header : "详情",
			items : [{
				icon : 'img/button/add.gif',
				handler : function(grid, rindex, cindex) {
					var record = grid.getStore().getAt(rindex);
					var wuziId = record.get("id");
					display_win(wuziId);
				}
			}]
		} ],
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true
        },
		tbar : [ts,{
            text: '新增资产',
            tooltip: '添加资产明细',
            icon : "img/button/add.gif",
            handler: function(){
				var node = tree.getSelectionModel().getSelectedNode();
				if(node == null || node == '') {
					Ext.MessageBox.alert('提示','请选择物资类别的父节点');
					return;
				}if(node.attributes.isleaf==-1) {
					Ext.MessageBox.alert('提示','不能在父节点添加物资');
					return;
				}if(node.id==0) {
					Ext.MessageBox.alert('提示','不能在此添加物资');
					return;
				}else {
					addOrUpdateForm.form.reset();
					Ext.getCmp('wuziTypeId').setValue(node.id);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '资产追加入库',
            tooltip: '资产追加入库',
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
					Ext.getCmp('tcount').setValue(0);
					addOrUpdateWindow.show();
				}
			}
        },ts,{
            text: '删除资产',
            tooltip: '删除资产',
            disabled : true,
            icon : "img/button/add.gif",
            handler: function(){
            	
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
    /***************************************************************************
	 * Grid相关
	 **************************************************************************/
    function display_win(wuziId){
	  	/***************************************************************************
		 * Grid相关
		 **************************************************************************/
	    var reader = new Ext.data.JsonReader({
	        root: 'root',
	        totalProperty: 'totalProperty',
	        idProperty: 'id',
	        fields: [
	                 {name: 'persion_id', type: 'int'},
	                 {name: 'wuziId', type: 'int'},
	                 {name: 'tcount', type: 'int'},
	                 {name: 'orgId', type: 'int'},
	                 {name: 'wuziName', type: 'string'},
	                 {name: 'wuziCode', type: 'string'},
	                 {name: 'username', type: 'string'},
	                 {name: 'orgName', type: 'string'}
	             ]
	
	    });
		var store = new Ext.data.GroupingStore({
			reader:reader,
	        remoteSort: false,
	        groupField:'orgName',
	        sortInfo:{field: 'persion_id', direction: "ASC"},
	        proxy : new Ext.data.HttpProxy({
	             url: 'dZJK_PersionWuZiAction!listPersonInfoAll_infos?wuziId='+wuziId,
	             method: 'POST'
	         })
	    });
	    store.load({params:{start:0, limit:page }});
	    
	    var grid = new Ext.grid.GridPanel({
	    	height : height,
	        width : width*0.85,
	        title:'资产明细列表',
	        store: store,
	        autoScroll:true,
	        trackMouseOver:true,
	        disableSelection:false,
	        loadMask: true,
	        view: new Ext.grid.GroupingView({
	            forceFit:true,
	            groupTextTpl: '{text} ({[values.rs.length]} {[values.rs.length > 1 ? "项" : "项"]})'
	        }),
	        columns:[{
	            header: "persion_id",
	            hidden :true,
	            dataIndex: 'persion_id',
	            sortable: true
	        },{
	        	header: "wuziId",
	        	hidden :true,
	        	dataIndex: 'wuziId',
	        	sortable: true
	
	        },{
	        	header: "orgId",
	        	hidden :true,
	        	dataIndex: 'orgId',
	        	sortable: true
	        },{
	            header: "物资",
	            dataIndex: 'wuziName',
	            sortable: true
	        },{
	        	header: "物资编码",
	        	dataIndex: 'wuziCode',
	        	sortable: true
	        },{
	        	header: "持有人",
	        	dataIndex: 'username',
	        	sortable: true
	        },{
	        	header: "部门名称",
	        	dataIndex: 'orgName',
	        	sortable: true
	        },{
	        	header: "数量",
	        	dataIndex: 'tcount',
	        	sortable: true
	        }]
	    });
	    /***************************************************************************
		 * Grid相关
		 **************************************************************************/
	    var win=new Ext.Window({
	        width:width*.85,
	        modal:true,
	        height:height*.85,
	        items:[grid]
	     });
	     win.show();
		
}	
    
    /***************************************************************************
	 * Tree相关
	 **************************************************************************/
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
		width : width*0.15,
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
			url: 'dZJK_WuZiAction!listAll',
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
	/***************************************************************************
	 * 布局相关
	 **************************************************************************/
});
