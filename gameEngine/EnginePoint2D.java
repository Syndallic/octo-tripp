package gameEngine;


/*********************************************************
 * Beginning Java Game Programming, 3rd Edition
 * by Jonathan S. Harbour
 * Point2D Class
 **********************************************************/
public class EnginePoint2D extends Object {
    private double x, y;

    //int constructor
    public EnginePoint2D(int x, int y) {
        setX(x);
        setY(y);
    }
    //float constructor
    public EnginePoint2D(float x, float y) {
        setX(x);
        setY(y);
    }
    //double constructor
    public EnginePoint2D(double x, double y) {
        setX(x);
        setY(y);
    }

    //X property
    public double X() { return x; }
    public void setX(double x) { this.x = x; }
    public void setX(float x) { this.x = (double) x; }
    public void setX(int x) { this.x = (double) x; }

    //Y property
    public double Y() { return y; }
    public void setY(double y) { this.y = y; }
    public void setY(float y) { this.y = (double) y; }
    public void setY(int y) { this.y = (double) y; }
}


