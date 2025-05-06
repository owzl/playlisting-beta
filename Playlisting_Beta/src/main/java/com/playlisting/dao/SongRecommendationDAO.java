package com.playlisting.dao;

import com.playlisting.util.DBUtil;
import com.playlisting.vo.SongRecommendationVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongRecommendationDAO {

    // 답글 작성
    public boolean addRecommendation(SongRecommendationVO recommendation) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmtSelectMaxOrder = null;
        PreparedStatement pstmtIncrementOrder = null;
        PreparedStatement pstmtInsert = null;
        ResultSet rs = null;

        int targetOrder = 0;


        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            String selectMaxOrderSql = "SELECT COALESCE(MAX(`order`), 0) FROM playlist_recommendations WHERE playlist_id = ?";
            pstmtSelectMaxOrder = conn.prepareStatement(selectMaxOrderSql);
            pstmtSelectMaxOrder.setInt(1, recommendation.getPlaylistId());
            rs = pstmtSelectMaxOrder.executeQuery();

            if (rs.next()) {
                int maxOrder = rs.getInt(1);
                targetOrder = maxOrder + 1;
            } else {
                 targetOrder = 1;
            }

            String insertSql = "INSERT INTO playlist_recommendations (playlist_id, song_title, artist, author_id, `order`) " +
                               "VALUES (?, ?, ?, ?, ?)";
            pstmtInsert = conn.prepareStatement(insertSql);

            pstmtInsert.setInt(1, recommendation.getPlaylistId());
            pstmtInsert.setString(2, recommendation.getSongTitle());
            pstmtInsert.setString(3, recommendation.getArtist());
            pstmtInsert.setInt(4, recommendation.getAuthorId());
            pstmtInsert.setInt(5, targetOrder);

            int rowsAffected = pstmtInsert.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            System.err.println("SongRecommendationDAO - addRecommendation 오류: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rb) {
                    System.err.println("Rollback 실패: " + rb.getMessage());
                }
            }
        } finally {
            DBUtil.close(rs);
            DBUtil.close(pstmtSelectMaxOrder);
            DBUtil.close(pstmtInsert);
            DBUtil.close(conn);
        }

        return success;
    }


    public List<SongRecommendationVO> getRecommendationsByPlaylistId(int playlistId) {
        List<SongRecommendationVO> recommendationList = new ArrayList<>(); // 결과를 담을 리스트
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT recommendation_id, playlist_id, song_title, artist, author_id, recommended_at, `order` " +
                     "FROM playlist_recommendations " +
                     "WHERE playlist_id = ? " +
                     "ORDER BY `order` ASC, recommended_at ASC";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

            rs = pstmt.executeQuery();

            while (rs.next()) { 
                SongRecommendationVO recommendation = new SongRecommendationVO();
                recommendation.setRecommendationId(rs.getInt("recommendation_id"));
                recommendation.setPlaylistId(rs.getInt("playlist_id"));
                recommendation.setSongTitle(rs.getString("song_title"));
                recommendation.setArtist(rs.getString("artist"));
                recommendation.setAuthorId(rs.getInt("author_id"));
                recommendation.setRecommendedAt(rs.getTimestamp("recommended_at"));
                recommendation.setOrder(rs.getInt("order"));

                recommendationList.add(recommendation);
            }

        } catch (SQLException e) {
            System.err.println("SongRecommendationDAO - getRecommendationsByPlaylistId 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return recommendationList;
    }

}