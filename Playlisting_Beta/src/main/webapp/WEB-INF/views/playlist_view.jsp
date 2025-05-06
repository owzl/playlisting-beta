<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- JSTL Core 태그 --%>
<%-- TODO: 필요시 JSTL fmt 태그 (날짜 포맷) 사용 선언 --%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%-- 서블릿에서 request.setAttribute("playlist", ...)로 넘겨준 PlaylistVO를 ${requestScope.playlist}로 접근 --%>
<title>${requestScope.playlist.playlistTitle} 상세 보기 - 플레이리스팅</title>
<style>
    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; background-color: #f8f9fa; }
    .container { max-width: 800px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }

    .playlist-details { margin-bottom: 30px; padding-bottom: 20px; border-bottom: 2px solid #007bff; }
    .playlist-title { font-size: 2em; margin-bottom: 10px; color: #333; }
    .playlist-meta { font-size: 0.9em; color: #555; margin-bottom: 10px; }
    .playlist-description { margin-top: 15px; padding-top: 15px; border-top: 1px solid #eee; color: #444; white-space: pre-wrap; } /* 설명 내용 줄바꿈 유지 */

    .section-title { font-size: 1.5em; margin-top: 30px; margin-bottom: 15px; border-bottom: 2px solid #007bff; padding-bottom: 5px; color: #333; }

    .recommendation-item { margin-bottom: 15px; padding: 15px; border: 1px solid #ccc; border-left: 5px solid #007bff; background-color: #e9f5ff; border-radius: 5px; }
    .comment-item { margin-bottom: 15px; padding: 15px; border: 1px solid #ddd; background-color: #f9f9f9; border-radius: 5px; }

    .author-info { font-size: 0.9em; color: #333; font-weight: bold; margin-bottom: 5px;}
    .meta-info { font-size: 0.8em; color: #777; text-align: right; }

    /* 폼 스타일 */
    .form-section { margin-top: 20px; padding: 20px; border: 1px solid #ddd; background-color: #f8f8f8; border-radius: 8px; }
    .form-section h3 { margin-top: 0; color: #333; }
    textarea, input[type="text"] { width: calc(100% - 22px); padding: 10px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px; }
    button { padding: 10px 20px; background-color: #007bff; color: white; border: none; cursor: pointer; border-radius: 5px; }
    button:hover { background-color: #0056b3; }

    .back-link { display: block; margin-top: 20px; text-align: center; color: #007bff; text-decoration: none; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="container">

    <%-- 플레이리스트가 없을 경우 메시지 표시 --%>
    <c:if test="${empty requestScope.playlist}">
        <h1>플레이리스트를 찾을 수 없습니다.</h1>
        <p>요청하신 플레이리스트가 존재하지 않거나 삭제되었습니다.</p>
         <p><a href="${pageContext.request.contextPath}/playlist/list" class="back-link">← 목록으로 돌아가기</a></p>
    </c:if>

    <%-- 플레이리스트가 존재할 경우 상세 내용 표시 --%>
    <c:if test="${not empty requestScope.playlist}">
        <div class="playlist-details">
            <h1 class="playlist-title"><c:out value="${requestScope.playlist.playlistTitle}"/></h1>
            <div class="playlist-meta">
                시작 노래: <strong><c:out value="${requestScope.playlist.initialSongTitle}"/></strong> - <c:out value="${requestScope.playlist.initialArtist}"/><br>
                <%-- TODO: authorId로 MemberDAO를 통해 작성자 닉네임 가져와서 표시 --%>
                <%-- 서블릿에서 전달받은 authorNicknames 맵 사용 --%>
                작성자: ${requestScope.authorNicknames[requestScope.playlist.authorId]} | <%-- <--- 수정된 부분 --%>
                작성일: ${requestScope.playlist.createdAt} | <%-- fmt 태그로 날짜 포맷팅 고려 --%>
                조회수: ${requestScope.playlist.viewCount}
            </div>
            <%-- 설명이 있다면 표시 --%>
            <c:if test="${not empty requestScope.playlist.description}">
                <div class="playlist-description">
                    <p><strong>설명:</strong></p>
                    <p><c:out value="${requestScope.playlist.description}"/></p>
                </div>
            </c:if>
        </div>

        <%-- 수정 삭제 --%>
        <p>
             <a href="${pageContext.request.contextPath}/playlist/edit?id=${requestScope.playlist.playlistId}" class="button-link">수정</a>
             <a href="${pageContext.request.contextPath}/playlist/delete?id=${requestScope.playlist.playlistId}"
                class="button-link delete-button"
                onclick="return confirm('정말로 이 플레이리스트를 삭제하시겠습니까? 관련 추천곡과 댓글도 모두 삭제됩니다.');">삭제</a>
        </p>
        <style> /* 버튼처럼 보이도록 간단한 스타일 추가 */
            .button-link {
                 display: inline-block; /* 링크를 버튼처럼 */
                 padding: 8px 15px;
                 background-color: #ffc107; /* 주황색 계열 */
                 color: #212529; /* 글자색 */
                 text-decoration: none;
                 border-radius: 4px;
                 margin-right: 10px;
                 transition: background-color 0.2s ease-in-out;
            }
            .button-link:hover {
                 background-color: #e0a800; /* 호버 시 더 진한 주황색 */
            }
            .delete-button { /* 삭제 버튼은 빨간색 계열 */
                 background-color: #dc3545;
                 color: white;
            }
            .delete-button:hover {
                 background-color: #c82333;
            }
        </style>


        <%-- 추천 노래 목록 표시 영역 --%>
        <div class="recommendations-section">
            <h2 class="section-title">추천 노래 (${requestScope.recommendationList.size()})</h2>
            <c:if test="${not empty requestScope.recommendationList}">
                <c:forEach var="recommendation" items="${requestScope.recommendationList}">
                    <div class="recommendation-item">
                        <div class="author-info">
                             <%-- TODO: recommendation.authorId로 MemberDAO를 통해 추천자 닉네임 가져와서 표시 --%>
                             <%-- 서블릿에서 전달받은 authorNicknames 맵 사용 --%>
                             추천자: ${requestScope.authorNicknames[recommendation.authorId]} <%-- <--- 수정된 부분 --%>
                         </div>
                        <strong><c:out value="${recommendation.songTitle}"/></strong> - <c:out value="${recommendation.artist}"/><br>
                        <div class="meta-info">
                             추천일: ${recommendation.recommendedAt} | 순서: ${recommendation.order}
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty requestScope.recommendationList}">
                <p class="no-data">아직 추천된 노래가 없습니다. 첫 번째로 추천해주세요!</p>
            </c:if>

            <%-- 추천 노래 추가 폼 (POST 요청은 /recommendation/add 서블릿으로) --%>
             <div class="form-section">
                 <form action="${pageContext.request.contextPath}/recommendation/add" method="post">
                     <input type="hidden" name="playlistId" value="${requestScope.playlist.playlistId}">
                     <h3>이 플레이리스트에 노래 추천하기</h3>
                     노래 제목: <input type="text" name="songTitle" required><br>
                     아티스트: <input type="text" name="artist" required><br>
                     <%-- TODO: 작성자 ID는 로그인된 회원 정보에서 가져와야 합니다. --%>
                     <%-- <input type="hidden" name="authorId" value="로그인된 사용자 ID"> --%>
                     <button type="submit">추천하기</button>
                 </form>
             </div>
        </div>

        <%-- 댓글 목록 표시 영역 --%>
        <div class="comments-section">
            <h2 class="section-title">댓글 (${requestScope.commentList.size()})</h2>
            <c:if test="${not empty requestScope.commentList}">
                <c:forEach var="comment" items="${requestScope.commentList}">
                    <div class="comment-item">
                         <div class="author-info">
                             <%-- TODO: comment.authorId로 MemberDAO를 통해 작성자 닉네임 가져와서 표시 --%>
                             <%-- 서블릿에서 전달받은 authorNicknames 맵 사용 --%>
                             작성자: ${requestScope.authorNicknames[comment.authorId]} <%-- <--- 수정된 부분 (getInt 대신 getId 또는 getAuthorId) --%>
                         </div>
                        <p><c:out value="${comment.commentContent}"/></p>
                        <div class="meta-info">
                            작성일: ${comment.getCreatedAt()}
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty requestScope.commentList}">
                <p class="no-data">아직 댓글이 없습니다. 첫 번째 댓글을 남겨보세요!</p>
            </c:if>

            <%-- 댓글 추가 폼 (POST 요청은 /comment/add 서블릿으로) --%>
             <div class="form-section">
                 <form action="${pageContext.request.contextPath}/comment/add" method="post">
                     <input type="hidden" name="playlistId" value="${requestScope.playlist.playlistId}">
                     <h3>댓글 작성</h3>
                     <%-- TODO: 이모지 댓글 기능 구현 시 여기에 이모지 입력 방식 적용 --%>
                     <textarea name="commentContent" rows="3" required placeholder="댓글을 입력하세요"></textarea><br>
                     <%-- TODO: 작성자 ID는 로그인된 회원 정보에서 가져와야 합니다. --%>
                     <%-- <input type="hidden" name="authorId" value="로그인된 사용자 ID"> --%>
                     <button type="submit">댓글 달기</button>
                 </form>
             </div>
        </div>

    </c:if>

    <%-- 목록으로 돌아가는 링크 --%>
    <p><a href="${pageContext.request.contextPath}/playlist/list" class="back-link">← 목록으로 돌아가기</a></p>

</div>
</body>
</html>