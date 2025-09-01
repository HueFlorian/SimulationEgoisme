import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class GardenParameterPanel extends JPanel {
    // Sliders de contrôle
    private JSlider vitesseSlider;
    private JSlider maxCarottesSlider;
    private JSlider probabiliteCarottesSlider;
    private JSlider populationSlider;
    private JSlider seuilSurvieSlider;
    private JSlider visionRadiusSlider;
    private JSlider dureeJourneeSlider;
    
    // Labels d'affichage des valeurs
    private JLabel vitesseLabel;
    private JLabel maxCarottesLabel;
    private JLabel probabiliteCarottesLabel;
    private JLabel populationLabel;
    private JLabel seuilSurvieLabel;
    private JLabel visionRadiusLabel;
    private JLabel dureeJourneeLabel;
    
    // Boutons de preset
    private JButton facileButton;
    private JButton normalButton;
    private JButton difficileButton;
    private JButton expertButton;
    
    // Listener pour les changements
    private ChangeListener parameterChangeListener;
    
    public GardenParameterPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(300, 600));
        setBorder(BorderFactory.createTitledBorder("🎛️ Paramètres de Simulation"));
        setBackground(new Color(248, 249, 250));
        
        initializeComponents();
        layoutComponents();
        setupEventHandlers();
    }
    
    private void initializeComponents() {
        // Vitesse de simulation (délai du timer)
        vitesseSlider = new JSlider(20, 500, 100);
        vitesseSlider.setInverted(true); // Plus haut = plus rapide
        vitesseLabel = new JLabel("Vitesse: Normal (100ms)");
        
        // Nombre maximum de carottes
        maxCarottesSlider = new JSlider(20, 150, 70);
        maxCarottesLabel = new JLabel("Max Carottes: 70");
        
        // Probabilité d'apparition des carottes
        probabiliteCarottesSlider = new JSlider(10, 80, 35);
        probabiliteCarottesLabel = new JLabel("Probabilité Carottes: 35%");
        
        // Population initiale (nécessite reset)
        populationSlider = new JSlider(10, 100, 30);
        populationLabel = new JLabel("Population Initiale: 30");
        
        // Seuil de survie (points nécessaires)
        seuilSurvieSlider = new JSlider(2, 8, 5);
        seuilSurvieLabel = new JLabel("Seuil Survie: 5 points");
        
        // Rayon de vision des agents
        visionRadiusSlider = new JSlider(2, 15, 7);
        visionRadiusLabel = new JLabel("Rayon Vision: 7");
        
        // Durée d'une journée
        dureeJourneeSlider = new JSlider(30, 120, 60);
        dureeJourneeLabel = new JLabel("Durée Journée: 60 steps");
        
        // Configuration des sliders
        configureSlider(vitesseSlider, "Très Lent", "Rapide");
        configureSlider(maxCarottesSlider, "Peu", "Beaucoup");
        configureSlider(probabiliteCarottesSlider, "Rare", "Fréquent");
        configureSlider(populationSlider, "Petit", "Grand");
        configureSlider(seuilSurvieSlider, "Facile", "Difficile");
        configureSlider(visionRadiusSlider, "Myope", "Aigle");
        configureSlider(dureeJourneeSlider, "Courte", "Longue");
        
        // Boutons de preset
        facileButton = new JButton("😊 Facile");
        normalButton = new JButton("😐 Normal");  
        difficileButton = new JButton("😰 Difficile");
        expertButton = new JButton("💀 Expert");
        
        // Couleurs des boutons
        facileButton.setBackground(new Color(76, 175, 80));
        facileButton.setForeground(Color.WHITE);
        normalButton.setBackground(new Color(33, 150, 243));
        normalButton.setForeground(Color.WHITE);
        difficileButton.setBackground(new Color(255, 152, 0));
        difficileButton.setForeground(Color.WHITE);
        expertButton.setBackground(new Color(244, 67, 54));
        expertButton.setForeground(Color.WHITE);
    }
    
    private void configureSlider(JSlider slider, String minLabel, String maxLabel) {
        slider.setMajorTickSpacing((slider.getMaximum() - slider.getMinimum()) / 4);
        slider.setMinorTickSpacing((slider.getMaximum() - slider.getMinimum()) / 8);
        slider.setPaintTicks(true);
        
        // Labels personnalisés aux extrémités
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(slider.getMinimum(), new JLabel(minLabel));
        labels.put(slider.getMaximum(), new JLabel(maxLabel));
        slider.setLabelTable(labels);
        slider.setPaintLabels(true);
        
        slider.setBackground(getBackground());
    }
    
    private void layoutComponents() {
        // Titre et info
        JLabel titleLabel = new JLabel("🎮 Contrôlez la Simulation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(10));
        
        // Presets en haut
        JPanel presetPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        presetPanel.setBorder(BorderFactory.createTitledBorder("🎯 Presets"));
        presetPanel.add(facileButton);
        presetPanel.add(normalButton);
        presetPanel.add(difficileButton);
        presetPanel.add(expertButton);
        add(presetPanel);
        add(Box.createVerticalStrut(10));
        
        // Paramètres dans un panel avec scroll
        JPanel parametersPanel = new JPanel();
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));
        
        // Ajouter chaque paramètre
        addParameterGroup(parametersPanel, "⚡ Vitesse", vitesseLabel, vitesseSlider);
        addParameterGroup(parametersPanel, "🥕 Max Carottes", maxCarottesLabel, maxCarottesSlider);
        addParameterGroup(parametersPanel, "🎲 Génération Carottes", probabiliteCarottesLabel, probabiliteCarottesSlider);
        addParameterGroup(parametersPanel, "👥 Population", populationLabel, populationSlider);
        addParameterGroup(parametersPanel, "💀 Seuil Survie", seuilSurvieLabel, seuilSurvieSlider);
        addParameterGroup(parametersPanel, "👁️ Vision", visionRadiusLabel, visionRadiusSlider);
        addParameterGroup(parametersPanel, "⏰ Durée Journée", dureeJourneeLabel, dureeJourneeSlider);
        
        JScrollPane scrollPane = new JScrollPane(parametersPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(280, 400));
        add(scrollPane);
        
        // Note en bas
        JTextArea noteArea = new JTextArea(3, 20);
        noteArea.setText("💡 Vitesse et paramètres de jeu s'appliquent immédiatement.\n" +
                        "⚠️ Population nécessite un Reset.\n" +
                        "🎯 Utilisez les presets pour des configurations équilibrées.");
        noteArea.setEditable(false);
        noteArea.setOpaque(false);
        noteArea.setFont(new Font("Arial", Font.ITALIC, 10));
        noteArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(noteArea);
    }
    
    private void addParameterGroup(JPanel parent, String title, JLabel valueLabel, JSlider slider) {
        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBorder(BorderFactory.createTitledBorder(title));
        group.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        slider.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        group.add(valueLabel);
        group.add(Box.createVerticalStrut(5));
        group.add(slider);
        
        parent.add(group);
        parent.add(Box.createVerticalStrut(5));
    }
    
    private void setupEventHandlers() {
        vitesseSlider.addChangeListener(e -> {
            String speedText = getSpeedText(vitesseSlider.getValue());
            vitesseLabel.setText("Vitesse: " + speedText + " (" + vitesseSlider.getValue() + "ms)");
            notifyParameterChange();
        });
        
        maxCarottesSlider.addChangeListener(e -> {
            maxCarottesLabel.setText("Max Carottes: " + maxCarottesSlider.getValue());
            notifyParameterChange();
        });
        
        probabiliteCarottesSlider.addChangeListener(e -> {
            probabiliteCarottesLabel.setText("Probabilité Carottes: " + probabiliteCarottesSlider.getValue() + "%");
            notifyParameterChange();
        });
        
        populationSlider.addChangeListener(e -> {
            populationLabel.setText("Population Initiale: " + populationSlider.getValue() + " (Reset requis)");
            notifyParameterChange();
        });
        
        seuilSurvieSlider.addChangeListener(e -> {
            seuilSurvieLabel.setText("Seuil Survie: " + seuilSurvieSlider.getValue() + " points");
            notifyParameterChange();
        });
        
        visionRadiusSlider.addChangeListener(e -> {
            visionRadiusLabel.setText("Rayon Vision: " + visionRadiusSlider.getValue());
            notifyParameterChange();
        });
        
        dureeJourneeSlider.addChangeListener(e -> {
            dureeJourneeLabel.setText("Durée Journée: " + dureeJourneeSlider.getValue() + " steps");
            notifyParameterChange();
        });
        
        // Presets
        facileButton.addActionListener(e -> applyPreset("facile"));
        normalButton.addActionListener(e -> applyPreset("normal"));
        difficileButton.addActionListener(e -> applyPreset("difficile"));
        expertButton.addActionListener(e -> applyPreset("expert"));
    }
    
    private String getSpeedText(int value) {
        if (value <= 50) return "Très Rapide";
        else if (value <= 100) return "Rapide";
        else if (value <= 200) return "Normal";
        else if (value <= 300) return "Lent";
        else return "Très Lent";
    }
    
    private void applyPreset(String preset) {
        switch (preset) {
            case "facile":
                vitesseSlider.setValue(150);
                maxCarottesSlider.setValue(100);
                probabiliteCarottesSlider.setValue(50);
                populationSlider.setValue(20);
                seuilSurvieSlider.setValue(3);
                visionRadiusSlider.setValue(10);
                dureeJourneeSlider.setValue(80);
                break;
            case "normal":
                vitesseSlider.setValue(100);
                maxCarottesSlider.setValue(70);
                probabiliteCarottesSlider.setValue(35);
                populationSlider.setValue(30);
                seuilSurvieSlider.setValue(5);
                visionRadiusSlider.setValue(7);
                dureeJourneeSlider.setValue(60);
                break;
            case "difficile":
                vitesseSlider.setValue(80);
                maxCarottesSlider.setValue(50);
                probabiliteCarottesSlider.setValue(25);
                populationSlider.setValue(40);
                seuilSurvieSlider.setValue(6);
                visionRadiusSlider.setValue(5);
                dureeJourneeSlider.setValue(50);
                break;
            case "expert":
                vitesseSlider.setValue(60);
                maxCarottesSlider.setValue(30);
                probabiliteCarottesSlider.setValue(15);
                populationSlider.setValue(50);
                seuilSurvieSlider.setValue(8);
                visionRadiusSlider.setValue(3);
                dureeJourneeSlider.setValue(40);
                break;
        }
        notifyParameterChange();
    }
    
    private void notifyParameterChange() {
        if (parameterChangeListener != null) {
            parameterChangeListener.stateChanged(null);
        }
    }
    
    // Getters pour les valeurs
    public int getVitesse() { return vitesseSlider.getValue(); }
    public int getMaxCarottes() { return maxCarottesSlider.getValue(); }
    public double getProbabiliteCarottes() { return probabiliteCarottesSlider.getValue() / 100.0; }
    public int getPopulationInitiale() { return populationSlider.getValue(); }
    public int getSeuilSurvie() { return seuilSurvieSlider.getValue(); }
    public int getVisionRadius() { return visionRadiusSlider.getValue(); }
    public int getDureeJournee() { return dureeJourneeSlider.getValue(); }
    
    // Setter pour le listener
    public void addParameterChangeListener(ChangeListener listener) {
        this.parameterChangeListener = listener;
    }
}
