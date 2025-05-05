package com.playlisting.vo;

import java.sql.Timestamp;

public class PlaylistVO {

    private int playlistId;
    private String playlistTitle;
    private String initialSongTitle;
    private String initialArtist;
    private String description;
    private int authorId;       // FK
    private Timestamp createdAt;
    private int viewCount;
    private Timestamp updatedAt;

    // 생성자
    public PlaylistVO() {
    }

    public PlaylistVO(int playlistId, String playlistTitle, String initialSongTitle, String initialArtist, String description, int authorId, Timestamp createdAt, int viewCount, Timestamp updatedAt) {
        this.playlistId = playlistId;
        this.playlistTitle = playlistTitle;
        this.initialSongTitle = initialSongTitle;
        this.initialArtist = initialArtist;
        this.description = description;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
        this.updatedAt = updatedAt;
    }

    // Getter 및 Setter
    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistTitle() {
        return playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getInitialSongTitle() {
        return initialSongTitle;
    }

    public void setInitialSongTitle(String initialSongTitle) {
        this.initialSongTitle = initialSongTitle;
    }

    public String getInitialArtist() {
        return initialArtist;
    }

    public void setInitialArtist(String initialArtist) {
        this.initialArtist = initialArtist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
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
        return "PlaylistVO{" +
               "playlistId=" + playlistId +
               ", playlistTitle='" + playlistTitle + '\'' +
               ", initialSongTitle='" + initialSongTitle + '\'' +
               ", initialArtist='" + initialArtist + '\'' +
               ", description='" + description + '\'' +
               ", authorId=" + authorId +
               ", createdAt=" + createdAt +
               ", viewCount=" + viewCount +
               ", updatedAt=" + updatedAt +
               '}';
    }
}