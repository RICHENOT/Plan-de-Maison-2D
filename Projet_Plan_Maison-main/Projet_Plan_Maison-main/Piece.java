import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Piece implements Serializable {
    private String nom;
    private String type;
    private double longueur; 
    private double largeur;  
    private String position;
    private ArrayList<Porte> portes;
    private ArrayList<Fenetre> fenetres;
    private double positionX; 
    private double positionY; 
    private int rotation = 0; 
    // constructeur
    public Piece(String nom, String type, double longueur, double largeur, String position, ArrayList<Porte> portes) {
        this.nom = nom;
        this.type = type;
        this.longueur = longueur;
        this.largeur = largeur;
        this.position = position;
        this.portes = portes != null ? portes : new ArrayList<>();
        this.positionX = 0;
        this.positionY = 0;
    }

    public boolean besoinAjustementMaison(double maisonLongueur, double maisonLargeur) {
        return (positionX + getLongueurEffective() > maisonLongueur) || 
            (positionY + getLargeurEffective() > maisonLargeur);
    }

    public void ajusterPosition(double maisonLongueur, double maisonLargeur) {
        this.positionX = Math.max(0, Math.min(positionX, maisonLongueur - getLongueurEffective()));
        this.positionY = Math.max(0, Math.min(positionY, maisonLargeur - getLargeurEffective()));
    }


    public boolean verifierDebordement(double maisonLongueur, double maisonLargeur) {
        // Calcul des positions effectives en tenant compte de la rotation
        double longueurEffective = (rotation % 180 == 0) ? longueur : largeur;
        double largeurEffective = (rotation % 180 == 0) ? largeur : longueur;
        
        return positionX < 0 || 
            positionY < 0 || 
            (positionX + longueurEffective) > maisonLongueur || 
            (positionY + largeurEffective) > maisonLargeur;
    }

    public boolean verifierDebordementFenetres() {
        if (fenetres == null) return false;
        
        double longueur = getLongueurEffective();
        double largeur = getLargeurEffective();
        
        for (Fenetre fenetre : fenetres) {
            String mur = fenetre.getMur();
            double dimensionMur = mur.equalsIgnoreCase("haut") || mur.equalsIgnoreCase("bas") 
                ? longueur : largeur;
            
            if (fenetre.getLargeur() > dimensionMur || 
                fenetre.getPosition() < 0 || 
                (fenetre.getPosition() + fenetre.getLargeur()) > dimensionMur) {
                return true;
            }
        }
        return false;
    }

    public double getLongueurEffective() {
        return (rotation % 180 == 0) ? longueur : largeur;
    }

    public double getLargeurEffective() {
        return (rotation % 180 == 0) ? largeur : longueur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLongueur() {
        return longueur;
    }

    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ArrayList<Porte> getPortes() {
        return portes;
    }

    // Getter et setter
    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        // Normalise la rotation entre 0 et 360 degrés
        this.rotation = ((rotation % 360) + 360) % 360;
    }

    public void setPortes(ArrayList<Porte> portes) {
    this.portes = portes != null ? portes : new ArrayList<>();
    }

    public void ajouterPorte(Porte porte) {
        if (portes == null) {
            portes = new ArrayList<>();
        }
        portes.add(porte);
    }

    

    public ArrayList<Fenetre> getFenetres() {
        if (fenetres == null) {
            fenetres = new ArrayList<>();
        }
        return fenetres;
    }

    public void setFenetres(ArrayList<Fenetre> fenetres) {
        this.fenetres = fenetres;
    }

    public void ajouterFenetre(Fenetre fenetre) {
        getFenetres().add(fenetre);
    }




     // Méthode pour créer une porte depuis le formulaire
    public static Porte creerPorteDepuisChamps(
        JComboBox<String> murBox, 
        JTextField positionField,
        JComboBox<String> directionBox,
        JComboBox<String> typeBox,
        JComboBox<String> sensBox,
        JTextField largeurField) {
        
        try { 
            return new Porte(
                (String) murBox.getSelectedItem(),
                Double.parseDouble(positionField.getText()),
                (String) directionBox.getSelectedItem(),
                (String) typeBox.getSelectedItem(),
                (String) sensBox.getSelectedItem(),
                Double.parseDouble(largeurField.getText())
            );
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Valeur numérique invalide pour une porte", "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void setPositionX(double x) { this.positionX = x; }
    public void setPositionY(double y) { this.positionY = y; }
    public double getPositionX() { return positionX; }
    public double getPositionY() { return positionY; }
    
    @Override
    public String toString() {
        return nom + " (" + type + ") - " + String.format("%.2f", longueur) + "m x " + String.format("%.2f", largeur) + "m - " + position;
    }
}
