package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import Model.Notification;
import Controller.NotificationController;
import util.UserSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationsController extends BaseController implements Initializable {
    @FXML
    private VBox notificationsContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("NotificationsController initialized");
        loadNotifications();
    }

    private void loadNotifications() {
        System.out.println("\n=== Loading Notifications ===");
        
        if (notificationsContainer == null) {
            System.out.println("ERROR: notificationsContainer is null!");
            return;
        }
        
        notificationsContainer.getChildren().clear();
        System.out.println("Cleared notifications container");
        
        try {
            URL cssUrl = getClass().getResource("/view/styles/notifications.css");
            if (cssUrl == null) {
                System.out.println("ERROR: Could not find notifications.css file");
            } else {
                notificationsContainer.getStylesheets().add(cssUrl.toExternalForm());
                System.out.println("Added CSS stylesheet");
            }
        } catch (Exception e) {
            System.out.println("Error loading CSS: " + e.getMessage());
            e.printStackTrace();
        }
        
        try {
            System.out.println("Getting current user...");
            UserSession session = UserSession.getInstance();
            if (session == null) {
                System.out.println("ERROR: UserSession is null!");
                return;
            }
            if (session.getCurrentUser() == null) {
                System.out.println("ERROR: Current user is null!");
                return;
            }
            
            int userId = session.getCurrentUser().getId();
            System.out.println("Current user ID: " + userId);
            
            System.out.println("Calling NotificationController.getNotificationsByUserId...");
            List<Notification> notifications = NotificationController.getNotificationsByUserId(userId);
            System.out.println("Retrieved notifications list, size: " + notifications.size());
            
            if (notifications.isEmpty()) {
                System.out.println("No notifications found for user");
                displayNoNotifications();
                return;
            }

            System.out.println("Processing " + notifications.size() + " notifications:");
            for (Notification notification : notifications) {
                System.out.println("Creating card for notification: " + 
                    "\n  ID=" + notification.getId() + 
                    "\n  Type=" + notification.getNotificationType() + 
                    "\n  Message=" + notification.getMessage() +
                    "\n  Date=" + notification.getDateCreation());
                HBox notificationCard = createNotificationCard(notification);
                notificationsContainer.getChildren().add(notificationCard);
            }
            System.out.println("=== Finished Loading Notifications ===\n");
        } catch (Exception e) {
            System.out.println("Error loading notifications: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private HBox createNotificationCard(Notification notification) {
        System.out.println("Creating notification card...");
        HBox card = new HBox();
        card.getStyleClass().add("notification-card");
        
        Label icon = new Label(getNotificationIcon(notification.getNotificationType()));
        icon.getStyleClass().add("notification-icon");
        
        VBox content = new VBox();
        content.setSpacing(4);
        
        Label message = new Label(notification.getMessage());
        message.getStyleClass().add("notification-message");
        
        Label date = new Label(formatDate(notification.getDateCreation()));
        date.getStyleClass().add("notification-date");
        
        content.getChildren().addAll(message, date);
        card.getChildren().addAll(icon, content);
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        
        System.out.println("Notification card created");
        return card;
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        return dateTime.format(formatter);
    }

    private String getNotificationIcon(String type) {
        switch (type.toLowerCase()) {
            case "task_assigned":
                return "📋";
            case "task_completed":
                return "✅";
            case "task_updated":
                return "🔄";
            case "comment":
                return "💬";
            case "mention":
                return "👤";
            default:
                return "🔔";
        }
    }

    private void displayNoNotifications() {
        Label noNotificationsLabel = new Label("No notifications to display");
        noNotificationsLabel.getStyleClass().add("no-notifications-label");
        notificationsContainer.getChildren().add(noNotificationsLabel);
    }
} 