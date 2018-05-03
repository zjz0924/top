/**
 *  工具类
 */


/**
 *  判断是否为空
 */
function isNull(data){
	if(data == null || data == "" || data == "undefined"){
		return true;
	}else{
		return false;
	}
}


/**
 * 判断是否为正整数
 */ 
function isPositiveNum(num){
	if(num == 0){
		return true;
	}
	
	var re = /^[0-9]*[1-9][0-9]*$/; 
	return re.test(num) 
}


/**
 * 判断是否为正整数, 则提示
 * @param field  字段ID
 * @param type   提示的字段名称
 * @returns {Boolean}
 */
function isPositive(field, type){
	if (!isPositiveNum($("#"+field).val())) {
		errorMsg("系统提示：提交失败，" + type + "为正整数");
		$("#" + field).focus();
		return false;
	}
	return true;
}


/**
 * 判断字段是否必填
 * @param field  字段ID
 * @param type   提示的字段名称
 * @returns {Boolean}
 */
function isRequired(field, type) {
	var data = $("#" + field).val();

	if (data == null || data == "" || data == "undefined") {
		errorMsg("系统提示：提交失败，" + type + "为必填");
		$("#" + field).focus();
		return false;
	}
	return true;
}


/**
 * 是否为数字
 * @param field
 * @param type
 * @returns {Boolean}
 */
function isDouble(field, type) {
	var data = $("#" + field).val();

	if (isNaN($("#"+ field).val())) {
		errorMsg("系统提示：提交失败，" + type + "为数字");
		$("#" + field).focus();
		return false;
	}
	return true;
}


/**
 * 判断两个密码是否相等
 * @param val1
 * @param val2
 * @returns {Boolean}
 */
function isEqual(val1, val2) {
	var data1 = $("#" + val1).val();
	var data2 = $("#" + val2).val();

	if (data1 != data2) {
		errorMsg("系统提示：提交失败，两次输入密码不一致");
		$("#" + val1).focus();
		$("#" + val1).val("");
		$("#" + val2).val("");
		return false;
	}
	return true;
}


/**
 *  检查金额输入
 */
function checkMoney(id){
	var money = $("#"+ id).val();
	if(isNull(money)){
		errorMsg("请输入金额");
		$("#"+ id).focus();
		return false;
	}
	if(isNaN(money)){
		errorMsg("请输入数字");
		$("#"+ id).focus();
		$("#"+ id).val('');
		return false;
	}
	return true;
}

/**
 * 检查手机号码 
 * @param mobile
 */
function checkMobile(mobile){
	if (!(/^1[3|4|5|7|8]\d{9}$/.test(mobile))) {
		return false;
	}
	return true;
}


/**
 * 	格式化数字 （少于10的在前面加上0）
 */ 
function formatNum(val){
	if(parseInt(val) < 10){
		val = "0" + val;
    }
	return val;
}

/**
 * 格式化日期
 * @param date  整型 
 */
function formatDateTime(time){
	if(isNull(time)){
		return "";
	}else{
		var date = new Date(time * 1000);
		var str = date.getFullYear() + "-" + formatNum(date.getMonth()+1) + "-" + formatNum(date.getDate()) + " " + formatNum((date.getHours())) + ":" + formatNum(date.getMinutes()) + ":" + formatNum(date.getSeconds());
		return str; 
	}
}

function formatDate(value){
	if (!isNull(value)) {
	    var date = new Date(value);
	    var year = date.getFullYear().toString();
	    var month = (date.getMonth() + 1);
	    var day = date.getDate().toString();
	    var hour = date.getHours().toString();
	    var minutes = date.getMinutes().toString();
	    var seconds = date.getSeconds().toString();
	    if (month < 10) {
	        month = "0" + month;
	    }
	    if (day < 10) {
	        day = "0" + day;
	    }
	    return  year + "-" + month + "-" + day;
	}else{
		return "";
	}
}


/**
 * 成功提示信息
 * @param msg       信息
 * @param callback  回调函数
 * @param time      窗口关闭时间
 */
function tipMsg(msg, callback, time){
	var time = 2;
	if(!isNull(time)){
		time = time;
	}
	art.dialog.tips(msg, time, "succeed", callback);
}


/**
 * 错误提示信息
 * @param msg       信息
 * @param callback  回调函数
 * @param time      窗口关闭时间
 */
function errorMsg(msg, callback, time){
	var time = 2;
	if(!isNull(time)){
		time = time;
	}
	art.dialog.tips(msg, time, "error", callback);
}


/**
 * 清空查询框
 */
function doCancel(){
	$(":input").val("");
	doSubmit();
}

/**
 * 提交表单
 */
function doSubmit(){
	$("#queryForm").submit();
}


/**
 * 页面跳转
 * @param url
 */
function goTo(url){
	window.location.href = url;
}


/**
 * 确认对话框
 * @param tips 要提示的信息
 * @param url  提交的url
 */
function confirm(tips, url){
	art.dialog.confirm(tips, function () {
		$.ajax({
			url: url + "&time=" + new Date(),
			success: function(data){
				var suc = data.success;
				
				if(suc){
					tipMsg(data.msg, function(){
						window.location.reload();
					});
				}else{
					errorMsg(data.msg);
				}
			}
		});
	});
}


/**
 * 检查 email 格式
 */
function checkEmail(email) {
	var re = /^(?:\w+\.?)*\w+@(?:\w+\.)*\w+$/;
	if (!re.test(email)) {
		return false;
	}
	return true;
}


/**
 * 自适应高度
 */
function adjustHeight(){
	window.parent.adapter(document.body.scrollHeight + 10);
}


/**
 * datagrid - 格式化日期时间
 */
function DateTimeFormatter(value) {
	if (!isNull(value)) {
		var date = new Date(value);
		var year = date.getFullYear().toString();
		var month = (date.getMonth() + 1);
		var day = date.getDate().toString();
		var hour = date.getHours().toString();
		var minutes = date.getMinutes().toString();
		var seconds = date.getSeconds().toString();
		if (month < 10) {
			month = "0" + month;
		}
		if (day < 10) {
			day = "0" + day;
		}
		if (hour < 10) {
			hour = "0" + hour;
		}
		if (minutes < 10) {
			minutes = "0" + minutes;
		}
		if (seconds < 10) {
			seconds = "0" + seconds;
		}
		var value = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
		return "<span title='" + value + "'>" + value + "</span>";
	}else{
		return "";
	}
   
    
    
}


/**
 * datagrid - 格式化日期时间
 */
function DateFormatter(value) {
	if (!isNull(value)) {
	    var date = new Date(value);
	    var year = date.getFullYear().toString();
	    var month = (date.getMonth() + 1);
	    var day = date.getDate().toString();
	    var hour = date.getHours().toString();
	    var minutes = date.getMinutes().toString();
	    var seconds = date.getSeconds().toString();
	    if (month < 10) {
	        month = "0" + month;
	    }
	    if (day < 10) {
	        day = "0" + day;
	    }
	    var val =  year + "-" + month + "-" + day;
	    return "<span title='" + val + "'>" + val + "</span>";
	}else{
		return "";
	}
}

/**
 * datagrid - 格式化单元格提示信息 
 */ 
function formatCellTooltip(value){
	if(!isNull(value)){
		return "<span title='" + value + "'>" + value + "</span>";
	}
}


/**
 * datagrid - 格式化单元格提示信息 
 */ 
function formatCellLock(value){
	if(!isNull(value) && value == 'Y'){
		return "<span style='color:red;font-weight:bold;' title='锁定'>锁定 </span>";
	}else{
		return "<span title='正常'>正常</span>";
	}
} 


/**
 * datagrid - 获取数据
 * @param id    datagrid ID
 * @param url   访问的url
 * @param data  过滤的数据
 */
function getData(id, url, data){
	$.ajax({
        type : "POST",
        dataType : "json",
        url : url,
        data : data,
        success : function(data) {
            $("#" + id).datagrid('loadData', data);
        },
        error : function(err) {
            $.messager.alert('操作提示', '获取信息失败...请联系管理员!', 'error');
        }
  });
}


function getTaskRecordState(taskType, state){
	var str = "";
	if(taskType == 1 || taskType == 4){
		if(state == 1){
			str = "基准信息录入";
		}else if(state == 2){
			str = "审核通过";
		}else if(state == 3){
			str = "审核不通过";
		}else if(state == 4){
			str = "任务下达"
		}else if(state == 5){
			str = "审批同意";
		}else if(state == 6){
			str = "审批不同意";
		}else if(state == 7){
			str = "结果上传";
		}else if(state == 8){
			str = "结果发送";
		}else if(state == 9){
			str = "结果确认";
		}else if(state == 10){
			str = "基准保存";
		}else if(state == 11){
			str = "收费通知";
		}else if(state == 12){
			str = "信息修改";
		}else if(state == 13){
			str = "申请信息修改";
		}else if(state == 14){
			str = "申请试验结果修改";
		}else if(state == 15){
			str = "基本信息修改";
		}else if(state == 16){
			str = "试验结果修改";
		}
	}else if(taskType == 2 || taskType == 3){
		if(state == 1){
			str = "任务下达";
		}else if(state == 2){
			str = "审批同意";
		}else if(state == 3){
			str = "审批不同意";
		}else if(state == 4){
			str = "结果上传"
		}else if(state == 5){
			str = "结果比对正常";
		}else if(state == 6){
			str = "结果比对异常";
		}else if(state == 7){
			str = "结果发送";
		}else if(state == 8){
			str = "结果确认";
		}else if(state == 9){
			str = "结果留存";
		}else if(state == 10){
			str = "重新下任务";
		}else if(state == 11){
			str = "发送警告书";
		}else if(state == 12){
			str = "收费通知";
		}else if(state == 13){
			str = "二次确认";
		}else if(state == 14){
			str = "申请信息修改";
		}else if(state == 15){
			str = "申请试验结果修改";
		}else if(state == 16){
			str = "基础信息修改";
		}else if(state == 17){
			str = "试验结果修改";
		}
	}
	return str;
}


