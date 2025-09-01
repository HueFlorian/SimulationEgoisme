public class GardenStatistics {
    private int agentsVivants;
    private int agentsEnActivite;
    private int carottesDisponibles;
    private int jourActuel;
    private int stepDansJour;
    private double pointsMoyensToday;
    private double joursSurvecusMoyens;
    private int totalCarottesProduites;
    private int totalCarottesMangees;
    private boolean isNuit;
    
    public GardenStatistics(int agentsVivants, int agentsEnActivite, int carottesDisponibles,
                           int jourActuel, int stepDansJour, double pointsMoyensToday,
                           double joursSurvecusMoyens, int totalCarottesProduites,
                           int totalCarottesMangees, boolean isNuit) {
        this.agentsVivants = agentsVivants;
        this.agentsEnActivite = agentsEnActivite;
        this.carottesDisponibles = carottesDisponibles;
        this.jourActuel = jourActuel;
        this.stepDansJour = stepDansJour;
        this.pointsMoyensToday = pointsMoyensToday;
        this.joursSurvecusMoyens = joursSurvecusMoyens;
        this.totalCarottesProduites = totalCarottesProduites;
        this.totalCarottesMangees = totalCarottesMangees;
        this.isNuit = isNuit;
    }
    
    // Getters
    public int getAgentsVivants() { return agentsVivants; }
    public int getAgentsEnActivite() { return agentsEnActivite; }
    public int getCarottesDisponibles() { return carottesDisponibles; }
    public int getJourActuel() { return jourActuel; }
    public int getStepDansJour() { return stepDansJour; }
    public double getPointsMoyensToday() { return pointsMoyensToday; }
    public double getJoursSurvecusMoyens() { return joursSurvecusMoyens; }
    public int getTotalCarottesProduites() { return totalCarottesProduites; }
    public int getTotalCarottesMangees() { return totalCarottesMangees; }
    public boolean isNuit() { return isNuit; }
    
    // Méthodes utiles
    public String getPeriodeJour() {
        return isNuit ? "🌙 NUIT" : "☀️ JOUR";
    }
    
    public int getProgressionJour() {
        return (stepDansJour * 100) / 60; // Pourcentage du jour écoulé
    }
    
    public double getTauxSurvie() {
        return totalCarottesMangees > 0 ? (double) totalCarottesMangees / totalCarottesProduites * 100 : 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s - Jour %d (%d%%) | Agents: %d actifs/%d total | Carottes: %d | Points moy: %.1f", 
                           getPeriodeJour(), jourActuel, getProgressionJour(), 
                           agentsEnActivite, agentsVivants, carottesDisponibles, pointsMoyensToday);
    }
}
