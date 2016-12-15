package com.mydomain.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.mydomain.model.service.HealthService;
import com.mydomain.model.service.MemberService;
import com.mydomain.model.service.ProductService;
import com.mydomain.vo.Board;
import com.mydomain.vo.Code;
import com.mydomain.vo.CodePage;
import com.mydomain.vo.Color;
import com.mydomain.vo.ManagerInfo;
import com.mydomain.vo.Member;
import com.mydomain.vo.OperatorBoard;
import com.mydomain.vo.Order;
import com.mydomain.vo.Orderation;
import com.mydomain.vo.Product;
import com.mydomain.vo.ShoppingCart;
import com.mydomain.vo.TColor;
import com.mydomain.vo.dagle;

@Controller
public class HealthController {
	
	//ApplicationContext context=new ClassPathXmlApplicationContext("com/domain/config/applicationContext.xml");
	@Autowired
	HealthService service;
	@Autowired
	ProductService service2;
	@Autowired
	MemberService service3;
	
	/*ProductService service2 = (ProductService) context.getBean("productService");*/
	/*ProductService service2=(ProductService) context.getBean("productService");
	MemberService service3 =(MemberService) context.getBean("MemberService");*/
	private boolean overlapCheck(int productNo,Product product,ProductService service){ //중복확인 메소드
		product = service.getItemByNo(productNo);
		if(product == null){
			return false;
		}
		return true;
	}//value="/ViewList.do"
	
	
	
	@RequestMapping(value="/Board/BoardList",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView getList(String page, ModelMap map, HttpSession session, HttpServletRequest request)
	{
		
		List list2=service.findBoardCode();
	
		 
		List list=service.getBoardList();
		
		session.setAttribute("select",list2);
		
		int pagingno=1;
		
		try{
			pagingno=Integer.parseInt(page);

		}catch(Exception e)
		{}
		
		try{
			Map<String, Object> pagingmap=service.getBoardListPaging(pagingno);
			//System.out.println(pagingmap.get("pageBean").toString());
			return new ModelAndView("Board/BoardList.tiles","pageBean",pagingmap.get("pageBean"));
		}catch(Exception e)
		{
			System.out.println("오류얌");
		}
		
		
		return new ModelAndView("Board/BoardList.tiles");
		
	}
	
	@RequestMapping(value="/Board/insertSuccess",method={RequestMethod.GET ,RequestMethod.POST})
	public String insert(String header, String name, String content, String password, String writer, HttpSession session, HttpServletRequest request)
	{

		Board board=null;
		
		int no=service.selectCount();
		int count=1;

		

		Date date=new Date(System.currentTimeMillis());
		
		board=new Board(no,header,name,content,date,count,password,writer);
		if(board.getBoard_name().equals("")||board.getBoard_header().equals("")||board.getBoard_password().equals("")||board.getBoard_content().equals(""))
		{
			request.setAttribute("errorinsert", "빈값이 존재합니다.");
			System.out.println("칸중에 빈값이 있습니다.");
			return "Board/insert.tiles";
		}else
		{
			System.out.println(board);
			service.insertBoard(board);
		
			session.setAttribute("list", board);
			request.setAttribute("errorinsert", "빈값이 존재합니다.");
			
			return "redirect:insertSuccessre.do";
		}
	
	}
	
	
	@RequestMapping(value="/insertSuccessre",method = {RequestMethod.GET,RequestMethod.POST})
	public String insertSuccessre(HttpSession session, HttpServletRequest request)
	{		
		return "Board/insertSuccess.tiles";
	}
	
	@RequestMapping("/Board/updatereset")
	public String updatereset(HttpSession session)
	{
		System.out.println("이거다");
		session.removeAttribute("dagleupdateerror");
		return "redirect:insertSuccessre.do";
	}
	
	
	@RequestMapping(value="/Board/delete",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView delete(HttpSession session)
	{
		Board board=(Board)session.getAttribute("list");
		
		System.out.println(board);
		int no=board.getBoard_no();
		int count=service.deleteBoard(no);
		
		System.out.println(no);
		System.out.println(count+":");
		
		return new ModelAndView("BoardList.do");
	}
	
	@RequestMapping(value="/Board/update",method={RequestMethod.GET ,RequestMethod.POST})
	public String update(HttpSession session)
	{	
		/*Board board=(Board)session.getAttribute("list");*/
		return "Board/update.tiles";
	}
	
	@RequestMapping(value="/Board/updateSuccess",method={RequestMethod.GET ,RequestMethod.POST})
	public String updateSuccess(HttpSession session, HttpServletRequest request)
	{
		Board board=(Board) session.getAttribute("list");
		
		int no= board.getBoard_no();
		String header=request.getParameter("header");
		String name=request.getParameter("name");
		String content=request.getParameter("content");
		Date date=new Date(System.currentTimeMillis());
		int count=1;
		String password=request.getParameter("password");
		String writer=request.getParameter("writer");
		
		if(header.equals("")||name.equals("")||content.equals("")||password.equals("")||writer.equals(""))
		{
			request.setAttribute("errorupdate", "빈값이 존재합니다.");
			System.out.println("칸중에 빈값이 있습니다.");
			return "Board/update.tiles";
		}
		
		
		board=new Board(no,header,name,content,date,count,password, writer);
		
		int count1=service.updateBoard(board);
		/*System.out.println(board);
		System.out.println(count1);*/
		session.setAttribute("list", board);
		
		return "redirect:updateSuccessre.do";
	}
	
	@RequestMapping(value="/updateSuccessre",method={RequestMethod.GET ,RequestMethod.POST})
	public String updateSuccessre()
	{
		return "Board/updateSuccess.tiles";
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(value="/Board/dagleinsert",method={RequestMethod.GET ,RequestMethod.POST})
	public String dagleinsert(String board_password, String content, String writer, HttpSession session)
	{
		Board board=(Board)session.getAttribute("list");
		System.out.println(board.getBoard_no());
		int no=board.getBoard_no();
		dagle dag=null;
		Date date=new Date(System.currentTimeMillis());
		
		System.out.println(content + writer);
		if(content.equals("")||writer.equals(""))
		{
			session.setAttribute("errorcontent", "내용이 비어있습니다.");
			session.setAttribute("errorwriter", "작성자칸이 비어있습니다.");
			return "Board/insertSuccess.tiles";
		}
		else
		{
		
		session.removeAttribute("errorcontent");
		session.removeAttribute("errorwriter");
		System.out.println(board_password);
		
		dag=new dagle(no,content,date,0,writer,board_password);
		
		
		service.insertdagle(dag);
		List listjoin = service.selectjoin(no);
		session.setAttribute("listjoin", listjoin);
		
		return "redirect:insertSuccessre.do";
		}
	}
	
	
	
	@RequestMapping(value="/Board/insertList",method={RequestMethod.GET ,RequestMethod.POST})
	public String insertList(String page, ModelMap map,HttpSession session,HttpServletRequest request)
	{
	
		Board board=null;
		
		request.setAttribute("list", board);
		Integer no=new Integer(page);

		int no1=no;
		
		List listjoin = service.selectjoin(no1);
		
		
		
		System.out.println(listjoin);
		List list=(List)map.get("BoardList");
		//board=(Board) list.get(board_no);
		
		board=service.selectBoardNo(no);
		//map.addAttribute("select", list2);
		service.updateCount(board);
		
		session.setAttribute("list",board);
		session.setAttribute("listjoin", listjoin);

		session.removeAttribute("errorcontent");
		session.removeAttribute("errorwriter");
		session.removeAttribute("dagleupdateerror");
		session.removeAttribute("error");

		
		return "redirect:insertSuccessre.do";
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(value="/Board/mainBoard",method={RequestMethod.GET ,RequestMethod.POST})
	public String login(HttpSession session)
	{
		
		return "Board/mainBoard.tiles";
	
	}
	
	@RequestMapping(value="/mainguest1",method={RequestMethod.GET ,RequestMethod.POST})
	public String person(HttpSession session)
	{
		session.removeAttribute("managerID");
		session.removeAttribute("managerPW");
		
		return "mainguest1.tiles";
	}
	
	@RequestMapping(value="/mainSuccess",method={RequestMethod.GET ,RequestMethod.POST})
	public String mangerSuccess(@ModelAttribute ManagerInfo member, BindingResult errors, HttpServletRequest request, HttpSession session)
	{
		ManagerInfo member2=null;
		
		List list=service.selectMember();
		
		member2=(ManagerInfo)list.get(0);
		
		String failMessage="";
		String failMessagepw="";
		
		String failMessageidempty="id는 필수 입력사항입니다.";
		request.setAttribute("failMessageidempty", failMessageidempty);
		String failMessagepwempty="pw는 필수 입력사항입니다.";
		request.setAttribute("failMessagepwempty", failMessagepwempty);
		
		if(!(member.getManagerId().equals(member2.getManagerId())))
		{
			failMessage="ID가 다릅니다.";
			request.setAttribute("failMessage", failMessage);
			session.removeAttribute("managerID");
			session.removeAttribute("managerPW");
			
			if(!(member.getManagerPw().equals(member2.getManagerPw())))
			{
				failMessagepw="PW가 다릅니다.";
				request.setAttribute("failMessagepw", failMessagepw);
				session.removeAttribute("managerID");
				session.removeAttribute("managerPW");
				return "/main.tiles";
			}
			return "/main.tiles";
		}
		
		if(!(member.getManagerPw().equals(member2.getManagerPw())))
		{
			failMessagepw="PW가 다릅니다.";
			request.setAttribute("failMessagepw", failMessagepw);
			session.removeAttribute("managerID");
			session.removeAttribute("managerPW");
			if(!(member.getManagerId().equals(member2.getManagerId())))
			{
				failMessage="ID가 다릅니다.";
				request.setAttribute("failMessage", failMessage);
				session.removeAttribute("managerID");
				session.removeAttribute("managerPW");
				return "/main.tiles";
			}
			return "/main.tiles";
		}
		
		
		
		System.out.println("새로운 메인이다.");
		
		
		
		if(member.getManagerId().equals("manager"))
		{
			if(member.getManagerPw().equals("pw"))
			{
				session.setAttribute("managerID", member2.getManagerId());
				session.setAttribute("managerPW", member2.getManagerPw());
				return "Board/mainSuccess.tiles";
			}
			else
			{
				failMessage="입력하신정보가 다릅니다.";
				return "/main.tiles";
			}
		}
		else
		{
			return "/main.tiles";
		}
	}
	
	@RequestMapping(value="/Board/updateajax",method={RequestMethod.GET ,RequestMethod.POST})
	public String updateajax(String updatepassword, HttpSession session, HttpServletRequest request)
	{
		session.removeAttribute("error");
		Board board=(Board)session.getAttribute("list");
		System.out.println("기존 PW는 :" + board.getBoard_password());
	
		/*System.out.println(updatepassword);*/
		
		if(board.getBoard_password().equals(updatepassword))
		{
			session.removeAttribute("error");
			return "Board/update.tiles";
		}else 
		{
			session.setAttribute("error", "비밀번호가 틀렸습니다.");
			System.out.println("비밀번호가 틀렸습니다.");
			return "redirect:insertSuccessre.do";
		}
	}

	
	@RequestMapping(value="/Board/namefind",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView namefind(String Boardnamefind, String page, ModelMap map, HttpSession session, HttpServletRequest request){
		
		List list2=service.findBoardCode();
		//System.out.println(list2);
		System.out.println(page);
		
		System.out.println(Boardnamefind);
		List list=service.selectBoardbyname(Boardnamefind);
		
		request.setAttribute("Boardnamefind", Boardnamefind);
		
		System.out.println(list);
		session.setAttribute("select",list2);
		
		int pagingno=1;
		
		try{
			pagingno=Integer.parseInt(page);

		}catch(Exception e)
		{}
		
		try{
			Map<String, Object> pagingmap=service.getListPaging(pagingno, Boardnamefind);
			session.setAttribute("BoardList", pagingmap.get("list"));
		
			return new ModelAndView("Board/namefind.tiles","pageBean",pagingmap.get("pageBean"));
		}catch(Exception e)
		{
			System.out.println("오류얌");
		}
	
		return new ModelAndView("Board/namefind.tiles");
	}
	
	
	@RequestMapping(value="/Board/passwordfind",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView passwordfind(String Boardpasswordfind, String page, ModelMap map, HttpSession session, HttpServletRequest request ){
		
		
		
		System.out.println(Boardpasswordfind);
		List list2=service.findBoardCode();
		 
		List list=service.selectBoardbypassword(Boardpasswordfind);
		
		session.setAttribute("select",list2);
		
		request.setAttribute("Boardpasswordfind", Boardpasswordfind);
		
		
		int pagingno=1;
		
		try{
			pagingno=Integer.parseInt(page);

		}catch(Exception e)
		{}
		
		try{
			Map<String, Object> pagingmap=service.getListPagingwriter(pagingno, Boardpasswordfind);
			session.setAttribute("BoardList", pagingmap.get("list"));
			return new ModelAndView("Board/passwordfind.tiles","pageBean",pagingmap.get("pageBean"));
		}catch(Exception e)
		{
			System.out.println("오류얌");
		}
		
		return new ModelAndView("Board/passwordfind.tiles");
	}
	
	
	
	
	
	
	//공지사항
	
	
	@RequestMapping(value="/Board/operationBoardList",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView operationBoardList(String page, HttpSession session, HttpServletRequest request){
		 
		List list=service.selectOperatorList();
		
		int pagingno=1;
		
		try{
			pagingno=Integer.parseInt(page);

		}catch(Exception e)
		{}
		
		try{
			Map<String, Object> pagingmap2=service.selectoperatorListPaging(pagingno);
			session.setAttribute("operationBoardList", pagingmap2.get("list"));
			return new ModelAndView("Board/operationBoardList.tiles","pageBeanoperator",pagingmap2.get("pageBean2"));
		}catch(Exception e)
		{
			System.out.println("오류얌");
		}

		return new ModelAndView("Board/operationBoardList.tiles");
	}
	
	@RequestMapping(value="/Board/operationinsertList",method={RequestMethod.GET ,RequestMethod.POST})
	public String operationinsertList(String page, ModelMap map,HttpSession session,HttpServletRequest request)
	{
		OperatorBoard board=null;
		
		request.setAttribute("list", board);
		Integer no=new Integer(page);

		int no1=no;
		
		
		board=service.selectoperatorById(no);
		
		service.operatorCount(board);
		session.setAttribute("list",board);
		return "redirect:operationinsertListre.do";
	}
	
	@RequestMapping("/operationinsertListre")
	public String operationinsertListre()
	{
		return "Board/operationinsertList.tiles";
	}
	
	@RequestMapping(value="/Board/operatorinsertSuccess",method={RequestMethod.GET ,RequestMethod.POST})
	public String operatorinsert(String header, String name, String content, String password, String writer, HttpSession session, HttpServletRequest request)
	{
		
		OperatorBoard board=null;
		
		int no=service.operatorselectCount();
		System.out.println(no);
		int count=1;

		

		Date date=new Date(System.currentTimeMillis());
		
		board=new OperatorBoard(no,header,name,content,date,count,password,writer);
		if(board.getBoard_name().equals("")||board.getBoard_header().equals("")||board.getBoard_password().equals("")||board.getBoard_content().equals(""))
		{
			request.setAttribute("errorinsert", "빈값이 존재합니다.");
			System.out.println("칸중에 빈값이 있습니다.");
			return "Board/operatorinsert.tiles";
		}else
		{
			System.out.println(board);
			service.operatorinsert(board);
		
			session.setAttribute("list", board);

			return "redirect:operatorinsertSuccessre.do";
		}
	}
	
	@RequestMapping(value="/operatorinsertSuccessre",method={RequestMethod.GET ,RequestMethod.POST})
	public String operatorinsertSuccessre()
	{
		return "Board/operatorinsertSuccess.tiles";
	}
	
	
	
	
	@RequestMapping(value="/Board/operatorupdate",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView operatorupdate()
	{	
		return new ModelAndView("Board/operatorupdate.tiles");
	}
	
	@RequestMapping(value="/Board/operatordelete",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView operatordelete(String page, HttpSession session)
	{
		Integer no=new Integer(page);
		
		service.deleteoperatorById(no);
		
		List list = service.selectOperatorList();
		
		session.setAttribute("operationBoardList",list);
		
		return new ModelAndView("Board/operationBoardList.tiles");
	}
	
	@RequestMapping(value="/Board/operatorupdateSuccess",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView operatorupdateSuccess(HttpSession session, HttpServletRequest request)
	{
		OperatorBoard board=(OperatorBoard) session.getAttribute("list");
		
		System.out.println(board);
		int no= board.getBoard_no();
		String header=request.getParameter("header");
		String name=request.getParameter("name");
		String content=request.getParameter("content");
		Date date=new Date(System.currentTimeMillis());
		int count=1;
		String password=request.getParameter("password");
		String writer=request.getParameter("writer");
		
		board=new OperatorBoard(no,"공지사항",name,content,date,count,password, writer);
			
		int count1=service.updateoperatorById(board);
		
		session.setAttribute("list", board);
		
		return new ModelAndView("Board/operatorupdateSuccess.tiles");
	}
	
	
	
	//답변게시판
	@RequestMapping("/Board/QAList")
	public ModelAndView selectQAList(HttpSession session)
	{
		return new ModelAndView("Board/QAList.tiles");
	}
	
	
	@RequestMapping("/Board/QAinsert")
	public ModelAndView insertQA(String page, String QAinsert)
	{
		System.out.println(page);
		System.out.println(QAinsert);
		return new ModelAndView("Board/QAList.tiles");
	}
	
	@RequestMapping("/Board/QAdrong")
	public ModelAndView drong(String count, HttpSession session)
	{
		Integer no=new Integer(count);
		List list3=service.findgrp(no);
		
		session.setAttribute("QAList", list3);
		return new ModelAndView("Board/QAList.tiles"); 
	}
	
	//댓글 삭제
	@RequestMapping(value="/Board/deletedagle",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView dagledelete(String page,String content,HttpSession session)
	{
		Integer no=new Integer(page);
		
		Board board=(Board)session.getAttribute("list");
		
		
		service.dagledelete(content);
		
		List listjoin = service.selectjoin(board.getBoard_no());
		session.setAttribute("listjoin", listjoin);
		
		
		return new ModelAndView("redirect:insertSuccessre.do");
		
	}
	
	@RequestMapping(value="/Board/updatedagle",method={RequestMethod.GET ,RequestMethod.POST})
	public ModelAndView dagleupdate(String pgaeing,String board_password, String updatepassword, String updatecontent,HttpSession session)
	{
		Date date=new Date(System.currentTimeMillis());

		Integer no=new Integer(pgaeing);
		
		if(board_password.equals(updatepassword))
		{
			Board board=(Board)session.getAttribute("list");
			
			dagle dag=new dagle(no,updatecontent,date,0,"수정",updatepassword);
			
			int a=service.dagleupdate(dag);
			System.out.println(a);
			
			List listjoin = service.selectjoin(board.getBoard_no());	
			session.setAttribute("listjoin", listjoin);
		
			return new ModelAndView("redirect:insertSuccessre.do");
		}
		else
		{
			session.setAttribute("dagleupdateerror", "댓글비밀번호가 다릅니다.");
			return new ModelAndView("redirect:insertSuccessre.do");
		}
			
		
		//session.setAttribute("listjoin", listjoin);
		
	}
	
	@RequestMapping(value="/Board/deletedageguest",method={RequestMethod.GET ,RequestMethod.POST})
	public String deletedagleguest(String pgaeingin,String board_passwordre, String updatepassworddelete,HttpSession session )
	{
		System.out.println("들어간다");
		Integer no=new Integer(pgaeingin);
		
		
		if(board_passwordre.equals(updatepassworddelete))
		{
			Board board=(Board)session.getAttribute("list");
			service.dagledeletepassword(updatepassworddelete);
			List listjoin = service.selectjoin(board.getBoard_no());
			session.setAttribute("listjoin", listjoin);
		
			return "redirect:insertSuccessre.do";

		}
		else
		{
			session.setAttribute("dagleupdateerror", "댓글비밀번호가 다릅니다.");
			return "redirect:insertSuccessre.do";
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//// 채환이꺼
	
	

	@RequestMapping(value={"/ViewList.do","/WEB-INF/view/ViewList.do"})
	public ModelAndView ViewItemList(HttpServletRequest request, HttpServletResponse response,String page1,String code,HttpSession session) {
		int page = 1;
		
		try {
			page = Integer.parseInt(request.getParameter("page1"));
		} catch (NumberFormatException e) {
		}
		CodePage codePage = new CodePage(code,page);
		
		Map<String, Object> attributes = service2.list(codePage);
		
		if(attributes.isEmpty()&&attributes.size() == 0){
			System.out.println("error발생");
			return new ModelAndView("/viewItem.do","errorMessage","조회된 값이 없습니다. 물품을 추가해주세요.");
		}
		
		request.setAttribute("result", attributes);
		session.setAttribute("admin", "admin");
		return new ModelAndView("/viewItem.do",attributes);
	}
	
	@RequestMapping("/addItemController.do")
	public ModelAndView addItemController(HttpServletRequest request){
		System.out.println("addItemController");
		List<Code> codeList= service2.selectCodeList();
		System.out.println(codeList);
		List<TColor> colorList = service2.getColor();
		request.setAttribute("colorList", colorList);
		return new ModelAndView("/addItem_form.do","codeList",codeList);
		
	}
	@RequestMapping(value="/addItem.do",method = {RequestMethod.POST,RequestMethod.GET})
	public String addItem(String comment,@RequestParam MultipartFile upImage,
			ModelMap map,HttpServletRequest request,
			String productCategory,String[] color,String[] size,
			String name,String amount,String price,String frame ) throws IllegalStateException, IOException{
		int tAmount = Integer.parseInt(amount);
		int tPrice = Integer.parseInt(price);
		System.out.println(frame);
		
		int productNo = (int) (Math.random() * 9999999) + 1;
		
		/*중복체크*/ 
		Product product = service2.getItemByNo(productNo);
		while (overlapCheck(productNo, product, (ProductService) service2)) {
			productNo = (int) (Math.random() * 10) + 1;
		}

		if(upImage !=null&&!upImage.isEmpty()){
			String fileName = upImage.getOriginalFilename();
		//	String dir = System.getProperty("catalina.home") + "/ect";
			String dir = request.getServletContext().getRealPath("/ect");
			System.out.println(dir);
			File dest = new File(dir, fileName);
			upImage.transferTo(dest);
			
			service2.addItem(new Product(productNo,name,tPrice,productCategory,tAmount,comment,fileName));
			if(color == null){
				service2.addOptionDrunk(new Color(productNo, frame));
			}else if(color !=null&&size == null){
				for(String colorName : color){
					service2.addOptionColor(new Color(colorName, productNo, frame));	
				}
				
			}else if(color !=null&&size !=null){
				for(String colorName : color){	
					for(String sizeName : size){
						service2.addOption(new Color(colorName,productNo,sizeName,frame));
					}
				}	
			}
		
			map.addAttribute("imageName", fileName);
			map.addAttribute("comment",comment);
			}
		
		return "redirect:ViewList.do?code="+productCategory;
		
	}
	@RequestMapping("/ViewOne.do")
	public ModelAndView ViewItemForm(int no){
		Product product = service2.getItemByNo(no);
		List<TColor> colorList = service2.getColor();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("item", product);
		result.put("colorList", colorList);
		return new ModelAndView("/ViewItemPage.do","result",result);
		
	}
	@RequestMapping("/OrderForm.do")
	@ResponseBody
	public ModelAndView ViewOrderForm(int price,@RequestParam(required=false) String memberNumber,@RequestParam(required=false) String phoneEnd,int amount
			,String productName,String code,String productNo,HttpSession session){
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(productNo);
		System.out.println(phoneEnd);
		System.out.println(memberNumber);
 
		int pProductNo= Integer.parseInt(productNo);
		if(code.equals("D")){
			code = "운동기구";
		}else if(code.equals("E")){
			code = "운동복";
		}else if(code.equals("F")){
			code = "영양제";
		}
		System.out.println(code);
		if(memberNumber == ""){
			result.put("price", price);
			 result.put("userAmount", amount);
			 result.put("productNo", productNo);
			 result.put("productName", productName);
			 
			 result.put("category", code);
		 
		 service2.amountUpdate(result);
		 	return new ModelAndView("/Order.do","result",result);
		}
		int mNumber = 0;
		try{
			 mNumber = Integer.parseInt(memberNumber);	
		}catch(Exception e){
			return new ModelAndView("/ViewOne.do?no="+pProductNo,"Oerror","숫자만 입력할수있습니다.");
		}
		
		
		System.out.println("----");
		System.out.println(mNumber);
		Member member  =  service2.getMemberName(mNumber);
		System.out.println(member);
		if(member == null){
			return new ModelAndView("/ViewOne.do?no="+pProductNo,"Oerror","회원번호가 틀렸습니다 다시 입력해주세요.");
		}
		session.setAttribute("member", member);
		price = (int) Math.round(price*0.8);
		System.out.println(price);
		 result.put("price", price);
		 result.put("userAmount", amount);
		 result.put("productNo", productNo);
		 result.put("productName", productName);
		 
		 result.put("category", code);
		 
		 service2.amountUpdate(result);
		//이얍!
		
		 return new ModelAndView("/Order.do","result",result);
	}
	@RequestMapping("/addOrderation.do")
	public ModelAndView addOrderation(String productName,String productAmount,String price,String code,String buyerName,String postalNumber,String orderationAddress,String phone1,String phone2,String phone3,String productNo,@RequestParam(required=false) String memberNo
			,@RequestParam(required = false) String password,HttpSession session
			){
		if(code.equals("운동기구")){
			code = "D";
		}else if(code.equals("운동복")){
			code = "E";
		}else if(code.equals("영양제")){
			code="F";
		}
		
		String orderationNo = code;
		int randomNumber = (int) (Math.random() * 9999) + 1;
		orderationNo = code+":"+randomNumber;
		if(memberNo != null){
			
			password = memberNo;
		}
		System.out.println(orderationNo);
		int tProductNo = Integer.parseInt(productNo);
		int orderationAmount = Integer.parseInt(productAmount);
		int orderationPrice = Integer.parseInt(price);
		
		Orderation orderation = new Orderation(orderationNo,productName,buyerName,phone1,phone2,phone3,orderationAddress,postalNumber,"G",orderationAmount,orderationPrice,service2.getImageName(tProductNo));
		service2.addOrderation(orderation);
		service2.addOrder(new Order(orderationNo,buyerName,orderationAmount,password));
//		Orderation result = service2.getOrderation(orderationNo);
		List<Order> result = service2.getOrder(password);
		System.out.println(code);
		session.setAttribute("code", code);
		session.setAttribute("password", password);
		session.setAttribute("name",result.get(0).getOrdererName());
		return new ModelAndView("redirect:payment.do","code",code);
		
	
	}
	@RequestMapping("/ViewOrder.do")
	public ModelAndView ViewOrder(HttpSession session){
		String password = (String) session.getAttribute("password");
		Member member = (Member) session.getAttribute("member");
		if(password == null&&member==null){
		return new ModelAndView("/passwordFrom.do");
		}
		if(password == null){
			password = Integer.toString(member.getMemberNo());
			List<Order> result = service2.getOrder(password);
			return new ModelAndView("/ViewOrderer.do","order",result);
		}else if(member==null){
			List<Order> result = service2.getOrder(password);
			return new ModelAndView("/ViewOrderer.do","order",result);
		}else if(member != null && password !=null){
			List<Order> result = service2.getOrder(password);
			return new ModelAndView("/ViewOrderer.do","order",result);
		}
		
		return new ModelAndView("/passwordFrom.do");
	}
	
	@RequestMapping("/memberCheck.do")
	public ModelAndView MemberCheck(@RequestParam(required=false) String memberNo,@RequestParam(required=false)String memberPhoneEnd,
			@RequestParam(required=false) String password, @RequestParam(required=false) String orderName,HttpSession session,@RequestParam(required=false) String cart
			){
		System.out.println(memberNo);
		if(memberNo == null&&memberPhoneEnd == null){
			List<Order> list = (List<Order>) service2.getOrderByName(password,orderName);
			if(list.isEmpty()){
				return new ModelAndView("/passwordFrom.do","errorMessage","비밀번호를 잘못입력하셨습니다.");
			}
			session.setAttribute("password", password );
			session.setAttribute("ordername", orderName);
			List<Order> result = service2.getOrder(password);
			if(cart !=null){
				
				return new ModelAndView("/shopping.do");
				
			}
			return new ModelAndView("/ViewOrder.do","order",result);
		}
		int memNo = 0; 
		try{
			memNo= Integer.parseInt(memberNo);	
		}catch (Exception e) {
			// TODO: handle exception
			return new ModelAndView("/passwordFrom.do","errorMessage","숫자만 입력하세요.");
		}
		Member member = service2.getMemberName(memNo);
		if(member == null){
			return new ModelAndView("/passwordFrom.do","errorMessage","비밀번호를 잘못입력하셨습니다.");
		}
		password = Integer.toString(memNo);
	List<Order> result = null;
	try{
	}catch (Exception e) {
		// TODO: handle exception
		return new ModelAndView("/passwordFrom.do","errorMessage1","ee");
	}
	System.out.println(password);
	result = service2.getOrder(password);	
	session.setAttribute("member", member);	
	if(cart !=null){
		
		return new ModelAndView("/shopping.do");
		
	}
	return new ModelAndView("/ViewOrder.do","order",result);
	}

	@RequestMapping("/ViewOrderAdmin")
	public ModelAndView ViewOrderAdmin(@RequestParam(required = false) String error){
		if(error !=null){
			return new ModelAndView("/OrderAdmin.do","errorMessage","에러발생");
		}
		List<Order> list = service2.getOrderList();
		if(list.isEmpty()){
			return new ModelAndView("/OrderAdmin.do","errorMessage","에러발생");
		}
		System.out.println(list);
		return new ModelAndView("/OrderAdmin.do","order",list);
		
	}
	@RequestMapping("/logout.do")
	public String logout(HttpSession session){
		session.invalidate();
		return "/shopping.do";
	}
	@RequestMapping("/updateCode.do")
	public ModelAndView updateCode(String code,@RequestParam(required=false)String[] orderationNo){
		System.out.println(code);
		System.out.println(orderationNo);
	
		if(code.equals("D")){
			service2.removeOrderationCode();
			return new ModelAndView("/ViewOrderAdmin.do");
		}
		if(code.equals("VG")){
			code = "G";
		List<Order> list = service2.getOrderCodeList(code);
			if(list.isEmpty()){
				String error = "에러발생";
				return new ModelAndView("/ViewOrderAdmin.do?error="+error);
			}
			return new ModelAndView("/OrderAdmin.do","order",list);
			
		}
		if(code.equals("VH")){
			code = "H";
			List<Order> list = service2.getOrderCodeList(code);
			if(list.isEmpty()){
				String error = "에러발생";
				return new ModelAndView("/ViewOrderAdmin.do?error="+error);
			}	
			return new ModelAndView("/OrderAdmin.do","order",list);
		}
		if(code.equals("VI")){
			code = "I";
			List<Order> list = service2.getOrderCodeList(code);
			if(list.isEmpty()){
				String error = "에러발생";
				return new ModelAndView("/ViewOrderAdmin.do?error="+error);
			}
			return new ModelAndView("/OrderAdmin.do","order",list);
		}
	
		if(orderationNo == null){
			return new ModelAndView("/ViewOrderAdmin.do");
		}
		for(String no : orderationNo ){
			service2.modifyOrderationCode(code, no);	
		}
		 
		
		return new ModelAndView("/ViewOrderAdmin.do");
	}
	@RequestMapping("/ViewOneOrder.do")
	public ModelAndView ViewOneOrder(String no){
	 Orderation order = service2.getOrderation(no);
	 System.out.println(order);
		return new ModelAndView("/ViewOneOrderPage.do","orderation",order);
	}
	@RequestMapping("/ViewOneOrderAdmin.do")
	public ModelAndView ViewOneOrderAdmin(String no,String name,HttpServletRequest request){
	 Orderation order = service2.getOrderation(no);
	 
	 request.setAttribute("amount",service2.getItemAmount(order.getProductName()));
	 System.out.println(order);
		return new ModelAndView("/ViewOneOrderPage.do","orderation",order);
	}
	@RequestMapping("/updateOrderation.do")
	public ModelAndView updateOrderation(String orderationNo,@RequestParam(required = false) String deleteBtn){
		System.out.println(deleteBtn);
		if(deleteBtn != null){
			service2.removeOrderaion(orderationNo);
			return new ModelAndView("/ViewOrder.do");	
		}
		Orderation order = service2.getOrderation(orderationNo);
		return new ModelAndView("/updateOrderationForm.do","orderation",order);
	}
	@RequestMapping("/updateOrderationClear.do")
	public ModelAndView updateOrderation(String orderationNo,String phone1,String phone2,String phone3,String orderationAddress,String postalNumber){
		service2.modifyOrderation(new Orderation(orderationNo,phone1,phone2,phone3,orderationAddress,postalNumber));
		Orderation order = service2.getOrderation(orderationNo);
		return new ModelAndView("/ViewOneOrderPage.do","orderation",order);
	}
	@RequestMapping("/updateProductForm.do")
	public ModelAndView updateForm(HttpServletRequest request,String tproductNo){
		int productNo = Integer.parseInt(tproductNo);
		
		System.out.println(productNo);
		List<Color> productOptinList = service2.getColorListByNo(productNo);
		List<Code> codeList= service2.selectCodeList();
		System.out.println(codeList);
		List<TColor> colorList = service2.getColor();
		request.setAttribute("colorList", colorList);
		request.setAttribute("item",service2.getItemByNo(productNo) );
		request.setAttribute("productColor", productOptinList);
		return new ModelAndView("/updateProductPage.do","codeList",codeList);
	}
	@RequestMapping("updateItem.do")
	public String updateProduct(String comment,@RequestParam MultipartFile upImage,
			ModelMap map,HttpServletRequest request,
			String productCategory,String[] color,String[] size,
			String name,String amount,String price,String frame,String tProductNo) throws IllegalStateException, IOException{
		int productNo = Integer.parseInt(tProductNo);
		int tAmount = Integer.parseInt(amount);
		int tPrice = Integer.parseInt(price);
		if(upImage !=null&&!upImage.isEmpty()){
			String fileName = upImage.getOriginalFilename();
		//	String dir = System.getProperty("catalina.home") + "/ect";
			String dir = request.getServletContext().getRealPath("/ect");
			System.out.println(dir);
			File dest = new File(dir, fileName);
			upImage.transferTo(dest);
			
			service2.modifyItem(new Product(productNo,name,tPrice,productCategory,tAmount,comment,fileName));
			if(color == null){
				service2.modifyOptionDrunk(new Color(productNo, frame));
			}else if(color !=null&&size == null){
				for(String colorName : color){
					service2.modifyOptionColor(new Color(colorName, productNo, frame));	
				}
				
			}else if(color !=null&&size !=null){
				for(String colorName : color){	
					for(String sizeName : size){
						service2.modifyOption(new Color(colorName,productNo,sizeName,frame));
					}
				}	
			}
		
			map.addAttribute("imageName", fileName);
			map.addAttribute("comment",comment);
			}
		
		return "redirect:ViewList.do?code="+productCategory;
		
		
	}
	@RequestMapping("/deleteProduct.do")
	public String deleteProduct(String tProductNo,String code){
		int productNo = Integer.parseInt(tProductNo);
		service2.removeOption(productNo);
		service2.removeItem(productNo);
		
		
		return "redirect:ViewList.do?code="+code;
		
	}
	@RequestMapping("/addShoppingCart.do")
	public ModelAndView addShoppingCart(String memberNo,String productNo, String productName,HttpSession session,HttpServletRequest request){
		String name = (String) session.getAttribute("ordername");
		String password = (String) session.getAttribute("password");
		Member member = (Member) session.getAttribute("member");
		int tProductNo = Integer.parseInt(productNo);
		System.out.println(member);
		if(name == null && password == null){
			if(member == null){
				
				return new ModelAndView("/passwordFrom.do","cart","cart");
				
			}
			password = Integer.toString(member.getMemberNo());
			System.out.println(member.getMemberName());
			
			if(service2.getCartCount(password, member.getMemberName()) == 0){
				
				service2.addCart(new ShoppingCart(tProductNo,productName,password,member.getMemberName()));	
				
				
			}else{
				
				service2.addCart(new ShoppingCart(tProductNo,productName,password,member.getMemberName()));
			}
			Product product = service2.getItemByNo(tProductNo);
			List<TColor> colorList = service2.getColor();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("item", product);
			result.put("colorList", colorList);
			request.setAttribute("message", productName+"을 장바구니에 담았습니다.");
			return new ModelAndView("/ViewItemPage.do","result",result);
		}
		if(service2.getCartCount(password, name) == 0){
			service2.addCart(new ShoppingCart(tProductNo, productName, password, name));		
		}else{
			service2.addCart(new ShoppingCart(tProductNo, productName, password, name));
		}
		Product product = service2.getItemByNo(tProductNo);
		List<TColor> colorList = service2.getColor();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("item", product);
		result.put("colorList", colorList);
		request.setAttribute("message", productName+"을 장바구니에 담았습니다.");
		return new ModelAndView("/ViewItemPage.do","result",result);
	}
	@RequestMapping("/ViewCart.do")
	public ModelAndView joinPassword(HttpServletRequest request,HttpSession session){
		Member member =(Member) session.getAttribute("member");
		String name = (String) session.getAttribute("ordername");
		String password = (String) session.getAttribute("password");
		System.out.println(member);
		if(name ==null &&password == null&& member == null){
		return new ModelAndView("/passwordFrom.do","cart","cart");
		}
		if(name ==null&&password == null){
			String memberNo = Integer.toString(member.getMemberNo());
			List<ShoppingCart> cart = service2.getJoinShoppingList(member.getMemberName(),memberNo);
			for(ShoppingCart carts : cart){
				System.out.println(carts);
			}
			return new ModelAndView( "/ViewCartList.do","cart", cart);
		}
		List<ShoppingCart> cart = service2.getJoinShoppingList(name,password);
		System.out.println(cart.size());
		for(ShoppingCart carts : cart){
			System.out.println(carts);
		}
		return new ModelAndView( "/ViewCartList.do","cart", cart);
	}
	@RequestMapping("/deleteCart")
	public String deleteCart(String[] productNo,String password){
		
		
		int tProductNo = 0;
		System.out.println(productNo);
		for(String no : productNo){
			
			tProductNo = Integer.parseInt(no);
			System.out.println();
			service2.removeCart(password, tProductNo);
			
		}
		
		
		return "/ViewCart.do";
	}
}










