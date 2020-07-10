package com.example.uniwise;

public class FindFriendsSearch {

    private String profileImage,Username,Department;

    public FindFriendsSearch() {
    }

    public FindFriendsSearch(String profileImage, String username, String department) {
        this.profileImage = profileImage;
        Username = username;
        Department = department;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
