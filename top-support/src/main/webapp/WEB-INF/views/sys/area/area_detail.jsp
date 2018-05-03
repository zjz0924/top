<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<div style="margin-top:15px;margin-left:20px;">
	<div class="info-div">
		<span class="title-span"><span class="req-span">*</span>区域编码：</span> 
		<input id="e_code" name="e_code" value="${area.code}" <c:if test="${not empty id}">disabled</c:if> class="easyui-textbox">
		<span id="code_error" class="error-message"></span>
	</div>
	
	<div class="info-div">
		<span class="title-span"><span class="req-span">*</span>区域名称： </span>
		<input id="e_name" name="e_name" value="${area.name}" class="easyui-textbox">
		<span id="name_error" class="error-message"></span>
	</div>
	
	<div class="info-div">
		<span class="title-span">上级区域： </span>
		${parentArea.name } 
	</div>
	
	<div class="info-div">
		<span class="title-span">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </span>
		<input type="text" id="e_desc" name="e_desc" value="${area.desc}" class="easyui-textbox">
	</div>
	
	<div style="text-align:center;margin-top:5px;" class="info-div">
		<a href="javascript:void(0)"  onclick="saveArea()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		<span id="exception_error" class="error-message"></span>
	</div>
</div>
	
<script type="text/javascript">
	function saveArea(){
		var name = $("#e_name").val();
		var code = $("#e_code").val();
		var desc = $("#e_desc").val();
		
		if(isNull(code)){
			errArea("code_error", "区域编码必填");
			return;
		}else{
			errArea("code_error", "");	
		}
		
		if(isNull(name)){
			errArea("name_error", "区域名称必填");
			return;
		}else{
			errArea("name_error", "");
		}
		
		$.ajax({
			url: "${ctx}/area/save",
			data: {
				"text": name,
				"code": code,
				"desc": desc,
				"id": "${id}",
				"parentid": "${parentArea.id}"
			},
			success:function(data){
				if(data.success){
					closeAreaDialog(data.data, data.msg);
				}else{
					if(data.data == "name"){
						errArea("name_error", data.msg);
					}else if(data.data == "code"){
						errArea("code_error", data.msg);
					}else{
						errArea("exception_error", data.msg);
					}
				}
			}
		});
	}
	
    function errArea(id, message){
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
