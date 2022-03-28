<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
<style type="text/css">
header {
	margin: 0;
	padding: 0;
}
header h1#logo {
	font-size: 36pt;
	font-style: italic;
	color: navy;
	text-shadow: 2px 2px 2px gray;
}
header ul#menubar {
	list-style: none;
	position: relative;
	left: 150px;
	top: -30px;
}
header ul#menubar li {
	float: left;
	width: 120px;
	height: 30px;
	margin-right: 5px;
	padding: 0;
}
header ul#menubar li a {
	text-decoration: none;
	width: 120px;
	height: 30px;
	display: block;
	background-color: orange;
	text-align: center;
	color: navy;
	font-weight: bold;
	margin: 0;
	text-shadow: 1px 1px 2px white;
	padding-top: 5px;
}
header ul#menubar li a:hover {
	text-decoration: none;
	width: 120px;
	height: 30px;
	display: block;
	background-color: navy;
	text-align: center;
	color: white;
	font-weight: bold;
	margin: 0;
	text-shadow: 1px 1px 2px navy;
	padding-top: 5px;
}
hr {
	clear: both;
}
</style>
</head>
<body>
<header>
	<h1 id="logo">Spring Project : first</h1>
	<!-- 로그인 안 한 경우 -->
	<c:if test="${ empty sessionScope.loginMember }">
		<ul id="menubar">
			<li><a href="${ pageContext.servletContext.contextPath }/nlist.do">공지사항</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글</a></li>
			<li><a href="">암호화테스트</a></li>
			<li><a href="">테스트</a></li>
			<li><a href="">AOP란?</a></li>
		</ul>
	</c:if>
	<!-- 로그인 한 경우 : 관리자인 경우 -->
	<c:if test="${ !empty sessionScope.loginMember and sessionScope.loginMember.admin eq 'Y'}">
		<ul id="menubar">
			<li><a href="${ pageContext.servletContext.contextPath }/nlist.do">공지사항관리</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/mlist.do">회원관리</a></li>
			<li><a href="">사진게시글관리</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/main.do">홈</a></li>
		</ul>
	</c:if>
	<!-- el 에서의 절대경로 표기 : "${ pageContext.servletContext.contextPath }/대상이름.do"-->
	<!-- 로그인 한 경우 : 회원 경우 -->
	<c:if test="${ !empty sessionScope.loginMember and sessionScope.loginMember.admin eq 'N' }">
		<ul id="menubar">
			<li><a href="${ pageContext.servletContext.contextPath }/nlist.do">공지사항</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/blist.do?page=1">게시글</a></li>
			<li><a href="">QnA</a></li>
			<li><a href="">암호화회원가입</a></li>
			<li><a href="${ pageContext.servletContext.contextPath }/main.do">홈</a></li>
		</ul>
	</c:if>
</header>
</body>
</html>





