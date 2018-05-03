<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<form method="POST" enctype="multipart/form-data" id="dataForm">
	
	<input type="hidden" id="t_id" name="t_id" value="${facadeBean.id }">
	<input type="hidden" id="taskType" name="taskType" value="${taskType }">
	<input type="hidden" id="v_id" name="v_id" value="${facadeBean.info.vehicle.id}">
	<input type="hidden" id="p_id" name="p_id" value="${facadeBean.info.parts.id }">
	<input type="hidden" id="m_id" name="m_id" value="${facadeBean.info.material.id }">
	<input type="hidden" id="result" name="result">
	<input type="hidden" id="examine_remark" name="examine_remark">
	
	<div style="margin-left: 10px;margin-top:20px;">
		<div class="title">整车信息</div>
		<table class="info">
			<tr>
				<td>
					<span class="title-span"><span class="req-span">*</span>代码：</span> 
					<input id="v_code" name="v_code" class="easyui-textbox" value="${facadeBean.info.vehicle.code }"  style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>车型：</span> 
					<input id="v_type" name="v_type" class="easyui-textbox" value="${facadeBean.info.vehicle.type }"  style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
					<input id="v_proTime" name="v_proTime" type="text" class="easyui-datebox" data-options="editable:false " value="<fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>生产地址：</span> 
					<input id="v_proAddr" name="v_proAddr" class="easyui-textbox" value="${facadeBean.info.vehicle.proAddr }" style="width:150px;">
				</td>
			</tr>
			<tr>
				<td>
					<span class="title-span">&nbsp;备注：</span> 
					<input id="v_remark" name="v_remark" class="easyui-textbox" value="${facadeBean.info.vehicle.remark }" style="width:150px;">	
				</td>
			</tr>
		</table>
	</div>
		
	<c:if test="${facadeBean.type == 1 }">
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">零部件信息&nbsp;&nbsp;
				<a href="javascript:void(0)" onclick="partsInfo()"><i class="icon icon-search"></i></a>&nbsp;&nbsp;&nbsp;
				<a href="javascript:void(0)" onclick="addParts()" title="清空"><i class="icon icon-edit"></i></a>
			</div>
			
			<table class="info">
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>零件号：</span> 
						<input id="p_code" name="p_code" class="easyui-textbox" value="${facadeBean.info.parts.code }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>名称：</span> 
						<input id="p_name" name="p_name" class="easyui-textbox" value="${facadeBean.info.parts.name }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产商：</span> 
						<input id="p_orgId" name="p_orgId" >
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
						<input id="p_proNo" name="p_proNo" class="easyui-textbox" value="${facadeBean.info.parts.proNo }" style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
						<input id="p_proTime" name="p_proTime" type="text" class="easyui-datebox" data-options="editable:false" value="<fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产场地：</span> 
						<input id="p_place" name="p_place" class="easyui-textbox" value="${facadeBean.info.parts.place }" style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>关键零件：</span> 
						<select id="p_isKey" name="p_isKey" style="width:160px;" class="easyui-combobox" data-options="panelHeight: 'auto', onChange: function(n, o){isKeyChange()}">
							<option value="0" <c:if test="${facadeBean.info.parts.isKey == 0 }">selected="selected"</c:if>>否</option>
							<option value="1" <c:if test="${facadeBean.info.parts.isKey == 1 }">selected="selected"</c:if>>是</option>
						</select>
					</td>
					<td>
						<span class="title-span"><span class="req-span" id="keyCode_req" style="display:none;">*</span>零件型号：</span> 
						<input id="p_keyCode" name="p_keyCode" class="easyui-textbox" value="${facadeBean.info.parts.keyCode }" style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>联系人：</span> 
						<input id="p_contacts" name="p_contacts" class="easyui-textbox" value="${facadeBean.info.parts.contacts }" style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>联系电话：</span> 
						<input id="p_phone" name="p_phone" class="easyui-textbox" value="${facadeBean.info.parts.phone }" style="width:150px;">
					</td>
					<td>
						<span class="title-span">&nbsp;备注：</span> 
						<input id="p_remark" name="p_remark" class="easyui-textbox" value="${facadeBean.info.parts.remark }" style="width:150px;">	
					</td>
				</tr>
			</table>
		</div>
	</c:if>
	
	<div style="margin-left: 10px;margin-top:20px;">	
		<div class="title">原材料信息</div>
		<table class="info">
			<tr>
				<td>
					<span class="title-span"><span class="req-span">*</span>材料名称：</span> 
					<input id="m_matName" name="m_matName" class="easyui-textbox" value="${facadeBean.info.material.matName }" style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
					<input id="m_proNo" name="m_proNo" class="easyui-textbox" value="${facadeBean.info.material.proNo }" style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>生产商：</span> 
					<input id="m_orgId" name="m_orgId" style="width:150px;">
				</td>
				<td>
					<span class="title-span"><span class="req-span">*</span>材料牌号：</span> 
					<input id="m_matNo" name="m_matNo" class="easyui-textbox" value="${facadeBean.info.material.matNo }" style="width:150px;">
				</td>
			</tr>
			<tr>
				<td>
					<span class="title-span"><span class="req-span">*</span>材料颜色：</span> 
					<input id="m_matColor" name="m_matColor" class="easyui-textbox" value="${facadeBean.info.material.matColor }" style="width:150px;">
				</td>
				
				<td>
					<span class="title-span"><span class="req-span">*</span>联系人：</span> 
					<input id="m_contacts" name="m_contacts" class="easyui-textbox" value="${facadeBean.info.material.contacts }" style="width:150px;">	
				</td>
				
				<td>
					<span class="title-span"><span class="req-span">*</span>联系电话：</span> 
					<input id="m_phone" name="m_phone" class="easyui-textbox" value="${facadeBean.info.material.phone }" style="width:150px;">	
				</td>
				
				<td>
					<span class="title-span">备注：</span> 
					<input id="m_remark" name="m_remark" class="easyui-textbox" value="${facadeBean.info.material.remark }" style="width:150px;">	
				</td>
			</tr>
			
			<tr>
				<td>
					<span class="title-span">成分表：</span> 
					<input id="m_pic" name="m_pic" class="easyui-filebox" style="width:150px" data-options="buttonText: '选择'">
				</td>
				
				<c:if test="${ not empty facadeBean.info.material.pic}">
					<td>
						<span class="title-span">成分表：</span>
						<span>
							<a target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">${fn:substringAfter(facadeBean.info.material.pic, "/")}</a>
						</span> 
					</td>
				</c:if>
			</tr>
		</table>
	</div>
	</form>
	
	<div style="text-align:center;margin-top:25px;margin-bottom: 15px;" class="data-row">
		<a href="javascript:void(0);"  onclick="examine(${facadeBean.id}, 1, '')" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">通过</a>&nbsp;&nbsp;
		<a href="javascript:void(0);"  onclick="notPass()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">不通过</a>
	</div>

	<div id="dlg" class="easyui-dialog" title="审核不通过" style="width: 400px; height: 200px; padding: 10px" closed="true" data-options="modal:true">
		<input id="remark" class="easyui-textbox" label="不通过原因：" labelPosition="top" multiline="true" style="width: 350px;height: 100px;"/>
		
		<div align=center style="margin-top: 15px;">
			<a href="javascript:void(0);"  onclick="doSubmit()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
			<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</div>
			
	<style type="text/css">
		.title-span{
			display: inline-block;
			width: 85px;
			padding-right: 5px;
			text-align: right; 
		}
		
		.req-span{
        	font-weight: bold;
        	color: red;
        }
			
		.title {
			margin-left: 10px;
			margin-bottom: 8px;
			font-size: 14px;
			color: #1874CD;
    		font-weight: bold;
		}
		
		.info{
			border: 1px dashed #C9C9C9;
        	margin-left:10px;
        	margin-right: 10px;
        	font-size: 14px;
        	padding-top: 5px;
        	padding-bottom: 5px;
		}
		
		.info tr{
			height: 35px;
		}
		
		.title-td {
			width:13%;
			padding-left: 5px;
			font-weight: bold;
		}
		
		.value-td{
			width:32%;
			padding-left: 5px;
		}
		
		.single-row{
			background: #F0F0F0;
		}
		
		.couple-row{
			background: #f5f5f5;
		}
	</style>
	
	<script type="text/javascript">
		// 是否提交中
		var saving = false;
		
		$(function(){
			var taskType = "${taskType}";
			
			if(taskType == 1){
				$('#p_orgId').combotree({
					url: '${ctx}/org/getTreeByType?type=2',
					multiple: false,
					animate: true,
					width: '163px'
				});
				
				// 只有最底层才能选择
				var pOrgTree = $('#p_orgId').combotree('tree');	
				pOrgTree.tree({
				   onBeforeSelect: function(node){
					   if(isNull(node.children)){
							return true;
					   }else{
						   return false;
					   }
				   }
				});
				
				// 设置机构的值
				$('#p_orgId').combotree('setValue', "${facadeBean.info.parts.orgId}");
			}
			
			
			$('#m_orgId').combotree({
				url: '${ctx}/org/getTreeByType?type=2',
				multiple: false,
				animate: true,
				width: '163px'
			});
			
			// 只有最底层才能选择
			var mOrgTree = $('#m_orgId').combotree('tree');	
			mOrgTree.tree({
			   onBeforeSelect: function(node){
				   if(isNull(node.children)){
						return true;
				   }else{
					   return false;
				   }
			   }
			});
			
			// 设置机构的值
			$('#m_orgId').combotree('setValue', "${facadeBean.info.material.orgId}");
		});
	
		function examine(id, result, remark){
			if(saving){
				return false;
			}
			saving = true;
			
			$("#result").val(result);
			$("#examine_remark").val(remark);
			
			// 整车信息
			if(!isRequire("v_code", "整车代码必填")){ saving = false; return false; }
			if(!isRequire("v_type", "车型必填")){ saving = false; return false; }
			if(!isRequire("v_proTime", "整车生产日期必填")){ saving = false; return false; }
			if(!isRequire("v_proAddr", "整车生产地址必填")){ saving = false; return false; }
			
			// 零部件信息
			var taskType = "${taskType}";
			if(taskType == 1){
				if(!isRequire("p_code", "零件号必填")){ saving = false; return false; }
				if(!isRequire("p_name", "零部件名称必填")){ saving = false; return false; }
				if(!isRequire("p_orgId", "零部件生产商必填")){ saving = false; return false; }
				if(!isRequire("p_proTime", "零部件生产日期必填")){ saving = false; return false; }
				if(!isRequire("p_place", "零部件生产场地必填")){ saving = false; return false; }
				if(!isRequire("p_proNo", "零部件生产批号必填")){ saving = false; return false; }
				var isKey = $("#p_isKey").val();
				if(isKey == 1){
					if(!isRequire("p_keyCode", "零件型号必填")){ saving = false; return false; }
				}
				if(!isRequire("p_contacts", "零部件联系人必填")){ saving = false; return false; }
				if(!isRequire("p_phone", "零部件联系电话必填")){ saving = false; return false; }
				
				var p_phone = $("#p_phone").val();
				if (!isNull(p_phone)) {
					if (!(/^[1][3,4,5,7,8][0-9]{9}$/.test(p_phone))) {
						errorMsg("零部件联系电话格式不正确");
						$("#p_phone").next('span').find('input').focus();
						saving = false; 
						return false;
					}
				}
			}
			
			// 原材料信息
			if(!isRequire("m_matName", "原材料名称必填")){ saving = false; return false; }
			if(!isRequire("m_proNo", "原材料生产批号必填")){ saving = false; return false; }
			if(!isRequire("m_orgId", "材料生产商必填")){ saving = false; return false; }
			if(!isRequire("m_matNo", "原材料材料牌号必填")){ saving = false; return false; }
			if(!isRequire("m_matColor", "原材料材料颜色必填")){ saving = false; return false; }
			
			if(!isRequire("m_contacts", "原材料联系人必填")){ saving = false; return false; }
			if(!isRequire("m_phone", "原材料联系电话必填")){ saving = false; return false; }
			
			var m_phone = $("#m_phone").val();
			if (!isNull(m_phone)) {
				if (!(/^[1][3,4,5,7,8][0-9]{9}$/.test(m_phone))) {
					errorMsg("原材料联系电话格式不正确");
					$("#m_phone").next('span').find('input').focus();
					saving = false; 
					return false;
				}
			}
			
			$('#dataForm').ajaxSubmit({
				url: "${ctx}/ots/examine",
				dataType : 'text',
				success:function(msg){
					var data = eval('(' + msg + ')');
					if(data.success){
						tipMsg(data.msg, function(){
							window.location.reload();
						});
					}else{
						saving = false; 
						errorMsg(data.msg);
					}
				}
			});
		}
		
		function notPass(){
			$("#remark").textbox("setValue", "");
			$("#dlg").dialog("open");
		}
		
		function doSubmit(){
			var remark = $("#remark").textbox("getValue");
			if(isNull(remark)){
				errorMsg("请输入原因");
				$("#remark").next('span').find('input').focus();
				return false;
			}
			
			examine("${facadeBean.id}", 2, remark);
			$("#dlg").dialog("close");
		}
		
		function doCancel(){
			$("#dlg").dialog("close");
		}
		
		/**
		  * 判断是否必填
		  * id: 属性ID
		  * emsg: 错误信息
		*/
		function isRequire(id, emsg){
			var val = $("#" + id).val();
			if(isNull(val)){
				errorMsg(emsg);
				$("#" + id).next('span').find('input').focus();
				return false;
			}
			return true;
		}
	</script>	
	
</body>
