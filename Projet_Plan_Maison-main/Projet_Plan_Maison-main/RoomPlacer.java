import java.util.ArrayList;
import java.util.Comparator;

public class RoomPlacer {
    public static boolean placeRoomsAutomatically(double maisonLongueur, double maisonLargeur, ArrayList<Piece> pieces) {
        // Trier les pièces par taille décroissante (meilleur taux de remplissage)
        pieces.sort(Comparator.comparingDouble((Piece p) -> p.getLongueur() * p.getLargeur()).reversed());

        // Créer une grille de placement avec une précision de 0.5m
        int gridWidth = (int)(maisonLongueur * 2);
        int gridHeight = (int)(maisonLargeur * 2);
        boolean[][] occupationGrid = new boolean[gridWidth][gridHeight];

        for (Piece piece : pieces) {
            boolean placed = false;
            
            for (int rotation = 0; rotation < 4; rotation++) {
                double longueur = (rotation % 2 == 0) ? piece.getLongueur() : piece.getLargeur();
                double largeur = (rotation % 2 == 0) ? piece.getLargeur() : piece.getLongueur();
                
                // Convertir en unités de grille
                int gridLongueur = (int)(longueur * 2);
                int gridLargeur = (int)(largeur * 2);

                // Chercher une position valide
                for (int x = 0; x <= gridWidth - gridLongueur; x++) {
                    for (int y = 0; y <= gridHeight - gridLargeur; y++) {
                        if (canPlace(occupationGrid, x, y, gridLongueur, gridLargeur)) {
                            placePiece(occupationGrid, x, y, gridLongueur, gridLargeur);
                            piece.setPositionX(x / 2.0);
                            piece.setPositionY(y / 2.0);
                            if (piece != null) {
                                piece.setRotation(rotation * 90); 
                            }
                            placed = true;
                            break;
                        }
                    }
                    if (placed) break;
                }
                if (placed) break;
            }
            
            if (!placed) return false;
        }
        return true;
    }

    private static boolean canPlace(boolean[][] grid, int x, int y, int l, int w) {
        for (int i = x; i < x + l; i++) {
            for (int j = y; j < y + w; j++) {
                if (grid[i][j]) return false;
            }
        }
        return true;
    }

    private static void placePiece(boolean[][] grid, int x, int y, int l, int w) {
        for (int i = x; i < x + l; i++) {
            for (int j = y; j < y + w; j++) {
                grid[i][j] = true;
            }
        }
    }
}