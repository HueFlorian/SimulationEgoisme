import javax.swing.*;
import java.awt.*;

public class GardenControlPanel extends JPanel {
    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton stepButton;
    
    private JLabel jourLabel;
    private JLabel periodeLabel;
    private JLabel progressionLabel;
    private JLabel populationLabel;
    private JLabel carottesLabel;
    private JLabel pointsLabel;
    private JLabel survieLabel;
    
    private JProgressBar jourProgressBar;
    private JProgressBar survieProgressBar;
    
    public GardenControlPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 250));
        setBackground(new Color(240, 248, 255));
        setBorder(BorderFactory.createTitledBorder("🌱 Contrôles Jardin"));
        
        initializeComponents();
        layoutComponents();
        
        System.out.println("🎮 GardenControlPanel créé avec boutons");
    }
    
    private void initializeComponents() {
        // Boutons de contrôle - PLUS GRANDS ET VISIBLES
        startButton = new JButton("▶️ Commencer Journée");
        pauseButton = new JButton("⏸️ Pause");
        resetButton = new JButton("🔄 Nouveau Jardin");
        stepButton = new JButton("⭐ Un Step");
        
        // Rendre les boutons plus grands et visibles
        Dimension buttonSize = new Dimension(130, 35);
        startButton.setPreferredSize(buttonSize);
        pauseButton.setPreferredSize(buttonSize);
        resetButton.setPreferredSize(buttonSize);
        stepButton.setPreferredSize(buttonSize);
        
        // Style des boutons - couleurs vives
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        pauseButton.setBackground(new Color(255, 193, 7));
        pauseButton.setForeground(Color.BLACK);
        pauseButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        resetButton.setBackground(new Color(244, 67, 54));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        stepButton.setBackground(new Color(33, 150, 243));
        stepButton.setForeground(Color.WHITE);
        stepButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Labels d'information
        jourLabel = new JLabel("Jour: 1");
        periodeLabel = new JLabel("🌙 NUIT");
        progressionLabel = new JLabel("Progression: 0%");
        populationLabel = new JLabel("Population: 0");
        carottesLabel = new JLabel("Carottes: 0");
        pointsLabel = new JLabel("Points moyens: 0.0");
        survieLabel = new JLabel("Taux survie: 0%");
        
        // Barres de progression
        jourProgressBar = new JProgressBar(0, 100);
        jourProgressBar.setStringPainted(true);
        jourProgressBar.setString("Journée");
        jourProgressBar.setForeground(new Color(255, 215, 0));
        
        survieProgressBar = new JProgressBar(0, 100);
        survieProgressBar.setStringPainted(true);
        survieProgressBar.setString("Agents en survie");
        survieProgressBar.setForeground(new Color(34, 139, 34));
        
        System.out.println("🎮 Boutons initialisés:");
        System.out.println("  - Start: " + startButton.getText());
        System.out.println("  - Pause: " + pauseButton.getText());
        System.out.println("  - Reset: " + resetButton.getText());
        System.out.println("  - Step: " + stepButton.getText());
    }
    
    private void layoutComponents() {
        // Panel des boutons - TRÈS VISIBLE
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("🎮 COMMANDES PRINCIPALES"));
        buttonPanel.setBackground(new Color(255, 255, 255));
        
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stepButton);
        buttonPanel.add(resetButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel des informations temporelles
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBorder(BorderFactory.createTitledBorder("⏰ Temps"));
        
        timePanel.add(Box.createVerticalStrut(5));
        timePanel.add(jourLabel);
        timePanel.add(Box.createVerticalStrut(5));
        timePanel.add(periodeLabel);
        timePanel.add(Box.createVerticalStrut(5));
        timePanel.add(progressionLabel);
        timePanel.add(Box.createVerticalStrut(10));
        timePanel.add(new JLabel("Progression journée:"));
        timePanel.add(jourProgressBar);
        timePanel.add(Box.createVerticalStrut(10));
        
        // Panel des statistiques
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("📊 Statistiques"));
        
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(populationLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(carottesLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(pointsLabel);
        statsPanel.add(Box.createVerticalStrut(5));
        statsPanel.add(survieLabel);
        statsPanel.add(Box.createVerticalStrut(10));
        statsPanel.add(new JLabel("Agents en bonne voie:"));
        statsPanel.add(survieProgressBar);
        statsPanel.add(Box.createVerticalStrut(10));
        
        // Panel des règles
        JPanel rulesPanel = createRulesPanel();
        
        // Panel central avec scroll pour tout voir
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(timePanel);
        centerPanel.add(statsPanel);
        centerPanel.add(rulesPanel);
        
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(280, 200));
        
        // Layout principal - BOUTONS EN HAUT
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        System.out.println("📐 Layout des boutons configuré - VISIBLE EN HAUT");
    }
    
    private JPanel createRulesPanel() {
        JPanel rules = new JPanel();
        rules.setLayout(new BoxLayout(rules, BoxLayout.Y_AXIS));
        rules.setBorder(BorderFactory.createTitledBorder("📋 Règles"));
        
        JTextArea rulesText = new JTextArea();
        rulesText.setEditable(false);
        rulesText.setOpaque(false);
        rulesText.setFont(new Font("Arial", Font.PLAIN, 10));
        rulesText.setText(
            "🥕 Les carottes apparaissent aléatoirement\n" +
            "👁️ Les agents voient dans un rayon limité\n" +
            "🎯 Manger une carotte = 1 point\n" +
            "✅ 5+ points = survie + reproduction\n" +
            "❌ <5 points = mort\n" +
            "🏠 Max 10 agents par maison\n" +
            "🌅 1 minute = 1 journée (60 steps)\n" +
            "🌙 Retour maison chaque nuit"
        );
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        
        rules.add(rulesText);
        return rules;
    }
    
    // Méthodes pour mettre à jour l'affichage
    public void updateStatistics(GardenStatistics stats) {
        jourLabel.setText("Jour: " + stats.getJourActuel());
        periodeLabel.setText(stats.getPeriodeJour());
        progressionLabel.setText("Progression: " + stats.getProgressionJour() + "%");
        populationLabel.setText("Population: " + stats.getAgentsVivants() + " (" + stats.getAgentsEnActivite() + " actifs)");
        carottesLabel.setText("Carottes: " + stats.getCarottesDisponibles());
        pointsLabel.setText(String.format("Points moyens: %.1f", stats.getPointsMoyensToday()));
        survieLabel.setText(String.format("Survie moyenne: %.1f jours", stats.getJoursSurvecusMoyens()));
        
        // Mise à jour des barres de progression
        jourProgressBar.setValue(stats.getProgressionJour());
        
        // Couleur de la barre selon période
        if (stats.isNuit()) {
            jourProgressBar.setForeground(new Color(72, 61, 139)); // Bleu nuit
            jourProgressBar.setString("🌙 Nuit - Repos");
        } else {
            jourProgressBar.setForeground(new Color(255, 215, 0)); // Jaune jour
            jourProgressBar.setString("☀️ Jour - Activité");
        }
        
        // Barre de survie (pourcentage d'agents avec 5+ points ou équivalent)
        int agentsEnBonneVoie = (int) (stats.getPointsMoyensToday() * 20); // Approximation
        survieProgressBar.setValue(Math.min(100, Math.max(0, agentsEnBonneVoie)));
        
        if (stats.getPointsMoyensToday() >= 5.0) {
            survieProgressBar.setForeground(new Color(34, 139, 34)); // Vert
        } else if (stats.getPointsMoyensToday() >= 3.0) {
            survieProgressBar.setForeground(new Color(255, 215, 0)); // Jaune
        } else {
            survieProgressBar.setForeground(new Color(220, 20, 60)); // Rouge
        }
    }
    
    // Getters pour les boutons - IMPORTANT !
    public JButton getStartButton() { 
        System.out.println("🔍 getStartButton appelé: " + (startButton != null));
        return startButton; 
    }
    public JButton getPauseButton() { 
        System.out.println("🔍 getPauseButton appelé: " + (pauseButton != null));
        return pauseButton; 
    }
    public JButton getResetButton() { 
        System.out.println("🔍 getResetButton appelé: " + (resetButton != null));
        return resetButton; 
    }
    public JButton getStepButton() { 
        System.out.println("🔍 getStepButton appelé: " + (stepButton != null));
        return stepButton; 
    }
}