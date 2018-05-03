<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

			
	<body>
		<div style="margin-top: 15px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
			<div>
				<div>
					<span class="qlabel">材料名称：</span>
					<input id="q_matName" name="q_matName" class="easyui-textbox" style="width: 200px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">生产批次：</span>
					<input id="q_proNo" name="q_proNo" class="easyui-textbox" style="width: 200px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">材料牌号：</span>
					<input id="q_matNo" name="q_matNo" class="easyui-textbox" style="width: 200px;"> &nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				
				<div style="margin-top:10px;margin-right: 40px;">
					<span class="qlabel">材料生产商：</span>
					<input id="q_orgId" name="q_orgId">&nbsp;&nbsp;&nbsp;&nbsp;
					
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="doClear()">清空</a>
				</div>
			</div>
		</div>
				
		<div style="margin-top:10px;">
			<table id="materialTable" style="height:auto;width:auto"></table>
		</div>
		
		<script type="text/javascript">
			var getDataUrl = "${ctx}/material/getList";
			var detailUrl = "${ctx}/material/detail";
			var datagrid = "materialTable";
			
			$(function(){
				 $('#q_orgId').combotree({
					url: '${ctx}/org/getTreeByType?type=2',
					multiple: false,
					animate: true,
					width: '200px'
				});
				
				// 只有最底层才能选择
				var pOrgTree = $('#q_orgId').combotree('tree');	
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
			        columns : [ [ {
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
						field : 'matName',
						title : '材料名称',
						width : '100',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'proNo',
						title : '生产批次',
						width : '100',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'matNo',
						title : '材料牌号',
						width : '100',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'matColor',
						title : '材料颜色',
						width : '100',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'org',
						title : '材料生产商',
						width : '120',
						align : 'center',
						formatter: function(val){
							if(val){
								return "<span title="+ val.name+">"+ val.name + "</span>";
							}
						}
					}, {
						field : 'contacts',
						title : '联系人',
						width : '90',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'phone',
						title : '联系电话',
						width : '100',
						align : 'center',
						formatter : formatCellTooltip
					}, {
						field : 'remark',
						title : '备注',
						width : '150',
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
							'proNo' : $("#q_proNo").textbox("getValue"), 
							'matName' : $("#q_matName").textbox("getValue"), 
							'matNo' : $("#q_matNo").textbox("getValue"), 
							'orgId' : $("#q_orgId").combotree("getValue"),
							'pageNum' : pageNumber,
							'pageSize' : pageSize
						}
						getData(datagrid, getDataUrl, data);
					}
				});
			});
		
			function doSearch() {
				var data = {
					'proNo' : $("#q_proNo").textbox("getValue"), 
					'matName' : $("#q_matName").textbox("getValue"), 
					'matNo' : $("#q_matNo").textbox("getValue"), 
					'orgId' : $("#q_orgId").combotree("getValue")
				}
				getData(datagrid, getDataUrl, data);
			}
		
			function doClear() {
				$("#q_proNo").textbox('clear');
				$("#q_matName").textbox('clear');
				$("#q_matNo").textbox('clear');
				$("#q_orgId").combotree("setValue","");
				getData(datagrid, getDataUrl, {});
			}
			
			// 关掉对话时回调
			function closeDialog(index) {
				var rows = $('#' + datagrid).datagrid('getRows');
				var row = rows[index];

				if (!isNull(row)) {
					$("#m_matName").textbox("setValue", row.matName);
					$("#m_proNo").textbox("setValue", row.proNo);
					if(!isNull(row.org)){
						$("#m_orgId").combotree("setValue", row.org.id);
						$("#m_orgName").textbox("setValue", row.org.name);
					}else{
						$("#m_orgId").combotree("setValue", "");
						$("#m_orgName").textbox("setValue", "");
					}
					$("#m_matNo").textbox("setValue", row.matNo);
					$("#m_matColor").textbox("setValue", row.matColor);
					$("#m_remark").textbox("setValue", row.remark);
					$("#m_contacts").textbox("setValue", row.contacts);
					$("#m_phone").textbox("setValue", row.phone);
					$("#m_pic_span").show();
					$("#m_pic_a").attr("href", "${resUrl}/" + row.pic);
					$("#m_pic").attr("src", "${resUrl}/" + row.pic);
					$("#m_id").val(row.id);
					
					if($('#qCode').length > 0){
						$('#qCode').textbox("setValue", "");
					}
					
					// 不可编辑
					$('#m_matName').textbox('disable'); 
					$('#m_proNo').textbox('disable'); 
					$('#m_phone').textbox('disable'); 
					$('#m_contacts').textbox('disable'); 
					$('#m_matNo').datebox('disable');
					$('#m_matColor').textbox('disable');
					$('#m_remark').textbox('disable'); 					
					$("#m_orgId").combotree('disable');
					
				}
				
				$('#materialDialog').dialog('close');
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
