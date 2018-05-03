<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../common/source.jsp"%>
		<script src="${ctx}/resources/js/jquery.form.js"></script>
		
		<style type="text/css">
			.qlabel{
				display: inline-block;
				width: 70px;
			}
		</style>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/message/getListData";
			var datagrid = "messageTable";
			
			$(function(){
				 // 任务列表
				 $("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "500px",
					title: '消息列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true, 
			        nowrap: false,   // 自动换行
			        idField: 'id',
			        columns : [ [ {
						field : '_operation',
						title : '操作',
						width : '80',
						align : 'center',
						formatter : function(value,row,index){
							return '<a href="javascript:void(0)" onclick="detail('+ row.id +')">详情</a>';  	
						}
					}, {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'task',
						title : '任务号',
						width : '150',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.code + "'>" + val.code + "</span>";
							}	
						}
					}, {
						field : 'subject',
						title : '主题',
						width : '110',
						align : 'center',
						formatter: formatCellTooltip
					}, {
						field : 'content',
						title : '内容',
						width : '320',
						align : 'left',
						formatter : function(value,row,index){
							if(row.type != 2){
								return "<span title='" + row.content + "'>" +  row.content  + "</span>";
							}
						}
					}, {
						field : 'type',
						title : '类型',
						width : '80',
						align : 'center',
						formatter : function(val){
							var str = "";
							if(val == 1){
								str = "结果消息";
							}else if(val == 2){
								str = "收费通知";
							}else{
								str = "警告书";
							}
							return "<span title='" + str + "'>" + str + "</span>";
						}
					}, {
						field : 'state',
						title : '状态',
						width : '80',
						align : 'center',
						formatter : function(val){
							var str = "";
							var color = "red";
							if(val == 1){
								str = "未读";
							}else{
								str = "已读";
								color = "green";
							}
							return "<span title='" + str + "' style='color: "+ color +"'>" + str + "</span>";
						}
					},{
						field : 'account',
						title : '发送人',
						width : '120',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.userName + "'>" + val.userName + "</span>";
							}
						}
					},{
						field : 'createTime',
						title : '发送时间',
						width : '130',
						align : 'center',
						formatter : DateTimeFormatter
					}  ] ],
					onDblClickRow : function(rowIndex, rowData) {
						detail(rowData.id);
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
							'type': $("#q_type").combotree("getValue"),
							'state': $("#q_state").combobox("getValue"),
							'startCreateTime' : $("#q_startCreateTime").val(),
							'endCreateTime' : $("#q_endCreateTime").val(),
							'pageNum' : pageNumber,
							'pageSize' : pageSize
						}
						getData(datagrid, getDataUrl, data);
					}
				});
				
			});
		
			function doSearch() {
				var data = {
					'code' : $("#q_code").textbox("getValue"), 
					'type': $("#q_type").combotree("getValue"),
					'state': $("#q_state").combobox("getValue"),
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val()
				}
				getData(datagrid, getDataUrl, data);
			}
			
			function doClear() {
				$("#q_code").textbox('clear');
				$("#q_startCreateTime").val('');
				$("#q_endCreateTime").val('');
				$("#q_type").combobox("select", "");
				$("#q_state").combobox("select", "");
				getData(datagrid, getDataUrl, {});
			}
			
			function detail(id) {
				$('#detailDialog').dialog({
					title : '消息详情',
					width : 700,
					height : 400,
					closed : false,
					cache : false,
					href : "${ctx}/message/detail?id=" + id,
					modal : true,
					onClose: function(){
						doSearch();
					}
				});
				$('#detailDialog').window('center');
			}
			
		</script>
	</head>
	
	<body>
		<%@include file="../common/header.jsp"%>
		
		<!--banner-->
		<div class="inbanner XSLR">
			<span style="font-size: 30px;font-weight: bold; margin-top: 70px; display: inline-block; margin-left: 80px;color: #4169E1">${menuName}</span>
		</div>
	
		<div style="margin-top: 25px; padding-left: 20px; margin-bottom: 10px;font-size:12px;margin-left: 5%;margin-right: 5%;">
			<div>
				<div>
					<span class="qlabel">任务号：</span>
					<input id="q_code" name="q_code" class="easyui-textbox" style="width: 120px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">消息类型：</span>
					<select id="q_type" name="q_type" style="width:120px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
						<option value="">全部</option>
						<option value="1">结果消息</option>
						<option value="2">收费通知</option>
						<option value="3">警告书</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">消息状态：</span>
					<select id="q_state" name="q_state" style="width:120px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
						<option value="">全部</option>
						<option value="1">未读</option>
						<option value="2">已读</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;width:110px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;width:110px;display:inline-block;"/>
				
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
			</div>
			
			<div style="margin-top:10px;">
				<table id="messageTable" style="width:auto"></table>
			</div>
			
			<div id="detailDialog"></div>
		
		</div>

		<!-- footer -->
		<%@include file="../common/footer.jsp"%>
	</body>	
</html>