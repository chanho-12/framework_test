<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>first</title>
<style type="text/css">
form.sform{
	display: none;
	background: lightgray;
	
}
</style>
<script type="text/javascript" src="${ pageContext.servletContext.contextPath }/resources/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
//jQuery 로 이벤트 처리 : 검색 form 을 보이게 안보이게 처리함
/*
 * jQuery(document.ready(){ 코드작성 }); => 이 페이지가 브라우저에 읽어들이기가 완료되면 {}안의 내용이 자동 실행됨 
   jQuery 는 $로 표기해도됨. 위의 구문을 줄여서  $(function(){}) ;  사용해도됨
   => 자바스크립트로는 window.onload = function(){}; 과 같음
 */
$(function(){
	//작성된 이벤트 처리 코드는 실행 대기 상태가 됨
	//jQuery("태크선택자").실행할메소드(전달값,....);
	$("input[name=item]").on("change", function(){
		//change 이벤트가 발생간 radio 와 연결된 폼만 보이게 하고, 나머지 폼들은 안보이게 처리함
		$("input[name=item]").each(function(index){
			//해당 index 번째 radio 가 checked 인지 확인하고
			if($(this).is(":checked")){
				$("form.sform").eq(index).css("display", "block");
			}else{
				$("form.sform").eq(index).css("display", "none");
			}
		});
	});
});
   
//로그인 가능/제한 라디오 체크를 변경했을때 실행되는 함수임
function changeLogin(element){
	//선택한 radio의 name 속성의 이름에서 userid 분리 추출함
	var userid = element.name.substring(8);
	console.log("changeLogin :" + userid);
	if(element.checked == true && element.value == "false"){
		//로그인 제한을 체크했다면
		console.log("로그인 제한 체크함");
		location.href = "${ pageContext.servletContext.contextPath}/loginok.do?userid="+ userid + "&login_ok=N";
	}else{
		console.log("로그인 제한 해제함");
		location.href = "${ pageContext.servletContext.contextPath}/loginok.do?userid="+ userid + "&login_ok=Y";
	}
	
}

</script>
</head>
<body>
<c:import url="/WEB-INF/views/common/menubar.jsp"/>
<hr>
<h1 align="center">전체 회원 관리 페이지</h1>
<h2 align="center">현재 회원수 : ${ list.size() } 명</h2>
<center>
	<button onclick="javascript:location.href='${ pageContext.servletContext.contextPath }/mlist.do';">전체 보기</button>
	<br><br>
	<!-- 항목별 검색기능 추가 -->
	<fieldset id="ss">
	<legend>검색할 항목 선택하세요</legend>
	<input type="radio" name="item" id="uid"> 회원 아이디 &nbsp;
	<input type="radio" name="item" id="ugen"> 성별 &nbsp;
	<input type="radio" name="item" id="uage"> 연령대 &nbsp;
	<input type="radio" name="item" id="uenroll"> 가입날짜 &nbsp;
	<input type="radio" name="item" id="ulogok"> 로그인 제한 &nbsp;
	</fieldset>
	<!-- 검색 항목 제공 끝 -->
	<br>
	<!-- 회원 아이디로 검색 폼 -->
	<form action="${ pageContext.servletContext.contextPath }/msearch.do" method="post" id="idform" class="sform">
		<input type="hidden" name="action" value="id">
		<fieldset>
			<legend>검색할 아이디를 입력하세요</legend>
			<input type="search" name="keyword"> &nbsp;
			<button type="summit" value="검색"> 검색
		</fieldset>
	</form>
	<!-- 성별로 검색 폼 -->
	<form action="${ pageContext.servletContext.contextPath }/msearch.do" method="post" id="genderform" class="sform">
		<input type="hidden" name="action" value="gender">
		<fieldset>
			<legend>검색할 성별을 선택하세요</legend>
			<input type="radio" name="keyword" value="M"> 남자 &nbsp;
			<input type="radio" name="keyword" value="F"> 여자 &nbsp;
			<button type="summit" value="검색"> 검색
		</fieldset>
	</form>
	
	<!-- 연령대로 검색 폼 -->
	<form action="${ pageContext.servletContext.contextPath }/msearch.do" method="post" id="ageform" class="sform">
		<input type="hidden" name="action" value="age">
		<fieldset>
			<legend>검색할 연령대를 선택하세요</legend>
			<input type="radio" name="keyword" value="20"> 20대 &nbsp;
			<input type="radio" name="keyword" value="30"> 30대 &nbsp;
			<input type="radio" name="keyword" value="40"> 40대 &nbsp;
			<input type="radio" name="keyword" value="50"> 50대 &nbsp;
			<input type="radio" name="keyword" value="60"> 60대이상 &nbsp;
			<button type="summit" value="검색"> 검색
		</fieldset>
	</form>
	
	<!-- 가입날짜로 검색 폼 -->
	<form action="${ pageContext.servletContext.contextPath }/msearch.do" method="post" id="enrollform" class="sform">
		<input type="hidden" name="action" value="enrolldate">
		<fieldset>
			<legend>검색할 가입날짜를 입력하세요</legend>
			<input type="date" name="begin"> ~ <input type="date" name="end"> &nbsp;
			<button type="summit" value="검색"> 검색
		</fieldset>
	</form>
	
	<!-- 로그인제안 여부로 검색 폼 -->
	<form action="${ pageContext.servletContext.contextPath }/msearch.do" method="post" id="lokform" class="sform">
		<input type="hidden" name="action" value="loginok">
		<fieldset>
			<legend>검색할 로그인 제한/가능을 입력하세요</legend>
			<input type="radio" name="keyword" value="Y"> 로그인 가능 회원 &nbsp;
			<input type="radio" name="keyword" value="N"> 로그인 제한 회원 &nbsp;
			<button type="summit" value="검색"> 검색
		</fieldset>
	</form>
</center>
<br><br>
<!-- 조회해 온 리스트 정보 출력 처리 -->
<table align="center" border="1" cellspacing="0" cellpadding="3">
	<tr>
		<th>아이디</th>
		<th>이름</th>
		<th>성별</th>
		<th>나이</th>
		<th>전화번호</th>
		<th>이메일</th>
		<th>취미</th>
		<th>추가 사항</th>
		<th>가입날짜</th>
		<th>마지막 수정날짜</th>
		<th>로그인 제한여부</th>
	</tr>
	<c:forEach items="${ requestScope.list }" var="m">
	<tr>
		<td>${ m.userid }</td>
		<td>${ m.username }</td>
		<td>${ m.gender eq 'M'? "남자" : "여자" }</td>
		<td>${ m.age }</td>
		<td>${ m.phone }</td>
		<td>${ m.email }</td>
		<td>${ m.hobby }</td>
		<td>${ m.etc }</td>
		<td><fmt:formatDate value="${ m.enroll_date }" type="date" dateStyle="medium"/></td>
		<td><fmt:formatDate value="${ m.lastmodified }" type="date" dateStyle="medium"/></td>
		<td>
			<c:if test="${ m.login_ok eq 'Y' }">
				<input type="radio" name="loginok_${ m.userid }" onchange="changeLogin(this);" value="true" checked> 가능 &nbsp;
				<input type="radio" name="loginok_${ m.userid }" onchange="changeLogin(this);" value="false"> 제한 
			</c:if>
			<c:if test="${ m.login_ok eq 'N' }">
				<input type="radio" name="loginok_${ m.userid }" onchange="changeLogin(this);" value="true"> 가능 &nbsp;
				<input type="radio" name="loginok_${ m.userid }" onchange="changeLogin(this);" value="false" checked> 제한 
			</c:if>
		</td>
	</tr>
	</c:forEach>
</table>
<hr>
<c:import url="/WEB-INF/views/common/footer.jsp"/>
</body>
</html>