<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<input type="hidden" id="id" name="id" value="${facadeBean.id}"/>
	
	<div style="margin-top:15px;margin-left:20px;">
		<div class="data-row">
			<span class="title-span"><span class="req-span">*</span>代码：</span> 
			<input id="code" name="code" value="${facadeBean.code}" <c:if test="${not empty facadeBean.id}">disabled</c:if> class="easyui-textbox">
			<span id="code_error" class="error-message"></span>
		</div>
		
		<div class="data-row">
			<span class="title-span"><span class="req-span">*</span>车型：</span> 
			<input id="type" name="type" class="easyui-textbox" value="${facadeBean.type}">
			<span id="type_error" class="error-message"></span>
		</div>
		
		<div class="data-row">
			<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
			<input id="proTime" name="proTime" type="text" class="easyui-datebox" value="${facadeBean.proTime}">
			<span id="proTime_error" class="error-message"></span>
		</div>
		
		<div class="data-row">
			<span class="title-span"><span class="req-span">*</span>生产地址：</span> 
			<input id="proAddr" name="proAddr" class="easyui-textbox" multiline="true" value="${facadeBean.proAddr}">
			<span id="proAddr_error" class="error-message"></span>
		</div>
		
		<div class="data-row" style="margin-top:15px;">
			<span class="title-span">备注：</span> 
			<input id="remark" name="remark" class="easyui-textbox" multiline="true" value="${facadeBean.remark }" style="height:50px">		
		</div>
		 
		 <div style="text-align:center;margin-top:35px;" class="data-row">
			<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<span id="exception_error" class="error-message"></span>
		</div>
	</div>
	
	<script type="text/javascript">
		function save(){
			var code = $("#code").val();
			var type = $("#type").val();
			var proTime = $("#proTime").val();
			var proAddr = $("#proAddr").val();
			var remark = $("#remark").val();
			
			if(isNull(code)){
				err("code_error", "编码必填");
				return false;
			}else{
				err("code_error", "");
			}
			
			if(isNull(type)){
				err("type_error", "车型必填");
				return false;
			}else{
				err("type_error", "");
			}
			
			if(isNull(proTime)){
				err("proTime_error", "生产日期必填");
				return false;
			}else{
				err("proTime_error", "");
			}

			if(isNull(proAddr)){
				err("proAddr_error", "生产地址必填");
				return false;
			}else{
				err("proAddr_error", "");
			}
			
			$.ajax({
				url: "${ctx}/vehicle/save",
				data: {
					code: code,
					type: type,
					proTime: proTime,
					proAddr: proAddr,
					remark: remark,
					id: '${facadeBean.id}'
				},
				success:function(data){
					if(data.success){
						closeDialog(data.msg);
					}else{
						if(data.data == "code"){
							err("code_error", data.msg);
						}else{
							err("exception_error", data.msg);
						}
					}
				}
			});
		}
		
		function err(id, message){
            $("#" + id).html(message);
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
