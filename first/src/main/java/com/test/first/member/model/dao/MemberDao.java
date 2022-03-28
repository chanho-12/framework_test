package com.test.first.member.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.test.first.common.SearchDate;
import com.test.first.member.model.vo.Member;

@Repository("memberDao") //xml 에 자동 등록됨
public class MemberDao {
	
	//마이바티스의 매퍼파일의 쿼리문 실행
	//root-context.xml 에 생성된 객체를 연결하여 사용함
	@Autowired
	private SqlSessionTemplate session;

	public MemberDao() {}
	
	public Member selectLogin(Member member) {
		return session.selectOne("memberMapper.selectLogin", member);
	}

	
	public int insertMember(Member member) {
		return session.insert("memberMapper.insertMember", member);
	}

	
	public int selectDupCheckId(String userid) {
		return session.selectOne("memberMapper.selectCheckId", userid);
	}

	
	public int updateMember(Member member) {
		return session.update("memberMapper.updateMember", member);
	}

	
	public int deleteMember(String userid) {
		return session.delete("memberMapper.deleteMember", userid);
	}

	
	public ArrayList<Member> selectList() {
		List<Member> list = session.selectList("memberMapper.selectList");
		return (ArrayList<Member>)list;
	}

	
	public Member selectMember(String userid) {
		return session.selectOne("memberMapper.selectMember", userid);
	}

	
	public int updateLoginOk(Member member) {
		return session.update("memberMapper.updateLoginOK", member);
	}

	
	public ArrayList<Member> selectSearchUserid(String keyword) {
		List<Member> list = session.selectList(
				"memberMapper.selectSearchUserid",keyword);
		return (ArrayList<Member>)list;
	}

	
	public ArrayList<Member> selectSearchGender(String keyword) {
		List<Member> list = session.selectList(
				"memberMapper.selectSearchGender",keyword);
		return (ArrayList<Member>)list;
	}

	
	public ArrayList<Member> selectSearchAge(int age) {
		List<Member> list = session.selectList(
				"memberMapper.selectSearchAge",age);
		return (ArrayList<Member>)list;
	}

	
	public ArrayList<Member> selectSearchEnrollDate(SearchDate searchDate) {
		List<Member> list = session.selectList(
				"memberMapper.selectSearchEnrollDate",searchDate);
		return (ArrayList<Member>)list;
	}

	
	public ArrayList<Member> selectSearchLoginOK(String keyword) {
		List<Member> list = session.selectList(
				"memberMapper.selectSearchLoginOk",keyword);
		return (ArrayList<Member>)list;
	}
	
	
}









