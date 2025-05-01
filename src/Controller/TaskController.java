package Controller;

import Model.DatabaseManager;
import Model.Task;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class TaskController {
    // Méthode pour ajouter task
    public boolean addTask(Task task) {
        return DatabaseManager.addTask(task);
    }

    public boolean shareTasksWithUserByEmail(int ownerId, String viewerEmail) {
        // Appel à la méthode de DatabaseManager pour gérer la logique de partage
        return DatabaseManager.shareTasksWithUserByEmail(ownerId, viewerEmail);
    }
    
    
    
    // Méthode pour récupérer les tasks d'un utilisateur
    public List<Task> getTasksByUserId(int userId) {
        return DatabaseManager.getTasksByUserId(userId);
    }
    
    
    public List<Task> getTasksByUserIdSortedByPriority(int userId) {
        return DatabaseManager.getTasksByUserIdSortedByPriority(userId); 
    }
    
    public List<Task> getTasksByCategory(int userId, String category) {
        return DatabaseManager.getTasksByCategory(userId, category);
    }
    
    public List<Task> getTasksSortedByDate(int userId) {
        return DatabaseManager.getTasksSortedByDate(userId);
    }
    
    public List<Task> getTasksByStatus(int userId, String status) {
        return DatabaseManager.getTasksByStatus(userId, status);
    }


    
    
    
    // Méthode pour marquer une tâche comme terminée et attribuer des coins
    public boolean completeTask(Task task, User user) {
        return terminerTacheEtMAJDB(task, user);
    }
    
    
    
    
    public static boolean terminerTacheEtMAJDB(Task task, User user) {
        if (task == null || user == null) {
            System.out.println("Erreur : Tâche ou utilisateur invalide.");
            return false;
        }
        
        //about this try catch version :  handling errors AND automatically closing resources 
        //closing resources is the think uniq in this forme
        try (Connection conn = DatabaseManager.getConnection()) {
            // Vérifier si la tâche est déjà terminée
            String checkStatusQuery = "SELECT statut FROM tasks WHERE id = ?";
            try (PreparedStatement stmtCheckStatus = conn.prepareStatement(checkStatusQuery)) {
                stmtCheckStatus.setInt(1, task.getId());
                System.out.println("Exécution de la requête : SELECT statut FROM tasks WHERE id = " + task.getId()); // Debugging
                ResultSet rs = stmtCheckStatus.executeQuery();
                if (rs.next()) {
                    System.out.println("verify"); 
                    String currentStatus = rs.getString("statut");
                    System.out.println("Statut actuel de la tâche : " + currentStatus);
                    if ("Terminé".equalsIgnoreCase(currentStatus)) {
                        System.out.println("Cette tâche est déjà terminée.");
                        return false; // La tâche est déjà terminée, on ne fait rien
                    }
                } else {
                    System.out.println("Aucune tâche trouvée avec l'ID : " + task.getId()); // Debugging
                    return false;
                }
            }

            // Si la tâche n'est pas encore terminée, on la marque comme terminée et on ajoute les coins
            conn.setAutoCommit(false); //When we use this, changes are not saved until you call conn.commit();
            //This allows multiple operations to be grouped into a single transaction.
            //qo in our case task status and user coins happen together

            try (PreparedStatement stmtTask = conn.prepareStatement("UPDATE tasks SET statut = 'Terminé' WHERE id = ?");
                 PreparedStatement stmtUser = conn.prepareStatement("UPDATE users SET coin = ? WHERE id = ?")) {

                // Mettre à jour le statut de la tâche
                stmtTask.setInt(1, task.getId());
                stmtTask.executeUpdate();

                // Mettre à jour les coins de l'utilisateur
                int newCoinBalance = user.getCoin() + task.getCoinsForTask();
                stmtUser.setInt(1, newCoinBalance);
                stmtUser.setInt(2, user.getId());
                stmtUser.executeUpdate();

                conn.commit(); // saving changes and valide transaction
                System.out.println("Tâche terminée, " + task.getCoinsForTask() + " coins ajoutés !");
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Annuler la transaction en cas d'erreur
                System.out.println("Erreur SQL : " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }
    
    
    
    public boolean deleteTask(int taskId) {
        return DatabaseManager.deleteTask(taskId);
    }
    
    public List<User> getInvitedUsers(int ownerId) {
        return DatabaseManager.getInvitedUsers(ownerId);
    }

    public boolean removeInvitationByEmail(int ownerId, String email) {
        // Récupérer le viewer à partir de l'email
    	int viewerId = 0;
         viewerId = DatabaseManager.getUserIdByEmail(email);
        if (viewerId != 0) {
            return DatabaseManager.removeSharedUser(ownerId, viewerId);
        }
        return false; // Viewer non trouvé
    }


    public List<User> getOwnersWhoSharedWith(int viewerId) {
        return DatabaseManager.getOwnersWhoSharedWith(viewerId);
    }

    
    
    
    public boolean editTask(Task task) {
        // verify sif task is Terminer or no 
        if ("Terminé".equalsIgnoreCase(task.getStatut())) {
            System.out.println("Cette tâche est déjà terminée et ne peut pas être modifiée.");
            return false;
        }

        // Mettre à jour la tâche dans la base de données
        return DatabaseManager.updateTask(task);
    }

    public int getCoinCountForUser(int userId) {
        return DatabaseManager.getCoinCountForUser(userId); // Delegate to DatabaseManager
    }
}