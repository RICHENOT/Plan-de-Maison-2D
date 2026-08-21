import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class OngletPieces extends JPanel {
    private DefaultListModel<Piece> modelListePieces;
    private JList<Piece> listePieces;
    private JTextField nomField;
    private JComboBox<String> typeBox, positionBox, positionMaisonBox;  
    private JPanel panelPortes;
    private JTextField maisonPositionXField, maisonPositionYField;
    private JPanel panelFenetres;
    private ArrayList<JComboBox<String>> listeMurFenetres;
    private ArrayList<JTextField> listePositionFenetres;
    private ArrayList<JTextField> listeLargeurFenetres;
    private ArrayList<JComboBox<String>> listeMurPortes;
    private ArrayList<JTextField> listePositionPortes;
    private ArrayList<JComboBox<String>> directionBoxes;
    private ArrayList<JComboBox<String>> typeBoxes;
    private ArrayList<JComboBox<String>> sensBoxes;
    private ArrayList<JTextField> largeurFields;

    // Changer les types des champs texte
    private JTextField longueurField, largeurField;
    private JTextField terrainLongueurField, terrainLargeurField;
    private JTextField maisonLongueurField, maisonLargeurField;
    // Ajoutez ces champs à la classe OngletPieces
    private JTextField positionXField, positionYField;

    public OngletPieces() {
  
        listeMurPortes = new ArrayList<>();
        listePositionPortes = new ArrayList<>();
        directionBoxes = new ArrayList<>();
        typeBoxes = new ArrayList<>();
        sensBoxes = new ArrayList<>();
        largeurFields = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Liste des pièces
        modelListePieces = new DefaultListModel<>();
        listePieces = new JList<>(modelListePieces);    
        listePieces.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listePieces.setCellRenderer(new PieceListRenderer());
        
        JScrollPane scrollListe = new JScrollPane(listePieces);
        scrollListe.setBorder(new TitledBorder("Liste des pièces"));
        scrollListe.setPreferredSize(new Dimension(250, 400));

        // Création des onglets
        JTabbedPane onglets = new JTabbedPane();
        
        // Onglet Pièces
        JPanel ongletPieces = creerOngletPieces();
        
        // Onglet Terrain/Maison
        JPanel ongletTerrainMaison = creerOngletTerrainMaison();
        
        onglets.addTab("Pièces", ongletPieces);
        onglets.addTab("Terrain/Maison", ongletTerrainMaison);

        // Organisation principale
        JPanel panelGauche = new JPanel(new BorderLayout());
        panelGauche.add(scrollListe, BorderLayout.CENTER);

        add(panelGauche, BorderLayout.WEST);
        add(onglets, BorderLayout.CENTER);
    }


    private JPanel creerOngletPieces() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Formulaire principal avec BoxLayout pour un défilement vertical
        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setBorder(new TitledBorder("Détails de la pièce"));

        // Panel pour les informations de base de la pièce
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Création d'un panel horizontal pour fenêtres et portes
        JPanel fenetresPortesPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Configuration des champs
        nomField = new JTextField(15);
        typeBox = new JComboBox<>(new String[]{"Chambre", "Salon", "Cuisine", "Salle de bain", "Couloir"});
        longueurField = new JTextField(5);
        largeurField = new JTextField(5);
        positionBox = new JComboBox<>(new String[]{"Gauche", "Droite", "Haut", "Bas"});
        positionXField = new JTextField(5);
        positionYField = new JTextField(5);

        // Organisation des champs de la pièce
        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(typeBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Longueur (m):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(longueurField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Largeur (m):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(largeurField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        infoPanel.add(new JLabel("Position dans la maison:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(positionBox, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        infoPanel.add(new JLabel("Position X (m):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(positionXField, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        infoPanel.add(new JLabel("Position Y (m):"), gbc);
        gbc.gridx = 1;
        infoPanel.add(positionYField, gbc);

        // Ajout du panel d'informations au formulaire principal
        formulaire.add(infoPanel);

        // Panel pour les portes
        panelPortes = new JPanel();
        panelPortes.setLayout(new BoxLayout(panelPortes, BoxLayout.Y_AXIS));
        panelPortes.setBorder(new TitledBorder("Portes de la pièce"));

        listeMurPortes = new ArrayList<>();
        listePositionPortes = new ArrayList<>();

        JButton btnAjouterPorte = new JButton("Ajouter une porte");
        btnAjouterPorte.addActionListener(e -> ajouterChampPorte(null, null, "Intérieur", "Simple", "Gauche", 0.9));
        
        // Container pour les portes avec bouton en bas
        JPanel portesContainer = new JPanel(new BorderLayout());
        portesContainer.add(new JScrollPane(panelPortes), BorderLayout.CENTER);
        
        JPanel buttonPanelPortes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanelPortes.add(btnAjouterPorte);
        portesContainer.add(buttonPanelPortes, BorderLayout.SOUTH);

        // Ajout du panel des portes au formulaire principal
        formulaire.add(portesContainer);

        // Boutons d'actions
        JPanel panelBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAjouter = new JButton("Ajouter", new ImageIcon(getClass().getResource("/icons/ajout.png")));
        JButton btnModifier = new JButton("Modifier", new ImageIcon(getClass().getResource("/icons/modifier.png")));
        JButton btnSupprimer = new JButton("Supprimer", new ImageIcon(getClass().getResource("/icons/supprimer.png")));
        JButton btnNouvelle = new JButton("Nouvelle pièce");                                 
        JButton btnPlacerAuto = new JButton("Placer automatiquement");

        btnPlacerAuto.addActionListener(e -> placerPiecesAutomatiquement());
        btnAjouter.addActionListener(this::ajouterPiece);
        btnModifier.addActionListener(this::modifierPiece);
        btnSupprimer.addActionListener(e -> supprimerPiece());
        btnNouvelle.addActionListener(e -> nouvellePiece());

        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);
        panelBoutons.add(btnNouvelle);
        panelBoutons.add(btnPlacerAuto);

        // Organisation de l'onglet
        JPanel panelDroit = new JPanel(new BorderLayout(10, 10));
        panelDroit.add(formulaire, BorderLayout.CENTER);
        panelDroit.add(panelBoutons, BorderLayout.SOUTH);

        panel.add(panelDroit, BorderLayout.CENTER);

        // Sélection dans la liste
        listePieces.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                chargerPieceSelectionnee();
            }
        });



        // Panel pour les fenêtres
        panelFenetres = new JPanel();
        panelFenetres.setLayout(new BoxLayout(panelFenetres, BoxLayout.Y_AXIS));
        panelFenetres.setBorder(new TitledBorder("Fenêtres de la pièce"));

        listeMurFenetres = new ArrayList<>();
        listePositionFenetres = new ArrayList<>();
        listeLargeurFenetres = new ArrayList<>();

        JButton btnAjouterFenetre = new JButton("Ajouter une fenêtre");
        btnAjouterFenetre.addActionListener(e -> ajouterChampFenetre(null, null, null));
    
        // Container pour les fenêtres avec bouton en bas
        JPanel fenetresContainer = new JPanel(new BorderLayout());
        fenetresContainer.add(new JScrollPane(panelFenetres), BorderLayout.CENTER);

        JPanel buttonPanelFenetres = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanelFenetres.add(btnAjouterFenetre);
        fenetresContainer.add(buttonPanelFenetres, BorderLayout.SOUTH);

        // Ajout des deux panneaux au panel horizontal
        fenetresPortesPanel.add(portesContainer);
        fenetresPortesPanel.add(fenetresContainer);

        // Ajout du panel horizontal au formulaire principal
        formulaire.add(fenetresPortesPanel);

        
        return panel;
    }

    private void ajouterChampFenetre(String mur, Double position, Double largeur) {
        JPanel fenetrePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        JComboBox<String> murBox = new JComboBox<>(new String[]{"Gauche", "Droite", "Haut", "Bas"});
        JTextField positionField = new JTextField(5);
        JTextField largeurField = new JTextField(5);
        
        if (mur != null) murBox.setSelectedItem(mur);
        if (position != null) positionField.setText(String.valueOf(position));
        if (largeur != null) largeurField.setText(String.valueOf(largeur));
        
        fenetrePanel.add(new JLabel("Mur:"));
        fenetrePanel.add(murBox);
        fenetrePanel.add(new JLabel("Position (m):"));
        fenetrePanel.add(positionField);
        fenetrePanel.add(new JLabel("Largeur (m):"));
        fenetrePanel.add(largeurField);
        
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> {
            panelFenetres.remove(fenetrePanel);
            listeMurFenetres.remove(murBox);
            listePositionFenetres.remove(positionField);
            listeLargeurFenetres.remove(largeurField);
            revalidate();
            repaint();
        });
        fenetrePanel.add(btnSupprimer);
        
        listeMurFenetres.add(murBox);
        listePositionFenetres.add(positionField);
        listeLargeurFenetres.add(largeurField);
        
        panelFenetres.add(fenetrePanel);
        revalidate();
        repaint();
    }

    private void chargerFenetresPiece(Piece piece) {
        panelFenetres.removeAll();
        listeMurFenetres.clear();
        listePositionFenetres.clear();
        listeLargeurFenetres.clear();
        
        if (piece.getFenetres() != null && !piece.getFenetres().isEmpty()) {
            for (Fenetre fenetre : piece.getFenetres()) {
                ajouterChampFenetre(
                    fenetre.getMur(),
                    fenetre.getPosition(),
                    fenetre.getLargeur()
                );
            }
        }
    }
    private void placerPiecesAutomatiquement() {
        try {
            double maisonLong = getMaisonLongueur();
            double maisonLarg = getMaisonLargeur();
            ArrayList<Piece> pieces = getListePieces();
            
            if (RoomPlacer.placeRoomsAutomatically(maisonLong, maisonLarg, pieces)) {
                JOptionPane.showMessageDialog(this, 
                    "Pièces placées automatiquement avec succès!", 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
                // Rafraîchir l'affichage
                setListePieces(pieces);
                chargerPieceSelectionnee();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Impossible de placer toutes les pièces automatiquement", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors du placement automatique: " + ex.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean verifierEtDemanderAjustement(Component parent) {
        double maisonLongueur = getMaisonLongueur();
        double maisonLargeur = getMaisonLargeur();
        boolean ajustementEffectue = false;
        
        // Vérifier chaque pièce
        for (Piece piece : getListePieces()) {
            if (piece.besoinAjustementMaison(maisonLongueur, maisonLargeur)) {
                int choix = JOptionPane.showConfirmDialog(parent,
                    "La pièce '" + piece.getNom() + "' dépasse les dimensions de la maison.\n" +
                    "Position actuelle: X=" + piece.getPositionX() + "m, Y=" + piece.getPositionY() + "m\n" +
                    "Nouvelle position proposée: X=" + 
                    Math.min(piece.getPositionX(), maisonLongueur - piece.getLongueurEffective()) + "m, Y=" +
                    Math.min(piece.getPositionY(), maisonLargeur - piece.getLargeurEffective()) + "m\n\n" +
                    "Voulez-vous appliquer cet ajustement?",
                    "Confirmation d'ajustement",
                    JOptionPane.YES_NO_OPTION);
                
                if (choix == JOptionPane.YES_OPTION) {
                    piece.ajusterPosition(maisonLongueur, maisonLargeur);
                    ajustementEffectue = true;
                }
            }
        }
        
        // Vérifier maison/terrain
        if (maisonLongueur > getTerrainLongueur() || maisonLargeur > getTerrainLargeur()) {
            int choix = JOptionPane.showConfirmDialog(parent,
                "La maison dépasse les dimensions du terrain.\n" +
                "Dimensions actuelles: " + maisonLongueur + "m x " + maisonLargeur + "m\n" +
                "Nouvelles dimensions proposées: " + 
                Math.min(maisonLongueur, getTerrainLongueur()) + "m x " + 
                Math.min(maisonLargeur, getTerrainLargeur()) + "m\n\n" +
                "Voulez-vous appliquer cet ajustement?",
                "Confirmation d'ajustement",
                JOptionPane.YES_NO_OPTION);
            
            if (choix == JOptionPane.YES_OPTION) {
                setMaisonLongueur(Math.min(maisonLongueur, getTerrainLongueur()));
                setMaisonLargeur(Math.min(maisonLargeur, getTerrainLargeur()));
                ajustementEffectue = true;
                
                // Revérifier les pièces après ajustement maison
                verifierEtDemanderAjustement(parent);
            }
        }
        
        return ajustementEffectue;
    }

    public boolean verifierEtAjusterDebordements(JFrame parent) {
        boolean ajustementEffectue = false;
        
        // 1. Vérification de débordement des pièces dans la maison
        double[] tailleMaisonRequise = calculerTailleMaisonRequise();
        if (tailleMaisonRequise[0] > getMaisonLongueur() || tailleMaisonRequise[1] > getMaisonLargeur()) {
            int choix = JOptionPane.showConfirmDialog(parent,
                "La maison est trop petite pour contenir toutes les pièces.\n" +
                "Dimensions minimales requises: " + tailleMaisonRequise[0] + "m x " + tailleMaisonRequise[1] + "m\n\n" +
                "Voulez-vous ajuster automatiquement la taille de la maison?",
                "Ajustement nécessaire",
                JOptionPane.YES_NO_OPTION);
                
            if (choix == JOptionPane.YES_OPTION) {
                setMaisonLongueur(tailleMaisonRequise[0]);
                setMaisonLargeur(tailleMaisonRequise[1]);
                ajustementEffectue = true;
            } else {
                // Si l'utilisateur refuse, on ajuste les pièces à la place
                JOptionPane.showMessageDialog(parent,
                    "Les pièces seront réduites pour s'adapter à la maison",
                    "Ajustement des pièces",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        return ajustementEffectue;
    }

    private JPanel creerOngletTerrainMaison() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Champs pour le terrain et la maison
        terrainLongueurField = new JTextField(5);
        terrainLargeurField = new JTextField(5);
        maisonLongueurField = new JTextField(5);
        maisonLargeurField = new JTextField(5);
        maisonPositionXField = new JTextField(5);
        maisonPositionYField = new JTextField(5);

        // Configuration des tailles
        terrainLongueurField.setPreferredSize(new Dimension(80, 25));
        terrainLargeurField.setPreferredSize(new Dimension(80, 25));
        maisonLongueurField.setPreferredSize(new Dimension(80, 25));
        maisonLargeurField.setPreferredSize(new Dimension(80, 25));
        
        // Ajout des composants
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Longueur terrain (m):"), gbc);
        gbc.gridx = 1;
        panel.add(terrainLongueurField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Largeur terrain (m):"), gbc);
        gbc.gridx = 1;
        panel.add(terrainLargeurField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Longueur maison (m):"), gbc);
        gbc.gridx = 1;
        panel.add(maisonLongueurField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Largeur maison (m):"), gbc);
        gbc.gridx = 1;
        panel.add(maisonLargeurField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Position X maison (m):"), gbc);
        gbc.gridx = 1;
        panel.add(maisonPositionXField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Position Y maison (m):"), gbc);
        gbc.gridx = 1;
        panel.add(maisonPositionYField, gbc);

        return panel;
    }


    private void nouvellePiece() {
        // Vérifier si une pièce est sélectionnée
        if (listePieces.getSelectedIndex() >= 0) {
            // Demander confirmation
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "Voulez-vous vraiment créer une nouvelle pièce? Les modifications non enregistrées seront perdues.",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                nettoyerFormulaire();
                listePieces.clearSelection();
            }
        } else {
            // Si aucune pièce n'est sélectionnée, simplement nettoyer
            nettoyerFormulaire();
        }
    }
    


    // Ajout des méthodes getter/setter pour les champs terrain/maison
    public double getTerrainLongueur() throws NumberFormatException {
        try {
            double value = Double.parseDouble(terrainLongueurField.getText());
            if (value <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "La longueur du terrain doit être supérieure à 0", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            terrainLongueurField.selectAll();
            terrainLongueurField.requestFocus();
            throw new NumberFormatException("Longueur terrain invalide");
        }
    }

    public double getTerrainLargeur() {
        return Double.parseDouble(terrainLargeurField.getText());
    }

    public double getMaisonLongueur() {
        return Double.parseDouble(maisonLongueurField.getText());
    }

        public double getMaisonLargeur() {
        return Double.parseDouble(maisonLargeurField.getText());
    }



    //getters/setters
    public double getMaisonPositionX() {
        try {
            return Double.parseDouble(maisonPositionXField.getText());
        } catch (NumberFormatException e) {
            return 0; // Valeur par défaut si non spécifiée
        }
    }

    public double getMaisonPositionY() {
        try {
            return Double.parseDouble(maisonPositionYField.getText());
        } catch (NumberFormatException e) {
            return 0; // Valeur par défaut si non spécifiée
        }
    }

    public void setMaisonPositionX(double x) {
        maisonPositionXField.setText(String.valueOf(x));
    }

    public void setMaisonPositionY(double y) {
        maisonPositionYField.setText(String.valueOf(y));
    }

    public String getPositionMaison() {
        return (String) positionMaisonBox.getSelectedItem();
    }
    
    public void setTerrainLongueur(double longueur) {
        terrainLongueurField.setText(String.valueOf(longueur));
    }
    
    public void setTerrainLargeur(double largeur) {
        terrainLargeurField.setText(String.valueOf(largeur));
    }

    public void setMaisonLongueur(double longueur) {
        if (longueur > getTerrainLongueur()) {
            int choix = JOptionPane.showConfirmDialog(this,
                "La longueur dépasse celle du terrain.\n" +
                "Voulez-vous la réduire à " + getTerrainLongueur() + "m?",
                "Confirmation d'ajustement",
                JOptionPane.YES_NO_OPTION);
            
            if (choix == JOptionPane.YES_OPTION) {
                maisonLongueurField.setText(String.valueOf(getTerrainLongueur()));
            }
        } else {
            maisonLongueurField.setText(String.valueOf(longueur));
        }
    }
    
    public void setMaisonLargeur(double largeur) {
        maisonLargeurField.setText(String.valueOf(largeur));
    }
    
    public void setPositionMaison(String position) {
        if (positionMaisonBox != null) {
            positionMaisonBox.setSelectedItem(position);
        }
    }

    // Méthode pour afficher une pièce sélectionnée
    private void chargerPieceSelectionnee() {
        if (listePieces.getSelectedIndex() == -1) {
            nettoyerFormulaire();
            return;
        }
        
        Piece piece = listePieces.getSelectedValue();
        if (piece != null) {
            // Charger les informations de base
            nomField.setText(piece.getNom());
            typeBox.setSelectedItem(piece.getType());
            longueurField.setText(String.valueOf(piece.getLongueur()));
            largeurField.setText(String.valueOf(piece.getLargeur()));
            positionBox.setSelectedItem(piece.getPosition());
            positionXField.setText(String.valueOf(piece.getPositionX()));
            positionYField.setText(String.valueOf(piece.getPositionY()));
                
            // Charger les portes
            panelPortes.removeAll();
            listeMurPortes.clear();
            listePositionPortes.clear();
            directionBoxes.clear();
            typeBoxes.clear();
            sensBoxes.clear();
            largeurFields.clear();

            // Charger les fenêtres
            chargerFenetresPiece(piece);
            
            // Charger les portes seulement si elles existent
            if (piece.getPortes() != null && !piece.getPortes().isEmpty()) { 
                for (Porte porte : piece.getPortes()) {
                    ajouterChampPorte(
                        porte.getMur(),
                        porte.getPositionMetres(),
                        porte.getDirection(),
                        porte.getType(),
                        porte.getSensOuverture(),
                        porte.getLargeur()
                    );
                }
            }
            
            // Charger les fenêtres seulement si elles existent
            if (piece.getFenetres() != null && !piece.getFenetres().isEmpty()) {
                for (Fenetre fenetre : piece.getFenetres()) {
                    ajouterChampFenetre(
                        fenetre.getMur(),
                        fenetre.getPosition(),
                        fenetre.getLargeur()
                    );
                }
            }
            
            revalidate();
            repaint();
        }
    }
    



    private void ajouterChampPorte(String mur, Double position, String direction, String type, String sensOuverture, Double largeur) {
        JPanel portePanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        // Champs existants
        JComboBox<String> murBox = new JComboBox<>(new String[]{"Gauche", "Droite", "Haut", "Bas"});
        JTextField positionField = new JTextField(5);
        
        JButton btnAjouterPorte = new JButton("Ajouter une porte");
        btnAjouterPorte.addActionListener(e -> ajouterChampPorte(null, null, "Intérieur", "Simple", "Gauche", 0.9));


        // Nouveaux champs
        JComboBox<String> directionBox = new JComboBox<>(new String[]{"Intérieur", "Extérieur"});
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Simple", "Double"});
        JComboBox<String> sensBox = new JComboBox<>(new String[]{"Gauche", "Droite"});
        JTextField largeurField = new JTextField("0.9", 5);
        
        // Initialisation des valeurs
        if (mur != null) murBox.setSelectedItem(mur);
        if (position != null) positionField.setText(String.valueOf(position));
        if (direction != null) directionBox.setSelectedItem(direction);
        if (type != null) typeBox.setSelectedItem(type);
        if (sensOuverture != null) sensBox.setSelectedItem(sensOuverture); // Correction ici

        if (largeur != null) largeurField.setText(String.valueOf(largeur));
        
        // Organisation des champs
        portePanel.add(new JLabel("Mur:"));
        portePanel.add(murBox);
        portePanel.add(new JLabel("Position (m):"));
        portePanel.add(positionField);
        portePanel.add(new JLabel("Direction:"));
        portePanel.add(directionBox);
        portePanel.add(new JLabel("Type:"));
        portePanel.add(typeBox);
        portePanel.add(new JLabel("Sens:"));
        portePanel.add(sensBox);
        portePanel.add(new JLabel("Largeur (m):"));
        portePanel.add(largeurField);
        
        // Bouton suppression
        JButton btnSupprimer = new JButton("Supprimer");
        btnSupprimer.addActionListener(e -> {
            panelPortes.remove(portePanel);
            listeMurPortes.remove(murBox);
            listePositionPortes.remove(positionField);
            directionBoxes.remove(directionBox);
            typeBoxes.remove(typeBox);
            sensBoxes.remove(sensBox);
            largeurFields.remove(largeurField);
            revalidate();
            repaint();
        });
        portePanel.add(btnSupprimer);
        
        // Ajout aux listes
        listeMurPortes.add(murBox);
        listePositionPortes.add(positionField);
        directionBoxes.add(directionBox);
        typeBoxes.add(typeBox);
        sensBoxes.add(sensBox);
        largeurFields.add(largeurField);
        
        panelPortes.add(portePanel);
        revalidate();
        repaint();
    }
        
    public void setListePieces(ArrayList<Piece> pieces) {
        modelListePieces.clear();
        for (Piece piece : pieces) {
            modelListePieces.addElement(piece);
        }
    }

    private void ajouterPiece(ActionEvent e) {
        if (!validerDonneesPiece()) return;
        
        Piece p = creerPieceDepuisFormulaire();
        if (p != null) {
            if (p.besoinAjustementMaison(getMaisonLongueur(), getMaisonLargeur())) {
                int choix = JOptionPane.showConfirmDialog(this,
                    "Cette pièce dépasse les dimensions de la maison.\n" +
                    "Voulez-vous ajuster sa position automatiquement?",
                    "Confirmation d'ajustement",
                    JOptionPane.YES_NO_OPTION);
                
                if (choix == JOptionPane.YES_OPTION) {
                    p.ajusterPosition(getMaisonLongueur(), getMaisonLargeur());
                } else {
                    return; // Annule l'ajout si l'utilisateur refuse l'ajustement
                }
            }
            
            modelListePieces.addElement(p);
            listePieces.setSelectedIndex(modelListePieces.size() - 1);
        }
    }

    private void modifierPiece(ActionEvent e) {
        int index = listePieces.getSelectedIndex();
        if (index >= 0) {
            try {
                Piece pieceSelectionnee = modelListePieces.getElementAt(index);
                Piece piece = modelListePieces.getElementAt(index);
                
                // Mettre à jour les propriétés
                pieceSelectionnee.setNom(nomField.getText());
                pieceSelectionnee.setType((String) typeBox.getSelectedItem());
                pieceSelectionnee.setLongueur(Double.parseDouble(longueurField.getText()));
                pieceSelectionnee.setLargeur(Double.parseDouble(largeurField.getText()));
                pieceSelectionnee.setPosition((String) positionBox.getSelectedItem());
                pieceSelectionnee.setPositionX(Double.parseDouble(positionXField.getText()));
                pieceSelectionnee.setPositionY(Double.parseDouble(positionYField.getText()));
                
                // Mettre à jour les portes
                ArrayList<Porte> portes = new ArrayList<>();
                for (int i = 0; i < listeMurPortes.size(); i++) {
                    portes.add(new Porte(
                        (String) listeMurPortes.get(i).getSelectedItem(),
                        Double.parseDouble(listePositionPortes.get(i).getText()),
                        (String) directionBoxes.get(i).getSelectedItem(),
                        (String) typeBoxes.get(i).getSelectedItem(),
                        (String) sensBoxes.get(i).getSelectedItem(),
                        Double.parseDouble(largeurFields.get(i).getText())
                    ));
                }
                pieceSelectionnee.setPortes(portes);


                // Mettre à jour les fenêtres
                ArrayList<Fenetre> fenetres = new ArrayList<>();
                for (int i = 0; i < listeMurFenetres.size(); i++) {
                    fenetres.add(new Fenetre(
                        (String) listeMurFenetres.get(i).getSelectedItem(),
                        Double.parseDouble(listePositionFenetres.get(i).getText()),
                        Double.parseDouble(listeLargeurFenetres.get(i).getText())
                    ));
                }
                pieceSelectionnee.setFenetres(fenetres);
                
                // Vérifier et ajuster les portes et fenêtres
                boolean ajustementPortes = verifierEtAjusterPortes(pieceSelectionnee);
                boolean ajustementFenetres = verifierEtAjusterFenetres(pieceSelectionnee);
                
                if (ajustementPortes || ajustementFenetres) {
                    JOptionPane.showMessageDialog(this,
                        "Certaines portes ou fenêtres ont été ajustées pour s'adapter aux dimensions de la pièce",
                        "Ajustement automatique", JOptionPane.INFORMATION_MESSAGE);
                }
                
                // Vérification du débordement
                if (piece.besoinAjustementMaison(getMaisonLongueur(), getMaisonLargeur())) {
                    int choix = JOptionPane.showConfirmDialog(this,
                        "Les modifications font dépasser la pièce des dimensions de la maison.\n" +
                        "Voulez-vous ajuster sa position automatiquement?",
                        "Confirmation d'ajustement",
                        JOptionPane.YES_NO_OPTION);
                    
                    if (choix == JOptionPane.YES_OPTION) {
                        piece.ajusterPosition(getMaisonLongueur(), getMaisonLargeur());
                    }
                }
                
                modelListePieces.set(index, pieceSelectionnee);
                JOptionPane.showMessageDialog(this, "Pièce modifiée avec succès!");
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erreur: Veuillez entrer des valeurs numériques valides", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner une pièce à modifier", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        }
    }



    private boolean verifierEtAjusterFenetres(Piece piece) {
        boolean ajustementEffectue = false;
        double longueurPiece = piece.getLongueur();
        double largeurPiece = piece.getLargeur();
        
        for (Fenetre fenetre : piece.getFenetres()) {
            // Calculer la dimension maximale selon le mur
            double dimensionMur = fenetre.getMur().equalsIgnoreCase("haut") || 
                                fenetre.getMur().equalsIgnoreCase("bas") 
                                ? longueurPiece : largeurPiece;
            
            // Vérifier si un ajustement est nécessaire
            if (fenetre.getLargeur() > dimensionMur) {
                fenetre.setLargeur(dimensionMur);
                ajustementEffectue = true;
            }
            
            double positionMax = dimensionMur - fenetre.getLargeur();
            if (fenetre.getPosition() > positionMax) {
                fenetre.setPosition(positionMax);
                ajustementEffectue = true;
            }
            if (fenetre.getPosition() < 0) {
                fenetre.setPosition(0);
                ajustementEffectue = true;
            }
        }
        
        return ajustementEffectue;
    }





    public boolean verifierDebordements(JFrame parent) {
        // 1. Vérifier le débordement maison/terrain
        double terrainLongueur = getTerrainLongueur();
        double terrainLargeur = getTerrainLargeur();
        double maisonLongueur = getMaisonLongueur();
        double maisonLargeur = getMaisonLargeur();
        
        if (maisonLongueur > terrainLongueur || maisonLargeur > terrainLargeur) {
            // Calculer la réduction nécessaire
            double newLongueur = Math.min(maisonLongueur, terrainLongueur);
            double newLargeur = Math.min(maisonLargeur, terrainLargeur);
            
            // Afficher un message d'information (pas de choix)
            JOptionPane.showMessageDialog(parent,
                "La maison a été automatiquement ajustée pour tenir dans le terrain.\n" +
                "Nouvelles dimensions: " + String.format("%.2f", newLongueur) + "m x " + 
                String.format("%.2f", newLargeur) + "m",
                "Ajustement automatique",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Appliquer la réduction
            setMaisonLongueur(newLongueur);
            setMaisonLargeur(newLargeur);
            
            return true;
        }
        
        // 2. Vérifier le débordement pièces/maison
        ArrayList<Piece> piecesDebordantes = getPiecesDebordantes();
        if (!piecesDebordantes.isEmpty()) {
            ajusterPiecesDebordantes(piecesDebordantes);
            return true;
        }
        
        return false;
    }
    
    // Nouvelle méthode pour identifier les pièces débordantes
    private ArrayList<Piece> getPiecesDebordantes() {
        ArrayList<Piece> result = new ArrayList<>();
        double maisonLong = getMaisonLongueur();
        double maisonLarg = getMaisonLargeur();
        
        for (Piece p : getListePieces()) {
            if (p.getPosition().equalsIgnoreCase("gauche") || p.getPosition().equalsIgnoreCase("droite")) {
                if (p.getLargeur() > maisonLarg || p.getLongueur() > maisonLong) {
                    result.add(p);
                }
            } else {
                if (p.getLongueur() > maisonLong || p.getLargeur() > maisonLarg) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    // méthode pour ajuster les pièces
    private void ajusterPiecesDebordantes(ArrayList<Piece> pieces) {
        double maisonLong = getMaisonLongueur();
        double maisonLarg = getMaisonLargeur();
        
        for (Piece p : pieces) {
            boolean estCote = p.getPosition().equalsIgnoreCase("gauche") || 
                            p.getPosition().equalsIgnoreCase("droite");
            
            double longueurEffective = p.getLongueurEffective();
            double largeurEffective = p.getLargeurEffective();
            
            // Calculer le ratio de réduction nécessaire
            double ratioLongueur = 1.0;
            double ratioLargeur = 1.0;
            
            if (longueurEffective > maisonLong) {
                ratioLongueur = maisonLong / longueurEffective;
            }
            if (largeurEffective > maisonLarg) {
                ratioLargeur = maisonLarg / largeurEffective;
            }
            
            // Prendre le ratio le plus petit pour conserver les proportions
            double ratio = Math.min(ratioLongueur, ratioLargeur);
            
            // Appliquer la réduction
            if (estCote) {
                p.setLargeur(p.getLargeur() * ratio);
                p.setLongueur(p.getLongueur() * ratio);
            } else {
                p.setLongueur(p.getLongueur() * ratio);
                p.setLargeur(p.getLargeur() * ratio);
            }
            
            // Ajuster la position si nécessaire
            if (p.getPositionX() + p.getLongueurEffective() > maisonLong) {
                p.setPositionX(maisonLong - p.getLongueurEffective());
            }
            if (p.getPositionY() + p.getLargeurEffective() > maisonLarg) {
                p.setPositionY(maisonLarg - p.getLargeurEffective());
            }
        }
        
        // Mettre à jour l'affichage
        setListePieces(getListePieces());
    }



    // Méthode pour calculer la taille minimale du terrain
    public double[] calculerTailleTerrainRequise() {
        double maisonLong = getMaisonLongueur();
        double maisonLarg = getMaisonLargeur();
        String position = getPositionMaison();
        
        // Calcul selon la position de la maison
        switch(position.toLowerCase()) {
            case "centre":
                return new double[]{maisonLong * 2, maisonLarg * 2};
            case "haut":
            case "bas":
                return new double[]{maisonLong, (int)(maisonLarg * 1.5)};
            case "gauche":
            case "droite":
                return new double[]{(int)(maisonLong * 1.5), maisonLarg};
            default: // Cas des coins
                return new double[]{maisonLong, maisonLarg};
        }
    }

    public boolean verifierDebordementsPieces(JFrame parent) {
        ArrayList<Piece> piecesDebordantes = getPiecesDebordantes();
        if (!piecesDebordantes.isEmpty()) {
            // Afficher un message de confirmation avant l'ajustement
            int choix = JOptionPane.showConfirmDialog(parent,
                piecesDebordantes.size() + " pièce(s) débordent de la maison.\n" +
                "Voulez-vous les ajuster automatiquement pour qu'elles tiennent dans la maison?",
                "Ajustement nécessaire",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (choix == JOptionPane.YES_OPTION) {
                ajusterPiecesDebordantes(piecesDebordantes);
                return true;
            } else {
                return false; // L'utilisateur a refusé l'ajustement
            }
        }
        return true; // Aucun débordement
    }

    public double[] calculerTailleMaisonRequise() {
        ArrayList<Piece> pieces = getListePieces();
        double maxLongueur = getMaisonLongueur();
        double maxLargeur = getMaisonLargeur();
        
        for (Piece p : pieces) {
            if (p.getPosition().equalsIgnoreCase("gauche") || p.getPosition().equalsIgnoreCase("droite")) {
                maxLargeur = Math.max(maxLargeur, p.getLargeur());
            } else {
                maxLongueur = Math.max(maxLongueur, p.getLongueur());
            }
        }
        return new double[]{maxLongueur, maxLargeur};
    }

    private Piece creerPieceDepuisFormulaire() {
        try {
            String nom = nomField.getText();
            String type = (String) typeBox.getSelectedItem();
            double longueur = Double.parseDouble(longueurField.getText());
            double largeur = Double.parseDouble(largeurField.getText());
            String position = (String) positionBox.getSelectedItem();
            
            // Gestion des positions X/Y avec valeurs par défaut à 0 si vide
            double positionX = 0;
            if (!positionXField.getText().trim().isEmpty()) {
                positionX = Double.parseDouble(positionXField.getText());
            }
            double positionY = 0;
            if (!positionYField.getText().trim().isEmpty()) {
                positionY = Double.parseDouble(positionYField.getText());
            }

            // Création de la liste des portes
            ArrayList<Porte> portes = new ArrayList<>();
            for (int i = 0; i < listeMurPortes.size(); i++) {
                portes.add(new Porte(
                (String) listeMurPortes.get(i).getSelectedItem(),
                Double.parseDouble(listePositionPortes.get(i).getText()),
                (String) directionBoxes.get(i).getSelectedItem(),
                (String) typeBoxes.get(i).getSelectedItem(),
                (String) sensBoxes.get(i).getSelectedItem(),
                Double.parseDouble(largeurFields.get(i).getText())
            ));
            }






            // Vérification taille positive
            if (longueur <= 0 || largeur <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Les dimensions doivent être positives",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Vérification positions positives
            if (positionX < 0 || positionY < 0) {
                JOptionPane.showMessageDialog(this,
                    "Les positions doivent être positives",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Création de la pièce
            Piece p = new Piece(nom, type, longueur, largeur, position, portes);
            p.setPositionX(positionX);
            p.setPositionY(positionY);
            



            // Création de la liste des fenêtres
        ArrayList<Fenetre> fenetres = new ArrayList<>();
        for (int i = 0; i < listeMurFenetres.size(); i++) {
            String mur = (String) listeMurFenetres.get(i).getSelectedItem();
            double pos = Double.parseDouble(listePositionFenetres.get(i).getText());
            double larg = Double.parseDouble(listeLargeurFenetres.get(i).getText());
            
            fenetres.add(new Fenetre(mur, pos, larg));
        }
        p.setFenetres(fenetres);
        
        return p;
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, 
            "Veuillez entrer des valeurs numériques valides", 
            "Erreur", JOptionPane.ERROR_MESSAGE);
        return null;
    }


        
    }

            
    private void nettoyerFormulaire() {
        nomField.setText("");
        typeBox.setSelectedIndex(0);
        longueurField.setText("");
        largeurField.setText("");
        positionBox.setSelectedIndex(0);
        positionXField.setText("");
        positionYField.setText("");

        // Vider complètement les portes
        panelPortes.removeAll();
        listeMurPortes.clear();
        listePositionPortes.clear();
        directionBoxes.clear();
        typeBoxes.clear();
        sensBoxes.clear();
        largeurFields.clear();

        // Vider les fenêtres
        panelFenetres.removeAll();
        listeMurFenetres.clear();
        listePositionFenetres.clear();
        listeLargeurFenetres.clear();
        
        revalidate();
        repaint();
        
        // Donner le focus au champ nom
        nomField.requestFocusInWindow();
    }

    public ArrayList<Piece> getListePieces() {
        ArrayList<Piece> liste = new ArrayList<>();
        for (int i = 0; i < modelListePieces.size(); i++) {
            liste.add(modelListePieces.get(i));
        }
        return liste;
    }


    
    private void supprimerPiece() {
        int index = listePieces.getSelectedIndex();
        if (index >= 0) {
            modelListePieces.remove(index);
            nettoyerFormulaire();
            JOptionPane.showMessageDialog(this, 
                "Pièce supprimée avec succès!", 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Veuillez sélectionner une pièce à supprimer", 
                "Aucune sélection", JOptionPane.WARNING_MESSAGE);
        }
    }



    private boolean verifierEtAjusterPortes(Piece piece) {
        boolean ajustementEffectue = false;
        double longueurPiece = piece.getLongueur();
        double largeurPiece = piece.getLargeur();
        
        for (Porte porte : piece.getPortes()) {
            // Vérifier et ajuster chaque porte
            boolean ajustement = porte.validerEtAjusterPorte(longueurPiece, largeurPiece, this);
            if (ajustement) {
                ajustementEffectue = true;
            }
        }
        
        return ajustementEffectue;
    }

    


    private boolean validerDonneesPiece() {
        // Validation du nom
        if (nomField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Le nom de la pièce ne peut pas être vide", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            nomField.requestFocus();
            return false;
        }
        
        try {
            // Validation des dimensions
            double longueur = Double.parseDouble(longueurField.getText());
            double largeur = Double.parseDouble(largeurField.getText());
            if (longueur <= 0 || largeur <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Les dimensions doivent être positives", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Validation des positions
            double posX = Double.parseDouble(positionXField.getText());
            double posY = Double.parseDouble(positionYField.getText());
            if (posX < 0 || posY < 0) {
                JOptionPane.showMessageDialog(this, 
                    "Les positions doivent être positives", 
                    "Erreur", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validation des portes
            for (int i = 0; i < listeMurPortes.size(); i++) {
                try {
                    String mur = (String) listeMurPortes.get(i).getSelectedItem();
                    double position = Double.parseDouble(listePositionPortes.get(i).getText());
                    double largeurPorte = Double.parseDouble(largeurFields.get(i).getText());
                    
                    // Vérifier que les valeurs sont positives
                    if (position < 0 || largeurPorte <= 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Les valeurs des portes doivent être positives", 
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        listePositionPortes.get(i).requestFocus();
                        return false;
                    }
                    
                    // Calculer la dimension maximale selon le mur
                    double dimensionMur = mur.equalsIgnoreCase("haut") || mur.equalsIgnoreCase("bas") 
                        ? longueur : largeur;
                    
                    // Vérifier si un ajustement est nécessaire
                    boolean ajustementNecessaire = false;
                    String messageAjustement = "";
                    
                    if (largeurPorte > dimensionMur) {
                        ajustementNecessaire = true;
                        messageAjustement = "La largeur de la porte (" + largeurPorte + "m) dépasse la dimension du mur (" + dimensionMur + "m).";
                    } else if (position + largeurPorte > dimensionMur) {
                        ajustementNecessaire = true;
                        messageAjustement = "La porte dépasse la dimension du mur (" + dimensionMur + "m).";
                    }
                    
                    if (ajustementNecessaire) {
                        // Demander confirmation pour l'ajustement automatique
                        int choix = JOptionPane.showConfirmDialog(this,
                            messageAjustement + "\n\nVoulez-vous ajuster automatiquement cette porte?",
                            "Ajustement nécessaire",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        
                        if (choix == JOptionPane.YES_OPTION) {
                            // Appliquer les ajustements
                            if (largeurPorte > dimensionMur) {
                                // Réduire la largeur de la porte
                                largeurPorte = dimensionMur;
                                largeurFields.get(i).setText(String.valueOf(dimensionMur));
                            }
                            
                            // Ajuster la position si nécessaire
                            double positionMax = dimensionMur - largeurPorte;
                            if (position > positionMax) {
                                position = positionMax;
                                listePositionPortes.get(i).setText(String.valueOf(positionMax));
                            }
                        } else {
                            // L'utilisateur refuse l'ajustement, on annule
                            listePositionPortes.get(i).requestFocus();
                            return false;
                        }
                    }
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Veuillez entrer des valeurs numériques valides pour les portes", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                
            }

            // Validation des fenêtres
            // Validation des fenêtres
            for (int i = 0; i < listeMurFenetres.size(); i++) {
                try {
                    String mur = (String) listeMurFenetres.get(i).getSelectedItem();
                    double position = Double.parseDouble(listePositionFenetres.get(i).getText());
                    double largeurFenetre = Double.parseDouble(listeLargeurFenetres.get(i).getText());
                    
                    // Vérifier que les valeurs sont positives
                    if (position < 0 || largeurFenetre <= 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Les valeurs des fenêtres doivent être positives", 
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                        listePositionFenetres.get(i).requestFocus();
                        return false;
                    }
                    
                    // Calculer la dimension maximale selon le mur
                    double dimensionMur = mur.equalsIgnoreCase("haut") || mur.equalsIgnoreCase("bas") 
                        ? longueur : largeur;  // Utilisez les variables existantes
                    
                    // Vérifier si un ajustement est nécessaire
                    boolean ajustementNecessaire = false;
                    String messageAjustement = "";
                    
                    if (largeurFenetre > dimensionMur) {
                        ajustementNecessaire = true;
                        messageAjustement = "La largeur de la fenêtre (" + largeurFenetre + "m) dépasse la dimension du mur (" + dimensionMur + "m).";
                    } else if (position + largeurFenetre > dimensionMur) {
                        ajustementNecessaire = true;
                        messageAjustement = "La fenêtre dépasse la dimension du mur (" + dimensionMur + "m).";
                    }
                    
                    if (ajustementNecessaire) {
                        // Demander confirmation pour l'ajustement automatique
                        int choix = JOptionPane.showConfirmDialog(this,
                            messageAjustement + "\n\nVoulez-vous ajuster automatiquement cette fenêtre?",
                            "Ajustement nécessaire",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        
                        if (choix == JOptionPane.YES_OPTION) {
                            // Appliquer les ajustements
                            if (largeurFenetre > dimensionMur) {
                                // Réduire la largeur de la fenêtre
                                largeurFenetre = dimensionMur;
                                listeLargeurFenetres.get(i).setText(String.valueOf(dimensionMur));
                            }
                            
                            // Ajuster la position si nécessaire
                            double positionMax = dimensionMur - largeurFenetre;
                            if (position > positionMax) {
                                position = positionMax;
                                listePositionFenetres.get(i).setText(String.valueOf(positionMax));
                            }
                        } else {
                            // L'utilisateur refuse l'ajustement, on annule
                            listePositionFenetres.get(i).requestFocus();
                            return false;
                        }
                    }
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Veuillez entrer des valeurs numériques valides pour les fenêtres", 
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer des valeurs numériques valides", 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }



    // Renderer personnalisé pour la liste
    class PieceListRenderer extends DefaultListCellRenderer {
        @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Piece) {
                    Piece p = (Piece) value;
                    setText(String.format("%s (%.2f x %.2fm)", p.getNom(), p.getLongueur(), p.getLargeur()));
                }
                return this;
            }
        }
    }
