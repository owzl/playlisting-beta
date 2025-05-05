package com.playlisting.dao;

import com.playlisting.util.DBUtil;
import com.playlisting.vo.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    // 회원 고유 번호(member_id)로 회원 정보(MemberVO)를 가져오는 메서드
    public MemberVO getMemberById(int memberId) {
        MemberVO member = null; // 결과를 담을 VO 객체
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: member_id로 members 테이블 조회
        String sql = "SELECT member_id, login_id, nickname, email, password, role, oauth_provider, oauth_uid, created_at, updated_at " +
                     "FROM members " +
                     "WHERE member_id = ?"; // PreparedStatement를 사용하여 값 바인딩

        try {
            // 1. DB 연결 가져오기
            conn = DBUtil.getConnection();

            // 2. PreparedStatement 생성 및 파라미터 설정
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId); // WHERE 절의 첫 번째 '?'에 memberId 설정

            // 3. SQL 실행 및 결과 가져오기
            rs = pstmt.executeQuery();

            // 4. 결과 집합 처리 (결과는 최대 1개)
            if (rs.next()) {
                member = new MemberVO();
                // ResultSet에서 값을 가져와 MemberVO 객체에 설정
                member.setMemberId(rs.getInt("member_id"));
                member.setLoginId(rs.getString("login_id"));
                member.setNickname(rs.getString("nickname"));
                member.setEmail(rs.getString("email"));
                member.setPassword(rs.getString("password"));
                member.setRole(rs.getString("role"));
                member.setOauthProvider(rs.getString("oauth_provider"));
                member.setOauthUid(rs.getString("oauth_uid"));
                member.setCreatedAt(rs.getTimestamp("created_at"));
                member.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            // SQL 실행 중 오류 발생
            System.err.println("MemberDAO - getMemberById 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 5. 사용한 자원 반납
            DBUtil.close(conn, pstmt, rs);
        }

        return member; // 조회된 MemberVO 객체 반환 (없으면 null)
    }

    // TODO: 회원 가입 (createMember), 로그인 (getMemberByLoginIdAndPassword), 회원 정보 수정 (updateMember), 회원 탈퇴 (deleteMember) 등 필요시 추가 구현
}