package com.playlisting.vo;

import java.sql.Timestamp;

public class SongRecommendationVO {

    private int recommendationId;
    private int playlistId;       // FK
    private String songTitle;
    private String artist;
    private int authorId;         // FK
    private Timestamp recommendedAt;
    private int order;

    // 생성자
    public SongRecommendationVO() {
    }

    public SongRecommendationVO(int recommendationId, int playlistId, String songTitle, String artist, int authorId, Timestamp recommendedAt, int order) {
        this.recommendationId = recommendationId;
        this.playlistId = playlistId;
        this.songTitle = songTitle;
        this.artist = artist;
        this.authorId = authorId;
        this.recommendedAt = recommendedAt;
        this.order = order;
    }

    // Getter 및 Setter
    public int getRecommendationId() {
        return recommendationId;
    }

    public void setRecommendationId(int recommendationId) {
        this.recommendationId = recommendationId;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Timestamp getRecommendedAt() {
        return recommendedAt;
    }

    public void setRecommendedAt(Timestamp recommendedAt) {
        this.recommendedAt = recommendedAt;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    // toString (디버깅용)
    @Override
    public String toString() {
        return "SongRecommendationVO{" +
               "recommendationId=" + recommendationId +
               ", playlistId=" + playlistId +
               ", songTitle='" + songTitle + '\'' +
               ", artist='" + artist + '\'' +
               ", authorId=" + authorId +
               ", recommendedAt=" + recommendedAt +
               ", order=" + order +
               '}';
    }
}