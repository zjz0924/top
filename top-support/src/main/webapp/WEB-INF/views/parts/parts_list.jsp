<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>


<body>
	<div style="margin-top: 15px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
		<div>
			<div>
				<span class="qlabel">零件号：</span>
				<input id="p_q_code" name="p_q_code" class="easyui-textbox" style="width: 140px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				
				<span class="qlabel">名称：</span>
				<input id="p_q_name" name="p_q_name" class="easyui-textbox" style="width: 140px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				
				<span class="qlabel">生产时间：</span>
				<input type="text" id="p_q_startProTime" name="p_q_startProTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'p_q_endProTime\')}'})" class="textbox" style="line-height: 23px;width:140px;display:inline-block"/> - 
			   	<input type="text" id="p_q_endProTime" name="p_q_endProTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'p_q_startProTime\')}'})" class="textbox"  style="line-height: 23px;width:140px;display:inline-block;margin-right:60px;"/>
			</div>
			
			<div style="margin-top:10px;">
				 <span class="qlabel">关键零件：</span>
				<select id="p_q_isKey" name="p_q_isKey" style="width:140px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
					<option value="">全部</option>
					<option value="0">否</option>
					<option value="1">是</option>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
				<span class="qlabel">生产批次：</span>
				<input id="p_q_proNo" name="p_q_proNo" class="easyui-textbox" style="width: 140px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				
				<span class="qlabel">生产商：</span>
				<input id="p_q_orgId" name="p_q_orgId">
			</div>
			
			<div style="margin-top:10px;">
				<span class="qlabel">零件型号：</span>
				<input id="p_q_keyCode" name="p_q_keyCode" class="easyui-textbox" style="width: 140px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="p_doSearch()">查询</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
			</div>
		</div>
	</div>
			
	<div style="margin-top:10px;">
		<table id="partsTable" style="width:auto"></table>
	</div>
	
	<script type="text/javascript">
		var getDataUrl = "${ctx}/parts/getList";
		var detailUrl = "${ctx}/parts/detail";
		var datagrid = "partsTable";
		
		$(function(){
			$('#p_q_orgId').combotree({
				url: '${ctx}/org/getTreeByType?type=2',
				multiple: false,
				animate: true,
				width: '290px'
			});
			
			// 只有最底层才能选择
			var pOrgTree = $('#p_q_orgId').combotree('tree');	
			pOrgTree.tree({
			   onBeforeSelect: function(node){
				   if(isNull(node.children)){
						return true;
				   }else{
					   return false;
				   }
			   }
			});
			
			 $("#" + datagrid).datagrid({
		        url : getDataUrl,
		        singleSelect : true, /*是否选中一行*/
		        width:'auto', 	
		        height: "390px",
		        pagination : true,  /*是否显示下面的分页菜单*/
		        border:false,
		        rownumbers: true,
		        idField: 'id',
		        frozenColumns:[[{
					field : '_operation',
					title : '操作',
					width : '70',
					align : 'center',
					formatter : function(value,row,index){
						return '<a href="javascript:void(0)" onclick="closeDialog('+ index +')">选择</a>';  	
					}
				  },{
		            field : 'id', 
		            hidden: 'true'
		        }, {
		            field : 'code',
		            title : '零件号',
		            width : '80',
		            align : 'center',
		            formatter: formatCellTooltip
		        }, {
		            field : 'name',
		            title : '名称',
		            width : '80',
		            align : 'center',
		            formatter: formatCellTooltip
		        }, {
		            field : 'isKey',
		            title : '关键零件',
		            width : '80',
		            align : 'center',
		            formatter: function(val){
	            		var tip = "否";
	            		if(val == 1){
	            			tip = "是";
	            		}
	            		return "<span title="+ tip +">" + tip + "</span>";
		            }
		        }, {
		            field : 'keyCode',
		            title : '零件型号',
		            width : '80',
		            align : 'center',
		            formatter: formatCellTooltip
		        }, {
		            field : 'org',
		            title : '生产商',
		            width : '120',
		            align : 'center',
		            formatter: function(val){
						if(val){
							return "<span title="+ val.name+">"+ val.name + "</span>";
						}
					}
		        }]],
		        columns : [ [{
		            field : 'proTime',
		            title : '生产时间',
		            width : '90',
		            align : 'center',
		            formatter: DateFormatter
		        }, {
					field : 'place',
					title : '生产场地',
					width : '150',
					align : 'center',
					formatter : formatCellTooltip
				}, {
					field : 'proNo',
					title : '生产批次',
					width : '80',
					align : 'center',
					formatter : formatCellTooltip
				}, {
					field : 'contacts',
					title : '联系人',
					width : '70',
					align : 'center',
					formatter : formatCellTooltip
				}, {
					field : 'phone',
					title : '联系电话',
					width : '90',
					align : 'center',
					formatter : formatCellTooltip
				}, {
					field : 'remark',
					title : '备注',
					width : '100',
					align : 'center',
					formatter : formatCellTooltip
				} ] ]
			});
	
			// 分页信息
			$('#' + datagrid).datagrid('getPager').pagination({
				pageSize : "${defaultPageSize}",
				pageNumber : 1,
				displayMsg : '当前显示 {from} - {to} 条记录    共  {total} 条记录',
				onSelectPage : function(pageNumber, pageSize) {//分页触发  
					var data = {
						'code' : $("#p_q_code").textbox("getValue"), 
						'name' : $("#p_q_name").textbox("getValue"), 
						'startProTime' : $("#p_q_startProTime").val(),
						'endProTime' : $("#p_q_endProTime").val(),
						'proNo' : $("#p_q_proNo").textbox("getValue"), 
						'orgId' : $("#p_q_orgId").combotree("getValue"),
						'isKey' : $("#p_q_isKey").combobox("getValue"),
						'keyCode' : $("#p_q_keyCode").textbox("getValue"), 
						'pageNum' : pageNumber,
						'pageSize' : pageSize
					}
					getData(datagrid, getDataUrl, data);
				}
			});
		});
	
		function p_doSearch() {
			var data = {
				'code' : $("#p_q_code").textbox("getValue"), 
				'name' : $("#p_q_name").textbox("getValue"), 
				'startProTime' : $("#p_q_startProTime").val(),
				'endProTime' : $("#p_q_endProTime").val(),
				'proNo' : $("#p_q_proNo").textbox("getValue"), 
				'orgId' : $("#p_q_orgId").combotree("getValue"),
				'isKey' : $("#p_q_isKey").combobox("getValue"),
				'keyCode' : $("#p_q_keyCode").textbox("getValue")
			}
			getData(datagrid, getDataUrl, data);
		}
	
		function doClear() {
			$("#p_q_code").textbox('clear');
			$("#p_q_startProTime").val('');
			$("#p_q_endProTime").val('');
			$("#p_q_name").textbox('clear');
			$("#p_q_proNo").textbox('clear');
			$("#p_q_keyCode").textbox('clear');
			$("#p_q_isKey").combobox('select', "");
			$("#p_q_orgId").combotree("setValue","");
			getData(datagrid, getDataUrl, {});
		}
	
		// 关掉对话时回调
		function closeDialog(index) {
			var rows = $('#' + datagrid).datagrid('getRows');
			var row = rows[index];

			if (!isNull(row)) {
				$("#p_code").textbox("setValue", row.code);
				$("#p_name").textbox("setValue", row.name);
				$("#p_proTime").datebox("setValue", formatDate(row.proTime));
				$("#p_place").textbox("setValue", row.place);
				$("#p_proNo").textbox("setValue", row.proNo);
				$("#p_remark").textbox("setValue", row.remark);	
				$("#p_isKey").combobox('select', row.isKey);
				$("#p_keyCode").textbox("setValue", row.keyCode);
				if(!isNull(row.org)){
					$("#p_orgId").combotree("setValue", row.org.id);
					$("#p_orgName").textbox("setValue", row.org.name);
				}else{
					$("#p_orgId").combotree("setValue", "");
					$("#p_orgName").textbox("setValue", "");
				}
				$("#p_phone").textbox("setValue", row.phone);
				$("#p_contacts").textbox("setValue", row.contacts);
				$("#p_id").val(row.id);
				
				if($('#qCode').length > 0){
					$('#qCode').textbox("setValue", "");
				}
				
				// 不可编辑
				$('#p_code').textbox('disable'); 
				$('#p_name').textbox('disable'); 
				$('#p_proTime').datebox('disable');
				$('#p_place').textbox('disable'); 
				$('#p_proNo').textbox('disable'); 
				$('#p_remark').textbox('disable'); 
				$('#p_keyCode').textbox('disable'); 
				$('#p_phone').textbox('disable'); 
				$('#p_contacts').textbox('disable'); 
				$("#p_isKey").combobox('disable');
				$("#p_orgId").combotree('disable');
			}
			
			$('#partsDialog').dialog('close');
		}
		
	</script>
	
	
	<style style="text/css">
		
		.lock-unlock {
			display: inline-block;
		    width: 16px;
		    height: 16px;
		    margin-right: 3px;
		}
		
		.qlabel{
			display: inline-block;
			width: 72px;
		}
	</style>

</body>


	
