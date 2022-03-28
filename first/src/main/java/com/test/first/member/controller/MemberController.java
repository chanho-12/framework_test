package com.test.first.member.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.test.first.common.SearchDate;
import com.test.first.member.model.service.MemberService;
import com.test.first.member.model.vo.Member;

@Controller
public class MemberController {
   // 이 클래스의 메소드 안에서 로그 출력을 원하면 로그 객체 생성
   // src/main/resources/log4j.xml 에 설정된 내용으로 출력됨
   private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
 // 의존성 주입 ( DI : Dependency Injection )
   // 클래스명 레퍼런스변수 = new 생성자(); => 자동 처리함
   @Autowired
   private MemberService memberService;

   @Autowired
   private BCryptPasswordEncoder bcryptPasswordEncoder;
   // 뷰페이지 이동 처리용 메소드 ------------------------

   @RequestMapping("loginPage.do")
   public String moveLoginPage() {
      return "member/loginPage";
   }

   @RequestMapping("enrollPage.do")
   public String moveEnrollPage() {
      return "member/enrollPage";
   }

   // ---------------------------------------------------------

   // 로그인 처리용 메소드
   // 전송방식은 포스트 방식으로,설정안하면 기본 GET방식임
//   @RequestMapping(value = "login.do", method = RequestMethod.POST)
//   public String loginMethod(HttpServletRequest request, HttpServletResponse response, Model model) {
//      // 서비스로 전달할 멤버객체 생성
//      Member member = new Member();
//      member.setUserid(request.getParameter("userid"));
//      member.setUserpwd(request.getParameter("userpwd"));
//
//      Member loginMember = memberService.selectLogin(member);
//      String viewName = null;
//      if (loginMember != null) {
//         // 로그인 상태관리 방법 : 상태관리 메커니즘 이라고 함
//         // 1.세션 이용
//         // 2.쿠키 이용(보안문제)
//         // 3.URL 제작성 이용(보안문제)
//         // 1번방식을 기본으로 사용함
//         HttpSession session = request.getSession();
//         // getSession() == getSession(true)
//         // 세션객체가 없으면 자동으로 생성시킴
//         // 세션객체가 있으면 해당 세션의 정보를 리턴받음
//         // 세션객체안에 loginMember이라는 이름으로 loginMember 객체가 저장되게함
//         session.setAttribute("loginMember", loginMember);
//         viewName = "common/main";
//      } else { // 로그인 실패시
//         model.addAttribute("message", "로그인 실패 : 아이디나 패스워드 확인하세요.");
//         viewName = "common/error";
//      }
//      return viewName;
//   }
   // 부모인 MemberService 으로 써라
   // 다형성 적용한 동적 바인딩
  @RequestMapping(value="login.do", method=RequestMethod.POST)
  public String loginMethod(Member member, HttpSession session, SessionStatus status, Model model) {
	  logger.info("login.do : " + member);
	  
	  //암호화 처리된 패스워드 일치 조회는 select 해온 값으로 비교해야함
	  //회원 아이디로 회원 정보를 먼저 조회함
	  Member loginMember = memberService.selectMember(member.getUserid());
	  
	  //암호화된 패스워드와 전달된 패스워드가 일치하는지 확인 : matches(일반글자패스워드 , 암호화된 패스워드)  
	  if(loginMember != null && bcryptPasswordEncoder.matches(member.getUserpwd(), loginMember.getUserpwd())
			  				&& loginMember.getLogin_ok().equals("Y")) {
		  //세션 객체 생성 > 세션 안에 회원정보 저장
		  session.setAttribute("loginMember", loginMember);
		  status.setComplete(); // 로그인 요청 성공, 200번 전송됨
		  return "common/main";
	  }else {
		  model.addAttribute("message", "로그인 실패 / 아이디 암호 확인");
		  return "common/error";
	  }
	  
  }

   // 로그아웃 처리용
   // 가짜이름 등록하는것이 requestMapping
   // 실제구동은 logoutmethod임
   @RequestMapping("logout.do")
   public String logoutMethod(HttpServletRequest request, Model model) {
      // 로그인할때 생성한 세션객체를 없애는것이 로그아웃!
      // 먼저 세션객체가 있는지 없는지 파악해야
      HttpSession session = request.getSession(false);
      // 세션객체가 있으면 리턴받음
      // 세션객체가 없으면 null 리턴
      if (session != null) {
         session.invalidate(); // 해당 세션객체 없앰
         return "common/main";
      } else {
         model.addAttribute("message", "로그인 세션이 존재하지 않습니다.");
         return "common/error";
      }
   }

   // 회원 가입 처리용
   @RequestMapping(value = "enroll.do", method = RequestMethod.POST)
   public String memberInsertMethod(Member member, Model model) {
      // 메소드 매개변수에 vo 에 대한 객체를 작성하면,
      // 뷰 form 태그 input 의 name 과 vo 의 필드명이 같으면
      // 자동 값이 꺼내져서 객체에 옮겨 기록 저장됨
      // = 커맨드 객체(command object)
      
      logger.info("enroll.do : " + member);
      
      //패스워드 암호화 처리
      member.setUserpwd(bcryptPasswordEncoder.encode(member.getUserpwd()));
      logger.info("after encode : " + member);

      if (memberService.insertMember(member) > 0) {
         return "common/main";
      } else {
         model.addAttribute("message", "회원 가입 실패");
         return "common/error";
      }
   }
   
   @RequestMapping("myinfo.do")
   public ModelAndView myInfoMethod(@RequestParam("userid") String userid, ModelAndView mv) {
	   Member member = memberService.selectMember(userid);
	   
	   if(member != null) { //조회가 성공했다면
		   mv.addObject("member", member);
		   mv.setViewName("member/myInfoPage");
	   }else { //조회가 실패했다면
		   mv.addObject("mesaage", userid + " : 회원 정보 조회 실패!");
		   mv.setViewName("common/error");
		   
	   }
	   return mv;
   }
   
   @RequestMapping(value="mupdate.do", method=RequestMethod.POST)
	public String memberUpdateMethod(Member member, Model model, 
			@RequestParam("origin_userpwd") String originUserpwd) {
		logger.info("mupdate.do : " + member);
		logger.info("origin_userpwd : " + originUserpwd);
		
		//새로운 암호가 전송이 왔다면
		String userpwd = member.getUserpwd().trim();
		if(userpwd != null && userpwd.length() > 0) {
			//기존 암호와 다른 값이면
			if(!bcryptPasswordEncoder.matches(userpwd, originUserpwd)) {
				//멤버에 새로운 암호를 저장 : 암호화처리
				member.setUserpwd(bcryptPasswordEncoder.encode(userpwd));
			}
		}else {
			//새로운 암호가 없다면, 원래 암호 기록함
			member.setUserpwd(originUserpwd);
		}
		
		logger.info("after : " + member);
		
		if(memberService.updateMember(member) > 0) {
			//수정이 성공했다면, 컨트롤러의 메소드를 직접 호출(실행)할 수도 있음
			//내정보보기 페이지에 수정된 회원정보를 다시 조회해 와서 출력되게 처리
			return "redirect:myinfo.do?userid=" + member.getUserid();
		}else {
			model.addAttribute("message", "회원 정보 수정 실패!");
			return "common/error";
		}
	}
   
   
   //회원 탈퇴 처리(삭제처리)
   @RequestMapping("mdel.do")
   public String memberDeleteMethod(@RequestParam("userid") String userid, Model model) {
	   if(memberService.deleteMember(userid)>0) {  
		   return "redirect:logout.do";
	   }else {
		   model.addAttribute("message", userid + " 회원 삭제 실패");
		   return "common/error";
	   }
   }
   //아이디 중복 확인 체크 ajax 통신 요청 처리용
   //ajax 통신은 뷰리졸버로 뷰파일을 리턴하면 안됨(뷰페이지가 바뀜)
   //요청한 클라이언트와 출력스트림을 만들어서 통신하는 방식으로 값을 리턴함
   @RequestMapping(value="idchk.do", method=RequestMethod.POST)
   public void dupIdCheckMethod(@RequestParam("userid") String userid,
		   HttpServletResponse response) throws IOException{
	   int idcount = memberService.selectDupCheckId(userid);
	   
	   String returnValue = null;
	   if(idcount ==0) {
		   returnValue ="ok";
	   }else {
		   returnValue ="dup";
	   }
	   //response 를 이용해서 클라이언트로 출력스트림 만들고 값 보내기
	   response.setContentType("text/html; charset=utf-8");
	   PrintWriter out = response.getWriter();
	   out.append(returnValue);
	   out.flush();
	   out.close();
   }
   
   //관리자 회원관리 기능 처리용
   @RequestMapping("mlist.do")
   public String memberListViewMethod(Model model) {
	   ArrayList<Member> list = memberService.selectList();	
	   
	   if(list.size() > 0) {
		   model.addAttribute("list", list);
		   return "member/memberListView";
	   }else {
		   model.addAttribute("mesaage", "회원 목록이 존재하지 않음.");
		   return "common/error";
	   }
   }
   
   //로그인 제한/가능 변경 처리용
   @RequestMapping("loginok.do")
   public String changeLoginOKMethod(Member member, Model model) {
	   logger.info("loginok.do :" + member.getUserid() + ", " + member.getLogin_ok());
	   
	   if(memberService.updateLoginOk(member) > 0) {
		   return "redirect:mlist.do";
	   }else {
		   model.addAttribute("message", "로그인 제한/허용 처리 오류 발생");
		   return "common.error";
	   }
   }
   
   //회원 검색 기능 처리용
   @RequestMapping(value="msearch.do", method=RequestMethod.POST)
   public String memberSearchMethod(HttpServletRequest request, Model model) {
	   String action = request.getParameter("action");
	   String keyword = null, beginDate = null, endDate = null;
	   
	   if(action.equals("enrolldate")) {
		   beginDate = request.getParameter("begin");
		   endDate = request.getParameter("end");
	   }else {
		   keyword = request.getParameter("keyword");
	   }
	   
	   
	   //서비스 메소드 리턴값 받을 리스트 준비
	   ArrayList<Member> list = null;
	   
	   switch(action) {
	   case "id": 	list = memberService.selectSearchUserid(keyword);
	   				break;
	   case "gender":list = memberService.selectSearchGender(keyword);
	   				break;
	   case "age":	list = memberService.selectSearchAge(Integer.parseInt(keyword));
	   				break;
	   case "enrolldate": list = memberService.selectSearchEnrollDate(new SearchDate(Date.valueOf(beginDate), Date.valueOf(endDate)));
			   			
	   				break;
	   case "loginok": 	list = memberService.selectSearchLoginOK(keyword);
	   				break;
		   
	   }
	   
	   if(list.size() > 0) {
		   model.addAttribute("list", list);
		   return "member/memberListView";
	   }else {
		   model.addAttribute("message", action + "검색에 대한" + keyword + "결과가 존재하지않습니다.");
		  return "common/error";
	   }
   }
   

   
   
}