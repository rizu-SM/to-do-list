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
        if (conn == null || isConnectionClosed(conn)) { // pour vérify si isConnectionClosed(conn) 
            try {
                System.out.println("Établissement de la connexion...");
                Class.forName("com.mysql.jdbc.Driver");//Cette ligne charge le driver JDBC nécessaire pour connecter Java à une base de données MySQL.
                conn = DriverManager.getConnection(URL, USER, PASSWORD);//Établissement de la connexion avec la base de données
                System.out.println("Connexion réussie !");
            } catch (ClassNotFoundException e) {
                System.out.println("Driver non trouvé : " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erreur de connexion : " + e.getMessage());
            }
        }
        return conn;
    }
    
    //verifié  la connextion a base de donnée  
    private static boolean isConnectionClosed(Connection conn) {
        try {
            return conn == null || conn.isClosed();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la connexion : " + e.getMessage());
            return true;
        }
    }
    
	 // Méthode pour fermer la connexion (à appeler à la fin de l'application)
    //pour éviter plusieurs problemes liés à la gestion des ressources
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
        //c est un requette sql
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
        	//ouvre la connexion a la base de donner avec getConnection et evit sql injection utilisant prepareStatement (concaténation de string)+permet dexecuter la requette sql 
            stmt.setString(1, user.getNom());//remplace les parametres par les valeur real
            stmt.setString(2, user.getPrenom()); 
            stmt.setString(3, String.valueOf(user.getSex()));
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getMotdepass());
            stmt.setInt(6, user.getCoin());
            stmt.executeUpdate(); //execute la requet sql 
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
            
            //Quand tu exécutes stmt.executeQuery() on obtient tableau contenant les resultat de la requet sql
            //si rs.next return true ca veut dire  Il y a une ligne dans le résultat, donc l’utilisateur existe dans la base de données.
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
        String query = "INSERT INTO tasks (user_id, titre, description, date_limite, statut, priorite, categorie) VALUES (?, ?, ?, ?, ?, ? ,?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getTitre());
            stmt.setString(3, task.getDescription());
            stmt.setDate(4, java.sql.Date.valueOf(task.getDateLimite()));
            stmt.setString(5, task.getStatut());
            stmt.setString(6, task.getPriorite());
            stmt.setString(7, task.getCategorie());
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
                        rs.getString("priorite"),
                        rs.getString("categorie")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des tâches : " + e.getMessage());
        }
        return tasks;
    }
    
    public static boolean deleteTask(int taskId) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) //Prepares the DELETE query for execution
        {
            stmt.setInt(1, taskId);
            int rowsAffected = stmt.executeUpdate();// Execute Delete Query , normalement 1
            return rowsAffected > 0; // Retourne true si une tâche a été supprimée
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la tâche : " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateTask(Task task) {
        String query = "UPDATE tasks SET titre = ?, description = ?, date_limite = ?, statut = ?, priorite = ?, categorie = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
        	stmt.setString(1, task.getTitre());
        	stmt.setString(2, task.getDescription());
        	stmt.setDate(3, java.sql.Date.valueOf(task.getDateLimite()));
        	stmt.setString(4, task.getStatut());
        	stmt.setString(5, task.getPriorite());
        	stmt.setString(6, task.getCategorie()); // Correct ici
        	stmt.setInt(7, task.getId()); // Correct ici

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Retourne true si la tâche a été mise à jour
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la tâche : " + e.getMessage());
            return false;
        }
    }
    
    public static List<Task> getTasksByUserIdSortedByPriority(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ? ORDER BY FIELD(priorite, 'Haute', 'Moyenne', 'Faible')";

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
                        rs.getString("priorite"),
                        rs.getString("categorie")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des tâches triées : " + e.getMessage());
        }
        return tasks;
    }
    
    public static List<Task> getTasksByCategory(int userId, String category) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ? AND categorie = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_limite").toLocalDate(),
                        rs.getString("statut"),
                        rs.getString("priorite"),
                        rs.getString("categorie")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return tasks;
    }
    
    public static List<Task> getTasksSortedByDate(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ? ORDER BY date_limite ASC"; // Tri par date croissante

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
                        rs.getString("priorite"),
                        rs.getString("categorie")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println(" Erreur SQL : " + e.getMessage());
        }
        return tasks;
    }
    
    public static List<Task> getTasksByStatus(int userId, String status) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ? AND statut = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, status);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Task task = new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_limite").toLocalDate(),
                        rs.getString("statut"),
                        rs.getString("priorite"),
                        rs.getString("categorie")
                    );
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return tasks;
    }

    
    public static boolean addNote(Note note) {
        String sql = "INSERT INTO notes (user_id, titre, description) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Utilisation de 'sql' au lieu de 'query'
            
            stmt.setInt(1, note.getUserId());
            stmt.setString(2, note.getTitre()); // Correction de l'ordre des paramètres
            stmt.setString(3, note.getDescription());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la note : " + e.getMessage());
            return false;
        }
    }

    
 // Récupérer les notes d’un utilisateur
    public static List<Note> getNotesByUserId(int userId) {
        List<Note> notes = new ArrayList<>();
        String query = "SELECT * FROM notes WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Note note = new Note(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("titre"),
                        rs.getString("description")

                );
                notes.add(note);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des notes : " + e.getMessage());
        }
        return notes;
    }
    public static boolean updateNote(Note note) {
        String query = "UPDATE notes SET titre = ?, description = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, note.getTitre());
            stmt.setString(2, note.getDescription());
            stmt.setInt(3, note.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la note : " + e.getMessage());
            return false;
        }
    }
    public static boolean deleteNote(int id) {
        String query = "DELETE FROM notes WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la note : " + e.getMessage());
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