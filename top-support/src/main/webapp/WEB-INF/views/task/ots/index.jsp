<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>SGMW</title>
		<%@include file="../../common/source.jsp"%>
		
		<script type="text/javascript">
	        
	        $(function(){	
	        	
				$("#systemTabs").tabs({
					onSelect : function(title, index) {
						refreshTab($('#systemTabs').tabs('getTab', index));
					}
				});
				
				var selectTabIndex = parseInt("${choose}");
				var sub = '${menu}';
				var menuJson = eval('(' + sub + ')');
					
				if(!isNull(menuJson)){
					for(var i = 0; i < menuJson.length; i++){
						var selected = false;
						var obj = menuJson[i];
						if(i == selectTabIndex){
							selected = true;
						}
						createTab(obj.name, obj.url, selected);
					}
				}
				
				$(".tabs").css("height", "36px");
				$(".tabs-inner").css("height", "35px");
				$(".tabs-inner").css("line-height", "35px");
			});
	        
			function refreshTab(currentTab) {
				var url = currentTab.panel('options').href; 
	
			 	$('#systemTabs').tabs('update', {
					tab : currentTab,
					options : {
						href : url
					}
				});
			 	
			 	// 刷新时样式会重新设置
			 	$(".tabs").css("height", "36px");
				$(".tabs-inner").css("height", "35px");
				$(".tabs-inner").css("line-height", "35px");
			}
			
			// 创建tab
		 	function createTab(title, url, selected) {  
				var content = '<iframe src="${ctx}/' + url + '" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="auto" width="100%" height="900px;">'; 
			    $('#systemTabs').tabs('add', {   
			        title : title,   
			        selected : selected,   
			        closable : false,   
			        content : content   
			    });   
			}; 
			
		</script>
		
	</head>
	
	<body>
		<%@include file="../../common/header.jsp"%>
		
		<!--banner-->
		<div class="inbanner XSLR">
			<span style="font-size: 30px;font-weight: bold; margin-top: 70px; display: inline-block; margin-left: 80px;color: #4169E1">${menuName}</span>
		</div>
		
		<div style="width: auto;height: 900px; min-height: 900px; background: #e6e6e6; font-size: 14px;margin-left: 5%;margin-right: 5%;margin-top:20px;margin-bottom: 20px;">
			<div id="systemTabs" style="width:100%;height:auto;" data-options="plain: true,pill: true, justified: true, narrow: false">
		    </div>
		</div>
		
		<!-- footer -->
		<%@include file="../../common/footer.jsp"%>
	</body>
</html>