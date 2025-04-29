package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.sql.Statement;  // Add this with your other imports

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/project?characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "supra_2006";
    private static final Set<Integer> notifiedTasks = new HashSet<>();

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
	        
	        try (Connection conn = getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
	            
	            // Set user parameters
	            stmt.setString(1, user.getNom());
	            stmt.setString(2, user.getPrenom()); 
	            stmt.setString(3, String.valueOf(user.getSex()));
	            stmt.setString(4, user.getEmail());
	            stmt.setString(5, user.getMotdepass());
	            stmt.setInt(6, user.getCoin());
	            
	            // Execute the insert
	            int affectedRows = stmt.executeUpdate();
	            
	            if (affectedRows > 0) {
	                // Get the auto-generated user ID
	                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
	                    if (generatedKeys.next()) {
	                        int userId = generatedKeys.getInt(1);
	                        
	                        // Send welcome notification
	                        String welcomeMessage = String.format(
	                            "Bienvenue %s %s dans notre application! Nous sommes ravis de vous compter parmi nous.",
	                            user.getPrenom(), user.getNom()
	                        );
	                        
	                        addNotification(userId, "Bienvenue", welcomeMessage);
	                    }
	                }
	                return true;
	            }
	            return false;
	            
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
            addNotification(task.getUserId(), task.getId(), "Tâche ajoutée", 
                    "La tâche '" + task.getTitre() + "' a été ajoutée.");
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
    
    public static int getUserIdByTaskId(int taskId) {
        String query = "SELECT user_id FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, taskId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
        }
        return -1; // ou lancez une exception si préférable
    }
    
    public static boolean deleteTask(int taskId) {
        // D'abord récupérer l'userId avant suppression
        int userId = getUserIdByTaskId(taskId);
        if (userId == -1) {
            System.out.println("Impossible de trouver l'utilisateur pour la tâche ID: " + taskId);
            return false;
        }

        String query = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setInt(1, taskId);
            int rowsDeleted = stmt.executeUpdate();
            
            if (rowsDeleted > 0) {
                addNotification(userId, null, "Tâche supprimée", 
                             "Une tâche a été supprimée de votre liste.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }
        return false;
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
            addNotification(task.getUserId(), task.getId(), "Tâche modifiée", 
                    "La tâche '" + task.getTitre() + "' a été mise à jour.");
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


     public static void addNotification(int userId, String notificationType, String message) {
    	System.out.println("version 2");
        addNotification(userId, null, notificationType, message);
    }

     public static void addNotification(int userId, Integer taskId, String notificationType, String message) {
        // Utilisation d'un bloc try-with-resources pour la connexion
        try (Connection conn = getConnection()) {
            // Désactivation de l'auto-commit pour la transaction
            conn.setAutoCommit(false);
            
            try {
                // 1. Insertion de la nouvelle notification
                String insertQuery = taskId != null ?
                    "INSERT INTO notification (notification, user_id, task_id, message) VALUES (?, ?, ?, ?)" :
                    "INSERT INTO notification (notification, user_id, message) VALUES (?, ?, ?)";
                
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, notificationType);
                    insertStmt.setInt(2, userId);
                    if (taskId != null) {
                        insertStmt.setInt(3, taskId);
                        insertStmt.setString(4, message);
                    } else {
                        insertStmt.setString(3, message);
                    }
                    insertStmt.executeUpdate();
                }

                // 2. Nettoyage des anciennes notifications (version unifiée)
                String cleanupQuery = "DELETE FROM notification WHERE user_id = ? AND id NOT IN (" +
                                    "SELECT id FROM (" +
                                    "  SELECT id FROM notification WHERE user_id = ? " +
                                    "  ORDER BY date_creation DESC LIMIT 12" +
                                    ") AS last_12" +
                                    ")";
                
                try (PreparedStatement cleanupStmt = conn.prepareStatement(cleanupQuery)) {
                    cleanupStmt.setInt(1, userId);
                    cleanupStmt.setInt(2, userId);
                    int deleted = cleanupStmt.executeUpdate();
                    
                    if (deleted > 0) {
                        System.out.println("[NETTOYAGE] " + deleted + " anciennes notifications supprimées");
                    }
                }

                // Validation de la transaction
                conn.commit();
                System.out.println("[SUCCÈS] Notification ajoutée et limite de 12 respectée");

            } catch (SQLException e) {
                // Annulation en cas d'erreur
                conn.rollback();
                System.out.println("[ERREUR] Transaction annulée : " + e.getMessage());
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("[ERREUR] Échec de l'opération : " + e.getMessage());
        }
    }


    public static void sendTaskReminders() {
        System.out.println("[DEBUG] Vérification des tâches non terminées...");

        String query = "SELECT id, user_id, titre, statut, date_limite " +
                       "FROM tasks " +
                       "WHERE statut = 'À faire' " +
                       "AND date_limite <= DATE_ADD(CURDATE(), INTERVAL 3 DAY)";

        class TaskReminder {
            int taskId, userId;
            String titre, statut, dateLimite;
            TaskReminder(int taskId, int userId, String titre, String statut, String dateLimite) {
                this.taskId = taskId;
                this.userId = userId;
                this.titre = titre;
                this.statut = statut;
                this.dateLimite = dateLimite;
            }
        }

        List<TaskReminder> reminders = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reminders.add(new TaskReminder(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("titre"),
                    rs.getString("statut"),
                    rs.getString("date_limite")
                ));
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] Problème lors de la lecture des rappels : " + e.getMessage());
            return;
        }

        // Envoi des notifications après la fermeture du ResultSet
        for (TaskReminder r : reminders) {
            String message = "RAPPEL: \"" + r.titre + "\" (" + r.statut + ") - échéance: " + r.dateLimite;
            addNotification(r.userId, r.taskId, "Rappel", message);
        }

        System.out.println("[SUCCÈS] Tous les rappels ont été envoyés proprement.");
    }

    
    public static void startTaskNotificationScheduler() {
        Timer timer = new Timer();
        // 2 heures = 2 * 60 * 60 * 1000 = 7200000 ms
        final long interval = 7200000;
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    sendTaskReminders();
                    // Nettoyer périodiquement l'historique
                    if (new Random().nextInt(10) == 0) { // ~10% des exécutions
                        notifiedTasks.clear();
                    }
                } catch (Exception e) {
                    System.err.println("Erreur dans le scheduler: " + e.getMessage());
                }
            }
        }, 0, interval); // Démarrer immédiatement puis répéter toutes les 2h
    }

    
    public static List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String query = "SELECT * FROM notification WHERE user_id = ? ORDER BY date_creation DESC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	Notification notif = new Notification(
                		    rs.getInt("id"),
                		    rs.getString("notification"),
                		    rs.getInt("user_id"),
                		    rs.getObject("task_id") != null ? rs.getInt("task_id") : null,  // gestion de null
                		    rs.getString("message"),
                		    rs.getTimestamp("date_creation").toLocalDateTime()  // ✅ conversion correcte
                		);

                    notifications.add(notif);
                }
            }
        } catch (SQLException e) {
            System.err.println("[ERREUR] Impossible de récupérer les notifications : " + e.getMessage());
        }

        return notifications;
    }


    
    public static boolean shareTasksWithUserByEmail(int ownerId, String viewerEmail) {
        String getUserIdQuery = "SELECT id FROM users WHERE email = ?";
        String checkDuplicateQuery = "SELECT 1 FROM task_shares WHERE owner_id = ? AND viewer_id = ?";
        String insertShareQuery = "INSERT INTO task_shares (owner_id, viewer_id) VALUES (?, ?)";

        try (Connection conn = getConnection()) {

            // 1. Vérifie si l'utilisateur avec cet email existe
            int viewerId;
            try (PreparedStatement stmt = conn.prepareStatement(getUserIdQuery)) {
                stmt.setString(1, viewerEmail);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("[ERREUR] Aucun utilisateur avec cet email.");
                        return false;
                    }
                    viewerId = rs.getInt("id");
                }
            }

            // 2. Vérifie les doublons
            try (PreparedStatement stmt = conn.prepareStatement(checkDuplicateQuery)) {
                stmt.setInt(1, ownerId);
                stmt.setInt(2, viewerId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("[INFO] Invitation déjà existante.");
                        return false;
                    }
                }
            }

            // 3. Insertion
            try (PreparedStatement stmt = conn.prepareStatement(insertShareQuery)) {
                stmt.setInt(1, ownerId);
                stmt.setInt(2, viewerId);
                stmt.executeUpdate();
                System.out.println("[SUCCÈS] Utilisateur invité !");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("[ERREUR] " + e.getMessage());
            return false;
        }
    }
    
 // Récupérer les utilisateurs invités par un owner
    public static List<User> getInvitedUsers(int ownerId) {
        List<User> invitedUsers = new ArrayList<>();
        String sql = "SELECT u.* FROM task_shares s JOIN users u ON s.viewer_id = u.id WHERE s.owner_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email")
                );
                invitedUsers.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invitedUsers;
    }

    // Supprimer un partage
    public static boolean removeSharedUser(int ownerId, int viewerId) {
        String sql = "DELETE FROM task_shares WHERE owner_id = ? AND viewer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ownerId);
            stmt.setInt(2, viewerId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
 // Récupérer les propriétaires qui ont partagé avec un viewer
    public static List<User> getOwnersWhoSharedWith(int viewerId) {
        List<User> owners = new ArrayList<>();
        String sql = "SELECT u.* FROM task_shares s JOIN users u ON s.owner_id = u.id WHERE s.viewer_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, viewerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                User user = new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email")
                );
                owners.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owners;
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