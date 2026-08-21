import java.io.*;
import java.util.ArrayList;

public class ProjectSerializer {
    public static void sauvegarderProjet(String fichier, 
                                    double terrainLongueur, double terrainLargeur,
                                    double maisonLongueur, double maisonLargeur,
                                    String positionMaison,
                                    ArrayList<Piece> pieces,
                                    double maisonPositionX, double maisonPositionY) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier))) {
            oos.writeObject(terrainLongueur);
            oos.writeObject(terrainLargeur);
            oos.writeObject(maisonLongueur);
            oos.writeObject(maisonLargeur);
            oos.writeObject(positionMaison != null ? positionMaison : "");
            oos.writeObject(pieces != null ? pieces : new ArrayList<Piece>());
            oos.writeObject(maisonPositionX);
            oos.writeObject(maisonPositionY);
        }
    }
    
    public static Object[] chargerProjet(String fichier) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier))) {
            return new Object[] {
                ois.readObject(), // terrainLongueur
                ois.readObject(), // terrainLargeur
                ois.readObject(), // maisonLongueur
                ois.readObject(), // maisonLargeur
                ois.readObject(), // positionMaison
                ois.readObject(), // pieces
                ois.readObject(), // maisonPositionX
                ois.readObject()  // maisonPositionY
            };
        }
    }
}