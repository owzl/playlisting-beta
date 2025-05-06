<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- JSTL Core 태그 사용 선언 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>플레이리스팅 - 전체 플레이리스트 목록</title>
<style>
    /* 기본 스타일 */
    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; background-color: #f8f9fa; }
    .container { max-width: 800px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    h1 { text-align: center; color: #333; margin-bottom: 20px; } /* H1 마진 조정 */

    /* 검색 폼 스타일 */
    .search-form {
        text-align: center;
        margin-bottom: 20px; /* 목록과 간격 */
        padding: 15px; /* 내부 여백 */
        border: 1px solid #ddd; /* 테두리 */
        border-radius: 5px; /* 모서리 둥글게 */
        background-color: #f9f9f9; /* 배경색 */
    }
    .search-form input[type="text"] {
        padding: 8px; /* 입력 필드 패딩 */
        border: 1px solid #ccc; /* 입력 필드 테두리 */
        border-radius: 4px; /* 입력 필드 모서리 둥글게 */
        margin-right: 5px; /* 입력 필드와 버튼 간 간격 */
        width: 250px; /* 입력 필드 너비 */
    }
    .search-form button {
        padding: 8px 15px; /* 버튼 패딩 */
        background-color: #007bff; /* 버튼 배경색 */
        color: white; /* 버튼 글자색 */
        border: none; /* 버튼 테두리 없음 */
        border-radius: 4px; /* 버튼 모서리 둥글게 */
        cursor: pointer; /* 마우스 오버 시 커서 변경 */
        transition: background-color 0.2s ease-in-out; /* 배경색 변경 애니메이션 */
    }
    .search-form button:hover {
        background-color: #0056b3; /* 호버 시 배경색 변경 */
    }


    /* 목록 아이템 스타일 */
    .playlist-list-item {
        border: 1px solid #ddd;
        padding: 15px;
        margin-bottom: 15px;
        border-radius: 5px;
        background-color: #fff;
        transition: box-shadow 0.2s ease-in-out;
    }
    .playlist-list-item:hover {
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .item-title {
        font-size: 1.2em;
        font-weight: bold;
        margin-bottom: 5px;
    }

    .item-title a {
        text-decoration: none;
        color: #0056b3;
        display: block;
    }
     .item-title a:hover {
        text-decoration: underline;
    }

    .item-meta {
        font-size: 0.9em;
        color: #555;
    }

    .item-meta span {
        margin-right: 15px;
    }

    .no-data {
        text-align: center;
        font-style: italic;
        color: #888;
        padding: 20px;
        border: 1px dashed #ccc;
        margin-top: 20px;
        border-radius: 5px;
    }

    /* 작성 링크 스타일 */
    .create-link {
        display: block;
        width: 200px;
        margin: 30px auto 10px auto;
        text-align: center;
        padding: 12px 20px;
        background-color: #28a745;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-size: 1.1em;
        transition: background-color 0.2s ease-in-out;
    }
    .create-link:hover {
        background-color: #218838;
    }

     /* 페이지네이션 스타일 */
    .pagination {
         text-align: center;
         margin-top: 20px;
         margin-bottom: 20px; /* 하단 여백 추가 */
    }
    .pagination a {
         display: inline-block;
         padding: 5px 10px;
         margin: 0 3px;
         border: 1px solid #ccc;
         text-decoration: none;
         color: #333;
         border-radius: 3px;
    }
     .pagination a:hover {
         background-color: #eee;
    }
     .pagination strong {
         display: inline-block;
         padding: 5px 10px;
         margin: 0 3px;
         border: 1px solid #007bff;
         color: #007bff;
         font-weight: bold;
         border-radius: 3px;
         background-color: #e9f5ff;
    }
     /* '이전', '다음' 링크 스타일 */
    .pagination .nav-link {
         border: none;
         font-weight: bold;
    }


</style>
</head>
<body>
<div class="container">

    <h1>플레이리스팅 전체 목록</h1>

    <%-- ======================================== --%>
    <%-- 검색 폼 --%>
    <div class="search-form">
        <%-- action: 목록 서블릿으로 GET 방식 전송. method="get" 중요! --%>
        <form action="${pageContext.request.contextPath}/playlist/list" method="get">
            <%-- input hidden type으로 검색어 유지 --%>
            <%-- value에 서블릿에서 전달받은 기존 검색어 ${requestScope.searchTerm} 채워넣기 --%>
            <input type="text" name="searchTerm" placeholder="제목, 노래, 아티스트 검색" value="${requestScope.searchTerm}">
            <button type="submit">검색</button>
            <%-- 검색 결과가 있을 때만 초기화 링크 표시 --%>
            <c:if test="${not empty requestScope.searchTerm}">
                <%-- 검색어 파라미터 없이 목록 서블릿 호출 시 전체 목록 로드 --%>
                <a href="${pageContext.request.contextPath}/playlist/list" style="margin-left: 10px;">검색 결과 초기화</a>
            </c:if>
        </form>
    </div>
    <%-- ======================================== --%>


    <%-- 플레이리스트 목록 표시 --%>
    <c:choose>
        <%-- 목록이 비어있지 않다면 (즉, 데이터가 로드되었다면) --%>
        <c:when test="${not empty requestScope.playlistList}">
            <%-- totalItemCount는 검색 결과 개수일 수도 있으므로, 이게 0일 때 검색 결과 없음을 표시 --%>
            <c:if test="${requestScope.totalItemCount == 0}">
                 <div class="no-data">
                     <c:if test="${not empty requestScope.searchTerm}">
                        "${requestScope.searchTerm}"(으)로 검색된 플레이리스트가 없습니다.
                     </c:if>
                     <c:if test="${empty requestScope.searchTerm}">
                         플레이리스트가 없습니다. 첫 플레이리스트를 작성해보세요!
                     </c:if>
                 </div>
            </c:if>
            <%-- totalItemCount가 0보다 클 때만 목록 반복 표시 --%>
            <c:if test="${requestScope.totalItemCount > 0}">
                <c:forEach var="playlist" items="${requestScope.playlistList}">
                    <div class="playlist-list-item">
                        <div class="item-title">
                            <a href="${pageContext.request.contextPath}/playlist/view?id=${playlist.playlistId}">
                                <c:out value="${playlist.playlistTitle}"/>
                            </a>
                        </div>
                        <div class="item-meta">
                            <%-- 서블릿에서 전달받은 authorNicknames 맵 사용 --%>
                            <span class="item-author">작성자: ${requestScope.authorNicknames[playlist.authorId]}</span>
                            <span class="item-date">작성일: ${playlist.createdAt}</span>
                            <span class="item-views">조회수: ${playlist.viewCount}</span>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </c:when>
         <%-- 목록이 null 또는 비어있고 totalItemCount도 0인 경우 (보통 처음 로드 시 데이터 없을 때) --%>
         <c:otherwise>
             <div class="no-data">
                  <c:if test="${not empty requestScope.searchTerm}">
                    "${requestScope.searchTerm}"(으)로 검색된 플레이리스트가 없습니다.
                 </c:if>
                 <c:if test="${empty requestScope.searchTerm}">
                     플레이리스트가 없습니다. 첫 플레이리스트를 작성해보세요!
                 </c:if>
             </div>
        </c:otherwise>
    </c:choose>


    <%-- 플레이리스트 작성 링크 --%>
    <%-- 검색 결과가 없어도 작성 링크는 항상 표시 --%>
    <a href="${pageContext.request.contextPath}/playlist/write" class="create-link">새 플레이리스트 작성</a>


    <%-- ======================================== --%>
    <%-- 페이지네이션 컨트롤 --%>
    <%-- 전체 (또는 검색 결과) 아이템 수가 0보다 클 때만 페이지네이션 표시 --%>
    <c:if test="${requestScope.totalItemCount > 0}">
        <div class="pagination">
            <%-- '이전' 블록 링크 --%>
            <c:if test="${requestScope.hasPrevBlock}">
                <%-- 페이지네이션 링크 클릭 시 검색어 파라미터를 유지 --%>
                <a href="${pageContext.request.contextPath}/playlist/list?page=${requestScope.prevBlockPage}<c:if test="${not empty requestScope.searchTerm}">&searchTerm=${requestScope.searchTerm}</c:if>" class="nav-link">[이전]</a>
            </c:if>

            <%-- 페이지 번호 링크 --%>
            <%-- startPageInBlock 부터 endPageInBlock 까지 반복 --%>
            <c:forEach var="i" begin="${requestScope.startPageInBlock}" end="${requestScope.endPageInBlock}">
                <c:choose>
                    <c:when test="${i == requestScope.currentPage}">
                        <%-- 현재 페이지는 링크 없이 굵게 표시 --%>
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <%-- 다른 페이지는 링크 표시 --%>
                        <%-- 페이지네이션 링크 클릭 시 검색어 파라미터를 유지 --%>
                        <a href="${pageContext.request.contextPath}/playlist/list?page=${i}<c:if test="${not empty requestScope.searchTerm}">&searchTerm=${requestScope.searchTerm}</c:if>">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <%-- '다음' 블록 링크 --%>
            <c:if test="${requestScope.hasNextBlock}">
                 <%-- 페이지네이션 링크 클릭 시 검색어 파라미터를 유지 --%>
                <a href="${pageContext.request.contextPath}/playlist/list?page=${requestScope.nextBlockPage}<c:if test="${not empty requestScope.searchTerm}">&searchTerm=${requestScope.searchTerm}</c:if>" class="nav-link">[다음]</a>
            </c:if>
        </div>
    </c:if>
    <%-- ======================================== --%>


</div>
</body>
</html>