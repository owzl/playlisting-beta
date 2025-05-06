package com.playlisting.servlet;

import com.playlisting.dao.PlaylistDAO;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playlist/delete")
public class PlaylistDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PlaylistDAO playlistDAO;

    public void init() {
        playlistDAO = new PlaylistDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         int playlistId = -1;

        try {
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isEmpty()) {
                playlistId = Integer.parseInt(idParam);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "삭제할 플레이리스트 ID가 필요합니다.");
                return;
            }

            boolean success = playlistDAO.deletePlaylist(playlistId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/playlist/list");
            } else {
                System.err.println("플레이리스트 삭제 실패! Playlist ID: " + playlistId);
                 request.setAttribute("errorMessage", "플레이리스트 삭제 중 오류가 발생했습니다. (ID: " + playlistId + ")");
                 String path = "/WEB-INF/views/error.jsp";
                 RequestDispatcher dispatcher = request.getRequestDispatcher(path);
                 dispatcher.forward(request, response);
            }

        } catch (NumberFormatException e) {
            System.err.println("PlaylistDeleteServlet - 유효하지 않은 플레이리스트 ID 형식: " + request.getParameter("id"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 플레이리스트 ID 형식입니다.");
        } catch (Exception e) {
            System.err.println("PlaylistDeleteServlet (GET) 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("플레이리스트 삭제 중 오류 발생", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method is not supported for this URL.");
    }

     public void destroy() {
     }
}