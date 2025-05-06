package com.playlisting.servlet;

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.vo.PlaylistVO;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playlist/edit")
public class PlaylistEditServlet extends HttpServlet {
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
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "수정할 플레이리스트 ID가 필요합니다.");
                return;
            }

            PlaylistVO playlist = playlistDAO.getPlaylistById(playlistId);

            if (playlist != null) {
                request.setAttribute("playlist", playlist);
                String path = "/WEB-INF/views/playlist_edit.jsp";
                RequestDispatcher dispatcher = request.getRequestDispatcher(path);
                dispatcher.forward(request, response);

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "수정할 플레이리스트를 찾을 수 없습니다.");
            }

        } catch (NumberFormatException e) {
            System.err.println("PlaylistEditServlet - 유효하지 않은 플레이리스트 ID 형식: " + request.getParameter("id"));
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 플레이리스트 ID 형식입니다.");
        } catch (Exception e) {
            System.err.println("PlaylistEditServlet (GET) 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("플레이리스트 수정 폼 로딩 중 오류 발생", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int playlistId = Integer.parseInt(request.getParameter("playlistId"));
        String playlistTitle = request.getParameter("playlistTitle");
        String initialSongTitle = request.getParameter("initialSongTitle");
        String initialArtist = request.getParameter("initialArtist");
        String description = request.getParameter("description");

        PlaylistVO updatedPlaylist = new PlaylistVO();
        updatedPlaylist.setPlaylistId(playlistId);
        updatedPlaylist.setPlaylistTitle(playlistTitle);
        updatedPlaylist.setInitialSongTitle(initialSongTitle);
        updatedPlaylist.setInitialArtist(initialArtist);
        updatedPlaylist.setDescription(description); 

        boolean success = playlistDAO.updatePlaylist(updatedPlaylist);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/playlist/view?id=" + playlistId);
        } else {
            System.err.println("플레이리스트 수정 실패! Playlist ID: " + playlistId);
            request.setAttribute("errorMessage", "플레이리스트 수정 중 오류가 발생했습니다.");
            String path = "/WEB-INF/views/error.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
    }

     public void destroy() {
     }
}