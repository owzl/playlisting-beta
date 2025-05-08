-- 데이터베이스 생성
CREATE DATABASE playlisting_beta DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE playlisting_beta;

-- 드롭문
DROP TABLE IF EXISTS `playlist_comments`;
DROP TABLE IF EXISTS `playlist_recommendations`;
DROP TABLE IF EXISTS `playlists`;
DROP TABLE IF EXISTS `members`;

-- members 테이블
CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '회원 고유 번호 (PK, 자동 증가)',
    login_id VARCHAR(50) UNIQUE NOT NULL COMMENT '로그인 시 사용할 ID 또는 사용자명 (중복 불가)',
    nickname VARCHAR(50) UNIQUE NOT NULL COMMENT '사이트에서 표시될 닉네임 (중복 불가)',
    email VARCHAR(100) UNIQUE NOT NULL COMMENT '이메일 주소 (중복 불가)',
    password VARCHAR(255) NULL COMMENT '사이트 자체 비밀번호 (OAuth 사용 시 NULL 허용)',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '사용자 역할 (예: user, admin)',
    oauth_provider VARCHAR(50) NULL COMMENT 'OAuth 로그인 제공자 (예: google, kakao)',
    oauth_uid VARCHAR(255) NULL COMMENT 'OAuth 제공자의 사용자 고유 ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '정보 수정일시',

    INDEX idx_login_id (login_id),
    INDEX idx_oauth (oauth_provider, oauth_uid)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '회원 정보 테이블';


-- playlists 테이블 (게시글)
CREATE TABLE playlists (
    playlist_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '플레이리스트 고유 ID (PK, 자동 증가)',
    playlist_title VARCHAR(255) NOT NULL COMMENT '플레이리스트 제목',
    initial_song_title VARCHAR(100) NOT NULL COMMENT '시작 노래 제목',
    initial_artist VARCHAR(100) NOT NULL COMMENT '시작 노래 아티스트',
    description TEXT NULL COMMENT '플레이리스트 설명 또는 기타 내용', -- 설명 컬럼 추가 반영 (NULL 허용)
    author_id INT NOT NULL COMMENT '작성자 회원 ID (members 테이블 참조)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    view_count INT DEFAULT 0 COMMENT '조회수',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    FOREIGN KEY (author_id) REFERENCES members(member_id) ON DELETE RESTRICT,
    INDEX idx_author_id (author_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '플레이리스트 메인 게시글 테이블';


-- playlist_recommendations 테이블 (답글)
CREATE TABLE playlist_recommendations (
    recommendation_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '추천 노래 고유 ID (PK, 자동 증가)',
    playlist_id INT NOT NULL COMMENT '이 추천곡이 속한 플레이리스트 ID (playlists 테이블 참조)',
    song_title VARCHAR(100) NOT NULL COMMENT '추천 노래 제목',
    artist VARCHAR(100) NOT NULL COMMENT '추천 노래 아티스트',
    author_id INT NOT NULL COMMENT '추천한 회원 ID (members 테이블 참조)',
    recommended_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '추천일시',
    `order` INT NOT NULL DEFAULT 0 COMMENT '플레이리스트 내 추천곡 순서',

    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES members(member_id) ON DELETE RESTRICT,
    INDEX idx_playlist_id (playlist_id),
    INDEX idx_author_id (author_id),
    INDEX idx_playlist_order (playlist_id, `order`)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '플레이리스트 추천 노래 (답글) 테이블';


-- playlist_comments 테이블 (댓글)
CREATE TABLE playlist_comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '댓글 고유 ID (PK, 자동 증가)',
    playlist_id INT NOT NULL COMMENT '이 댓글이 달린 플레이리스트 ID (playlists 테이블 참조)',
    comment_content TEXT NOT NULL COMMENT '댓글 내용 (긴 텍스트 및 이모지 저장)',
    author_id INT NOT NULL COMMENT '작성자 회원 ID (members 테이블 참조)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작성일시',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일시',

    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES members(member_id) ON DELETE RESTRICT,
    INDEX idx_playlist_id (playlist_id),
    INDEX idx_author_id (author_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '플레이리스트 댓글 테이블';

-- 사용자 생성
CREATE USER 'playlisting_beta'@'%' IDENTIFIED BY 'playlisting_beta';
-- 사용 권한 부여
GRANT ALL PRIVILEGES ON playlisting_beta.* TO 'playlisting_beta'@'%';

-- 적용
FLUSH PRIVILEGES;


-- 샘플 데이터 삽입

-- 샘플 멤버
-- member_id는 AUTO_INCREMENT로 자동 생성됩니다. (보통 1부터 시작)
INSERT INTO members (login_id, nickname, email, password, role)
VALUES ('testuser', '테스터1', 'test@example.com', 'dummy_hashed_password', 'user'); -- password는 NOT NULL 이므로 값 필요


-- 샘플 플레이리스트
INSERT INTO playlists (playlist_title, initial_song_title, initial_artist, description, author_id)
VALUES
('퇴근길 힐링 플레이리스트', 'Dynamite', 'BTS', '오늘 하루 수고한 나에게 주는 선물 같은 곡들', 1), -- author_id를 위 멤버의 ID로 설정
('비 오는 날 창가 감성', 'Rainy Days', 'V', '창밖 보면서 듣기 좋은 잔잔한 노래 모음', 1), -- author_id를 위 멤버의 ID로 설정
('카페에서 코딩할 때 듣는 노래', 'Attention', 'NewJeans', '집중 잘 되는 적당한 비트의 곡들', 1); -- author_id를 위 멤버의 ID로 설정

