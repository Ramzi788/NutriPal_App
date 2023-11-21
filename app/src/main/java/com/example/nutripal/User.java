package com.example.nutripal;
import org.mindrot.jbcrypt.BCrypt;

public class User {
    private String username;
    private String email;
    private String hashedPassword;

    public User() {}
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.hashedPassword = hashPassword(password);
    }

    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword){
        this.hashedPassword = hashedPassword;
    }
}

