package main;

import Model.User;
import Model.Task;
import Controller.AuthController;
import Controller.TaskController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthController authController = new AuthController();
        TaskController taskController = new TaskController();

        User user = null; // Utilisateur connecté

        while (true) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Se connecter");
            System.out.println("2. Créer un nouveau compte");
            System.out.println("3. Quitter");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Nettoyer le buffer

            switch (choix) {
                case 1:
                    // Connexion
                    System.out.println("======== BONJOUR ========");
                    System.out.print("Entrez votre email : ");
                    String email = scanner.nextLine();
                    System.out.print("Entrez votre mot de passe : ");
                    String password = scanner.nextLine();

                    user = authController.login(email, password);
                    if (user != null) {
                        System.out.println("Connexion réussie ! Bienvenue, " + user.getNom() + ".");
                        menuTaches(scanner, authController, taskController, user); // Afficher le menu des tâches
                    } else {
                        System.out.println("Email ou mot de passe incorrect.");
                    }
                    break;

                case 2:
                    // Création d'un nouveau compte
                    System.out.println("----- Bienvenue -----");
                    System.out.print("Entrez votre nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Entrez votre prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Entrez votre sexe (M/F) : ");
                    char sex = scanner.next().charAt(0);
                    scanner.nextLine(); // Nettoyer le buffer
                    System.out.print("Entrez votre email : ");
                    String newEmail = scanner.nextLine();
                    System.out.print("Entrez votre mot de passe : ");
                    String newPassword = scanner.nextLine();

                    if (authController.signup(nom, prenom, sex, newEmail, newPassword)) {
                        System.out.println("Compte créé avec succès, bienvenue " + nom + " !");
                        user = authController.login(newEmail, newPassword); // Connexion après inscription
                        if (user != null) {
                            menuTaches(scanner, authController, taskController, user); // Afficher le menu des tâches
                        }
                    } else {
                        System.out.println("Erreur lors de la création de l'utilisateur.");
                    }
                    break;

                case 3:
                    // Quitter
                    System.out.println("Au revoir !");
                    return;

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Méthode pour afficher le menu des tâches
    private static void menuTaches(Scanner scanner, AuthController authController, TaskController taskController, User user) {
        while (true) {
            System.out.println("\n=== Menu des Tâches ===");
            System.out.println("1. Ajouter une tâche");
            System.out.println("2. Afficher les tâches");
            System.out.println("3. Marquer une tâche comme terminée");
            System.out.println("4. Se déconnecter");
            System.out.print("Choix : ");
            int choix = scanner.nextInt();
            scanner.nextLine(); // Nettoyer le buffer

            switch (choix) {
                case 1:
                    // Ajouter une tâche
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
                    String statut = scanner.nextLine();
                    System.out.print("Entrez la priorité (Faible, Moyenne, Haute) : ");
                    String priorite = scanner.nextLine();

                    Task newTask = new Task(0, user.getId(), titre, description, date, statut, priorite);
                    if (taskController.addTask(newTask)) {
                        System.out.println("Tâche ajoutée avec succès !");
                    } else {
                        System.out.println("Échec de l'ajout de la tâche.");
                    }
                    break;

                case 2:
                    // Afficher les tâches
                    List<Task> tasks = taskController.getTasksByUserId(user.getId());
                    if (tasks.isEmpty()) {
                        System.out.println("Aucune tâche trouvée.");
                    } else {
                        System.out.println("=== Tâches de l'utilisateur ===");
                        for (Task t : tasks) {
                            System.out.println(t);
                        }
                    }
                    break;

                case 3:
                    List<Task> userTasks = taskController.getTasksByUserId(user.getId());
                    if (userTasks.isEmpty()) {
                        System.out.println("Aucune tâche trouvée.");
                        break;
                    }

                    System.out.println("=== Sélectionnez une tâche à marquer comme terminée (par ID) ===");
                    for (Task t : userTasks) {
                        System.out.println("ID: " + t.getId() + " - " + t.getTitre() + " (" + t.getStatut() + ")");
                    }

                    System.out.print("Entrez l'ID de la tâche : ");
                    int taskId = scanner.nextInt();
                    scanner.nextLine(); // Nettoyer le buffer

                    Task selectedTask = null;
                    for (Task t : userTasks) {
                        if (t.getId() == taskId) {
                            selectedTask = t;
                            break;
                        }
                    }

                    if (selectedTask == null) {
                        System.out.println("Aucune tâche trouvée avec cet ID.");
                        break;
                    }

                    if (selectedTask.getStatut().equals("Terminé")) {
                        System.out.println("Cette tâche est déjà terminée.");
                        break;
                    }

                    // Appeler la fonction pour terminer la tâche
                    if (TaskController.terminerTacheEtMAJDB(selectedTask, user)) {
                        System.out.println("Mise à jour réussie !");
                    } else {
                        System.out.println("Erreur lors de la mise à jour.");
                    }
                    break;


                case 4:
                    // Déconnexion
                    authController.logout();
                    System.out.println("Déconnexion réussie.");
                    return; // Retour au menu principal

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}






































/*package main;

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

        // Vérification connection de l utilisateur
        if (user == null) {
            System.out.println("Erreur : utilisateur non identifié.");
            return;
        }

        System.out.println("1. Insérer une nouvelle tâche");
        System.out.println("2. Afficher les tâches");
        System.out.println("3. log-out");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer man /n

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
        else if (choix==3) {
        	DatabaseManager.logout();
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
