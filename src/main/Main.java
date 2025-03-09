package main;

import Model.User;
import Model.Task;
import Model.Note;
import Controller.AuthController;
import Controller.TaskController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import Controller.NoteController;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthController authController = new AuthController();
        TaskController taskController = new TaskController();
        NoteController noteController = new NoteController();
        

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

                        // Menu des tâches
                        while (true) {
                            System.out.println("\n=== Menu des task ===");
                            System.out.println("1. Ajouter une task");
                            System.out.println("2. Afficher les task");
                            System.out.println("3. Marquer une task comme terminée");
                            System.out.println("4. Supprimer une task");
                            System.out.println("5. Modifier une task");
                            System.out.println("6. Se déconnecter");
                            System.out.println("7. afficher les task d apres priorité");
                            System.out.println("8. afficher task d'apres catégorie");
                            System.out.println("9. afficher les task d'apres la date");
                            System.out.println("10. d apres l état");
                            System.out.println("11. Gérer les notes");
                            System.out.print("Choix : ");
                            int taskChoice = scanner.nextInt();
                            scanner.nextLine(); // Nettoyer le buffer

                            switch (taskChoice) {
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
                                    System.out.print("Entrez la catégorie (routin, work, study ,sport , other) : ");
                                    String categorie = scanner.nextLine();

                                    Task newTask = new Task(0, user.getId(), titre, description, date, statut, priorite, categorie);
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
                                    // Marquer une tâche comme terminée
                                    List<Task> userTasks = taskController.getTasksByUserId(user.getId());
                                    if (userTasks.isEmpty()) {
                                        System.out.println("Aucune tâche trouvée.");
                                        break;
                                    }

                                    System.out.println("=== Sélectionnez une tâche à marquer comme terminée ===");
                                    for (int i = 0; i < userTasks.size(); i++) {
                                        System.out.println((i + 1) + ". " + userTasks.get(i).getTitre() + " (" + userTasks.get(i).getStatut() + ")");
                                    }

                                    System.out.print("Choix : ");
                                    int completeChoice = scanner.nextInt();
                                    scanner.nextLine(); // Nettoyer le buffer

                                    if (completeChoice < 1 || completeChoice > userTasks.size()) {
                                        System.out.println("Choix invalide.");
                                        break;
                                    }

                                    Task selectedTask = userTasks.get(completeChoice - 1);
                                    if (taskController.completeTask(selectedTask, user)) {
                                        System.out.println("Tâche marquée comme terminée ! Vous avez gagné " + selectedTask.getCoinsForTask() + " coins.");
                                    } else {
                                        System.out.println("Échec de la mise à jour de la tâche.");
                                    }
                                    break;

                                case 4:
                                    // Supprimer une tâche
                                    List<Task> tasksToDelete = taskController.getTasksByUserId(user.getId());
                                    if (tasksToDelete.isEmpty()) {
                                        System.out.println("Aucune tâche trouvée.");
                                        break;
                                    }

                                    System.out.println("=== Sélectionnez une tâche à supprimer ===");
                                    for (int i = 0; i < tasksToDelete.size(); i++) {
                                        System.out.println((i + 1) + ". " + tasksToDelete.get(i).getTitre() + " (" + tasksToDelete.get(i).getStatut() + ")");
                                    }

                                    System.out.print("Choix : ");
                                    int deleteChoice = scanner.nextInt();
                                    scanner.nextLine(); // Nettoyer le buffer

                                    if (deleteChoice < 1 || deleteChoice > tasksToDelete.size()) {
                                        System.out.println("Choix invalide.");
                                        break;
                                    }

                                    Task taskToDelete = tasksToDelete.get(deleteChoice - 1);
                                    if (taskController.deleteTask(taskToDelete.getId())) {
                                        System.out.println("Tâche supprimée avec succès !");
                                    } else {
                                        System.out.println("Échec de la suppression de la tâche.");
                                    }
                                    break;

                                case 5:
                                    // Modifier une tâche
                                    List<Task> tasksToEdit = taskController.getTasksByUserId(user.getId());
                                    if (tasksToEdit.isEmpty()) {
                                        System.out.println("Aucune tâche trouvée.");
                                        break;
                                    }

                                    System.out.println("=== Sélectionnez une tâche à modifier ===");
                                    for (int i = 0; i < tasksToEdit.size(); i++) {
                                        System.out.println((i + 1) + ". " + tasksToEdit.get(i).getTitre() + " (" + tasksToEdit.get(i).getStatut() + ")");
                                    }

                                    System.out.print("Choix : ");
                                    int editChoice = scanner.nextInt();
                                    scanner.nextLine(); // Nettoyer le buffer

                                    if (editChoice < 1 || editChoice > tasksToEdit.size()) {
                                        System.out.println("Choix invalide.");
                                        break;
                                    }

                                    Task taskToEdit = tasksToEdit.get(editChoice - 1);

                                    // Vérifier si la tâche est déjà terminée
                                    if ("Terminé".equalsIgnoreCase(taskToEdit.getStatut())) {
                                        System.out.println("Cette tâche est déjà terminée et ne peut pas être modifiée.");
                                        break;
                                    }

                                    // Demander les nouvelles informations pour la tâche
                                    System.out.println("====== Modification de la tâche ======");
                                    System.out.print("Entrez le nouveau titre (actuel : " + taskToEdit.getTitre() + ") : ");
                                    String newTitre = scanner.nextLine();
                                    System.out.print("Entrez la nouvelle description (actuelle : " + taskToEdit.getDescription() + ") : ");
                                    String newDescription = scanner.nextLine();

                                    LocalDate newDate = null;
                                    DateTimeFormatter editFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                                    while (newDate == null) {
                                        System.out.print("Entrez la nouvelle date (yyyy-MM-dd) (actuelle : " + taskToEdit.getDateLimite() + ") : ");
                                        String dateStr = scanner.nextLine().trim();

                                        try {
                                            newDate = LocalDate.parse(dateStr, editFormatter);
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Format invalide, veuillez entrer une date valide !");
                                        }
                                    }

                                    System.out.print("Entrez le nouveau statut (À faire, En cours, Terminé) (actuel : " + taskToEdit.getStatut() + ") : ");
                                    String newStatut = scanner.nextLine();
                                    System.out.print("Entrez la nouvelle priorité (Faible, Moyenne, Haute) (actuelle : " + taskToEdit.getPriorite() + ") : ");
                                    String newPriorite = scanner.nextLine();
                                    System.out.print("Entrez la nouvelle categorie (routin, work, study, sport, other) (actuelle : " + taskToEdit.getCategorie() + ") : ");
                                    String newCategorie = scanner.nextLine();

                                    // Mettre à jour la tâche
                                    taskToEdit.setTitre(newTitre);
                                    taskToEdit.setDescription(newDescription);
                                    taskToEdit.setDateLimite(newDate);
                                    taskToEdit.setStatut(newStatut);
                                    taskToEdit.setPriorite(newPriorite);
                                    taskToEdit.setCategorie(newCategorie);

                                    if (taskController.editTask(taskToEdit)) {
                                        System.out.println("Tâche modifiée avec succès !");
                                    } else {
                                        System.out.println("Échec de la modification de la tâche.");
                                    }
                                    break;

                                case 6:
                                    // Déconnexion
                                    authController.logout();
                                    System.out.println("Déconnexion réussie.");
                                    return; // Retour au menu principal
                                case 7:
                                    System.out.println("=== Tâches triées par priorité ===");
                                    List<Task> sortedTasks = taskController.getTasksByUserIdSortedByPriority(user.getId());
                                    for (Task t : sortedTasks) {
                                        System.out.println(t);
                                    }
                                    break;
                                case 8:
                                    System.out.println("Entrez la catégorie : ");
                                    String category = scanner.nextLine();
                                    System.out.println("=== Tâches de la catégorie '" + category + "' ===");
                                    List<Task> categoryTasks = taskController.getTasksByCategory(user.getId(), category);
                                    for (Task t : categoryTasks) {
                                        System.out.println(t);
                                    }
                                    break;
                                case 9:
                                    System.out.println("=== Tâches triées par date ===");
                                    List<Task> sortedByDateTasks = taskController.getTasksSortedByDate(user.getId());
                                    for (Task t : sortedByDateTasks) {
                                        System.out.println(t);
                                    }
                                    break;
                                case 10:
                                    System.out.println("Entrez le statut des tâches à afficher ( 'À faire', 'En cours', 'Terminé'):");
                                    String status = scanner.nextLine();
                                    System.out.println("=== Tâches avec le statut : " + status + " ===");
                                    List<Task> filteredTasks = taskController.getTasksByStatus(user.getId(), status);
                                    for (Task t : filteredTasks) {
                                        System.out.println(t);
                                    }
                                    break;
                                case 11:
                                    // Afficher les tâches
                                	while (true) {
                                        System.out.println("\n=== Menu des Notes ===");
                                        System.out.println("1. Ajouter une note");
                                        System.out.println("2. Afficher mes notes");
                                        System.out.println("3. Modifier une note");
                                        System.out.println("4. Supprimer une note");
                                        System.out.println("5. Retour au menu principal");
                                        System.out.print("Choix : ");
                                        int choixNote = scanner.nextInt();
                                        scanner.nextLine();

                                        switch (choixNote) {
                                            case 1:
                                                System.out.print("Titre : ");
                                                String title = scanner.nextLine();
                                                System.out.print("Description : ");
                                                String describe = scanner.nextLine();
                                                Note note = new Note(0, user.getId() , title, describe);
                                                if (noteController.addNote(note)) {
                                                    System.out.println("Note ajoutée !");
                                                } else {
                                                    System.out.println("Erreur lors de l'ajout.");
                                                }
                                                break;

                                            case 2:
                                                List<Note> notes = noteController.getNotesByUserId(user.getId());
                                                for (Note n : notes) {
                                                    System.out.println(n);
                                                }
                                                break;

                                            case 3:
                                                System.out.print("ID de la note à modifier : ");
                                                int noteId = scanner.nextInt();
                                                scanner.nextLine();
                                                System.out.print("Nouveau titre : ");
                                                String newTitle = scanner.nextLine();
                                                System.out.print("Nouvelle description : ");
                                                String newDesc = scanner.nextLine();
                                                Note updatedNote = new Note(noteId, user.getId(), newTitle, newDesc);
                                                if (noteController.updateNote(updatedNote)) {
                                                    System.out.println("Note mise à jour !");
                                                } else {
                                                    System.out.println("Erreur lors de la mise à jour.");
                                                }
                                                break;

                                            case 4:
                                                System.out.print("ID de la note à supprimer : ");
                                                int deleteId = scanner.nextInt();
                                                if (noteController.deleteNote(deleteId)) {
                                                    System.out.println("Note supprimée !");
                                                } else {
                                                    System.out.println("Erreur lors de la suppression.");
                                                }
                                                break;

                                            case 5:
                                                break;
                                        }}
                                        




                                default:
                                    System.out.println("Choix invalide.");
                                	}}}}}}}

























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
