package Model;

import java.time.LocalDateTime;

public class Notification {
    private int id;
    private String notificationType;  // Correspond à la colonne 'notification' dans la table
    private int userId;
    private Integer taskId;  // Integer pour permettre null
    private String message;
    private LocalDateTime dateCreation;

    // Constructeur
    public Notification(int id, String notificationType, int userId, Integer taskId, 
                       String message, LocalDateTime dateCreation) {
        this.id = id;
        this.notificationType = notificationType;
        this.userId = userId;
        this.taskId = taskId;
        this.message = message;
        this.dateCreation = dateCreation;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public int getUserId() {
        return userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    // Setters (si nécessaires)
    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "[" + notificationType + "] "
             + message
             + (taskId != null ? " (Tâche liée: " + taskId + ")" : "")
             + " — le " + dateCreation;
    }



}