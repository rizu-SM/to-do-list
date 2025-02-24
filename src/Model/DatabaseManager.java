package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/project?characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "supra_2006";

    private static Connection conn = null;

    // Méthode pour établir la connexion à la base de données
    public static Connection getConnection() {
        if (conn == null || isConnectionClosed(conn)) {
            try {
                System.out.println("Établissement de la connexion...");
                Class.forName("com.mysql.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion réussie !");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver non trouvé : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return conn;
    }
    
    private static boolean isConnectionClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la connexion : " + e.getMessage());
            return true;
        }
    }
    
	 // Méthode pour fermer la connexion (à appeler à la fin de l'application)
	    public static void closeConnection() {
	        if (conn != null) {
	            try {
	                conn.close();
	                System.out.println("Connexion fermée.");
	            } catch (SQLException e) {
	                System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
	            }
	        }
	    }

    // Méthode pour créer un nouvel utilisateur
    public static boolean createUser(User user) {
        String query = "INSERT INTO users (nom, prenom, sex, email, motdepass, coin) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getNom());
            stmt.setString(2, user.getPrenom());
            stmt.setString(3, String.valueOf(user.getSex()));
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getMotdepass());
            stmt.setInt(6, user.getCoin());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la création de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour connecter un utilisateur
    public static User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND motdepass = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("sex").charAt(0),
                    rs.getString("email"),
                    rs.getString("motdepass"),
                    rs.getInt("coin")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
        }
        return null;
    }

    // Méthode pour ajouter une tâche
    public static boolean addTask(Task task) {
        String query = "INSERT INTO tasks (user_id, titre, description, date_limite, statut, priorite) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
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

    // Méthode pour récupérer les tâches d'un utilisateur
    public static List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des tâches : " + e.getMessage());
        }
        return tasks;
    }

    // Méthode pour marquer une tâche comme terminée et mettre à jour les coins
    public static boolean terminerTacheEtMAJDB(Task task, User user) {
        int userId = getUserIdByEmail(user.getEmail());
        if (userId == -1) {
            System.out.println("Erreur : Aucun utilisateur trouvé avec cet email.");
            return false;
        }

        try (Connection conn = getConnection()) {
            // Marquer la tâche comme terminée
            String updateTask = "UPDATE tasks SET statut = 'Terminé' WHERE id = ?";
            PreparedStatement stmtTask = conn.prepareStatement(updateTask);
            stmtTask.setInt(1, task.getId());
            stmtTask.executeUpdate();

            // Mettre à jour les coins de l'utilisateur
            String updateUser = "UPDATE users SET coin = ? WHERE id = ?";
            PreparedStatement stmtUser = conn.prepareStatement(updateUser);
            stmtUser.setInt(1, user.getCoin());
            stmtUser.setInt(2, userId);
            stmtUser.executeUpdate();

            System.out.println("Tâche terminée, " + task.getCoinsForTask() + " coins ajoutés !");
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la tâche : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour récupérer l'ID d'un utilisateur par son email
    public static int getUserIdByEmail(String email) {
        String query = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'ID utilisateur : " + e.getMessage());
        }
        return -1;
    }
}