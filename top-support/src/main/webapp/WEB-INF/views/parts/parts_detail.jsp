<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<body>
	<form method="POST" enctype="multipart/form-data" id="uploadForm">
		<input type="hidden" id="id" name="id" value="${facadeBean.id}"/>
		
		<div style="margin-top:15px;margin-left:20px;">
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span"><span class="req-span">*</span>代码：</span> 
					<input id="code" name="code" value="${facadeBean.code}" <c:if test="${not empty facadeBean.id}">disabled</c:if>>
					<span id="code_error" class="error-message"></span>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span"><span class="req-span">*</span>名称：</span> 
					<input id="name" name="name" class="easyui-textbox" value="${facadeBean.name}">
					<span id="name_error" class="error-message"></span>
				</div>
				
			</div>
			
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span">&nbsp;&nbsp;类型：</span> 
					<select id="type" name="type" style="width:171px;" onChange="autoFillIn()">
					    <option value="1" <c:if test="${facadeBean.type == 1}">selected="selected"</c:if>>基准</option>
					    <option value="2" <c:if test="${facadeBean.type == 2}">selected="selected"</c:if>>抽样</option>
					</select>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span">&nbsp;&nbsp;生产商：</span> 
					<input id="producer" name="producer" class="easyui-textbox" value="${facadeBean.producer}">
					<span id="producer_error" class="error-message"></span>
				</div>
			</div>
			
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span">&nbsp;&nbsp;生产日期：</span> 
					<input id="proTime" name="proTime" type="text" class="easyui-datebox" value="${facadeBean.proTime}">
					<span id="proTime_error" class="error-message"></span>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span">&nbsp;&nbsp;生产场地：</span> 
					<input id="place" name="place" class="easyui-textbox" value="${facadeBean.place}">
					<span id="place_error" class="error-message"></span>
				</div>
			</div>
			
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span">&nbsp;&nbsp;生产批号：</span> 
					<input id="proNo" name="proNo" class="easyui-textbox" value="${facadeBean.proNo}">
					<span id="proNo_error" class="error-message"></span>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span">&nbsp;&nbsp;生产工艺：</span> 
					<input id="technology" name="technology" class="easyui-textbox" value="${facadeBean.technology}">
					<span id="technology_error" class="error-message"></span>
				</div>
			</div>
			
			
			<p style="font-weight:bold;font-size: 14px;color: #4F94CD">材料信息</p>
			
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span">&nbsp;&nbsp;材料名称：</span> 
					<input id="matName" name="matName" class="easyui-textbox" value="${facadeBean.matName}">
					<span id="matName_error" class="error-message"></span>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span">&nbsp;&nbsp;材料牌号：</span> 
					<input id="matNo" name="matNo" class="easyui-textbox" value="${facadeBean.matNo}">
					<span id="matNo_error" class="error-message"></span>
				</div>
			</div>
			
			
			<div class="data-row">
				<div class="data-cell-left">
					<span class="title-span">&nbsp;&nbsp;材料颜色：</span> 
					<input id="matColor" name="matColor" class="easyui-textbox"  value="${facadeBean.matColor}">
					<span id="matColor_error" class="error-message"></span>
				</div>
				
				<div class="data-cell-right">
					<span class="title-span">&nbsp;&nbsp;材料生产商：</span> 
					<input id="matProducer" name="matProducer" class="easyui-textbox" value="${facadeBean.matProducer}">
					<span id="matProducer_error" class="error-message"></span>
				</div>
			</div>
			
			<div class="data-row" style="margin-top:15px;">
				<span class="title-span">图片：</span> 
				<input id="pic" name="pic" class="easyui-filebox" style="width:171px" data-options="buttonText: '选择'">
				<span id="pic_error" class="error-message"></span>
			</div>
			
			<c:if test="${not empty facadeBean.pic}">
				<div style="margin-top:5px;margin-left: 83px;margin-bottom: 10px;">
					<a href="${resUrl}/${facadeBean.pic}" target="_blank">
						<img src="${resUrl}/${facadeBean.pic}" style="width: 100px; height: 50px;"></img>
					</a>
				</div>
			</c:if>
			
			<div class="data-row" style="margin-top:10px;">
				<span class="title-span">备注：</span> 
				<input id="remark" name="remark" class="easyui-textbox" multiline="true" value="${facadeBean.remark }" style="height:50px">		
			</div>
			 
			 <div style="text-align:center;margin-top:35px;" class="data-row">
				<a href="javascript:void(0);"  onclick="save()" class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
				<span id="exception_error" class="error-message"></span>
			</div>
		</form>
	</div>
	
	<script type="text/javascript">
		$(function(){
			$("#code").textbox({
				onChange: function(n,o){
					autoFillIn();
				}
			});
			
			$("#type").combobox({
				panelHeight: 'auto',
				onChange: function (n,o) {
					autoFillIn();
				}
			});
		});
	
		function save(){
			if(!isRequire("code", "编码必填")){ return false; }
			if(!isRequire("name", "名称必填")){ return false; }
			
			var fileDir = $("#pic").filebox("getValue");
			if (!isNull(fileDir)) {
				var suffix = fileDir.substr(fileDir.lastIndexOf("."));
				if (".jpg" != suffix && ".png" != suffix && ".jpeg" != suffix && ".gif" != suffix) {
					$("#pic_error").html("请选择图片格式文件导入！");
					return false;
				}
			}else{
				$("#pic_error").html("");
			}
			
			$('#uploadForm').ajaxSubmit({
				url: "${ctx}/parts/save",
				dataType : 'text',
				success:function(msg){
					var data = eval('(' + msg + ')');
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
		
		/**
		  * 判断是否必填
		  * id: 属性ID
		  * emsg: 错误信息
		*/
		function isRequire(id, emsg){
			var val = $("#" + id).val();
			if(isNull(val)){
				err(id + "_error", emsg);
				return false;
			}else{
				err(id + "_error", "");
				return true;
			}
		}
		
		// 自动填写
		function autoFillIn(){
			var type = $("#type").val();
			var code = $("#code").val();
		
			if(type == 2 && !isNull(code)){
				$.ajax({
					url: "${ctx}/parts/getByCode",
					data: {
						code: code
					},
					success: function(data){
						if(!isNull(data)){
							$("#name").textbox('setValue', data.name);
							$("#producer").textbox('setValue', data.producer);
							if(!isNull(data.proTime)){
								var d = formatDate(data.createTime);
								$("#proTime").datebox('setValue', d);
							}else{
								$("#proTime").datebox('setValue', '');
							}
							$("#place").textbox('setValue', data.place);
							$("#proNo").textbox('setValue', data.proNo);
							$("#technology").textbox('setValue', data.technology);
							$("#matName").textbox('setValue', data.matName);
							$("#matNo").textbox('setValue', data.matNo);
							$("#matColor").textbox('setValue', data.matNo);
							$("#matProducer").textbox('setValue', data.matProducer);
						}
					}
				});
			}
		}
	</script>
	
	<style type="text/css">
		.data-row{
			height: 35px;
		}
		
		.data-cell-left{
			display: inline-block;
			width: 50%;
		}
		
		.data-cell-right{
			display: inline-block;
			width: 45%;
		}
	
		.title-span{
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
