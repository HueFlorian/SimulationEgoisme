import java.util.*;

public class Maison {
    private Position position;
    private List<GardenAgent> habitants;
    private final int capaciteMax = 10;
    private int maisonId;
    
    public Maison(Position position, int id) {
        this.position = position;
        this.habitants = new ArrayList<>();
        this.maisonId = id;
    }
    
    // Ajouter un habitant
    public boolean ajouterHabitant(GardenAgent agent) {
        if (habitants.size() < capaciteMax) {
            habitants.add(agent);
            agent.setMaison(this);
            return true;
        }
        return false; // Maison pleine
    }
    
    // Retirer un habitant (quand il meurt)
    public void retirerHabitant(GardenAgent agent) {
        habitants.remove(agent);
    }
    
    // Vérifier si la maison a de la place
    public boolean aDelaPlace() {
        return habitants.size() < capaciteMax;
    }
    
    // Faire rentrer tous les habitants
    public void rentrerHabitants() {
        for (GardenAgent agent : habitants) {
            agent.rentrerMaison();
        }
    }
    
    // Getters
    public Position getPosition() { return position; }
    public List<GardenAgent> getHabitants() { return new ArrayList<>(habitants); }
    public int getNombreHabitants() { return habitants.size(); }
    public int getCapaciteMax() { return capaciteMax; }
    public int getMaisonId() { return maisonId; }
    
    @Override
    public String toString() {
        return String.format("Maison_%d[%d/%d habitants]", 
                           maisonId, habitants.size(), capaciteMax);
    }
}
