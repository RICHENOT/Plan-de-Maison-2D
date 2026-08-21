import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PieceForm extends JPanel {

    private JComboBox<String> typeField;
    private JTextField longueurField;
    private JTextField largeurField;
    private JComboBox<String> positionField;
    private String nomAuto;

    // Champs pour les portes par mur
    private JTextField portesGaucheField;
    private JTextField portesDroiteField;
    private JTextField portesHautField;
    private JTextField portesBasField;

    private JTextField positionXField;
    private JTextField positionYField;

    public PieceForm(int index) {
        nomAuto = "Chambre " + index;
        setBorder(BorderFactory.createTitledBorder(nomAuto));
        setLayout(new BorderLayout(10, 10));

        // Partie principale
        JPanel mainPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        typeField = new JComboBox<>(new String[]{"Chambre", "Salon", "Cuisine", "Toilettes"});
        longueurField = new JTextField();
        largeurField = new JTextField();
        positionField = new JComboBox<>(new String[]{"Gauche", "Droite", "Haut", "Bas"});

        mainPanel.add(new JLabel("Type :"));
        mainPanel.add(typeField);
        mainPanel.add(new JLabel("Longueur (m) :"));
        mainPanel.add(longueurField);
        mainPanel.add(new JLabel("Largeur (m) :"));
        mainPanel.add(largeurField);
 
        mainPanel.add(new JLabel("Position X:"));
        positionXField = new JTextField();
        mainPanel.add(positionXField);
        mainPanel.add(new JLabel("Position Y:"));
        positionYField = new JTextField();
        mainPanel.add(positionYField);

        // Portes
        JPanel portesPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        portesPanel.setBorder(BorderFactory.createTitledBorder("Portes (positions en m)"));

        portesGaucheField = new JTextField();
        portesDroiteField = new JTextField();
        portesHautField = new JTextField();
        portesBasField = new JTextField();

        portesPanel.add(new JLabel("Gauche :"));
        portesPanel.add(portesGaucheField);
        portesPanel.add(new JLabel("Droite :"));
        portesPanel.add(portesDroiteField);
        portesPanel.add(new JLabel("Haut :"));
        portesPanel.add(portesHautField);
        portesPanel.add(new JLabel("Bas :"));
        portesPanel.add(portesBasField);

        add(mainPanel, BorderLayout.NORTH);
        add(portesPanel, BorderLayout.CENTER);
    }

    public Piece toPiece() {
        try {
            // Récupération des valeurs du formulaire
            String type = (String) typeField.getSelectedItem();
            double longueur = Double.parseDouble(longueurField.getText());
            double largeur = Double.parseDouble(largeurField.getText());
            String position = (String) positionField.getSelectedItem();
            
            // Récupération des positions X et Y (avec valeurs par défaut à 0 si vide)
            double positionX = 0;
            double positionY = 0;
            if (!positionXField.getText().trim().isEmpty()) {
                positionX = Double.parseDouble(positionXField.getText());
            }
            if (!positionYField.getText().trim().isEmpty()) {
                positionY = Double.parseDouble(positionYField.getText());
            }

            // Validation des dimensions
            if (longueur <= 0 || largeur <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Les dimensions doivent être positives",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Validation des positions
            if (positionX < 0 || positionY < 0) {
                JOptionPane.showMessageDialog(this,
                    "Les positions doivent être positives",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Création de la liste des portes
            ArrayList<Porte> portes = new ArrayList<>();
            ajouterPortesDepuisChamp(portes, "Gauche", portesGaucheField);
            ajouterPortesDepuisChamp(portes, "Droite", portesDroiteField);
            ajouterPortesDepuisChamp(portes, "Haut", portesHautField);
            ajouterPortesDepuisChamp(portes, "Bas", portesBasField);

            // Création de la pièce
            Piece piece = new Piece(nomAuto, type, longueur, largeur, position, portes);
            piece.setPositionX(positionX);
            piece.setPositionY(positionY);
            
            return piece;

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer des valeurs numériques valides", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void ajouterPortesDepuisChamp(ArrayList<Porte> portes, String mur, JTextField champ) {
    String texte = champ.getText().trim();
    if (!texte.isEmpty()) {
        String[] positions = texte.split(",");
        for (String pos : positions) {
            try {
                double position = Double.parseDouble(pos.trim());
                // Remplacer l'ancienne création de Porte:
                portes.add(new Porte(
                    mur, 
                    position,
                    "Intérieur",  // Valeur par défaut
                    "Simple",    // Valeur par défaut 
                    "Gauche",    // Valeur par défaut
                    1          // Largeur par défaut (0.9m)
                ));
            } catch (NumberFormatException e) {
                System.err.println("Position de porte invalide sur " + mur + " : " + pos);
            }
        }
    }
}
}