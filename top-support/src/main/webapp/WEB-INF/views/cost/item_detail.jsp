<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<div style="margin-top: 20px;">
		<div class="title">结果确认</div>
		<div style="margin-left: 10px; margin-bottom: 15px;">
			<c:choose>
				<c:when test="${facadeBean.task.type == 1}">
					OTS阶段任务
				</c:when>
				<c:when test="${facadeBean.task.type == 2}">
					PPAP阶段任务
				</c:when>
				<c:when test="${facadeBean.task.type == 3}">
					SOP阶段任务
				</c:when>
				<c:otherwise>
					材料研究所任务
				</c:otherwise>
			</c:choose>
			
			<c:choose>
				<c:when test="${facadeBean.labType == 1}">
					-- 零部件图谱实验
				</c:when>
				<c:when test="${facadeBean.labType == 2}">
					-- 零部件型式实验
				</c:when>
				<c:when test="${facadeBean.labType == 3}">
					-- 原材料图谱实验
				</c:when>
				<c:otherwise>
					-- 原材料型式实验
				</c:otherwise>
			</c:choose>
			
			&nbsp;&nbsp;&nbsp;&nbsp;第 <span style="color:red;font-weight:bold;">${facadeBean.times}</span> 次实验，结果确认：<c:if test="${ facadeBean.labResult == 1}"><span style="color:green;font-weight:bold;">合格</span></c:if><c:if test="${ facadeBean.labResult == 2}"><span style="color:red;font-weight:bold;">不合格</span></c:if>
		</div>
	</div>
	
	<div class="title">收费清单&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);"  onclick="addItem()" class="easyui-linkbutton" data-options="iconCls:'icon-add'">添加</a></div>
	<div>
		<table class="info" id="expItemTable">
			<tr>
				<td style="background: #F0F0F0;font-weight:bold;">序号</td>
				<td class="title-td">试验项目</td>
				<td class="title-td">参考标准</td>
				<td class="title-td">单价</td>
				<td class="title-td">数量</td>
				<td class="title-td">备注</td>
				<td style="background: #F0F0F0;font-weight:bold;">操作</td>
			</tr>
			
			<c:forEach var="i" begin="1" end="1" varStatus="status">
				<tr num="${status.index}">
					<td style="background: #f5f5f5;padding-left:5px;">${status.index}</td>
					<td class="value-td1">
						<input id="project_${status.index}" name="project_${status.index}" class="easyui-textbox"/>
						<span id="project_${status.index}_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="standard_${status.index}" name="standard_${status.index}" class="easyui-textbox"/>
						<span id="standard_${status.index}_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="price_${status.index}" name="price_${status.index}" class="easyui-numberbox" style="width: 100px;" data-options="min:0,precision:2"/>
						<span id="price_${status.index}_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="num_${status.index}" name="num_${status.index}" class="easyui-numberbox" style="width: 100px;" data-options="min:0,precision:0"/>
						<span id="num_${status.index}_error" class="req-span"></span>
					</td>
					<td class="value-td1">
						<input id="remark_${status.index}" name="remark_${status.index}" class="easyui-textbox"/>
						<span id="remark_${status.index}_error" class="req-span"></span>
					</td>
					<td style="background: #f5f5f5;padding-left:5px;">
						<a href="javascript:void(0);"  onclick="deleteItem(${status.index})"><i class="icon icon-cancel"></i></a>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
	
	<div style="margin-top: 15px;">
		<div class="title">发送机构</div>
		<div style="margin-left: 10px;">
			<input id="org" name="org"/>
			<span id="org_error" style="color:red;margin-left: 20px;"></span>
		</div>
	</div>
	
	<div id="tips" style="color:red;font-weight:bold;" align="center"></div>
	<div id="saveBtn" align=center style="margin-top: 15px;">
		<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">提交</a>&nbsp;&nbsp;
		<a href="javascript:void(0);"  onclick="doCancel()" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">取消</a>
	</div>
			
	<script type="text/javascript">
		$(function(){
			$('#org').combotree({
				url: '${ctx}/org/tree',
				multiple: true,
				animate: true,
				width: '250px',
				panelHeight: '150px'
			});
		
			// 只有最底层才能选择
			var orgTree = $('#org').combotree('tree');	
			orgTree.tree({
				checkbox: function(node){
				   if(isNull(node.children)){
						return true;
				   }else{
					   return false;
				   }
			   }
			});
		});
		
		
		function save(){
			$("#saveBtn").hide();
			
			var dataArray = [];
			var flag = true;
			var date = new Date();
			
			var expItemTable = $("#expItemTable").length;
			if(expItemTable > 0){
				if($("tr[num]").length < 1){
					$("#tips").html("请添加试验项目");
					$("#tips").html("");
					$("#saveBtn").show();
					return false;
				}
				$("#result_error").html("");
			}
			
			$("tr[num]").each(function(){
				var num = $(this).attr("num");
				
				// 实验项目
				var project = $("#project_" + num).textbox("getValue");
				if(isNull(project)){
					$("#project_"+ num +"_error").html("必填");
					flag = false;
					return false;
				}else{
					$("#project_"+ num +"_error").html("");
				}
				
				// 参考标准
				var standard = $("#standard_" + num).textbox("getValue");
				if(isNull(standard)){
					$("#standard_"+ num +"_error").html("必填");
					flag = false;
					return false;
				}else{
					$("#standard_"+ num +"_error").html("");
				}
				
				// 单价
				var price = $("#price_" + num).numberbox("getValue");
				if(isNull(price)){
					$("#price_"+ num +"_error").html("必填");
					flag = false;
					return false;
				}else{
					$("#price_"+ num +"_error").html("");
				}
				
				// 数量
				var num_val = $("#num_" + num).numberbox("getValue");
				if(isNull(num_val)){
					$("#num_"+ num +"_error").html("必填");
					flag = false;
					return false;
				}else{
					$("#num_"+ num +"_error").html("");
				}
				var remark = $("#remark_" + num).textbox("getValue");
				
				var obj = new Object();
				obj.aId = "${currentAccount.id}"
				obj.project = project;
				obj.standard = standard;
				obj.cId = "${facadeBean.id}";
				obj.price = price;
				obj.num = num_val;
				obj.total = price * num_val;
				obj.remark = remark
				obj.createTime = date;
				dataArray.push(obj);
			});
			
			if(flag == false){
				$("#saveBtn").show();
				return false;
			}
			
			var orgs = $("#org").combotree("getValues");
			if(isNull(orgs)){
				$("#org_error").html("请选择要发送的机构");
				$("#saveBtn").show();
				return false;
			}
			$("#org_error").html("");
			
			$("#tips").html("正在发送中，请稍等");
			$.ajax({
				url: "${ctx}/cost/costSend",
				data: {
					"costId": "${facadeBean.id}",
					"result": JSON.stringify(dataArray),
					"orgs": orgs.toString()
				},
				success: function(data){
					if(data.success){
						$("#tips").html(data.msg);
						$("#tips").css("color", "green");
						
						tipMsg(data.msg, function(){
							window.location.reload();
						});
					}else{
						$("#saveBtn").show();
						$("#tips").html(data.msg);
					}
				}
			})
		}
		
		
		function doCancel(){
			$("#dlg").dialog("close");
		}
		
		/**
		 *  添加项目
		 */
		function addItem(){
			$("#tips").html("");
			
			var num;
			for(var i = 1; i >= 1 ; i++){
				var len = $("tr[num="+ i + "]").length;
				if(len < 1){
					num = i;
					break;
				}
			}
			
			var str = "<tr num='"+ num + "'>"
					+	 "<td style='background: #f5f5f5;padding-left:5px;'>"+ num +"</td>"
					+	 "<td class='value-td1'>"
					+		"<input id='project_"+ num +"' name='project_"+ num +"' class='easyui-textbox'/>"
					+		"<span id='project_"+ num +"_error' class='req-span'></span>"
					+	 "</td>"
					+	 "<td class='value-td1'>"
					+		"<input id='standard_"+ num +"' name='standard_"+ num +"' class='easyui-textbox'/>"
					+		"<span id='standard_"+ num +"_error' class='req-span'></span>"
					+	 "</td>"
					+	 "<td class='value-td1'>"
					+		"<input id='price_"+ num +"' name='price__"+ num +"' class='easyui-numberbox' style='width: 100px;' data-options='min:0,precision:2'/>"
					+		"<span id='price_"+ num +"_error' class='req-span'></span>"
					+	 "</td>"
					+	 "<td class='value-td1'>"
					+		"<input id='num_"+ num +"' name='num_"+ num +"' class='easyui-numberbox' style='width: 100px;' data-options='min:0,precision:0'/>"
					+		"<span id='num_"+ num +"_error' class='req-span'></span>"
					+	 "</td>"
					+	 "<td class='value-td1'>"
					+		"<input id='remark_"+ num +"' name='remark_"+ num +"' class='easyui-textbox'/>"
					+		"<span id='remark_"+ num +"_error' class='req-span'></span>"
					+	 "</td>"
					+	 "<td style='background: #f5f5f5;padding-left:5px;'>"
					+	    "<a href='javascript:void(0);'  onclick='deleteItem("+num+")'><i class='icon icon-cancel'></i></a>"
					+	 "</td>"
					+"</tr>";
			
			$("#expItemTable").append(str);
			
			// 渲染输入框
			$.parser.parse($("#project_"+ num).parent());
			$.parser.parse($("#standard_"+ num).parent());
			$.parser.parse($("#price_"+ num).parent());
			$.parser.parse($("#num_"+ num).parent());
			$.parser.parse($("#remark_"+ num).parent());
		}
		
		function deleteItem(num){
			$("tr[num='"+ num + "']").remove();
		}
	</script>	
	
	<style type="text/css">
		.title {
			margin-left: 10px;
			margin-bottom: 8px;
			font-size: 14px;
			color: #1874CD;
    		font-weight: bold;
		}
		
		.info{
			width:98%;
			margin-left: 5px;
			font-size: 14px;
		}
		
		.info tr{
			height: 30px;
		}
		
		.title-td {
			width:13%;
			background: #F0F0F0;
			padding-left: 5px;
			font-weight: bold;
		}
		
		.value-td{
			width:32%;
			background: #f5f5f5;
			padding-left: 5px;
		}
		
		.value-td1{
			background: #f5f5f5;
			padding-left: 5px;
		}
		
		.req-span{
			font-weight: bold;
			color: red;
		}
		
		.icon{
			width:16px;
			height: 16px;
			display: inline-block;
		}
		
	</style>
	
</body>
