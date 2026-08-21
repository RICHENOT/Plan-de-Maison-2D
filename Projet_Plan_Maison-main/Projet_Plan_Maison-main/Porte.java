import java.awt.*;
import java.awt.geom.Arc2D;
import java.io.Serializable;
import javax.swing.JOptionPane;

public class Porte implements Serializable {
            private String mur;
            private double positionMetres;
            private String direction; // "Intérieur" ou "Extérieur"
            private String type; // "Simple" ou "Double"
            private String sensOuverture; // "Gauche" ou "Droite"
            private double largeur; // en mètres
            private static final long serialVersionUID = 1L;
            private int epaisseurMurPorte = 8;

            // Nouveaux points contrôlables pour les rayons (à ajouter en haut de la classe)    
            private int rayon1StartX, rayon1StartY; // Point de départ rayon 1
            private int rayon1EndX, rayon1EndY;     // Point d'arrivée rayon 1
            private int rayon2StartX, rayon2StartY; // Point de départ rayon 2
            private int rayon2EndX, rayon2EndY;     // Point d'arrivée rayon 2


            public Porte(String mur, double positionMetres, String direction, 
                        String type, String sensOuverture, double largeur) {
                this.mur = mur;
                this.positionMetres = positionMetres;
                this.direction = direction;
                this.type = type;
                this.sensOuverture = sensOuverture;
                this.largeur = largeur;
            }

            public void dessiner(Graphics2D g2d, int x, int y, int epaisseurMur, double scale) {
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int largeurPixels = (int)(largeur * scale);
                int rayonPixels = largeurPixels;

                
                // 1. Dessin du cadre de la porte - UTILISER LA MÊME ÉPAISSEUR QUE LES MURS
                g2d.setColor(new Color(255, 240, 200));
                g2d.setStroke(new BasicStroke(epaisseurMurPorte)); // Utilisez epaisseurMur au lieu de 8
                
                if (mur.equalsIgnoreCase("gauche") || mur.equalsIgnoreCase("droite")) {
                    // Pour les portes verticales, ajustez la position x selon le mur
                    if (mur.equalsIgnoreCase("gauche")) {
                        g2d.drawLine(x + epaisseurMur, y + epaisseurMur, x + epaisseurMur, y-epaisseurMur + largeurPixels);
                    } else {
                        g2d.drawLine(x, y + epaisseurMur, x, y - (epaisseurMur/2) + largeurPixels);
                    }
                } else {
                    // Pour les portes horizontales, ajustez la position y selon le mur
                    if (mur.equalsIgnoreCase("haut")) {
                        g2d.drawLine(x + epaisseurMur, y + epaisseurMur, x + largeurPixels -epaisseurMur, y + epaisseurMur);
                    } else {
                        g2d.drawLine(x+epaisseurMur, y, x + largeurPixels - epaisseurMur, y);
                    }
                }
                // 2. Dessin du battant avec les nouveaux rayons
                dessinerBattantPorte(g2d, x, y, epaisseurMur, largeurPixels, rayonPixels);                 
                
            }

            private void dessinerBattantPorte(Graphics2D g2d, int x, int y, int epaisseurMur, 
                                            int largeurPixels, int rayonPixels) {
                int arcX = x;
                int arcY = y;
                int startAngle = 0;
                int arcAngle = 90;
                int diameter = rayonPixels * 2;

                if (type.equalsIgnoreCase("Double")) {
                dessinerPorteDoubleUnifiee(g2d, x, y, epaisseurMur, largeurPixels, rayonPixels);
                return;
            }

            switch (mur.toLowerCase()) {
                case "gauche":
                    if (direction.equals("Intérieur")) {
                        if (sensOuverture.equals("Gauche")) {        
                            // Intérieur gauche - ouverture gauche
                            arcX = x - rayonPixels + epaisseurMur;
                            arcY = y - rayonPixels;
                            startAngle = -90;
                            arcAngle = 90;
                            // Rayon 1 (vertical)
                            rayon1StartX = x +epaisseurMur;
                            rayon1StartY = y + rayonPixels;
                            rayon1EndX = x + epaisseurMur;
                            rayon1EndY = y;
                            // Rayon 2 (horizontal)
                            rayon2StartX = x + epaisseurMur;
                            rayon2StartY = y;
                            rayon2EndX = x + rayonPixels + epaisseurMur;
                            rayon2EndY = y;
                            
                        } else {
                                                        
                            // Intérieur gauche - ouverture droite
                            arcX = x - rayonPixels + epaisseurMur;
                            arcY = y;
                            startAngle = 0;
                            arcAngle = 90;
                            // Rayon 1 (vertical)
                            rayon1StartX = x + epaisseurMur;
                            rayon1StartY = y + rayonPixels;
                            rayon1EndX = x+ epaisseurMur;
                            rayon1EndY = y;
                            // Rayon 2 (diagonal)
                            rayon2StartX = x + epaisseurMur;
                            rayon2StartY = y + rayonPixels;
                            rayon2EndX = x + rayonPixels + epaisseurMur;
                            rayon2EndY = y + rayonPixels;
                        }
                    } else {
                        if (sensOuverture.equals("Gauche")) {
                                                
                            // Extérieur gauche - ouverture gauche
                            arcX = x + epaisseurMur - rayonPixels;
                            arcY = y;
                            startAngle = 90;
                            arcAngle = 90;
                            // Rayon 1 (horizontal)
                            rayon1StartX = x + epaisseurMur;
                            rayon1StartY = y + rayonPixels;
                            rayon1EndX = x + epaisseurMur - rayonPixels;
                            rayon1EndY = y + rayonPixels;
                            // Rayon 2 (vertical)
                            rayon2StartX = x + epaisseurMur;
                            rayon2StartY = y + rayonPixels;
                            rayon2EndX = x + epaisseurMur;
                            rayon2EndY = y ;
                            
                        } else {
                        // Extérieur gauche - ouverture droite
                            arcX = x + epaisseurMur - rayonPixels;
                            arcY = y - rayonPixels;
                            startAngle = -90;
                            arcAngle = -90;
                            // Rayon 1 (horizontal)
                            rayon1StartX = x + epaisseurMur;
                            rayon1StartY = y;
                            rayon1EndX = x + epaisseurMur - rayonPixels;
                            rayon1EndY = y;
                            // Rayon 2 (vertical)
                            rayon2StartX = x + epaisseurMur;
                            rayon2StartY = y;
                            rayon2EndX = x + epaisseurMur;
                            rayon2EndY = y + rayonPixels;
                        }
                    }
            break;


                        
                case "droite":
            if (direction.equals("Intérieur")) {
                if (sensOuverture.equals("Gauche")) {
                                    
                    // Intérieur droite - ouverture gauche
                    arcX = x - rayonPixels;
                    arcY = y;
                    startAngle = 90;
                    arcAngle = 90;
                    // Rayon 1 (horizontal)
                    rayon1StartX = x;
                    rayon1StartY = y + rayonPixels;
                    rayon1EndX = x - rayonPixels;
                    rayon1EndY = y + rayonPixels;
                    // Rayon 2 (vertical)
                    rayon2StartX = x;
                    rayon2StartY = y + rayonPixels;
                    rayon2EndX = x;
                    rayon2EndY = y;
                                
                } else {
                    // Extérieur gauche - ouverture droite
                    arcX = x - rayonPixels;
                    arcY = y - rayonPixels;
                    startAngle = -90;
                    arcAngle = -90;
                    // Rayon 1 (horizontal)
                    rayon1StartX = x;
                    rayon1StartY = y;
                    rayon1EndX = x - rayonPixels;
                    rayon1EndY = y;
                    // Rayon 2 (vertical)
                    rayon2StartX = x ;
                    rayon2StartY = y;
                    rayon2EndX = x;
                    rayon2EndY = y + rayonPixels;
                }
            } else {
                if (sensOuverture.equals("Gauche")) {
                    // Extérieur droite - ouverture gauche
                    arcX = x - rayonPixels;
                    arcY = y - rayonPixels;
                    startAngle = 0;
                    arcAngle = -90;
                    // Rayon 1 (vertical)
                    rayon1StartX = x;
                    rayon1StartY = y;
                    rayon1EndX = x;
                    rayon1EndY = y + rayonPixels;
                    // Rayon 2 (horizontal)
                    rayon2StartX = x ;
                    rayon2StartY = y;
                    rayon2EndX = x + rayonPixels;
                    rayon2EndY = y;
                } else {
                    // Extérieur droite - ouverture droite
                    arcX = x - rayonPixels;
                    arcY = y;
                    startAngle = 0;
                    arcAngle = 90;
                    // Rayon 1 (vertical)
                    rayon1StartX = x;
                    rayon1StartY = y + rayonPixels;
                    rayon1EndX = x;
                    rayon1EndY = y;
                    // Rayon 2 (horizontal)
                    rayon2StartX = x;
                    rayon2StartY = y + rayonPixels;
                    rayon2EndX = x + rayonPixels;
                    rayon2EndY = y + rayonPixels;
                }
            }
            break;
            
            case "haut":
                if (direction.equals("Intérieur")) {
                    if (sensOuverture.equals("Gauche")) {
                        // Intérieur haut - ouverture gauche
                        arcX = x;
                        arcY = y - rayonPixels + epaisseurMur;
                        startAngle = 180;
                        arcAngle = 90;
                        // Rayon 1 (horizontal)
                        rayon1StartX = x + rayonPixels;
                        rayon1StartY = y + epaisseurMur;
                        rayon1EndX = x;
                        rayon1EndY = y + epaisseurMur;
                        // Rayon 2 (vertical)
                        rayon2StartX = x + rayonPixels;
                        rayon2StartY = y + epaisseurMur;
                        rayon2EndX = x + rayonPixels;
                        rayon2EndY = y + rayonPixels + epaisseurMur;
                    } else {
                    // Intérieur haut - ouverture droite
                        arcX = x - rayonPixels ;
                        arcY = y - rayonPixels + epaisseurMur;
                        startAngle = 0;
                        arcAngle = -90;

                        // Rayon 1 (vertical)
                        rayon1StartX = x;
                        rayon1StartY = y + epaisseurMur;
                        rayon1EndX = x;
                        rayon1EndY = y + rayonPixels + epaisseurMur;

                        // Rayon 2 (horizontal)
                        rayon2StartX = x;
                        rayon2StartY = y + epaisseurMur;
                        rayon2EndX = x + rayonPixels;
                        rayon2EndY = y + epaisseurMur;
                    }
                } else {
                    if (sensOuverture.equals("Gauche")) {
                        // Extérieur haut - ouverture gauche
                        arcX = x - rayonPixels;
                        arcY = y + epaisseurMur - rayonPixels;
                        startAngle = 0;
                        arcAngle = 90;
                        // Rayon 1 (horizontal)
                        rayon1StartX = x + rayonPixels;
                        rayon1StartY = y + epaisseurMur;
                        rayon1EndX = x;
                        rayon1EndY = y + epaisseurMur;
                        // Rayon 2 (vertical)
                        rayon2StartX = x;
                        rayon2StartY = y + epaisseurMur;
                        rayon2EndX = x;
                        rayon2EndY = y + epaisseurMur - rayonPixels;
                    } else {
                        // Extérieur haut - ouverture droite
                        arcX = x;
                        arcY = y + epaisseurMur - rayonPixels;
                        startAngle = 90;
                        arcAngle = 90;
                        // Rayon 1 (horizontal)
                        rayon1StartX = x;
                        rayon1StartY = y + epaisseurMur ;
                        rayon1EndX = x + rayonPixels;
                        rayon1EndY = y + epaisseurMur ;
                        // Rayon 2 (vertical)
                        rayon2StartX = x + rayonPixels;
                        rayon2StartY = y + epaisseurMur;
                        rayon2EndX = x + rayonPixels;
                        rayon2EndY = y + epaisseurMur - rayonPixels;
                    }
                }
                break;
                
                case "bas":
                    if (direction.equals("Intérieur")) {
                        if (sensOuverture.equals("Gauche")) {
                            // Intérieur bas - ouverture gauche
                            arcX = x - rayonPixels;
                            arcY = y - rayonPixels;
                            startAngle = 0;
                            arcAngle = 90;
                            // Rayon 1 (horizontal)
                            rayon1StartX = x;
                            rayon1StartY = y;
                            rayon1EndX = x + rayonPixels;
                            rayon1EndY = y;
                            // Rayon 2 (vertical)
                            rayon2StartX = x ;
                            rayon2StartY = y;
                            rayon2EndX = x ;
                            rayon2EndY = y- rayonPixels;
                        } else {
                            // Intérieur bas - ouverture droite
                            arcX = x;
                            arcY = y - rayonPixels;
                            startAngle = 90;
                            arcAngle = 90;

                        
                                // Rayon 1 (horizontal)
                                rayon1StartX = x;
                                rayon1StartY = y;
                                rayon1EndX = x + rayonPixels;
                                rayon1EndY = y;

                                // Rayon 2 (vertical)
                                rayon2StartX = x + rayonPixels;
                                rayon2StartY = y;
                                rayon2EndX = x + rayonPixels;
                                rayon2EndY = y - rayonPixels;
                        }
                    } else {
                        if (sensOuverture.equals("Gauche")) {
                                // Extérieur bas - ouverture gauche
                                arcX = x;
                                arcY = y - rayonPixels ;
                                startAngle = 180;
                                arcAngle = 90;

                            // Rayon 1 (horizontal)
                                rayon1StartX = x + rayonPixels;
                                rayon1StartY = y;
                                rayon1EndX = x;
                                rayon1EndY = y;

                                // Rayon 2 (vertical)
                                rayon2StartX = x + rayonPixels;
                                rayon2StartY = y;
                                rayon2EndX = x + rayonPixels;
                                rayon2EndY = y + rayonPixels;

                        } else {
                                                
                            arcX = x - rayonPixels ;
                            arcY = y - rayonPixels ;
                            startAngle = 0;
                            arcAngle = -90;

                            // Rayon 1 (vertical)
                            rayon1StartX = x;
                            rayon1StartY = y;
                            rayon1EndX = x;
                            rayon1EndY = y + rayonPixels;

                            // Rayon 2 (horizontal)
                            rayon2StartX = x;
                            rayon2StartY = y;
                            rayon2EndX = x + rayonPixels;
                            rayon2EndY = y;
                        }
                    }
                    break;
                }

                // Dessin de l'arc
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new Arc2D.Double(arcX, arcY, diameter, diameter, startAngle, arcAngle, Arc2D.OPEN));
                
                // Dessin des rayons
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(2));
                
                g2d.drawLine(rayon1StartX, rayon1StartY, rayon1EndX, rayon1EndY);
                g2d.drawLine(rayon2StartX, rayon2StartY, rayon2EndX, rayon2EndY);
            }
                
            

            //porte aroa
            private void dessinerPorteDoubleUnifiee(Graphics2D g2d, int x, int y, int epaisseurMur, 
                                                int largeurPixels, int rayonPixels) {
                // Paramètres communs
                int demiRayon = rayonPixels / 2;
                    
                
                // Tableau pour stocker les 4 rayons (2 par battant)
                int[][] rayons = new int[4][4]; // [battant][rayon][x1,y1,x2,y2]
                
                // Dessin des battants et calcul des rayons
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(1));
                
                switch (mur.toLowerCase()) {
                    case "gauche":
                        if (direction.equals("Intérieur")) {
                            // Battant gauche (intérieur, ouverture gauche)
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon + epaisseurMur, y - demiRayon, rayonPixels, rayonPixels, -90, 90, Arc2D.OPEN));
                            // Rayons battant gauche
                            rayons[0] = new int[]{x + epaisseurMur, y + demiRayon, x + epaisseurMur, y}; // Vertical
                            rayons[1] = new int[]{x + epaisseurMur, y, x + epaisseurMur + demiRayon, y}; // Horizontal
                            
                            // Battant droit (intérieur, ouverture droite)
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon + epaisseurMur, y + largeurPixels - demiRayon, rayonPixels, rayonPixels, 0, 90, Arc2D.OPEN));
                            // Rayons battant droit
                            rayons[2] = new int[]{x + epaisseurMur, y + largeurPixels, x + epaisseurMur, y + largeurPixels - rayonPixels}; // Vertical
                            rayons[3] = new int[]{x + epaisseurMur, y + largeurPixels , x + demiRayon + epaisseurMur, y + largeurPixels}; // Horizontal
                        } else {
                            // Extérieur - configuration inversée
                            // Battant gauche (extérieur)
                            g2d.draw(new Arc2D.Double(
                                x + epaisseurMur - demiRayon, y + demiRayon, rayonPixels, rayonPixels, 90, 90, Arc2D.OPEN));
                            // Rayons battant gauche
                            rayons[0] = new int[]{x + epaisseurMur, y + rayonPixels, x + epaisseurMur - demiRayon, y + rayonPixels}; // Horizontal
                            rayons[1] = new int[]{x + epaisseurMur, y, x + epaisseurMur, y + demiRayon}; // Vertical
                            
                            // Battant droit (extérieur)
                            g2d.draw(new Arc2D.Double(
                                x + epaisseurMur - demiRayon, y - demiRayon, rayonPixels, rayonPixels, 180, 90, Arc2D.OPEN));
                            // Rayons battant droit
                            rayons[2] = new int[]{x + epaisseurMur, y, x + epaisseurMur - demiRayon, y}; // horiz
                            rayons[3] = new int[]{x + epaisseurMur, y + largeurPixels, x + epaisseurMur, y + largeurPixels - demiRayon}; // Vertical
                        }
                        break;
                        
                    case "droite":
                        if (direction.equals("Intérieur")) {
                            // Battant gauche (intérieur)
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon, y + demiRayon, rayonPixels, rayonPixels, 90, 90, Arc2D.OPEN));
                            // Rayons battant gauche
                            rayons[0] = new int[]{x, y + rayonPixels, x - demiRayon, y + rayonPixels}; // Horizontal
                            rayons[1] = new int[]{x, y, x, y + demiRayon}; // Vertical
                            
                            // Battant droit (extérieur)
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon, y - demiRayon, rayonPixels, rayonPixels, 180, 90, Arc2D.OPEN));
                            // Rayons battant droit
                            rayons[2] = new int[]{x, y, x - demiRayon, y}; // horiz
                            rayons[3] = new int[]{x, y + largeurPixels, x, y + largeurPixels - demiRayon}; // Vertical
                        } else {
                            // Extérieur
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon, y - demiRayon, rayonPixels, rayonPixels, -90, 90, Arc2D.OPEN));
                            // Rayons battant gauche
                            rayons[0] = new int[]{x, y + demiRayon, x, y}; // Vertical
                            rayons[1] = new int[]{x, y, x + demiRayon, y}; // Horizontal
                            
                            // Battant droit (intérieur, ouverture droite)
                            g2d.draw(new Arc2D.Double(
                                x - demiRayon, y + largeurPixels - demiRayon, rayonPixels, rayonPixels, 0, 90, Arc2D.OPEN));
                            // Rayons battant droit
                            rayons[2] = new int[]{x, y + largeurPixels, x, y + largeurPixels - rayonPixels}; // Vertical
                            rayons[3] = new int[]{x, y + largeurPixels , x + demiRayon, y + largeurPixels}; // Horizontal
                        }
                        break;
                        
                    case "haut":
                if (direction.equals("Intérieur")) {
                    // Battant gauche (intérieur, ouverture gauche)
                    g2d.draw(new Arc2D.Double(
                        x + demiRayon , y - demiRayon + epaisseurMur, rayonPixels, rayonPixels, 180, 90, Arc2D.OPEN));
                    // Rayons battant gauche
                    rayons[0] = new int[]{x + demiRayon, y + epaisseurMur, x, y + epaisseurMur}; // Horizontal
                    rayons[1] = new int[]{x + rayonPixels, y + epaisseurMur, x + rayonPixels, y + demiRayon + epaisseurMur}; // Vertical
                    
                    // Battant droit (intérieur, ouverture droite)
                    g2d.draw(new Arc2D.Double(
                        x + largeurPixels - demiRayon*3, y - demiRayon + epaisseurMur, rayonPixels, rayonPixels, 270, 90, Arc2D.OPEN));
                    // Rayons battant droit
                    rayons[2] = new int[]{x + largeurPixels - demiRayon, y + epaisseurMur, x + largeurPixels, y + epaisseurMur}; // Horizontal
                    rayons[3] = new int[]{x + largeurPixels - rayonPixels, y + epaisseurMur, x + largeurPixels - rayonPixels, y + demiRayon + epaisseurMur}; // Vertical
                } else {
                    // Extérieur - configuration inversée
                    // Battant gauche (extérieur)
                    g2d.draw(new Arc2D.Double(
                        x - demiRayon, y + epaisseurMur - demiRayon, rayonPixels, rayonPixels, 0, 90, Arc2D.OPEN));
                    // Rayons battant gauche
                    rayons[0] = new int[]{x + demiRayon, y + epaisseurMur, x, y + epaisseurMur}; // Horizontal
                    rayons[1] = new int[]{x, y + epaisseurMur, x , y + epaisseurMur - demiRayon}; // Vertical
                    
                    // Battant droit (extérieur)
                    g2d.draw(new Arc2D.Double(
                        x + largeurPixels - demiRayon, y + epaisseurMur - demiRayon, rayonPixels, rayonPixels, 90, 90, Arc2D.OPEN));
                    // Rayons battant droit
                    rayons[2] = new int[]{x + largeurPixels - demiRayon, y + epaisseurMur, x + largeurPixels, y + epaisseurMur}; // Horizontal
                    rayons[3] = new int[]{x + largeurPixels, y + epaisseurMur, x + largeurPixels, y + epaisseurMur - demiRayon}; // Vertical
                }
                break;

            case "bas":
                if (direction.equals("Intérieur")) {
                    // Battant gauche (intérieur, ouverture gauche)
                    // Extérieur - configuration inversée
                    // Battant gauche (extérieur)
                    g2d.draw(new Arc2D.Double(
                        x - demiRayon, y - demiRayon, rayonPixels, rayonPixels, 0, 90, Arc2D.OPEN));
                    // Rayons battant gauche
                    rayons[0] = new int[]{x + demiRayon, y , x, y}; // Horizontal
                    rayons[1] = new int[]{x, y, x , y - demiRayon}; // Vertical
                    
                    // Battant droit (extérieur)
                    g2d.draw(new Arc2D.Double(
                        x + largeurPixels - demiRayon, y - demiRayon, rayonPixels, rayonPixels, 90, 90, Arc2D.OPEN));
                    // Rayons battant droit
                    rayons[2] = new int[]{x + largeurPixels - demiRayon, y, x + largeurPixels, y}; // Horizontal
                    rayons[3] = new int[]{x + largeurPixels, y, x + largeurPixels, y - demiRayon}; // Vertical
                } else {
                    // Extérieur
                    // Battant gauche (intérieur, ouverture gauche)
                    g2d.draw(new Arc2D.Double(
                        x + demiRayon, y - demiRayon, rayonPixels, rayonPixels, 180, 90, Arc2D.OPEN));
                    // Rayons battant gauche
                    rayons[0] = new int[]{x + demiRayon, y, x, y}; // Horizontal
                    rayons[1] = new int[]{x + rayonPixels, y, x + rayonPixels, y + demiRayon}; // Vertical
                    
                    // Battant droit (intérieur, ouverture droite)
                    g2d.draw(new Arc2D.Double(
                        x + largeurPixels - demiRayon*3, y - demiRayon, rayonPixels, rayonPixels, 270, 90, Arc2D.OPEN));
                    // Rayons battant droit
                    rayons[2] = new int[]{x + largeurPixels - demiRayon, y, x + largeurPixels, y}; // Horizontal
                    rayons[3] = new int[]{x + largeurPixels - rayonPixels, y, x + largeurPixels - rayonPixels, y + demiRayon}; // Vertical
                }
                break;
                }
                
                // Dessine des rayons
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(2));
                for (int[] rayon : rayons) {
                    if (rayon[0] != 0 || rayon[1] != 0 || rayon[2] != 0 || rayon[3] != 0) {
                        g2d.drawLine(rayon[0], rayon[1], rayon[2], rayon[3]);
                    }
                }
            }

            public boolean validerEtAjusterPorte(double longueurPiece, double largeurPiece, Component parent) {
                double dimensionMur = (mur.equalsIgnoreCase("haut") || mur.equalsIgnoreCase("bas")) 
                        ? longueurPiece : largeurPiece;
                
                // Vérification si un ajustement est nécessaire
                boolean ajustementNecessaire = (largeur > dimensionMur) || (positionMetres > (dimensionMur - largeur)) || (positionMetres < 0);
                
                if (!ajustementNecessaire) {
                    return false; // Aucun ajustement nécessaire
                }
                
                // Demander confirmation à l'utilisateur
                int choix = JOptionPane.showConfirmDialog(parent,
                    "La porte dépasse les dimensions du mur.\nVoulez-vous l'ajuster automatiquement?",
                    "Ajustement nécessaire",
                    JOptionPane.YES_NO_OPTION);
                
                if (choix == JOptionPane.YES_OPTION) {
                    // Appliquer les ajustements
                    if (largeur > dimensionMur) {
                        largeur = dimensionMur;
                    }
                    
                    double positionMax = dimensionMur - largeur;
                    if (positionMetres > positionMax) {
                        positionMetres = positionMax;
                    }
                    if (positionMetres < 0) {
                        positionMetres = 0;
                    }
                    return true;
                }
                
                return false; // L'utilisateur a choisi de ne pas ajuster
            }

            
            public void setLargeur(double largeur) {
                this.largeur = largeur;
            }

            public void setPositionMetres(double positionMetres) {
                this.positionMetres = positionMetres;
            }

            public String getNom() {
                return "Porte " + mur + " pos:" + positionMetres + "m";
            }

            
            // Getters
            public String getMur() { return mur; }
            public double getPositionMetres() { return positionMetres; }
            public String getDirection() { return direction; }
            public String getType() { return type; }
            public String getSensOuverture() { return sensOuverture; }
            public double getLargeur() { return largeur; }
        }