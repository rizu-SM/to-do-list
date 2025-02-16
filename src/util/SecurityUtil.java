package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;//Une classe d'exception qui est levée lorsqu'un algorithme demandé

public class SecurityUtil {

    // Méthode pour hacher une chaîne de caractères en SHA-256
    public static String hashSHA256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //MessageDigest : Une classe Java utilisée pour appliquer des fonctions de hachage (comme SHA-256) sur des données.
            
            byte[] hash = md.digest(data.getBytes());

            StringBuilder hexString = new StringBuilder();
            //md.digest() : C'est une méthode de l'objet MessageDigest qui Prend un tableau d'octets en entrée puis Applique l'algorithme de hachage apres Retourne le résultat sous forme d’un tableau de bytes
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            // cette partie convertit les valeurs hachées (bytes) en format hexadécimal.

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hashage", e);
        }
    }
}