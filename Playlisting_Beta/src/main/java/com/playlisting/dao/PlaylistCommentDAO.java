package com.playlisting.dao;

import com.playlisting.util.DBUtil;
import com.playlisting.vo.PlaylistCommentVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistCommentDAO {

    public boolean addComment(PlaylistCommentVO comment) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO playlist_comments (playlist_id, comment_content, author_id) " +
                     "VALUES (?, ?, ?)";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, comment.getPlaylistId());
            pstmt.setString(2, comment.getCommentContent());
            pstmt.setInt(3, comment.getAuthorId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistCommentDAO - addComment 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }

        return success;
    }

    // 특정 플레이리스트 ID에 달린 모든 댓글 목록을 최신순으로 가져오는 메서드
    public List<PlaylistCommentVO> getCommentsByPlaylistId(int playlistId) {
        List<PlaylistCommentVO> commentList = new ArrayList<>(); // 결과를 담을 리스트
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT comment_id, playlist_id, comment_content, author_id, created_at, updated_at " +
                     "FROM playlist_comments " +
                     "WHERE playlist_id = ? " +
                     "ORDER BY created_at DESC";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PlaylistCommentVO comment = new PlaylistCommentVO();
                
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setPlaylistId(rs.getInt("playlist_id"));
                comment.setCommentContent(rs.getString("comment_content"));
                comment.setAuthorId(rs.getInt("author_id"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comment.setUpdatedAt(rs.getTimestamp("updated_at"));

                commentList.add(comment);
            }

        } catch (SQLException e) {
            System.err.println("PlaylistCommentDAO - getCommentsByPlaylistId 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return commentList;
    }

}