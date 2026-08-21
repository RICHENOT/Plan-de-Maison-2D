import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.JPanel;

public class DragAndDropPanel extends JPanel {
    private Piece pieceSelectionnee;
    private int offsetX, offsetY;
    private List<Piece> pieces; // Liste des pièces à gérer
    private double scaleFactor = 40.0; // Facteur d'échelle (Mila mitovy @ MaisonPanel)
    
    public DragAndDropPanel() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pieceSelectionnee = trouverPiece(e.getX(), e.getY());
                if (pieceSelectionnee != null) {
                    offsetX = e.getX() - (int)(pieceSelectionnee.getPositionX() * scaleFactor);
                    offsetY = e.getY() - (int)(pieceSelectionnee.getPositionY() * scaleFactor);
                }
            }
            
            public void mouseReleased(MouseEvent e) {
                pieceSelectionnee = null;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (pieceSelectionnee != null) {
                    // Mettre à jour la position en tenant compte de l'échelle
                    pieceSelectionnee.setPositionX((e.getX() - offsetX) / scaleFactor);
                    pieceSelectionnee.setPositionY((e.getY() - offsetY) / scaleFactor);
                    repaint();
                }
            }
        });
    }
    
    // Méthode pour définir la liste des pièces
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }
    
    // Méthode pour trouver la pièce sous les coordonnées (x,y)
    private Piece trouverPiece(int x, int y) {
        if (pieces == null || pieces.isEmpty()) {
            return null;
        }
        
        // Parcourir les pièces dans l'ordre inverse (pour gérer le z-index)
        for (int i = pieces.size() - 1; i >= 0; i--) {
            Piece p = pieces.get(i);
            
            // Calculer les coordonnées et dimensions de la pièce à l'écran
            int pieceX = (int)(p.getPositionX() * scaleFactor);
            int pieceY = (int)(p.getPositionY() * scaleFactor);
            int pieceWidth = (int)(p.getLongueurEffective() * scaleFactor);
            int pieceHeight = (int)(p.getLargeurEffective() * scaleFactor);
            
            // Vérifier si le point (x,y) est dans la pièce
            if (x >= pieceX && x <= pieceX + pieceWidth &&
                y >= pieceY && y <= pieceY + pieceHeight) {
                return p;
            }
        }
        
        return null; // Aucune pièce trouvée à ces coordonnées
    }
}