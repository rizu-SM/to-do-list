package main;

import Model.User;
import Model.DatabaseManager;
import java.util.*;
import Model.Task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.getConnection();
        Scanner scanner = new Scanner(System.in);

        System.out.println("1. login");
        System.out.println("2. créer un nouveau compte");

        int x = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer

        User user = null; // Initialisation de l'utilisateur

        if (x == 1) {
            System.out.println("======== BONJOUR ========");
            System.out.print("Entrez votre email : ");
            String Vemail = scanner.nextLine();

            System.out.print("Entrez votre mot de passe : ");
            String Vpassword = scanner.nextLine();

            user = DatabaseManager.login(Vemail, Vpassword);

            if (user != null) {
                System.out.println("Connexion réussie ! Bienvenue, " + user.getNom() + ".");
            } else {
                System.out.println("Email ou mot de passe incorrect.");
                return; // Quitter le programme
            }
        } else if (x == 2) {
            System.out.println("----- Bienvenue -----");
            System.out.print("Entrez votre nom : ");
            String name = scanner.nextLine();
            System.out.print("Entrez votre prénom : ");
            String prenom = scanner.nextLine();
            System.out.print("Entrez votre sexe (M/F) : ");
            char sex = scanner.next().charAt(0);
            scanner.nextLine(); // Nettoyer le buffer
            System.out.print("Entrez votre email : ");
            String email = scanner.nextLine();
            System.out.print("Entrez votre mot de passe : ");
            String motdepass = scanner.nextLine();

            user = new User(name, prenom, sex, email, motdepass, 0);

            if (DatabaseManager.createUser(user)) {
                System.out.println("Compte créé avec succès, bienvenue " + user.getNom());
                user = DatabaseManager.login(email, motdepass); // Connexion après inscription
            } else {
                System.out.println("Erreur lors de la création de l'utilisateur.");
                return;
            }
        }

        // Vérification si l'utilisateur est bien connecté
        if (user == null) {
            System.out.println("Erreur : utilisateur non identifié.");
            return;
        }

        System.out.println("1. Insérer une nouvelle tâche");
        System.out.println("2. Afficher les tâches");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer

        int userId = DatabaseManager.getUserIdByEmail(user.getEmail());

        if (choix == 1) {
            System.out.println("====== Ajout d'une tâche ======");
            System.out.print("Entrez le titre : ");
            String titre = scanner.nextLine();
            System.out.print("Entrez la description : ");
            String description = scanner.nextLine();

            LocalDate date = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (date == null) {
                System.out.print("Entrez la date (yyyy-MM-dd) : ");
                String dateStr = scanner.nextLine().trim();

                try {
                    date = LocalDate.parse(dateStr, formatter);
                } catch (DateTimeParseException e) {
                    System.out.println("Format invalide, veuillez entrer une date valide !");
                }
            }

            System.out.print("Entrez le statut (À faire, En cours, Terminé) : ");
            String status = scanner.nextLine();
            System.out.print("Entrez la priorité (Faible, Moyenne, Haute) : ");
            String priorite = scanner.nextLine();

            Task newTask = new Task(userId, titre, description, date, status, priorite);
            if (DatabaseManager.addTask(newTask)) {
                System.out.println("Tâche ajoutée avec succès !");
            } else {
                System.out.println("Échec de l'ajout de la tâche.");
            }
        } else if (choix == 2) {
            List<Task> tasks = DatabaseManager.getTasksByUserId(userId);
            if (tasks.isEmpty()) {
                System.out.println("Aucune tâche trouvée.");
            } else {
                System.out.println("=== Tâches de l'utilisateur ===");
                for (Task t : tasks) {
                    System.out.println(t);
                }
            }
        }
    }
}



/*
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

/*
User user = new User(1, "Doe", "John", "M", "john.doe@example.com", "password", 0);
Task task = new Task(1, user.getId(), "Tâche de test", "Finaliser le module coin", LocalDate.now().plusDays(1), "À faire", "Haute");

System.out.println("Coins de l'utilisateur AVANT : " + user.getCoin());
System.out.println("Statut de la tâche AVANT : " + task.getStatut());
boolean result = DatabaseManager.terminerTacheEtMAJDB(task, user);
if (result) {
    System.out.println("Tâche terminée avec succès !");
} else {
    System.out.println("Échec de la fin de tâche.");
}

System.out.println("Coins de l'utilisateur APRÈS : " + user.getCoin());
System.out.println("Statut de la tâche APRÈS : " + task.getStatut());*/
