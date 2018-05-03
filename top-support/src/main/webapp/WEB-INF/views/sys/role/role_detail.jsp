<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<div style="margin-top:15px;margin-left:20px;">
		<c:if test="${type == 1}">
			<div class="info-div">
				<span class="title-span"><span class="req-span">*</span>${title}编码：</span> 
				<input id="e_code" name="e_code" value="${facade.code}" <c:if test="${not empty facade.id}">disabled</c:if> class="easyui-textbox">
				<span id="code_error" class="error-message"></span>
			</div>
		</c:if>
		
		<div class="info-div">
			<span class="title-span"><span class="req-span">*</span>${title}名称： </span>
			<input id="e_name" name="e_name" value="${facade.name}" class="easyui-textbox">
			<span id="name_error" class="error-message"></span>
		</div>
		
		<div class="info-div">
			<span class="title-span">上级角色组： </span>
			${parentGroup.name } 
		</div>
		
		<div class="info-div">
			<span class="title-span">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </span>
			<input type="text" id="e_desc" name="e_desc" value="${facade.desc}" class="easyui-textbox">
		</div>
		
		<div style="text-align:center;margin-top:5px;" class="info-div">
			<a href="javascript:void(0)"  onclick="saveRole()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<span id="exception_error" class="error-message"></span>
		</div>
	</div>
	
	<script type="text/javascript">
		function saveRole(){
			var name = $("#e_name").val();
			var code = $("#e_code").val();
			var type = "${type}";
			
			if(type == 1 && isNull(code)){
				errRole("code_error", "编码必填");
				return;
			}else{
				errRole("code_error", "");	
			}
			
			if(isNull(name)){
				errRole("name_error", "名称必填");
				return;
			}else{
				errRole("name_error", "");
			}
			
			$.ajax({
				url: "${ctx}/role/save",
				data: {
					"name": name,
					"code": code,
					"desc": $("#e_desc").val(),
					"id": "${facade.id}",
					"parentid": "${parentGroup.id}",
					"type": type
				},
				success:function(data){
					if(data.success){
						closeRoleDialog(data.data, data.msg);
					}else{
						if(data.data == "name"){
							errRole("name_error", data.msg);
						}else if(data.data == "code"){
							errRole("code_error", data.msg);
						}else{
							errRole("exception_error", data.msg);
						}
					}
				}
			});
		}
		
        function errRole(id, message){
            $("#" + id).html(message);
        }
	</script>
		
	<style type="text/css">
		.info-div{
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
