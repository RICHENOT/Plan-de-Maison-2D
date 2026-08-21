import java.util.ArrayList;

public class HouseLayoutValidator {
    public static boolean checkRoomFitsHouse(double houseLength, double houseWidth, 
                                           ArrayList<Piece> pieces) {
        double totalLeftRight = 0;
        double totalTopBottom = 0;
        for (Piece piece : pieces) {
            if (piece.getPosition().equalsIgnoreCase("gauche") || 
                piece.getPosition().equalsIgnoreCase("droite")) {
                totalLeftRight += piece.getLargeur();
            } else {
                totalTopBottom += piece.getLongueur();
            }
        }
        
        return totalLeftRight <= houseWidth && totalTopBottom <= houseLength;
    }
    
    public static boolean checkHouseFitsTerrain(double terrainLength, double terrainWidth,
                                              double houseLength, double houseWidth) {
        return houseLength <= terrainLength && houseWidth <= terrainWidth;
    }
    
    public static double[] calculateRequiredHouseSize(ArrayList<Piece> pieces) {
        double totalLeftRight = 0;
        double totalTopBottom = 0;
        
        for (Piece piece : pieces) {
            if (piece.getPosition().equalsIgnoreCase("gauche") || 
                piece.getPosition().equalsIgnoreCase("droite")) {
                totalLeftRight += piece.getLargeur();
            } else {
                totalTopBottom += piece.getLongueur();
            }
        }
        
        return new double[]{totalTopBottom + 2, totalLeftRight + 2}; // +2 pour marges
    }
}