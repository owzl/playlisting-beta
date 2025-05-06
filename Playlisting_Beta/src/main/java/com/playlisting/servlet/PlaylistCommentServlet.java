package com.playlisting.servlet;

import com.playlisting.dao.PlaylistCommentDAO;
import com.playlisting.vo.PlaylistCommentVO;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/comment/add")
public class PlaylistCommentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PlaylistCommentDAO commentDAO;

    public void init() {
        commentDAO = new PlaylistCommentDAO();
    }

    // 댓글 작성
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int playlistId = Integer.parseInt(request.getParameter("playlistId"));
        String commentContent = request.getParameter("commentContent");

        int authorId = 1;

        PlaylistCommentVO newComment = new PlaylistCommentVO();
        newComment.setPlaylistId(playlistId);
        newComment.setCommentContent(commentContent);
        newComment.setAuthorId(authorId);

        boolean success = commentDAO.addComment(newComment);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/playlist/view?id=" + playlistId);
        } else {
            System.err.println("댓글 작성 실패! Playlist ID: " + playlistId);
            request.setAttribute("errorMessage", "댓글 작성 중 오류가 발생했습니다.");
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