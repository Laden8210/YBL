// BaseResponse.java
package com.example.ybl.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private T data;

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // Helper method to check if response has direct user data (for login)
    public boolean hasDirectUserData() {
        return user != null && token != null;
    }
}