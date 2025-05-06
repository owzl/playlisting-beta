package com.playlisting.servlet; // 패키지명

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.vo.PlaylistVO;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/playlist/write")
public class PlaylistWriteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PlaylistDAO playlistDAO;

    public void init() {
        playlistDAO = new PlaylistDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/WEB-INF/views/playlist_write.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String playlistTitle = request.getParameter("playlistTitle");
        String initialSongTitle = request.getParameter("initialSongTitle");
        String initialArtist = request.getParameter("initialArtist");
        String description = request.getParameter("description");

        int authorId = 1;

        PlaylistVO newPlaylist = new PlaylistVO();
        newPlaylist.setPlaylistTitle(playlistTitle);
        newPlaylist.setInitialSongTitle(initialSongTitle);
        newPlaylist.setInitialArtist(initialArtist);
        newPlaylist.setDescription(description);
        newPlaylist.setAuthorId(authorId);

        boolean success = playlistDAO.createPlaylist(newPlaylist);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/playlist/list");
        } else {
            System.err.println("플레이리스트 작성 실패!");
            request.setAttribute("errorMessage", "플레이리스트 작성 중 오류가 발생했습니다.");
            String path = "/WEB-INF/views/error.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(path);
            dispatcher.forward(request, response);
        }
    }

     public void destroy() {
     }
}