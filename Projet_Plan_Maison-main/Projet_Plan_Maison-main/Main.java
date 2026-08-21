    import javax.swing.*;
    import java.awt.*;
    import java.util.ArrayList;

    public class Main {
        public static void main(String[] args) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JFrame frame = new JFrame("Plan de Maison");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(1080, 720);

                    JTabbedPane tabbedPane = new JTabbedPane();

                    // Création du seul onglet qui contient maintenant tout
                    OngletPieces ongletPieces = new OngletPieces();
                    tabbedPane.addTab("Configuration", ongletPieces);
                    
                    // Onglet pour afficher le plan avec zoom et scroll
                    MaisonPanel maisonPanel = new MaisonPanel();
                    JScrollPane scrollPane = new JScrollPane(maisonPanel);
                    scrollPane.setWheelScrollingEnabled(false);
                    scrollPane.setViewportBorder(null); // Supprime la bordure
                    scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

                    tabbedPane.addTab("Visualisation", scrollPane);

                    // Panel pour les contrôles de zoom
                    JPanel zoomPanel = new JPanel();
                    JButton zoomInBtn = new JButton("+", new ImageIcon(getClass().getResource("/icons/zoom-in.png")));
                    JButton zoomOutBtn = new JButton("-", new ImageIcon(getClass().getResource("/icons/zoom-out.png")));
                    JLabel zoomLabel = new JLabel("Zoom: 100%");

                    // Ajout des tooltips
                    zoomInBtn.setToolTipText("Zoom avant");
                    zoomOutBtn.setToolTipText("Zoom arrière");
                    
                    zoomInBtn.addActionListener(e -> {
                        maisonPanel.zoomIn();
                        zoomLabel.setText(String.format("Zoom: %d%%", (int)(maisonPanel.getZoomFactor() * 100)));
                        scrollPane.revalidate();
                    });
                    
                    zoomOutBtn.addActionListener(e -> {
                        maisonPanel.zoomOut();
                        zoomLabel.setText(String.format("Zoom: %d%%", (int)(maisonPanel.getZoomFactor() * 100)));
                        scrollPane.revalidate();
                    });
                    
                    zoomPanel.add(zoomOutBtn);
                    zoomPanel.add(zoomLabel);
                    zoomPanel.add(zoomInBtn);   

                    // Bouton pour dessiner le plan
                    JButton btnAfficher = new JButton("Afficher le plan", new ImageIcon(getClass().getResource("/icons/view.png")));
                    btnAfficher.setToolTipText("Générer le plan de la maison");
                    btnAfficher.addActionListener(e -> {
                        try {
                            // Vérifier et corriger automatiquement les débordements
                            ongletPieces.verifierDebordements(frame);
                            
                            // Vérifier les positions des pièces
                            if (!ongletPieces.verifierDebordementsPieces(frame)) {
                                return; // L'utilisateur a refusé l'ajustement
                            }
                            
                            // Mettre à jour l'affichage
                            maisonPanel.setDonnees(
                                ongletPieces.getTerrainLongueur(),
                                ongletPieces.getTerrainLargeur(),
                                ongletPieces.getMaisonLongueur(),
                                ongletPieces.getMaisonLargeur(),
                                ongletPieces.getMaisonPositionX(),
                                ongletPieces.getMaisonPositionY(),
                                ongletPieces.getListePieces()
                            );
                            
                            tabbedPane.setSelectedComponent(scrollPane);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, 
                                "Veuillez entrer des valeurs numériques valides", 
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    });

                    // Boutons d'export
                    JButton exportPngBtn = new JButton("Exporter en PNG", new ImageIcon(getClass().getResource("/icons/image.png")));
                    
                    exportPngBtn.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            try {
                                String filePath = fileChooser.getSelectedFile().getPath();
                                if (!filePath.toLowerCase().endsWith(".png")) {
                                    filePath += ".png";
                                }
                                ExportUtils.exportToPNG(maisonPanel, filePath);
                                JOptionPane.showMessageDialog(frame, "Export PNG réussi !");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Erreur lors de l'export : " + ex.getMessage());
                            }
                        }
                    });

                    // Boutons de sauvegarde/chargement
                    JButton saveBtn = new JButton("Sauvegarder", new ImageIcon(getClass().getResource("/icons/sauvegarder.png")));
                    JButton loadBtn = new JButton("Charger", new ImageIcon(getClass().getResource("/icons/open.png")));
                    
                    saveBtn.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            try {
                                ProjectSerializer.sauvegarderProjet(
                                    fileChooser.getSelectedFile().getPath(),
                                    ongletPieces.getTerrainLongueur(),
                                    ongletPieces.getTerrainLargeur(),
                                    ongletPieces.getMaisonLongueur(),
                                    ongletPieces.getMaisonLargeur(),
                                    "", // On ne passe plus de position textuelle
                                    ongletPieces.getListePieces(),
                                    ongletPieces.getMaisonPositionX(),
                                    ongletPieces.getMaisonPositionY()
                                );
                                JOptionPane.showMessageDialog(frame, "Sauvegarde réussie !");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Erreur de sauvegarde : " + ex.getMessage());
                            }
                        }
                    });
                    
                    loadBtn.addActionListener(e -> {
                        JFileChooser fileChooser = new JFileChooser();
                        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                            try {
                                Object[] data = ProjectSerializer.chargerProjet(fileChooser.getSelectedFile().getPath());
                                ongletPieces.setTerrainLongueur((double)data[0]);
                                ongletPieces.setTerrainLargeur((double)data[1]);
                                ongletPieces.setMaisonLongueur((double)data[2]);
                                ongletPieces.setMaisonLargeur((double)data[3]);
                                ongletPieces.setListePieces((ArrayList<Piece>)data[5]);
                                ongletPieces.setMaisonPositionX((double)data[6]);
                                ongletPieces.setMaisonPositionY((double)data[7]);
                                
                                JOptionPane.showMessageDialog(frame, "Chargement réussi !");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(frame, "Erreur de chargement : " + ex.getMessage());
                            }
                        }
                    });

                    // Organisation des panels
                    JPanel northPanel = new JPanel();
                    northPanel.add(exportPngBtn);                    
                    JPanel southPanel = new JPanel(new BorderLayout());
                    JPanel filePanel = new JPanel();
                    filePanel.add(saveBtn);
                    filePanel.add(loadBtn);
                    
                    southPanel.add(zoomPanel, BorderLayout.WEST);
                    southPanel.add(filePanel, BorderLayout.CENTER);
                    southPanel.add(btnAfficher, BorderLayout.EAST);

                    frame.add(northPanel, BorderLayout.NORTH);
                    frame.add(tabbedPane, BorderLayout.CENTER);
                    frame.add(southPanel, BorderLayout.SOUTH);
                    frame.setVisible(true);
                }
            });
        }
    }