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

    // 새로운 댓글을 DB에 삽입하는 메서드
    // 삽입 성공 시 true, 실패 시 false 반환
    // comment 객체는 playlistId, commentContent, authorId 정보를 담고 있어야 합니다.
    public boolean addComment(PlaylistCommentVO comment) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        // SQL: playlist_comments 테이블에 데이터 삽입
        // comment_id, created_at, updated_at는 DB에서 자동 처리/기본값 사용
        String sql = "INSERT INTO playlist_comments (playlist_id, comment_content, author_id) " +
                     "VALUES (?, ?, ?)"; // PreparedStatement 사용

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // VO 객체에서 값을 가져와 SQL 파라미터에 설정
            pstmt.setInt(1, comment.getPlaylistId());
            pstmt.setString(2, comment.getCommentContent());
            pstmt.setInt(3, comment.getAuthorId());

            // SQL 실행
            int rowsAffected = pstmt.executeUpdate();

            // 삽입된 행의 수가 1 이상이면 성공
            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistCommentDAO - addComment 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt); // INSERT는 결과 집합(ResultSet)이 없음
        }

        return success; // 성공 여부 반환
    }

    // 특정 플레이리스트 ID에 달린 모든 댓글 목록을 최신순으로 가져오는 메서드
    public List<PlaylistCommentVO> getCommentsByPlaylistId(int playlistId) {
        List<PlaylistCommentVO> commentList = new ArrayList<>(); // 결과를 담을 리스트
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: playlist_id에 해당하는 댓글들 조회, 최신 작성일시 순으로 정렬
        String sql = "SELECT comment_id, playlist_id, comment_content, author_id, created_at, updated_at " +
                     "FROM playlist_comments " +
                     "WHERE playlist_id = ? " +
                     "ORDER BY created_at DESC"; // 최신 댓글이 목록 상단에 오도록 정렬 (또는 ASC로 오래된 댓글부터)

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId); // WHERE 절의 '?'에 playlistId 설정

            rs = pstmt.executeQuery();

            // 결과 집합 처리
            while (rs.next()) { // 결과 행이 있는 동안 반복
                PlaylistCommentVO comment = new PlaylistCommentVO();
                // ResultSet에서 값을 가져와 PlaylistCommentVO 객체에 설정
                comment.setCommentId(rs.getInt("comment_id"));
                comment.setPlaylistId(rs.getInt("playlist_id"));
                comment.setCommentContent(rs.getString("comment_content"));
                comment.setAuthorId(rs.getInt("author_id"));
                comment.setCreatedAt(rs.getTimestamp("created_at"));
                comment.setUpdatedAt(rs.getTimestamp("updated_at"));

                commentList.add(comment); // 리스트에 추가
            }

        } catch (SQLException e) {
            System.err.println("PlaylistCommentDAO - getCommentsByPlaylistId 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs); // 자원 반납
        }

        return commentList; // 댓글 목록 리스트 반환 (없으면 빈 리스트)
    }

    // TODO: 댓글 수정 (updateComment), 댓글 삭제 (deleteComment) 등 필요시 추가 구현
}