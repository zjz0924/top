<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../../common/source.jsp"%>
		
		<script type="text/javascript">
			var editingId;
		
			$(function(){
				var toolbar = [{
		            text:'编辑',
		            iconCls:'icon-edit',
		            handler: function(){
		            	if (editingId != undefined) {
							$('#menuTree').treegrid('select', editingId);
							return;
						}
						var row = $('#menuTree').treegrid('getSelected'); 
						if (row) { 
							editingId = row.id;
							$('#menuTree').treegrid('beginEdit', editingId);
						}else{
							errorMsg("请选择要编辑的菜单项");
						}
		            }
		        }, '-', {
		            text:'保存',
		            iconCls:'icon-save',
		            handler: function(){
						if (editingId != undefined) {
							//先开始编辑，获取最新修改的信息						
							$('#menuTree').treegrid('endEdit', editingId);
							var row = $('#menuTree').treegrid('getSelected');
							
							$.ajax({
								url: "${ctx}/menu/update?time" + new Date(),
								data: {
									name: row.name,
									id: row.id,
									sortNum: row.sortNum
								},
								success: function(data){
									if(data.success){
										editingId = undefined;
										$("#menuTree").treegrid('reload');
									}else{
										$('#menuTree').treegrid('beginEdit', editingId);
										errorMsg(data.msg);
									}
								}
							});
						}else{
							errorMsg("没有正在编辑中的菜单项");
						}
					}
				}, '-',  {
					text : '取消',
					iconCls : 'icon-cancel',
					handler : function() {
						if (editingId != undefined) {
							$('#menuTree').treegrid('cancelEdit', editingId);
							editingId = undefined;
						}
						$("#menuTree").treegrid('reload');
					}
				} ];
	
				$('#menuTree').treegrid({
					url : '${ctx}/menu/tree',
					idField : 'id',
					treeField : 'name',
					rownumbers : true,
					animate : true,
					fitColumns : true,
					toolbar : toolbar,
					columns : [ [ {
						field : 'name',
						title : '菜单名',
						width : 200,
						editor : 'text'
					}, {
						field : 'sortNum',
						title : '排序号',
						width : 80,
						align : 'center',
						editor : 'numberbox'
					} ] ]
				});
			});
		</script>
		
	</head>
	
	<body>
		<div>
			<table id="menuTree" style="width:auto;height:auto"></table>
		</div>
	</body>
</html>
