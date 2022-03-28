package com.test.first.board.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.first.board.model.service.BoardService;
import com.test.first.board.model.vo.Board;
import com.test.first.notice.model.vo.Notice;

@Controller
public class BoardController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
	
	@Autowired
	private BoardService boardService;
	
	@RequestMapping(value="btop3.do", method=RequestMethod.POST)
	@ResponseBody
	public String boardReadCountTop3(HttpServletResponse response) throws UnsupportedEncodingException {
		//조회수 많은 게시 원글 3개 조회해 옴
		ArrayList<Board> list = boardService.selectTop3();
		
		//전송용 json 객체 생성
		JSONObject sendJson = new JSONObject();
		//list 옮길 json 배열 준비;
		JSONArray jarr = new JSONArray();
		
		//list 를 jarr 로 옮기기(복사)
		for(Board board : list) {
			//notice 필드값 저장용 json 객체 생성
			JSONObject job = new JSONObject();
			
			job.put("board_num", board.getboard_num());
			job.put("board_title",URLEncoder.encode(board.getBoard_title(), "utf-8"));
			job.put("board_readcount", board.getBoard_readcount()); 
			
			
			jarr.add(job); //job을 jarr에 저장
		}
		//전송용 객체에 jarr 을 담음
		sendJson.put("list", jarr);
		
		return sendJson.toJSONString(); // json을 json string 형으로 바꿔서 전송함
		//뷰리졸버에게로 리턴됨
		
		
	}
	
}
