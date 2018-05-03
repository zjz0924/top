<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<div style="margin-left: 10px;margin-top:20px;">
		
		<c:if test="${not empty recordList}">
			<div style="margin-bottom: 20px;margin-left: 10px;">
				<span style="font-weight:bold;color:red;">审批记录（不通过）：</span>
				<c:forEach items="${recordList}" var="vo" varStatus="vst">
       	    		<div style="margin-top:5px; margin-bottom: 5px;margin-left: 5px;">
       	    			<fmt:formatDate value='${vo.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>&nbsp;&nbsp;&nbsp;&nbsp;
						<c:choose>
							<c:when test="${vo.catagory == 1}">
								<span>零部件图谱试验</span>
							</c:when>
							<c:when test="${vo.catagory == 2}">
								<span>原材料图谱试验</span>
							</c:when>
							<c:when test="${vo.catagory == 3}">
								<span>零部件型式试验</span>
							</c:when>
							<c:when test="${vo.catagory == 4}">
								<span>原材料型式试验</span>
							</c:when>
							<c:otherwise>   
						        <span>所有试验</span>  
						  </c:otherwise>
						</c:choose>&nbsp;&nbsp;&nbsp;&nbsp;
						审批意见：<span style="font-weight:bold;">${vo.remark}</span>
       	    		</div>
       	    	</c:forEach> 
			</div>
		</c:if>
	
		<div class="title">整车信息</div>
		<div style="width: 98%;">
			<table class="info">
				<tr class="single-row">
					<td class="title-td">代码：</td>
					<td class="value-td">${facadeBean.info.vehicle.code}</td>
					<td class="title-td">车型：</td>
					<td class="value-td">${facadeBean.info.vehicle.type}</td>
				</tr>
				<tr class="couple-row">
					<td class="title-td">生产日期：</td>
					<td class="value-td"><fmt:formatDate value='${facadeBean.info.vehicle.proTime}' type="date" pattern="yyyy-MM-dd"/></td>
					<td class="title-td">生产地址：</td>
					<td class="value-td">${facadeBean.info.vehicle.proAddr}</td>
				</tr>
				<tr class="single-row">
					<td class="title-td">备注：</td>
					<td class="value-td" colspan="3">${facadeBean.info.vehicle.remark}</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<c:if test="${taskType == 1}">
			<div class="title">零部件信息</div>
			<div style="width: 98%;">
				<table class="info">
					<tr class="single-row">
						<td class="title-td">代码：</td>
						<td class="value-td">${facadeBean.info.parts.code}</td>
						<td class="title-td">名称：</td>
						<td class="value-td">${facadeBean.info.parts.name}</td>
					</tr>
					<tr class="couple-row">
						<td class="title-td">生产商：</td>
						<td class="value-td">${facadeBean.info.parts.org.name}</td>
						<td class="title-td">生产批号：</td>
						<td class="value-td">${facadeBean.info.parts.proNo}</td>
					</tr>
					<tr class="single-row">
						<td class="title-td">生产日期：</td>
						<td class="value-td"><fmt:formatDate value='${facadeBean.info.parts.proTime}' type="date" pattern="yyyy-MM-dd"/></td>
						<td class="title-td">生产地址：</td>
						<td class="value-td">${facadeBean.info.parts.place}</td>
					</tr>
					<tr class="couple-row">
						<td class="title-td">关键零件：</td>
						<td class="value-td">
							<c:choose>
								<c:when test="${facadeBean.info.parts.isKey == 0}">
									否
								</c:when>
								<c:otherwise>
									是
								</c:otherwise>
							</c:choose>
						</td>
						<td class="title-td">零件型号</td>
						<td class="value-td">
							${facadeBean.info.parts.keyCode}
						</td>
					</tr>
					
					<tr class="single-row">
						<td class="title-td">联系人：</td>
						<td class="value-td">${facadeBean.info.parts.contacts}</td>
						
						<td class="title-td">联系电话：</td>
						<td class="value-td">${facadeBean.info.parts.phone}</td>
					</tr>
					
					<tr class="couple-row">
						<td class="title-td">备注：</td>
						<td class="value-td" colspan="3">${facadeBean.info.parts.remark}</td>
					</tr>
				</table>
			</div>
			
			<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		</c:if>
		
		<div class="title">原材料信息</div>
		<div style="width: 98%;">
			<table class="info">
				<tr class="single-row">
					<td class="title-td">材料名称：</td>
					<td class="value-td">${facadeBean.info.material.matName}</td>
					<td class="title-td">生产批号：</td>
					<td class="value-td">${facadeBean.info.material.proNo}</td>
				</tr>
				
				<tr class="couple-row">
					<td class="title-td">生产商：</td>
					<td class="value-td">${facadeBean.info.material.org.name}</td>
					<td class="title-td">生产商地址：</td>
					<td class="value-td">${facadeBean.info.material.org.addr}</td>
				</tr>
				
				<tr class="single-row">
					<td class="title-td">材料牌号：</td>
					<td class="value-td">${facadeBean.info.material.matNo}</td>
					<td class="title-td">材料颜色：</td>
					<td class="value-td">${facadeBean.info.material.matColor}</td>
				</tr>
				
				<tr class="couple-row">
					<td class="title-td">联系人：</td>
					<td class="value-td">${facadeBean.info.material.contacts}</td>
					
					<td class="title-td">联系电话：</td>
					<td class="value-td">${facadeBean.info.material.phone}</td>
				</tr>
				
				<tr class="single-row">
					<td class="title-td">材料成分表：</td>
					<td class="value-td">
						<c:if test="${not empty facadeBean.info.material.pic}">
							<a target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">${fn:substringAfter(facadeBean.info.material.pic, "/")}</a>
						</c:if>
					</td>
					<td class="title-td">备注：</td>
					<td class="value-td">${facadeBean.info.material.remark}</td>
				</tr>
			</table>
		</div>

		 <div style="text-align:center;margin-top:25px;margin-bottom: 15px;" class="data-row">
			<a href="javascript:void(0);"  onclick="doTransmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">分配任务</a>&nbsp;&nbsp;
			<a href="javascript:void(0);"  onclick="$('#transmitDetailDialog').dialog('close');" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>

		<div id="dlg" class="easyui-dialog" title="任务下达" style="width: 550px; height: 550px; padding: 10px" closed="true" data-options="modal:true">
			
			<c:if test="${taskType == 1 and empty facadeBean.partsAtlId}">
				<div>
					<span class="title-span">零部件图谱试验：</span>
					<input id="partsAtlId" name="partsAtlId">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="partsAtlCheck" onclick="doCheck('partsAtlCheck')"><span class="red-font">不选</span>
				
					<div style="margin-top:5px;">
						任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="partsAtlCode" name="partsAtlCode" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
						商定完成时间：<input id="partsAtlTime" name="partsAtlTime" class="easyui-datebox" style="width:140px;" data-options="editable:false">
						<div style="margin-top:5px;">
							测试要求：<input id="partsAtlReq" name="partsAtlReq" class="easyui-textbox"  style="width:86%;">
						</div>
					</div>
					<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
				</div>
			</c:if>
			
			<c:if test="${empty facadeBean.matAtlId}">
				<div style="margin-top:5px;">
					<span class="title-span">原材料图谱试验： </span>
					<input id="matAtlId" name="matAtlId">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="matAtlCheck" onclick="doCheck('matAtlCheck')"><span class="red-font">不选</span>
				
					<div style="margin-top:5px;">
						任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="matAtlCode" name="matAtlCode" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
						商定完成时间：<input id="matAtlTime" name="matAtlTime" class="easyui-datebox" style="width:140px;" data-options="editable:false">
						<div style="margin-top:5px;">
							测试要求：<input id="matAtlReq" name="matAtlReq" class="easyui-textbox"  style="width:86%;">
						</div>
					</div>
					<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
				</div>
			</c:if>
			
			<c:if test="${taskType == 1 and empty facadeBean.partsPatId}">
				<div style="margin-top:5px;">
					<span class="title-span">零部件型式试验： </span>
					<input id="partsPatId" name="partsPatId">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="partsPatCheck" onclick="doCheck('partsPatCheck')"><span class="red-font">不选</span></br>
					
					<div style="margin-top:5px;">
						任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="partsPatCode" name="partsPatCode" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
						商定完成时间：<input id="partsPatTime" name="partsPatTime" class="easyui-datebox" style="width:140px;" data-options="editable:false">
						<div style="margin-top:5px;">
							测试要求：<input id="partsPatReq" name="partsPatReq" class="easyui-textbox"  style="width:86%;">
						</div>
					</div>
					<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 20px;"></div>
				</div>
			</c:if>
			
			<c:if test="${empty facadeBean.matPatId}">
				<div style="margin-top:5px;">
					<span class="title-span">原材料型式试验： </span>
					<input id="matPatId" name="matPatId">&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="checkbox" id="matPatCheck" onclick="doCheck('matPatCheck')"><span class="red-font">不选</span></br>
					
					<div style="margin-top:5px;">
						任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="matPatCode" name="matPatCode" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
						商定完成时间：<input id="matPatTime" name="matPatTime" class="easyui-datebox" style="width:140px;" data-options="editable:false">
						<div style="margin-top:5px;">
							测试要求：<input id="matPatReq" name="matPatReq" class="easyui-textbox"  style="width:86%;">
						</div>
					</div>
					<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
				</div>
			</c:if>
			
			<div align=center style="margin-top: 15px;">
				<a href="javascript:void(0);"  onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
				<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
	</div>
			
	<style type="text/css">
		.red-font{
		   color:red;
		   font-weight: bold;
		}
	
		.title {
			margin-left: 10px;
			margin-bottom: 8px;
			font-size: 14px;
			color: #1874CD;
    		font-weight: bold;
		}
		
		.title-span{
			display: inline-block;
			width: 120px;
			font-weight:bold;
		}
		
		.info{
			width:98%;
			margin-left: 5px;
			font-size: 14px;
			border-collapse:collapse;
		}
		
		.info tr{
			height: 30px;
		}
		
		.title-td {
			width:13%;
			padding-left: 5px;
			font-weight: bold;
		}
		
		.value-td{
			width:32%;
			padding-left: 5px;
		}
		
		.single-row{
			background: #F0F0F0;
		}
		
		.couple-row{
			background: #f5f5f5;
		}
	</style>
	
	<script type="text/javascript">
		var partsAtlId;
		var matAtlId;
		var partsPatId;
		var matPatId;
		// 是否提交中
		var saving = false;
		
		$(function(){	
			var taskType = "${taskType}";
			
			if(taskType == 1){
				partsAtlId = "${facadeBean.partsAtlId}";
				if(isNull(partsAtlId)){
					// 零部件图谱
					$('#partsAtlId').combotree({
						url: '${ctx}/org/getTreeByType?type=3',
						multiple: false,
						animate: true,
						width: '250px'				
					});
					setupTree("partsAtlId");
				}
				
				partsPatId = "${facadeBean.partsPatId}";
				if(isNull(partsPatId)){
					// 零部件型式
					$('#partsPatId').combotree({
						url: '${ctx}/org/getTreeByType?type=3',
						multiple: false,
						animate: true,
						width: '250px'				
					});
					setupTree("partsPatId");
				}
			}
			
			matAtlId = "${facadeBean.matAtlId}";
			if(isNull(matAtlId)){
				// 原材料图谱
				$('#matAtlId').combotree({
					url: '${ctx}/org/getTreeByType?type=3',
					multiple: false,
					animate: true,
					width: '250px'					
				});
				setupTree("matAtlId");
			}
			
			matPatId = "${facadeBean.matPatId}";
			if(isNull(matPatId)){
				// 原材料型式
				$('#matPatId').combotree({
					url: '${ctx}/org/getTreeByType?type=3',
					multiple: false,
					animate: true,
					width: '250px'				
				});
				setupTree("matPatId");
			}
		});
		
		
		function doTransmit(){
			var taskType = "${taskType}";
			// 默认选中CQC实验室
			
			$("#matAtlId").combotree({ disabled: false });
			$("#matPatId").combotree({ disabled: false });
			$("#matAtlId").combotree("setValue", "20");
			$("#matPatId").combotree("setValue", "20");
			$("#matAtlCheck").prop("checked",false);
			$("#matPatCheck").prop("checked",false);
			
			if(taskType == 1){
				$("#partsAtlId").combotree({ disabled: false });
				$("#partsPatId").combotree({ disabled: false });
				$("#partsAtlId").combotree("setValue", "20");
				$("#partsPatId").combotree("setValue", "20");
				$("#partsAtlCheck").prop("checked",false);
				$("#partsPatCheck").prop("checked",false);
				
				// 清空原来输入的值
				$("#partsAtlCode").textbox("setValue", "");
				$("#partsAtlTime").datebox("setValue", "");
				$("#partsAtlReq").textbox("setValue", "");
			}
			
			
			// 清空原来输入的值
			$("#matAtlCode").textbox("setValue", "");
			$("#matAtlTime").datebox("setValue", "");
			$("#matAtlReq").textbox("setValue", "");
			
			// 清空原来输入的值
			$("#partsPatCode").textbox("setValue", "");
			$("#partsPatTime").datebox("setValue", "");
			$("#partsPatReq").textbox("setValue", "");
			
			// 清空原来输入的值
			$("#matPatCode").textbox("setValue", "");
			$("#matPatTime").datebox("setValue", "");
			$("#matPatReq").textbox("setValue", "");
			
			$("#dlg").dialog("open");
		}
		
		function doSubmit(){
			var taskType = "${taskType}";
			var partsAtlId_val;
			var matAtlId_val;
			var partsPatId_val;
			var matPatId_val;
			// 零部件图谱试验说明
			var partsAtlCode;
			var partsAtlTime;
			var partsAtlReq;
			// 原材料图谱试验说明
			var matAtlCode;
			var matAtlTime;
			var matAtlReq;
			// 零部件型式试验说明
			var partsPatCode;
			var partsPatTime;
			var partsPatReq;
			// 原材型式试验说明
			var matPatCode;
			var matPatTime;
			var matPatReq;
			
			if(saving){
				return false;
			}
			saving = true;
			
			if(taskType == 1){
				if(!$("#partsAtlCheck").is(':checked')){
					if(isNull(partsAtlId)){
						partsAtlId_val = $("#partsAtlId").combotree("getValue");
						if(isNull(partsAtlId_val)){
							errorMsg("请为零部件图谱试验选择实验室");
							saving = false;
							return false;
						}
					}
				}
				
				if(!$("#partsPatCheck").is(':checked')){
					if(isNull(partsPatId)){
						partsPatId_val = $("#partsPatId").combotree("getValue");
						if(isNull(partsPatId_val)){
							errorMsg("请为零部件型式试验选择实验室");
							saving = false;
							return false;
						}
					}	
				}
			}
			
			if(!$("#matAtlCheck").is(':checked')){
				if(isNull(matAtlId)){
					matAtlId_val = $("#matAtlId").combotree("getValue");
					if(isNull(matAtlId_val)){
						errorMsg("请为原材料图谱试验选择实验室");
						saving = false;
						return false;
					}
				}
			}
			
			if(!$("#matPatCheck").is(':checked')){
				if(isNull(matPatId)){
					matPatId_val = $("#matPatId").combotree("getValue");
					if(isNull(matPatId_val)){
						errorMsg("请为原材料型式试验选择实验室");
						saving = false;
						return false;
					}
				}
			}
			
			if(!isNull(partsAtlId_val)){
				partsAtlCode = $("#partsAtlCode").textbox("getValue");
				partsAtlTime = $("#partsAtlTime").datebox("getValue");
				partsAtlReq = $("#partsAtlReq").textbox("getValue");
			}
			
			if(!isNull(matAtlId_val)){
				matAtlCode = $("#matAtlCode").textbox("getValue");
				matAtlTime = $("#matAtlTime").datebox("getValue");
				matAtlReq = $("#matAtlReq").textbox("getValue");
			}

			if(!isNull(partsPatId_val)){
				partsPatCode = $("#partsPatCode").textbox("getValue");
				partsPatTime = $("#partsPatTime").datebox("getValue");
				partsPatReq = $("#partsPatReq").textbox("getValue");
			}
			
			if(!isNull(matPatId_val)){
				matPatCode = $("#matPatCode").textbox("getValue");
				matPatTime = $("#matPatTime").datebox("getValue");
				matPatReq = $("#matPatReq").textbox("getValue");
			}
			
			$.ajax({
				url: "${ctx}/ots/transmit",
				data: {
					"id": '${facadeBean.id}',
					"partsAtlId": partsAtlId_val,
					"matAtlId": matAtlId_val,
					"partsPatId": partsPatId_val,
					"matPatId": matPatId_val,
					"partsAtlCode": partsAtlCode,
					"partsAtlTime": partsAtlTime,
					"partsAtlReq": partsAtlReq,
					"matAtlCode": matAtlCode,
					"matAtlTime": matAtlTime,
					"matAtlReq": matAtlReq,
					"partsPatCode": partsPatCode,
					"partsPatTime": partsPatTime,
					"partsPatReq": partsPatReq,
					"matPatCode": matPatCode,
					"matPatTime": matPatTime,
					"matPatReq": matPatReq
				},
				success: function(data){
					if(data.success){
						$("#dlg").dialog("close");
						closeDialog(data.msg);
					}else{
						saving = false;
						errorMsg(data.msg);						
					}
				}
			});
		}
		
		function doCancel(){
			$("#dlg").dialog("close");
		}
		
		// 只有最底层才能选择
		function setupTree(id){
			var treeObj = $('#' + id).combotree('tree');	
			treeObj.tree({
			   onBeforeSelect: function(node){
				   if(isNull(node.children)){
						return true;
				   }else{
					   return false;
				   }
			   }
			});
		}
		
		function doCheck(id){
			var treeId = id.replace("Check", "Id");
			var codeId = id.replace("Check", "Code");
			var timeId = id.replace("Check", "Time");
			var reqId = id.replace("Check", "Req");
			
			if($("#" + id).is(':checked')){
				$("#" + treeId).combotree("setValue","");
				$("#" + treeId).combotree({ disabled: true }); 
				
				$("#" + codeId).textbox("setValue","");
				$("#" + reqId).textbox("setValue","");
				$("#" + timeId).datebox("setValue","");
				$("#" + codeId).textbox({ disabled: true });
				$("#" + reqId).textbox({ disabled: true });
				$("#" + timeId).datebox({ disabled: true });
			}else{
				$("#" + treeId).combotree({ disabled: false });
				setupTree(treeId);
				
				$("#" + codeId).textbox({ disabled: false });
				$("#" + reqId).textbox({ disabled: false });
				$("#" + timeId).datebox({ disabled: false });
			}
		}
	</script>	
	
</body>
