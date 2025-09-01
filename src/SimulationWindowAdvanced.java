import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class SimulationWindowAdvanced extends JFrame {
    private Environment environment;
    private SimulationPanel simulationPanel;
    private ControlPanel controlPanel;
    private GraphPanel graphPanel;
    private ParameterPanel parameterPanel;
    private Timer simulationTimer;
    private boolean isRunning = false;
    
    // Paramètres par défaut
    private int gridWidth = 25;
    private int gridHeight = 25;
    private int initialPopulation = 1;
    private int timerDelay = 150; // ms entre générations
    
    public SimulationWindowAdvanced() {
        setTitle("🧬 Simulation d'Évolution de l'Égoïsme - Version Avancée");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        initializeEnvironment();
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
        
        // Taille et position correctes
        pack();
        setLocationRelativeTo(null); // Centrer
        
        // Ajuster la taille si nécessaire
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = Math.min(1200, screenSize.width - 100);
        int height = Math.min(800, screenSize.height - 100);
        setSize(width, height);
        setLocationRelativeTo(null); // Re-centrer après redimensionnement
        
        updateDisplay();
        setVisible(true);
    }
    
    private void initializeEnvironment() {
        environment = new Environment(gridWidth, gridHeight, initialPopulation);
        System.out.println("🎮 Interface graphique avancée initialisée");
    }
    
    private void initializeComponents() {
        simulationPanel = new SimulationPanel(environment);
        controlPanel = new ControlPanel();
        graphPanel = new GraphPanel();
        parameterPanel = new ParameterPanel();
        
        // Timer pour l'animation
        simulationTimer = new Timer(timerDelay, e -> {
            if (isRunning) {
                runOneGeneration();
            }
        });
    }
    
    private void layoutComponents() {
        setLayout(new BorderLayout(5, 5)); // Espacement entre les sections
        
        // Panel central avec la simulation
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Simulation avec scroll - taille fixe raisonnable
        JScrollPane scrollPane = new JScrollPane(simulationPanel);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        scrollPane.setMinimumSize(new Dimension(400, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Graphiques en bas - hauteur fixe
        graphPanel.setPreferredSize(new Dimension(500, 250));
        centerPanel.add(graphPanel, BorderLayout.SOUTH);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Panel de contrôles à droite avec taille fixe
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(280, 0)); // Largeur fixe
        
        // Contrôles en haut du panel droit
        controlPanel.setPreferredSize(new Dimension(270, 200));
        rightPanel.add(controlPanel, BorderLayout.NORTH);
        
        // Paramètres en bas du panel droit
        parameterPanel.setPreferredSize(new Dimension(270, 400));
        rightPanel.add(parameterPanel, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);
        
        // Panel d'informations en bas - hauteur fixe
        JPanel infoPanel = createInfoPanel();
        infoPanel.setPreferredSize(new Dimension(0, 40));
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JLabel info = new JLabel("💡 Interface avancée - Graphiques temps réel et paramètres ajustables");
        info.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(info);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Bouton Démarrer
        controlPanel.getStartButton().addActionListener(e -> startSimulation());
        
        // Bouton Pause
        controlPanel.getPauseButton().addActionListener(e -> pauseSimulation());
        
        // Bouton Reset
        controlPanel.getResetButton().addActionListener(e -> resetSimulation());
        
        // Bouton Étape
        controlPanel.getStepButton().addActionListener(e -> runOneGeneration());
        
        // Écouteur des paramètres avec ChangeListener explicite
        parameterPanel.addParameterChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateSimulationParameters();
            }
        });
    }
    
    private void startSimulation() {
        isRunning = true;
        simulationTimer.start();
        
        controlPanel.getStartButton().setEnabled(false);
        controlPanel.getPauseButton().setEnabled(true);
        controlPanel.getResetButton().setEnabled(true);
        controlPanel.getStepButton().setEnabled(false);
        
        System.out.println("▶️ Simulation avancée démarrée");
    }
    
    private void pauseSimulation() {
        isRunning = false;
        simulationTimer.stop();
        
        controlPanel.getStartButton().setEnabled(true);
        controlPanel.getPauseButton().setEnabled(false);
        controlPanel.getResetButton().setEnabled(true);
        controlPanel.getStepButton().setEnabled(true);
        
        System.out.println("⏸️ Simulation en pause");
    }
    
    private void resetSimulation() {
        pauseSimulation();
        
        // Récupérer les nouveaux paramètres
        gridWidth = parameterPanel.getGridWidth();
        gridHeight = parameterPanel.getGridHeight();
        initialPopulation = parameterPanel.getInitialPopulation();
        timerDelay = parameterPanel.getSpeed();
        
        // Recréer l'environnement
        initializeEnvironment();
        simulationPanel.updateEnvironment(environment);
        graphPanel.clearData();
        
        // Mettre à jour le timer
        simulationTimer.setDelay(timerDelay);
        
        updateDisplay();
        System.out.println("🔄 Simulation réinitialisée avec nouveaux paramètres");
    }
    
    private void runOneGeneration() {
        environment.runOneGeneration();
        updateDisplay();
        
        // Vérifier extinction
        Statistics stats = environment.calculateStatistics();
        if (stats.getTotalAgents() == 0) {
            pauseSimulation();
            JOptionPane.showMessageDialog(this, 
                "💀 Population éteinte à la génération " + environment.getGeneration(),
                "Fin de simulation", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void updateDisplay() {
        Statistics stats = environment.calculateStatistics();
        controlPanel.updateStatistics(stats);
        controlPanel.updateGeneration(environment.getGeneration());
        graphPanel.updateData(environment.getGeneration(), stats);
        simulationPanel.repaint();
        
        // Titre avec informations
        setTitle(String.format("🧬 Simulation Avancée - Gen: %d | Pop: %d | Égoïsme: %.2f", 
                               environment.getGeneration(), 
                               stats.getTotalAgents(),
                               stats.getAverageEgoism()));
    }
    
    private void updateSimulationParameters() {
        // Mettre à jour la vitesse en temps réel
        timerDelay = parameterPanel.getSpeed();
        simulationTimer.setDelay(timerDelay);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("🚀 Lancement de l'interface avancée...");
                new SimulationWindowAdvanced();
                System.out.println("✅ Interface avancée prête !");
            } catch (Exception e) {
                System.err.println("❌ Erreur:");
                e.printStackTrace();
            }
        });
    }
}
