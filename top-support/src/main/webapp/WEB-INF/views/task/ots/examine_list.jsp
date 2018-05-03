<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../../common/source.jsp"%>
		<script src="${ctx}/resources/js/jquery.form.js"></script>
		
		<style type="text/css">			
			.qlabel{
				display: inline-block;
				width: 75px;
			}
		</style>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/ots/examineListData?taskType=${taskType}";
			var datagrid = "examineTable";
			
			var recordDatagrid = "taskRecordTable";
			var getRecordUrl = "${ctx}/ots/taskRecordListData";
			// 当前选中的任务的任务号
			var currentTaskCode = "";
			// 是否提交中
			var saving = false;
			
			var toolbar = [{
				text : '通过',
				iconCls : 'icon-ok',
				handler : function() {
					var result = getSelectedIds();
					if(!result){
						return false;
					}
					batchExamine(result, 1, "");
				}
			},{
				text : '不通过',
				iconCls : 'icon-cancel',
				handler : function() {
					var result = getSelectedIds();
					if(!result){
						return false;
					}
					$("#batchRemark").textbox("setValue", "");
					$("#seasonDialog").dialog("open");
					$('#seasonDialog').window('center');
				}
			}];
			
			$(function(){
				 $("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : false, /*是否选中一行*/
			        checkOnSelect: false, // 只有点击checkbox才会选中
			        width:'auto', 	
			        height: "375px",
					title: '任务列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        toolbar : toolbar,
			        idField: 'id',
			        columns : [ [ {
			        	field:'ck',
			        	checkbox:true 
			        },  {
						field : '_operation',
						title : '操作',
						width : '40',
						align : 'center',
						formatter : function(value,row,index){
							return '<a href="javascript:void(0)" onclick="examineDetail('+ row.id +')">审核</a>';  	
						}
					}, {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'code',
						title : '任务号',
						width : '150',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'info.vehicle.type',
						title : '车型',
						width : '80',
						align : 'center',
						formatter : function(value, row, index){
							var vehicle = row.info.vehicle;
							if(!isNull(vehicle)){
								return "<span title='"+ vehicle.type +"'>"+ vehicle.type +"</span>";
							}							
						}
					}, {
						field : 'info.parts.code',
						title : '零件号',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							var parts = row.info.parts;
							if(!isNull(parts)){
								return "<span title='"+ parts.code +"'>"+ parts.code +"</span>";
							}							
						}
					}, {
						field : 'info.parts.name',
						title : '零件名称',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							var parts = row.info.parts;
							if(!isNull(parts)){
								return "<span title='"+ parts.name +"'>"+ parts.name +"</span>";
							}							
						}
					}, {
						field : 'info.parts.org',
						title : '生产商',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							var parts = row.info.parts;
							if(!isNull(parts)){
								var org = row.info.parts.org;
								if(!isNull(org)){
									return "<span title='"+ org.name +"'>"+ org.name +"</span>";
								}
							}							
						}
					}, {
						field : 'info.material.name',
						title : '材料名称',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							var material = row.info.material;
							if(!isNull(material)){
								return "<span title='"+ material.matName +"'>"+ material.matName +"</span>";
							}							
						}
					}, {
						field : 'info.material.org',
						title : '生产商',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							var org = row.info.material.org;
							if(!isNull(org)){
								return "<span title='"+ org.name +"'>"+ org.name +"</span>";
							}							
						}
					}, {
						field : 'org',
						title : '录入单位',
						width : '100',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.name + "'>" + val.name + "</span>";
							}
						}
					}, {
						field : 'account',
						title : '录入用户',
						width : '80',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.nickName + "'>" + val.nickName + "</span>";
							}
						}
					},{
						field : 'createTime',
						title : '录入时间',
						width : '125',
						align : 'center',
						formatter : DateTimeFormatter
					} ] ],
					onDblClickRow : function(rowIndex, rowData) {
						examineDetail(rowData.id);
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
							'parts_code': $("#parts_code").textbox("getValue"),
							'parts_name': $("#parts_name").textbox("getValue"),
							'matName': $("#matName").textbox("getValue"),
							'vehicle_type': $("#vehicle_type").textbox("getValue"),
							'parts_org': $("#parts_org").combotree("getValue"),
							'mat_org': $("#mat_org").combotree("getValue"),
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
			        height: "370px",
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
						width : '220',
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
						formatter : function(value,row,index){
							var str = getTaskRecordState(row.taskType, row.state);
							return "<span title='" + str + "'>" + str + "</span>";
						}
					}, {
						field : 'remark',
						title : '备注',
						width : '300',
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
				
				$('#parts_org').combotree({
					url: '${ctx}/org/getTreeByType?type=2',
					multiple: false,
					animate: true,
					width: '163px'
				});
				
				// 只有最底层才能选择
				var pOrgTree = $('#parts_org').combotree('tree');	
				pOrgTree.tree({
				   onBeforeSelect: function(node){
					   if(isNull(node.children)){
							return true;
					   }else{
						   return false;
					   }
				   }
				});
				
				$('#mat_org').combotree({
					url: '${ctx}/org/getTreeByType?type=2',
					multiple: false,
					animate: true,
					width: '163px'
				});
				
				// 只有最底层才能选择
				var mOrgTree = $('#mat_org').combotree('tree');	
				mOrgTree.tree({
				   onBeforeSelect: function(node){
					   if(isNull(node.children)){
							return true;
					   }else{
						   return false;
					   }
				   }
				});
				
				if("${taskType}" == 4){
					// 隐藏列
					var dg = $("#" + datagrid);
					dg.datagrid('hideColumn', 'info.parts.name'); 
					dg.datagrid('hideColumn', 'info.parts.org'); 
					dg.datagrid('hideColumn', 'info.parts.code'); 
					
					// 修改列宽度 
					dg.datagrid('getColumnOption', 'code').width = 180;
					dg.datagrid('getColumnOption', 'org').width = 140;
					dg.datagrid('getColumnOption', 'info.vehicle.type').width = 140;
					dg.datagrid('getColumnOption', 'info.material.name').width = 140;
					dg.datagrid('getColumnOption', 'info.material.org').width = 140;
					dg.datagrid('getColumnOption', 'account').width = 100;
					dg.datagrid();
				}
			});
		
			function doSearch() {
				var data = {
					'code' : $("#q_code").textbox("getValue"), 
					'orgId': $("#q_org").combotree("getValue"),
					'nickName' : $("#q_nickName").textbox("getValue"), 
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val(),
					'parts_code': $("#parts_code").textbox("getValue"),
					'parts_name': $("#parts_name").textbox("getValue"),
					'matName': $("#matName").textbox("getValue"),
					'vehicle_type': $("#vehicle_type").textbox("getValue"),
					'parts_org': $("#parts_org").combotree("getValue"),
					'mat_org': $("#mat_org").combotree("getValue")
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
				$("#parts_code").textbox("clear");
				$("#parts_name").textbox("clear");
				$("#matName").textbox("clear");
				$("#vehicle_type").textbox("clear");
				$("#parts_org").combotree("setValue","");
				$("#mat_org").combotree("setValue","");
				getData(datagrid, getDataUrl, {});
			}
			
			// 关掉对话时回调
			function closeDialog(msg) {
				tipMsg(msg, function(){
					$('#examineDetailDialog').dialog('close');
					$('#' + datagrid).datagrid('reload');
					$('#' + recordDatagrid).datagrid('reload');
				});
			}
			
			function examineDetail(id) {
				$('#examineDetailDialog').dialog({
					title : '审核信息',
					width : 1100,
					height : 690,
					closed : false,
					cache : false,
					href : "${ctx}/ots/examineDetail?taskType=${taskType}&id=" + id,
					modal : true
				});
				$('#examineDetailDialog').window('center');
			}
			
			function getSelectedIds(){
				var rows =  $("#" + datagrid).datagrid('getChecked');
				if(!isNull(rows)){
					var ids = [];
					for(var i = 0; i < rows.length; i++){
						ids.push(rows[i].id);
					}
					return ids;
				}else{
					errorMsg("请选择要审核的任务");
					return false;
				}
			}
			
			function batchExamine(ids, type, remark){
				if(saving){
					return false;
				}
				saving = true;
				
				$.ajax({
					url: "${ctx}/ots/batchExamine",
					data:{
						"ids": ids,
						"type": type,
						"remark": remark
					},
					success: function(data){
						if(data.success){
							tipMsg(data.msg, function(){
								$('#' + datagrid).datagrid('reload');
								$('#' + recordDatagrid).datagrid('reload');
							});
						}else{
							errorMsg(data.msg);
						}
						saving = false;
					}
				});
			}
			
			function doBatchSubmit(){
				var remark = $("#batchRemark").textbox("getValue");
				if(isNull(remark)){
					errorMsg("请输入原因");
					$("#batchRemark").next('span').find('input').focus();
					return false;
				}
				
				batchExamine(getSelectedIds(), 2, remark);
				$("#seasonDialog").dialog("close");
			}
			
		</script>
	</head>
	
	<body>
		<div style="margin-top: 25px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
			<div>
				<div>
					<span class="qlabel">任务号：</span>
					<input id="q_code" name="q_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入单位：</span>
					<input id="q_org" name="q_org"  class="easyui-combotree" data-options="url:'${ctx}/org/tree'" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入用户：</span>
					<input id="q_nickName" name="q_nickName" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">车型：</span>
					<input id="vehicle_type" name="vehicle_type" class="easyui-textbox" style="width: 168px;">
				</div>
				
				<div style="margin-top: 5px;">
					<span class="qlabel">材料生产商：</span>
					<input id="mat_org" name="mat_org"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">材料名称：</span>
					<input id="matName" name="matName" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">录入时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;width:110px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;width:110px;display:inline-block;margin-right: 40px;"/>
					
					<c:if test="${taskType == 4}">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
					</c:if>
				</div>
				
				<div style="margin-top: 5px;<c:if test="${taskType == 4}">display:none;</c:if>">
					<span class="qlabel">零件号：</span>
					<input id="parts_code" name="parts_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">零件名称：</span>
					<input id="parts_name" name="parts_name" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">零件生产商：</span>
					<input id="parts_org" name="parts_org"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
				
			</div>
		</div>
	
		<div style="margin-top:10px;">
			<table id="examineTable" style="height:auto;width:auto"></table>
		</div>
		
		<div style="margin-top:10px;">
			<table id="taskRecordTable" style="height:auto;width:auto"></table>
		</div>
		
		<div id="examineDetailDialog"></div>
		
		<div id="seasonDialog" class="easyui-dialog" title="审核不通过" style="width: 400px; height: 200px; padding: 10px" closed="true" data-options="modal:true">
			<input id="batchRemark" class="easyui-textbox" label="不通过原因：" labelPosition="top" multiline="true" style="width: 350px;height: 100px;"/>
			
			<div align=center style="margin-top: 15px;">
				<a href="javascript:void(0);"  onclick="doBatchSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
				<a href="javascript:void(0);"  onclick="$('#seasonDialog').dialog('close')" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
		
	</body>	
</html>