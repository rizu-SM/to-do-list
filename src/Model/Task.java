package Model;

import java.time.LocalDate;

public class Task {
    private int id;
    private int userId; // ID de l'utilisateur propriétaire de la tâche
    private String titre;
    private String description;
    private LocalDate dateLimite;
    private String statut; // À faire, En cours, Terminé
    private String priorite; // Faible, Moyenne, Haute


    public Task(int id, int userId, String titre, String description, LocalDate dateLimite, String statut, String priorite) {
        this.id = id;
        this.userId = userId;
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
        this.statut = statut;
        this.priorite = priorite;
    }
    public Task( int userId, String titre, String description, LocalDate dateLimite, String statut, String priorite) {
        this.userId = userId;
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
        this.statut = statut;
        this.priorite = priorite;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public LocalDate getDateLimite() { return dateLimite; }
    public String getStatut() { return statut; }
    public String getPriorite() { return priorite; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setTitre(String titre) { this.titre = titre; }
    public void setDescription(String description) { this.description = description; }
    public void setDateLimite(LocalDate dateLimite) { this.dateLimite = dateLimite; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setPriorite(String priorite) { this.priorite = priorite; }
    
    //system de coin
    public int getCoinsForTask() {
        switch (priorite) {
            case "Faible":
                return 5;
            case "Moyenne":
                return 10;
            case "Haute":
                return 20;
            default:
                return 0;
        }
    }
  

    public boolean terminerTache(User user) {
        if (!statut.equals("Terminé")) {
            statut = "Terminé";
            user.ajouterCoins(getCoinsForTask());
            return true;
        }
        return false;
    }

    
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateLimite=" + dateLimite +
                ", statut='" + statut + '\'' +
                ", priorite='" + priorite + '\'' +
                '}';
    }
}