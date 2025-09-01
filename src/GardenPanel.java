import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GardenPanel extends JPanel {
    private GardenEnvironmentConfigurable environment;
    private final int cellSize = 20;
    
    public GardenPanel(GardenEnvironmentConfigurable environment) {
        this.environment = environment;
        setPreferredSize(new Dimension(
            environment.getWidth() * cellSize,
            environment.getHeight() * cellSize
        ));
        setBackground(new Color(34, 139, 34)); // Vert jardin
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawBackground(g2d);
        drawMaisons(g2d);
        drawCarottes(g2d);
        drawAgents(g2d);
        drawOverlay(g2d);
    }
    
    private void drawBackground(Graphics2D g) {
        GardenStatistics stats = environment.calculateStatistics();
        
        // Couleur de fond selon jour/nuit
        if (stats.isNuit()) {
            setBackground(new Color(25, 25, 50)); // Bleu nuit
        } else {
            setBackground(new Color(34, 139, 34)); // Vert jour
        }
        
        // Grille subtile
        g.setColor(new Color(255, 255, 255, 20));
        for (int x = 0; x <= environment.getWidth(); x++) {
            g.drawLine(x * cellSize, 0, x * cellSize, getHeight());
        }
        for (int y = 0; y <= environment.getHeight(); y++) {
            g.drawLine(0, y * cellSize, getWidth(), y * cellSize);
        }
    }
    
    private void drawMaisons(Graphics2D g) {
        List<Maison> maisons = environment.getMaisons();
        
        for (Maison maison : maisons) {
            Position pos = maison.getPosition();
            int x = pos.x * cellSize + 2;
            int y = pos.y * cellSize + 2;
            int size = cellSize - 4;
            
            // Couleur selon occupation
            double occupation = (double) maison.getNombreHabitants() / maison.getCapaciteMax();
            if (occupation > 0.8) {
                g.setColor(new Color(139, 69, 19)); // Marron foncé (pleine)
            } else if (occupation > 0.5) {
                g.setColor(new Color(160, 82, 45)); // Marron moyen
            } else {
                g.setColor(new Color(210, 180, 140)); // Beige (vide)
            }
            
            // Dessiner la maison
            g.fillRect(x, y, size, size);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, size, size);
            
            // Toit triangulaire
            g.setColor(new Color(178, 34, 34)); // Rouge brique
            int[] xPoints = {x, x + size/2, x + size};
            int[] yPoints = {y, y - 6, y};
            g.fillPolygon(xPoints, yPoints, 3);
            g.setColor(Color.BLACK);
            g.drawPolygon(xPoints, yPoints, 3);
            
            // Nombre d'habitants
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 10));
            String habitants = String.valueOf(maison.getNombreHabitants());
            FontMetrics fm = g.getFontMetrics();
            int textX = x + (size - fm.stringWidth(habitants)) / 2;
            int textY = y + (size + fm.getAscent()) / 2;
            g.drawString(habitants, textX, textY);
        }
    }
    
    private void drawCarottes(Graphics2D g) {
        List<Carotte> carottes = environment.getCarottes();
        
        g.setColor(new Color(255, 140, 0)); // Orange carotte
        
        for (Carotte carotte : carottes) {
            if (!carotte.isEaten()) {
                Position pos = carotte.getPosition();
                int x = pos.x * cellSize + 6;
                int y = pos.y * cellSize + 6;
                int size = cellSize - 12;
                
                // Corps de la carotte (triangle)
                int[] xPoints = {x + size/2, x, x + size};
                int[] yPoints = {y, y + size, y + size};
                g.fillPolygon(xPoints, yPoints, 3);
                
                // Feuilles (petit rectangle vert)
                g.setColor(new Color(0, 128, 0));
                g.fillRect(x + size/3, y - 3, size/3, 4);
                
                // Contour
                g.setColor(Color.BLACK);
                g.drawPolygon(xPoints, yPoints, 3);
                
                g.setColor(new Color(255, 140, 0)); // Reset couleur
            }
        }
    }
    
    private void drawAgents(Graphics2D g) {
        List<GardenAgent> agents = environment.getAgents();
        
        for (GardenAgent agent : agents) {
            if (agent.isAlive()) {
                Position pos = agent.getPosition();
                int x = pos.x * cellSize + 4;
                int y = pos.y * cellSize + 4;
                int size = cellSize - 8;
                
                // Couleur selon l'état
                Color couleurAgent;
                if (agent.isInGarden()) {
                    // Dans le jardin : couleur selon points
                    int points = agent.getPointsToday();
                    if (points >= 5) {
                        couleurAgent = new Color(0, 255, 0); // Vert (survie assurée)
                    } else if (points >= 3) {
                        couleurAgent = new Color(255, 255, 0); // Jaune (en bonne voie)
                    } else if (points >= 1) {
                        couleurAgent = new Color(255, 165, 0); // Orange (quelques points)
                    } else {
                        couleurAgent = new Color(255, 0, 0); // Rouge (en danger)
                    }
                } else {
                    couleurAgent = new Color(100, 100, 255); // Bleu (à la maison)
                }
                
                g.setColor(couleurAgent);
                g.fillOval(x, y, size, size);
                
                // Contour plus foncé
                g.setColor(couleurAgent.darker());
                g.drawOval(x, y, size, size);
                
                // Rayon de vision si dans le jardin
                if (agent.isInGarden()) {
                    g.setColor(new Color(couleurAgent.getRed(), couleurAgent.getGreen(), 
                                       couleurAgent.getBlue(), 30));
                    int visionRadius = agent.getVisionRadius();
                    int visionSize = visionRadius * cellSize * 2;
                    int visionX = pos.x * cellSize + cellSize/2 - visionSize/2;
                    int visionY = pos.y * cellSize + cellSize/2 - visionSize/2;
                    g.fillOval(visionX, visionY, visionSize, visionSize);
                }
                
                // Points aujourd'hui (petit texte)
                if (agent.isInGarden() && agent.getPointsToday() > 0) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.BOLD, 8));
                    g.drawString(String.valueOf(agent.getPointsToday()), 
                               x + size - 8, y + 8);
                }
            }
        }
    }
    
    private void drawOverlay(Graphics2D g) {
        GardenStatistics stats = environment.calculateStatistics();
        
        // Info en haut à gauche
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        
        String periode = stats.getPeriodeJour();
        String jour = "Jour " + stats.getJourActuel();
        String progression = stats.getProgressionJour() + "%";
        
        g.drawString(periode + " - " + jour + " (" + progression + ")", 10, 20);
        
        // Légende en bas
        drawLegend(g);
    }
    
    private void drawLegend(Graphics2D g) {
        int legendY = getHeight() - 60;
        int legendX = 10;
        
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(legendX - 5, legendY - 15, 300, 50);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Légende agents
        g.drawString("Agents: ", legendX, legendY);
        
        g.setColor(new Color(255, 0, 0));
        g.fillOval(legendX + 50, legendY - 8, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString("Danger", legendX + 62, legendY);
        
        g.setColor(new Color(255, 165, 0));
        g.fillOval(legendX + 95, legendY - 8, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString("Faim", legendX + 107, legendY);
        
        g.setColor(new Color(0, 255, 0));
        g.fillOval(legendX + 135, legendY - 8, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString("Rassasié", legendX + 147, legendY);
        
        // Légende objets
        legendY += 15;
        g.drawString("Objets: ", legendX, legendY);
        
        // Maison
        g.setColor(new Color(160, 82, 45));
        g.fillRect(legendX + 50, legendY - 8, 8, 8);
        g.setColor(Color.WHITE);
        g.drawString("Maison", legendX + 62, legendY);
        
        // Carotte
        g.setColor(new Color(255, 140, 0));
        int[] xPoints = {legendX + 99, legendX + 95, legendX + 103};
        int[] yPoints = {legendY - 8, legendY, legendY};
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.WHITE);
        g.drawString("Carotte", legendX + 107, legendY);
    }
    
    public void updateEnvironment(GardenEnvironmentConfigurable environment) {
        this.environment = environment;
        repaint();
    }
}
