<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
<style type="text/css">
table {
	width: 100%;
	border-collapse: collapse;
	text-align: center;
	font-family: 'Trebuchet MS';
}

td, th {
	text-align: center;
	font-size: 10pt;
	border: 1px solid #231EB7;
	height: 30px;
	padding: 5px;
}

th {
	background-color: #5B5AFF;
	color: #ffffff;
	font-family: Georgia;
}
</style>
<script type="text/javascript"
	src="/health_member_manager/scripts/jquery.js"></script>
<script type="text/javascript">
history.pushState(null, null, location.href); 
window.onpopstate = function(event) { 
history.go(1); 
}



	$(document).ready(function() {
		
		$("#attendChoice").on("click", function(){
			$("tr td:nth-child(1)").append("<input type='checkbox'  name='box'>");
		});
		
		$("#checkedDelete").click(function() {
			var ids = "";
			var dates = "";
			//alert($("input[name=box]:checked").length);
			$("input[name=box]:checked").each(function(idx) {
				if(idx==$("input[name=box]:checked").length-1){
					 //ids+="id="+$(this).parent().text();
					 dates+="attendanceDates="+$(this).parent().nextAll(":eq(1)").text();
				}else{
					//ids+="id="+$(this).parent().text()+"&";
					 dates+="attendanceDates="+$(this).parent().nextAll(":eq(1)").text()+"&";
				}
				ids="id="+$(this).parent().text();
			});
			var queryString = ids+"&"+dates;

			  $.ajax({
				 "url":"/health_member_manager/health/deleteByAttend.do",
				 "data":queryString,
				 "dataType":"json",
				"type":"post",
				"success":function(obj){
					var txt="";
					$.each(obj,function(){
						txt= txt+"<tr><td>"
						+this.memberNo+"</td><td>"
						+this.memberName+"</td><td>"
						+this.attendanceDate+"</td><td></td>"
					})
					$("#listBody").html(txt);
				},
				error:function(xhr, msg, msg2){
					alert("오류발생"+msg2);
				},
	 			"beforeSend":function(){
	 				if(dates==0){
	 					alert("삭제할 출석내역을 선택한 후 삭제 해주세요");
	 					return false;
	 				}
	 			}
			 }) 
		});
		
	});
</script>
</head>
<body>
	<br>
	<br>
	<hr>
	
	

	출석수 : ${requestScope.attendCount }
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	
	<a href="/health_member_manager/health/endedMember.do?page=${sessionScope.pageBean.page}"><button>뒤로가기</button></a>
	<button id="attendChoice">출석선택</button>
	<button id="checkedDelete">출석내역삭제</button>

	<table border="1">
		<tr>
			<th>회원번호
			<th>회원이름
			<th>출석내역
			<th>환불금액 
			<tbody id="listBody">
			<c:forEach items="${requestScope.attendList}" var="attend">
					<tr>
						<td>${attend.memberNo}</td>
						<td>${attend.memberName}</td>
						<td><fmt:formatDate value="${attend.attendanceDate}" type="date" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td>
				</c:forEach>
				</tbody>
	</table>
</body>
</html>