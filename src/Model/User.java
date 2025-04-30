package Model;
import util.ValidationUtil;

public class User {
    private int id; // Auto-incrémenté en base de données
    private String nom;
    private String prenom;
    private char sex;
    private String email;
    private String motdepass;
    private int coin;

    // Constructeur sans id (pour création d'un nouvel utilisateur)
    public User(String nom, String prenom, char sex, String email, String motdepass, int coin) {
        this.nom = nom;
        this.prenom = prenom;
        this.sex = sex;
        this.coin = coin;
        if (ValidationUtil.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Email invalide : " + email);
        }
        this.motdepass = motdepass;
    }
    
    public User(int id, String nom, String prenom, String email) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }


    // Constructeur complet (si besoin)
    public User(int id, String nom, String prenom, char sex, String email, String motdepass, int coin) {
        this(nom, prenom, sex, email, motdepass, coin); // Réutilise l'autre constructeur
        this.id = id;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public char getSex() { return sex; }
    public String getEmail() { return email; }
    public String getMotdepass() { return motdepass; }
    public int getCoin() { return coin; }
    public String getFullName() {
        return nom + " " + lastName;
    }
    
    public void setCoin(int coin) { this.coin = coin; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setSex(char sex) { this.sex = sex; }

    public void ajouterCoins(int coins) {
        this.coin += coins;
    }
}


