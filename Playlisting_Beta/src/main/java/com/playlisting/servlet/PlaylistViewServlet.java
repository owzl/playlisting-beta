package com.playlisting.servlet; // 패키지명

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.dao.SongRecommendationDAO;
import com.playlisting.dao.PlaylistCommentDAO;
import com.playlisting.dao.MemberDAO; // MemberDAO 임포트
import com.playlisting.vo.PlaylistVO;
import com.playlisting.vo.SongRecommendationVO;
import com.playlisting.vo.PlaylistCommentVO;
import com.playlisting.vo.MemberVO; // MemberVO 임포트

import java.io.IOException;
import java.util.List;
import java.util.Map; // Map 사용
import java.util.HashMap; // HashMap 사용
import java.util.Set; // Set 사용
import java.util.HashSet; // HashSet 사용

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playlist/view")
public class PlaylistViewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PlaylistDAO playlistDAO;
    private SongRecommendationDAO recommendationDAO;
    private PlaylistCommentDAO commentDAO;
    private MemberDAO memberDAO;

    public void init() {
        playlistDAO = new PlaylistDAO();
        recommendationDAO = new SongRecommendationDAO();
        commentDAO = new PlaylistCommentDAO();
        memberDAO = new MemberDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int playlistId = -1;

        try {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                playlistId = Integer.parseInt(idParam);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "플레이리스트 ID가 필요합니다.");
                return;
            }

            PlaylistVO playlist = playlistDAO.getPlaylistById(playlistId);

            if (playlist != null) {
                 playlistDAO.incrementViewCount(playlistId);
                 playlist.setViewCount(playlist.getViewCount() + 1);

                 List<SongRecommendationVO> recommendationList = recommendationDAO.getRecommendationsByPlaylistId(playlistId);

                 List<PlaylistCommentVO> commentList = commentDAO.getCommentsByPlaylistId(playlistId);

                 // --- 작성자 닉네임 표시 ---
                 Set<Integer> authorIds = new HashSet<>();
                 authorIds.add(playlist.getAuthorId()); 
                 for (SongRecommendationVO recommendation : recommendationList) {
                     authorIds.add(recommendation.getAuthorId());
                 }
                 for (PlaylistCommentVO comment : commentList) {
                     authorIds.add(comment.getAuthorId());
                 }

                 Map<Integer, String> authorNicknames = new HashMap<>();
                 for (int authorId : authorIds) {
                     MemberVO member = memberDAO.getMemberById(authorId);
                     if (member != null) {
                         authorNicknames.put(authorId, member.getNickname());
                     } else {
                          authorNicknames.put(authorId, "(알 수 없음)");
                     }
                 }
                 // --- 작성자 닉네임 표시 ---

                 request.setAttribute("playlist", playlist);
                 request.setAttribute("recommendationList", recommendationList);
                 request.setAttribute("commentList", commentList);
                 request.setAttribute("authorNicknames", authorNicknames);

                 String path = "/WEB-INF/views/playlist_view.jsp";
                 RequestDispatcher dispatcher = request.getRequestDispatcher(path);
                 dispatcher.forward(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청한 플레이리스트를 찾을 수 없습니다.");
            }

        } catch (NumberFormatException e) {
            System.err.println("PlaylistViewServlet - 유효하지 않은 플레이리스트 ID 형식: " + request.getParameter("id"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 플레이리스트 ID 형식입니다.");
        } catch (Exception e) {
            System.err.println("PlaylistViewServlet 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("플레이리스트 상세 정보 로딩 중 오류 발생", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method is not supported for this URL.");
    }

     public void destroy() {
     }
}