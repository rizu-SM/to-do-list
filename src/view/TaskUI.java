package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import Controller.TaskController;
import Model.Task;
import Model.DatabaseManager; // Import pour accéder à getUserIdByEmail

public class TaskUI extends JFrame {
    private TaskController taskController;
    private JTextField emailField;
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskUI(TaskController taskController) {
        this.taskController = taskController;
        setTitle("Gestion des Tâches");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel du haut (saisie de l'email utilisateur + bouton)
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Email Utilisateur:"));
        emailField = new JTextField(15);
        inputPanel.add(emailField);
        JButton fetchButton = new JButton("Afficher les tâches");
        inputPanel.add(fetchButton);

        // Tableau des tâches
        String[] columnNames = {"ID", "Titre", "Description", "Date Limite", "Statut", "Priorité"};
        tableModel = new DefaultTableModel(columnNames, 0);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Action du bouton
        fetchButton.addActionListener((ActionEvent e) -> loadTasks());

        // Ajouter les composants à la fenêtre
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadTasks() {
        tableModel.setRowCount(0); // Effacer les anciennes données
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un email.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = DatabaseManager.getUserIdByEmail(email);

        if (userId == -1) {
            JOptionPane.showMessageDialog(this, "Aucun utilisateur trouvé avec cet email.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Task> tasks = taskController.getTasksByUserId(userId);
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucune tâche trouvée.");
        } else {
            for (Task task : tasks) {
                tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitre(),
                    task.getDescription(),
                    task.getDateLimite(),
                    task.getStatut(),
                    task.getPriorite()
                });
            }
        }
    }

    public static void main(String[] args) {
        TaskController taskController = new TaskController();
        SwingUtilities.invokeLater(() -> new TaskUI(taskController).setVisible(true));
    }
}
