<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../common/source.jsp"%>
		<script src="${ctx}/resources/js/jquery.form.js"></script>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/query/getListData";
			var getRecordUrl = "${ctx}/ots/taskRecordListData";
			var datagrid = "taskTable";
			var recordDatagrid = "taskRecordTable";
			// 当前选中的任务的任务号
			var currentTaskCode = "";
			
			var toolbar = [{
				text : '导出',
				iconCls : 'icon-export',
				handler : function() {
					window.location.href = "${ctx}/query/exportTask";
				}
			}];
			
			$(function(){
				 // 任务列表
				 $("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "390px",
					title: '任务列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true, 
			        toolbar : toolbar,
			        idField: 'id',
			        frozenColumns:[[{
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
						field : 'code',
						title : '任务号',
						width : '150',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'type',
						title : '任务类型',
						width : '110',
						align : 'center',
						formatter : function(val){
							var str = "材料研究所任务"
							if(val == 1){
								str = "OTS阶段任务";
							}else if(val == 2){
								str = "PPAP阶段任务";
							}else if(val == 3){
								str = "SOP阶段任务";
							}
							return "<span title='" + str + "'>" + str + "</span>";
						}
					}, {
						field : 'state',
						title : '状态',
						width : '120',
						align : 'center',
						formatter : function(value,row,index){
							var str = "";
							if(row.type == 1 || row.type == 4){
								if(row.state == 1){
									str = "审核中";
								}else if(row.state == 2){
									str = "审核不通过";
								}else if(row.state == 3){
									str = "试验中";
								}else if(row.state == 4){
									str = "完成";
								}else if(row.state == 5){
									str = "申请修改";
								}else {
									str = "申请不通过";
								}
							}else if(row.type == 2 || row.type == 3){
								if(row.state == 1){
									str = "审批中";
								}else if(row.state == 2){
									str = "审批不通过";
								}else if(row.state == 3){
									str = "结果上传中";
								}else if(row.state == 4){
									str = "结果比对中";
								}else if(row.state == 5){
									str = "结果发送中";
								}else if(row.state == 6){
									str = "结果接收中";
								}else if(row.state == 7){
									str = "完成";
								}else if(row.state == 8){
									str = "申请修改";
								}else if(row.state == 9){
									str = "申请不通过";
								}else if(row.state == 10){
									str = "等待是否二次抽样";
								}else {
									str = "中止任务";
								}
							}
							return "<span title='" + str + "'>" + str + "</span>";
						}
					}, {
						field : 'isReceive',
						title : '是否接收',
						width : '80',
						align : 'center',
						formatter : function(val){
							var str = "";
							var color = "red";
							if(val == 1){
								str = "接收";
								color = "green";
							}else if(val == 2){
								str = "不接收";
							}
							return "<span title='" + str + "' style='color:"+ color +"'>" + str + "</span>";
						}
					}, {
						field : 'result',
						title : '结果',
						width : '80',
						align : 'center',
						formatter : function(val){
							var str = "";
							var color = "red";
							if(val == 1){
								str = "合格";
								color = "green";
							}else if(val == 2){
								str = "不合格";
							}
							return "<span title='" + str + "' style='color:"+ color +"'>" + str + "</span>";
						}
					}]],
			        columns : [ [ {
						field : 'info.vehicle.type',
						title : '车型',
						width : '80',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var vehicle = row.info.vehicle;
								if(!isNull(vehicle)){
									return "<span title='"+ vehicle.type +"'>"+ vehicle.type +"</span>";
								}	
							}
						}
					}, {
						field : 'info.parts.code',
						title : '零件号',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var parts = row.info.parts;
								if(!isNull(parts)){
									return "<span title='"+ parts.code +"'>"+ parts.code +"</span>";
								}
							}
						}
					}, {
						field : 'info.parts.name',
						title : '零件名称',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var parts = row.info.parts;
								if(!isNull(parts)){
									return "<span title='"+ parts.name +"'>"+ parts.name +"</span>";
								}
							}
						}
					}, {
						field : 'info.parts.org',
						title : '生产商',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var parts = row.info.parts;
								if(!isNull(parts)){
									var org = row.info.parts.org;
									if(!isNull(org)){
										return "<span title='"+ org.name +"'>"+ org.name +"</span>";
									}
								}	
							}
						}
					}, {
						field : 'info.material.name',
						title : '材料名称',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var material = row.info.material;
								if(!isNull(material)){
									return "<span title='"+ material.matName +"'>"+ material.matName +"</span>";
								}
							}
						}
					}, {
						field : 'info.material.org',
						title : '生产商',
						width : '100',
						align : 'center',
						formatter : function(value, row, index){
							if(!isNull(row.info)){
								var org = row.info.material.org;
								if(!isNull(org)){
									return "<span title='"+ org.name +"'>"+ org.name +"</span>";
								}	
							}
						}
					}, {
						field : 'account',
						title : '录入用户',
						width : '120',
						align : 'center',
						formatter : function(val){
							if(val){
								return "<span title='" + val.nickName + "'>" + val.nickName + "</span>";
							}
						}
					},{
						field : 'createTime',
						title : '录入时间',
						width : '130',
						align : 'center',
						formatter : DateTimeFormatter
					},{
						field : 'confirmTime',
						title : '完成时间',
						width : '130',
						align : 'center',
						formatter : DateTimeFormatter
					}  ] ],
					onDblClickRow : function(rowIndex, rowData) {
						detail(rowData.id);
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
							'taskType': $("#q_taskType").textbox("getValue"),
							'nickName' : $("#q_nickName").textbox("getValue"), 
							'startCreateTime' : $("#q_startCreateTime").val(),
							'endCreateTime' : $("#q_endCreateTime").val(),
							'startConfirmTime' : $("#q_startConfirmTime").val(),
							'endConfirmTime' : $("#q_endConfirmTime").val(),
							'parts_code': $("#parts_code").textbox("getValue"),
							'parts_name': $("#parts_name").textbox("getValue"),
							'matName': $("#matName").textbox("getValue"),
							'vehicle_type': $("#vehicle_type").textbox("getValue"),
							'parts_org': $("#parts_org").combotree("getValue"),
							'mat_org': $("#mat_org").combotree("getValue"),
							'applicant': $("#applicant").textbox("getValue"),
							'department': $("#department").textbox("getValue"),
							'reason': $("#reason").textbox("getValue"),
							'provenance': $("#provenance").textbox("getValue"),
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
			        height: "360px",
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        idField: 'id',
					title: '操作记录',
					nowrap: false,   // 自动换行
			        columns : [ [ {
			            field : 'id', 
			            hidden: 'true'
			        }, {
						field : 'code',
						title : '任务号',
						width : '180',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'account',
						title : '操作用户',
						width : '120',
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
						width : '350',
						align : 'left',
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
			});
		
			function doSearch() {
				var data = {
					'code' : $("#q_code").textbox("getValue"), 
					'nickName' : $("#q_nickName").textbox("getValue"), 
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val(),
					'startConfirmTime' : $("#q_startConfirmTime").val(),
					'endConfirmTime' : $("#q_endConfirmTime").val(),
					'taskType': $("#q_taskType").combobox("getValue"),
					'parts_code': $("#parts_code").textbox("getValue"),
					'parts_name': $("#parts_name").textbox("getValue"),
					'matName': $("#matName").textbox("getValue"),
					'vehicle_type': $("#vehicle_type").textbox("getValue"),
					'parts_org': $("#parts_org").combotree("getValue"),
					'mat_org': $("#mat_org").combotree("getValue"),
					'applicant': $("#applicant").textbox("getValue"),
					'department': $("#department").textbox("getValue"),
					'reason': $("#reason").textbox("getValue"),
					'provenance': $("#provenance").textbox("getValue")
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
				$("#q_code").textbox('clear');
				$("#q_nickName").textbox('clear');
				$("#q_startCreateTime").val('');
				$("#q_endCreateTime").val('');
				$("#q_startConfirmTime").val('');
				$("#q_endConfirmTime").val('');
				$("#q_taskType").combobox("select", "");
				$("#parts_code").textbox("clear");
				$("#parts_name").textbox("clear");
				$("#matName").textbox("clear");
				$("#vehicle_type").textbox("clear");
				$("#parts_org").combotree("setValue","");
				$("#mat_org").combotree("setValue","");
				$("#applicant").textbox("clear");
				$("#department").textbox("clear");
				$("#reason").textbox("clear");
				$("#provenance").textbox("clear");
				
				getData(datagrid, getDataUrl, {});
			}
			
			// 关掉对话时回调
			function closeDialog(msg) {
				$('#detailDialog').dialog('close');
				tipMsg(msg, function(){
					$('#' + datagrid).datagrid('reload');
					$('#' + recordDatagrid).datagrid('reload');
				});
			}
			
			function detail(id) {
				$('#detailDialog').dialog({
					title : '任务详情',
					width : 1200,
					height : 660,
					closed : false,
					cache : false,
					href : "${ctx}/query/detail?id=" + id,
					modal : true
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
					<input id="q_code" name="q_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">任务类型：</span>
					<select id="q_taskType" name="q_taskType" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
						<option value="">全部</option>
						<option value="1">车型OTS阶段任务</option>
						<option value="2">车型PPAP阶段任务</option>
						<option value="3">车型SOP阶段任务</option>
						<option value="4">非车型材料任务</option>
					</select> &nbsp;&nbsp;&nbsp;&nbsp;
					
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
					
					<span class="qlabel">零件号：</span>
					<input id="parts_code" name="parts_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">零件名称：</span>
					<input id="parts_name" name="parts_name" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				
				<div style="margin-top: 5px;">
					<span class="qlabel">零件生产商：</span>
					<input id="parts_org" name="parts_org"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
					<span class="qlabel">录入时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;width:110px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;width:110px;display:inline-block;"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">完成时间：</span>
					<input type="text" id="q_startConfirmTime" name="q_startConfirmTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endConfirmTime\')}'})" class="textbox" style="line-height: 23px;width:110px;display:inline-block"/> - 
					<input type="text" id="q_endConfirmTime" name="q_endConfirmTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startConfirmTime\')}'})" class="textbox"  style="line-height: 23px;width:110px;display:inline-block;"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				
				<div style="margin-top: 5px;">
					<span class="qlabel">申请人：</span>
					<input id="applicant" name="applicant" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">科室：</span>
					<input id="department" name="department" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">抽检原因：</span>
					<input id="reason" name="reason" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">费用出处：</span>
					<input id="provenance" name="provenance" class="easyui-textbox" style="width: 168px;">
				</div>
				
				<div style="margin-top: 5px;text-align:right;">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
			</div>
			
			<div style="margin-top:10px;">
				<table id="taskTable" style="height:auto;width:auto"></table>
			</div>
			
			<div style="margin-top:10px;">
				<table id="taskRecordTable" style="height:auto;width:auto"></table>
			</div>
			
			<div id="detailDialog"></div>
		
		</div>

		<!-- footer -->
		<%@include file="../common/footer.jsp"%>
		
		<style type="text/css">
			.qlabel{
				display: inline-block;
				width: 75px;
			}
		</style>
	</body>	
</html>