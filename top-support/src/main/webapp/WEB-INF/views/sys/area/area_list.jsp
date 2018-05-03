<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../../common/source.jsp"%>
		
		<script type="text/javascript">
			//操作类型, 0: 新建修改  1：删除
			var areaOperation = "";
			var getAreaDataUrl = "${ctx}/area/tree";
			var svalue = "";
			var stype = ""; 
			
			$(function(){
				$('#areaTree').tree({
					method:'get',
					animate:true,
					dnd:true,
					lines: true,
				    url: getAreaDataUrl,
				    onBeforeDrop: function(target,source,point){
						var targetNode = $("#areaTree").tree('getNode', target);
						if(!isNull(targetNode.children)){
							for(var i = 0; i < targetNode.children.length; i++){
								var node = targetNode.children[i];
								if(node.text == source.text){
									return false;
								}
							}
						}
						return true;
					},
					onDrop : function(target, source, point) { 
						var targetId = $("#areaTree").tree('getNode', target).id;
						
						$.ajax({
							url : "${ctx}/area/move",
							data : {
								id : source.id,
								parentid : targetId
							},
							success : function(data) {
								if (!data.success) {
									errorMsg(data.msg);
								} 
								areaOperation = source.id;
								$("#areaTree").tree("reload");
							}
						});
					},
					onSelect : function(node) {
						$.ajax({
							url : "${ctx}/area/info",
							data : {
								id : node.id
							},
							success : function(data) {
								$("#name").html(data.name);
								$("#desc").html(data.desc);
								$("#code").html(data.code);
	
								if (!isNull(data.area)) {
									$("#areaName").html(data.area.name);
								}
								if (!isNull(data.parent)) {
									$("#parentName").html(data.parent.name);
								} else {
									$("#parentName").html("");
								}
							}
						});
					},
					onContextMenu : function(e, node) {
						e.preventDefault();
						$('#areaTree').tree('select', node.target);
	
						$('#areaTreeMenu').menu('show', {
							left : e.pageX,
							top : e.pageY
						})
					},
					onLoadSuccess : function(node, data) {
						if (areaOperation == 0 || isNull(areaOperation)) {
							// 删除后自动选中根节点
							$("#areaTree").tree("select", $("#areaTree").tree("getRoot").target);
						} else if (areaOperation > 0) {
							// 创建/编辑成功后自动选中该节点
							var node = $('#areaTree').tree('find', areaOperation);
							$("#areaTree").tree("select", node.target);
						}
					},
					onBeforeLoad: function(node, param){
						param.svalue = svalue;
						param.stype = stype;
					}
				});
	
				/* $('#areaSearchbox').searchbox({
					searcher : function(value, name) {
						svalue = value;
						stype = name;
						
						// 搜索时不选中任何
						if(isNull(value)){
							areaOperation = 0;
						}else{
							areaOperation = -1;
							$("#name").html("");
							$("#desc").html("");
							$("#code").html("");
							$("#parentName").html("");
						}
						
						$("#areaTree").tree("reload");
					},
					menu : '#searchMenu',
					prompt : ''
				}); */
			});
	
			function createUpdateArea(type) {
				var id = "";
				var parentid = "";
				var selecteNode = $("#areaTree").tree("getSelected");
				if (type == 1) {
					parentid = selecteNode.id;
				} else {
					id = selecteNode.id;
					
					var parentNode = $("#areaTree").tree("getParent", selecteNode.target);
					if(isNull(parentNode)){
						errorMsg("不能编辑根节点");
						return false;
					}
				}
	
				$('#areaDialog').dialog({
					title : '区域信息',
					width : 380,
					height : 250,
					closed : false,
					cache : false,
					href : '${ctx}/area/detail?id=' + id + '&parentid=' + parentid,
					modal : true
				});
				$('#areaDialog').window('center');
			}
	
			// 关掉对话时回调
			function closeAreaDialog(id, result) {
				$('#areaDialog').dialog('close');
	
				// 创建/编辑成功
				if (!isNull(id)) {
					tipMsg(result);
					areaOperation = id;
				}
				$("#areaTree").tree("reload");
			}
	
			function removeArea() {
				$.messager.confirm('确认', '确定要删除此区域？删除后不可恢复?', function(r) {
					if (r) {
						var node = $("#areaTree").tree("getSelected");
						var parentNode = $("#areaTree").tree("getParent", node.target);
	
						if (parentNode == null) {
							errorMsg("不能删除根节点");
							return;
						}
	
						var children = $("#areaTree").tree("getChildren", node.target);
						if (!isNull(children)) {
							errorMsg("请先删除下级区域");
							return;
						}
	
						$.ajax({
							url : "${ctx}/area/delete",
							data : {
								id : node.id
							},
							success : function(data) {
								if (data.success) {
									areaOperation = 0;
									$("#areaTree").tree("reload");
									tipMsg(data.msg);
								} else {
									errorMsg(data.msg);
								}
							}
						});
					}
				});
			}
		</script>
	
	
		<style type="text/css">
			.table {
				table-layout: fixed;
				display: table;
			}
			
			.column {
				display: table-cell;
				vertical-align: top;
				margin: 0;
				padding: 0;
			}
			
			.info{
				line-height: 30px;
			}
			
			.info_title{
				display: inline-block;
				width: 80px;
				font-weight:bold;
				padding-right: 20px;
			}
		</style>
		
	</head>
	

	<body>
		<div class="table">
			<div class="column" style="height: 600px;width: 300px;border-right:1px dashed #e6e6e6;overflow-x:auto;overflow-y:auto;padding-top:10px;padding-left:10px;">
				<!-- <input id="areaSearchbox" name="areaSearchbox" style="width: 280px;"></input>
				<div id="searchMenu">
			        <div data-options="name:'name'">名称</div>
			        <div data-options="name:'code'">编码</div>
			    </div> -->
				
				<div style="margin-right: 10px;margin-top:10px;">
					<ul id="areaTree" name="areaTree"></ul>
				</div>
			</div>
			
			<div class="column" style="padding:10px 20px;font-size: 14px;width:80%;">
				<p style="font-size: 16px;margin-bottom: 10px;">区域信息</p>
				
				<div style="border: 1px dashed #e6e6e6;width: 100%;margin-bottom:5px;"></div>
			
				<div class="info">
					<span class="info_title">区域编码：</span>
					<span id="code"></span>
				</div>
				
				<div class="info">
					<span class="info_title">区域名称：</span>
					<span id="name"></span>
				</div>
				
				<div class="info">
					<span class="info_title">上级区域：</span>
					<span id="parentName"></span>
				</div>
				
				<div class="info">
					<span class="info_title">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</span>
					<span id="desc"></span>
				</div>
			</div>
		</div>
		
		<div id="areaTreeMenu" class="easyui-menu" style="width:120px;">
			<div onclick="createUpdateArea(1)">添加</div>
			<div onclick="createUpdateArea(2)">编辑</div>
			<div onclick="removeArea()">删除</div>
		</div>
		
		<div id="areaDialog"></div>
	</body>

</html>
