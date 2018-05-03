<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>
	

	<div style="margin-top:15px;margin-left:20px;">
		<div class="info-div">
			<span class="title-span"><span class="req-span">*</span>机构编码：</span> 
			<input id="o_code" name="o_code" value="${org.code}" <c:if test="${not empty id}">disabled</c:if> class="easyui-textbox">
			<span id="ocode_error" class="error-message"></span>
		</div>
		
		<div class="info-div">
			<span class="title-span"><span class="req-span">*</span>机构名称： </span>
			<input id="o_name" name="o_name" value="${org.name}" class="easyui-textbox">
			<span id="oname_error" class="error-message"></span>
		</div>
		
		<div class="info-div">
			<span class="title-span"><span class="req-span">*</span>机构类型： </span>
			<select id="type" class="easyui-combobox" name="type" data-options="panelHeight:'auto'" style="width:171px;" <c:if test="${canEdit == false}">disabled</c:if>>
				<option value="">请选择</option>
			    <option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>通用五菱</option>
			    <option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>供应商</option>
			    <option value="3" <c:if test="${type == 3}">selected="selected"</c:if>>实验室</option>
			    <option value="4" <c:if test="${type == 4}">selected="selected"</c:if>>其它</option>
			</select>
			<span id="otype_error" class="error-message"></span>
		</div>
		
		<div class="info-div">
			<span class="title-span">上级机构： </span>
			${parentOrg.name } 
		</div>
		
		<div class="info-div">
			<span class="title-span"><span class="req-span">*</span>区&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;域： </span>
			<input id="oarea_id" >
			<span id="oarea_error" class="error-message"></span>
		</div>
		
		<div class="info-div">
			<span class="title-span">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址： </span>
			<input id="addr" name="addr" value="${org.addr }" class="easyui-textbox">
		</div>
		
		<div class="info-div">
			<span class="title-span">备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注： </span>
			<input type="text" id="o_desc" name="o_desc" value="${org.desc}" class="easyui-textbox">
		</div>
		
		<div style="text-align:center;margin-top:5px;" class="info-div">
			<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<span id="exception_error" class="error-message"></span>
		</div>
	</div>
	
	<script type="text/javascript">
		$(function(){
			$('#oarea_id').combotree({
			    url: '${ctx}/area/tree'
			});
			
			var areaid = "${areaid}";
			if(!isNull(areaid)){
				$('#oarea_id').combotree('setValue', {
					id: "${areaid}",
					text: "${areaname}"
				});
			}
		});
	
		function save(){
			var name = $("#o_name").val();
			var code = $("#o_code").val();
			var addr = $("#addr").val();
			var areaid = "";
			
			var tree = $('#oarea_id').combotree('tree');	
			var selecteNode = tree.tree('getSelected');
			if(!isNull(selecteNode)){
				areaid = selecteNode.id;
			}
			
			if(isNull(code)){
				err("ocode_error", "机构编码必填");
				return;
			}else{
				err("ocode_error", "");
			}
			
			if(isNull(name)){
				err("oname_error", "区域名称必填");	
				return;
			}else{
				err("oname_error", "");
			}
			
			var type = $("#type").val();
			if(isNull(type)){
				err("otype_error", "区域类型必选");	
				return;
			}else{
				err("otype_error", "");
			}
			
			if(isNull(areaid)){
				err("oarea_error", "区域必选");	
				return;
			}else{
				err("oarea_error", "");
			}
			
			$.ajax({
				url: "${ctx}/org/save",
				data: {
					"text": name,
					"code": code,
					"desc": $("#o_desc").val(),
					"id": "${id}",
					"parentid": "${parentOrg.id}",
					"areaid": areaid,
					"type": type,
					"addr": addr
				},
				success:function(data){
					if(data.success){
						closeOrgDialog(data.data, data.msg);
					}else{
						if(data.data == "name"){
							err("oname_error", data.msg);
						}else if(data.data == "code"){
							err("ocode_error", data.msg);
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
