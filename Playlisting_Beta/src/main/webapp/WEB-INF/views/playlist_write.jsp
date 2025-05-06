<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새 플레이리스트 작성 - 플레이리스팅</title>
<style>
     body { font-family: Arial, sans-serif; line-height: 1.6; margin: 20px; background-color: #f8f9fa; }
    .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    h1 { text-align: center; color: #333; margin-bottom: 30px; }
    label { display: block; margin-bottom: 8px; font-weight: bold; }
    input[type="text"], textarea { width: calc(100% - 22px); padding: 10px; margin-bottom: 15px; border: 1px solid #ccc; border-radius: 4px; }
    textarea { resize: vertical; min-height: 100px; }
    button { display: block; width: 100%; padding: 12px; background-color: #28a745; color: white; border: none; cursor: pointer; border-radius: 5px; font-size: 1.1em; }
    button:hover { background-color: #218838; }
    .back-link { display: block; text-align: center; margin-top: 20px; color: #007bff; text-decoration: none; }
    .back-link:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="container">

    <h1>새 플레이리스트 작성</h1>

    <%-- 플레이리스트 작성 폼 --%>
    <%-- action: 폼 데이터가 전송될 URL (PlaylistWriteServlet의 POST 매핑) --%>
    <%-- method: 데이터 전송 방식 (POST) --%>
    <form action="${pageContext.request.contextPath}/playlist/write" method="post">
        <label for="playlistTitle">플레이리스트 제목:</label>
        <input type="text" id="playlistTitle" name="playlistTitle" required><br> <%-- required: 필수 입력 --%>

        <label for="initialSongTitle">시작 노래 제목:</label>
        <input type="text" id="initialSongTitle" name="initialSongTitle" required><br>

        <label for="initialArtist">시작 노래 아티스트:</label>
        <input type="text" id="initialArtist" name="initialArtist" required><br>

        <label for="description">플레이리스트 설명 (선택 사항):</label>
        <textarea id="description" name="description"></textarea><br> <%-- textarea는 NULL 허용 --%>

        <%-- TODO: 작성자 ID 필드는 로그인 기능 구현 후 숨겨진 필드로 처리해야 합니다. --%>
        <%-- <input type="hidden" name="authorId" value="로그인된 사용자 ID"> --%>

        <button type="submit">작성 완료</button>
    </form>

    <p><a href="${pageContext.request.contextPath}/playlist/list" class="back-link">← 목록으로 돌아가기</a></p>

</div>
</body>
</html>