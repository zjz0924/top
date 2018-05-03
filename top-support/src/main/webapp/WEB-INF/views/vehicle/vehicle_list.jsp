<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>


<body>
	<div style="margin-top: 15px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
		<div>
			代码：<input id="q_code" name="q_code" class="easyui-textbox" style="width: 120px;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			车型：<input id="q_type" name="q_type" class="easyui-textbox" style="width: 120px;"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		
	      	生产时间：<input type="text" id="q_startProTime" name="q_startProTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endProTime\')}'})" class="textbox" style="line-height: 23px;width:120px;display:inline-block"/> - 
			   	   <input type="text" id="q_endProTime" name="q_endProTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startProTime\')}'})" class="textbox"  style="line-height: 23px;width:120px;display:inline-block;margin-right:20px;"/>
		
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
		</div>
	</div>
			
	<div style="margin-top:10px;">
		<table id="vehicleTable" style="height:390px;width:auto"></table>
	</div>
	
	<script type="text/javascript">
		var getDataUrl = "${ctx}/vehicle/getList";
		var detailUrl = "${ctx}/vehicle/detail";
		var datagrid = "vehicleTable";
		
		$(function(){
			 $("#" + datagrid).datagrid({
		        url : getDataUrl,
		        singleSelect : true, /*是否选中一行*/
		        width:'auto', 
		        pagination : true,  /*是否显示下面的分页菜单*/
		        border:false,
		        rownumbers: true,
		        idField: 'id',
		        columns : [ [{
					field : '_operation',
					title : '操作',
					width : '80',
					align : 'center',
					formatter : function(value,row,index){
						return '<a href="javascript:void(0)" onclick="closeDialog('+ index +')">选择</a>';  	
					}
				} ,{
		            field : 'id', 
		            hidden: 'true'
		        }, {
		            field : 'code',
		            title : '代码',
		            width : '150',
		            align : 'center',
		            formatter: formatCellTooltip
		        }, {
		            field : 'type',
		            title : '车型',
		            width : '150',
		            align : 'center',
		            formatter: formatCellTooltip
		        }, {
		            field : 'proTime',
		            title : '生产时间',
		            width : '150',
		            align : 'center',
		            formatter: DateFormatter
		        }, {
					field : 'proAddr',
					title : '生产地址',
					width : '200',
					align : 'center',
					formatter : formatCellTooltip
				}, {
					field : 'remark',
					title : '备注',
					width : '200',
					align : 'center',
					formatter : formatCellTooltip
				}] ]
			});
	
			// 分页信息
			$('#' + datagrid).datagrid('getPager').pagination({
				pageSize : "${defaultPageSize}",
				pageNumber : 1,
				displayMsg : '当前显示 {from} - {to} 条记录    共  {total} 条记录',
				onSelectPage : function(pageNumber, pageSize) {//分页触发  
					var data = {
						'code' : $("#q_code").textbox("getValue"), 
						'type' : $("#q_type").textbox("getValue"),
						'startProTime' : $("#q_startProTime").val(),
						'endProTime' : $("#q_endProTime").val(),
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
				'type' : $("#q_type").textbox("getValue"),
				'startProTime' : $("#q_startProTime").val(),
				'endProTime' : $("#q_endProTime").val()
			}
			getData(datagrid, getDataUrl, data);
		}
	
		function doClear() {
			$("#q_code").textbox('clear');
			$("#q_type").textbox('clear');
			$("#q_startProTime").val('');
			$("#q_endProTime").val('');
			
			getData(datagrid, getDataUrl, {});
		}
		
		// 关掉对话时回调
		
		function closeDialog(index) {
			var rows = $('#' + datagrid).datagrid('getRows');
			var row = rows[index];

			if (!isNull(row)) {
				$("#v_code").textbox("setValue", row.code);
				$("#v_type").textbox("setValue", row.type);
				$("#v_proTime").datebox('setValue',formatDate(row.proTime));
				$("#v_proAddr").textbox("setValue", row.proAddr);
				$("#v_remark").textbox("setValue", row.remark);
				$("#v_id").val(row.id);
				
				$('#v_code').textbox('disable'); 
				$('#v_type').textbox('disable'); 
				$('#v_proTime').datebox('disable');
				$('#v_proAddr').textbox('disable'); 
				$('#v_remark').textbox('disable'); 
				
				if($('#qCode').length > 0){
					$('#qCode').textbox("setValue", "");
				}
			}
			$('#vehicleDialog').dialog('close');
		}
	</script>

</body>
