package Model;


public class Note {
	private int id; //id de la note
	private String titre;
	private String description;
	private int userId; //id de l'utilisateur 
	
	public Note(int id, String titre, String descriptin, int userId) {
		this.id=id;
		this.titre=titre;
		this.description=description;
		this.userId=userId;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	
    public String getTitre() { 
    	return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }

}

import java.time.LocalDate;

public class Note {
 	
 	    private int id; // ID de la note
 	   private int userId; // ID de l'utilisateur 
 	    private String titre; //
 	    private String description; 
 	    
 
 
 	   public Note(int id, int userId, String titre, String description) {
 	        this.id = id;
 	        this.userId = userId;
 	        this.titre = titre;
 	        this.description = description;
 	        
 	    }
 	    public Note( int userId, String titre, String description){
 	        this.userId = userId;
 	        this.titre = titre;
 	        this.description = description;
 	        
 	    }
 
 	   
 	    public int getId() {
 	        return id;
 	    }
 
 	    public void setId(int id) {
 	        this.id = id;
 	    }
 
 	    public String getTitre() {
 	        return titre;
 	    }
 
 	    public void setTitre(String titre) {
 	        this.titre = titre;
 	    }
 
 	    public String getDescription() {
 	        return description;
 	    }
 
 	    public void setDescription(String description) {
 	        this.description = description;
 	    }
 
 	    public int getUserId() {
 	        return userId;
 	    }
 
 	    public void setUserId(int userId) {
 	        this.userId = userId;
 	    }
 
 	    @Override
 	    public String toString() {
 	        return "Note{" +
 	                "id=" + id +
 	                ", titre='" + titre + '\'' +
 	                ", description='" + description + '\'' +
 	                ", userId=" + userId +
 	                '}';
 	    }
 	
   
 }

