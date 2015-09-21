Ext
		.onReady(function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'qtip';

			var height = document.body.clientHeight;
			var width = document.body.clientWidth;
			var ts = '-';
			var page = 100;
			var nodeId = '0';
			var qx_nodeId = '0';
			var userid = '0';
			var apply_type;

			// ----------start 其它报修
			var baoxiuForm_other = new Ext.FormPanel({
				labelAlign : 'left',
				frame : true,
				title : '',
				bodyStyle : 'padding:5px 5px 0',
				width : 600,
				layout : 'form',
				border : false,
				items : [ {
					xtype : 'textarea',
					fieldLabel : '问题描述',
					disabled : false,
					id : 'baoyou_yuanyin_other',
					name : 'baoyou_yuanyin',
					allowBlank : false,
					blankText : '请填写问题描述',
					anchor : '95%'
				} ]
			});
			var baoxiuWindow_other = new Ext.Window(
					{
						title : '报修资产明细',
						width : 450,
						height : 180,
						layout : 'fit',
						plain : true,
						bodyStyle : 'padding:5px;',
						buttonAlign : 'right',
						closable : false,
						// items: tree_wuzitype,
						items : baoxiuForm_other,
						buttons : [
								{
									text : '报修',
									icon : "img/button/add.gif",
									handler : function() {
										debugger;
										if (baoxiuForm_other.form.isValid()) {
											Ext.Ajax
													.request({
														url : 'dZJK_PersionWuZiAction!wuziBaoXiuShenqing_other',
														success : function(
																response) {
															var responseArray = Ext.util.JSON
																	.decode(response.responseText);
															if (responseArray.success == 'true') {
																store.proxy = new Ext.data.HttpProxy(
																		{
																			url : 'dZJK_PersionWuZiAction!listPersonInfoAll',
																			method : 'POST'
																		});
																store
																		.load({
																			params : {
																				start : 0,
																				limit : page
																			}
																		});
																store.reload();
																baoxiuWindow_other
																		.hide();
																Ext.MessageBox
																		.alert(
																				'提示',
																				'故障报修成功');
															}
														},
														params : {
															baoyou_yuanyin : Ext
																	.getCmp(
																			'baoyou_yuanyin_other')
																	.getValue(),
															status : 0
														}
													});

										}
									}
								}, {
									text : '取消',
									icon : "img/button/add.gif",
									handler : function() {
										baoxiuForm_other.form.reset();
										baoxiuWindow_other.hide();
									}
								} ]
					});

			// --------stop 其它报修
			
			var mark_field = new Ext.form.TextField({
				fieldLabel : '申请理由',
				name : 'shenqingliyou',
				id : 'shenqingliyou',
				width : 250
            });
			
			var mark_field_1 = new Ext.form.TextField({
				fieldLabel : '申请理由',
				name : 'shenqingliyou1',
				id : 'shenqingliyou1',
				width : 250
			});
			
			var mark_window = new Ext.Window({
				id : 'mark_window',
				title : '申请理由',
				width : 750,
				height : 300,
				layout : 'fit',
				plain : true,
				bodyStyle : 'padding:5px;',
				buttonAlign : 'right',
				closable : false,
				items : [mark_field],
				buttons : [{
					text : '确认',
					icon : "img/button/add.gif",
					handler : function() {
						mark_window.hide();
						root_wuzitype.reload();
						tree_wuzitype.expandAll();
						wuzitypeWindow.show();
					}
				}, {
					text : '取消',
					icon : "img/button/add.gif",
					handler : function() {
						Ext.getCmp("shenqingliyou").setValue("");
						mark_window.hide();
					}
				} ]
			});
			var mark_window_1 = new Ext.Window({
				id : 'mark_window_1',
				title : '申请理由',
				width : 450,
				height : 300,
				layout : 'fit',
				plain : true,
				bodyStyle : 'padding:5px;',
				buttonAlign : 'right',
				closable : false,
				items : mark_field_1,
				buttons : [{
					text : '确认',
					icon : "img/button/add.gif",
					handler : function() {
						var records = grid.getSelectionModel().getSelections();
						Ext.Ajax
						.request({
							url : 'dZJK_PersionWuZiAction!wuziShenqing',
							success : function(response) {
								var responseArray = Ext.util.JSON.decode(response.responseText);
								if (responseArray.success == 'true') {
									mark_window_1.hide();
									Ext.MessageBox
											.alert(
													'提示',
													'资产申请成功');
								}
							},
							params : {
								ids : records[0].data.wuziTypeId,
								applytype : apply_type,
								bghid : records[0].data.id,
								shenqingliyou : Ext.getCmp("shenqingliyou1").getValue()
							}
						});
					}
				}, {
					text : '取消',
					icon : "img/button/add.gif",
					handler : function() {
						Ext.getCmp("shenqingliyou1").setValue("");
						mark_window_1.hide();
					}
				} ]
			});

			/*******************************************************************
			 * 物资类型数
			 ******************************************************************/
			var root_wuzitype = new Ext.tree.AsyncTreeNode({
				text : '物资类别',
				draggable : false,
				id : '0',
				icon : "img/tree/system.png"
			});
			var tree_wuzitype = new Ext.tree.TreePanel({
				renderTo : '',
				autoScroll : true,
				root : root_wuzitype,
				animate : true,
				enableDD : false,
				checkModel : 'cascade',
				border : false,
				rootVisible : true,
				containerScroll : true,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!wuziTypeCheckBoxTree?id=' + nodeId,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					}
				})
			});
			tree_wuzitype
					.on(
							'beforeload',
							function(node) {
								nodeId = node.id;
								tree_wuzitype.loader.dataUrl = 'treeAction!wuziTypeCheckBoxTree?id='
										+ nodeId;
							});
			var baoxiuForm = new Ext.FormPanel({
				labelAlign : 'left',
				frame : true,
				title : '',
				bodyStyle : 'padding:5px 5px 0',
				width : 600,
				items : [ {
					layout : 'column',
					items : [ {
						columnWidth : 1,
						layout : 'form',
						items : [ {
							xtype : 'textfield',
							fieldLabel : '个人资产ID',
							allowBlank : false,
							disabled : true,
							name : 'id',
							id : 'id',
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '申请人',
							allowBlank : false,
							disabled : true,
							name : 'persion_id',
							id : 'persion_id',
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '物资类别名称',
							disabled : true,
							name : 'wuziTypeName',
							id : 'wuziTypeName',
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '物资名称',
							disabled : true,
							name : 'wuziName',
							id : 'wuziName',
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '物资有效期',
							name : 'youxiaoqi',
							id : 'youxiaoqi',
							disabled : true,
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '资产编号',
							disabled : true,
							name : 'wuziCode',
							id : 'wuziCode',
							allowBlank : false,
							anchor : '95%'
						}, {
							xtype : 'textfield',
							fieldLabel : '问题描述',
							disabled : false,
							name : 'baoyou_yuanyin',
							id : 'baoyou_yuanyin',
							allowBlank : true,
							blankText : '请填写问题描述',
							anchor : '95%'
						} ]
					} ]
				} ]
			});
			var baoxiuWindow = new Ext.Window(
					{
						id : 'baoxiuWindow',
						title : '报修资产明细',
						width : 450,
						height : 300,
						layout : 'fit',
						plain : true,
						bodyStyle : 'padding:5px;',
						buttonAlign : 'right',
						closable : false,
						// items: tree_wuzitype,
						items : baoxiuForm,
						buttons : [
								{
									text : '报修',
									icon : "img/button/add.gif",
									handler : function() {
										if (baoxiuForm.form.isValid()) {
											Ext.Ajax
													.request({
														url : 'dZJK_PersionWuZiAction!wuziBaoXiuShenqing',
														success : function(
																response) {
															var responseArray = Ext.util.JSON
																	.decode(response.responseText);
															if (responseArray.success == 'true') {
																store.proxy = new Ext.data.HttpProxy(
																		{
																			url : 'dZJK_PersionWuZiAction!listPersonInfoAll',
																			method : 'POST'
																		});
																store
																		.load({
																			params : {
																				start : 0,
																				limit : page
																			}
																		});
																store.reload();
																baoxiuWindow
																		.hide();
																Ext.MessageBox
																		.alert(
																				'提示',
																				'故障报修成功');
															}
														},
														params : {
															person_wuzi_id : Ext
																	.getCmp(
																			'id')
																	.getValue(),
															baoyou_yuanyin : Ext
																	.getCmp(
																			'baoyou_yuanyin')
																	.getValue(),
															status : 0
														}
													});

										}
									}
								}, {
									text : '取消',
									icon : "img/button/add.gif",
									handler : function() {
										baoxiuForm.form.reset();
										baoxiuWindow.hide();
									}
								} ]
					});

			/*******************************************************************
			 * 物资类型数
			 ******************************************************************/
			var root_wuzitype = new Ext.tree.AsyncTreeNode({
				text : '物资类别',
				draggable : false,
				id : '0',
				icon : "img/tree/system.png"
			});
			var tree_wuzitype = new Ext.tree.TreePanel({
				renderTo : '',
				autoScroll : true,
				root : root_wuzitype,
				animate : true,
				enableDD : false,
				checkModel : 'cascade',
				border : false,
				rootVisible : true,
				containerScroll : true,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'treeAction!wuziTypeCheckBoxTree?id=' + nodeId,
					baseAttrs : {
						uiProvider : Ext.ux.TreeCheckNodeUI
					}
				})
			});
			tree_wuzitype
					.on(
							'beforeload',
							function(node) {
								nodeId = node.id;
								tree_wuzitype.loader.dataUrl = 'treeAction!wuziTypeCheckBoxTree?id='
										+ nodeId;
							});

			var wuzitypeWindow = new Ext.Window(
					{
						id : 'wuzitypeWindow',
						title : '资产类别目录',
						width : 300,
						height : 450,
						layout : 'fit',
						plain : true,
						bodyStyle : 'padding:5px;',
						buttonAlign : 'right',
						closable : false,
						items : [tree_wuzitype],
						buttons : [
								{
									text : '保存',
									icon : "img/button/add.gif",
									handler : function() {
										var nodeIds = '-1';
										var checkedNodes = tree_wuzitype
												.getChecked();
										if (checkedNodes.length == 0) {
											Ext.MessageBox.alert('提示', '请选择物资');
											return;
										} else {
											for ( var i = 0; i < checkedNodes.length; i++) {
												if (checkedNodes[i].leaf) {
													nodeIds += ','
															+ checkedNodes[i].id;
												}
											}
											Ext.Ajax
													.request({
														url : 'dZJK_PersionWuZiAction!wuziShenqing',
														success : function(
																response) {
															var responseArray = Ext.util.JSON
																	.decode(response.responseText);
															if (responseArray.success == 'true') {
																wuzitypeWindow
																		.hide();
																// store.reload();
																Ext.MessageBox
																		.alert(
																				'提示',
																				'资产申请成功');
															}
														},
														params : {
															ids : nodeIds,
															applytype : 1,
															shenqingliyou : Ext.getCmp("shenqingliyou").getValue()
														}
													});
										}
									}
								}, {
									text : '取消',
									icon : "img/button/add.gif",
									handler : function() {
										wuzitypeWindow.hide();
									}
								} ]
					});

			/*******************************************************************
			 * 自定义函数相关
			 ******************************************************************/
			function reloadAddNode(node) {
				if (node == '0') {
					tree.getRootNode().reload();
				} else {
					tree.getNodeById(node).parentNode.reload();
				}
			}

			function reloadUpdateNode(node) {
				tree.getRootNode().reload();
			}
			/*******************************************************************
			 * Grid相关
			 ******************************************************************/
			var store = new Ext.data.JsonStore({
				root : 'root',
				totalProperty : 'totalProperty',
				idProperty : 'id',
				remoteSort : false,
				fields : [ {
					name : 'id',
					type : 'int'
				}, {
					name : 'persion_id',
					type : 'string'
				}, {
					name : 'wuziId',
					type : 'int'
				}, {
					name : 'wuziTypeId',
					type : 'string'
				}, {
					name : 'wuziName',
					type : 'string'
				}, {
					name : 'wuziDescr',
					type : 'string'
				}, {
					name : 'wuziTypeName',
					type : 'string'
				}, {
					name : 'wuziTypeDescr',
					type : 'string'
				}, {
					name : 'tcount',
					type : 'string'
				}, {
					name : 'youxiaoqi',
					type : 'string'
				}, {
					name : 'tipDays',
					type : 'string'
				}, {
					name : 'wuziCode',
					type : 'string'
				}, {
					name : 'type',
					type : 'string'
				} ],
				proxy : new Ext.data.HttpProxy({
					url : 'dZJK_PersionWuZiAction!listPersonInfoAll',
					method : 'POST'
				})
			});
			// store.setDefaultSort('code', 'desc');
			store.load({
				params : {
					start : 0,
					limit : page
				}
			});

			var sm = new Ext.grid.CheckboxSelectionModel();
			var grid = new Ext.grid.GridPanel(
					{
						height : height,
						width : width,
						title : '个人资产列表',
						store : store,
						trackMouseOver : true,
						disableSelection : false,
						loadMask : true,
						sm : sm,
						columns : [ sm, {
							id : 'id',
							header : "ID",
							dataIndex : 'id',
							sortable : true,
							hidden : true
						}, {
							header : "持有人",
							dataIndex : 'persion_id',
							sortable : true,
							hidden : true
						}, {
							header : "物资ID",
							dataIndex : 'wuziId',
							sortable : true,
							hidden : true
						}, {
							header : "物资名称",
							dataIndex : 'wuziName',
							sortable : true
						}, {
							header : "物资描述",
							dataIndex : 'wuziDescr',
							sortable : true,
							hidden : true
						}, {
							header : "物资类别ID",
							dataIndex : 'wuziTypeId',
							sortable : true,
							hidden : true
						}, {
							header : "物资类别名称",
							dataIndex : 'wuziTypeName',
							sortable : true
						}, {
							header : "物资类别描述",
							dataIndex : 'wuziTypeDescr',
							sortable : true,
							hidden : true
						}, {
							header : "数量",
							dataIndex : 'tcount',
							sortable : true,
							hidden : false
						}, {
							header : "有效期",
							dataIndex : 'youxiaoqi',
							sortable : true,
							hidden : true
						}, {
							header : "提醒天数",
							dataIndex : 'tipDays',
							sortable : true,
							hidden : true
						}, {
							header : "物资编号",
							dataIndex : 'wuziCode',
							sortable : true,
							hidden : true
						}, {
							header : "软硬件标志",
							dataIndex : 'type',
							sortable : true,
							hidden : false,
							renderer : function(value) {
								if (value == '1') {
									return "软件";
								}
								if (value == '2') {
									return "硬件";
								}
							}
						} ],
						viewConfig : {
							forceFit : true,
							enableRowBody : true,
							showPreview : true
						},
						tbar : [
								ts,
								{
									text : '资产报修',
									tooltip : '资产报修',
									icon : "img/button/add.gif",
									handler : function() {
										var records = grid.getSelectionModel()
												.getSelections();
										if (records.length == 0) {
											Ext.MessageBox.alert('提示',
													'请选择要报修的记录');
											return;
										} else if (records.length > 1) {
											Ext.MessageBox.alert('提示',
													'仅支持单条资产报修');
											return;
										} else {
											baoxiuForm.form.reset();
											baoxiuForm.form
													.loadRecord(records[0]);
											baoxiuWindow.show();
										}
									}
								},
								ts,
								{
									text : '其它资产报修',
									tooltip : '其它资产报修',
									icon : "img/button/add.gif",
									handler : function() {
										var records = grid.getSelectionModel()
												.getSelections();
										if (records.length != 0) {
											Ext.MessageBox.alert('提示',
													'请点击“资产报修”进行处理');
										} else {
											baoxiuForm_other.form.reset();
											baoxiuWindow_other.show();
										}
									}
								},
								// ts,
								// {
								// text : '资产报修',
								// tooltip : '资产报修',
								// icon : "img/button/add.gif",
								// handler : function() {
								// var records = grid.getSelectionModel()
								// .getSelections();
								// if (records.length == 0) {
								// Ext.MessageBox.alert('提示',
								// '请选择要报修的记录');
								// return;
								// } else if (records.length > 1) {
								// Ext.MessageBox.alert('提示',
								// '仅支持单条资产报修');
								// return;
								// } else {
								// baoxiuForm.form.reset();
								// baoxiuForm.form
								// .loadRecord(records[0]);
								// baoxiuWindow.show();
								// }
								// }
								// },
								ts,
								{
									text : '申请资产',
									tooltip : '资产申请',
									icon : "img/button/add.gif",
									handler : function() {
										Ext.getCmp("shenqingliyou").setValue("");
										mark_window.show();
									}
								},
								ts,
								{
									text : '更换申请',
									tooltip : '更换申请',
									icon : "img/button/add.gif",
									handler : function() {
										var records = grid.getSelectionModel()
												.getSelections();
										if (records.length == 0) {
											Ext.MessageBox.alert('提示',
													'请选择要更换的资产');
											return;
										} else if (records.length > 1) {
											Ext.MessageBox.alert('提示',
													'仅支持单资产更换');
											return;
										} else {
											apply_type = 2;
											Ext.getCmp("shenqingliyou1").setValue("");
											mark_window_1.show();
										}
									}
								},
								ts,
								{
									text : '退还申请',
									tooltip : '退还申请',
									icon : "img/button/add.gif",
									handler : function() {
										var records = grid.getSelectionModel()
												.getSelections();
										if (records.length == 0) {
											Ext.MessageBox.alert('提示',
													'请选择要退还的资产');
											return;
										} else if (records.length > 1) {
											Ext.MessageBox.alert('提示',
													'仅支持单资产退还');
											return;
										} else {
											apply_type = 3;
											Ext.getCmp("shenqingliyou1").setValue("");
											mark_window_1.show();
										}
									}
								} ],
						bbar : new Ext.PagingToolbar({
							store : store,
							displayInfo : true,
							displayMsg : '第 {0} - {1} 条, 总共 {2} 条',
							emptyMsg : "没有数据"
						})
					});
			grid.render('center');
			/*******************************************************************
			 * Grid相关
			 * ***********************************************************************
			 * 
			 * /***********************************************************************
			 * 布局相关
			 ******************************************************************/
			new Ext.Viewport({
				layout : 'border',
				items : [ {
					region : 'center',
					items : grid
				} ]
			});
			/*******************************************************************
			 * 布局相关
			 ******************************************************************/
		});
