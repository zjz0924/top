<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@include file="/page/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<link href="${ctx}/resources/css/style.css" rel="stylesheet">
	<link href="${ctx}/resources/css/index3.css" rel="stylesheet">
	<link href="${ctx}/resources/css/responsive-menu.css" rel="stylesheet">
	<link href="${ctx}/resources/css/menu.css" rel="stylesheet">
	<link href="${ctx}/resources/css/cssreset.css" rel="stylesheet">
	<script src="${ctx}/resources/js/responsive-menu.js"></script>
	
	<script type="text/javascript">
	    $(function(){
	    	$("li[_t_nav='GYSTXJS']").addClass('nojuris');
	    	var menu = $('.rm-nav').rMenu({
	            minWidth: '400px'
	        });
	    });
	    
	    function accountInfo(url) {
			$('#accountInfoDialog').dialog({
				title : '用户信息',
				width : 450,
				height : 400,
				closed : false,
				cache : false,
				href : "${ctx}/account/info",
				modal : true
			});
			$('#accountInfoDialog').window('center');
		}
    </script>
	
	<style type="text/css">
		.rm-nav {
		    letter-spacing: 1px;
		}
		
		.rm-toggle.rm-button {
		    margin-top: 25px;
		}
		
		.rm-css-animate.rm-menu-expanded {
		    max-height: none;
		    display: block;
		}
		
		.rm-nav li a,
		.rm-top-menu a {
		    /* padding: .75rem 1rem; */
		    font-size: .9em;
		    line-height: 1.5rem;
		    text-transform: uppercase;
		}
		.rm-layout-expanded .rm-nav > ul > li > a,
		.rm-layout-expanded .rm-top-menu > .rm-menu-item > a {
		    font-size: 15px;height: 49px;line-height: 16px;
		    padding-top: 24px;
		}
	</style>
</head>

<body>
	<div class="header">
		<div class="header_in" style="font-family: 微软雅黑">
			<a href="#"><img src='${ctx}/resources/img/logo.png' class="logo" /></a>
			<hr />
			<h2>上汽通用五菱材料管理平台</h2>
			<div class="ri">
				<p>
					<a href='javascript:void(0)' onclick="accountInfo()"><span><img src='${ctx}/resources/img/icon_11.png' /></span>${currentAccount.nickName }，晚上好</a>&nbsp;&nbsp;
					<a href="${ctx}/loginout" class="loginOut"><img src='${ctx}/resources/img/icon_04.png' />退出</a> 
				</p>
			</div>
			
			<div class="nav">
				<nav class="rm-nav rm-nojs rm-darken">
					<ul>
						<li class='base'><a class='base_a' style='' href='${ctx}/index'>首页</a></li>
						
						<c:forEach items="${currentMenuList}" var="vo">
							<li class="base" _t_nav="YWGL">
								<a href="${ctx}/${vo.url}" class="base_a">${vo.name}</a>
								
								<c:if test="${not empty vo.subList}">
									<ul>
										<c:forEach items="${vo.subList}" var="subVo" varStatus="vst">
											<li class="subli  <c:if test="${not empty subVo.subList}">hasitemli</c:if>">
												<c:choose>
													<c:when test="${not empty subVo.subList}">	
														   <a href="${ctx}/${subVo.url}">${subVo.name}</a>
														   <ul>
														   	  <c:forEach items="${subVo.subList}" var="tVo" varStatus="tst">
														   	  	 
														   	  	 <c:choose>
														   	  	 	<c:when test="${empty subVo.url}">
														   	  	 		<li class="subli">
																			<a href="${ctx}/${tVo.url}">${tVo.name}</a>
																		</li>
														   	  	 	</c:when>
														   	  	 	<c:otherwise>
														   	  	 		<li class="subli">
														   	  	 			<c:if test="${fn:indexOf(subVo.url, '?') < 1 }">
																				<a href="${ctx}/${subVo.url}?choose=${tst.index}">${tVo.name}</a>
																			</c:if>
																			<c:if test="${fn:indexOf(subVo.url, '?') > 0 }">
																				<a href="${ctx}/${subVo.url}&choose=${tst.index}">${tVo.name}</a>
																			</c:if>
																		</li>
														   	  	 	</c:otherwise>
														   	  	 </c:choose>
														   	  	 
														   	  </c:forEach>
														   </ul>
													</c:when>
													<c:otherwise>
														
														<c:choose>
														   	<c:when test="${not empty vo.url}">
														   		<c:if test="${fn:indexOf(vo.url, '?') < 1 }">
														   			<a href="${ctx}/${vo.url}?choose=${vst.index}">${subVo.name}</a>
														   		</c:if>
														   		<c:if test="${fn:indexOf(vo.url, '?') > 0 }">
														   			<a href="${ctx}/${vo.url}&choose=${vst.index}">${subVo.name}</a>
														   		</c:if>
														   	</c:when>
														   	<c:otherwise>
														   		<a href="${ctx}/${subVo.url}">${subVo.name}</a>
														   	</c:otherwise>
														</c:choose>
														
													</c:otherwise>
												</c:choose>
											</li>
										</c:forEach>
									</ul>
								</c:if>
							</li>
						</c:forEach>
					</ul>
				</nav>

			</div>
		</div>
		
		<div id="accountInfoDialog"></div>
		
	</div>
</body>
</html>