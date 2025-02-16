package Model;
import util.ValidationUtil;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private String sex;
    private String email;
    private String motdepass;

    // Constructeur
    public User(int id, String nom, String prenom, String sex, String email, String motdepass) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sex = sex;
        if (ValidationUtil.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email invalide : " + email);
        }
        this.motdepass = motdepass;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getSex() { return sex; }
    public String getEmail() { return email; }
    public String getMotdepass() { return motdepass; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setSex(String sex) { this.sex = sex; }
    
}



