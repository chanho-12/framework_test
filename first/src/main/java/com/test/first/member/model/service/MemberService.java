package com.test.first.member.model.service;

import java.util.ArrayList;

import com.test.first.common.SearchDate;
import com.test.first.member.model.vo.Member;

//스프링에서는 모델의 service 는 반드시 interface 를 만들도록 되어 있음
//서비스 인터페이스를 상속받는 후손클래스를 만들어서 
//정의된 메소드 오버라이딩 방식으로 구현하도록 해야 함
public interface MemberService {
	Member selectLogin(Member member);
	int insertMember(Member member);
	int selectDupCheckId(String userid); //회원가입시 중복체크
	int updateMember(Member member);
	int deleteMember(String userid);
	ArrayList<Member> selectList();
	Member selectMember(String userid);
	int updateLoginOk(Member member);
	ArrayList<Member> selectSearchUserid(String keyword);
	ArrayList<Member> selectSearchGender(String keyword);
	ArrayList<Member> selectSearchAge(int age);
	ArrayList<Member> selectSearchEnrollDate(SearchDate searchDate);
	ArrayList<Member> selectSearchLoginOK(String keyword);
	
	
}
