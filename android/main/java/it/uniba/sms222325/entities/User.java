package it.uniba.sms222325.entities;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String email;
    private Integer bestScore;

    public User() {}

    public User(@NonNull String username, @NonNull String email) {
        this.username = username;
        this.email = email;
        this.bestScore = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Integer getBestScore() {
        return bestScore;
    }

    public Map<String, Object> asMap(){
        Map<String, Object> userAsMap = new HashMap<>();
        userAsMap.put("username", this.username);
        userAsMap.put("email", this.email);
        userAsMap.put("bestScore", this.bestScore);

        return userAsMap;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", bestScore=" + bestScore +
                '}';
    }
}
