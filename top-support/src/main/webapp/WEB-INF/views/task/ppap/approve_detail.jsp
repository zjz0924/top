<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
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
							<a target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">${fn:substringAfter(facadeBean.info.material.pic, "/")}</a>
						</c:if>
					</td>
					<td class="title-td">备注：</td>
					<td class="value-td">${facadeBean.info.material.remark}</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<div class="title">任务信息</div>
		<div style="width: 98%;">
			<table class="info">
				<tr class="single-row">
					<td class="title-td">申请人：</td>
					<td class="value-td">${facadeBean.taskInfo.applicant}</td>
					<td class="title-td">科室：</td>
					<td class="value-td">${facadeBean.taskInfo.department}</td>
				</tr>
				
				<tr class="couple-row">
					<td class="title-td">零件图号：</td>
					<td class="value-td">${facadeBean.taskInfo.figure}</td>
					<td class="title-td">样品数量：</td>
					<td class="value-td">${facadeBean.taskInfo.num}</td>
				</tr>
				
				<tr class="single-row">
					<td class="title-td">样品来源：</td>
					<td class="value-td">${facadeBean.taskInfo.origin}</td>
					<td class="title-td">抽检原因：</td>
					<td class="value-td">${facadeBean.taskInfo.reason}</td>
				</tr>
				
				<tr class="couple-row">
					<td class="title-td">实验费用出处：</td>
					<td class="value-td">${facadeBean.taskInfo.provenance}</td>
				</tr>
			</table>
		</div>
		
		<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
		
		<c:choose>
			<c:when test="${approveType == 3}">
				<c:if test="${not empty labReqList}">
					<div class="title">试验说明</div>
					<div style="margin-bottom: 20px;">
						<table class="info">
							<tr class="single-row">
								<td class="title-td">试验编号</td>
								<td class="title-td">试验名称</td>
								<td class="title-td">任务号</td>
								<td class="title-td">实验要求</td>
								<td class="title-td">商定完成时间</td>
							</tr>
							
							<c:forEach items="${labReqList}" var="vo">
								<tr>
									<td>
										<c:choose>
											<c:when test="${vo.type eq 1}">${facadeBean.partsAtlCode}</c:when>
											<c:when test="${vo.type eq 2}">${facadeBean.matAtlCode}</c:when>
											<c:when test="${vo.type eq 3}">${facadeBean.partsPatCode}</c:when>
											<c:when test="${vo.type eq 4}">${facadeBean.matPatCode}</c:when>
										</c:choose>
									</td>
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
			
				<div class="title">试验分配</div>
				<div>
					<table class="info">
						<tr class="single-row">
							<td class="title-td">试验名称</td>
							<td class="title-td">分配实验室</td>
						</tr>
						
						<tr>
							<td class="value-td">零部件图谱试验</td>
							<td class="value-td">
								<c:if test="${not empty facadeBean.partsAtl}">
									<input id="partsAtlId" name="partsAtlId">
								</c:if>
								<c:if test="${empty facadeBean.partsAtl}">
									不分配
								</c:if>
							</td>
						</tr>
						
						<tr>
							<td class="value-td">原材料图谱试验</td>
							<td class="value-td">
								<c:if test="${not empty facadeBean.matAtl}">
									<input id="matAtlId" name="matAtlId">
								</c:if>
								<c:if test="${empty facadeBean.matAtl}">
									不分配
								</c:if>
							</td>
						</tr>
					</table>
				</div>
				
				<div style="text-align:center;margin-top:25px;margin-bottom: 15px;" class="data-row">
					<a href="javascript:void(0);"  onclick="approve(1, '', 3)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">同意</a>&nbsp;&nbsp;
					<a href="javascript:void(0);"  onclick="notPass(3)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">不同意</a>
				</div>
			
			</c:when>
			<c:when test="${approveType == 2}"> 
				<div class="title">试验结果</div>
		
				<div class="title">零部件图谱试验结果</div>
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
								<c:if test="${not empty vo.pic}">
									<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>									</c:if>
								<c:if test="${empty vo.pic}">
									<span class="img-span1">暂无</span>
								</c:if>
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
								<c:if test="${not empty vo.pic}">
									<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>									</c:if>
								<c:if test="${empty vo.pic}">
									<span class="img-span1">暂无</span>
								</c:if>
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
		
				<div class="title">原材料图谱试验结果</div>
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
								<c:if test="${not empty vo.pic}">
									<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>									</c:if>
								<c:if test="${empty vo.pic}">
									<span class="img-span1">暂无</span>
								</c:if>
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
								<c:if test="${not empty vo.pic}">
									<a href="${resUrl}/${vo.pic}" target="_blank"><img src="${resUrl}/${vo.pic}" style="width: 100px;height: 50px;"></a>									</c:if>
								<c:if test="${empty vo.pic}">
									<span class="img-span1">暂无</span>
								</c:if>
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
				
				
				
				<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>
				
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
										<div style="margin-top:5px;">
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
				
				 <div style="text-align:center;margin-top:15px;margin-bottom: 15px;" class="data-row">
					<a href="javascript:void(0);"  onclick="approve(1,'', 2)" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">同意</a>&nbsp;&nbsp;
					<a href="javascript:void(0);"  onclick="notPass(2)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">不同意</a>
				 </div>
			</c:when>
		</c:choose>

		<div id="dlg" class="easyui-dialog" title="审批" style="width: 400px; height: 200px; padding: 10px" closed="true" data-options="modal:true">
			<input id="remark" class="easyui-textbox" label="不同意原因：" labelPosition="top" multiline="true" style="width: 350px;height: 100px;"/>
			<input type="hidden" id="catagory" name="catagory">
			
			<div align=center style="margin-top: 15px;">
				<a href="javascript:void(0);"  onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
				<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
			</div>
		</div>
	</div>
			
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
		
		.img-span1{
			width: 100px;
			height: 50px;
			display:inline-block;
			border:0.5px dashed #C9C9C9;
			text-align:center;
			line-height:50px;
		}
	</style>
	
	<script type="text/javascript">
		// 是否提交中
		var saving = false;
		
		var partsAtl = "${facadeBean.partsAtl}";
		if(!isNull(partsAtl)){
			$('#partsAtlId').combotree({
				url: '${ctx}/org/getTreeByType?type=3',
				multiple: false,
				animate: true,
				width: '250px'
			});
			setupTree("partsAtlId");
			$("#partsAtlId").combotree("setValue", "${facadeBean.partsAtl.id}");
		}
		
		var matAtl = "${facadeBean.matAtl}";
		if(!isNull(matAtl)){
			$('#matAtlId').combotree({
				url: '${ctx}/org/getTreeByType?type=3',
				multiple: false,
				animate: true,
				width: '250px'			
			});
			setupTree("matAtlId");
			$("#matAtlId").combotree("setValue", "${facadeBean.matAtl.id}");
		}
	
		function approve(result, remark, catagory){
			if(saving){
				return false;
			}
			saving = true;
			
			var partsAtlResult = "${facadeBean.partsAtl}";
			var partsAtlId = "";
			if(!isNull(partsAtlResult)){
				partsAtlId = $("#partsAtlId").combotree("getValue");
			}
			
			var matAtlResult = "${facadeBean.matAtl}";
			var matAtlId = "";
			if(!isNull(matAtlResult)){
				matAtlId = $("#matAtlId").combotree("getValue");
			}
			
			$.ajax({
				url: "${ctx}/ppap/approve",
				data: {
					"id": "${facadeBean.id}",
					"result": result,
					"remark": remark,
					"catagory": catagory,
					"partsAtlId": partsAtlId,
					"matAtlId": matAtlId
				},
				success: function(data){
					saving = false;
					if(data.success){
						closeDialog(data.msg);
					}else{
						errorMsg(data.msg);						
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
	</script>	
	
</body>
