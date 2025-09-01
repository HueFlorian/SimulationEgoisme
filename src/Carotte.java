public class Carotte {
    private Position position;
    private boolean isEaten;
    private int spawnTime; // Moment d'apparition (pour statistiques)
    
    public Carotte(Position position, int currentTime) {
        this.position = position;
        this.isEaten = false;
        this.spawnTime = currentTime;
    }
    
    // Manger la carotte
    public boolean eat() {
        if (!isEaten) {
            isEaten = true;
            return true; // Succès
        }
        return false; // Déjà mangée
    }
    
    // Getters
    public Position getPosition() { return position; }
    public boolean isEaten() { return isEaten; }
    public int getSpawnTime() { return spawnTime; }
    
    @Override
    public String toString() {
        return "Carotte{" +
               "position=" + position.x + "," + position.y +
               ", eaten=" + isEaten +
               '}';
    }
}
