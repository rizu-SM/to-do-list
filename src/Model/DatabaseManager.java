package Model;

import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.SecurityUtil;
import util.ValidationUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
    public static User login(String email, String password) {
        // Hacher le mot de passe saisi
        String hashedPassword = SecurityUtil.hashSHA256(password);

        String query = "SELECT * FROM users WHERE email = ? AND motdepass = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	//getConnection() retourne une connexion active avec la base.
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            ResultSet rs = stmt.executeQuery();
            //executeQuery() exécute la requête et retourne le résultat dans un ResultSet.

            // Si un utilisateur correspondant est trouvé
            if (rs.next()) {
            	//Si un enregistrement est trouvé dans la base :
                return new User(
                		//Retourne un objet User 
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("sex"),
                    rs.getString("email"),
                    rs.getString("motdepass") // Retourne le mot de passe haché (optionnel)
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
        }
        return null; // Aucun utilisateur trouvé
    }
    
    //ajout les task-----------------------------------------------------------------------------------------------------------
    
    public static boolean addTask(Task task) {
        String query = "INSERT INTO tasks (user_id, titre, description, date_limite, statut, priorite) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitre());
            stmt.setString(3, task.getDescription());
            stmt.setDate(4, java.sql.Date.valueOf(task.getDateLimite()));
            stmt.setString(5, task.getStatut());
            stmt.setString(6, task.getPriorite());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la tâche : " + e.getMessage());
            return false;
        }
    }
    
    //method pour avait les task de l utilisateur avec son id
    
    public static List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("date_limite").toLocalDate(),
                    rs.getString("statut"),
                    rs.getString("priorite")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des tâches : " + e.getMessage());
        }
        return tasks;
    }
    
    
    
}