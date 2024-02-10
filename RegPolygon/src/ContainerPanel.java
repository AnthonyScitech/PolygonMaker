import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;


// ContainerPanel class for CE203 Assignment to use and modify if needed
// Date: 04/11/2023
// Author: F. Doctor

public class ContainerPanel extends JPanel{

    ContainerFrame conFrame;

    public ContainerPanel(ContainerFrame cf) {
        conFrame = cf;   // reference to ContainerFrame object
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D comp = (Graphics2D)g;   // You will need to use a Graphics2D objects for this
        Dimension size = getSize();        // You will need to use this Dimension object to get
        System.out.println("height "+size.height);
        System.out.println("width "+size.width);
        // the width / height of the JPanel in which the
                                           // Polygon is going to be drawn

        // Based on which stored PolygonContainer object you want to be retrieved from the
        // ArrayList and displayed, the object would be accessed and its drawPolygon() method
        // would be called here.

        // modify this to search for IDs to retrieve the
        if(conFrame.action !=null){
            switch (conFrame.action){
                case DISPLAY -> conFrame.display.drawPolygon(comp,size);
                case SEARCH -> {
                    if(conFrame.drawSelection  >= 0){
                        conFrame.polygons.get(conFrame.drawSelection ).drawPolygon(comp, size);
                    }
                }
            }
        }


        // adding



        //printing list ...

    }
}
