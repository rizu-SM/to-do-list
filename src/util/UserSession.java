package util;

import Model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private String firstName;
    private String lastName;
    private String email;
    private String fullName;
    
    private UserSession() {}
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setUser(User user) {
        this.currentUser = user;
        if (user != null) {
            this.firstName = user.getPrenom();
            this.lastName = user.getNom();
            this.email = user.getEmail();
            this.fullName = user.getPrenom() + " " + user.getNom();
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void setUserInfo(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.fullName = firstName + " " + lastName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
        String[] parts = fullName.split(" ", 2);
        if (parts.length > 0) {
            this.firstName = parts[0];
        }
        if (parts.length > 1) {
            this.lastName = parts[1];
        }
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    
    public void clearSession() {
        currentUser = null;
    }
} 