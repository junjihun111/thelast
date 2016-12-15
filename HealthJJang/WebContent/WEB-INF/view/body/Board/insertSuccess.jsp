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

#tableSuccess{
	width:70% ;
	max-width:1550px ; 
	margin:0 auto ; 
	text-align: left;
}

#dagle{
	margin-left: 55%;
}

#daglereal{
	margin-left: 72%;
	text-align: left;
}

#updatebox
{
	margin-left: 56%;

}



</style>

</head>
<script type="text/javascript" src="/HealthJJang/scripts/jquery.js"></script>
<body>
	<jsp:include page="/WEB-INF/view/body/boardmenu.jsp"/>
	<table border=1 id="tableSuccess">
		<tr height=50>
			<td>
				${sessionScope.list.board_no} . [${sessionScope.list.board_header }] ${sessionScope.list.board_name } 작성자 : ${sessionScope.list.board_writer }
			</td>
		</tr>	
		<tr>
			<td width=600 height=50>
				작성일시 : <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${sessionScope.list.board_date}"/>  조회수 : ${sessionScope.list.board_count}
			</td>
		</tr>
		<tr>
			<td width=600 height=100>
				${sessionScope.list.board_content}
			</td>
		</tr>
	</table>	
	
	<!-- <table>
		<tr>
			<th colspan="2"><h2>댓글란</h2></th>
		</tr>
	

		<tr>
			<td width=100 height=10>순번</td>
			<td width=500 height=10>내용</td>
		</tr>
	
	</table> -->
	
	
		<table id="dagle">
		<c:forEach items="${sessionScope.listjoin}" var="board" varStatus="cnt">
			<c:forEach items="${board.daglelist}" var="dagle" varStatus="cnt">
	
			<c:if test="${not empty board.daglelist}">
			<tr>
				<td width=500 height=40 colspan="2" >
					&nbsp;&nbsp; └ ${dagle.content}<span id="updatedaglelist${cnt.count}">
						<button id="board_update${cnt.count}">수정</button>
					</span>	
					<span id="deletedaglelist${cnt.count}"> <button id="board_delete${cnt.count}">삭제</button> </span>
					
					<c:if test="${not empty sessionScope.managerID}">
						<c:if test="${not empty sessionScope.managerPW}">
							<a href="deletedagle.do?page=${cnt.count}&content=${dagle.content}"><input type="button" name="delete" id="delete" value="관리자삭제"><input type="hidden" name="content"></a>
						</c:if>
					</c:if>
				
				</td>
				
			</tr>
			</c:if>
			</c:forEach>
			
		</c:forEach>		
		</table>
	
	<c:if test="${not empty sessionScope.dagleupdateerror}">
		${sessionScope.dagleupdateerror}
	</c:if>	
	<br>
		
	<form action="dagleinsert.do">
		<table id="daglereal" border="1">
			<tr>
				<td colspan="2">
					댓글 : <input type="text" id="content" name="content" value="댓글"><br><br>
					작성자 : <input type="text" id="writer" name="writer" value="작성자"><br><br>
				 	비밀번호 : <input type="password" id="board_password" name="board_password" value="비밀번호">
				 <c:choose>
					<c:when test="${not empty sessionScope.errorcontent }">
						<br>${sessionScope.errorcontent}
					</c:when>
					
					<c:when test="${not empty sessionScope.writer }">
						<br>${sessionScope.writer}
					</c:when>
				</c:choose>
				<br>
					<span id="dagleinsert"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="등록" ></span>
				</td>	
			</tr>
		</table>
	</form>
	
		<br>
	
		 <div id="updatebox">
		 	<button id="update">글 수정</button>
		 	<%-- <c:if test="${empty sessionScope.error }">
		 		${sessionScope.error}
		 	</c:if> --%>
		 	
		 	<button id="delete">글 삭제</button>
		 	<br>
		 	<c:if test="${not empty sessionScope.error}">
		 		${sessionScope.error}
		 	</c:if>
		 </div>
		 	
		 <br><br>
		 
		
		
		<!-- spring-mvc 경로 잘못될 가능성 존재.-->
		<script type="text/javascript">
			$(document).ready(function(){
				
				$("#update").on("click",function(){
					
				var txt ="<form action=updateajax.do name=updateBoxForm method=GET> 비밀번호 :<input type=password name=updatepassword value=수정하기위한 비밀번호> <a href=updateajax.do><button id=updateBoxForm>수정</button></a> <input type=reset value=초기화> </form>";
					
					$("#updatebox").html(txt);
					
				});
			
				
				$("#delete").on("click",function(){
					var txt ="<form action=delete.do name=deleteForm method=GET> 비밀번호 : <input type=password name=updatepassword value=삭제하기위한 비밀번호> <a href=delete.do><button id=deleteBoxForm>수정</button></a> <input type=reset value=초기화> </form>";
					$("#updatebox").html(txt);	
				});
				
				
				$("#content").on("click",function(){
					$("#content").val("");
				});
				
				$("#writer").on("click",function(){
					$("#writer").val("");
				});
				
				$("#board_password").on("click",function(){
					$("#board_password").val("");
				});

			<c:forEach items="${sessionScope.listjoin}" var="board" varStatus="cnt">
					<c:forEach items="${board.daglelist}" var="dagle" varStatus="cnt">
				$("#board_update${cnt.count}").on("click",function(){
					var txt = "<form action= updatedagle.do name=updateBoxFormlist method=GET>"
								+"비밀번호: <input type=password name=updatepassword id=updatepassword><br>" 
								+"수정내용 : <input type=text name=updatecontent id=updatecontent><br>"
								+"<a href=updatedagle.do?pgaeing=${cnt.count}&board_password=${dagle.board_password}><button id=updatedagle>수정</button></a>&nbsp;&nbsp;&nbsp;"
								+"<input type=hidden name=pgaeing value=${cnt.count}>"+"<input type=hidden name=board_password value=${dagle.board_password}>"
								/* +"<input type=hidden name=updatepassword value=$(#updatepassword).val()>";
								+"<input type=hidden name=updatecontent value=$(#updatecontent).val()>"
									 */
								+"</form>"+"<a href=updatereset.do><button id=reset${cnt.count}>새로고침(작업취소)</button></a>";
								
										
					$("#updatedaglelist${cnt.count}").html(txt);
					
				});
				
				
				$("#board_delete${cnt.count}").on("click",function(){
					var txtli = "<form action = deletedageguest.do>"
							+"비밀번호: <input type=password name=updatepassworddelete id=updatepassworddelete><br>" 
							+"<a href=deletedageguest.do?pgaeingin=${cnt.count}&board_passwordre=${dagle.board_password}><button id=deletedaglegu>삭제</button></a>&nbsp;&nbsp;&nbsp;"
							+"<input type=hidden name=pgaeingin value=${cnt.count}>"+"<input type=hidden name=board_passwordre value=${dagle.board_password}>"
							+"</form>";
						
					$("#deletedaglelist${cnt.count}").html(txtli);
				});

				
				</c:forEach>
			</c:forEach>
				
		});
		</script>
</body>
</html>