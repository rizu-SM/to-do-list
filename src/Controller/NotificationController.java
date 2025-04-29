package Controller;

import java.util.List;
import Model.Notification;
import Model.DatabaseManager;

public class NotificationController {
    public static void startScheduler() {
        DatabaseManager.startTaskNotificationScheduler();
    }

    public static void sendReminders() {
        DatabaseManager.sendTaskReminders();
    }

    public static void add(int userId, Integer taskId, String type, String msg) {
        DatabaseManager.addNotification(userId, taskId, type, msg);
    }

    public static void add(int userId, String type, String msg) {
        DatabaseManager.addNotification(userId, type, msg);
    }
    public static List<Notification> getNotificationsByUserId(int userId) {
        return DatabaseManager.getNotificationsByUserId(userId);
    }
}
