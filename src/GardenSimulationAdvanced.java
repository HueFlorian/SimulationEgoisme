import javax.swing.*;
import java.awt.*;

public class GardenSimulationAdvanced extends JFrame {
    private GardenEnvironmentConfigurable environment;
    private GardenPanel gardenPanel;
    private GardenControlPanel controlPanel;
    private GardenParameterPanel parameterPanel;
    private Timer simulationTimer;
    private boolean isRunning = false;
    
    // Paramètres par défaut
    private final int DEFAULT_WIDTH = 30;
    private final int DEFAULT_HEIGHT = 25;
    
    public GardenSimulationAdvanced() {
        setTitle("🌱🎛️ Simulation Jardin - Contrôles Avancés");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran pour tout voir
        
        initializeEnvironment();
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        
        updateDisplay();
        setVisible(true);
        
        System.out.println("🎛️ Interface avancée lancée avec contrôles complets");
        System.out.println("🎮 Boutons disponibles dans le panneau de droite");
    }
    
    private void initializeEnvironment() {
        // Utiliser les paramètres par défaut du panneau
        environment = new GardenEnvironmentConfigurable(DEFAULT_WIDTH, DEFAULT_HEIGHT, 30);
        System.out.println("🌱 Environnement configurable créé");
    }
    
    private void initializeComponents() {
        gardenPanel = new GardenPanel(environment);
        controlPanel = new GardenControlPanel();
        parameterPanel = new GardenParameterPanel();
        
        // Timer avec délai par défaut
        simulationTimer = new Timer(100, e -> {
            if (isRunning) {
                runOneStep();
            }
        });
        
        // S'assurer que les boutons sont créés et visibles
        System.out.println("🔍 Vérification des boutons:");
        System.out.println("  - Start Button: " + (controlPanel.getStartButton() != null));
        System.out.println("  - Pause Button: " + (controlPanel.getPauseButton() != null));
        System.out.println("  - Reset Button: " + (controlPanel.getResetButton() != null));
        System.out.println("  - Step Button: " + (controlPanel.getStepButton() != null));
        
        // Activer les boutons initiaux
        controlPanel.getStartButton().setEnabled(true);
        controlPanel.getPauseButton().setEnabled(false);
        controlPanel.getResetButton().setEnabled(true);
        controlPanel.getStepButton().setEnabled(true);
        
        System.out.println("🎮 Composants avancés initialisés");
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5));
        
        // Panel central avec le jardin
        JPanel centerPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(gardenPanel);
        scrollPane.setPreferredSize(new Dimension(700, 600));
        scrollPane.setBorder(BorderFactory.createTitledBorder("🌱 Jardin Contrôlé"));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel de droite avec contrôles ET paramètres
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(320, 0)); // Plus large pour visibilité
        rightPanel.setBorder(BorderFactory.createTitledBorder("🎛️ CONTRÔLES & PARAMÈTRES"));
        
        // Contrôles en haut - plus visible
        controlPanel.setPreferredSize(new Dimension(300, 250));
        controlPanel.setBorder(BorderFactory.createTitledBorder("🎮 COMMANDES PRINCIPALES"));
        rightPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Paramètres au centre (avec scroll)
        JScrollPane paramScrollPane = new JScrollPane(parameterPanel);
        paramScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paramScrollPane.setBorder(BorderFactory.createTitledBorder("⚙️ PARAMÈTRES"));
        rightPanel.add(paramScrollPane, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);
        
        // Panel d'information en bas - instructions claires
        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.setBackground(new Color(245, 245, 220));
        infoPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JLabel infoLabel = new JLabel("🎮 UTILISEZ LES BOUTONS À DROITE → Démarrer, Pause, Reset, Étape | Ajustez paramètres en temps réel !");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 12));
        infoLabel.setForeground(new Color(0, 100, 0));
        infoPanel.add(infoLabel);
        
        add(infoPanel, BorderLayout.SOUTH);
        
        System.out.println("📐 Layout configuré avec panneau de contrôle visible");
    }
    
    private void setupEventHandlers() {
        // Boutons de contrôle avec messages de debug
        controlPanel.getStartButton().addActionListener(e -> {
            System.out.println("🎮 Bouton DÉMARRER cliqué !");
            if (!isRunning) {
                startSimulation();
            }
        });
        
        controlPanel.getPauseButton().addActionListener(e -> {
            System.out.println("🎮 Bouton PAUSE cliqué !");
            pauseSimulation();
        });
        
        controlPanel.getResetButton().addActionListener(e -> {
            System.out.println("🎮 Bouton RESET cliqué !");
            resetSimulation();
        });
        
        controlPanel.getStepButton().addActionListener(e -> {
            System.out.println("🎮 Bouton ÉTAPE cliqué !");
            runOneStep();
        });
        
        // Changements de paramètres en temps réel
        parameterPanel.addParameterChangeListener(e -> {
            System.out.println("⚙️ Paramètres modifiés");
            updateSimulationParameters();
        });
        
        System.out.println("🔗 Event handlers configurés");
    }
    
    private void startSimulation() {
        isRunning = true;
        simulationTimer.start();
        
        controlPanel.getStartButton().setEnabled(false);
        controlPanel.getPauseButton().setEnabled(true);
        controlPanel.getResetButton().setEnabled(true);
        controlPanel.getStepButton().setEnabled(false);
        
        // Changer le texte du bouton start
        controlPanel.getStartButton().setText("▶️ En cours...");
        
        System.out.println("▶️ Simulation avancée démarrée");
    }
    
    private void pauseSimulation() {
        isRunning = false;
        simulationTimer.stop();
        
        controlPanel.getStartButton().setEnabled(true);
        controlPanel.getStartButton().setText("▶️ Continuer");
        controlPanel.getPauseButton().setEnabled(false);
        controlPanel.getResetButton().setEnabled(true);
        controlPanel.getStepButton().setEnabled(true);
        
        System.out.println("⏸️ Simulation avancée en pause");
    }
    
    private void resetSimulation() {
        pauseSimulation();
        
        // Recréer avec la population du panneau de paramètres
        int newPopulation = parameterPanel.getPopulationInitiale();
        environment = new GardenEnvironmentConfigurable(DEFAULT_WIDTH, DEFAULT_HEIGHT, newPopulation);
        
        // Appliquer immédiatement tous les paramètres
        updateSimulationParameters();
        
        // Mettre à jour l'affichage
        gardenPanel.updateEnvironment(environment);
        controlPanel.getStartButton().setText("▶️ Commencer Journée");
        
        updateDisplay();
        
        System.out.println("🔄 Simulation avancée réinitialisée avec population: " + newPopulation);
    }
    
    private void updateSimulationParameters() {
        // Mettre à jour la vitesse du timer
        int newDelay = parameterPanel.getVitesse();
        simulationTimer.setDelay(newDelay);
        
        // Mettre à jour les paramètres de l'environnement
        environment.updateParameters(
            parameterPanel.getMaxCarottes(),
            parameterPanel.getProbabiliteCarottes(),
            parameterPanel.getDureeJournee(),
            parameterPanel.getSeuilSurvie(),
            parameterPanel.getVisionRadius()
        );
        
        System.out.println("🔧 Paramètres appliqués - Timer: " + newDelay + "ms");
    }
    
    private void runOneStep() {
        environment.runOneStep();
        updateDisplay();
        
        GardenStatistics stats = environment.calculateStatistics();
        if (stats.getAgentsVivants() == 0) {
            pauseSimulation();
            
            JOptionPane.showMessageDialog(this, 
                String.format("💀 Extinction de la population au jour %d !\n" +
                             "Paramètres actuels:\n" +
                             "• Seuil survie: %d points\n" +
                             "• Max carottes: %d\n" +
                             "• Probabilité: %.0f%%\n\n" +
                             "💡 Essayez le preset 'Facile' !",
                             stats.getJourActuel(),
                             parameterPanel.getSeuilSurvie(),
                             parameterPanel.getMaxCarottes(),
                             parameterPanel.getProbabiliteCarottes() * 100),
                "Extinction - Ajustez les paramètres", 
                JOptionPane.WARNING_MESSAGE);
        }
        
        // Messages d'événements spéciaux
        if (stats.getJourActuel() > 1 && stats.getStepDansJour() == 1) {
            System.out.println("🌅 Jour " + stats.getJourActuel() + " - " + 
                             stats.getAgentsVivants() + " agents actifs");
        }
    }
    
    private void updateDisplay() {
        GardenStatistics stats = environment.calculateStatistics();
        controlPanel.updateStatistics(stats);
        gardenPanel.repaint();
        
        // Titre détaillé avec paramètres actuels
        setTitle(String.format("🌱🎛️ Jardin Contrôlé - %s | J%d | Pop:%d | 🥕%d | Seuil:%d | Vision:%d", 
                               stats.getPeriodeJour(),
                               stats.getJourActuel(),
                               stats.getAgentsVivants(),
                               stats.getCarottesDisponibles(),
                               parameterPanel.getSeuilSurvie(),
                               parameterPanel.getVisionRadius()));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🎛️ Lancement de la simulation jardin avancée...");
                new GardenSimulationAdvanced();
                System.out.println("✅ Interface avancée prête ! Utilisez les contrôles pour expérimenter.");
                System.out.println("🎮 Boutons visibles dans le panneau de droite !");
            } catch (Exception e) {
                System.err.println("❌ Erreur lors du lancement:");
                e.printStackTrace();
            }
        });
    }
}