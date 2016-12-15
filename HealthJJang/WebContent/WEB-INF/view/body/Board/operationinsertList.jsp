<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
#operatorSuccess
{
	width:100% ;
	max-width:1550px ; 
	margin:0 auto ; 
	text-align: center;
}

body
{
 	font-family: "맑은 고딕";
 	margin:0px;
 	padding:0px;
 	font-size:18px;
}

#date{
	text-align: right;
}

</style>


</head>
<script type="text/javascript" src="/HealthJJang/scripts/jquery.js"></script>

<body>
	<jsp:include page="/WEB-INF/view/body/boardmenu.jsp"/>
	<table id="operatorSuccess">
		<tr height=50>
			<td>
				<h1>[${sessionScope.list.board_header }] 제목 : ${sessionScope.list.board_name } 작성자 : ${sessionScope.list.board_writer } </h1>
			</td>
		</tr>	
		<tr>
			<td width=500 height=50 id="date">
				작성일시 : <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${sessionScope.list.board_date}"/>  조회수 : ${sessionScope.list.board_count}
			</td>
		</tr>
		<tr>
			<td width=500 height=400>
				<h1>${sessionScope.list.board_content}</h1>
			</td>
		</tr>
	</table>
		<br>
		
		 
		<c:if test="${not empty sessionScope.managerID}">
			<c:if test="${not empty sessionScope.managerPW}">
			 	<a href="operatorupdate.do"><button id="update">수정</button></a>
				 <a href="operatordelete.do?page=${sessionScope.list.board_no}"><button id="delete">삭제</button></a>
			</c:if>
		</c:if>
		
		 
		

</body>
</html>