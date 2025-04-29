package util;

import Model.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;
    
    private UserSession() {}
    
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    public void setUser(User user) {
        this.currentUser = user;
    }
    
    public String getFullName() {
        if (currentUser != null) {
            return currentUser.getNom() + " " + currentUser.getPrenom();
        }
        return "";
    }
    
    public String getEmail() {
        if (currentUser != null) {
            return currentUser.getEmail();
        }
        return "";
    }
    
    public String getFirstName() {
        if (currentUser != null) {
            return currentUser.getPrenom();
        }
        return "";
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void clearSession() {
        currentUser = null;
    }
} 