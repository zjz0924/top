<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<form method="POST" enctype="multipart/form-data" id="uploadForm">
		<input type="hidden" id="t_id" name="t_id" value="${facadeBean.id }">
		<input type="hidden" id="v_id" name="v_id" value="${facadeBean.info.vehicle.id}">
		<input type="hidden" id="p_id" name="p_id" value="${facadeBean.info.parts.id }">
		<input type="hidden" id="m_id" name="m_id" value="${facadeBean.info.material.id }">
	
		<div style="margin-left: 10px;margin-top:20px;">
			
			<c:if test="${not empty facadeBean.id && facadeBean.state == 2}">
				<div style="margin-bottom: 20px;margin-left: 10px;">
					当前状态： <span style="color:red; font-weight:bold;">审批不通过</span>
				       	    <div style="border: 1px dashed #C9C9C9;width: 97%;margin-top: 10px;">
				       	    	<c:forEach items="${recordList}" var="vo" varStatus="vst">
				       	    		<div style="margin-top:5px; margin-bottom: 5px;margin-left: 5px;">
				       	    			第&nbsp;<span style="font-weight:bold;">${vst.index + 1}</span>&nbsp;次审核&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatDate value='${vo.createTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>&nbsp;&nbsp;&nbsp;&nbsp;审核意见：<span style="font-weight:bold;">${vo.remark}</span>
				       	    		</div>
				       	    	</c:forEach> 
				       	    </div>
				</div>		
			</c:if>
			
			<div style="margin-top: 15px; margin-bottom: 15px;">
				<div class="title">任务号</div>
				<div style="margin-left: 10px;">
					<input type="text" id="qCode" name="qCode" class="easyui-textbox"> 
					<a href="javascript:void(0)" onclick="doQuery()" title="检索"><i class="icon icon-search"></i></a>
				</div>
			</div>
			
		
			<div class="title">整车信息&nbsp;&nbsp;
				<a href="javascript:void(0)" onclick="vehicleInfo()" title="检索"><i class="icon icon-search"></i></a>
			</div>
			
			<table class="info">
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>代码：</span> 
						<input id="v_code" name="v_code" class="easyui-textbox" value="${facadeBean.info.vehicle.code }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>车型：</span> 
						<input id="v_type" name="v_type" class="easyui-textbox" value="${facadeBean.info.vehicle.type }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
						<input id="v_proTime" name="v_proTime" type="text" class="easyui-datebox" data-options="editable:false " value="<fmt:formatDate value='${facadeBean.info.vehicle.proTime }' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产地址：</span> 
						<input id="v_proAddr" name="v_proAddr" class="easyui-textbox" value="${facadeBean.info.vehicle.proAddr }" disabled style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span">&nbsp;备注：</span> 
						<input id="v_remark" name="v_remark" class="easyui-textbox" value="${facadeBean.info.vehicle.remark }" disabled style="width:150px;">	
					</td>
				</tr>
			</table>
		</div>
	
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">零部件信息&nbsp;&nbsp;
				<a href="javascript:void(0)" onclick="partsInfo()"><i class="icon icon-search"></i></a>
			</div>
			
			<table class="info">
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>零件号：</span> 
						<input id="p_code" name="p_code" class="easyui-textbox" value="${facadeBean.info.parts.code }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>名称：</span> 
						<input id="p_name" name="p_name" class="easyui-textbox" value="${facadeBean.info.parts.name }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产商：</span> 
						<input id="p_orgName" name="p_orgName" class="easyui-textbox" value="${facadeBean.info.parts.org.name}" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
						<input id="p_proNo" name="p_proNo" class="easyui-textbox" value="${facadeBean.info.parts.proNo }" disabled style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产日期：</span> 
						<input id="p_proTime" name="p_proTime" type="text" class="easyui-datebox" data-options="editable:false" value="<fmt:formatDate value='${facadeBean.info.parts.proTime }' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产场地：</span> 
						<input id="p_place" name="p_place" class="easyui-textbox" value="${facadeBean.info.parts.place }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>关键零件：</span> 
						<select id="p_isKey" name="p_isKey" style="width:150px;" class="easyui-combobox" data-options="panelHeight: 'auto'" disabled>
							<option value="0" <c:if test="${facadeBean.info.parts.isKey == 0 }">selected="selected"</c:if>>否</option>
							<option value="1" <c:if test="${facadeBean.info.parts.isKey == 1 }">selected="selected"</c:if>>是</option>
						</select>
					</td>
					<td>
						<span class="title-span">零件型号：</span> 
						<input id="p_keyCode" name="p_keyCode" class="easyui-textbox" value="${facadeBean.info.parts.keyCode }" disabled style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>联系人：</span> 
						<input id="p_contacts" name="p_contacts" class="easyui-textbox" value="${facadeBean.info.parts.contacts }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>联系电话：</span> 
						<input id="p_phone" name="p_phone" class="easyui-textbox" value="${facadeBean.info.parts.phone }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span">&nbsp;备注：</span> 
						<input id="p_remark" name="p_remark" class="easyui-textbox" value="${facadeBean.info.parts.remark }" disabled style="width:150px;">	
					</td>
				</tr>
			</table>
		</div>
	
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">原材料信息
				<a href="javascript:void(0)" onclick="materialInfo()"><i class="icon icon-search"></i></a>
			</div>
			
			<table class="info">
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>材料名称：</span> 
						<input id="m_matName" name="m_matName" class="easyui-textbox" value="${facadeBean.info.material.matName }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产批号：</span> 
						<input id="m_proNo" name="m_proNo" class="easyui-textbox" value="${facadeBean.info.material.proNo }" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>生产商：</span> 
						<input id="m_orgName" name="m_orgName" class="easyui-textbox" value="${facadeBean.info.material.org.name}" disabled style="width:150px;">
					</td>
					<td>
						<span class="title-span"><span class="req-span">*</span>材料牌号：</span> 
						<input id="m_matNo" name="m_matNo" class="easyui-textbox" value="${facadeBean.info.material.matNo }" disabled style="width:150px;">
					</td>
				</tr>
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>材料颜色：</span> 
						<input id="m_matColor" name="m_matColor" class="easyui-textbox" value="${facadeBean.info.material.matColor }" disabled style="width:150px;">
					</td>
					
					<td>
						<span class="title-span"><span class="req-span">*</span>联系人：</span> 
						<input id="m_contacts" name="m_contacts" class="easyui-textbox" value="${facadeBean.info.material.contacts }" disabled style="width:150px;">	
					</td>
					
					<td>
						<span class="title-span"><span class="req-span">*</span>联系电话：</span> 
						<input id="m_phone" name="m_phone" class="easyui-textbox" value="${facadeBean.info.material.phone }" disabled style="width:150px;">	
					</td>
					
					<td>
						<span class="title-span">备注：</span> 
						<input id="m_remark" name="m_remark" class="easyui-textbox" value="${facadeBean.info.material.remark }" disabled style="width:150px;">	
					</td>
				</tr>
				
				<tr>
					<td>
						<span class="title-span"><span class="req-span">*</span>成分表：</span> 
						<span id="m_pic_span" <c:if test="${empty facadeBean.info.material.pic}">style="display:none;"</c:if>>	
							<a id="m_pic_a" target="_blank" href="${resUrl}/${facadeBean.info.material.pic}">${fn:substringAfter(facadeBean.info.material.pic, "/")}</a>
						</span>
					</td>
				</tr>
			</table>
		</div>

		<div style="margin-left: 10px; margin-top: 20px;">
			<div class="title">选择基准</div>
			<div style="margin-left: 20px;"><input id="standard" name="standard" style="width: 370px"></div>
		</div>
		
		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">任务信息</div>
			<table class="info">
				<tr>
					<td>
						<span class="title-span">申请人：</span> 
						<input id="applicant" name="applicant" class="easyui-textbox" value="${facadeBean.taskInfo.applicant }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span">科室：</span> 
						<input id="department" name="department" class="easyui-textbox" value="${facadeBean.taskInfo.department }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span">零件图号：</span> 
						<input id="figure" name="figure" class="easyui-textbox" value="${facadeBean.taskInfo.figure}"  style="width:150px;">
					</td>
					<td>
						<span class="title-span">样品数量：</span> 
						<input id="num" name="num" class="easyui-textbox" value="${facadeBean.taskInfo.num }"  style="width:150px;">
					</td>
				</tr>
				
				<tr>
					<td>
						<span class="title-span">样品来源：</span> 
						<input id="origin" name="origin" class="easyui-textbox" value="${facadeBean.taskInfo.origin }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span">抽检原因：</span> 
						<input id="reason" name="reason" class="easyui-textbox" value="${facadeBean.taskInfo.reason }"  style="width:150px;">
					</td>
					<td>
						<span class="title-span">费用出处：</span> 
						<input id="provenance" name="provenance" class="easyui-textbox" value="${facadeBean.taskInfo.provenance}"  style="width:150px;">
					</td>
				</tr>
			</table>
		</div>

		<div style="margin-left: 10px;margin-top:20px;">
			<div class="title">下达实验室</div>
			<div>
				<span class="title-span" style="width: 120px;font-weight: bold;">零部件图谱试验：</span>
				<input id="partsAtlId" name="partsAtlId">
				<input type="checkbox" id="partsAtlCheck" onclick="doCheck('partsAtlCheck')"><span class="red-font">不选</span>
				
				<div style="margin-top:5px;margin-left: 25px;">
					任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="partsAtlCode" name="partsAtlCode" value="${partsAtlCode }" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
					商定完成时间：<input id="partsAtlTime" name="partsAtlTime" value="<fmt:formatDate value='${partsAtlTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" class="easyui-datebox" style="width:140px;" data-options="editable:false">
					<div style="margin-top:5px;">
						测试要求：<input id="partsAtlReq" name="partsAtlReq" value="${partsAtlReq }" class="easyui-textbox"  style="width:390px">
					</div>
				</div>
			</div>
		
			<div style="margin-top:5px;margin-top: 20px;">
				<span class="title-span" style="width: 120px;font-weight: bold;">原材料图谱试验： </span>
				<input id="matAtlId" name="matAtlId">
				<input type="checkbox" id="matAtlCheck" onclick="doCheck('matAtlCheck')"><span class="red-font">不选</span>
				
				<div style="margin-top:5px;margin-left: 25px;">
					任务号：&nbsp;&nbsp;&nbsp;&nbsp;<input id="matAtlCode" name="matAtlCode" value="${matAtlCode }" class="easyui-textbox"  style="width:145px;">&nbsp;&nbsp;&nbsp;&nbsp;
					商定完成时间：<input id="matAtlTime" name="matAtlTime" value="<fmt:formatDate value='${matAtlTime}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>" class="easyui-datebox" style="width:140px;" data-options="editable:false">
					<div style="margin-top:5px;">
						测试要求：<input id="matAtlReq" name="matAtlReq" value="${matAtlReq }" class="easyui-textbox"  style="width:390px">
					</div>
				</div>
			</div>
			
		</div>
	
		 <div style="text-align:center;margin-top:35px;" class="data-row">
			<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">提交</a>
			<span id="exception_error" class="error-message"></span>
		</div>
	</form>
	
	<div id="vehicleDialog"></div>
	<div id="materialDialog"></div>
	<div id="partsDialog"></div>
	
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
	        
	        .red-font{
			   color:red;
			   font-weight: bold;
			}
		</style>
		
		<script type="text/javascript">
			var standardUrl = '${ctx}/ppap/standard';
			// 是否提交中
			var saving = false;
			
			$(function(){
				// 基准选择
				$("#standard").combobox({
					url : standardUrl,
					valueField : 'id',
					textField : 'text',
					formatter : formatItem,
					onLoadSuccess:function(){ //默认选中第一条数据
				        var data= $(this).combobox("getData");
					    //默认选中第一个
		                if (data.length > 0) {
		                	if(!isNull("${facadeBean.iId}")){
		                		$(this).combobox('select', "${facadeBean.iId}");
		                	}else{
		                		$(this).combobox('select', data[0].id);
		                	}
		                }else{
		                	$(this).combobox('select', '');
		                }
					}
				});

				// 编辑的时候
				var iId = "${facadeBean.iId}";
				if(!isNull(iId)){
					$('#standard').combobox('reload', standardUrl + "?v_id=" + $("#v_id").val() + "&p_id=" +  $("#p_id").val() + "&m_id=" + $("#m_id").val());
				}
				
				// 零部件图谱
				$('#partsAtlId').combotree({
					url : '${ctx}/org/getTreeByType?type=3',
					multiple : false,
					animate : true,
					width : '250px'
				});
				setupTree('partsAtlId');

				// 原材料图谱
				$('#matAtlId').combotree({
					url : '${ctx}/org/getTreeByType?type=3',
					multiple : false,
					animate : true,
					width : '250px'
				});
				setupTree('matAtlId');

				// 默认选中CQC实验室
				$("#partsAtlId").combotree("setValue", "20");
				$("#matAtlId").combotree("setValue", "20");
				
				// 编辑时
				if(!isNull(iId)){
					var partsAtlId = "${facadeBean.partsAtlId}";
					if(isNull(partsAtlId)){
						$("#partsAtlCheck").attr("checked", true);
						doCheck("partsAtlCheck");
					}
					
					var matAtlId = "${facadeBean.matAtlId}";
					if(isNull(matAtlId)){
					   $("#matAtlCheck").attr("checked", true);
					   doCheck("matAtlCheck");
					}
				}
			});

			function save() {
				if(saving){
					return false;
				}
				saving = true;
				
				// 零部件图谱试验说明
				var partsAtlCode;
				var partsAtlTime;
				var partsAtlReq;
				// 原材料图谱试验说明
				var matAtlCode;
				var matAtlTime;
				var matAtlReq;
				
				var t_id = $("#t_id").val();
				var v_id = $("#v_id").val();
				var p_id = $("#p_id").val();
				var m_id = $("#m_id").val();

				if (isNull(v_id)) {
					errorMsg("请选择整车信息");
					saving = false;
					return false;
				}

				if (isNull(p_id)) {
					errorMsg("请选择零部件信息");
					saving = false;
					return false;
				}

				if (isNull(m_id)) {
					errorMsg("请选择原材料信息");
					saving = false;
					return false;
				}
				
				var iId = $("#standard").combobox("getValue");
				if(isNull(iId)){
					errorMsg("请选择基准");
					saving = false;
					return false;
				}
				
				
				var num = $("#num").textbox("getValue");
				if(!isNull(num) && !isPositiveNum(num)){
					errorMsg("样品数量必须为整数");
					saving = false;
					return false;
				}
				
				var partsAtlId_val;
				if(!$("#partsAtlCheck").is(':checked')){
					partsAtlId_val = $("#partsAtlId").combotree("getValue");
					if(isNull(partsAtlId_val)){
						errorMsg("请为零部件图谱试验选择实验室");
						saving = false;
						return false;
					}
				}
				
				var matAtlId_val;
				if(!$("#matAtlCheck").is(':checked')){
					matAtlId_val = $("#matAtlId").combotree("getValue");
					if(isNull(matAtlId_val)){
						errorMsg("请为原材料图谱试验选择实验室");
						saving = false;
						return false;
					}
				}
				
				if($("#partsAtlCheck").is(':checked') && $("#matAtlCheck").is(':checked')){
					errorMsg("请选择要做的实验");
					saving = false;
					return false;
				}
				
				if(!isNull(partsAtlId_val)){
					partsAtlCode = $("#partsAtlCode").textbox("getValue");
					partsAtlTime = $("#partsAtlTime").datebox("getValue");
					partsAtlReq = $("#partsAtlReq").textbox("getValue");
				}
				
				if(!isNull(matAtlId_val)){
					matAtlCode = $("#matAtlCode").textbox("getValue");
					matAtlTime = $("#matAtlTime").datebox("getValue");
					matAtlReq = $("#matAtlReq").textbox("getValue");
				}

				$.ajax({
					url : "${ctx}/ppap/transmit",
					data : {
						"t_id" : t_id,
						"i_id" : iId,
						"partsAtlId" : partsAtlId_val,
						"matAtlId" : matAtlId_val,
						"taskType": "${taskType}",
						"partsAtlCode": partsAtlCode,
						"partsAtlTime": partsAtlTime,
						"partsAtlReq": partsAtlReq,
						"matAtlCode": matAtlCode,
						"matAtlTime": matAtlTime,
						"matAtlReq": matAtlReq,
						"num": num,
						"applicant": $("#applicant").textbox("getValue"),
						"department": $("#department").textbox("getValue"),
						"figure": $("#figure").textbox("getValue"),
						"origin": $("#origin").textbox("getValue"),
						"reason": $("#reason").textbox("getValue"),
						"provenance": $("#provenance").textbox("getValue")
					},
					success : function(data) {
						saving = false;
						if (data.success) {
							tipMsg(data.msg, function() {
								window.location.reload();
							});
						} else {
							errorMsg(data.msg);
						}
					}
				});
			}

			function vehicleInfo() {
				$('#vehicleDialog').dialog({
					title : '整车信息',
					width : 1000,
					height : 520,
					closed : false,
					cache : false,
					href : "${ctx}/vehicle/list",
					modal : true,
					onClose: function(){
						standardChange();
					}
				});
			}

			function materialInfo() {
				$('#materialDialog').dialog({
					title : '原材料信息',
					width : 1000,
					height : 520,
					closed : false,
					cache : false,
					href : "${ctx}/material/list",
					modal : true,
					onClose: function(){
						standardChange();
					}
				});
				$('#materialDialog').window('center');
			}

			function partsInfo() {
				$('#partsDialog').dialog({
					title : '零部件信息',
					width : 1000,
					height : 550,
					closed : false,
					cache : false,
					href : "${ctx}/parts/list",
					modal : true,
					onClose: function(){
						standardChange();
					}
				});
				$('#partsDialog').window('center');
			}

			function standardChange() { 
				var v_id = $("#v_id").val();
				var p_id = $("#p_id").val();
				var m_id = $("#m_id").val();  
				
				if(!isNull(v_id) && !isNull(p_id) && !isNull(m_id)){
				    $('#standard').combobox('reload', standardUrl + "?v_id=" + $("#v_id").val() + "&p_id=" +  $("#p_id").val() + "&m_id=" + $("#m_id").val());
				}
			}

			// 格式化基准下拉框
			function formatItem(row) {
				var s = '<span style="font-weight:bold;color:black;">' + row.text + '</span><br/>' + '<span style="color:black;">任务号：'
						+ row.taskCode + '&nbsp;&nbsp;&nbsp;创建时间：' + row.date + '</span>';
				return s;
			}
			
			function doQuery(){
				var qCode = $("#qCode").textbox("getValue");
				if(isNull(qCode)){
					errorMsg("请输入任务号");
					return false;
				}
				
				$.ajax({
					url: "${ctx}/ppap/queryTask",
					data: {
						qCode: qCode
					},
					success: function(data){
						if(data.success){
							var vehicle = eval('(' + data.data.vehicle  + ')');;
							var parts = eval('(' + data.data.parts + ')');;
							var material = eval('(' + data.data.material + ')');;
							
							// 整车信息
							$("#v_code").textbox("setValue", vehicle.code);
							$("#v_type").textbox("setValue", vehicle.type);
							$("#v_proTime").datebox('setValue',formatDate(vehicle.proTime));
							$("#v_proAddr").textbox("setValue", vehicle.proAddr);
							$("#v_remark").textbox("setValue", vehicle.remark);
							$("#v_id").val(vehicle.id);
							
							// 零部件信息
							$("#p_code").textbox("setValue", parts.code);
							$("#p_name").textbox("setValue", parts.name);
							$("#p_proTime").datebox("setValue", formatDate(parts.proTime));
							$("#p_place").textbox("setValue", parts.place);
							$("#p_proNo").textbox("setValue", parts.proNo);
							$("#p_remark").textbox("setValue", parts.remark);	
							$("#p_isKey").combobox('select', parts.isKey);
							$("#p_keyCode").textbox("setValue", parts.keyCode);	
							$("#p_orgId").combotree("setValue", parts.org.id);
							$("#p_orgName").textbox("setValue", parts.org.name);
							$("#p_phone").textbox("setValue", parts.phone);
							$("#p_contacts").textbox("setValue", parts.contacts);
							$("#p_id").val(parts.id);
							
							// 原材料信息
							$("#m_matName").textbox("setValue", material.matName);
							$("#m_proNo").textbox("setValue", material.proNo);
							$("#m_orgId").combotree("setValue", material.org.id);
							$("#m_matNo").textbox("setValue", material.matNo);
							$("#m_matColor").textbox("setValue", material.matColor);
							$("#m_remark").textbox("setValue", material.remark);
							$("#m_orgName").textbox("setValue", material.org.name);
							$("#m_contacts").textbox("setValue", material.contacts);
							$("#m_phone").textbox("setValue", material.phone);
							$("#m_pic_span").show();
							$("#m_pic_a").attr("href", "${resUrl}/" + material.pic);
							$("#m_pic").attr("src", "${resUrl}/" + material.pic);
							$("#m_id").val(material.id);
							
							standardChange();
						}else{
							errorMsg(data.msg);
						}
					}
				});
			}
			
			// 只有最底层才能选择
			function setupTree(id){
				var treeObj = $('#' + id).combotree('tree');	
				treeObj.tree({
				   onBeforeSelect: function(node){
					   if(isNull(node.children)){
							return true;
					   }else{
						   return false;
					   }
				   }
				});
			}
			
			function doCheck(id){
				var treeId = id.replace("Check", "Id");
				var codeId = id.replace("Check", "Code");
				var timeId = id.replace("Check", "Time");
				var reqId = id.replace("Check", "Req");
				
				if($("#" + id).is(':checked')){
					$("#" + treeId).combotree("setValue","");
					$("#" + treeId).combotree({ disabled: true });

					$("#" + codeId).textbox({ disabled: true });
					$("#" + reqId).textbox({ disabled: true });
					$("#" + timeId).datebox({ disabled: true });
					$("#" + codeId).textbox("setValue","");
					$("#" + reqId).textbox("setValue","");
					$("#" + timeId).datebox("setValue","");
				}else{
					$("#" + treeId).combotree({ disabled: false });
					setupTree(treeId);
					
					$("#" + codeId).textbox({ disabled: false });
					$("#" + reqId).textbox({ disabled: false });
					$("#" + timeId).datebox({ disabled: false });
				}
			}
		</script>
	
</body>
