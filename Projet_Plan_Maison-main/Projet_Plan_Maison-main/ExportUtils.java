import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportUtils {
    
    public static void exportToPNG(Component component, String filePath) throws Exception {
        BufferedImage image = new BufferedImage(
            component.getWidth(), 
            component.getHeight(), 
            BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g2d = image.createGraphics();
        component.paint(g2d);
        g2d.dispose();
        
        if (!filePath.toLowerCase().endsWith(".png")) {
            filePath += ".png";
        }
        
        ImageIO.write(image, "PNG", new File(filePath));
    }
}