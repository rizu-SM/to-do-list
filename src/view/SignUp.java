package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Cursor;

public class SignUp extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField CreateAccount;
    private JTextField FirstName;
    private JTextField LastName; // Déclaration de LastName
    private JTextField EmailAddress;
    private JTextField Sex;
    private JTextField Password;
    private JTextField ConfirmationOfPassword;
    private JButton logInBtn;
    private JTextField txtWelcomeBack;
    private JTextField txtToKeepConnected;
    private JButton logInBtn_1;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SignUp frame = new SignUp();
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
    public SignUp() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JPanel SignIn = new JPanel();
        SignIn.setBorder(null);
        SignIn.setBounds(211, 0, 235, 274);
        SignIn.setBackground(new Color(0x8AC1CB));
        contentPane.add(SignIn);
        SignIn.setLayout(null);

        CreateAccount = new JTextField();
        CreateAccount.setBounds(42, 5, 150, 21);
        CreateAccount.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 18));
        CreateAccount.setForeground(new Color(0, 51, 204));
        CreateAccount.setDisabledTextColor(new Color(51, 153, 204));
        CreateAccount.setBorder(null);
        CreateAccount.setBackground(new Color(0x8AC1CB));
        CreateAccount.setEditable(false);
        CreateAccount.setEnabled(false);
        CreateAccount.setText("Create account");
        SignIn.add(CreateAccount);
        CreateAccount.setColumns(10);

        FirstName = new JTextField();
        FirstName.setBounds(10, 37, 201, 21);
        FirstName.setText("First Name");
        FirstName.setForeground(Color.GRAY);
        FirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        FirstName.setColumns(10);
        SignIn.add(FirstName);

        FirstName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (FirstName.getText().equals("First Name")) {
                    FirstName.setText("");
                    FirstName.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (FirstName.getText().equals("")) {
                    FirstName.setText("First Name");
                    FirstName.setForeground(Color.GRAY);
                }
            }
        });

        LastName = new JTextField();
        LastName.setText("Last Name");
        LastName.setForeground(Color.GRAY);
        LastName.setFont(new Font("Tahoma", Font.PLAIN, 12));
        LastName.setColumns(10);
        LastName.setBounds(10, 69, 201, 21);
        SignIn.add(LastName);

        LastName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (LastName.getText().equals("Last Name")) {
                    LastName.setText("");
                    LastName.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (LastName.getText().equals("")) {
                    LastName.setText("Last Name");
                    LastName.setForeground(Color.GRAY);
                }
            }
        });

        EmailAddress= new JTextField();
        EmailAddress.setText("Email Address");
        EmailAddress.setForeground(Color.GRAY);
        EmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 12));
        EmailAddress.setColumns(10);
        EmailAddress.setBounds(10, 101, 201, 21);
        SignIn.add(EmailAddress);

		EmailAddress.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (EmailAddress.getText().equals("Email Address")) {
                    EmailAddress.setText("");
                    EmailAddress.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (EmailAddress.getText().equals("")) {
                    EmailAddress.setText("Email Address");
                    EmailAddress.setForeground(Color.GRAY);
                }
            }
        });


        Sex= new JTextField();
        Sex.setText("Sex");
        Sex.setForeground(Color.GRAY);
        Sex.setFont(new Font("Tahoma", Font.PLAIN, 12));
        Sex.setColumns(10);
        Sex.setBounds(10, 133, 201, 21);
        SignIn.add(Sex);


		Sex.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Sex.getText().equals("Sex")) {
                    Sex.setText("");
                    Sex.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Sex.getText().equals("")) {
                    Sex.setText("Sex");
                    Sex.setForeground(Color.GRAY);
                }
            }
        });

        Password = new JTextField();
        Password.setText("Password");
        Password.setForeground(Color.GRAY);
        Password.setFont(new Font("Tahoma", Font.PLAIN, 12));
        Password.setColumns(10);
        Password.setBounds(10, 165, 201, 21);
        SignIn.add(Password);


		Password.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (Password.getText().equals("Password")) {
                    Password.setText("");
                    Password.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Password.getText().equals("")) {
                    Password.setText("Password");
                    Password.setForeground(Color.GRAY);
                }
            }
        });


        ConfirmationOfPassword= new JTextField();
        ConfirmationOfPassword.setText("Confirmation Of Password");
        ConfirmationOfPassword.setForeground(Color.GRAY);
        ConfirmationOfPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
        ConfirmationOfPassword.setColumns(10);
        ConfirmationOfPassword.setBounds(10, 197, 201, 21);
        SignIn.add(ConfirmationOfPassword);



		
	
		ConfirmationOfPassword.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (ConfirmationOfPassword.getText().equals("Confirmation Of Password")) {
					ConfirmationOfPassword.setText("");
					ConfirmationOfPassword.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (ConfirmationOfPassword.getText().equals("")) {
					ConfirmationOfPassword.setText("Confirmation Of Password");
					ConfirmationOfPassword.setForeground(Color.GRAY);
				}
			}
		});
        logInBtn = new JButton("Sign Up");
        logInBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logInBtn.setForeground(Color.WHITE);
        logInBtn.setBorder(null);
        logInBtn.setBackground(new Color(51, 153, 204));
        logInBtn.setActionCommand("LogIn");
        logInBtn.setBounds(66, 229, 89, 23);
        SignIn.add(logInBtn);



       
        txtWelcomeBack = new JTextField();
        txtWelcomeBack.setText("Welcome Back !");
        txtWelcomeBack.setForeground(new Color(0, 102, 153));
        txtWelcomeBack.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 17));
        txtWelcomeBack.setEnabled(false);
        txtWelcomeBack.setEditable(false);
        txtWelcomeBack.setDisabledTextColor(new Color(51, 153, 204));
        txtWelcomeBack.setColumns(10);
        txtWelcomeBack.setBorder(null);
        txtWelcomeBack.setBounds(36, 44, 133, 20);
        contentPane.add(txtWelcomeBack);

        txtToKeepConnected = new JTextField();
        txtToKeepConnected.setText("To keep connected please Log In");
        txtToKeepConnected.setSelectedTextColor(new Color(0, 102, 153));
        txtToKeepConnected.setForeground(new Color(0, 102, 153));
        txtToKeepConnected.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 13));
        txtToKeepConnected.setEnabled(false);
        txtToKeepConnected.setEditable(false);
        txtToKeepConnected.setDisabledTextColor(new Color(51, 153, 204));
        txtToKeepConnected.setColumns(10);
        txtToKeepConnected.setBorder(null);
        txtToKeepConnected.setBounds(10, 113, 191, 20);
        contentPane.add(txtToKeepConnected);

        logInBtn_1 = new JButton("Log In");
        logInBtn_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logInBtn_1.setForeground(Color.WHITE);
        logInBtn_1.setBorder(null);
        logInBtn_1.setBackground(new Color(51, 153, 204));
        logInBtn_1.setActionCommand("LogIn");
        logInBtn_1.setBounds(57, 229, 89, 23);
        contentPane.add(logInBtn_1);
        
        logInBtn_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LogInPage L = new LogInPage();
                L.setVisible(true);
                dispose();
            }
        });

        
    }



}