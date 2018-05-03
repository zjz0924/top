<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../../common/source.jsp"%>
		
		<script type="text/javascript">
			var url = "${ctx}/operationlog/getLogList";
			var datagrid = "operationTable";
			
			$(function(){
				 $("#" + datagrid).datagrid({
			        url : url,
			        singleSelect : true, /*是否选中一行*/
			        width:'auto', 
			        pagination : true,  /*是否显示下面的分页菜单*/
			        border:false,
			        rownumbers: true,
			        height: "380px",
			        title: '日志信息',
			        columns : [ [  {
			            field : 'id', 
			            hidden: 'true'
			        }, {
			            field : 'userName',
			            title : '用户名',
			            width : '120',
			            align : 'center'
			        }, {
			            field : 'type',
			            title : '类型',
			            width : '120',
			            align : 'center'
			        }, {
			            field : 'operation',
			            title : '操作',
			            width : '120',
			            align : 'center'
			        }, {
			            field : 'clientIp',
			            title : '访问IP',
			            width : '120',
			            align : 'center'
			        }, {
			            field : 'detail',
			            title : '详情',
			            width : '480',
			            align : 'center',
			            formatter: formatCellTooltip
			        }, {
			            field : 'time',
			            title : '时间',
			            width : '140',
			            align : 'center',
			            formatter: DateTimeFormatter
			        }
			        ]],
			        onClickRow:function(rowIndex,rowData){
			        	info("${ctx}/operationlog/detail?id=" + rowData.id);
			       	}
			    });
				 
				// 分页信息
				$('#' + datagrid).datagrid('getPager').pagination({  
	                pageSize: "${defaultPageSize}",  
	                pageNumber: 1,  
	                displayMsg: '当前显示 {from} - {to} 条记录    共  {total} 条记录',
	                onSelectPage: function (pageNumber, pageSize) {//分页触发  
	                	var data = {
	                        'userName' : $("#account").val(),
	                        'type' : $("#type").val(),
	                        'startTimeFrom' : $("#startTimeFrom").val(),
	                        'startTimeTo' : $("#startTimeTo").val(),
	                        'operation' : $("#operation").val(),
	                        'pageNum' : pageNumber,
	                        'pageSize' : pageSize
	                    }
	                	getData(datagrid, url, data);
	            	} 
				});
			});
			
			function doSearch(){
				var data = {
	                'userName' : $("#account").textbox('getValue'),
	                'type' : $("#type").val(),
	                'startTimeFrom' : $("#startTimeFrom").val(),
	                'startTimeTo' : $("#startTimeTo").val(),
	                'operation' : $("#operation").val()
	            }
				getData(datagrid, url, data);
			}
			
			function doClear(){
				$("#account").textbox('clear');
				$("#operation").combobox('select', "");  
				$("#type").combobox('select', "");
				$("#startTimeFrom").val('');
				$("#startTimeTo").val('');
				getData(datagrid, url, {});
			}
			
			function info(url){
				$('#dd').dialog({
				    title: '日志信息',
				    width: 800,
				    height: 500,
				    closed: false,
				    cache: false,
				    href: url,
				    modal: true
				});
				$('#dd').window('center');
			}
		</script>
		
	</head>
	
	<body>
		<div style="margin-top: 15px; padding-left: 20px; margin-bottom: 10px;font-size:12px;">
			用户名：<input id="account" name="account" class="easyui-textbox" style="width: 130px;"> &nbsp;&nbsp;&nbsp;
			
			类型：<select id="type" name="type" class="easyui-combobox" style="width: 130px;" data-options="panelHeight:'auto'"> 
				<option value="">全部</option>
				<c:forEach items="${typeList}" var="vo">
					<option value="${vo}">${vo}</option>
				</c:forEach>
			</select> &nbsp;&nbsp;&nbsp;
			
			<%-- 操作：<select id="operation" name="operation" class="easyui-combobox" data-options="panelHeight:'auto'" style="width: 130px;margin-right:20px;" >
				<option value="">全部</option>
				<c:forEach items="${operationList}" var="vo">
	                  <option value="${vo}">${vo}</option>
	               </c:forEach>
			</select> --%>&nbsp;&nbsp;&nbsp;
	
			 时间：<input type="text" id="startTimeFrom" name="startTimeFrom" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'startTimeTo\')}'})" class="textbox" style="line-height: 23px;width:130px;display:inline-block"/> -
	            <input type="text" id="startTimeTo" name="startTimeTo" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startTimeFrom\')}'})" class="textbox" style="line-height: 23px;width:130px;display:inline-block"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doSearch()">查询</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" style="width:80px;" onclick="doClear()">清空</a>
		</div>
	
		<div style="margin-top:10px;">
			<table id="operationTable" style="height:auto;width:auto"></table>
		</div>
		
		<div id="dd"></div>
	</body>
</html>
	
	
