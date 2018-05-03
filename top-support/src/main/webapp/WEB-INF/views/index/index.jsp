<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>SGMW</title>

		<script src="${ctx}/resources/js/jquery-2.1.1.min.js"></script>
		<script src="${ctx}/resources/js/slides.js"></script>
		
		<style type="text/css">
			.title-sytle{
				display:inline-block;
				width: 80px;
				text-align: right;
				padding-right: 10px;
			}
			
			.title-style1{
				font-weight:bold;
				color: #1874CD;
				font-size:16px;
			}
			
			.row-style{
				height: 35px;
			}
			
			.row-style1{
				height: 25px;
			}
			
			.row-style2{
				display:inline-block;
				width: 150px;
				font-size:14px;
			}
			
			.row-content{
				font-size:14px;
				font-weight:bold;
				color:red;
			}
		</style>
		
		<script type="text/javascript">
			$(function() {
				$.ajax({
					url : "${ctx}/getTaskNum",
					success : function(data) {
						$("#examineNum").html(data.data[0]);
						$("#approveNum").html(data.data[1]);
						$("#patternUploadNum").html(data.data[2]);
						$("#atlasUploadNum").html(data.data[3]);
						$("#compareNum").html(data.data[4]);
						$("#waitConfirmNum").html(data.data[5]);
						$("#finishConfirmNum").html(data.data[6]);
					}
				});
			});
		</script>
	</head>

	<body style="margin:0px;">
		<%@include file="../common/header.jsp"%>
		
		<div class="main" style="margin-top:30px;">
			<div style="background-color:white;margin-left: 20px;margin-right:20px;margin-top: 20px;height:650px;">
				<div style="padding-top:30px;padding-left:35px;font-size: 20px;">欢迎进入上汽通用五菱材料管理平台！</div>
				<div style="padding-left:40px;margin-top: 20px;font-size: 16px;">
					<p class="row-style"><span class="title-sytle">您好：</span>${currentAccount.userName }</p>
					<p class="row-style"><span class="title-sytle">角色：</span>${currentAccount.role.name }</p>
					<p class="row-style"><span class="title-sytle">机构：</span>${currentAccount.org.name }</p>
					<p class="row-style"><span class="title-sytle">登录账号：</span>${currentAccount.userName }</p>
					<p class="row-style"><span class="title-sytle">真实姓名：</span>${currentAccount.nickName }</p>
					<p class="row-style"><span class="title-sytle">联系电话：</span>${ currentAccount.mobile }</p>
				</div>
				
				<div style="margin-left:35px;margin-top: 25px;">
					<div class="title-style1" style="margin-bottom: 10px;">任务情况：</div>
					<div class="row-style1"><span class="row-style2">待审核</span><span class="row-content" id="examineNum"></span>  个</div>
					<div class="row-style1"><span class="row-style2">待审批</span><span class="row-content" id="approveNum"></span>  个</div>
					
					<div class="row-style1"><span class="row-style2">待上传结果</span></div>
					<div class="row-style1"><span class="row-style2">&nbsp;- 型式结果</span><span class="row-content" id="patternUploadNum"></span>  个</div>
					<div class="row-style1"><span class="row-style2">&nbsp;- 图谱结果</span><span class="row-content" id="atlasUploadNum"></span>  个</div>
					
					<div class="row-style1"><span class="row-style2">待比对</span><span class="row-content" id="compareNum"></span>  个</div>
					
					<div class="row-style1"><span class="row-style2">待结果确认</span></div>
					<div class="row-style1"><span class="row-style2">&nbsp;- 待上传</span><span class="row-content" id="waitConfirmNum"></span>  个</div>
					<div class="row-style1"><span class="row-style2">&nbsp;- 已上传</span><span class="row-content" id="finishConfirmNum"></span>  个</div>
					
					<div class="title-style1" style="margin-top: 15px;">消息提醒：</div>
					<div style="margin-top:10px;font-size:14px;">您还有 <span style="font-weight:bold;color:red;">${unread}</span> 条消息未读，请及时阅读。</div>
				</div>
			</div>
		</div>
		
		<!-- footer -->
		<%@include file="../common/footer.jsp"%>
	</body>
</html>