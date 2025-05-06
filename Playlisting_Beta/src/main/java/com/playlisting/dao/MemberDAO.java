package com.playlisting.dao;

import com.playlisting.util.DBUtil;
import com.playlisting.vo.MemberVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    public MemberVO getMemberById(int memberId) {
        MemberVO member = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT member_id, login_id, nickname, email, password, role, oauth_provider, oauth_uid, created_at, updated_at " +
                     "FROM members " +
                     "WHERE member_id = ?";
        try {
            conn = DBUtil.getConnection();

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new MemberVO();
                
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
            System.err.println("MemberDAO - getMemberById 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return member;
    }

}