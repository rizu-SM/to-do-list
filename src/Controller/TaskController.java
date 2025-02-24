package Controller;

import Model.Task;
import Model.User;
import Model.DatabaseManager;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class TaskController {
    // Méthode pour ajouter une tâche
    public boolean addTask(Task task) {
        return DatabaseManager.addTask(task);
    }

    // Méthode pour récupérer les tâches d'un utilisateur
    public List<Task> getTasksByUserId(int userId) {
        return DatabaseManager.getTasksByUserId(userId);
    }

    // Méthode pour marquer une tâche comme terminée et attribuer des coins
    public boolean completeTask(Task task, User user) {
        return DatabaseManager.terminerTacheEtMAJDB(task, user);
    }
    
    public static boolean terminerTacheEtMAJDB(Task task, User user) {
        if (task == null || user == null) {
            System.out.println("Erreur : Tâche ou utilisateur invalide.");
            return false;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            // Vérifier si la tâche est déjà terminée
            String checkStatusQuery = "SELECT statut FROM tasks WHERE id = ?";
            try (PreparedStatement stmtCheckStatus = conn.prepareStatement(checkStatusQuery)) {
                stmtCheckStatus.setInt(1, task.getId());
                ResultSet rs = stmtCheckStatus.executeQuery();
                if (rs.next()) {
                    String currentStatus = rs.getString("statut");
                    if ("Terminé".equalsIgnoreCase(currentStatus)) {
                        System.out.println("Cette tâche est déjà terminée.");
                        return false; // La tâche est déjà terminée
                    }
                } else {
                    System.out.println("Aucune tâche trouvée avec l'ID : " + task.getId());
                    return false;
                }
            }

            // Si la tâche n'est pas terminée, mise à jour de la base de données
            conn.setAutoCommit(false); // Démarrer la transaction

            try {
                // Mise à jour du statut de la tâche
                String updateTaskQuery = "UPDATE tasks SET statut = ? WHERE id = ?";
                try (PreparedStatement stmtTask = conn.prepareStatement(updateTaskQuery)) {
                    stmtTask.setString(1, "Terminé");
                    stmtTask.setInt(2, task.getId());
                    stmtTask.executeUpdate();
                }

                // Mise à jour des coins de l'utilisateur
                String updateUserQuery = "UPDATE users SET coin = coin + ? WHERE id = ?";
                try (PreparedStatement stmtUser = conn.prepareStatement(updateUserQuery)) {
                    stmtUser.setInt(1, task.getCoinsForTask()); // Ajoute les coins de la tâche
                    stmtUser.setInt(2, user.getId());
                    stmtUser.executeUpdate();
                }

                conn.commit(); // Valider la transaction
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

}