package com.playlisting.vo;

import java.sql.Timestamp;

public class MemberVO {

    private int memberId;
    private String loginId;
    private String nickname;
    private String email;
    private String password;
    private String role;
    private String oauthProvider;
    private String oauthUid;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 생성자
    public MemberVO() {
    }

    public MemberVO(int memberId, String loginId, String nickname, String email, String password, String role, String oauthProvider, String oauthUid, Timestamp createdAt, Timestamp updatedAt) {
        this.memberId = memberId;
        this.loginId = loginId;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.oauthProvider = oauthProvider;
        this.oauthUid = oauthUid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter 및 Setter
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getOauthUid() {
        return oauthUid;
    }

    public void setOauthUid(String oauthUid) {
        this.oauthUid = oauthUid;
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
        return "MemberVO{" +
               "memberId=" + memberId +
               ", loginId='" + loginId + '\'' +
               ", nickname='" + nickname + '\'' +
               ", email='" + email + '\'' +
               ", password='" + (password != null ? "[PROTECTED]" : null) + '\'' + // 비밀번호는 출력 시 가림
               ", role='" + role + '\'' +
               ", oauthProvider='" + oauthProvider + '\'' +
               ", oauthUid='" + oauthUid + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}