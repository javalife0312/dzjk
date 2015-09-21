Ext.onReady(function() {
	Ext.QuickTips.init();
	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	var ts = '-';
	var page = 100;
	var btnId;
	var msgRecord;
	var showMsgs="-1";
	
	/***********************************************************************
	*Window,Form相关
	************************************************************************/
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
        title: '人员名单',
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
            	var weijian_records = org_usr_grid.getSelectionModel().getSelections();
            	if(weijian_records.length!=1){
            		Ext.MessageBox.alert('提示','仅支持单条消息处理');
            		return;
            	}else{
            		var msgDealPersionID = weijian_records[0].get('id');
            		Ext.Ajax.request({
    					url: 'dZJK_PersionWuZiAction!messageBoxDeal',
    				   	success: function(response){
    				   		var responseArray = Ext.util.JSON.decode(response.responseText);
    						if (responseArray.success == 'true') {
    							weihuPersonWindow.hide();
    							var btn = Ext.getCmp("padd"+msgRecord.id);
    							pnl.remove(btn); 
    		                	pnl.doLayout(true);
    		                	msg_window.hide();
    							Ext.MessageBox.alert('提示','已经分发任务');
    						}
    				   	},
    				   	params: { 
    				   		msgId:msgRecord.id,
    				   		msgType : msgRecord.type,
    				   		msgDealPersionID : msgDealPersionID
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
    /***********************************************************************
	*Window,Form相关
	************************************************************************/
	
	
	var msg_panel = new Ext.Panel({
		id:'msg_panel',
		autoscroll :true,
		width: width*3,
		height:height*.25,
		items:[
	       {xtype:'textarea',id:'msg_textarea',width:width*0.4,height: height*.5}
	    ]
	});
	
	 var msg_window = new Ext.Window({
        id : 'msg_window',
        title: '消息信息',
        width: width*.4,
        height: height*.5,
        layout: 'fit',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'right',
        closable:false,
        items: msg_panel,
        buttons: [ts,/*{
            text: '驳回',
            icon : "img/button/add.gif",
            handler: function(){
            	var baoxiuId = deal_id;
				Ext.Ajax.request({
					url: 'dZJK_PersionWuZiAction!wuziBaoXiuCheXiao',
				   	success: function(response){
				   		var responseArray = Ext.util.JSON.decode(response.responseText);
						if (responseArray.success == 'true') {
							msg_window.hide();
							Ext.MessageBox.alert('提示','维修记录已经驳回');
		                	
						}
				   	},
				   	params: { 
				   		ids:baoxiuId,
				   		status:'-2'
				   	}
				});
            }
        },ts,*/{
            text: '分配操作员',
            icon : "img/button/add.gif",
            handler: function(){
            	org_usr_store.load({
            		params:{
            			start:0, 
            			limit:page,
            			msg_type:msgRecord.type
            		}
            	});
            	msg_window.hide();
            	weihuPersonWindow.show();
            }
        },ts,{
            text: '取消',
            icon : "img/button/add.gif",
            handler: function(){
            	msg_window.hide();
            }
        },ts]
    });
	 
	/**
	 * 格式化输出字符长度
	 */
	function formatPrint(msg,len){
		var result = "";
		if(msg==null){
			msg = "";
		}
		if(msg.length>=len){
			result = msg.substr(0,len);
		}else{
			result = msg;
			for(var i=msg.length;i<len;i++){
				result += "&nbsp;";
			}
		}
		return result;
	}
	
	var pnl = new Ext.Panel({
	    layout: 'vbox',
	    id:'panelID',
	    autoscroll :true,
	    height : height,
	    defaults: {
	        margins :'5 0 0 5',
	        width: width,
	        height:50
	    },
	    bodyStyle:'background:#ffffff',
	    items:[
//	            {xtype:'panel',title:"<font color='red'>消息信息</font>",bodyStyle:'background:#000000'},
	            //{xtype:'button',text:'消息2asdf  fs dfasf asdfa sfasdf afdasdf asfdsadfasdf asdfasdfasdfa sadf',bodyStyle:'background:red'}
	        ]
	});
	Ext.Ajax.request({
		url: 'dZJK_PersionWuZiAction!listBaoXiuMsg',
	    method: 'post',
	    async :  false,//同步请求数据
	    success: function(response, request) {
	    	msgs = new Object()
	    	var json = response.responseText;
	        var arr = eval(json);
	    	msgs = arr;
	        if(arr==null || arr==""){
	        	return;
	        }
	    	for(var i=0;i<arr.length;i++){
	    		var record = arr[i];
	    		var id = "padd"+record.id;
	    		
	    		var level = record.level;
	    		var levelDesc = "普通优先级";
	    		if(record.level==1){
	    			levelDesc = "高优先级";
	    		}
	    		if(record.level==2){
	    			levelDesc = "中度优先级";
	    		}
            	
	    		var titleText = "消息类型 : "  +formatPrint(record.typeDesc,6)+  "|";
	    		titleText +=    "优先级别 : "  +formatPrint(levelDesc,6)+        "|";
	    		titleText +=    "申请时间 : "  +formatPrint(record.createDate,17)+"|";
	    		titleText +=    "申请人工位 : " +formatPrint(record.apply_personGongwei,15)+"|";
	    		titleText +=    "申请人名字 : " +formatPrint(record.apply_personName,5)+"|";
	    		titleText +=    "申请人电话 : " +formatPrint(record.apply_personPhone,15)+"|";
	    		titleText +=    "申请缘由 : " +formatPrint(record.apply_note,10)+"|";
	    		titleText +=    "资产类型 : "  +formatPrint(record.apply_wuziMsg,10)+"|";
	    		var font_size = 3;
	    		
	    		var title = "&nbsp;&nbsp;<font color='black' size='"+font_size+"'><b>"+titleText+"&nbsp;&nbsp;&nbsp;&nbsp;</b></font>";
	    		if(level==1){
	    			title = "&nbsp;&nbsp;<font color='red' size='"+font_size+"'><b>"+titleText+"&nbsp;&nbsp;&nbsp;&nbsp;</b></font>";
	    		}
	    		if(level==2){
	    			title = "&nbsp;&nbsp;<font color='orange' size='"+font_size+"'><b>"+titleText+"&nbsp;&nbsp;&nbsp;&nbsp;</b></font>";
	    		}
	    		//动态添加信息
	    		pnl.add(new Ext.Button({
		    		id:id,
		    	    text:title,
		    	    listeners:{
	                  "click":function(){
	                	  var id = this.id;
	                	  var record;
	                	  for(var k=0;k<msgs.length;k++){
	                		  var tmp_id = "padd"+msgs[k].id;
	                		  if(tmp_id==id){
	                			  record=msgs[k];
	                			  msgRecord=msgs[k];
	                			  
	                		  }
	                	  }
	                	  
	                	  var level = "普通优先级";
	                	  if(record.level==1){
	                		  level = "高优先级";
	                	  }
	                	  if(record.level==2){
	                		  level = "中度先级";
	                	  }
	                	  var msgText = "消息类型 : "+record.typeDesc+"\n\n";
	                	  msgText += "优先级别 : "+level + "\n\n";
	                	  msgText += "申请时间 : "+record.createDate + "\n\n";
	                	  msgText += "申请人工位 : "+record.apply_personGongwei + "\n\n";
	                	  msgText += "申请人名字 : "+record.apply_personName + "\n\n";
	                	  msgText += "申请人电话 : "+record.apply_personPhone + "\n\n";
	                	  msgText += "申请描述: "+record.apply_note + "\n\n";
	                	  msgText += "资产类型 : "+record.apply_wuziMsg + "\n\n";
	                	  
	                	  
	                	  
	                	  var textArea = Ext.getCmp("msg_textarea");
	                	  textArea.setValue(msgText);
	                	  textArea.readOnly=true;
	                	  
	                	  btnId = id;
//	                	  pnl.remove(this);
//	                	  pnl.doLayout(true);
	                	  msg_window.show();
	                  }
	              }
		    	}));
		    	pnl.doLayout(true);
	    	}
	    	//alert(showMsgs);
	    },
	   	params: { 
	   		status:0
	   	}
	});
	/***********************************************************************
	 *Task start
	 ************************************************************************/
	
//	var task = {
//	    run: function(){
//	    },
//	    interval: 10000 //1 second
//	}
//	Ext.TaskMgr.start(task);
	/***********************************************************************
	 *Task end
	 ************************************************************************/
	
	/***********************************************************************
	*布局相关
	************************************************************************/
	
	new Ext.Viewport({
		layout : 'border',
		border:false,
		items : [{
			region:'center',
			border:false,
			layout : 'fit',
		    autoscroll :true,
			items : [pnl]
		}]
	});
	/***********************************************************************
	*布局相关
	************************************************************************/
});
