import java.util.*;

public class GardenEnvironmentConfigurable {
    private final int width;
    private final int height;
    private final Random random;
    
    // Collections principales
    private List<GardenAgent> agents;
    private List<Carotte> carottes;
    private List<Maison> maisons;
    
    // Paramètres de simulation (non final pour permettre modification)
    private int maxCarottes = 100;
    private double probabiliteCarotte = 0.6;
    private int dureeJournee = 60;
    private int seuilSurvie = 5;
    private int visionRadius = 7;
    
    // État de simulation
    private int currentTime;
    private int currentDay;
    private int stepInDay;
    private boolean isNight;
    
    // Statistiques
    private int totalCarottesProduites;
    private int totalCarottesMangees;
    
    public GardenEnvironmentConfigurable(int width, int height, int initialPopulation) {
        this.width = width;
        this.height = height;
        this.random = new Random();
        
        this.agents = new ArrayList<>();
        this.carottes = new ArrayList<>();
        this.maisons = new ArrayList<>();
        
        this.currentTime = 0;
        this.currentDay = 1;
        this.stepInDay = 0;
        this.isNight = true;
        
        this.totalCarottesProduites = 0;
        this.totalCarottesMangees = 0;
        
        initialiserMaisons(initialPopulation);
        creerAgentsInitiaux(initialPopulation);
        
        System.out.println("🌱 Jardin créé: " + width + "x" + height);
    }
    
    public void updateParameters(int maxCarottes, double probabiliteCarotte, 
                               int dureeJournee, int seuilSurvie, int visionRadius) {
        this.maxCarottes = maxCarottes;
        this.probabiliteCarotte = probabiliteCarotte;
        this.dureeJournee = dureeJournee;
        this.seuilSurvie = seuilSurvie;
        this.visionRadius = visionRadius;
        
        for (GardenAgent agent : agents) {
            if (agent.isAlive()) {
                agent.setVisionRadius(visionRadius);
            }
        }
    }

    private void initialiserMaisons(int population) {
        int maisonsNecessaires = (int) Math.ceil(population / 10.0);
        
        for (int i = 0; i < maisonsNecessaires; i++) {
            Position pos;
            if (i % 4 == 0) {
                pos = new Position(0, (i / 4) * 3 + 1);
            } else if (i % 4 == 1) {
                pos = new Position(width - 1, (i / 4) * 3 + 1);
            } else if (i % 4 == 2) {
                pos = new Position((i / 4) * 3 + 1, 0);
            } else {
                pos = new Position((i / 4) * 3 + 1, height - 1);
            }
            
            pos = new Position(
                Math.max(0, Math.min(width - 1, pos.x)),
                Math.max(0, Math.min(height - 1, pos.y))
            );
            
            maisons.add(new Maison(pos, i + 1));
        }
    }
    
    private void creerAgentsInitiaux(int population) {
        int agentsCrees = 0;
        
        for (Maison maison : maisons) {
            while (maison.aDelaPlace() && agentsCrees < population) {
                GardenAgent agent = new GardenAgent(maison.getPosition(), maison, visionRadius);
                maison.ajouterHabitant(agent);
                agents.add(agent);
                agentsCrees++;
            }
        }
    }
    
    public void runOneStep() {
        currentTime++;
        stepInDay++;
        
        if (stepInDay >= dureeJournee) {
            finirJournee();
            commencerNouvelleJournee();
        } else if (stepInDay == 1) {
            commencerJournee();
        }
        
        if (!isNight) {
            genererCarottes();
            deplacerAgents();
            gererInteractions();
            nettoyerCarottes();
        }
    }
    
    private void commencerJournee() {
        isNight = false;
        
        for (int i = 0; i<50; i++){
            Position pos = trouverPositionLibre();
            Carotte carotte = new Carotte(pos, currentTime);
            carottes.add(carotte);
            totalCarottesProduites++;
        }

        for (GardenAgent agent : agents) {
            if (agent.isAlive()) {
                Position sortie = trouverPositionLibre();
                agent.sortirJardin(sortie);
            }
        }
    }
    
    private void finirJournee() {
        List<GardenAgent> survivants = new ArrayList<>();
        List<GardenAgent> nouveauxNes = new ArrayList<>();
        
        for (GardenAgent agent : agents) {
            if (agent.isAlive()) {
                agent.rentrerMaison();
                
                if (agent.getPointsToday() >= seuilSurvie) {
                    agent.incrementDaysSurvived();
                    survivants.add(agent);
                    
                    int reproductions = Math.max(0, (agent.getPointsToday() - seuilSurvie) / seuilSurvie);
                    for (int i = 0; i < reproductions; i++) {
                        Maison maisonDisponible = trouverMaisonDisponible();
                        if (maisonDisponible != null) {
                            GardenAgent enfant = agent.reproduire(maisonDisponible, visionRadius);
                            maisonDisponible.ajouterHabitant(enfant);
                            nouveauxNes.add(enfant);
                        }
                    }
                }
            }
        }
        
        agents.clear();
        agents.addAll(survivants);
        agents.addAll(nouveauxNes);
        
        isNight = true;
    }
    
    private void commencerNouvelleJournee() {
        currentDay++;
        stepInDay = 0;
        carottes.clear();
    }
    
    private void genererCarottes() {
        carottes.removeIf(Carotte::isEaten);
        
        while (carottes.size() < maxCarottes && random.nextDouble() < probabiliteCarotte) {
            Position pos = trouverPositionLibre();
            if (pos != null) {
                Carotte carotte = new Carotte(pos, currentTime);
                carottes.add(carotte);
                totalCarottesProduites++;
            }
        }
    }
    
    private void deplacerAgents() {
        for (GardenAgent agent : agents) {
            if (agent.isAlive() && agent.isInGarden()) {
                List<Carotte> carottesVisibles = agent.voirCarottes(carottes);
                Carotte cible = agent.choisirCarotteCible(carottesVisibles);
                agent.seDeplacerVers(cible, width, height);
            }
        }
    }
    
    private void gererInteractions() {
        for (GardenAgent agent : agents) {
            if (agent.isAlive() && agent.isInGarden()) {
                Carotte carotteIci = carottes.stream()
                    .filter(c -> !c.isEaten() && c.getPosition().equals(agent.getPosition()))
                    .findFirst()
                    .orElse(null);
                
                if (carotteIci != null) {
                    if (agent.tentativeMangerCarotte(carotteIci)) {
                        totalCarottesMangees++;
                    }
                }
            }
        }
    }
    
    private void nettoyerCarottes() {
        carottes.removeIf(Carotte::isEaten);
    }
    
    private Position trouverPositionLibre() {
        for (int tentative = 0; tentative < 50; tentative++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Position pos = new Position(x, y);
            
            boolean occupeeParMaison = maisons.stream()
                .anyMatch(m -> m.getPosition().equals(pos));
            
            if (!occupeeParMaison) {
                return pos;
            }
        }
        return new Position(width / 2, height / 2);
    }
    
    private Maison trouverMaisonDisponible() {
        for (Maison maison : maisons) {
            if (maison.aDelaPlace()) {
                return maison;
            }
        }
        
        Position nouvelleMaisonPos = trouverPositionPourMaison();
        if (nouvelleMaisonPos != null) {
            Maison nouvelleMaison = new Maison(nouvelleMaisonPos, maisons.size() + 1);
            maisons.add(nouvelleMaison);
            return nouvelleMaison;
        }
        
        return null;
    }
    
    private Position trouverPositionPourMaison() {
        List<Position> bordPositions = new ArrayList<>();
        
        for (int y = 0; y < height; y++) {
            bordPositions.add(new Position(0, y));
            bordPositions.add(new Position(width - 1, y));
        }
        
        for (int x = 1; x < width - 1; x++) {
            bordPositions.add(new Position(x, 0));
            bordPositions.add(new Position(x, height - 1));
        }
        
        Collections.shuffle(bordPositions);
        for (Position pos : bordPositions) {
            boolean libre = maisons.stream()
                .noneMatch(m -> m.getPosition().equals(pos));
            if (libre) {
                return pos;
            }
        }
        
        return null;
    }
    
    public GardenStatistics calculateStatistics() {
        int agentsVivants = (int) agents.stream().filter(GardenAgent::isAlive).count();
        int agentsJardin = (int) agents.stream().filter(a -> a.isAlive() && a.isInGarden()).count();
        
        double pointsMoyen = agents.stream()
            .filter(GardenAgent::isAlive)
            .mapToInt(GardenAgent::getPointsToday)
            .average()
            .orElse(0.0);
        
        double joursMoyens = agents.stream()
            .filter(GardenAgent::isAlive)
            .mapToInt(GardenAgent::getDaysSurvived)
            .average()
            .orElse(0.0);
        
        return new GardenStatistics(
            agentsVivants,
            agentsJardin,
            carottes.size(),
            currentDay,
            stepInDay,
            pointsMoyen,
            joursMoyens,
            totalCarottesProduites,
            totalCarottesMangees,
            isNight
        );
    }
    
    public List<GardenAgent> getAgents() { return new ArrayList<>(agents); }
    public List<Carotte> getCarottes() { return new ArrayList<>(carottes); }
    public List<Maison> getMaisons() { return new ArrayList<>(maisons); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getCurrentDay() { return currentDay; }
    public int getStepInDay() { return stepInDay; }
    public boolean isNight() { return isNight; }
}
