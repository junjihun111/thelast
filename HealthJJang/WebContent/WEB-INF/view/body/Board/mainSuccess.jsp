<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">

#Success
{
	font-family: "맑은 고딕";
 	margin:0px;
 	padding:0px;
 	font-size:18px;
}
</style>
</head>

<body>


<c:if test="${sessionScope.managerID !=null}">
	<c:if test="${sessionScope.managerPW != null}">
 	</c:if>
</c:if> 
<br>
<br><br><br>
<span id="Success"><h1>관리자 메인화면</h1></span>

<br><br><br>

</body>
</html>