package View;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import java.awt.Cursor;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JToolBar;
import javax.swing.JSplitPane;

public class LogInPage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtSignIn;
    private JTextField txtEmail;
    private JTextField txtPasswordPlaceholder; // Placeholder pour le mot de passe
    private JPasswordField txtPassword; // Champ de mot de passe réel
    private JCheckBox chkShowPassword;
    private JButton LogInBtn;
    private JTextField Hello;
    private JTextField texteA;
    private JTextField txtJoinUs;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LogInPage frame = new LogInPage();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public LogInPage() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel logInPanel = new JPanel();
        logInPanel.setBackground(new Color(0x8AC1CB)); // Couleur de fond #8AC1CB
        logInPanel.setBounds(0, 0, 219, 273);
        contentPane.add(logInPanel);
        logInPanel.setLayout(null);

        // Texte "SIGN IN"
        txtSignIn = new JTextField();
        txtSignIn.setDisabledTextColor(new Color(0, 153, 204));
        txtSignIn.setBorder(null);
        txtSignIn.setBackground(new Color(0x8AC1CB)); // Couleur de fond #8AC1CB
        txtSignIn.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 15));
        txtSignIn.setText("SIGN IN");
        txtSignIn.setForeground(new Color(0, 102, 153));
        txtSignIn.setEditable(false);
        txtSignIn.setEnabled(false);
        txtSignIn.setBounds(72, 25, 96, 20);
        logInPanel.add(txtSignIn);
        txtSignIn.setColumns(10);

        // Champ pour l'email
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtEmail.setText("Email"); // Texte par défaut
        txtEmail.setForeground(Color.GRAY); // Couleur du texte par défaut
        txtEmail.setBounds(30, 80, 150, 25);
        logInPanel.add(txtEmail);
        txtEmail.setColumns(10);

        // Ajout d'un FocusListener pour l'email
        txtEmail.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtEmail.getText().equals("Email")) {
                    txtEmail.setText(""); // Supprime le texte par défaut
                    txtEmail.setForeground(Color.BLACK); // Change la couleur du texte
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtEmail.getText().isEmpty()) {
                    txtEmail.setText("Email"); // Remet le texte par défaut
                    txtEmail.setForeground(Color.GRAY); // Change la couleur du texte
                }
            }
        });

        // Placeholder pour le mot de passe
        txtPasswordPlaceholder = new JTextField();
        txtPasswordPlaceholder.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtPasswordPlaceholder.setText("Password"); // Texte par défaut
        txtPasswordPlaceholder.setForeground(Color.GRAY); // Couleur du texte par défaut
        txtPasswordPlaceholder.setBounds(30, 120, 150, 25);
        logInPanel.add(txtPasswordPlaceholder);

        // Champ de mot de passe réel
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtPassword.setBounds(30, 120, 150, 25);
        txtPassword.setEchoChar('•'); // Caractère de masquage
        txtPassword.setVisible(false); // Masqué par défaut
        logInPanel.add(txtPassword);

        // Ajout d'un FocusListener pour le placeholder du mot de passe
        txtPasswordPlaceholder.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                txtPasswordPlaceholder.setVisible(false); // Masque le placeholder
                txtPassword.setVisible(true); // Affiche le champ de mot de passe
                txtPassword.requestFocus(); // Donne le focus au champ de mot de passe
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Ne rien faire ici
            }
        });

        // Ajout d'un FocusListener pour le champ de mot de passe
        txtPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Ne rien faire ici
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtPassword.getPassword().length == 0) {
                    txtPassword.setVisible(false); // Masque le champ de mot de passe
                    txtPasswordPlaceholder.setVisible(true); // Affiche le placeholder
                }
            }
        });

        // Case à cocher pour afficher/masquer le mot de passe
        chkShowPassword = new JCheckBox("Show password");
        chkShowPassword.setFont(new Font("Tahoma", Font.PLAIN, 10));
        chkShowPassword.setBounds(30, 150, 150, 20);
        chkShowPassword.setBackground(new Color(0x8AC1CB)); // Couleur de fond #8AC1CB
        logInPanel.add(chkShowPassword);

        // Bouton de connexion
        LogInBtn = new JButton("Log In");
        LogInBtn.setForeground(new Color(255, 255, 255));
        LogInBtn.setBackground(new Color(51, 153, 204));
        LogInBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        LogInBtn.setBorder(null);
        LogInBtn.setActionCommand("LogIn");
        LogInBtn.setBounds(67, 218, 89, 23);
        logInPanel.add(LogInBtn);

        // Texte "Hello friend !"
        Hello = new JTextField();
        Hello.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        Hello.setDisabledTextColor(new Color(51, 153, 204));
        Hello.setBorder(null);
        Hello.setEditable(false);
        Hello.setEnabled(false);
        Hello.setForeground(new Color(0, 102, 153));
        Hello.setText("Hello friend !");
        Hello.setBounds(273, 29, 104, 20);
        contentPane.add(Hello);
        Hello.setColumns(10);

        texteA = new JTextField();
        texteA.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
        texteA.setBorder(null);
        texteA.setEnabled(false);
        texteA.setEditable(false);
        texteA.setDisabledTextColor(new Color(51, 153, 204));
        texteA.setForeground(new Color(0, 102, 153));
        texteA.setSelectedTextColor(new Color(0, 102, 153));
        texteA.setText("if you do not have an account");
        texteA.setBounds(237, 88, 173, 20);
        contentPane.add(texteA);
        texteA.setColumns(10);

        txtJoinUs = new JTextField();
        txtJoinUs.setText("Join us !");
        txtJoinUs.setSelectedTextColor(new Color(0, 102, 153));
        txtJoinUs.setForeground(new Color(0, 102, 153));
        txtJoinUs.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
        txtJoinUs.setEnabled(false);
        txtJoinUs.setEditable(false);
        txtJoinUs.setDisabledTextColor(new Color(51, 153, 204));
        txtJoinUs.setColumns(10);
        txtJoinUs.setBorder(null);
        txtJoinUs.setBounds(291, 133, 86, 20);
        contentPane.add(txtJoinUs);

        JButton SignUP = new JButton("Sign Up");
        SignUP.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        SignUP.setForeground(Color.WHITE);
        SignUP.setBorder(null);
        SignUP.setBackground(new Color(51, 153, 204));
        SignUP.setActionCommand("LogIn");
        SignUP.setBounds(273, 216, 89, 23);
        contentPane.add(SignUP);

        // Ajouter l'écouteur d'événements au bouton "Sign Up"
        SignUP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir la fenêtre suivante
                new SignUp().setVisible(true);
                // Fermer la fenêtre actuelle
                dispose();
            }
        });

        // Ajout d'un ActionListener pour la case à cocher
        chkShowPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (chkShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char) 0); // Affiche le mot de passe en clair
                } else {
                    txtPassword.setEchoChar('•'); // Masque le mot de passe
                }
            }
        });
    }
}