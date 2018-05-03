<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../common/source.jsp"%>
		
		<style type="text/css">
			.qlabel{
				display: inline-block;
				width: 75px;
				text-align: right;
			}
			
			.title{
				font-size: 15px;
				text-transform: uppercase;
				font-weight: 600;
				text-align:center;
				margin-top:25px;
				margin-bottom: 10px;
			}
			
			.count{
				font-size: 20px;
				font-weight: 700;
				padding-top: 10px;"
			}
			
			.info-box{
				width: 220px;
				height:150px; 
				color: white;
				float: left;
			}
			
			.icon{
	        	width:16px;
	        	height: 16px;
	        	display: inline-block;
	        }
	        
	        .title1{
	        	display:inline-block;
	        	width: 80px;
	        	margin-left: 20px;
	        	font-size: 15px;
	        }
		</style>
		
		<script type="text/javascript">
			$(function(){
				$('#parts_org').combotree({
					url: '${ctx}/org/getTreeByType?type=2',
					multiple: false,
					animate: true,
					width: '163px'
				});
				
				// 只有最底层才能选择
				var pOrgTree = $('#parts_org').combotree('tree');	
				pOrgTree.tree({
				   onBeforeSelect: function(node){
					   if(isNull(node.children)){
							return true;
					   }else{
						   return false;
					   }
				   }
				});	
				
				$('#lab_org').combotree({
					url: '${ctx}/org/getTreeByType?type=3',
					multiple: false,
					animate: true
				});
			});
		
			function getResult(){
				var startConfirmTime = $("#q_startConfirmTime").val();
				var endConfirmTime = $("#q_endConfirmTime").val();
				var taskType = $("#q_taskType").combobox("getValue");
				var v_code = $("#v_code").textbox("getValue");
				var v_type = $("#v_type").textbox("getValue");
				var p_code = $("#p_code").textbox("getValue");
				var parts_org = $("#parts_org").combotree("getValue");
				var lab_org = $("#lab_org").combotree("getValue");
				
				if(isNull(startConfirmTime) && isNull(endConfirmTime)){
					errorMsg("请选择确定时间");
					return false;
				}
				
				if(isNull(taskType)){
					errorMsg("请选择任务类型时间");
					return false;
				}
				
				$.ajax({ 
					url: "${ctx}/statistic/getResult",
					data: {
						"startConfirmTime": startConfirmTime,
						"endConfirmTime": endConfirmTime, 
						"taskType": taskType,
						"v_code": v_code,
						"v_type": v_type,
						"p_code": p_code,
						"parts_org": parts_org, 
						"lab_org": lab_org,
						'applicant': $("#applicant").textbox("getValue"),
						'department': $("#department").textbox("getValue"),
						'reason': $("#reason").textbox("getValue"),
						'provenance': $("#provenance").textbox("getValue")
					},
					success: function(data){
						if(data.success){
							var taskNum = data.data.taskNum;
							var passNum = data.data.passNum;
							var onceNum = data.data.onceNum;
							
							$("#taskNum").html(taskNum);
							$("#passNum").html(passNum);
							$("#onceNum").html(onceNum);
							
							var passNumPer = percentNum(passNum, taskNum);
							var onceNumPer = percentNum(onceNum, taskNum);
							
							$("#passNum_per").html(passNumPer);
							$("#onceNum_per").html(onceNumPer);
						}
					}
				});
			}
			
			function percentNum(num, num2) { 
			   if(num == 0 || num2 == 0){
				   return "0 %";
			   }
			   return (Math.round(num / num2 * 10000) / 100.00 + " %"); //小数点后两位百分比
			}
			
			function clearAll(){
				$("#q_startConfirmTime").val('');
				$("#q_endConfirmTime").val('');
				$("#q_taskType").combobox('select', "");
				$("#v_code").textbox('clear');
				$("#v_type").textbox('clear');
				$("#p_code").textbox('clear');
				$("#parts_org").combotree("setValue","");
				$("#lab_org").combotree("setValue","");
				$("#applicant").textbox("clear");
				$("#department").textbox("clear");
				$("#reason").textbox("clear");
				$("#provenance").textbox("clear");
				
				$("#taskNum").html("0");
				$("#passNum").html("0");
				$("#onceNum").html("0");
				$("#passNum_per").html("0 %");
				$("#onceNum_per").html("0 %");
			}
		</script>
	</head>
	
	<body>
		<%@include file="../common/header.jsp"%>
		
		<!--banner-->
		<div class="inbanner XSLR">
			<span style="font-size: 30px;font-weight: bold; margin-top: 70px; display: inline-block; margin-left: 80px;color: #4169E1">${menuName}</span>
		</div>
	
		<div style="margin-top: 25px; padding-left: 20px; margin-bottom: 10px;font-size:12px;margin-left: 5%;margin-right: 5%;height: 500px; ">
			<div>
				<div>
					<span class="qlabel">整车代码：</span>
					<input id="v_code" name="v_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">车型：</span>
					<input id="v_type" name="v_type" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">零件号：</span>
					<input id="p_code" name="p_code" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">零件生产商：</span>
					<input id="parts_org" name="parts_org"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				
				<div style="margin-top:10px;">
					<span class="qlabel">实验室：</span>
					<input id="lab_org" name="lab_org" style="width: 168px;"/> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				
					<span class="qlabel">任务类型：</span>
					<select id="q_taskType" name="q_taskType" style="width:168px;" class="easyui-combobox" data-options="panelHeight: 'auto'">
						<option value="">全部</option>
						<option value="1">车型OTS阶段任务</option>
						<option value="2">车型PPAP阶段任务</option>
						<option value="3">车型SOP阶段任务</option>
						<option value="4">非车型材料任务</option>
					</select> &nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">确认时间：</span>
					<input type="text" id="q_startConfirmTime" name="q_startConfirmTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'q_endConfirmTime\')}'})" class="textbox" style="line-height: 23px;width:120px;display:inline-block"/> - 
					<input type="text" id="q_endConfirmTime" name="q_endConfirmTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'q_startConfirmTime\')}'})" class="textbox"  style="line-height: 23px;width:120px;display:inline-block;"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					
				</div>
				
				<div style="margin-top:10px;">
					<span class="qlabel">申请人：</span>
					<input id="applicant" name="applicant" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">科室：</span>
					<input id="department" name="department" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">抽检原因：</span>
					<input id="reason" name="reason" class="easyui-textbox" style="width: 168px;"> &nbsp;&nbsp;&nbsp;&nbsp;
					
					<span class="qlabel">费用出处：</span>
					<input id="provenance" name="provenance" class="easyui-textbox" style="width: 168px;">
				</div>
				
				<div style="margin-top: 10px;text-align:right;">
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="getResult()">查询</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" style="width:80px;" onclick="clearAll()">清空</a>
				</div>
			</div>

			<div style="border: 0.5px dashed #C9C9C9;width:98%;margin-top:15px;margin-bottom: 15px;"></div>

			<div style="margin-top: 50px;">
				<div style="font-weight: bold; color: #1874CD;margin-bottom: 30px;font-size:20px;">统计结果：</div>
				<div class="info-box" style="background-color: #57889c;">
					<div class="title">任务数量</div>
					<div class="count" id="taskNum" style="text-align: center;font-size: 50px;">0</div>
				</div>
				<div class="info-box" style="background-color: #26c281;margin-left: 50px;">
					<div class="title">合格 </div>
					<div class="count">
						<span class="title1">数量</span>
						<span id="passNum">0</span>
					</div>
					<div class="count">
						<span class="title1">比例</span>
						<span id="passNum_per">0 %</span>
					</div>
				</div>
				<div class="info-box" style="background-color: #e65097;margin-left: 50px;">
					<div class="title">一次不合格</div>
					<div class="count">
						<span class="title1">数量</span>
						<span id="onceNum">0</span>
					</div>
					<div class="count">
						<span class="title1">比例</span>
						<span id="onceNum_per">0 %</span>
					</div>
				</div>
			</div>
		</div>

		<!-- footer -->
		<%@include file="../common/footer.jsp"%>
		
	</body>	
</html>