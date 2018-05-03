<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../common/source.jsp"%>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/result/uploadListData?type=${type}";
			var getRecordUrl = "${ctx}/ots/taskRecordListData";
			var datagrid = "uploadTable";
			var recordDatagrid = "taskRecordTable";
			// 当前选中的任务的任务号
			var currentTaskCode = "";
			
			$(function(){
				 // 任务列表
				 $("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "420px",
					title: '任务列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        idField: 'id',
			        columns : [ [ {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'code',
						title : '任务号',
						width : '250',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'org',
						title : '录入单位',
						width : '250',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.name + "'>" + val.name + "</span>";
							}
						}
					}, {
						field : 'account',
						title : '录入用户',
						width : '200',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.nickName + "'>" + val.nickName + "</span>";
							}
						}
					},{
						field : 'createTime',
						title : '录入时间',
						width : '250',
						align : 'center',
						formatter : DateTimeFormatter
					}, {
						field : '_operation',
						title : '操作',
						width : '120',
						align : 'center',
						formatter : function(value,row,index){
							return '<a href="javascript:void(0)" onclick="uploadDetail('+ row.id +')">上传结果</a>';  	
						}
					}  ] ],
					onDblClickRow : function(rowIndex, rowData) {
						uploadDetail(rowData.id);
					},
					onClickRow: function(rowIndex, rowData) {
						currentTaskCode = rowData.code;
						getRecordList(rowData.code);
					}
				});
		
				// 分页信息
				$('#' + datagrid).datagrid('getPager').pagination({
					pageSize : "${defaultPageSize}",
					pageNumber : 1,
					displayMsg : '当前显示 {from} - {to} 条记录    共  {total} 条记录',
					onSelectPage : function(pageNumber, pageSize) {//分页触发  
						var data = {
							'code' : $("#q_code").textbox("getValue"), 
							'orgId': $("#q_org").combotree("getValue"),
							'nickName' : $("#q_nickName").textbox("getValue"), 
							'startCreateTime' : $("#q_startCreateTime").val(),
							'endCreateTime' : $("#q_endCreateTime").val(),
							'pageNum' : pageNumber,
							'pageSize' : pageSize
						}
						getData(datagrid, getDataUrl, data);
					}
				});
				
				
				// 任务记录列表
				$("#" + recordDatagrid).datagrid({
			        url : getRecordUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "420px",
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        idField: 'id',
					title: '操作记录',
			        columns : [ [ {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'code',
						title : '任务号',
						width : '250',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'account',
						title : '操作用户',
						width : '150',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.nickName + "'>" + val.nickName + "</span>";
							}
						}
					}, {
						field : 'state',
						title : '状态',
						width : '180',
						align : 'center',
						formatter : function(val){
							var str = "";
							if(val){
								if(val == 1){
									str = "基准信息录入";
								}else if(val == 2){
									str = "审核通过";
								}else if(val == 3){
									str = "审核不通过";
								}else if(val == 4){
									str = "任务下达"
								}else if(val == 5){
									str = "审批同意";
								}else if(val == 6){
									str = "审批不同意";
								}else if(val == 7){
									str = "图谱结果上传";
								}else if(val == 8){
									str = "型式结果上传";
								}else if(val == 9){
									str = "结果发送";
								}else if(val == 10){
									str = "结果确认合格";
								}else if(val == 11){
									str = "结果确认不合格";
								}else if(val == 12){
									str = "基准保存";
								}else if(val == 13){
									str = "收费通知";
								}else if(val == 12){
									str = "信息修改";
								}
								return "<span title='" + str + "'>" + str + "</span>";
							}
						}
					}, {
						field : 'remark',
						title : '备注',
						width : '350',
						align : 'center',
						formatter : formatCellTooltip
					},{
						field : 'createTime',
						title : '录入时间',
						width : '200',
						align : 'center',
						formatter : DateTimeFormatter
					}  ] ]
				});
				 
				 
				$("#" + recordDatagrid).datagrid('getPager').pagination({
					pageSize : "${recordPageSize}",
					pageNumber : 1,
					displayMsg : '当前显示 {from} - {to} 条记录    共  {total} 条记录',
					onSelectPage : function(pageNumber, pageSize) {//分页触发  
						var data = {
							'code' : currentTaskCode, 
							'pageNum' : pageNumber,
							'pageSize' : pageSize
						}
						getData(recordDatagrid, getRecordUrl, data);
					}
				});
				
			});
		
			function doSearch() {
				var data = {
					'code' : $("#q_code").textbox("getValue"), 
					'orgId': $("#q_org").combotree("getValue"),
					'nickName' : $("#q_nickName").textbox("getValue"), 
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val(),
				}
				getData(datagrid, getDataUrl, data);
			}
			
			function getRecordList(code){
				var data = {
					'code': code
				}
				getData(recordDatagrid, getRecordUrl, data);
			}
		
			function doClear() {
				$("#q_org").combotree("setValue","");
				$("#q_code").textbox('clear');
				$("#q_nickName").textbox('clear');
				$("#q_startCreateTime").val('');
				$("#q_endCreateTime").val('');
				getData(datagrid, getDataUrl, {});
			}
			
			// 关掉对话时回调
			function closeDialog(msg) {
				tipMsg(msg, function(){
					$('#uploadDetailDialog').dialog('close');
					$('#' + datagrid).datagrid('reload');
					$('#' + recordDatagrid).datagrid('reload');
				});
			}
			
			function uploadDetail(id) {
				$('#uploadDetailDialog').dialog({
					title : '结果上传信息',
					width : 900,
					height : 765,
					closed : false,
					cache : false,
					href : "${ctx}/result/uploadDetail?id=" + id + "&type=${type}",
					modal : true
				});
				$('#uploadDetailDialog').window('center');
			}
			
		</script>
	</head>
	
	<body>
		<%@include file="../common/header.jsp"%>
		
		<!--banner-->
		<div class="inbanner XSLR">
			<span style="font-size: 30px;font-weight: bold; margin-top: 70px; display: inline-block; margin-left: 80px;color: #4169E1">${menuName}</span>
		</div>
	
		<div style="margin-top: 25px; padding-left: 20px; margin-bottom: 10px;font-size:12px;margin-left: 5%;margin-right: 5%;border: 1px solid #D3D3D3">
			<div>
				<div>
					<span class="qlabel">任务号：</span>
					<input id="q_code" name="q_code" class="easyui-textbox" style="width: 138px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入单位：</span>
					<input id="q_org" name="q_org"  class="easyui-combotree" data-options="url:'${ctx}/org/tree'" style="width: 138px;">&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入用户：</span>
					<input id="q_nickName" name="q_nickName" class="easyui-textbox" style="width: 138px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					
					<span class="qlabel">录入时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;width:120px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;width:120px;display:inline-block;"/>&nbsp;&nbsp;&nbsp;&nbsp;
				
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
			</div>
			
			<div style="margin-top:10px;">
				<table id="uploadTable" style="height:auto;width:auto"></table>
			</div>
			
			<div style="margin-top:10px;">
				<table id="taskRecordTable" style="height:auto;width:auto"></table>
			</div>
			
			<div id="uploadDetailDialog"></div>
		
		</div>

		
		<style type="text/css">
			.qlabel{
				display: inline-block;
				width: 63px;
			}
		</style>
	</body>	
</html>