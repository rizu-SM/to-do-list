package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class FirstPgae extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel imageLabel;
    private ImageIcon originalIcon;
    private int yPosition = 0; // Position verticale de l'image
    private final int ANIMATION_SPEED = 5; // Vitesse de l'animation
    private Timer timer;
    private boolean animationStopped = false; // Indique si l'animation est arrêtée
    private JButton startButton; // Bouton pour démarrer

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FirstPgae frame = new FirstPgae();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public FirstPgae() {
        initializeUI();
        setupAnimation();
        setupListeners();
    }

    private void initializeUI() {
        originalIcon = new ImageIcon("C:\\Users\\qnes\\Desktop\\to-do-list\\src\\view\\photo\\to-do-list.png");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 452, 300);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(0x8AC1CB));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Création du JLabel avec l'image redimensionnée
        imageLabel = new JLabel();
        updateImageSize();
        contentPane.add(imageLabel);

        // Position initiale de l'image en haut de la fenêtre
        imageLabel.setLocation((contentPane.getWidth() - imageLabel.getWidth()) / 2, yPosition);

        // Création du bouton avec texte et icône
        startButton = new JButton("Let's get started");
        startButton.setIconTextGap(15); // Espace entre l'icône et le texte
        startButton.setForeground(new Color(100, 149, 237));
        startButton.setFont(new Font("MS Reference Sans Serif", Font.BOLD | Font.ITALIC, 11));
        startButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startButton.setIcon(new ImageIcon("C:\\Users\\qnes\\Desktop\\to-do-list\\src\\view\\photo\\right-arrow (1).png"));
        startButton.setBounds(159, 182, 184, 52);

        // Échanger les positions du texte et de l'icône
        startButton.setHorizontalTextPosition(JButton.LEFT); // Texte à gauche de l'icône
        startButton.setHorizontalAlignment(JButton.RIGHT); // Alignement du contenu à droite

        contentPane.add(startButton);

        // Ajouter l'écouteur d'événements au bouton
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir la fenêtre suivante
                new LogInPage().setVisible(true);
                // Fermer la fenêtre actuelle
                dispose();
            }
        });
    }

    private void setupAnimation() {
        // Création d'un Timer pour l'animation
        timer = new Timer(50, new ActionListener() { // 50 ms = 20 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mettre à jour la position verticale de l'image
                yPosition += ANIMATION_SPEED;

                // Arrêter l'animation si l'image atteint le centre de la fenêtre
                int centerY = (contentPane.getHeight() - imageLabel.getHeight()) / 2;
                if (yPosition >= centerY) {
                    yPosition = centerY; // Fixer la position au centre
                    timer.stop(); // Arrêter le Timer
                    animationStopped = true; // Marquer l'animation comme arrêtée
                }

                // Mettre à jour la position du JLabel
                imageLabel.setLocation((contentPane.getWidth() - imageLabel.getWidth()) / 2, yPosition);
            }
        });
        timer.start(); // Démarrer l'animation
    }

    private void setupListeners() {
        // Écouteur pour redimensionner la fenêtre
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Recentrer l'image si l'animation est arrêtée
                if (animationStopped) {
                    int centerY = (contentPane.getHeight() - imageLabel.getHeight()) / 2;
                    imageLabel.setLocation((contentPane.getWidth() - imageLabel.getWidth()) / 2, centerY);
                }

                // Recentrer le bouton horizontalement et ajuster sa position verticale
                int buttonX = (contentPane.getWidth() - startButton.getWidth()) / 2;
                int buttonY = contentPane.getHeight() - 70; // Position verticale fixe (70 pixels du bas)
                startButton.setLocation(buttonX, buttonY);
            }
        });
    }

    private void updateImageSize() {
        // Redimensionner l'image en fonction de la taille de la fenêtre
        int newSize = Math.min(getWidth(), getHeight()) / 4; // Taille de l'image = quart de la plus petite dimension
        Image resizedImage = originalIcon.getImage().getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(resizedImage));

        // Définir la taille du JLabel
        imageLabel.setSize(newSize, newSize);
    }
}