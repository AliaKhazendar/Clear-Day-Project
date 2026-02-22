package com.example.cleardayapplication.domain.model;

public class User {
    private String userId;
    private String userName;
    private String email;
    private String profileImage;
    private long createAt;

    public User(String userId, String userName, String email, String profileImage, long createAt) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.profileImage = profileImage;
        this.createAt = createAt;
    }
    public User(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }
}
