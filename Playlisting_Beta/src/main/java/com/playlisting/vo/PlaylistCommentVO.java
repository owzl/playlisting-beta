package com.playlisting.vo;

import java.sql.Timestamp;

public class PlaylistCommentVO {

    private int commentId;
    private int playlistId;     // FK
    private String commentContent; // 이모지로
    private int authorId;       // FK
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 생성자
    public PlaylistCommentVO() {
    }

    public PlaylistCommentVO(int commentId, int playlistId, String commentContent, int authorId, Timestamp createdAt, Timestamp updatedAt) {
        this.commentId = commentId;
        this.playlistId = playlistId;
        this.commentContent = commentContent;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter 및 Setter
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // toString (디버깅용)
    @Override
    public String toString() {
        return "PlaylistCommentVO{" +
               "commentId=" + commentId +
               ", playlistId=" + playlistId +
               ", commentContent='" + commentContent + '\'' +
               ", authorId=" + authorId +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}