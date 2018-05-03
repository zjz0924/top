<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>403</title>
	</head>
	
	<style type="text/css">
		body, div {
		    margin: 0;
		    padding: 0;
		}
		body {
		    background: url("../../../resources/img/error/error_bg.jpg") repeat-x scroll 0 0 #67ACE4;
		}
		#container {
		    margin: 0 auto;
		    padding-top: 100px;
		    text-align: center;
		    width: 560px;
		    height: 500px;
		}
		#container span {
		    border: medium none;
		    margin-bottom: 50px;
		}
		#container .msg {
		    margin-bottom: 65px;
		}
		#cloud {
		    background: url("../../../resources/img/error/error_cloud.png") repeat-x scroll 0 0 transparent;
		    bottom: 0;
		    height: 170px;
		    position: absolute;
		    width: 100%;
		}
	
	</style>

	<body>
		<div id="container">
			<span style="font-weight:bold;font-size: 150px;color:white;">403</span>
			<div>
				<span style="font-size: 25px;color:white;">:（  很抱歉，你没有访问权限，请与管理员联系 ）</span>
			</div>
			
		</div>
		<div id="cloud" class="png"></div>
	</body>
</html>