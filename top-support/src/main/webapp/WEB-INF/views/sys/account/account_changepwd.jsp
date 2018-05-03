<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>
<%@include file="/page/NavPageBar.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<%@include file="../../common/source.jsp"%>
	
	<script type="text/javascript">
		function updatePwd(){
			var newPwd = $("#newPwd").val();
			var confirmPwd = $("#confirmPwd").val();
			var oldPwd = $("#oldPwd").val();
				
			if(isNull(oldPwd)){
				art.dialog.tips("旧密码必填", 2, "error");
				$("#oldPwd").focus();
				return false;
			}
			
			if(isNull(newPwd)){
				art.dialog.tips("新密码必填", 2, "error");
				$("#newPwd").focus();
				return false;
			}
			
			if(isNull(confirmPwd)){
				art.dialog.tips("确认密码必填", 2, "error");
				$("#confirmPwd").focus();
				return false;
			}
			
			if(newPwd != confirmPwd){
				art.dialog.tips("新密码与确认密码不一致", 2, "error");
				return false;
			}
			
			$.ajax({
				url: "${ctx}/account/updatePwd?date=" + new Date(),
				data: {
					oldPwd: oldPwd,
					newPwd: newPwd
				},
				success: function(data){
					if(data.success){
						art.dialog.tips(data.msg, 2, "succeed", function(){
							window.location.href = "${ctx}/loginout";
						});
					}else{
						art.dialog.tips(data.msg, 2, "error");
					}
				}
			});
		}
		
		function doCancel(){
			$(":input").val("");
		}
		
		function isNull(data){
			if(data == null || data == "" || data == "undefined"){
				return true;
			}else{
				return false;
			}
		}
	</script>
	
</head>

<body style="overflow-x: hidden;overflow-y: hidden;">

	<div class="row" >
		<div class="col-lg-12">
			<div class="panel panel-default">
				<!-- body start -->
				<div class="panel-body" >
					<div class="form-group height_50">
						<label class="col-md-2 control-label"><span class="asterisk">*</span>原密码</label>
						<div class="col-md-3">
							<input type="password" id="oldPwd" name="oldPwd" class="form-control" placeholder="旧密码" autofocus="autofocus">
						</div>
					</div>
					
					<div class="form-group height_50">
						<label class="col-md-2 control-label"><span class="asterisk">*</span>新密码</label>
						<div class="col-md-3">
							<input type="password" id="newPwd" name="newPwd" class="form-control" placeholder="新密码">
						</div>
					</div>
					
					<div class="form-group height_50">
						<label class="col-md-2 control-label"><span class="asterisk">*</span>确认密码</label>
						<div class="col-md-3">
							<input type="password" id="confirmPwd" name="confirmPwd" class="form-control" placeholder="确认密码">
						</div>
					</div>
					
					<div class="form-group height_50 text-center">
						<button id="saveBtn" type="button" class="btn btn-primary" onclick="updatePwd();">提交</button>
						<button type="button" class="btn btn-danger" onclick="doCancel();">重置</button>
					</div>
					
				</div>
				<!-- body end -->
			</div>
		</div>
	</div>
</body>
</html>