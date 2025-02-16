package Model;

import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.SecurityUtil;
import util.ValidationUtil;
import java.sql.ResultSet;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/project?characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "supra_2006";

    private static Connection conn = null;
    //Une classe fournie par JDBC pour établir une connexion avec une base de données.

    // Méthode pour établir la connexion à la base de données
    public static Connection getConnection() {
        if (conn == null) {
            try {
                System.out.println("Vérification du driver...");
                Class.forName("com.mysql.jdbc.Driver"); //forname : charge une classe à l'exécution 
                conn = DriverManager.getConnection(URL, USER, PASSWORD);//Établir une connexion
                System.out.println("Connexion réussie !");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver non trouvé : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return conn;
    }
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Méthode pour valider l'email
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    // Méthode pour créer un nouvel utilisateur
    public static boolean createUser(User user) {
    	if (!ValidationUtil.isValidEmail(user.getEmail())) {
            System.out.println("Email invalide : " + user.getEmail());
            return false;
        }
    	String hashedPassword = SecurityUtil.hashSHA256(user.getMotdepass());
        String query = "INSERT INTO users (nom, prenom, sex, email, motdepass) VALUES (?, ?, ?, ?, ?)";//Cette ligne définit une requête SQL paramétrée pour insérer des données dans une table 
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	//evite sql injection , car les paramètres ? ne sont pas directement concaténés dans la requête
            stmt.setString(1, user.getNom());//ajout des valeur a ? avec l objet quon avait
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, user.getSex());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, hashedPassword);
            stmt.executeUpdate();
            ////stmt est un objet de type PreparedStatement, utilisé pour exécuter des requêtes SQL préparées.
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }
    
}