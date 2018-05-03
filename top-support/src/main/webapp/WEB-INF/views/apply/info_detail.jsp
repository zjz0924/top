<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<form method="POST" enctype="multipart/form-data" id="uploadForm">
		<input type="hidden" id="t_id" name="t_id" value="${facadeBean.id }">
	
		<div style="margin-left: 10px;margin-top:20px;">
			
			<div class="title">整车信息</div>
			<table class="info">
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>代码：</span>
						<span class="val" title="${facadeBean.info.vehicle.code }">${facadeBean.info.vehicle.code }</span>
					</td>
					<td class="input-td">
						<input id="v_code" name="v_code" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>车型：</span>
						<span class="val" title="${facadeBean.info.vehicle.type }">${facadeBean.info.vehicle.type }</span>
					</td>
					<td class="input-td">
						<input id="v_type" name="v_type" class="easyui-textbox" />
					</td>
				</tr>

				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产日期：</span>
						<span class="val" title="<fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
					</td>
					<td class="input-td">
						<input id="v_proTime" name="v_proTime" type="text" class="easyui-datebox" data-options="editable:false" />
					</td>
				</tr>

				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产地址：</span>
						<span class="val" title="${facadeBean.info.vehicle.proAddr }">${facadeBean.info.vehicle.proAddr }</span>
					</td>
					<td class="input-td">
						<input id="v_proAddr" name="v_proAddr" class="easyui-textbox" />
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span">&nbsp;备注：</span> 
						<span class="val" title="${facadeBean.info.vehicle.remark }">${facadeBean.info.vehicle.remark }</span>
					</td>
					<td class="input-td">
						<input id="v_remark" name="v_remark" class="easyui-textbox" />
					</td>
				</tr>
			</table>
		</div>
	
		<c:if test="${facadeBean.type != 4}">
			<div style="margin-left: 10px;margin-top:20px;">
				<div class="title">零部件信息</div>
				
				<table class="info">
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>零件号：</span> 
							<span class="val" title="${facadeBean.info.parts.code}">${facadeBean.info.parts.code}</span>
						</td>
						<td class="input-td">
							<input id="p_code" name="p_code" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>名称：</span>
							<span class="val" title="${facadeBean.info.parts.name}">${facadeBean.info.parts.name}</span>
						</td>
						<td class="input-td">
							<input id="p_name" name="p_name" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产商：</span> 
							<span class="val" title="${facadeBean.info.parts.org.name}">${facadeBean.info.parts.org.name}</span>
						</td>
						<td class="input-td">
							<input id="p_orgId" name="p_orgId" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
							<span class="val" title="${facadeBean.info.parts.proNo }">${facadeBean.info.parts.proNo }</span>
						</td>
						<td class="input-td">
							<input id="p_proNo" name="p_proNo" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
							<span class="val" title="<fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/>"><fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd"/></span>
						</td>
						<td class="input-td">
							<input id="p_proTime" name="p_proTime" type="text" class="easyui-datebox" data-options="editable:false" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>生产场地：</span> 
							<span class="val" title="${facadeBean.info.parts.place }">${facadeBean.info.parts.place }</span>
						</td>
						<td class="input-td">
							<input id="p_place" name="p_place" class="easyui-textbox" />
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span"><span class="req-span">*</span>关键零件：</span> 
							<span class="val" title="<c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if>"><c:if test="${facadeBean.info.parts.isKey == 0 }">否</c:if><c:if test="${facadeBean.info.parts.isKey == 1 }">是</c:if></span>
						</td>
						<td class="input-td">
							<select id="p_isKey" name="p_isKey" style="width:163px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
								<option value="0" <c:if test="${facadeBean.info.parts.isKey == 0 }">selected="selected"</c:if>>否</option>
								<option value="1" <c:if test="${facadeBean.info.parts.isKey == 1 }">selected="selected"</c:if>>是</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<span class="title-span">零件型号：</span> 
							<span class="val" title="${facadeBean.info.parts.keyCode }">${facadeBean.info.parts.keyCode }</span>
						</td>
						<td class="input-td">
							<input id="p_keyCode" name="p_keyCode" class="easyui-textbox">
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系人：</span> 
							<span class="val" title="${facadeBean.info.parts.contacts }">${facadeBean.info.parts.contacts }</span>
						</td>
						<td class="input-td">
							<input id="p_contacts" name="p_contacts" class="easyui-textbox">
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">联系电话：</span> 
							<span class="val" title="${facadeBean.info.parts.phone }">${facadeBean.info.parts.phone }</span>
						</td>
						<td class="input-td">
							<input id="p_phone" name="p_phone" class="easyui-textbox">
						</td>
					</tr>
					
					<tr>
						<td>
							<span class="title-span">&nbsp;备注：</span> 
							<span class="val" title="${facadeBean.info.parts.remark }">${facadeBean.info.parts.remark }</span>
						</td>
						<td class="input-td">
							<input id="p_remark" name="p_remark" class="easyui-textbox">	
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
						<span class="val" title="${facadeBean.info.material.matName }">${facadeBean.info.material.matName }</span>
					</td>
					<td class="input-td">
						<input id="m_matName" name="m_matName" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
						<span class="val" title="${facadeBean.info.material.proNo }">${facadeBean.info.material.proNo }</span>
					</td>
					<td class="input-td">
						<input id="m_proNo" name="m_proNo" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产商：</span> 
						<span class="val" title="${facadeBean.info.material.org.name }">${facadeBean.info.material.org.name }</span>
					</td>
					<td class="input-td">
						<input id="m_orgId" name="m_orgId">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>材料牌号：</span> 
						<span class="val" title="${facadeBean.info.material.matNo }">${facadeBean.info.material.matNo }</span>
					</td>
					<td class="input-td">
						<input id="m_matNo" name="m_matNo" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>材料颜色：</span> 
						<span class="val" title="${facadeBean.info.material.matColor }">${facadeBean.info.material.matColor }</span>
					</td>
					<td class="input-td">
						<input id="m_matColor" name="m_matColor" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>成分表：</span> 
						<c:if test="${not empty facadeBean.info.material.pic }">
							<a target= _blank  href="${resUrl}/${facadeBean.info.material.pic }">${fn:substringAfter(facadeBean.info.material.pic, "/")}</a>
						</c:if>
					</td>
					
					<td class="input-td">
						<input id="m_pic" name="m_pic" class="easyui-filebox" style="width:171px" data-options="buttonText: '选择'">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span">联系人：</span> 
						<span class="val" title="${facadeBean.info.material.contacts }">${facadeBean.info.material.contacts }</span>
					</td>
					<td class="input-td">
						<input id="m_contacts" name="m_contacts" class="easyui-textbox">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span">联系电话：</span> 
						<span class="val" title="${facadeBean.info.material.phone }">${facadeBean.info.material.phone }</span>
					</td>
					<td class="input-td">
						<input id="m_phone" name="m_phone" class="easyui-textbox">
					</td>
				</tr>
				<tr>	
					<td>
						<span class="title-span">备注：</span> 
						<span class="val" title="${facadeBean.info.material.remark }">${facadeBean.info.material.remark }</span>	
					</td>
					<td class="input-td">
						<input id="m_remark" name="m_remark" class="easyui-textbox">
					</td>
				</tr>
			</table>
		</div>
	
		 <div style="text-align:center;margin-top:35px;" class="data-row">
			<div id="exception_error" class="error-message"></div>
			<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
			<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
		</div>
	</form>
	
	<div id="vehicleDialog"></div>
	<div id="materialDialog"></div>
	<div id="partsDialog"></div>
	
		<style type="text/css">
			.title-span{
				display: inline-block;
				width: 85px;
				padding-right: 15px;
				text-align: right; 
			}
			
	        .req-span{
	        	font-weight: bold;
	        	color: red;
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
	        
	        .title{
	        	margin-left:10px;
	        	margin-bottom:8px;
	        	font-size: 14px;
	        	color: #1874CD;
    			font-weight: bold;
	        }
	        
	        .icon{
	        	width:16px;
	        	height: 16px;
	        	display: inline-block;
	        }
	        
	        .val{
	        	border: 1px solid #D3D3D3;
			    display: inline-block;
			    width: 180px;
			    border-radius: 5px 5px 5px 5px;
			    margin: 0px;
			    padding-top: 0px;
			    padding-bottom: 0px;
			    line-height: 22px;
			    padding-left: 5px;
			    opacity: 0.6;
			    background-color: rgb(235, 235, 228);
			}
			
			input{
				width: 180px;
			}
			
			.input-td{
				padding-left: 50px;
				padding-right: 20px;
			}
			
			.error-message{
				color:red;
				text-align:center;
				margin-bottom: 15px;
				font-weight:bold;
			}
		</style>
		
		<script type="text/javascript">
			// 是否提交中
			var saving = false;
		
			$(function(){
				var taskType = "${facadeBean.type}";
				if(taskType != 4){
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
			});
		
			function save(){
				if(saving){
					return false;
				}
				saving = true;
				$("#exception_error").html("");
				
				var p_phone = $("#p_phone").val();
				if (!isNull(p_phone)) {
					if (!(/^[1][3,4,5,7,8][0-9]{9}$/.test(p_phone))) {
						$("#exception_error").html("零部件联系电话格式不正确");
						$("#p_phone").next('span').find('input').focus();
						saving = false; 
						return false;
					}
				}
				$("#exception_error").html("");
				
				var m_phone = $("#m_phone").val();
				if (!isNull(m_phone)) {
					if (!(/^[1][3,4,5,7,8][0-9]{9}$/.test(m_phone))) {
						$("#exception_error").html("原材料联系电话格式不正确");
						$("#m_phone").next('span').find('input').focus();
						saving = false; 
						return false;
					}
				}
				$("#exception_error").html("");
				
				$('#uploadForm').ajaxSubmit({
					url: "${ctx}/apply/applyInfoSave",
					dataType : 'text',
					success:function(msg){
						saving = false;
						
						var data = eval('(' + msg + ')');
						if(data.success){
							closeDialog(data.msg, "infoDetailDialog");
						}else{
							$("#exception_error").html(data.msg);
						}
					}
				});
			}

			function doCancel(){
				$("#infoDetailDialog").dialog("close");
			}
			
		</script>
	
</body>
