<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%-- JSTL Core 태그 사용 선언 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>플레이리스팅 - 전체 플레이리스트 목록</title>
<style>
    body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; background-color: #f8f9fa; }
    .container { max-width: 800px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    h1 { text-align: center; color: #333; margin-bottom: 30px; }

    /* 목록 아이템 스타일 */
    .playlist-list-item {
        border: 1px solid #ddd;
        padding: 15px;
        margin-bottom: 15px;
        border-radius: 5px;
        background-color: #fff;
        transition: box-shadow 0.2s ease-in-out; /* 마우스 오버 효과 */
    }
    .playlist-list-item:hover {
        box-shadow: 0 2px 8px rgba(0,0,0,0.1); /* 마우스 오버 시 그림자 효과 */
    }

    .item-title {
        font-size: 1.2em;
        font-weight: bold;
        margin-bottom: 5px;
    }

    .item-title a {
        text-decoration: none;
        color: #0056b3; /* 링크 색상 */
        display: block; /* 제목 영역 전체를 클릭 가능하도록 */
    }
     .item-title a:hover {
        text-decoration: underline;
    }

    .item-meta {
        font-size: 0.9em;
        color: #555;
        /* float: right; */ /* 메타 정보를 오른쪽에 배치하고 싶다면 사용 */
    }

    .item-meta span {
        margin-right: 15px; /* 메타 정보 항목 간 간격 */
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

    .create-link {
        display: block;
        width: 200px; /* 버튼 너비 */
        margin: 30px auto 10px auto; /* 위 마진 더 주고 아래 마진 줄임 */
        text-align: center;
        padding: 12px 20px; /* 패딩 키움 */
        background-color: #28a745;
        color: white;
        text-decoration: none;
        border-radius: 5px;
        font-size: 1.1em; /* 글자 크기 키움 */
        transition: background-color 0.2s ease-in-out; /* 색상 변경 애니메이션 */
    }
    .create-link:hover {
        background-color: #218838;
    }

     /* 메타 정보가 오른쪽에 Float 되었을 때 레이아웃 깨짐 방지 (Float 사용 시 필요) */
     /* .clearfix::after {
        content: "";
        clear: both;
        display: table;
    } */

</style>
</head>
<body>
<div class="container">

    <h1>플레이리스팅 전체 목록</h1>

    <%-- TODO: 검색 폼 및 페이지네이션 영역 추가 예정 --%>

    <%-- 플레이리스트 목록 표시 --%>
    <c:choose>
        <%-- 목록이 비어있지 않다면 반복하여 표시 --%>
        <c:when test="${not empty requestScope.playlistList}">
            <c:forEach var="playlist" items="${requestScope.playlistList}">
                <div class="playlist-list-item"> <%-- div로 각 목록 아이템 표현 --%>
                    <div class="item-title">
                        <%-- 플레이리스트 제목을 클릭하면 상세 페이지로 이동하는 링크 --%>
                        <a href="${pageContext.request.contextPath}/playlist/view?id=${playlist.playlistId}">
                            <c:out value="${playlist.playlistTitle}"/> <%-- 제목 표시 --%>
                        </a>
                    </div>
                    <div class="item-meta">
                         <%-- TODO: author_id로 MemberDAO를 통해 작성자의 닉네임을 가져와서 표시해야 합니다. --%>
                        <span class="item-author">작성자: ${playlist.authorId}</span>
                        <span class="item-date">작성일: ${playlist.createdAt}</span>
                        <span class="item-views">조회수: ${playlist.viewCount}</span>
                        <%-- ID는 숨기거나 필요한 경우만 표시 --%>
                         <%-- <span class="item-id">ID: ${playlist.playlistId}</span> --%>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <%-- 목록이 비어있다면 메시지 표시 --%>
        <c:otherwise>
             <div class="no-data">
                플레이리스트가 없습니다. 첫 플레이리스트를 작성해보세요!
             </div>
        </c:otherwise>
    </c:choose>


    <%-- 플레이리스트 작성 링크 --%>
    <a href="${pageContext.request.contextPath}/playlist/write" class="create-link">새 플레이리스트 작성</a>


</div>
</body>
</html>