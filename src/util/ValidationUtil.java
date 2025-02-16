package util;

import java.util.regex.Pattern; //utilisée pour gérer et travailler avec les expressions régulières (regex)

public class ValidationUtil {

    // il peut entrer tout les caractére exist 
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    // Méthode pour valider l'email
    public static boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
}