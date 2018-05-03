<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<div style="margin-left: 10px;margin-top:20px;margin-bottom:30px;">
		<div style="width: 98%;">
			<table class="info">
				<tr class="couple-row">
					<td class="title-td">任务号：</td>
					<td class="value-td">${facadeBean.task.code}</td>
				</tr>
				<tr class="single-row">
					<td class="title-td">主题：</td>
					<td class="value-td">${facadeBean.subject}</td>
				</tr>
				<tr class="couple-row">
					<td class="title-td">内容：</td>
					<td class="value-td">${facadeBean.content}</td>
				</tr>
				<tr class="single-row">
					<td class="title-td">类型：</td>
					<td class="value-td">
						<c:if test="${ facadeBean.type == 1}">结果消息</c:if>
						<c:if test="${ facadeBean.type == 2}">收费通知</c:if>
						<c:if test="${ facadeBean.type == 3}">警告书</c:if>
					</td>
				</tr>
				
				<tr class="couple-row">
					<td class="title-td">接收地址：</td>
					<td class="value-td">${facadeBean.addr}</td>
				</tr>
				
				<tr class="single-row">
					<td class="title-td">发送人：</td>
					<td class="value-td">${facadeBean.account.userName}</td>
				</tr>
				<tr class="couple-row">
					<td class="title-td">发送地址：</td>
					<td class="value-td">${facadeBean.orginEmail}</td>
				</tr>
				<tr class="single-row">
					<td class="title-td">发送时间：</td>
					<td class="value-td"><fmt:formatDate value='${facadeBean.createTime}' type="date" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				
			</table>
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
		}
		
		.info tr{
			height: 30px;
		}
		
		.title-td {
			width:16%;
			padding-left: 5px;
			font-weight: bold;
		}
		
		.value-td{
			width:84%;
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
