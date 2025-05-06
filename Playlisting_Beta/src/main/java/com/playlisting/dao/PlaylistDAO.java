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

    // 게시글 목록 최신 정렬 조회
    public List<PlaylistVO> getAllPlaylists() {
        List<PlaylistVO> playlistList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                     "FROM playlists " +
                     "ORDER BY created_at DESC";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PlaylistVO playlist = new PlaylistVO();
                
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setPlaylistTitle(rs.getString("playlist_title"));
                playlist.setInitialSongTitle(rs.getString("initial_song_title"));
                playlist.setInitialArtist(rs.getString("initial_artist"));
                playlist.setDescription(rs.getString("description"));
                playlist.setAuthorId(rs.getInt("author_id"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setViewCount(rs.getInt("view_count"));
                playlist.setUpdatedAt(rs.getTimestamp("updated_at"));

                playlistList.add(playlist);
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - getAllPlaylists 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return playlistList;
    }

    // 상세 정보 조회
    public PlaylistVO getPlaylistById(int playlistId) {
        PlaylistVO playlist = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                     "FROM playlists " +
                     "WHERE playlist_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                playlist = new PlaylistVO();
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setPlaylistTitle(rs.getString("playlist_title"));
                playlist.setInitialSongTitle(rs.getString("initial_song_title"));
                playlist.setInitialArtist(rs.getString("initial_artist"));
                playlist.setDescription(rs.getString("description"));
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

        return playlist;
    }

    // 새로운 플레이리스트 작성
    public boolean createPlaylist(PlaylistVO playlist) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO playlists (playlist_title, initial_song_title, initial_artist, description, author_id) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, playlist.getPlaylistTitle());
            pstmt.setString(2, playlist.getInitialSongTitle());
            pstmt.setString(3, playlist.getInitialArtist());
            pstmt.setString(4, playlist.getDescription());
            pstmt.setInt(5, playlist.getAuthorId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                success = true;
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - createPlaylist 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }

        return success;
    }

    // 플레이리스트 수정
    public boolean updatePlaylist(PlaylistVO playlist) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE playlists " +
                     "SET playlist_title = ?, initial_song_title = ?, initial_artist = ?, description = ?, updated_at = CURRENT_TIMESTAMP " + // updated_at 자동 업데이트
                     "WHERE playlist_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, playlist.getPlaylistTitle());
            pstmt.setString(2, playlist.getInitialSongTitle());
            pstmt.setString(3, playlist.getInitialArtist());
            pstmt.setString(4, playlist.getDescription());
            pstmt.setInt(5, playlist.getPlaylistId());

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

        return success;
    }

    // 플레이리스트 삭제
    public boolean deletePlaylist(int playlistId) {
        boolean success = false;
        Connection conn = null;
        PreparedStatement pstmt = null;

        // FK ON DELETE CASCADE 설정했으므로, 이 플레이리스트에 연결된 추천곡과 댓글도 DB에서 자동으로 삭제
        String sql = "DELETE FROM playlists WHERE playlist_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, playlistId);

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

        return success;
    }

    // 특정 플레이리스트의 조회수를 1 증가시키는 메서드
    public boolean incrementViewCount(int playlistId) {
         boolean success = false;
         Connection conn = null;
         PreparedStatement pstmt = null;

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

         return success;
    }


    // ========================================
    // 페이지네이션 관련 메서드
    // ========================================

    // 전체 플레이리스트 개수를 조회하는 메서드
    public int getPlaylistCount() {
        int count = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT COUNT(*) FROM playlists";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - getPlaylistCount 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return count;
    }


    // 페이지네이션
    public List<PlaylistVO> getPlaylistsPaged(int startRow, int pageSize) {
        List<PlaylistVO> playlistList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                     "FROM playlists " +
                     "ORDER BY created_at DESC " + 
                     "LIMIT ?, ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, startRow);
            pstmt.setInt(2, pageSize);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                PlaylistVO playlist = new PlaylistVO();
                playlist.setPlaylistId(rs.getInt("playlist_id"));
                playlist.setPlaylistTitle(rs.getString("playlist_title"));
                playlist.setInitialSongTitle(rs.getString("initial_song_title"));
                playlist.setInitialArtist(rs.getString("initial_artist"));
                playlist.setDescription(rs.getString("description"));
                playlist.setAuthorId(rs.getInt("author_id"));
                playlist.setCreatedAt(rs.getTimestamp("created_at"));
                playlist.setViewCount(rs.getInt("view_count"));
                playlist.setUpdatedAt(rs.getTimestamp("updated_at"));

                playlistList.add(playlist);
            }

        } catch (SQLException e) {
            System.err.println("PlaylistDAO - getPlaylistsPaged 오류: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return playlistList;
    }
    
 // ========================================
 // 검색 + 페이지네이션 관련 메서드
 // ========================================
    
 // 검색 조건에 맞는 플레이리스트 개수를 조회하는 메서드
 // searchTerm: 검색어
 public int searchPlaylistCount(String searchTerm) {
     int count = 0;
     Connection conn = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;

     String sql = "SELECT COUNT(*) FROM playlists " +
                  "WHERE playlist_title LIKE ? OR initial_song_title LIKE ? OR initial_artist LIKE ?";

     try {
         conn = DBUtil.getConnection();
         pstmt = conn.prepareStatement(sql);

         pstmt.setString(1, "%" + searchTerm + "%");
         pstmt.setString(2, "%" + searchTerm + "%");
         pstmt.setString(3, "%" + searchTerm + "%");

         rs = pstmt.executeQuery();

         if (rs.next()) {
             count = rs.getInt(1);
         }

     } catch (SQLException e) {
         System.err.println("PlaylistDAO - searchPlaylistCount 오류: " + e.getMessage());
         e.printStackTrace();
     } finally {
         DBUtil.close(conn, pstmt, rs);
     }

     return count;
 }


 // 검색 조건에 맞는 특정 페이지의 플레이리스트 목록을 가져오는 메서드
 public List<PlaylistVO> searchPlaylistsPaged(String searchTerm, int startRow, int pageSize) {
     List<PlaylistVO> playlistList = new ArrayList<>();
     Connection conn = null;
     PreparedStatement pstmt = null;
     ResultSet rs = null;

     String sql = "SELECT playlist_id, playlist_title, initial_song_title, initial_artist, description, author_id, created_at, view_count, updated_at " +
                  "FROM playlists " +
                  "WHERE playlist_title LIKE ? OR initial_song_title LIKE ? OR initial_artist LIKE ?" +
                  "ORDER BY created_at DESC " +
                  "LIMIT ?, ?";

     try {
         conn = DBUtil.getConnection();
         pstmt = conn.prepareStatement(sql);

         pstmt.setString(1, "%" + searchTerm + "%");
         pstmt.setString(2, "%" + searchTerm + "%");
         pstmt.setString(3, "%" + searchTerm + "%");
         pstmt.setInt(4, startRow);
         pstmt.setInt(5, pageSize);

         rs = pstmt.executeQuery();

         while (rs.next()) {
             PlaylistVO playlist = new PlaylistVO();
             playlist.setPlaylistId(rs.getInt("playlist_id"));
             playlist.setPlaylistTitle(rs.getString("playlist_title"));
             playlist.setInitialSongTitle(rs.getString("initial_song_title"));
             playlist.setInitialArtist(rs.getString("initial_artist"));
             playlist.setDescription(rs.getString("description"));
             playlist.setAuthorId(rs.getInt("author_id"));
             playlist.setCreatedAt(rs.getTimestamp("created_at"));
             playlist.setViewCount(rs.getInt("view_count"));
             playlist.setUpdatedAt(rs.getTimestamp("updated_at"));

             playlistList.add(playlist);
         }

     } catch (SQLException e) {
         System.err.println("PlaylistDAO - searchPlaylistsPaged 오류: " + e.getMessage());
         e.printStackTrace();
     } finally {
         DBUtil.close(conn, pstmt, rs);
     }

     return playlistList;
 }
}