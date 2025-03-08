package Controller;

import Model.User;
import util.SecurityUtil;
import util.ValidationUtil;
import Model.DatabaseManager;


public class AuthController {
    private static User loggedInUser = null; // Stocker l'utilisateur connecté

    // Méthode pour connecter un utilisateur
    public static User login(String email, String password) {
    	String hashedPassword = SecurityUtil.hashSHA256(password);
        loggedInUser = DatabaseManager.login(email, hashedPassword);
        return loggedInUser;
    }

    // Méthode pour inscrire un nouvel utilisateur
    public boolean signup(String nom, String prenom, char sex, String email, String motdepass) {
        if (!ValidationUtil.isValidEmail(email)) {
            System.out.println("Email invalide : " + email);
            return false;
        }

        String hashedPassword = SecurityUtil.hashSHA256(motdepass);
        User user = new User(0, nom, prenom, sex, email, hashedPassword, 0); // 0 coins initialement
        return DatabaseManager.createUser(user);
    }

    // Méthode pour déconnecter un utilisateur (jjust faire user=null)
    public void logout() {
        if (loggedInUser != null) {
            System.out.println("Déconnexion en cours...");
            loggedInUser = null; // Réinitialiser la session utilisateur
            System.out.println("Déconnecté avec succès !");
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }

    // Méthode pour vérifier si un utilisateur est connecté
    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    // Méthode pour obtenir l'utilisateur connecté
    public User getLoggedInUser() {
        return loggedInUser;
    }
}