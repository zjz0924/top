<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

	<title>材料数据库登陆认证</title>
	<link href="${ctx}/resources/Wopop_files/style_log.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/Wopop_files/style.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/Wopop_files/userpanel.css">
	<script src="${ctx}/resources/js/jquery-2.1.1.min.js"></script>
</head>

<body class="login" mycollectionplug="bind">
	<div class="login_m">
		<form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal login" >
			<div class="login_boder">
				<div class="mainTitle">SGMW材料数据库平台登陆认证</div>
			
				<div class="login_padding" id="login_model">
					<div>
						<span class="labelSpan">用户名</span>
						<input type="text" id="username" name="username" class="txt_input txt_input2">
					</div>
					
					<div>
						<span class="labelSpan">密码</span>
						<input type="password" name="password" id="password" class="txt_input">
					</div>
	
					<div>
						<span class="labelSpan">验证码</span>
						<input name="vcode" id="vcode" type="text" maxLength="4" class="txt_input" style="width: 130px;"/>
		            	<span>
		            		<img src="${ctx}/verifycode" id="verifyimg" title="看不清？点击刷新！" style="width:80px;height:25px;"/>
		            	</span>
					</div>
					
					<div class="rem_sub">
						<span id="error_message" style="color:#d25; font-weight:bold;">${error}</span>
						<label> 
							<input type="button" class="sub_button" name="button" value="登录" style="opacity: 0.7;"  onclick="doSubmit()">
						</label>
					</div>
				</div>
			</div>
		</form>
	</div>
	
	<script type="text/javascript">
		$(function(){
			$("#verifyimg").click(function(){
				refreshVerify("verifyimg");
			});
			
			/**有父窗口则在父窗口打开*/  
	        if(self!=top){top.location=self.location;}  
		});
		
		document.onkeydown = function(event){
			var e = event || window.event || arguments.callee.caller.arguments[0];
			if(e && e.keyCode == 13){ // enter 键
				doSubmit();
			}
		}; 
		
		function checkVerify(verify) {
			var ret = false;
			$.ajax({
		        type: "GET",
		        cache: false,
		        url: "${ctx}/checkverify",
		        async: false,
		        data: "vcode="+verify,
		        success: function(msg) {
		        	ret = msg;
		        }
		    });
			
			return ret;
		}
		
		function refreshVerify(id){
			var imgurl = "${ctx}/verifycode?rnd="+new Date().getTime();
			$("#"+id).attr("src", imgurl);
		}

		function doSubmit(){
			var username = $("#username").val();
			var password = $("#password").val();
			var verifyCode = $("#vcode").val();
		
			if (username.length == 0){
				$("#error_message").html("请输入账号");
				$("#username").focus();
				return false;
			}else{
				$("#error_message").html("");
			}					
			
			if (password.length == 0){
				$("#error_message").html("请输入密码");
				$("#password").focus();
				return false;
			}
			
			if (verifyCode.length == 0){
				$("#error_message").html("请输入验证码");
				$("#vcode").focus();
				return false;
			}else {
				if (checkVerify(verifyCode) != true) {
		    		$("#error_message").html("验证码输入错误，看不清？点击图片换一个");
		    		$("#vcode").focus();
		    		
		    		//刷新验证码
		    		refreshVerify("verifyimg");
		    		return false;
				}else{
					$("#error_message").html("");
				}
			}
			
			$('#loginForm').submit();
		}
	</script>

</body>
</html>