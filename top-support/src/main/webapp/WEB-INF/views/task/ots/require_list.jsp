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
			var getDataUrl = "${ctx}/ots/requireListData?taskType=${taskType}";
			var datagrid = "requireTable";
		
			var toolbar = [{
	           text:'新增',
	           iconCls:'icon-add',
	           handler: function(){
	        	   detail(null);
	           }
		    }];
			
			$(function(){
				$("#" + datagrid).datagrid({
			        url : getDataUrl,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 	
			        height: "420px",
					title: '申请列表',
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        toolbar : toolbar,
			        idField: 'id',
			        columns : [ [ {
						field : '_operation',
						title : '操作',
						width : '50',
						align : 'center',
						formatter : function(value,row,index){
							return '<a href="javascript:void(0)" onclick="detail('+ row.id +')">编辑</a>';  	
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
					},{
						field : 'account',
						title : '申请人',
						width : '80',
						align : 'center',
						formatter : function(value){
							if(!isNull(value)){
								return "<span title='"+ value.userName +"'>"+ value.userName +"</span>";
							}
						}
					},{
						field : 'createTime',
						title : '申请时间',
						width : '140',
						align : 'center',
						formatter : DateTimeFormatter
					},{
						field : 'state',
						title : '状态',
						width : '90',
						align : 'center',
						formatter : function(val){
							if(val == 1){
								return "<span title='等待审核'>等待审核</span>";
							}else if(val == 2){
								return "<span style='color:red;' title='审核不通过'>审核不通过</span>";
							}
						}
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
							'state': $("#q_state").combobox('getValue'),
							'startCreateTime' : $("#q_startCreateTime").val(),
							'endCreateTime' : $("#q_endCreateTime").val(),
							'task_code': $("#task_code").textbox("getValue"),
							'parts_code': $("#parts_code").textbox("getValue"),
							'parts_name': $("#parts_name").textbox("getValue"),
							'req_name': $("#req_name").textbox("getValue"),
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
					var dg = $("#" + datagrid);
					// 隐藏列
					dg.datagrid('hideColumn', 'info.parts.name'); 
					dg.datagrid('hideColumn', 'info.parts.org'); 
					dg.datagrid('hideColumn', 'info.parts.code'); 
					
					// 修改列宽度 
					dg.datagrid('getColumnOption', 'code').width = 180;
					dg.datagrid('getColumnOption', 'state').width = 110;
					dg.datagrid('getColumnOption', 'info.vehicle.type').width = 140;
					dg.datagrid('getColumnOption', 'info.material.name').width = 160;
					dg.datagrid('getColumnOption', 'info.material.org').width = 160;
					dg.datagrid('getColumnOption', 'account').width = 100;
					dg.datagrid('getColumnOption', 'createTime').width = 160;
					dg.datagrid();
				}
			});
	
			function doSearch() {
				var data = {
					'state' : $("#q_state").combobox("getValue"), 
					'startCreateTime' : $("#q_startCreateTime").val(),
					'endCreateTime' : $("#q_endCreateTime").val(),
					'task_code': $("#task_code").textbox("getValue"),
					'parts_code': $("#parts_code").textbox("getValue"),
					'parts_name': $("#parts_name").textbox("getValue"),
					'req_name': $("#req_name").textbox("getValue"),
					'matName': $("#matName").textbox("getValue"),
					'vehicle_type': $("#vehicle_type").textbox("getValue"),
					'parts_org': $("#parts_org").combotree("getValue"),
					'mat_org': $("#mat_org").combotree("getValue")
				}
				getData(datagrid, getDataUrl, data);
			}
			
			function doClear() {
				$("#q_state").combobox('select', "");
				$("#q_startCreateTime").val('');
				$("#q_endCreateTime").val('');
				$("#task_code").textbox("clear");
				$("#parts_code").textbox("clear");
				$("#parts_name").textbox("clear");
				$("#req_name").textbox("clear");
				$("#matName").textbox("clear");
				$("#vehicle_type").textbox("clear");
				$("#parts_org").combotree("setValue","");
				$("#mat_org").combotree("setValue","");
				getData(datagrid, getDataUrl, {});
			}
			
			function detail(id) {
				var url = "${ctx}/ots/requireDetail?taskType=${taskType}";
				if(!isNull(id)){
					url += "&id=" + id;
				}
				
				$('#requireDialog').dialog({
					title : '详情',
					width : 1100,
					height : 625,
					closed : false,
					cache : false,
					href : url,
					modal : true,
					onClose: function(){
						window.location.reload();
					}
				});
				$('#requireDialog').window('center');
			}
			
		
		</script>
	</head>
	
	<body>
		<div style="margin-top: 25px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
			<div>
				<div>
					<span class="qlabel">任务号：</span>
					<input id="task_code" name="task_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				
					<span class="qlabel">状态：</span>
					<select id="q_state" name="q_state" class="easyui-combobox" data-options="panelHeight: 'auto'" style="width:168px;">
						<option value="">全部</option>
						<option value="0">不通过</option>
						<option value="1">通过</option>
					</select> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">申请时间：</span>
					<input type="text" id="q_startCreateTime" name="q_startCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endCreateTime\')}'})" class="textbox" style="line-height: 23px;display:inline-block"/> - 
					<input type="text" id="q_endCreateTime" name="q_endCreateTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startCreateTime\')}'})" class="textbox"  style="line-height: 23px;display:inline-block;"/>
				</div>
				
				<div style="margin-top: 5px;">
					<span class="qlabel">车型：</span>
					<input id="vehicle_type" name="vehicle_type" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">材料名称：</span>
					<input id="matName" name="matName" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">材料生产商：</span>
					<input id="mat_org" name="mat_org"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">申请人：</span>
					<input id="req_name" name="req_name" class="easyui-textbox" style="width: 168px;">
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
				
				<c:if test="${taskType == 4}">
					<div style="margin-top: 5px;">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
					</div>
				</c:if>
			</div>
		</div>
		
		
		<div style="margin-top:10px;">
			<table id="requireTable" style="height:auto;width:auto"></table>
		</div>
		
		<div id="requireDialog"></div>
	</body>
</html>