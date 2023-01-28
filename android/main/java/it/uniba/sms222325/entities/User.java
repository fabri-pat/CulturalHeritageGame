package it.uniba.sms222325.entities;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public User(@NonNull String username, @NonNull String email, @NonNull Integer bestScore) {
        this.username = username;
        this.email = email;
        this.bestScore = bestScore;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) || email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}
