<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<div style="margin-left:10px;">
		<div class="data-row">
			<span class="data-title">类型：</span>
			<span class="data-content">${opeartionLog.type}</span>
		</div>
		
		<div class="data-row">
			<span class="data-title">操作：</span>
			<span class="data-content">${opeartionLog.operation}</span>
		</div>
		
		<div class="data-row">
			<span class="data-title">用户：</span>
			<span class="data-content">${opeartionLog.userName}</span>
		</div>
		
		<div class="data-row">
			<span class="data-title">Client IP：</span>
			<span class="data-content">${opeartionLog.clientIp}</span>
		</div>
		
		<div class="data-row">
			<span class="data-title">User Agent：</span>
			<span class="data-content">${opeartionLog.userAgent}</span>
		</div>
		
		<div class="data-row">
			<span class="data-title">时间：</span>
			<span class="data-content"><fmt:formatDate value='${opeartionLog.time}' type="date" pattern="yyyy-MM-dd HH:mm:ss" /></span>
		</div>
	</div>	
	
	<div style="margin-top:10px;">
		<div style="font-weight:bold;margin-left:10px;">Detail Information</div>
		
		<c:choose>
			<c:when test="${not empty dataList}">
				<c:choose>
					<c:when test="${opeartionLog.operation != 'Move' }">
						<table class="table table-bordered table-striped table-condensed table-hover" style="width: 70%;margin-left: 5px;">
							<thead>
								<tr>
									<th>Field</th>
									<th>Value</th>
									
									<c:if test="${operation == '编辑'}">
										<th>Old Value</th>
									</c:if>
								</tr>
							</thead>
							
							<c:forEach items="${dataList}" var="vo" > 
								<tr>
									<td>${vo.name}</td>
									<c:choose>
										<c:when test="${opeartionLog.operation == '编辑'}">
											<td>${vo.newValue}</td>
											<td>${vo.oldValue}</td>
										</c:when>
										<c:when test="${opeartionLog.operation == '新建'}">
											<td>${vo.newValue}</td>
										</c:when>
										<c:otherwise>
											<td>${vo.oldValue}</td>
										</c:otherwise>
									</c:choose>
								</tr>
							</c:forEach> 
						</table>
					</c:when>
					<c:otherwise>
						<c:forEach items="${dataList}" var="vo" > 
							<p>区域名称： ${vo.name}</p>
							<p>从： ${vo.oldValue}</p>
							<p>到: ${vo.newValue }</p>
						</c:forEach>
					</c:otherwise>
				</c:choose>	
			</c:when>
			<c:otherwise>
				<div style="margin-left: 10px;margin-top: 5px;">${opeartionLog.detail }</div>
			</c:otherwise>
		</c:choose>
	
	</div>
	
	<style type="text/css">
		.data-row{
			line-height: 30px;
		}
		
		.data-title{
			display:inline-block;
			width: 120px;
		}
		
		.table thead > tr > th, .table tbody > tr > th, .table tfoot > tr > th, .table thead > tr > td, .table tbody > tr > td, .table tfoot > tr > td {
			padding: 5px;
			text-align:left;
			font-weight: normal;
			font-size: 14px;
		}
	</style>
	

</body>
