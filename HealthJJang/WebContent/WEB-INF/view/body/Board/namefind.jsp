<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">

header{
 font-family: "맑은 고딕";

}

body{
 font-family: "맑은 고딕";
 margin:0px;
 padding:0px;
 font-size:20px;
}

#bbs td {
    padding-top: 3px;
    padding-bottom: 3px;
    border-bottom: 1px solid silver;
}

#bbs td a {
    color: #555;
    text-decoration: none;
}

#bbs td a:hover {
    color:#555;
    text-decoration: underline;
} 

#tablelist {
	width:100% ;
	max-width:1550px ; 
	margin:0 auto ; 
}

#list{
	width:100% ;
	max-width:1550px ; 
	margin:0 auto ; 
	text-align: center;
	
}

#insert{
	margin-left: 35%;
	
}

#paging{
}

#namefind{
	margin-left: 60%;
}

#passwordfind{
	margin-left: 60%;
}

</style>
</head>
<body>
<jsp:include page="/WEB-INF/view/body/boardmenu.jsp"/>
<br>
	이름으로 조회.

<table border=2 id="tablelist">
	<thead>
			<tr>
				<td width=300 height=100><span class="bbs"><h2>번호</h2></span></td>
				<td width=300 height=100><span class="bbs"><h2>제목</h2></span></td>
				<td width=300 height=100><span class="bbs"><h2>공지일</h2></span></td>
				<td width=300 height=100><span class="bbs"><h2>조회수</h2></span></td>
				<td width=300 height=100><span class="bb"><h2>작성자</h2></span></td>
			</tr>
	</thead>
	<tbody>
		<c:forEach items="${sessionScope.BoardList}" var="board" varStatus="cnt">
		<tr>
			<td><h3>${board.board_no}</h3> </td>
			<td><h3><a href="insertList.do?page=${board.board_no }">${board.board_name}
			</a></h3></td>
			<td><h3><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${board.board_date}"/></h3></td>
			<td><h3>${board.board_count}</h3></td>
			<td><h3>${board.board_writer}</h3></td>
		</tr>
	</c:forEach>
	</tbody>
</table>
	<span id="insert"><a href='operatorinsert.do'><button id="insert">글 등록</button></a></span>&nbsp;&nbsp;
	<!-- 관리자 전용메뉴 <a href='Board/delete.do'>글 삭제</a>&nbsp;&nbsp; -->
</body>


<p>
	<!-- 첫페이지로 이동 -->
	<a href="namefind.do?page=1&Boardnamefind=${requestScope.Boardnamefind}">
		◁&nbsp;&nbsp;
	</a>
	<!--
		이전 페이지 그룹 처리.
		만약에 이전페이지 그룹이 있으면 링크처리하고 없으면 화살표만 나오도록 처리.
	 -->
 <c:choose>
 	<c:when test="${requestScope.pageBean.previousPageGroup }">
 		<a href="namefind.do?page=${requestScope.pageBean.beginPage-1 }&Boardnamefind=${requestScope.Boardnamefind}">
 			◀
 		</a>	
 		&nbsp;&nbsp;
 	</c:when>
 	<c:otherwise>
		◀&nbsp;&nbsp;
 	</c:otherwise>
 </c:choose>
<!-- 
	현재 page가 속한 page 그룹내의 페이지들 링크.
	현재 pageGroup의 시작page ~ 끝 page
 -->
 <!-- 만약에 p가 현재페이지면 링크처리를 하지 않고 p가 현재페이지가 아니라면 링크처리. -->
<c:forEach begin="${requestScope.pageBean.beginPage }" end="${requestScope.pageBean.endPage }"
			   var="p">
	<c:choose>
		<c:when test="${p != requestScope.pageBean.page }">
			<a href="namefind.do?page=${p }&Boardnamefind=${requestScope.Boardnamefind}">
				${p }
			</a>
			&nbsp;&nbsp;
		</c:when>
		<c:otherwise>
			[${p}]&nbsp;&nbsp;
		</c:otherwise>
	</c:choose>
</c:forEach>
<!-- 
	다음페이지 그룹으로 이동
	만약에 다음페이지 그룹이 있으면 링크 처리 없으면 화살표만 나오도록 처리
 -->
<c:choose>
	<c:when test="${requestScope.pageBean.nextPageGroup }">
		<a href="namefind.do?page=${requestScope.pageBean.endPage+1 }&Boardnamefind=${requestScope.Boardnamefind}">
			▶&nbsp;&nbsp;		
		</a>
	</c:when>
	<c:otherwise>
		▶&nbsp;&nbsp;
	</c:otherwise>
</c:choose>

<!-- 마지막 페이지로 이동 -->
<a href='namefind.do?page=${requestScope.pageBean.totalPage}&Boardnamefind=${requestScope.Boardnamefind}'>
	▷
</a>	
</p>
</html>	
