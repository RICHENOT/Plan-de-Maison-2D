import java.awt.*;
import java.io.Serializable;

import javax.swing.JOptionPane;

public class Fenetre implements Serializable {
    private String mur; // "gauche", "droite", "haut", "bas"
    private double position; // position sur le mur en mètres
    private double largeur; // largeur en mètres
    private static final long serialVersionUID = 1L;

    public Fenetre(String mur, double position, double largeur) {
        this.mur = mur;
        this.position = position;
        this.largeur = largeur;
    }

    public void dessiner(Graphics2D g2d, int x, int y, int epaisseurMur, double scale) {
        g2d.setColor(new Color(255, 240, 200)); //  pour les fenêtres
        g2d.setStroke(new BasicStroke(epaisseurMur));
        
        int largeurPixels = (int)(largeur * scale);
        
        switch (mur.toLowerCase()) {
            case "gauche":
                // Ligne verticale centrée sur le mur gauche
                g2d.drawLine(x, y, 
                            x , y + largeurPixels);
                break;
                
            case "droite":
                // Ligne verticale centrée sur le mur droit
                g2d.drawLine(x + epaisseurMur, y, 
                            x + epaisseurMur, y + largeurPixels);
                break;
                
            case "haut":
                // Ligne horizontale centrée sur le mur haut
                g2d.drawLine(x, y, 
                            x + largeurPixels, y);
                break;
                
            case "bas":
                // Ligne horizontale centrée sur le mur bas
                g2d.drawLine(x, y + epaisseurMur, 
                            x + largeurPixels, y + epaisseurMur);
                break;
        }
    }


    public boolean validerEtAjusterFenetre(double longueurPiece, double largeurPiece, Component parent) {
        // Calculer la dimension maximale selon le mur
        double dimensionMur = (mur.equalsIgnoreCase("haut") || mur.equalsIgnoreCase("bas")) 
                ? longueurPiece : largeurPiece;
        
        // Vérification si un ajustement est nécessaire
        boolean ajustementNecessaire = (largeur > dimensionMur) || 
                                    (position > (dimensionMur - largeur)) || 
                                    (position < 0);
        
        if (!ajustementNecessaire) {
            return false; // Aucun ajustement nécessaire
        }
        
        // Demander confirmation à l'utilisateur
        int choix = JOptionPane.showConfirmDialog(parent,
            "La fenêtre dépasse les dimensions du mur.\nVoulez-vous l'ajuster automatiquement?",
            "Ajustement nécessaire",
            JOptionPane.YES_NO_OPTION);
        
        if (choix == JOptionPane.YES_OPTION) {
            if (largeur > dimensionMur) {
                largeur = dimensionMur;
            }
            
            double positionMax = dimensionMur - largeur;
            if (position > positionMax) {
                position = positionMax;
            }
            if (position < 0) {
                position = 0;
            }
            return true;
        }
        
        return false; // L'utilisateur a choisi de ne pas ajuster
    }

    // Getters et setters
    public String getMur() { return mur; }
    public double getPosition() { return position; }
    public double getLargeur() { return largeur; }
    public void setPosition(double position) { this.position = position; }
    public void setLargeur(double largeur) { this.largeur = largeur; }
}