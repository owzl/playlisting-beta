package com.playlisting.servlet;

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.vo.PlaylistVO;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playlist/list")
public class PlaylistListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PlaylistDAO playlistDAO;

    public void init() {
        playlistDAO = new PlaylistDAO();
    }

    // HTTP GET 요청 처리
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 1. PlaylistDAO를 사용하여 전체 플레이리스트 목록을 가져옵니다.
            List<PlaylistVO> playlistList = playlistDAO.getAllPlaylists();

            // 2. 가져온 목록 데이터를 Request 객체에 담아 JSP로 전달할 준비를 합니다.
            request.setAttribute("playlistList", playlistList); // "playlistList"라는 이름으로 데이터를 저장

            // 3. 결과를 보여줄 JSP 페이지 경로 지정 (WEB-INF/views 폴더 안)
            String path = "/WEB-INF/views/playlist_list.jsp";

            // 4. JSP로 포워딩
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);

        } catch (Exception e) {
            System.err.println("PlaylistListServlet 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

            throw new ServletException("플레이리스트 목록 로딩 중 오류 발생", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

         response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "error");
    }


    public void destroy() {
        
    }
}