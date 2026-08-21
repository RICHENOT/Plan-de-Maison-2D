import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MaisonPanel extends JPanel {
    private double terrainLongueur, terrainLargeur; 
    private double maisonLongueur, maisonLargeur;  
    private double maisonPositionX, maisonPositionY;
    private ArrayList<Piece> pieces;
    private double zoomFactor = 1.0;
    private static final double ZOOM_INCREMENT = 0.1;
    private static final double MIN_ZOOM = 0.5;
    private static final double MAX_ZOOM = 3.0;
    private static final int EPAISSEUR_MUR_PIECE = 6; // 8px pour les murs des pièces

    public MaisonPanel() {
        this.pieces = new ArrayList<>();
        setBackground(Color.WHITE); //Background color
        setOpaque(true);

        this.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                if (e.getWheelRotation() < 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
                revalidate();
                repaint();
            }
        });
    }

    public void zoomIn() {
        if (zoomFactor < MAX_ZOOM) {
            zoomFactor += ZOOM_INCREMENT;
            revalidate();
            repaint();
        }
    }
    
    public void zoomOut() {
        if (zoomFactor > MIN_ZOOM) {
            zoomFactor -= ZOOM_INCREMENT;
            revalidate();
            repaint();
        }
    }


    public double getZoomFactor() {
        return zoomFactor;
    }

    public void setDonnees(double terrainLongueur, double terrainLargeur, 
                        double maisonLongueur, double maisonLargeur, 
                        double maisonPositionX, double maisonPositionY, 
                        ArrayList<Piece> pieces) {

        // Demander confirmation avant ajustement maison/terrain
        if (maisonLongueur > terrainLongueur || maisonLargeur > terrainLargeur) {
            int choix = JOptionPane.showConfirmDialog(this,
                "La maison dépasse le terrain. Ajuster automatiquement?",
                "Confirmation d'ajustement",
                JOptionPane.YES_NO_OPTION);
            
            if (choix == JOptionPane.YES_OPTION) {
                maisonLongueur = Math.min(maisonLongueur, terrainLongueur);
                maisonLargeur = Math.min(maisonLargeur, terrainLargeur);
            }
        }

        this.terrainLongueur = terrainLongueur;
        this.terrainLargeur = terrainLargeur;
        this.maisonLongueur = maisonLongueur;
        this.maisonLargeur = maisonLargeur;
        this.maisonPositionX = maisonPositionX;
        this.maisonPositionY = maisonPositionY;
        this.pieces = pieces != null ? pieces : new ArrayList<>();
        
        ajusterPositionMaison();
        ajusterDimensionsMaison();
        updateSize();
        repaint();
    }

    private void ajusterPositionMaison() {
        // Si la maison dépasse du terrain, ajustez sa position
        double maxX = terrainLongueur - maisonLongueur;
        double maxY = terrainLargeur - maisonLargeur;
        
        maisonPositionX = Math.max(0, Math.min(maisonPositionX, maxX));
        maisonPositionY = Math.max(0, Math.min(maisonPositionY, maxY));
    }
        
    private void updateSize() {
        int baseScale = 40;
        int panelWidth = (int)(terrainLongueur * baseScale * zoomFactor) + 100;
        int panelHeight = (int)(terrainLargeur * baseScale * zoomFactor) + 100;
        
        // Ajustez la taille préférée pour permettre le centrage
        setPreferredSize(new Dimension(
            Math.max(getParent().getWidth(), panelWidth),
            Math.max(getParent().getHeight(), panelHeight)
        ));
        revalidate(); // Important pour actualiser le layout
    }

    public double[] calculerTailleRequise() {
        double maxLongueur = maisonLongueur;
        double maxLargeur = maisonLargeur;
        
        for (Piece p : pieces) {
            if (p.getPosition().equalsIgnoreCase("gauche") || p.getPosition().equalsIgnoreCase("droite")) {
                maxLargeur = Math.max(maxLargeur, p.getLargeur());
            } else {
                maxLongueur = Math.max(maxLongueur, p.getLongueur());
            }
        }
        
        return new double[]{maxLongueur, maxLargeur};
    }

    private void ajusterDimensionsMaison() {
        if (maisonLongueur > terrainLongueur || maisonLargeur > terrainLargeur) {
            double ratioLongueur = terrainLongueur / maisonLongueur;
            double ratioLargeur = terrainLargeur / maisonLargeur;
            double ratioMin = Math.min(ratioLongueur, ratioLargeur);
            
            // Conserver les valeurs décimales
            maisonLongueur = maisonLongueur * ratioMin;
            maisonLargeur = maisonLargeur * ratioMin;
            
            ajusterDimensionsPieces();
        }
    }
    
    private void ajusterDimensionsPieces() {
        for (Piece p : pieces) {
            boolean estCote = p.getPosition().equalsIgnoreCase("gauche") || 
                            p.getPosition().equalsIgnoreCase("droite");
            
            if (estCote) {
                if (p.getLargeur() > maisonLargeur) {
                    double ratio = maisonLargeur / p.getLargeur();
                    p.setLargeur(maisonLargeur);
                    p.setLongueur(p.getLongueur() * ratio);
                }
            } else {
                if (p.getLongueur() > maisonLongueur) {
                    double ratio = maisonLongueur / p.getLongueur();
                    p.setLongueur(maisonLongueur);
                    p.setLargeur(p.getLargeur() * ratio);
                }
            }
        }
    }


    public boolean verifierDebordementMaison() {
        return maisonLongueur > terrainLongueur || maisonLargeur > terrainLargeur;
    }

    public int getRotation(Piece p) {
        return p.getRotation(); 
    }
        
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (terrainLongueur == 0 || terrainLargeur == 0) return;
        
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            // Configuration qualité
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            // Constantes d'échelle
            final int baseScale = 40;
            final double scaled = baseScale * zoomFactor;
            final int epaisseurMur = 5;

            // Calcul des dimensions du terrain en pixels
            int terrainWidthPx = (int)(terrainLongueur * scaled);
            int terrainHeightPx = (int)(terrainLargeur * scaled);

            // Calcul des marges pour centrer
            int marginX = (getWidth() - terrainWidthPx) / 2;
            int marginY = (getHeight() - terrainHeightPx) / 2;

            // Ajustement de la taille du panel
            setPreferredSize(new Dimension(
                Math.max(getWidth(), terrainWidthPx + 50),
                Math.max(getHeight(), terrainHeightPx + 50)
            ));

            // Translation pour centrer le dessin
            g2d.translate(marginX, marginY);

            // --------------------------
            // 1. DESSIN DU TERRAIN
            // --------------------------
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(0, 0, terrainWidthPx, terrainHeightPx);

            // Grille
            g2d.setColor(new Color(220, 220, 220));
            for (int x = 0; x <= terrainLongueur; x++) {
                g2d.drawLine((int)(x * scaled), 0, (int)(x * scaled), terrainHeightPx);
            }
            for (int y = 0; y <= terrainLargeur; y++) {
                g2d.drawLine(0, (int)(y * scaled), terrainWidthPx, (int)(y * scaled));
            }
            
            // Contour du terrain
            g2d.setColor(Color.BLACK);
            g2d.drawRect(0, 0, (int)(terrainLongueur * scaled), (int)(terrainLargeur * scaled));

            // --------------------------
            // 2. DESSIN DE LA MAISON AVEC DIMENSIONS
            // --------------------------
            if (maisonLongueur > 0 && maisonLargeur > 0) {
                int xMaison = (int)(maisonPositionX * scaled);
                int yMaison = (int)(maisonPositionY * scaled);

                // Dessin de la maison
                g2d.setColor(new Color(200, 230, 250));
                g2d.fillRect(xMaison, yMaison, 
                            (int)(maisonLongueur * scaled), 
                            (int)(maisonLargeur * scaled));
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(epaisseurMur/4));
                g2d.drawRect(xMaison, yMaison, 
                            (int)(maisonLongueur * scaled), 
                            (int)(maisonLargeur * scaled));
                g2d.setStroke(new BasicStroke(1));

                // Dimensions de la maison (extérieures)
                drawDimension(g2d, xMaison, yMaison, 
                            xMaison + (int)(maisonLongueur * scaled), yMaison, 
                            String.format("%.2fm", maisonLongueur), true);
                drawDimension(g2d, xMaison, yMaison, 
                            xMaison, yMaison + (int)(maisonLargeur * scaled), 
                            String.format("%.2fm", maisonLargeur), true);

                // --------------------------
                // 3. DESSIN DES PIÈCES (SANS PORTES/FENÊTRES)
                // --------------------------
                if (pieces != null && !pieces.isEmpty()) {
                    for (Piece p : pieces) {
                        double longueurEffective = p.getLongueurEffective();
                        double largeurEffective = p.getLargeurEffective();
                        
                        int pieceWidth = (int)(longueurEffective * scaled);
                        int pieceHeight = (int)(largeurEffective * scaled);
                        
                        int x = xMaison + (int)(p.getPositionX() * scaled);
                        int y = yMaison + (int)(p.getPositionY() * scaled);

                        AffineTransform oldTransform = g2d.getTransform();

                        // Dessin pièce
                        g2d.setColor(new Color(255, 240, 200));
                        g2d.fillRect(x, y, pieceWidth, pieceHeight);
                        g2d.setColor(Color.DARK_GRAY);
                        g2d.setStroke(new BasicStroke(EPAISSEUR_MUR_PIECE));
                        g2d.drawRect(x, y, pieceWidth, pieceHeight);
                        g2d.setStroke(new BasicStroke(1));

                        // Nom et type de la pièce centré
                        String nomComplet = p.getNom() + " (" + p.getType() + ")";
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(nomComplet);
                        int textX = x + (pieceWidth - textWidth) / 2;
                        int textY = y + pieceHeight / 2;

                        g2d.setColor(Color.BLACK);
                        g2d.drawString(nomComplet, textX, textY);

                        // Dimensions intérieures de la pièce
                        drawDimension(g2d, x, y, x + pieceWidth, y, 
                                    String.format("%.2fm", p.getLongueur()), false);
                        drawDimension(g2d, x, y, x, y + pieceHeight, 
                                    String.format("%.2fm", p.getLargeur()), false);

                        g2d.setTransform(oldTransform);
                    }
                }

                // --------------------------
                // 4. DESSIN DES PORTES ET FENÊTRES (EN PREMIER PLAN)
                // --------------------------
                if (pieces != null && !pieces.isEmpty()) {
                    for (Piece p : pieces) {
                        double longueurEffective = p.getLongueurEffective();
                        double largeurEffective = p.getLargeurEffective();
                        
                        int pieceWidth = (int)(longueurEffective * scaled);
                        int pieceHeight = (int)(largeurEffective * scaled);
                        
                        int x = xMaison + (int)(p.getPositionX() * scaled);
                        int y = yMaison + (int)(p.getPositionY() * scaled);

                        AffineTransform oldTransform = g2d.getTransform();

                        // PORTES
                        for (Porte porte : p.getPortes()) {
                            String mur = porte.getMur().toLowerCase();
                            if (p.getRotation() != 0) {
                                mur = ajusterMurPourRotation(mur, p.getRotation()).toLowerCase();
                            }

                            int px = 0, py = 0;
                            switch (mur) {
                                case "haut":
                                    px = x + (int)(porte.getPositionMetres() * scaled);
                                    py = y - epaisseurMur;
                                    break;
                                case "bas":
                                    px = x + (int)(porte.getPositionMetres() * scaled);
                                    py = y + pieceHeight;
                                    break;
                                case "gauche":
                                    px = x - epaisseurMur;
                                    py = y + (int)(porte.getPositionMetres() * scaled);
                                    break;
                                case "droite":
                                    px = x + pieceWidth;
                                    py = y + (int)(porte.getPositionMetres() * scaled);
                                    break;
                            }
                            
                            porte.dessiner(g2d, px, py, epaisseurMur, scaled);
                            
                            // Affichage dimension porte
                            g2d.setColor(Color.RED);
                            g2d.drawString(String.format("%.2fm", porte.getLargeur()), px + 5, py + 15);
                        }

                        // FENÊTRES
                        for (Fenetre fenetre : p.getFenetres()) {
                            String mur = fenetre.getMur().toLowerCase();
                            if (p.getRotation() != 0) {
                                mur = ajusterMurPourRotation(mur, p.getRotation()).toLowerCase();
                            }

                            int fx = 0, fy = 0;
                            switch (mur) {
                                case "haut":
                                    fx = x + (int)(fenetre.getPosition() * scaled);
                                    fy = y;
                                    break;
                                case "bas":
                                    fx = x + (int)(fenetre.getPosition() * scaled);
                                    fy = y + pieceHeight - epaisseurMur;
                                    break;
                                case "gauche":
                                    fx = x;
                                    fy = y + (int)(fenetre.getPosition() * scaled);
                                    break;
                                case "droite":
                                    fx = x + pieceWidth - epaisseurMur;
                                    fy = y + (int)(fenetre.getPosition() * scaled);
                                    break;
                            }
                            
                            fenetre.dessiner(g2d, fx, fy, epaisseurMur, scaled);
                            
                            // Affichage dimension fenêtre
                            g2d.setColor(Color.BLUE);
                            g2d.drawString(String.format("%.2fm", fenetre.getLargeur()), fx + 5, fy + 15);
                        }

                        g2d.setTransform(oldTransform);
                    }
                }
            }

        } finally {
            g2d.dispose();
        }
    }

    private String ajusterMurPourRotation(String murOriginal, int rotation) {
        rotation = ((rotation % 360) + 360) % 360; // Garantit une valeur entre 0 et 360
        int rotations = rotation / 90;
        
        // Mappage des murs et de leurs transformations
        Map<String, String[]> rotationMap = new HashMap<>();
        rotationMap.put("haut", new String[]{"haut", "droite", "bas", "gauche"});
        rotationMap.put("droite", new String[]{"droite", "bas", "gauche", "haut"});
        rotationMap.put("bas", new String[]{"bas", "gauche", "haut", "droite"});
        rotationMap.put("gauche", new String[]{"gauche", "haut", "droite", "bas"});
        
        // Vérifier que le mur original est valide
        if (!rotationMap.containsKey(murOriginal.toLowerCase())) {
            return murOriginal; // Retourne le mur original si non reconnu
        }
        
        // Retourne le mur après rotation
        return rotationMap.get(murOriginal.toLowerCase())[rotations];
    }
  
    private void drawDimension(Graphics2D g2d, int x1, int y1, int x2, int y2, String text, boolean exterieur) {
        int offset = exterieur ? 15: 5; 
        
        if (y1 == y2) {
            // Ligne horizontale
            // Ligne de dimension
            g2d.drawLine(x1, y1 - offset, x2, y2 - offset);
            // Petits traits verticaux aux extrémités
            g2d.drawLine(x1, y1 - offset - 2, x1, y1 - offset + 2);
            g2d.drawLine(x2, y2 - offset - 2, x2, y2 - offset + 2);
            
            // Positionnement du texte
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            int textY = y1 - offset - (exterieur ? 6 : 3);
            
            // Pour l'extérieur, vérifier qu'on ne sort pas du terrain
            if (exterieur) {
                if (textY < 10) textY = y1 - 17; // Si trop haut, mettre en bas
            }
            
            g2d.drawString(text, (x1 + x2) / 2 - textWidth / 2, textY);
        } else { 
            // Ligne verticale
            // Ligne de dimension
            g2d.drawLine(x1 - offset, y1, x1 - offset, y2);
            // Petits traits horizontaux aux extrémités
            g2d.drawLine(x1 - offset - 2, y1, x1 - offset + 2, y1);
            g2d.drawLine(x1 - offset - 2, y2, x1 - offset + 2, y2);
            
            // Positionnement du texte
            int textX = x1 - offset - (exterieur ? 6 : 3);
            
            // Pour l'extérieur, vérifier qu'on ne sort pas du terrain
            if (exterieur) {
                if (textX < 10) textX = x1 - 17; // Si trop à gauche, mettre à droite
            }
            
            // Texte tourné
            Graphics2D g2 = (Graphics2D) g2d.create();
            g2.rotate(-Math.PI/2, textX, (y1 + y2) / 2);
            g2.drawString(text, textX, (y1 + y2) / 2);
            g2.dispose();
        }
    }
    
        
}