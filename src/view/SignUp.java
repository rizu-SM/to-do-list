package View;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Cursor;

public class SignUp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField CreateAccount;
	private JTextField FirstName;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
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
		FirstName.setText("First Name\r\n");
		FirstName.setForeground(Color.GRAY);
		FirstName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		FirstName.setColumns(10);
		SignIn.add(FirstName);
		
		textField_1 = new JTextField();
		textField_1.setText("Email");
		textField_1.setForeground(Color.GRAY);
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(10, 69, 201, 21);
		SignIn.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setText("Email");
		textField_2.setForeground(Color.GRAY);
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(10, 101, 201, 21);
		SignIn.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setText("Email");
		textField_3.setForeground(Color.GRAY);
		textField_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_3.setColumns(10);
		textField_3.setBounds(10, 133, 201, 21);
		SignIn.add(textField_3);
		
		textField_4 = new JTextField();
		textField_4.setText("Email");
		textField_4.setForeground(Color.GRAY);
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_4.setColumns(10);
		textField_4.setBounds(10, 165, 201, 21);
		SignIn.add(textField_4);
		
		textField_5 = new JTextField();
		textField_5.setText("Email");
		textField_5.setForeground(Color.GRAY);
		textField_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_5.setColumns(10);
		textField_5.setBounds(10, 197, 201, 21);
		SignIn.add(textField_5);
		
		logInBtn = new JButton("Sign Up\r\n");
		logInBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		logInBtn.setForeground(Color.WHITE);
		logInBtn.setBorder(null);
		logInBtn.setBackground(new Color(51, 153, 204));
		logInBtn.setActionCommand("LogIn");
		logInBtn.setBounds(66, 229, 89, 23);
		SignIn.add(logInBtn);
		
		txtWelcomeBack = new JTextField();
		txtWelcomeBack.setText("Welcome Back !\r\n");
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
		txtToKeepConnected.setText("To keep connected please Log In\r\n\r\n");
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
	}
}
