package com.playlisting.servlet; // 패키지명

import com.playlisting.dao.SongRecommendationDAO;
import com.playlisting.vo.SongRecommendationVO;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recommendation/add")
public class SongRecommendationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SongRecommendationDAO recommendationDAO;

    public void init() {
        recommendationDAO = new SongRecommendationDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int playlistId = Integer.parseInt(request.getParameter("playlistId"));
        String songTitle = request.getParameter("songTitle");
        String artist = request.getParameter("artist");

        int authorId = 1;

        SongRecommendationVO newRecommendation = new SongRecommendationVO();
        newRecommendation.setPlaylistId(playlistId);
        newRecommendation.setSongTitle(songTitle);
        newRecommendation.setArtist(artist);
        newRecommendation.setAuthorId(authorId);

        boolean success = recommendationDAO.addRecommendation(newRecommendation);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/playlist/view?id=" + playlistId);
        } else {
            System.err.println("추천 노래 추가 실패! Playlist ID: " + playlistId);
            request.setAttribute("errorMessage", "추천 노래 추가 중 오류가 발생했습니다.");
            String path = "/WEB-INF/views/error.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET method is not supported for this URL.");
    }

     public void destroy() {
     }
}