import java.awt.*;
import java.util.Random;




public class RegPolygon implements Comparable<RegPolygon>{

    Color pColor = Color.BLACK; // Colour of the polygon, set to a Colour object, default set to black
    int pId = 000000;    // Polygon ID should be a six digit non-negative integer
    int pSides;          // Number of sides of the polygon, should be non-negative value

    double pStarting_angle;   // starting angle

    double pRadius;           // radius of polygon

    int polyCenX;    // x value of centre point (pixel) of polygon when drawn on the panel
    int polyCenY;    // y value of centre point (pixel of polygon when drawn on the panel
    double [] pointsX;  // int array containing x values of each vertex (corner point) of the polygon
    double [] pointsY;  // int array containing y values of each vertex (corner point) of the polygon

    // Constructor currently sets the number of sides, starting angle of the Polygon
    // You will need to modify the constructor to set the pId and pColour fields.
    public RegPolygon(int p_sides, double st_angle, double rad) {

        this.pSides = p_sides;              // user defined number of sides should be non-negative
        this.pStarting_angle = st_angle;   // user defined starting angle
        this.pRadius = rad;                // user defined radius
        pointsX = new double[pSides];
        pointsY = new double[pSides];
    }

    // Constructor used in the display only feature
    public RegPolygon(int p_sides, double st_angle, double rad,  Color color) {

        this.pSides = p_sides;              // user defined number of sides should be non-negative
        this.pStarting_angle = st_angle;   // user defined starting angle
        this.pRadius = rad;                // user defined radius
        this.pColor = color;
        pointsX = new double[pSides];
        pointsY = new double[pSides];
    }
    public RegPolygon(int pId, int p_sides, double st_angle, double rad,  Color color) {
        this.pSides = p_sides;              // user defined number of sides should be non-negative
        this.pStarting_angle = st_angle;   // user defined starting angle
        this.pRadius = rad;                // user defined radius
        this.pId = pId;
        this.pColor = color;
        pointsX = new double[pSides];
        pointsY = new double[pSides];
    }


    // Used to populate the points array with the vertices corners (points) and construct a polygon with the
    // number of sides defined by pSides and the length of each side defined by pSideLength.
    // Dimension object that is passed in as an argument is used to get the width and height of the ContainerPanel
    // and used to determine the x and y values of its centre point that will be used to position the drawn Polygon.
    private Polygon getPolygonPoints(Dimension dim) {

        polyCenX = dim.width / 2;                  // x value of centre point of the polygon
        polyCenY = dim.height / 2;                 // y value of centre point of the polygon
        double angleIncrement = 2.0 * Math.PI / pSides;  // incremenet of each angle
        Polygon p = new Polygon();                 // Polygon to be drawn

        // Compute coordinates for n-sided regular polygon of given radius and start angle
        int [] xPointsArray = new int[this.pSides];
        int [] yPointsArray = new int[this.pSides];
        for(int i = 0; i <this.pSides ; ++i){
            double x = pRadius * Math.cos(angleIncrement * i) + polyCenX;
            double y = pRadius * Math.sin(angleIncrement * i) + polyCenY;
            xPointsArray[i]  = (int) Math.round(x);
            yPointsArray[i]  =  (int) Math.round(y);
        }


        for(int i = 0; i < pSides; i++)
        {
            pointsX[i] = xPointsArray[i];
            pointsY[i] = yPointsArray[i];

            p.addPoint((int)pointsX[i], (int)pointsY[i]);
            pStarting_angle = pStarting_angle + angleIncrement;
        }
        return p;
    }


    // You will need to modify this method to set the colour of the Polygon to be drawn
    // Remember that Graphics2D has a setColor() method available for this purpose
    public void drawPolygon(Graphics2D g, Dimension d) {
        g.setColor(pColor);
        g.fillPolygon(getPolygonPoints(d));
    }


    // gets a stored ID
    public int getID() {
        return pId;
    }


    @Override
    // method used for comparing PolygonContainer objects based on stored ids, you need to complete the method
    public int compareTo(RegPolygon o) {
        return Integer.compare(this.getID(), o.getID());
    }

    // outputs a string representation of the PolygonContainer object, you need to complete this to use for testing
    public String toString()
    {
        return "";
    }
}
