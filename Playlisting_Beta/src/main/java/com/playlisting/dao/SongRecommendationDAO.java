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

    // 새로운 추천 노래를 DB에 삽입하는 메서드
    // 이 메서드에서 order 값 관리 로직을 수행합니다.
    // 삽입 성공 시 true, 실패 시 false 반환
    // recommendation 객체는 playlistId, songTitle, artist, authorId 정보를 담고 있어야 합니다.
    // 삽입될 위치의 order 값은 이 메서드 외부(Servlet 등)에서 결정하여 전달하거나,
    // 단순히 '현재 마지막 순서 다음'으로 추가하는 경우는 이 메서드 내에서 마지막 order 값을 조회하여 결정합니다.
    // 여기서는 간단하게 '현재 마지막 순서 다음'으로 추가하는 로직을 포함합니다.
    // 만약 '원하는 위치에 삽입' 기능을 구현하려면, 메서드 인자로 삽입될 targetOrder 값을 받아야 합니다.
    public boolean addRecommendation(SongRecommendationVO recommendation) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmtSelectMaxOrder = null; // 마지막 order 값 조회용
        PreparedStatement pstmtIncrementOrder = null; // order 값 증가용
        PreparedStatement pstmtInsert = null; // 삽입용
        ResultSet rs = null;

        int targetOrder = 0; // 삽입될 위치의 order 값. 기본값 0 (맨 처음 또는 마지막)

        // TODO: 만약 원하는 위치에 삽입 기능을 구현한다면, 메서드 시그니처를 addRecommendation(SongRecommendationVO recommendation, int targetOrder) 로 변경하고,
        // 아래 targetOrder 계산 대신 전달받은 targetOrder 값을 사용해야 합니다.
        // 여기서는 마지막 순서 다음으로 추가하는 로직을 기본으로 합니다.

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작: 여러 쿼리가 원자적으로 실행되도록 설정

            // 1. 해당 플레이리스트의 현재 가장 큰 order 값을 조회 (마지막 순서를 찾기 위해)
            // SELECT COALESCE(MAX(`order`), 0)는 결과가 없을 경우 0을 반환
            String selectMaxOrderSql = "SELECT COALESCE(MAX(`order`), 0) FROM playlist_recommendations WHERE playlist_id = ?";
            pstmtSelectMaxOrder = conn.prepareStatement(selectMaxOrderSql);
            pstmtSelectMaxOrder.setInt(1, recommendation.getPlaylistId());
            rs = pstmtSelectMaxOrder.executeQuery();

            if (rs.next()) {
                int maxOrder = rs.getInt(1);
                targetOrder = maxOrder + 1; // 마지막 순서 다음으로 추가
                // TODO: 만약 '원하는 위치에 삽입' 기능을 구현한다면, 여기서 targetOrder = 전달받은 targetOrder 값;
                // 그리고 아래 2번 UPDATE 쿼리를 실행해야 합니다.
            } else {
                 // 결과가 없으면 (추천곡이 처음 추가되는 경우) order를 1로 시작
                 targetOrder = 1; // 첫 번째 추천곡의 순서를 1로 시작
            }

            // 2. (중요) 만약 '원하는 위치에 삽입' 기능을 구현한다면, 여기서 UPDATE 쿼리로 순서를 밀어내야 합니다.
            // 예시: 삽입될 order 값 이상인 모든 행의 order 값을 1 증가 시킴
            // String incrementOrderSql = "UPDATE playlist_recommendations SET `order` = `order` + 1 WHERE playlist_id = ? AND `order` >= ?";
            // pstmtIncrementOrder = conn.prepareStatement(incrementOrderSql);
            // pstmtIncrementOrder.setInt(1, recommendation.getPlaylistId());
            // pstmtIncrementOrder.setInt(2, targetOrder);
            // pstmtIncrementOrder.executeUpdate();

            // 3. 새로운 추천 노래 삽입
            String insertSql = "INSERT INTO playlist_recommendations (playlist_id, song_title, artist, author_id, `order`) " +
                               "VALUES (?, ?, ?, ?, ?)";
            pstmtInsert = conn.prepareStatement(insertSql);

            // VO 객체에서 값을 가져와 SQL 파라미터에 설정
            pstmtInsert.setInt(1, recommendation.getPlaylistId());
            pstmtInsert.setString(2, recommendation.getSongTitle());
            pstmtInsert.setString(3, recommendation.getArtist());
            pstmtInsert.setInt(4, recommendation.getAuthorId());
            pstmtInsert.setInt(5, targetOrder); // 계산된/결정된 order 값 사용

            int rowsAffected = pstmtInsert.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
                conn.commit(); // 모든 작업 성공 시 트랜잭션 커밋
            } else {
                conn.rollback(); // 삽입 실패 시 트랜잭션 롤백
            }

        } catch (SQLException e) {
            System.err.println("SongRecommendationDAO - addRecommendation 오류: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 오류 발생 시 트랜잭션 롤백
                } catch (SQLException rb) {
                    System.err.println("Rollback 실패: " + rb.getMessage());
                }
            }
        } finally {
            // 자원 반납
            DBUtil.close(rs);
            DBUtil.close(pstmtSelectMaxOrder);
            // TODO: 만약 순서 밀어내기 UPDATE 쿼리를 사용했다면 pstmtIncrementOrder도 닫아야 함
            // DBUtil.close(pstmtIncrementOrder);
            DBUtil.close(pstmtInsert);
            DBUtil.close(conn); // Connection은 트랜잭션 종료(commit/rollback) 후에 닫기
        }

        return success; // 성공 여부 반환
    }


    // 특정 플레이리스트 ID에 속한 모든 추천 노래 목록을 순서대로 가져오는 메서드
    public List<SongRecommendationVO> getRecommendationsByPlaylistId(int playlistId) {
        List<SongRecommendationVO> recommendationList = new ArrayList<>(); // 결과를 담을 리스트
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: playlist_id에 해당하는 추천곡들 조회, order 순으로 정렬
        String sql = "SELECT recommendation_id, playlist_id, song_title, artist, author_id, recommended_at, `order` " +
                     "FROM playlist_recommendations " +
                     "WHERE playlist_id = ? " +
                     "ORDER BY `order` ASC, recommended_at ASC"; // order 순으로 먼저 정렬, order가 같으면 추천일시 순으로

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId); // WHERE 절의 '?'에 playlistId 설정

            rs = pstmt.executeQuery();

            // 결과 집합 처리
            while (rs.next()) { // 결과 행이 있는 동안 반복
                SongRecommendationVO recommendation = new SongRecommendationVO();
                // ResultSet에서 값을 가져와 SongRecommendationVO 객체에 설정
                recommendation.setRecommendationId(rs.getInt("recommendation_id"));
                recommendation.setPlaylistId(rs.getInt("playlist_id"));
                recommendation.setSongTitle(rs.getString("song_title"));
                recommendation.setArtist(rs.getString("artist"));
                recommendation.setAuthorId(rs.getInt("author_id"));
                recommendation.setRecommendedAt(rs.getTimestamp("recommended_at"));
                recommendation.setOrder(rs.getInt("order"));

                recommendationList.add(recommendation); // 리스트에 추가
            }

        } catch (SQLException e) {
            System.err.println("SongRecommendationDAO - getRecommendationsByPlaylistId 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs); // 자원 반납
        }

        return recommendationList; // 추천 노래 목록 리스트 반환 (없으면 빈 리스트)
    }

    // TODO: 추천 노래 수정 (updateRecommendation), 추천 노래 삭제 (deleteRecommendation) 등 필요시 추가 구현
}