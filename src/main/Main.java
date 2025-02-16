package main; 

import Model.User;
import Model.DatabaseManager;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Établir la connexion à la base de données
        DatabaseManager.getConnection();

        // Création d'un objet User
        User user = new User(0, "hamroun", "sami", "M", "w@gmail.com", "look");

        boolean inserted = DatabaseManager.createUser(user);
        
        if (inserted) {
            System.out.println("Utilisateur créé avec succès !");
        } else {
            System.out.println("Erreur lors de la création de l'utilisateur.");
        }
        

}}