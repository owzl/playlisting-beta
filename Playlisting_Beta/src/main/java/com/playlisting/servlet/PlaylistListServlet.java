package com.playlisting.servlet;

import com.playlisting.dao.PlaylistDAO;
import com.playlisting.dao.MemberDAO;
import com.playlisting.vo.PlaylistVO;
import com.playlisting.vo.MemberVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList; // 빈 리스트 생성을 위해 추가

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
    private MemberDAO memberDAO;

    private static final int ITEMS_PER_PAGE = 5;
    private static final int PAGES_PER_BLOCK = 5;

    public void init() {
        playlistDAO = new PlaylistDAO();
        memberDAO = new MemberDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	// --- 검색 로직 시작 ---
            String searchTerm = request.getParameter("searchTerm");

            boolean isSearching = (searchTerm != null && !searchTerm.trim().isEmpty());
            if (isSearching) {
                searchTerm = searchTerm.trim();
            } else {
                searchTerm = "";
            }
            // --- 검색 로직 끝 ---

         // --- 페이지네이션 로직 시작 ---
            int currentPage = 1;
            String pageParam = request.getParameter("page");

            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage <= 0) {
                        currentPage = 1;
                    }
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }

            int totalItemCount;
            List<PlaylistVO> playlistList;
            int pageSize = ITEMS_PER_PAGE;
            int startRow = (currentPage - 1) * pageSize;

            if (isSearching) {
                totalItemCount = playlistDAO.searchPlaylistCount(searchTerm);
                playlistList = playlistDAO.searchPlaylistsPaged(searchTerm, startRow, pageSize);
            } else {
                totalItemCount = playlistDAO.getPlaylistCount();
                playlistList = playlistDAO.getPlaylistsPaged(startRow, pageSize);
            }

            int totalPageCount = (int) Math.ceil((double) totalItemCount / pageSize);
             if (totalItemCount > 0 && totalPageCount == 0) totalPageCount = 1;


            if (currentPage > totalPageCount && totalPageCount > 0) {
                 currentPage = totalPageCount;
                 startRow = (currentPage - 1) * pageSize;
                 if (isSearching) {
                      playlistList = playlistDAO.searchPlaylistsPaged(searchTerm, startRow, pageSize);
                 } else {
                      playlistList = playlistDAO.getPlaylistsPaged(startRow, pageSize);
                 }
            } else if (totalPageCount == 0) {
                 currentPage = 1;
                 startRow = 0;
                 playlistList = new ArrayList<>();
            }

            int pageBlockSize = PAGES_PER_BLOCK;
            int currentBlock = (int) Math.ceil((double) currentPage / pageBlockSize);
            int startPageInBlock = (currentBlock - 1) * pageBlockSize + 1;
            int endPageInBlock = Math.min(startPageInBlock + pageBlockSize - 1, totalPageCount);

            boolean hasPrevBlock = startPageInBlock > 1;
            boolean hasNextBlock = endPageInBlock < totalPageCount;
             int prevBlockPage = startPageInBlock - 1;
             int nextBlockPage = endPageInBlock + 1;

            // --- 페이지네이션 로직 끝 ---


            // --- 작성자 닉네임 표시 ---
            Set<Integer> authorIds = new HashSet<>();
            if (playlistList != null) {
                for (PlaylistVO playlist : playlistList) {
                    authorIds.add(playlist.getAuthorId());
                }
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
            // --- 작성자 닉네임 로직 끝 ---


            request.setAttribute("playlistList", playlistList); 
            request.setAttribute("authorNicknames", authorNicknames); 
            request.setAttribute("searchTerm", searchTerm); 

            request.setAttribute("totalItemCount", totalItemCount); 
            request.setAttribute("currentPage", currentPage);      
            request.setAttribute("totalPageCount", totalPageCount); 
            request.setAttribute("startPageInBlock", startPageInBlock);
            request.setAttribute("endPageInBlock", endPageInBlock);   
            request.setAttribute("hasPrevBlock", hasPrevBlock);     
            request.setAttribute("hasNextBlock", hasNextBlock);     
            request.setAttribute("prevBlockPage", prevBlockPage);     
            request.setAttribute("nextBlockPage", nextBlockPage);    


            String path = "/WEB-INF/views/playlist_list.jsp";

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