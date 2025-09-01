import java.util.*;

public class GardenAgent {
    private static final Random random = new Random();
    private static int nextId = 1;
    
    private int agentId;
    private Position position;
    private Maison maison;
    private boolean isAlive;
    private boolean isInGarden; // true = jardin, false = maison
    
    // Statistiques journalières
    private int pointsToday; // Carottes mangées aujourd'hui
    private int daysSurvived; // Jours survécus
    
    // Paramètres comportementaux
    private int visionRadius; // Rayon de vision pour carottes
    private double speed; // Vitesse de déplacement
    
    public GardenAgent(Position startPosition, Maison maison) {
        this.agentId = nextId++;
        this.position = startPosition;
        this.maison = maison;
        this.isAlive = true;
        this.isInGarden = false; // Commence à la maison
        this.pointsToday = 0;
        this.daysSurvived = 0;
        
        // Vision configurable
        this.visionRadius = visionRadius;
        this.speed = 0.8 + random.nextDouble() * 0.4; 
    }

    // Constructeur avec vision configurable
    public GardenAgent(Position startPosition, Maison maison, int visionRadius) {
        this(startPosition, maison); // Appel au constructeur existant
        this.visionRadius = visionRadius; // Override la vision
    }

    // Setter pour la vision 
    public void setVisionRadius(int visionRadius) {
        this.visionRadius = visionRadius;
    }

    // Méthode pour incrémenter les jours survécus
    public void incrementDaysSurvived() {
        this.daysSurvived++;
    }
    
    // Sortir dans le jardin
    public void sortirJardin(Position jardinPosition) {
        this.position = jardinPosition;
        this.isInGarden = true;
        this.pointsToday = 0; // Reset points pour nouvelle journée
    }
    
    // Rentrer à la maison
    public void rentrerMaison() {
        if (maison != null) {
            this.position = maison.getPosition();
            this.isInGarden = false;
        }
    }
    
    // Chercher des carottes dans le rayon de vision
    public List<Carotte> voirCarottes(List<Carotte> toutesCarottes) {
        List<Carotte> carottesVisibles = new ArrayList<>();
        
        for (Carotte carotte : toutesCarottes) {
            if (!carotte.isEaten()) {
                double distance = position.distanceTo(carotte.getPosition());
                if (distance <= visionRadius) {
                    carottesVisibles.add(carotte);
                }
            }
        }
        
        return carottesVisibles;
    }
    
    // Choisir une carotte cible (pas forcément la plus proche)
    public Carotte choisirCarotteCible(List<Carotte> carottesVisibles) {
        if (carottesVisibles.isEmpty()) return null;
        
        // Choix aléatoire pondéré (plus proche = plus probable)
        List<Double> poids = new ArrayList<>();
        double poidsTotal = 0;
        
        for (Carotte carotte : carottesVisibles) {
            double distance = position.distanceTo(carotte.getPosition());
            double poids_carotte = 1.0 / (distance + 0.5); // Éviter division par 0
            poids.add(poids_carotte);
            poidsTotal += poids_carotte;
        }
        
        // Sélection pondérée
        double rand = random.nextDouble() * poidsTotal;
        double cumul = 0;
        
        for (int i = 0; i < carottesVisibles.size(); i++) {
            cumul += poids.get(i);
            if (rand <= cumul) {
                return carottesVisibles.get(i);
            }
        }
        
        return carottesVisibles.get(0); // Fallback
    }
    
    // Se déplacer vers une carotte
    public void seDeplacerVers(Carotte cible, int gridWidth, int gridHeight) {
        if (cible == null) {
            // Mouvement aléatoire si pas de cible
            mouvementAleatoire(gridWidth, gridHeight);
            return;
        }
        
        Position ciblePos = cible.getPosition();
        int dx = 0, dy = 0;
        
        // Calcul direction (simple)
        if (ciblePos.x > position.x) dx = 1;
        else if (ciblePos.x < position.x) dx = -1;
        
        if (ciblePos.y > position.y) dy = 1;
        else if (ciblePos.y < position.y) dy = -1;
        
        // Mouvement avec vitesse
        if (random.nextDouble() < speed) {
            int newX = Math.max(0, Math.min(gridWidth - 1, position.x + dx));
            int newY = Math.max(0, Math.min(gridHeight - 1, position.y + dy));
            position = new Position(newX, newY);
        }
    }
    
    // Mouvement aléatoire
    private void mouvementAleatoire(int gridWidth, int gridHeight) {
        if (random.nextDouble() < speed * 1.8) { 
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        
        // Parfois faire un mouvement double
        if (random.nextDouble() < 0.25) { // 25% chance
            dx *= 2;
            dy *= 2;
        }
        
        int newX = Math.max(0, Math.min(gridWidth - 1, position.x + dx));
        int newY = Math.max(0, Math.min(gridHeight - 1, position.y + dy));
        position = new Position(newX, newY);
    }
    }
    
    // Essayer de manger une carotte
    public boolean tentativeMangerCarotte(Carotte carotte) {
        if (carotte != null && position.equals(carotte.getPosition())) {
            if (carotte.eat()) {
                pointsToday++;
                return true;
            }
        }
        return false;
    }
    
    // Vérifier survie en fin de journée
    public boolean verifierSurvie() {
        if (pointsToday >= 5) {
            daysSurvived++;
            return true;
        } else {
            isAlive = false;
            if (maison != null) {
                maison.retirerHabitant(this);
            }
            return false;
        }
    }
    
    // Calculer nombre de reproductions possibles
    public int calculerReproductions() {
        return Math.max(0, (pointsToday - 5) / 5); // 1 repro pour chaque 5 points après le seuil
    }
    
    // Reproduction avec vision héritée
    public GardenAgent reproduire(Maison nouvelleMaison, int visionParent) {
        GardenAgent enfant = new GardenAgent(nouvelleMaison.getPosition(), nouvelleMaison, visionParent);
    
        // Héritage avec mutations
        double mutationRate = 0.1;
    
        if (random.nextDouble() < mutationRate) {
            enfant.visionRadius = Math.max(1, visionParent + random.nextInt(3) - 1);
        }
    
        if (random.nextDouble() < mutationRate) {
            enfant.speed = Math.max(0.1, Math.min(2.0, this.speed + (random.nextDouble() - 0.5) * 0.2));
        } else {
            enfant.speed = this.speed;
        }
    
        return enfant;
    }
    
    // Getters et setters
    public int getAgentId() { return agentId; }
    public Position getPosition() { return position; }
    public Maison getMaison() { return maison; }
    public boolean isAlive() { return isAlive; }
    public boolean isInGarden() { return isInGarden; }
    public int getPointsToday() { return pointsToday; }
    public int getDaysSurvived() { return daysSurvived; }
    public int getVisionRadius() { return visionRadius; }
    public double getSpeed() { return speed; }
    
    public void setMaison(Maison maison) { this.maison = maison; }
    public void setPosition(Position position) { this.position = position; }
    
    @Override
    public String toString() {
        return String.format("Agent_%d[%d points, %d jours, vision=%d, vitesse=%.2f]", 
                           agentId, pointsToday, daysSurvived, visionRadius, speed);
    }
}
