package com.playlisting.servlet;

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.dao.SongRecommendationDAO;
import com.playlisting.dao.PlaylistCommentDAO;
import com.playlisting.dao.MemberDAO;
import com.playlisting.vo.PlaylistVO;
import com.playlisting.vo.SongRecommendationVO;
import com.playlisting.vo.PlaylistCommentVO;
// import com.playlisting.vo.MemberVO;

import java.io.IOException;
import java.util.List;

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
        
        // request.setCharacterEncoding("UTF-8"); // GET 방식에서는 WAS 설정에 따라 필요 없을 수 있으나 POST에서는 필수

        int playlistId = -1; // 플레이리스트 ID 초기값

        try {
            // 1. 요청 파라미터에서 "id" 값을 가져와 int로 변환
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                playlistId = Integer.parseInt(idParam);
            } else {
                // ID 파라미터가 없으면 오류 처리 (예: 목록 페이지로 리다이렉트 또는 오류 메시지 표시)
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "플레이리스트 ID가 필요합니다.");
                return; // 처리 중단
            }

            // 2. PlaylistDAO를 사용하여 플레이리스트 상세 정보 가져오기
            PlaylistVO playlist = playlistDAO.getPlaylistById(playlistId);

            // 플레이리스트가 존재하는 경우에만 추가 정보 로딩 및 조회수 증가
            if (playlist != null) {
                 // 3. 해당 플레이리스트의 조회수 증가 (DB 업데이트)
                 playlistDAO.incrementViewCount(playlistId);
                 // 조회수 증가는 DB에서 바로 반영되지만, 현재 페이지에 보여지는 조회수를 업데이트하기 위해
                 // VO 객체의 viewCount 값도 1 증가시켜주는 것이 좋음
                 playlist.setViewCount(playlist.getViewCount() + 1);


                 // 4. SongRecommendationDAO를 사용하여 해당 플레이리스트의 추천곡 목록 가져오기
                 List<SongRecommendationVO> recommendationList = recommendationDAO.getRecommendationsByPlaylistId(playlistId);

                 // 5. PlaylistCommentDAO를 사용하여 해당 플레이리스트의 댓글 목록 가져오기
                 List<PlaylistCommentVO> commentList = commentDAO.getCommentsByPlaylistId(playlistId);

                 // TODO: 작성자 닉네임 처리
                 // 모든 플레이리스트, 추천곡, 댓글 객체는 author_id를 가집니다.
                 // JSP에서 author_id를 이용해 MemberDAO를 호출하여 닉네임을 가져오거나,
                 // 서블릿에서 미리 필요한 작성자들의 닉네임을 Map 등에 담아 전달하는 방법이 있습니다.
                 // JSP에서 EL로 DAO를 호출하는 방법 (JSP에 Java 코드 또는 EL 함수 필요)이 간단할 수 있습니다.

                 // 6. 가져온 모든 데이터를 Request 객체에 담아 JSP로 전달
                 request.setAttribute("playlist", playlist); // 플레이리스트 상세 정보
                 request.setAttribute("recommendationList", recommendationList); // 추천곡 목록
                 request.setAttribute("commentList", commentList); // 댓글 목록
                 // TODO: 작성자 닉네임 처리를 위한 데이터 추가 필요 (예: request.setAttribute("memberDAO", memberDAO); 또는 닉네임 맵)

                 // 7. 결과를 보여줄 JSP 페이지 경로 지정 (WEB-INF/views 폴더 안)
                 String path = "/WEB-INF/views/playlist_view.jsp";

                 // 8. JSP로 포워딩
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