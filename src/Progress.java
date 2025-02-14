import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.SQLException;
	import java.sql.ResultSet;
	import java.security.MessageDigest;
	import java.security.NoSuchAlgorithmException;
	
	public class Progress {
	    private static final String URL = "jdbc:mysql://localhost:3306/etudiantDB?characterEncoding=UTF-8";
	    private static final String USER = "root";
	    private static final String PASSWORD = "supra_2006";
	    
	    private static Connection conn = null;
	
	    public static Connection getConnection() {
	        if (conn == null) {
	            try {
	                System.out.println("Vérification du driver...");
	                Class.forName("com.mysql.jdbc.Driver"); // Utiliser l'ancien driver
	                conn = DriverManager.getConnection(URL, USER, PASSWORD);
	                System.out.println("Connexion réussie !");
	            } catch (ClassNotFoundException e) {
	                System.out.println("Driver non trouvé : " + e.getMessage());
	            } catch (SQLException e) {
	                System.out.println("Erreur de connexion : " + e.getMessage());
	            }
	        }
	        return conn;
	    }
	
	    public static void insertEtudiant(String nom, String prenom, int age) {
	        String sql = "INSERT INTO etudiant (nom, prenom, age) VALUES (?, ?, ?)";
	
	        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
	            stmt.setString(1, nom);
	            stmt.setString(2, prenom);
	            stmt.setInt(3, age);
	
	            int rowsInserted = stmt.executeUpdate();
	            if (rowsInserted > 0) {
	                System.out.println("Étudiant inséré avec succès !");
	            }
	        } catch (SQLException e) {
	            System.out.println("Erreur lors de l'insertion : " + e.getMessage());
	        }
	    }
	
	    public static void afficherEtudiants() {
	        String sql = "SELECT * FROM etudiant";
	
	        try (PreparedStatement stmt = getConnection().prepareStatement(sql);
	             ResultSet resultSet = stmt.executeQuery()) {
	
	            System.out.println("Liste des étudiants :");
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String nom = resultSet.getString("nom");
	                String prenom = resultSet.getString("prenom");
	                int age = resultSet.getInt("age");
	
	                System.out.println(id + " - " + nom + " " + prenom + ", " + age + " ans");
	            }
	        } catch (SQLException e) {
	            System.out.println("Erreur lors de l'affichage : " + e.getMessage());
	        }
	    }
	    
	        public static String hashSHA256(String data) {
	        try {
	            MessageDigest md = MessageDigest.getInstance("SHA-256");
	            byte[] hash = md.digest(data.getBytes());
	
	            StringBuilder hexString = new StringBuilder();
	            for (byte b : hash) {
	                hexString.append(String.format("%02x", b));
	            }
	            return hexString.toString();
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("Erreur de hashage", e);
	        }
	    }
	
	    public static void main(String[] args) {
	        getConnection();
	        //insertEtudiant("lina",hashSHA256("ssd") , 19);
	        afficherEtudiants();
	    }
	}

