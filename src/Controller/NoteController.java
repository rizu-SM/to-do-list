package Controller;
 
 import Model.Note;
 import Model.DatabaseManager;
 import java.util.List;

 
 public class NoteController {
     
     // Ajouter une note en base de données
     public boolean addNote(Note note) {
         return DatabaseManager.addNote(note);
     }
 
     // Récupérer les notes d'un utilisateur
     public List<Note> getNotesByUserId(int userId) {
         return DatabaseManager.getNotesByUserId(userId);
     }
 
     // Modifier une note
     public boolean updateNote(Note note) {
         return DatabaseManager.updateNote(note);
     }
 
     // Supprimer une note
     public boolean deleteNote(int id) {
         return DatabaseManager.deleteNote(id);
     }
 }