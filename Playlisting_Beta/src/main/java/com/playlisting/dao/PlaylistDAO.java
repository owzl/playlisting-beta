package com.playlisting.dao;

import com.playlisting.util.DBUtil;
import com.playlisting.vo.PlaylistVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // executeQuery(sql) 사용할 때 필요
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    // 모든 플레이리스트 목록을 최신순으로 가져오는 메서드
    public List<PlaylistVO> getAllPlaylists() {
        List<PlaylistVO> playlistList = new ArrayList<>(); // 결과를 담을 리스트
        Connection conn = null;
        PreparedStatement pstmt = null; // PreparedStatement 권장 (파라미터 없어도 성능상 이점)
        ResultSet rs = null;

        // SQL: playlists 테이블 전체 조회, 최신 작성일시 순으로 정렬
        String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                     "FROM playlists " +
                     "ORDER BY created_at DESC"; // 최신 글이 목록 상단에 오도록 정렬

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql); // 파라미터 없어도 PreparedStatement 사용 습관 들이기
            rs = pstmt.executeQuery();

            // 결과 집합 처리
            while (rs.next()) { // 결과 행이 있는 동안 반복
                PlaylistVO playlist = new PlaylistVO();
                // ResultSet에서 값을 가져와 PlaylistVO 객체에 설정
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setPlaylistTitle(rs.getString("playlist_title"));
                playlist.setInitialSongTitle(rs.getString("initial_song_title"));
                playlist.setInitialArtist(rs.getString("initial_artist"));
                playlist.setDescription(rs.getString("description")); // NULL 가능성 있음
                playlist.setAuthorId(rs.getInt("author_id"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setViewCount(rs.getInt("view_count"));
                playlist.setUpdatedAt(rs.getTimestamp("updated_at"));

                playlistList.add(playlist); // 리스트에 추가
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - getAllPlaylists 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs); // 자원 반납
        }

        return playlistList; // 플레이리스트 목록 리스트 반환 (없으면 빈 리스트)
    }

    // 특정 플레이리스트 ID로 상세 정보를 가져오는 메서드
    public PlaylistVO getPlaylistById(int playlistId) {
        PlaylistVO playlist = null; // 결과를 담을 VO 객체
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // SQL: playlist_id로 playlists 테이블 조회
        String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                     "FROM playlists " +
                     "WHERE playlist_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId); // WHERE 절의 '?'에 playlistId 설정

            rs = pstmt.executeQuery();

            // 결과 집합 처리 (결과는 최대 1개)
            if (rs.next()) {
                playlist = new PlaylistVO();
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setPlaylistTitle(rs.getString("playlist_title"));
                playlist.setInitialSongTitle(rs.getString("initial_song_title"));
                playlist.setInitialArtist(rs.getString("initial_artist"));
                playlist.setDescription(rs.getString("description")); // NULL 가능성 있음
                playlist.setAuthorId(rs.getInt("author_id"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setViewCount(rs.getInt("view_count"));
                playlist.setUpdatedAt(rs.getTimestamp("updated_at"));
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - getPlaylistById 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return playlist; // 조회된 PlaylistVO 객체 반환 (없으면 null)
    }

    // 새로운 플레이리스트를 DB에 삽입하는 메서드 (작성 기능)
    public boolean createPlaylist(PlaylistVO playlist) {
        boolean success = false; // 작업 성공 여부
        Connection conn = null;
        PreparedStatement pstmt = null;

        // SQL: playlists 테이블에 데이터 삽입
        // playlist_id, created_at, updated_at, view_count는 DB에서 자동 처리/기본값 사용
        String sql = "INSERT INTO playlists (playlist_title, initial_song_title, initial_artist, description, author_id) " +
                     "VALUES (?, ?, ?, ?, ?)"; // PreparedStatement를 사용하여 값 바인딩

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // VO 객체에서 값을 가져와 SQL 파라미터에 설정
            pstmt.setString(1, playlist.getPlaylistTitle());
            pstmt.setString(2, playlist.getInitialSongTitle());
            pstmt.setString(3, playlist.getInitialArtist());
            pstmt.setString(4, playlist.getDescription()); // description은 NULL 허용, String으로 설정
            pstmt.setInt(5, playlist.getAuthorId());

            // SQL 실행 (INSERT, UPDATE, DELETE 쿼리는 executeUpdate)
            int rowsAffected = pstmt.executeUpdate();

            // 삽입된 행의 수가 1 이상이면 성공
            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - createPlaylist 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt); // INSERT는 결과 집합(ResultSet)이 없음
        }

        return success; // 성공 여부 반환
    }

    // 기존 플레이리스트 정보를 수정하는 메서드
    // 수정 성공 시 true, 실패 시 false 반환
    public boolean updatePlaylist(PlaylistVO playlist) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        // SQL: playlist_id에 해당하는 행의 정보 업데이트
        String sql = "UPDATE playlists " +
                     "SET playlist_title = ?, initial_song_title = ?, initial_artist = ?, description = ?, updated_at = CURRENT_TIMESTAMP " + // updated_at 자동 업데이트
                     "WHERE playlist_id = ?"; // 어떤 행을 업데이트할지 지정

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            // VO 객체에서 값을 가져와 SQL 파라미터에 설정
            pstmt.setString(1, playlist.getPlaylistTitle());
            pstmt.setString(2, playlist.getInitialSongTitle());
            pstmt.setString(3, playlist.getInitialArtist());
            pstmt.setString(4, playlist.getDescription()); // description 설정
            pstmt.setInt(5, playlist.getPlaylistId()); // WHERE 절 조건에 사용

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - updatePlaylist 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }

        return success; // 성공 여부 반환
    }

    // 특정 플레이리스트를 삭제하는 메서드
    // 삭제 성공 시 true, 실패 시 false 반환
    public boolean deletePlaylist(int playlistId) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        // SQL: playlist_id에 해당하는 행 삭제
        // FK ON DELETE CASCADE 설정했으므로, 이 플레이리스트에 연결된 추천곡과 댓글도 DB에서 자동으로 삭제됩니다.
        String sql = "DELETE FROM playlists WHERE playlist_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId); // WHERE 절 조건에 사용

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - deletePlaylist 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }

        return success; // 성공 여부 반환
    }

    // 특정 플레이리스트의 조회수를 1 증가시키는 메서드
    // 증가 성공 시 true, 실패 시 false 반환
    public boolean incrementViewCount(int playlistId) {
         boolean success = false;
         Connection conn = null;
         PreparedStatement pstmt = null;

         // SQL: playlist_id에 해당하는 행의 view_count를 1 증가
         String sql = "UPDATE playlists SET view_count = view_count + 1 WHERE playlist_id = ?";

         try {
             conn = DBUtil.getConnection();
             pstmt = conn.prepareStatement(sql);
             pstmt.setInt(1, playlistId);

             int rowsAffected = pstmt.executeUpdate();

             if (rowsAffected > 0) {
                 success = true;
             }

         } catch (SQLException e) {
             System.err.println("PlaylistDAO - incrementViewCount 오류: " + e.getMessage());
             e.printStackTrace();
         } finally {
             DBUtil.close(conn, pstmt);
         }

         return success; // 성공 여부 반환
    }


    // TODO: 검색 (searchPlaylists), 페이지네이션 관련 (getPlaylistCount, getPlaylistsPaged) 메서드 추가 예정
}