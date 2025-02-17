package main; 

import Model.User;
import Model.DatabaseManager;
import java.util.*;
import Model.Task;
import java.time.LocalDate;
public class Main {
    public static void main(String[] args) {
        // Établir la connexion à la base de données
        DatabaseManager.getConnection();

        // Création d'un objet User
       /* User user = new User(0, "hamroun", "sami", "M", "w@gmail.com", "look");

        boolean inserted = DatabaseManager.createUser(user);
        
        if (inserted) {
            System.out.println("Utilisateur créé avec succès !");
        } else {
            System.out.println("Erreur lors de la création de l'utilisateur.");
        }
        */
        /*Scanner scanner = new Scanner(System.in);
        
        System.out.print("Entrez votre email : ");
        String email = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        // Appeler la méthode login pour vérifier les identifiants
        User user = DatabaseManager.login(email, password);

        // Vérifier si la connexion a réussi
        if (user != null) {
            System.out.println("Connexion réussie ! Bienvenue, " + user.getNom() + ".");
        } else {
            System.out.println("Email ou mot de passe incorrect.");
        }
        */
        int userId = 7;
        
        Task task = new Task(
                0, // L'ID est auto-incrémenté, donc 0 est utilisé ici
                userId, // ID de l'utilisateur propriétaire de la tâche
                "Faire les courses", // Titre de la tâche
                "Acheter du lait, des œufs et du pain", // Description
                LocalDate.now().plusDays(3), // Date limite (3 jours à partir d'aujourd'hui)
                "À faire", // Statut
                "Moyenne" // Priorité
            );
        /*boolean isTaskAdded = DatabaseManager.addTask(task);
        if (isTaskAdded) {
            System.out.println("Tâche ajoutée avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout de la tâche.");
        }*/

        // Récupérer et afficher les tâches de l'utilisateur
        List<Task> tasks = DatabaseManager.getTasksByUserId(userId);
        if (tasks.isEmpty()) {
            System.out.println("Aucune tâche trouvée pour l'utilisateur avec l'ID : " + userId);
        } else {
            System.out.println("=== Tâches de l'utilisateur ===");
            for (Task t : tasks) {
                System.out.println(t);
            }
        }
        

    } 
    
        
        

}