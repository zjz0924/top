<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../common/source.jsp"%>
		
		<style type="text/css">			
			.qlabel{
				display: inline-block;
				width: 63px;
			}
		</style>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/apply/applyListData";
			var datagrid = "applyTable";
			
			$(function(){
				 $("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "360px",
					title: '申请列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        idField: 'id',
			        columns : [ [ {
						field : '_operation',
						title : '操作',
						width : '120',
						align : 'center',
						formatter : function(value,row,index){
							return '<a href="javascript:void(0)" onclick="applyDetail('+ row.id +')">终止</a>';  	
						}
					}, {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'task',
						title : '任务号',
						width : '250',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.code + "'>" + val.code + "</span>";
							}
						}
							
					}, {
						field : 'type',
						title : '类型',
						width : '150',
						align : 'center',
						formatter : function(val){
							var str = "";
							if(val == 1){
								str = "信息修改";
							}else{
								str = "试验结果修改";
							}
							return "<span title='" + str + "'>" + str + "</span>";
						}
					},{
						field : 'createTime',
						title : '申请时间',
						width : '220',
						align : 'center',
						formatter : DateTimeFormatter
					},{
						field : 'state',
						title : '状态',
						width : '220',
						align : 'center',
						formatter : function(val){
							var str = "";
							if(val == 0){
								str = "待审批";
							}else if(val == 1){
								str = "通过";
							}else if(val == 2){
								str = "不通过";
							}else{
								str = "取消"
							}
							return "<span title='" + str + "'>" + str + "</span>";
						}
					}  ] ],
					onDblClickRow : function(rowIndex, rowData) {
						applyDetail(rowData.id);
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
							'startCreateTime' : $("#q_startCreateTime").val(),
							'endCreateTime' : $("#q_endCreateTime").val(),
							'type': $("#q_type").combobox("getValue"),
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
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val(),
					'type': $("#q_type").combobox("getValue")
				}
				getData(datagrid, getDataUrl, data);
			}
		
			function doClear() {
				$("#q_type").combobox("select","");
				$("#q_startCreateTime").val('');
				$("#q_endCreateTime").val('');
				$("#q_code").textbox("setValue", "");
				getData(datagrid, getDataUrl, {});
			}
			
			// 关掉对话时回调
			function closeDialog(msg) {
				$('#applyDetailDialog').dialog('close');
				tipMsg(msg, function(){
					$('#' + datagrid).datagrid('reload');
				});
			}
			
			function applyDetail(id) {
				$('#applyDetailDialog').dialog({
					title : '申请信息',
					width : 1100,
					height : 700,
					closed : false,
					cache : false,
					href : "${ctx}/apply/applyDetail?id=" + id,
					modal : true
				});
				$('#applyDetailDialog').window('center');
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
					<input id="q_code" name="q_code" class="easyui-textbox" style="width: 138px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">申请类型：</span>
					<select id="q_type" name="q_type" style="width:140px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
						<option value="">全部</option>
						<option value="1">信息修改</option>
						<option value="2">试验结果修改</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp;
					
					
					<span class="qlabel">申请时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;width:120px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;width:120px;display:inline-block;"/>&nbsp;&nbsp;&nbsp;&nbsp;
				
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
			</div>
			
			<div style="margin-top:10px;">
				<table id="applyTable" style="height:auto;width:auto"></table>
			</div>
			
			<div id="applyDetailDialog"></div>
			
		</div>
		
		<!-- footer -->
		<%@include file="../common/footer.jsp"%>
		
	</body>	
</html>