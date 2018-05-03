<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<input type="hidden" id="id" name="id" value="${facadeBean.id}"/>
	
	<div style="margin-top:15px;margin-left:20px;">
		<div id="infoDiv">
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>用户名：</span> 
				<input id="userName" name="userName" value="${facadeBean.userName}" disabled class="easyui-textbox">
				<span id="userName_error" class="error-message"></span>
			</div>
			
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>姓名：</span> 
				<input id="nickName" name="nickName" class="easyui-textbox" value="${facadeBean.nickName}">
				<span id="nickName_error" class="error-message"></span>
			</div>
			
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>角色：</span> 
				<input id="role" name="role">
			</div>
			
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>机构：</span> 
				<input id="org" name="org">
			</div>
			
			<div class="data-row">
				<span class="title-span">手机：</span> 
				<input id="mobile" name="mobile" class="easyui-textbox" value="${facadeBean.mobile}" data-options="validType:'phone'">
			</div>
			
			<div class="data-row">
				<span class="title-span">邮箱：</span> 
				<input id="email" name="email" class="easyui-textbox" value="${facadeBean.email}" data-options="validType:'email'">
			</div>
			
              <div class="data-row">
                  <span class="title-span">状态</span>
                   <c:choose>
						<c:when test="${facadeBean.lock == 'N'}">
							<span class="label label-success">正常</span>
						</c:when>
						<c:otherwise>
							<span class="label label-danger">锁定</span>
						</c:otherwise>
					</c:choose>
              </div>
              
              <div class="data-row">
                  <span class="title-span">创建时间：</span>
       			  <span><fmt:formatDate value='${facadeBean.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss" /></span>
       		  </div>
       		  
       		  <a onclick="redo(1)" href="javascript:void(0);" class="easyui-linkbutton c1" style="width:120px">修改密码</a>
			 
			 <div style="text-align:center;margin-top:5px;" class="data-row">
				<a href="javascript:void(0);"  onclick="saveAccount()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<span id="exception_error" class="error-message"></span>
				<span id="inforesult" style="color:green"></span>
			</div>
			
		</div> 
		
		<div id="passwordDiv" style="display:none;">
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>原密码：</span> 
				<input id="oldpwd" name="oldpwd" class="easyui-passwordbox" style="width: 200px;">
				<span id="oldpwd_error" class="error-message"></span>
			</div>
		
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>新密码：</span> 
				<input id="newpwd" name="newpwd" class="easyui-passwordbox" style="width: 200px;">
				<span id="newpwd_error" class="error-message"></span>
			</div>
			
			<div class="data-row">
				<span class="title-span"><span class="req-span">*</span>确认密码：</span> 
				<input id="repwd" name="repwd" class="easyui-passwordbox" style="width: 200px;">
				<span id="repwd_error" class="error-message"></span>
			</div>
		
			<p id="pwdexception_error" style="color:red;font-size:14px;"></p>
			<div style="text-align:center;margin-top:5px;" class="data-row">
				 <a onclick="savePwd()" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				 <a onclick="redo(2)" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">返回</a>
			</div>
			
			<p id="pwdresult" style="color:green;font-size:14px;"></p>
		</div>
		
	</div>
	
	<script type="text/javascript">
		$(function(){
			$('#role').combotree({
				url: '${ctx}/role/tree',
				multiple: false,
				animate: true,
				disabled: true 
			});
			
			
			$('#org').combotree({
				url: '${ctx}/org/tree',
				disabled: true 
			});
			
			var roleVal = "${roleVal}";  
		 	if(!isNull(roleVal)){
		 		var roleJson = eval('(' + roleVal + ')');
		 		$('#role').combotree('setValues', roleJson);
		 	}
		 	
		 	var orgId = "${orgId}"; 
		 	if(!isNull(orgId)){
				$('#org').combotree('setValue', {id: orgId, text: '${orgName}'});
			}
		});
	
		function saveAccount(){
			$("#inforesult").html("");
			var userName = $("#userName").textbox("getValue");
			
			if(isNull(userName)){
				errAccount("userName_error", "用户名必填");
				return false;
			}else{
				errAccount("userName_error", "");
			}
			
			var accountId = $("#id").val();
			
			var nickName = $("#nickName").val();
			if(isNull(nickName)){
				errAccount("nickName_error", "姓名必填");
				return false;
			}else{
				errAccount("nickName_error", "");
			}
			
			var roleId = "";
			var roleVals = $("#role").combotree('getValues');
			if(roleVals.length > 0){
				for(var i = 0; i < roleVals.length; i++){
					var id = roleVals[i];
					id = id.split("_")[1];
					
					if(i != roleVals.length - 1){
						roleId += id + ",";
					}else{
						roleId += id;
					}
				}
			}
			if(isNull(roleId)){
				errAccount("role_error", "请选择角色");
				return false;
			}else{
				errAccount("role_error", "");
			}
			
			var orgId = "";
			var orgTree = $('#org').combotree('tree');	
			var selecteNode = orgTree.tree('getSelected');
			if(!isNull(selecteNode)){
				orgId = selecteNode.id;
			}
			if(isNull(orgId)){
				errAccount("org_error", "请选择机构");
				return false;
			}else{
				errAccount("org_error", "");
			}
			
			var mobile = $("#mobile").textbox("getValue");
			var email = $("#email").textbox("getValue");
			
			$.ajax({
				url: "${ctx}/account/save",
				data: {
					userName: userName,
					nickName: nickName,
					roleId: roleId,
					orgId: orgId,
					mobile: mobile,
					email: email,
					id: accountId
				},
				success:function(data){
					if(data.success){
						$("#inforesult").html(data.msg);
					}else{
						if(data.data == "userName"){
							errAccount("userName_error", data.msg);
						}else{
							errAccount("exception_error", data.msg);
						}
					}
				}
			});
		}
		
		function errAccount(id, message){
            $("#" + id).html(message);
        }
		
		function redo(type){
			if(type == 1){
				$("#infoDiv").hide();
				$("#passwordDiv").show();
				
				$("#pwdresult").html("");
				$("#pwdexception_error").html("");
			}else{
				$("#infoDiv").show();
				$("#passwordDiv").hide();
				$("#exception_error").html("");
				$("#inforesult").html("");
			}
		}
		
		function savePwd(){
			var oldpwd = $("#oldpwd").val();
			var newpwd = $("#newpwd").val();
			var repwd = $("#repwd").val();
			
			if(isNull(oldpwd)){
				$("#oldpwd_error").html("原密码必填");
				return false;
			}else{
				$("#oldpwd_error").html("");
			}
			
			if(isNull(newpwd)){
				$("#newpwd_error").html("新密码必填");
				return false;
			}else{
				$("#newpwd_error").html("");
			}
			
			if(isNull(repwd)){
				$("#repwd_error").html("确认密码必填");
				return false;
			}else{
				$("#repwd_error").html("");
			}
			
			if(newpwd != repwd){
				$("#repwd_error").html("两次密码不一致");
				return false;
			}else{
				$("#repwd_error").html("");
			}
			
			$.ajax({
				url: "${ctx}/account/updatePwd",
				data:{
					oldPwd: oldpwd,
					newPwd: newpwd
				},
				success: function(data){
					if(data.success){
						$("#pwdresult").html(data.msg);
						$("#oldpwd").val('');
						$("#newpwd").val('');
						$("#repwd").val('');
					}else{
						if(data.data == "oldpwd"){
							errAccount("oldpwd_error", data.msg);
						}else{
							errAccount("pwdexception_error", data.msg);
						}
					}
				}
			});
			
		}
	</script>
	
	<style type="text/css">
		.data-row{
			height: 35px;
		}
	
		.title-span{
			font-weight: bold;
			display: inline-block;
			width: 80px;
		}
		
        .error-message{
            margin: 4px 0 0 5px;
            padding: 0;
            color: red;
        }
        
        .req-span{
        	font-weight: bold;
        	color: red;
        }
	</style>
	
</body>
