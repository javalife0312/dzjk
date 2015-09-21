Ext.onReady(function() {

	var height = document.body.clientHeight;
	var width = document.body.clientWidth;
	
	var radiogroup= new Ext.form.RadioGroup({
        fieldLabel : "系统",
        height : 27,
        items: [
                {boxLabel: '管理系统', name: 'xt', inputValue: 1,checked: true},
                {boxLabel: '访客自助', name: 'xt', inputValue: 2},
                {boxLabel: '商户自助', name: 'xt', inputValue: 3},
                {boxLabel: '测试系统', name: 'xt', inputValue: 4}
            ]
    });
	
	var loginPanel = new Ext.FormPanel({
        labelAlign: 'right',
        buttonAlign : "center",
        minButtonWidth : 120,
        renderTo : 'loginPanel',
        shadow : true,
        style:'border: 3px inset #8db2e3;', 
        //bodyStyle:"filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='img/system/loginBg2.bmp', sizingMethod='scale')", //背景图片
		//bodyStyle:"background-color:green", //背景图片
        frame : true,
        border : true,
        height : height*0.28,
        width : width * 0.4,
        items: [{
            layout:'column',
            items:[{
                columnWidth:1,
                layout: 'form',
                items: [{
                    xtype:'displayfield',
                    hideLabel : true,
                    fieldLabel: '欢迎',
                    name: 'displayname',
                    id: 'displayname',
                    value : "<center><font size='6' face='华文行楷'>欢迎使用LS3X模板系统</font></center>",
                    anchor:'100%'
                },{
                    xtype:'textfield',
                    height : 27,
                    fieldLabel: '用户名',
                    allowBlank:false,
                    blankText:'用户名不能为空',
                    name: 'username',
                    id: 'username',
                    anchor:'90%'
                }, {
                    xtype:'textfield',
                    height : 27,
                    inputType : 'password',
                    fieldLabel: '密码',
                    allowBlank:false,
                    blankText:'密码不能为空',
                    name: 'password',
                    id: 'password',
                    anchor:'90%'
                },radiogroup]
            }]
        }],
        buttons: [{
            text: '登陆',
            icon : "img/system/loginBtn.png",
           	iconAlign: 'left',
            handler: function(){
				if (loginPanel.form.isValid()) { 
            		var username = Ext.getCmp('username').getValue();
            	 	var password = Ext.getCmp('password').getValue();

					Ext.MessageBox.show({ 
						title : "请等待正在登陆", 
						progress : true, 
						width : 300 
					});
					
					Ext.Ajax.request({
						url: 'loginAction!login',
						method : 'POST',
					   	success: function(response){
					   		var responseArray = Ext.util.JSON.decode(response.responseText);
							if (responseArray.success == 'true') {
								Ext.MessageBox.hide();
					   			window.location.href = responseArray.msg;
							}else{
								loginPanel.form.reset();
								Ext.MessageBox.hide();
								Ext.MessageBox.alert('提示','用户名或者密码错误，请重新登陆');
							}
					   	},
					   	params: { 
					   		username:username,
					   		password:password
					   	}
					});
				}
            }
        },{
            text: '取消',
            icon : "img/system/logoutBtn.png",
            handler: function(){
            	loginPanel.form.reset();
            }
        }]
    });
    
    loginPanel.el.setOpacity(0.5,true);//true 
});
