package com.test.first.notice.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.test.first.member.model.vo.Member;
import com.test.first.notice.model.service.NoticeService;
import com.test.first.notice.model.vo.Notice;

@Controller
public class NoticeController {
	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Autowired
	private NoticeService noticeService;
	
	@RequestMapping(value="ntop3.do", method=RequestMethod.POST)
	@ResponseBody
	public String noticeNewTop3Method(HttpServletResponse response) throws UnsupportedEncodingException {
		//최근 등록 공지글 3개 조회해 옴
		ArrayList<Notice> list = noticeService.selectNewTop3();
		
		//전송용 json 객체 생성
		JSONObject sendJson = new JSONObject();
		//list 옮길 json 배열 준비;
		JSONArray jarr = new JSONArray();
		
		//list 를 jarr 로 옮기기(복사)
		for(Notice notice : list) {
			//notice 필드값 저장용 json 객체 생성
			JSONObject job = new JSONObject();
			
			job.put("noticeno", notice.getNoticeno());
			job.put("noticetitle",URLEncoder.encode(notice.getNoticetitle(), "utf-8"));
			job.put("noticedate", notice.getNoticedate().toString()); 
			// 날짜는 반드시 to스트링 으로 문자열로 바꿔서 json에 담아야함
			
			jarr.add(job); //job을 jarr에 저장
		}
		//전송용 객체에 jarr 을 담음
		sendJson.put("list", jarr);
		
		return sendJson.toJSONString(); // json을 json string 형으로 바꿔서 전송함
		//뷰리졸버에게로 리턴됨
		
		
	}
	
	//공지사항 전체 글 목록 조회용
	@RequestMapping("nlist.do")
	public String noticeListMethod(Model model) {
		ArrayList<Notice> list = noticeService.selectAll();
		
		if(list.size()>0) {
			model.addAttribute("list", list);
			return "notice/noticeListView";
		}else {
			model.addAttribute("message", "등록된 공지사항 정보 없음");
			return "common/error";
		}
	}
	
	
	//공지글 상세보기 요청 처리용
	@RequestMapping("ndetail.do")
	public String noticeDetailMethod(@RequestParam("noticeno") int noticeno, Model model, HttpSession session) {
		Notice notice = noticeService.selectNotice(noticeno);
		
		if(notice != null) {
			model.addAttribute("notice", notice);
			Member loginMember = (Member)session.getAttribute("loginMember");
			if(loginMember != null && loginMember.getAdmin().equals("Y")) {
				//관리자가 상세보기 요청했을 때 
				return "notice/noticeAdminDetailView";
			}else {
				//관리자가 아닌 클라이언트가 상세보기 요청했을 때
				return "notice/noticeDetailView";
			} 
		}else {
			model.addAttribute("message", noticeno + "번 공지글 상세보기 실패");
			return "common/error";
		}
	}
	
	//첨부파일 다운로드 요청
	@RequestMapping("nfdown.do")
	public ModelAndView fileDownMethod(HttpServletRequest request,
			@RequestParam("ofile") String originFileName,
			@RequestParam("rfile") String renameFileName, ModelAndView mv) {
		//공지사항 첨부파일 저장 폴더 경로 지정
		String savePath = request.getSession().getServletContext().getRealPath("resources/notice_upfiles");
		//저장 폴더에서 읽을 파일에 대해 경로 추가하면서 File 객체 생성
		File renameFile = new File(savePath +"\\" + renameFileName);
		//다운을 위해 내보내는 파일 객체 생성
		File originFile = new File(originFileName);
		
		mv.setViewName("filedown"); //등록된 파일 다운로드 처리용 뷰 클래서 id 명
		mv.addObject("renameFile", renameFile); //전달할 파일객체 Model 에 저장
		mv.addObject("originFile", originFile); 
		
		return mv;
	}
}
