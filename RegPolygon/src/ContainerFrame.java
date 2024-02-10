import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class ContainerFrame extends JFrame{

    JTextField regNoField,noOfSidesField,startAngleField,radiusField;  // fields used to collect data from users
    List<RegPolygon> polygons = new ArrayList<>();  // used to store saved polygon entered by user
    String[] choices = {"","black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange","pink","red","yellow"}; // used to store color for the dropdown
    final JComboBox<String> colorChooser = new JComboBox<String>(choices); //  dropdown for color selection
    final JLabel listSize = new JLabel(); //  used to display number of polygon in list
    int drawSelection = -1;   // Selection variable to determine which Polygon is selected to be drawn
    int width = 700 ; // width of the application
    int height = 720; // height of the application
    DisplayAction action = null;  // unsaved polygon that is displayed when display is clicked
    RegPolygon display = null; // used to determine whether to display new without saving or one from the list
    public void createComponents() {
        addTestData();


        JPanel drawPanel = new ContainerPanel(this);

        // setup button panel
        JPanel buttPanel = new JPanel();
        buttPanel.add(getAddBtn());
        buttPanel.add(getDisplayBtn());
        buttPanel.add(getSearchBtn());
        buttPanel.add(getSortBtn());
        buttPanel.add(new JLabel("Polygons "));
        setListSizeLabel();
        buttPanel.add(listSize);

        regNoField = new JTextField(6);
        noOfSidesField = new JTextField(6);
        startAngleField = new JTextField(6);
        radiusField = new JTextField(6);
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("RegNo"));
        inputPanel.add(regNoField);
        inputPanel.add(new JLabel("Sides"));
        inputPanel.add(noOfSidesField);
        inputPanel.add(new JLabel("Angle"));
        inputPanel.add(startAngleField);
        inputPanel.add(new JLabel("Radius"));
        inputPanel.add(radiusField);
        inputPanel.add(colorChooser);
        add(inputPanel,BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(buttPanel, BorderLayout.SOUTH);

        setSize(width, height);
        setVisible(true);
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );	// Close action.
    }

    private void addTestData() {
        // public RegPolygon(int pId, int p_sides, double st_angle, double rad,  Color color) {
        polygons.add(new RegPolygon(263762,7,10.0,140.0,Color.GRAY));
        polygons.add(new RegPolygon(109284,3,10.0,100.0,Color.BLACK));
        polygons.add(new RegPolygon(747993,13,10.0,150.0,Color.GREEN));
        polygons.add(new RegPolygon(386438,6,10.0,130.0,Color.DARK_GRAY));
        polygons.add(new RegPolygon(298463,4,10.0,110.0,Color.BLUE));
        polygons.add(new RegPolygon(982478,10,10.0,160.0,Color.LIGHT_GRAY));
        polygons.add(new RegPolygon(123434,5,10.0,120.0,Color.CYAN));
        polygons.add(new RegPolygon(235642,11,10.0,150.0,Color.MAGENTA));
        polygons.add(new RegPolygon(258723,12,10.0,150.0,Color.ORANGE));
        polygons.add(new RegPolygon(123098,14,10.0,150.0,Color.PINK));
    }

    // create Add Button and implement is actions
    private JButton getAddBtn(){
        final JButton btn = new JButton("Add");
        btn.addActionListener(e -> {
            try {
                polygons.add(
                        new RegPolygon(
                                validateRegNo(regNoField.getText(),true),
                                validateSides(noOfSidesField.getText()),
                                validateStartAngle(startAngleField.getText()),
                                validateRadius(radiusField.getText()),
                                validateColor(Objects.requireNonNull(colorChooser.getSelectedItem()).toString())
                        )
                );
                showMessage("Polygon "+regNoField.getText()+" has been added", "Success",JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                setListSizeLabel();
            }catch (FieldValidationException ex){
                showMessage(ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }

        });
        return btn;
    }

    private void setListSizeLabel() {
        listSize.setText(""+polygons.size());
    }

    private void clearFields() {
        regNoField.setText("");
        noOfSidesField.setText("");
        startAngleField.setText("");
        radiusField.setText("");
        colorChooser.setSelectedIndex(0);
    }

    // create Display Button and implement is actions
    private JButton getDisplayBtn(){
        final JButton btn = new JButton("Display");
        btn.addActionListener(e -> {
            action = DisplayAction.DISPLAY;
            try {
                display = new RegPolygon(
                        validateSides(noOfSidesField.getText()),
                        validateStartAngle(startAngleField.getText()),
                        validateRadius(radiusField.getText()),
                        validateColor(Objects.requireNonNull(colorChooser.getSelectedItem()).toString()));
                repaint();
            } catch (FieldValidationException ex) {
                showMessage(ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        return btn;
    }

    // create Search Button and implement is actions
    private JButton getSearchBtn(){
        final JButton btn = new JButton("Search");
        btn.addActionListener(e -> {
            try {
                validateRegNo(regNoField.getText(),false);
                if(!contain(regNoField.getText())){
                    showMessage("Polygon with ID "+ regNoField.getText()+ " does not exits","",JOptionPane.ERROR_MESSAGE);
                }else{
                   Optional<RegPolygon> optional =  polygons.stream().filter(regPolygon -> regPolygon.pId==Integer.parseInt(regNoField.getText())).findFirst();
                    optional.ifPresent(regPolygon -> {
                        action = DisplayAction.SEARCH;
                        drawSelection = polygons.indexOf(regPolygon);
                        repaint();
                    });
                }
            } catch (FieldValidationException ex) {
                showMessage(ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        return btn;
    }

    // create Sort Button and implement is actions
    private JButton getSortBtn(){
        final JButton btn = new JButton("Sort");
        btn.addActionListener(e -> {
            Collections.sort(polygons);
            polygons.forEach(regPolygon -> System.out.println("id ="+regPolygon.getID()));
        });
        return btn;
    }

    // validation to check if string is a number
    private boolean isStringANumericValue(String strNum) {
        if (strNum == null) {
            return true;
        } else{
            try {
                double d = Double.parseDouble(strNum);
            } catch (NumberFormatException nfe) {
                return true;
            }
        }
        return false;
    }

    // validation for Polygon Id Field Value
    private int validateRegNo(String regNo,boolean searchList) throws FieldValidationException{
        String message = "";
        if(regNo.isBlank()){
            message = "RegNo Field Required";
        } else if (regNo.length() != 6 ) {
            message = "RegNo Field length 6";
        } else if (isStringANumericValue(regNo)) {
            message = "RegNo Field should be numeric";
        } else if (regNo.contains("-") || Integer.parseInt(regNo) < 0) {
            message = "RegNo Field should a positive number";
        }
        else if(searchList && contain(regNo)) {
            message = "The Polygon with id " + regNo + " cannot be added as the Id is duplicated";
        }
        if(!message.isBlank())
            throw new FieldValidationException(message);
        return Integer.parseInt(regNo);
    }
    // validation for Polygon Sides Field Value
    private int validateSides(String sides) throws FieldValidationException{
        String message = "";
        if(sides.isBlank()){
            message = "Sides Field Required";
        } else if (isStringANumericValue(sides)) {
            message= "Sides Field should be numeric";
        } else if (Integer.parseInt(sides) < 3) {
            message ="Sides Field should be greater than 2";
        }
        if(!message.isBlank())
            throw new FieldValidationException(message);
        return Integer.parseInt(sides);
    }

    // validation for Polygon Start Angle Field Value
    private double validateStartAngle(String angle) throws FieldValidationException{
        String message = "";
        if (angle.isBlank()) {
            message = "Angle Field Required";
        }else if (isStringANumericValue(angle)) {
            message = "Angle Field should be numeric";
        }
        if(!message.isBlank())
            throw new FieldValidationException(message);
        return Double.parseDouble(angle);
    }

    // validation for Polygon Radius Field Value
    private double validateRadius(String radius) throws FieldValidationException{
        String message = "";
        if(radius.isBlank())
            message = "Radius Field Required";
        else if (isStringANumericValue(radius)) {
            message = "Radius Field should be numeric";
        } else if (Double.parseDouble(radius) > ((double) (height - 120) / 2) || Double.parseDouble(radius) < 50.0) {
            message = "Radius Field should be no more than "+ ((height - 120) / 2) +" but no less than 50 ";
        }
        if(!message.isBlank())
            throw new FieldValidationException(message);
        return Double.parseDouble(radius);
    }

    // validation for Polygon Color Field Value
    private Color validateColor(String color){
       return switch (color){
           case "blue" -> Color.BLUE;
           case "cyan" -> Color.CYAN;
           case "darkGray" -> Color.DARK_GRAY;
           case "gray" -> Color.GRAY;
           case "green" -> Color.GREEN;
           case "lightGray" -> Color.LIGHT_GRAY;
           case "magenta" -> Color.MAGENTA;
           case "orange" -> Color.ORANGE;
           case "pink" -> Color.PINK;
           case "red" -> Color.RED;
           case "yellow" -> Color.YELLOW;
           default -> Color.BLACK;
        };
    }

    // check if polygon with id exists in list
    private boolean contain(String regNo){
        return polygons.stream().anyMatch(regPolygon -> regPolygon.pId == Integer.parseInt(regNo));
    }
    // show messages output ot optionPane
    public void showMessage(String infoMessage, String title,int MessageType)
    {
        JOptionPane.showMessageDialog(this, infoMessage,  title, MessageType);
    }
    public static void main(String[] args) {
        ContainerFrame cFrame = new ContainerFrame();
        cFrame.createComponents();
    }

}
