package tankAttack;

/*********************************************************
 * Base game image class for bitmapped game entities
 **********************************************************/
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

import javax.swing.JPanel;

public class ImageEntity extends BaseGameEntity {
    //variables
    protected Image image;
    protected JPanel panel;
    protected AffineTransform at;
    protected Graphics2D g2d;

    //default constructor
    
    public ImageEntity(JPanel p) {
        panel = p;
        setAlive(true);
    }    
    
    public ImageEntity(JPanel p, String path) {
        panel = p;
        setImage(load(path));
        setAlive(true);
    }

    public Image getImage() { return image; }

    public void setImage(Image image) {
        this.image = image;
        double x = panel.getSize().width/2  - width()/2;
        double y = panel.getSize().height/2 - height()/2;
        at = AffineTransform.getTranslateInstance(x, y);
    }

    public int width() {
        if (image != null)
            return image.getWidth(panel);
        else
            return 0;
    }
    public int height() {
        if (image != null)
            return image.getHeight(panel);
        else
            return 0;
    }

    public double getCenterX() {
        return getX() + width() / 2;
    }
    public double getCenterY() {
        return getY() + height() / 2;
    }

    public void setGraphics(Graphics2D g) {
        g2d = g;
    }

    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        }
        catch (Exception e) { }

        return url;
    }

    public Image load(String filename) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        image = tk.getImage(getURL("/res/" + filename));
        while(getImage().getWidth(panel) <= 0);
        double x = panel.getSize().width/2  - width()/2;
        double y = panel.getSize().height/2 - height()/2;
        at = AffineTransform.getTranslateInstance(x, y);
        return image;
    }

    public void transform() {
        at.setToIdentity();
        at.translate((int)getX() + width()/2, (int)getY() + height()/2);
        at.rotate(Math.toRadians(getFaceAngle()));
        at.translate(-width()/2, -height()/2);
    }

    public void draw() {
        g2d.drawImage(getImage(), at, panel);
    }

    //bounding rectangle
    public Rectangle getBounds() {
        Rectangle r;
        r = new Rectangle((int)getX(), (int)getY(), width(), height());
        return r;
    }

}
