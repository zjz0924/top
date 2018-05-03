<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<form method="POST" enctype="multipart/form-data" id="uploadForm">
		<input type="hidden" id="t_id" name="t_id" value="${facadeBean.id }">
	
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">整车信息</div>
			<c:if test="${empty newVehicle}">
				<div style="width: 98%;">
					<table class="info" style="width: 98%;">
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
			</c:if>
			
			<c:if test="${not empty newVehicle}">
				<table class="info">
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>代码：</span>
							<span class="val" title="${facadeBean.info.vehicle.code }">${facadeBean.info.vehicle.code }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.vehicle.code != newVehicle.code}">red-color</c:if>" title="${newVehicle.code }">${newVehicle.code }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>车型：</span>
							<span class="val" title="${facadeBean.info.vehicle.type }">${facadeBean.info.vehicle.type }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.vehicle.type != newVehicle.type}">red-color</c:if>" title="${newVehicle.type }">${newVehicle.type }</span>
						</td>
					</tr>
	
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产日期：</span>
							<span class="val" title="<fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.vehicle.proTime != newVehicle.proTime}">red-color</c:if>" title="<fmt:formatDate value='${newVehicle.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${newVehicle.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
					</tr>
	
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产地址：</span>
							<span class="val" title="${facadeBean.info.vehicle.proAddr }">${facadeBean.info.vehicle.proAddr }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.vehicle.proAddr != newVehicle.proAddr}">red-color</c:if>" title="${newVehicle.proAddr }">${newVehicle.proAddr }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span">&nbsp;备注：</span> 
							<span class="val" title="${facadeBean.info.vehicle.remark }">${facadeBean.info.vehicle.remark }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.vehicle.remark != newVehicle.remark}">red-color</c:if>" title="${newVehicle.remark }">${newVehicle.remark }</span>
						</td>
					</tr>
				</table>
			</c:if>
		</div>
	
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">零部件信息</div>
			
			<c:if test="${empty newParts}">
				<div style="width: 98%;">
					<table class="info" style="width: 98%;">
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
			</c:if>
			
			<c:if test="${not empty newParts}">
				<table class="info">
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>零件号：</span> 
							<span class="val" title="${facadeBean.info.parts.code}">${facadeBean.info.parts.code}</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.code != newParts.code}">red-color</c:if>" title="${newParts.code}">${newParts.code}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>名称：</span>
							<span class="val" title="${facadeBean.info.parts.name}">${facadeBean.info.parts.name}</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.name != newParts.name}">red-color</c:if>" title="${newParts.name}">${newParts.name}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产商：</span> 
							<span class="val" title="${facadeBean.info.parts.org.name}">${facadeBean.info.parts.org.name}</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.org.name != newParts.org.name}">red-color</c:if>" title="${newParts.org.name}">${newParts.org.name}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
							<span class="val" title="${facadeBean.info.parts.proNo }">${facadeBean.info.parts.proNo }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.proNo != newParts.proNo}">red-color</c:if>" title="${newParts.proNo }">${newParts.proNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
							<span class="val" title="<fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.proTime != newParts.proTime}">red-color</c:if>" title="<fmt:formatDate value='${newParts.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${newParts.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产场地：</span> 
							<span class="val" title="${facadeBean.info.parts.place }">${facadeBean.info.parts.place }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.place != newParts.place}">red-color</c:if>" title="${newParts.place }">${newParts.place }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>关键零件：</span> 
							<span class="val" title="<c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if>"><c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if></span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.isKey != newParts.isKey}">red-color</c:if>" title="<c:if test="${newParts.isKey == 0 }">否</c:if><c:if test="${newParts.isKey == 1 }">是</c:if>"><c:if test="${newParts.isKey == 0 }">否</c:if><c:if test="${newParts.isKey == 1 }">是</c:if></span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span">零件型号：</span> 
							<span class="val" title="${facadeBean.info.parts.keyCode }">${facadeBean.info.parts.keyCode }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.keyCode != newParts.keyCode}">red-color</c:if>" title="${newParts.keyCode }">${newParts.keyCode }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系人：</span> 
							<span class="val" title="${facadeBean.info.parts.contacts }">${facadeBean.info.parts.contacts }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.contacts != newParts.contacts}">red-color</c:if>" title="${newParts.contacts }">${newParts.contacts }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系电话：</span> 
							<span class="val" title="${facadeBean.info.parts.phone }">${facadeBean.info.parts.phone }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.phone != newParts.phone}">red-color</c:if>" title="${newParts.phone }">${newParts.phone }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">&nbsp;备注：</span> 
							<span class="val" title="${facadeBean.info.parts.remark }">${facadeBean.info.parts.remark }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.parts.remark != newParts.remark}">red-color</c:if>" title="${newParts.remark }">${newParts.remark }</span>	
						</td>
					</tr>
				</table>
			</c:if>
		</div>
	
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">原材料信息</div>
			
			<c:if test="${empty newMaterial}">
				<div style="width: 98%;">
					<table class="info" style="width: 98%;">
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
			</c:if>
			
			<c:if test="${not empty newMaterial}">
				<table class="info">
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>材料名称：</span> 
							<span class="val" title="${facadeBean.info.material.matName }">${facadeBean.info.material.matName }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.matName != newMaterial.matName}">red-color</c:if>" title="${newMaterial.matName }">${newMaterial.matName }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
							<span class="val" title="${facadeBean.info.material.proNo }">${facadeBean.info.material.proNo }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.proNo != newMaterial.proNo}">red-color</c:if>" title="${newMaterial.proNo }">${newMaterial.proNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产商：</span> 
							<span class="val" title="${facadeBean.info.material.org.name }">${facadeBean.info.material.org.name }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.org.name != newMaterial.org.name}">red-color</c:if>" title="${newMaterial.org.name }">${newMaterial.org.name }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>材料牌号：</span> 
							<span class="val" title="${facadeBean.info.material.matNo }">${facadeBean.info.material.matNo }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.matNo != newMaterial.matNo}">red-color</c:if>" title="${newMaterial.matNo }">${newMaterial.matNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>材料颜色：</span> 
							<span class="val" title="${facadeBean.info.material.matColor }">${facadeBean.info.material.matColor }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.matColor != newMaterial.matColor}">red-color</c:if>" title="${newMaterial.matColor }">${newMaterial.matColor }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>成分表：</span> 
							<c:if test="${not empty facadeBean.info.material.pic }">
								<a target= _blank  href="${resUrl}/${facadeBean.info.material.pic }">${facadeBean.info.material.pic }</a>
							</c:if>
						</td>
						
						<td class="input-td">
							<c:if test="${not empty newMaterial.pic }">
								<a target= _blank  href="${resUrl}/${newMaterial.pic }">${newMaterial.pic }</a>
							</c:if>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>联系人：</span> 
							<span class="val" title="${facadeBean.info.material.contacts }">${facadeBean.info.material.contacts }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.contacts != newMaterial.contacts}">red-color</c:if>" title="${newMaterial.contacts }">${newMaterial.contacts }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>联系电话：</span> 
							<span class="val" title="${facadeBean.info.material.phone }">${facadeBean.info.material.phone }</span>
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.phone != newMaterial.phone}">red-color</c:if>" title="${newMaterial.phone }">${newMaterial.phone }</span>
						</td>
					</tr>
					
					<tr>	
						<td>
							<span class="title-span">备注：</span> 
							<span class="val" title="${facadeBean.info.material.remark }">${facadeBean.info.material.remark }</span>	
						</td>
						<td class="input-td">
							<span class="val <c:if test="${ facadeBean.info.material.remark != newMaterial.remark}">red-color</c:if>" title="${newMaterial.remark }">${newMaterial.remark }</span>
						</td>
					</tr>
				</table>
			</c:if>
		</div>
		
		 <div id="errorMsg" style="font-weight:bold;color:red;text-align:center;margin-top:10px;"></div>
		 <div style="text-align:center;margin-top:15px;margin-bottom: 15px;" class="data-row">
			<a href="javascript:void(0);"  onclick="approve(1,'', 1)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">同意</a>&nbsp;&nbsp;
			<a href="javascript:void(0);"  onclick="notPass(1)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">不同意</a>
		 </div>
	
		 <div id="dlg" class="easyui-dialog" title="审批" style="width: 400px; height: 200px; padding: 10px" closed="true" data-options="modal:true">
			<input type="hidden" id="catagory" name="catagory">
			<input id="remark" class="easyui-textbox" label="不同意原因：" labelPosition="top" multiline="true" style="width: 350px;height: 100px;"/>
			
			<div align=center style="margin-top: 15px;">
				<a href="javascript:void(0);"  onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
				<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
	</form>
	
	<script type="text/javascript">
		// 是否提交中
		var saving = false;
	
		function approve(result, remark, catagory){
			if(saving){
				return false;
			}
			saving = true;
			
			$("#errorMsg").html("");
			
			$.ajax({
				url: "${ctx}/ppap/approve",
				data: {
					"id": "${facadeBean.id}",
					"result": result,
					"remark": remark,
					"catagory": catagory
				},
				success: function(data){
					saving = false;
					if(data.success){
						closeDialog(data.msg);
					}else{
						$("#errorMsg").html(data.msg);
					}
				}
			});
		}
		
		function notPass(catagory){
			$("#catagory").val(catagory);
			$("#remark").textbox("setValue", "");
			$("#dlg").dialog("open");
		}
		
		function doSubmit(){
			var remark = $("#remark").textbox("getValue");
			var catagory = $("#catagory").val();
			if(isNull(remark)){
				errorMsg("请输入原因");
				$("#remark").next('span').find('input').focus();
				return false;
			}
			
			approve(2, remark, catagory);
			$("#dlg").dialog("close");
		}
		
		function doCancel(){
			$("#dlg").dialog("close");
		}
		
		
	</script>
	
	<style type="text/css">
		.title-span{
			display: inline-block;
			width: 85px;
			padding-right: 15px;
			text-align: right; 
		}
		
        .req-span{
        	font-weight: bold;
        	color: red;
        }
        
        .info{
        	border: 1px dashed #C9C9C9;
        	margin-left:10px;
        	margin-right: 10px;
        	font-size: 14px;
        	padding-top: 5px;
        	padding-bottom: 5px;
        	border-collapse:collapse;
        }
        
        .info tr{
        	height: 35px;
        }
        
        .title{
        	margin-left:10px;
        	margin-bottom:8px;
        	font-size: 14px;
        	color: #1874CD;
   			font-weight: bold;
        }
        
        .icon{
        	width:16px;
        	height: 16px;
        	display: inline-block;
        }
        
        .val{
        	border: 1px solid #D3D3D3;
		    display: inline-block;
		    width: 180px;
		    border-radius: 5px 5px 5px 5px;
		    margin: 0px;
		    padding-top: 0px;
		    padding-bottom: 0px;
		    line-height: 22px;
		    padding-left: 5px;
		    opacity: 0.6;
		    background-color: rgb(235, 235, 228);
		}
		
		.red-color{
			color: red;
			font-weight: bold;
		}
		
		input{
			width: 180px;
		}
		
		.input-td{
			padding-left: 50px;
			padding-right: 20px;
		}
		
		.error-message{
			color:red;
			text-align:center;
			margin-bottom: 15px;
			font-weight:bold;
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
		
		
	
</body>
