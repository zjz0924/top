<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<c:if test="${applyRecord.type == 1 }">
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
							<span class="val red-color" title="${newVehicle.code }">${newVehicle.code }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>车型：</span>
							<span class="val" title="${facadeBean.info.vehicle.type }">${facadeBean.info.vehicle.type }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newVehicle.type }">${newVehicle.type }</span>
						</td>
					</tr>
	
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产日期：</span>
							<span class="val" title="<fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="<fmt:formatDate value='${newVehicle.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${newVehicle.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
					</tr>
	
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产地址：</span>
							<span class="val" title="${facadeBean.info.vehicle.proAddr }">${facadeBean.info.vehicle.proAddr }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newVehicle.proAddr }">${newVehicle.proAddr }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span">&nbsp;备注：</span> 
							<span class="val" title="${facadeBean.info.vehicle.remark }">${facadeBean.info.vehicle.remark }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newVehicle.remark }">${newVehicle.remark }</span>
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
							<span class="val red-color" title="${newParts.code}">${newParts.code}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>名称：</span>
							<span class="val" title="${facadeBean.info.parts.name}">${facadeBean.info.parts.name}</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.name}">${newParts.name}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产商：</span> 
							<span class="val" title="${facadeBean.info.parts.org.name}">${facadeBean.info.parts.org.name}</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.org.name}">${newParts.org.name}</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
							<span class="val" title="${facadeBean.info.parts.proNo }">${facadeBean.info.parts.proNo }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.proNo }">${newParts.proNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
							<span class="val" title="<fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="<fmt:formatDate value='${newParts.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${newParts.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产场地：</span> 
							<span class="val" title="${facadeBean.info.parts.place }">${facadeBean.info.parts.place }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.place }">${newParts.place }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>关键零件：</span> 
							<span class="val" title="<c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if>"><c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if></span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="<c:if test="${newParts.isKey == 0 }">否</c:if><c:if test="${newParts.isKey == 1 }">是</c:if>"><c:if test="${newParts.isKey == 0 }">否</c:if><c:if test="${newParts.isKey == 1 }">是</c:if></span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span">零件型号：</span> 
							<span class="val" title="${facadeBean.info.parts.keyCode }">${facadeBean.info.parts.keyCode }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.keyCode }">${newParts.keyCode }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系人：</span> 
							<span class="val" title="${facadeBean.info.parts.contacts }">${facadeBean.info.parts.contacts }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.contacts }">${newParts.contacts }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系电话：</span> 
							<span class="val" title="${facadeBean.info.parts.phone }">${facadeBean.info.parts.phone }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.phone }">${newParts.phone }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">&nbsp;备注：</span> 
							<span class="val" title="${facadeBean.info.parts.remark }">${facadeBean.info.parts.remark }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newParts.remark }">${newParts.remark }</span>	
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
									<a target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">${facadeBean.info.material.pic }</a>
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
							<span class="val red-color" title="${newMaterial.matName }">${newMaterial.matName }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
							<span class="val" title="${facadeBean.info.material.proNo }">${facadeBean.info.material.proNo }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.proNo }">${newMaterial.proNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产商：</span> 
							<span class="val" title="${facadeBean.info.material.org.name }">${facadeBean.info.material.org.name }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.org.name }">${newMaterial.org.name }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>材料牌号：</span> 
							<span class="val" title="${facadeBean.info.material.matNo }">${facadeBean.info.material.matNo }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.matNo }">${newMaterial.matNo }</span>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>材料颜色：</span> 
							<span class="val" title="${facadeBean.info.material.matColor }">${facadeBean.info.material.matColor }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.matColor }">${newMaterial.matColor }</span>
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
							<span class="title-span">联系人：</span> 
							<span class="val" title="${facadeBean.info.material.contacts }">${facadeBean.info.material.contacts }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.contacts }">${newParts.contacts }</span>
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系电话：</span> 
							<span class="val" title="${facadeBean.info.material.phone }">${facadeBean.info.material.phone }</span>
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.phone }">${newParts.phone }</span>
						</td>
					</tr>
					
					<tr>	
						<td>
							<span class="title-span">备注：</span> 
							<span class="val" title="${facadeBean.info.material.remark }">${facadeBean.info.material.remark }</span>	
						</td>
						<td class="input-td">
							<span class="val red-color" title="${newMaterial.remark }">${newMaterial.remark }</span>
						</td>
					</tr>
				</table>
			</c:if>
		</div>
	</c:if>
	
	<c:if test="${applyRecord.type == 2 }">
		<div style="margin-left: 10px;margin-top:20px;">
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
							<a target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">
								<img src="${resUrl}/${facadeBean.info.material.pic}" style="width: 100px;height: 50px;"></img>
							</a>
						</c:if>
					</td>
					<td class="title-td">备注：</td>
					<td class="value-td">${facadeBean.info.material.remark}</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
	
		<div class="title">试验结果</div>
		
		<c:if test="${not empty labReqList}">
			<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
			<div class="title">试验说明</div>
			<div style="margin-bottom: 20px;">
				<table class="info">
					<tr class="single-row">
						<td class="title-td">试验名称</td>
						<td class="title-td">任务号</td>
						<td class="title-td">实验要求</td>
						<td class="title-td">商定完成时间</td>
					</tr>
					
					<c:forEach items="${labReqList}" var="vo">
						<tr>
							<td>
								<c:choose>
									<c:when test="${vo.type eq 1}">零部件图谱试验</c:when>
									<c:when test="${vo.type eq 2}">原材料图谱试验</c:when>
									<c:when test="${vo.type eq 3}">零部件型式试验</c:when>
									<c:when test="${vo.type eq 4}">原材料型式试验</c:when>
								</c:choose>
							</td>
							<td>${vo.code}</td>
							<td style="word-break : break-all;line-height: 20px;">${vo.remark }</td>
							<td><fmt:formatDate value='${vo.time}' type="date" pattern="yyyy-MM-dd"/></td>
						</tr>
					</c:forEach>
				</table>
			</div>	
		</c:if>
				
		<div class="title">零部件试验结果</div>
		<table class="info">
			<tr class="single-row">
				<td class="remark-span">序号</td>
				<td class="remark-span">试验项目</td>
				<td class="remark-span">参考标准</td>
				<td class="remark-span">试验要求</td>
				<td class="remark-span">试验结果</td>
				<td class="remark-span">结果评价</td>
				<td class="remark-span">备注</td>
			</tr>
		
			<c:forEach items="${pPfResult_old}" var="vo" varStatus="status">
				<tr>
					<td style="padding-left:5px;">${status.index + 1}</td>
					<td class="value-td1">
						<span class="val" title="${vo.project}">${vo.project}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.standard}">${vo.standard}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.require}">${vo.require}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.result}">${vo.result}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.evaluate}">${vo.evaluate}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.remark}">${vo.remark}</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		<table class="info">
			<tr class="single-row">
				<td class="remark-span">序号</td>
				<td class="remark-span">试验项目</td>
				<td class="remark-span">参考标准</td>
				<td class="remark-span">试验要求</td>
				<td class="remark-span">试验结果</td>
				<td class="remark-span">结果评价</td>
				<td class="remark-span">备注</td>
			</tr>
		
			<c:forEach items="${pPfResult_new}" var="vo" varStatus="status">
				<tr>
					<td style="padding-left:5px;">${status.index + 1}</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.project}">${vo.project}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.standard}">${vo.standard}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.require}">${vo.require}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.result}">${vo.result}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.evaluate}">${vo.evaluate}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.remark}">${vo.remark}</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-top: 30px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="partsPat_conclusion" name="partsPat_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${partsPatConclusion_old.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${partsPatConclusion_old.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${partsPatConclusion_old.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="partsPat_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="partsPat_repNum" name="partsPat_repNum" value="${partsPatConclusion_old.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="partsPat_mainInspe" name="partsPat_mainInspe" value="${partsPatConclusion_old.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_examine" name="partsPat_examine" value="${partsPatConclusion_old.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_issue" name="partsPat_issue" value="${partsPatConclusion_old.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_receiveDate" name="partsPat_receiveDate" value="${partsPatConclusion_old.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_examineDate" name="partsPat_examineDate" value="${partsPatConclusion_old.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_issueDate" name="partsPat_issueDate" value="${partsPatConclusion_old.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_remark" name="partsPat_remark" value="${partsPatConclusion_old.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		
		<div style="margin-top: 5px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="partsPat_conclusion" name="partsPat_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${partsPatConclusion_new.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${partsPatConclusion_new.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${partsPatConclusion_new.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="partsPat_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="partsPat_repNum" name="partsPat_repNum" value="${partsPatConclusion_new.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="partsPat_mainInspe" name="partsPat_mainInspe" value="${partsPatConclusion_new.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_examine" name="partsPat_examine" value="${partsPatConclusion_new.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_issue" name="partsPat_issue" value="${partsPatConclusion_new.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_receiveDate" name="partsPat_receiveDate" value="${partsPatConclusion_new.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_examineDate" name="partsPat_examineDate" value="${partsPatConclusion_new.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_issueDate" name="partsPat_issueDate" value="${partsPatConclusion_new.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsPat_remark" name="partsPat_remark" value="${partsPatConclusion_new.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<table class="info">
			<tr class="single-row">
				<td class="title-td">图谱类型</td>
				<td class="title-td">图谱描述</td>
				<td class="title-td">选择图谱</td>
			</tr>
		
			<c:forEach items="${pAtlasResult_old}" var="vo" varStatus="vst">
				<tr style="line-height: 60px;">
					<td class="value-td">
						<c:if test="${vo.type == 1}">红外光分析</c:if>
						<c:if test="${vo.type == 2}">差热扫描</c:if>
						<c:if test="${vo.type == 3}">热重分析</c:if>
						<c:if test="${vo.type == 4}">样品照片</c:if>
					</td>
					<td class="value-td" title="${vo.remark}" style="word-break : break-all;line-height: 20px;">${vo.remark}</td>
					<td class="value-td">
						<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		<table class="info">
			<tr class="single-row">
				<td class="title-td">图谱类型</td>
				<td class="title-td">图谱描述</td>
				<td class="title-td">选择图谱</td>
			</tr>
		
			<c:forEach items="${pAtlasResult_new}" var="vo" varStatus="vst">
				<tr style="line-height: 60px;">
					<td class="value-td">
						<c:if test="${vo.type == 1}">红外光分析</c:if>
						<c:if test="${vo.type == 2}">差热扫描</c:if>
						<c:if test="${vo.type == 3}">热重分析</c:if>
						<c:if test="${vo.type == 4}">样品照片</c:if>
					</td>
					<td class="value-td" title="${vo.remark}" style="word-break : break-all;line-height: 20px;">${vo.remark}</td>
					<td class="value-td">
						<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-top: 30px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="partsAtl_conclusion" name="partsAtl_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${partsAtlConclusion_old.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${partsAtlConclusion_old.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${partsAtlConclusion_old.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="partsAtl_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="partsAtl_repNum" name="partsAtl_repNum" value="${partsAtlConclusion_old.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="partsAtl_mainInspe" name="partsAtl_mainInspe" value="${partsAtlConclusion_old.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_examine" name="partsAtl_examine" value="${partsAtlConclusion_old.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_issue" name="partsAtl_issue" value="${partsAtlConclusion_old.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_receiveDate" name="partsAtl_receiveDate" value="${partsAtlConclusion_old.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_examineDate" name="partsAtl_examineDate" value="${partsAtlConclusion_old.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_issueDate" name="partsAtl_issueDate" value="${partsAtlConclusion_old.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_remark" name="partsAtl_remark" value="${partsAtlConclusion_old.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		
		<div style="margin-top: 5px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="partsAtl_conclusion" name="partsAtl_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${partsAtlConclusion_new.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${partsAtlConclusion_new.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${partsAtlConclusion_new.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="partsAtl_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="partsAtl_repNum" name="partsAtl_repNum" value="${partsAtlConclusion_new.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="partsAtl_mainInspe" name="partsAtl_mainInspe" value="${partsAtlConclusion_new.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_examine" name="partsAtl_examine" value="${partsAtlConclusion_new.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_issue" name="partsAtl_issue" value="${partsAtlConclusion_new.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_receiveDate" name="partsAtl_receiveDate" value="${partsAtlConclusion_new.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_examineDate" name="partsAtl_examineDate" value="${partsAtlConclusion_new.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_issueDate" name="partsAtl_issueDate" value="${partsAtlConclusion_new.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="partsAtl_remark" name="partsAtl_remark" value="${partsAtlConclusion_new.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<div class="title" style="margin-top: 10px;">原材料试验结果</div>
		<table class="info">
			<tr class="single-row">
				<td class="remark-span">序号</td>
				<td class="remark-span">试验项目</td>
				<td class="remark-span">参考标准</td>
				<td class="remark-span">试验要求</td>
				<td class="remark-span">试验结果</td>
				<td class="remark-span">结果评价</td>
				<td class="remark-span">备注</td>
			</tr>
		
			<c:forEach items="${mPfResult_old}" var="vo" varStatus="status">
				<tr>
					<td style="padding-left:5px;">${status.index + 1}</td>
					<td class="value-td1">
						<span class="val" title="${vo.project}">${vo.project}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.standard}">${vo.standard}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.require}">${vo.require}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.result}">${vo.result}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.evaluate}">${vo.evaluate}</span>
					</td>
					<td class="value-td1">
						<span class="val" title="${vo.remark}">${vo.remark}</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		<table class="info">
			<tr class="single-row">
				<td class="remark-span">序号</td>
				<td class="remark-span">试验项目</td>
				<td class="remark-span">参考标准</td>
				<td class="remark-span">试验要求</td>
				<td class="remark-span">试验结果</td>
				<td class="remark-span">结果评价</td>
				<td class="remark-span">备注</td>
			</tr>
		
			<c:forEach items="${mPfResult_new}" var="vo" varStatus="status">
				<tr>
					<td style="padding-left:5px;">${status.index + 1}</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.project}">${vo.project}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.standard}">${vo.standard}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.require}">${vo.require}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.result}">${vo.result}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.evaluate}">${vo.evaluate}</span>
					</td>
					<td class="value-td1">
						<span class="val red-color" title="${vo.remark}">${vo.remark}</span>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-top: 30px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="matPat_conclusion" name="matPat_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
							<option value="">请选择</option>
							<option value="合格" <c:if test="${matPatConclusion_old.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${matPatConclusion_old.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${matPatConclusion_old.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="matPat_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="matPat_repNum" name="matPat_repNum" value="${matPatConclusion_old.repNum }" class="easyui-textbox" style="width:115px">
					</td>
					<td class="value-td1">
						<input id="matPat_mainInspe" name="matPat_mainInspe" value="${matPatConclusion_old.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_examine" name="matPat_examine" value="${matPatConclusion_old.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_issue" name="matPat_issue" value="${matPatConclusion_old.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_receiveDate" name="matPat_receiveDate" value="${matPatConclusion_old.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_examineDate" name="matPat_examineDate" value="${matPatConclusion_old.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_issueDate" name="matPat_issueDate" value="${matPatConclusion_old.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_remark" name="matPat_remark" value="${matPatConclusion_old.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		
		<div style="margin-top: 5px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="matPat_conclusion" name="matPat_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
							<option value="">请选择</option>
							<option value="合格" <c:if test="${matPatConclusion_new.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${matPatConclusion_new.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${matPatConclusion_new.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="matPat_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="matPat_repNum" name="matPat_repNum" value="${matPatConclusion_new.repNum }" class="easyui-textbox" style="width:115px">
					</td>
					<td class="value-td1">
						<input id="matPat_mainInspe" name="matPat_mainInspe" value="${matPatConclusion_new.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_examine" name="matPat_examine" value="${matPatConclusion_new.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_issue" name="matPat_issue" value="${matPatConclusion_new.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_receiveDate" name="matPat_receiveDate" value="${matPatConclusion_new.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_examineDate" name="matPat_examineDate" value="${matPatConclusion_new.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_issueDate" name="matPat_issueDate" value="${matPatConclusion_new.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matPat_remark" name="matPat_remark" value="${matPatConclusion_new.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<table class="info">
			<tr class="single-row">
				<td class="title-td">图谱类型</td>
				<td class="title-td">图谱描述</td>
				<td class="title-td">选择图谱</td>
			</tr>
		
			<c:forEach items="${mAtlasResult_old}" var="vo" varStatus="vst">
				<tr style="line-height: 60px;">
					<td class="value-td">
						<c:if test="${vo.type == 1}">红外光分析</c:if>
						<c:if test="${vo.type == 2}">差热扫描</c:if>
						<c:if test="${vo.type == 3}">热重分析</c:if>
						<c:if test="${vo.type == 4}">样品照片</c:if>
					</td>
					<td class="value-td" title="${vo.remark}" style="word-break : break-all;line-height: 20px;">${vo.remark}</td>
					<td class="value-td">
						<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		<table class="info">
			<tr class="single-row">
				<td class="title-td">图谱类型</td>
				<td class="title-td">图谱描述</td>
				<td class="title-td">选择图谱</td>
			</tr>
		
			<c:forEach items="${mAtlasResult_new}" var="vo" varStatus="vst">
				<tr style="line-height: 60px;">
					<td class="value-td">
						<c:if test="${vo.type == 1}">红外光分析</c:if>
						<c:if test="${vo.type == 2}">差热扫描</c:if>
						<c:if test="${vo.type == 3}">热重分析</c:if>
						<c:if test="${vo.type == 4}">样品照片</c:if>
					</td>
					<td class="value-td" title="${vo.remark}" style="word-break : break-all;line-height: 20px;">${vo.remark}</td>
					<td class="value-td">
						<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<div style="margin-top: 30px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="matAtl_conclusion" name="matAtl_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${matAtlConclusion_old.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${matAtlConclusion_old.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${matAtlConclusion_old.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="matAtl_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="matAtl_repNum" name="matAtl_repNum" value="${matAtlConclusion_old.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="matAtl_mainInspe" name="matAtl_mainInspe" value="${matAtlConclusion_old.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_examine" name="matAtl_examine" value="${matAtlConclusion_old.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_issue" name="matAtl_issue" value="${matAtlConclusion_old.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_receiveDate" name="matAtl_receiveDate" value="${matAtlConclusion_old.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_examineDate" name="matAtl_examineDate" value="${matAtlConclusion_old.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_issueDate" name="matAtl_issueDate" value="${matAtlConclusion_old.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_remark" name="matAtl_remark" value="${matAtlConclusion_old.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
		
		<div style="margin-top: 5px;">	
			<table class="info">
				<tr class="single-row">
					<td class="remark-span"><span class="req-span">*</span>试验结论</td>
					<td class="remark-span"><span class="req-span">*</span>报告编号</td>
					<td class="remark-span"><span class="req-span">*</span>主检</td>
					<td class="remark-span"><span class="req-span">*</span>审核</td>
					<td class="remark-span"><span class="req-span">*</span>签发</td>
					<td class="remark-span"><span class="req-span">*</span>收样时间</td>
					<td class="remark-span"><span class="req-span">*</span>试验时间</td>
					<td class="remark-span"><span class="req-span">*</span>签发时间</td>
					<td class="remark-span">备注</td>
				</tr>
				
				<tr>
					<td class="value-td1">
						<select id="matAtl_conclusion" name="matAtl_conclusion" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'" >
							<option value="">请选择</option>
							<option value="合格" <c:if test="${matAtlConclusion_new.conclusion == '合格' }">selected="selected"</c:if>>合格</option>
							<option value="不合格" <c:if test="${matAtlConclusion_new.conclusion == '不合格' }">selected="selected"</c:if>>不合格</option>
							<option value="其它" <c:if test="${matAtlConclusion_new.conclusion == '其它' }">selected="selected"</c:if>>其它</option>
						</select>
						<span id="matAtl_conclusion_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="matAtl_repNum" name="matAtl_repNum" value="${matAtlConclusion_new.repNum }" class="easyui-textbox" style="width:115px" >
					</td>
					<td class="value-td1">
						<input id="matAtl_mainInspe" name="matAtl_mainInspe" value="${matAtlConclusion_new.mainInspe }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_examine" name="matAtl_examine" value="${matAtlConclusion_new.examine }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_issue" name="matAtl_issue" value="${matAtlConclusion_new.issue }" class="easyui-textbox" style="width:115px" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_receiveDate" name="matAtl_receiveDate" value="${matAtlConclusion_new.receiveDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_examineDate" name="matAtl_examineDate" value="${matAtlConclusion_new.examineDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_issueDate" name="matAtl_issueDate" value="${matAtlConclusion_new.issueDate }" class="easyui-datebox" style="width:115px" data-options="editable:false" >
					</td>
					
					<td class="value-td1">
						<input id="matAtl_remark" name="matAtl_remark" value="${matAtlConclusion_new.remark }" class="easyui-textbox" style="width:115px" >
					</td>
				</tr>
			</table>
		</div>
		
		<c:if test="${ applyRecord.task.type == 2 or applyRecord.task.type == 3 }">
			<div class="title" style="margin-top: 30px;">结论</div>
			<div>
				<table style="width: 98%; font-size: 14px;">
					<tr style="background: #F0F0F0; height: 30px; font-weight: bold; text-align: center;">
						<td>类型</td>
						<td>样品照片</td>
						<td>红外光分析</td>
						<td>差热分析</td>
						<td>热重分析</td>
						<td>结论</td>
					</tr>
					
					<c:forEach items="${compareResult_old}" var="m">
						<tr>
							<td style="font-weight:bold;">${m.key}</td>
							<c:forEach items="${m.value}" var="vo" varStatus="vst">
								<td align="center">
									<div style="margin-top:5px;">${vo.state }
										<label><input name="${m.key}_radio_${vst.index}_old" type="radio" value="1" <c:if test="${vo.state == 1}">checked</c:if> disabled/>一致</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<label><input name="${m.key}_radio_${vst.index}_old" type="radio" value="2" <c:if test="${vo.state == 2}">checked</c:if> disabled/>不一致 </label> 
									</div>
									<div style="margin-top:5px;">
										<textarea rows="1" cols="25" disabled>${vo.remark}</textarea>
									</div>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</div>
			
			<div style="margin-left: 10px;color:red;margin-top: 20px;">修改后结果：</div>
			
			<div>
				<table style="width: 98%; font-size: 14px;">
					<tr style="background: #F0F0F0; height: 30px; font-weight: bold; text-align: center;">
						<td>类型</td>
						<td>样品照片</td>
						<td>红外光分析</td>
						<td>差热分析</td>
						<td>热重分析</td>
						<td>结论</td>
					</tr>
					
					<c:forEach items="${compareResult_new}" var="m">
						<tr>
							<td style="font-weight:bold;">${m.key}</td>
							<c:forEach items="${m.value}" var="vo" varStatus="vst">
								<td align="center">
									<div style="margin-top:5px;">
										<label><input name="${m.key}_radio_${vst.index}_new" type="radio" value="1" <c:if test="${vo.state == 1}">checked</c:if> disabled/>一致</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<label><input name="${m.key}_radio_${vst.index}_new" type="radio" value="2" <c:if test="${vo.state == 2}">checked</c:if> disabled/>不一致 </label> 
									</div>
									<div style="margin-top:5px;">
										<textarea rows="1" cols="25" disabled>${vo.remark}</textarea>
									</div>
								</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>
			</div>
		
		</c:if>
		
	</c:if>
	
	
		
	 <div id="errorMsg" style="font-weight:bold;color:red;text-align:center;margin-top:10px;"></div>
	 <div style="text-align:center;margin-top:15px;margin-bottom: 15px;" class="data-row">
		<a href="javascript:void(0);"  onclick="notPass()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">终止</a>
	 </div>

	 <div id="dlg" class="easyui-dialog" title="终止" style="width: 400px; height: 250px; padding: 10px" closed="true" data-options="modal:true">
		<input id="remark" class="easyui-textbox" label="终止原因：" labelPosition="top" multiline="true" style="width: 350px;height: 100px;"/>
		
		<div style="font-weight:bold;color:red;text-align:center;margin-top:10px;" id="remark_error"></div>
		<div align=center style="margin-top: 15px;">
			<a href="javascript:void(0);"  onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
			<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</div>
	
	<script type="text/javascript">
		function doSubmit(result, remark){
			$("#errorMsg").html("");
			
			var remark = $("#remark").textbox("getValue");
			if(isNull(remark)){
				$("#remark_error").html("请输入原因");
				$("#remark").next('span').find('input').focus();
				return false;
			}
			$("#remark_error").html("");
			
			$.ajax({
				url: "${ctx}/apply/end",
				data: {
					"id": "${applyRecord.id}",
					"remark": remark
				},
				success: function(data){
					if(data.success){
						$("#dlg").dialog("close");
						closeDialog(data.msg);
					}else{
						$("#remark_error").html(data.msg);
					}
				}
			});
		}
		
		function notPass(catagory){
			$("#remark").textbox("setValue", "");
			$("#dlg").dialog("open");
		}
		
		function doCancel(){
			$("#dlg").dialog("close");
		}
		
		
	</script>
	
	<style type="text/css">
		.title {
			margin-left: 10px;
			margin-bottom: 8px;
			font-size: 14px;
			color: #1874CD;
    		font-weight: bold;
		}
		
		.info{
			width:98%;
			margin-left: 5px;
			font-size: 14px;
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
		
		.red-color{
			color: red;
			font-weight: bold;
		}
		
		.title-span{
			display: inline-block;
			width: 80px;
		}
		
		.input-td{
			padding-left: 50px;
			padding-right: 20px;
		}
		
		.single-row{
			background: #F0F0F0;
		}
		
		.couple-row{
			background: #f5f5f5;
		}
	
		.remark-span{
			font-weight: bold;
		}
	</style>
		
		
	
</body>
