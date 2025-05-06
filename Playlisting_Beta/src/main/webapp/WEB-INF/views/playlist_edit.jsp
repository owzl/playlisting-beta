<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${requestScope.playlist.playlistTitle} 수정 - 플레이리스팅</title> <%-- 기존 제목 사용 --%>
<style>
     body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; background-color: #f8f9fa; }
    .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    h1 { text-align: center; color: #333; margin-bottom: 30px; }
    label { display: block; margin-bottom: 8px; font-weight: bold; }
    input[type="text"], textarea { width: calc(100% - 22px); padding: 10px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 4px; }
    textarea { resize: vertical; min-height: 100px; }
    button { display: block; width: 100%; padding: 12px; background-color: #ffc107; /* 수정 버튼 색상 */ color: #212529; border: none; cursor: pointer; border-radius: 5px; font-size: 1.1em; }
    button:hover { background-color: #e0a800; }
    .back-link { display: block; text-align: center; margin-top: 20px; color: #007bff; text-decoration: none; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="container">

    <h1>플레이리스트 수정</h1>

    <%-- 수정 폼 --%>
    <%-- action: 수정된 데이터가 전송될 URL (PlaylistEditServlet의 POST 매핑) --%>
    <%-- method: 데이터 전송 방식 (POST) --%>
    <form action="${pageContext.request.contextPath}/playlist/edit" method="post">

        <%-- !!! 수정할 플레이리스트의 ID를 숨겨서 전송해야 합니다. !!! --%>
        <input type="hidden" name="playlistId" value="${requestScope.playlist.playlistId}">

        <label for="playlistTitle">플레이리스트 제목:</label>
        <%-- value 속성에 기존 데이터 채워 넣기 --%>
        <input type="text" id="playlistTitle" name="playlistTitle" value="${requestScope.playlist.playlistTitle}" required><br>

        <label for="initialSongTitle">시작 노래 제목:</label>
        <input type="text" id="initialSongTitle" name="initialSongTitle" value="${requestScope.playlist.initialSongTitle}" required><br>

        <label for="initialArtist">시작 노래 아티스트:</label>
        <input type="text" id="initialArtist" name="initialArtist" value="${requestScope.playlist.initialArtist}" required><br>

        <label for="description">플레이리스트 설명 (선택 사항):</label>
        <%-- textarea의 경우 value 속성 대신 태그 내용으로 기존 데이터 채움 --%>
        <textarea id="description" name="description">${requestScope.playlist.description}</textarea><br>

        <%-- 작성자 ID 필드는 수정 대상이 아니므로 여기에 표시하거나 숨길 수 있습니다. --%>
        <%-- <p>작성자 ID: ${requestScope.playlist.authorId}</p> --%>

        <button type="submit">수정 완료</button>
    </form>

    <%-- 상세 페이지로 돌아가는 링크 (목록 페이지로 돌아가도 됩니다) --%>
    <p><a href="${pageContext.request.contextPath}/playlist/view?id=${requestScope.playlist.playlistId}" class="back-link">← 수정 취소</a></p>

</div>
</body>
</html>