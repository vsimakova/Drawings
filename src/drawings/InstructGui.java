package drawings;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.awt.Color;
import javax.swing.DefaultListModel;

/**
 * Manages the GUI for instruction file creation
 *
 * @author          Bill Barry
 * @version         2018-09-09
 */
public class InstructGui {

    // Static variables; used in utility functions
    private static JFrame canvasViewFrame;
    private static JFrame drawViewFrame;
    private static CanvasView canvasView;
    private static DrawView drawView;
    private static DefaultListModel<String> canvasListModel;
    private static DefaultListModel<String> drawListModel;

    /**
     * Private constructor meant to defeat instantiation; this class is really only meant to be used via main method
     */
    private InstructGui() {
    }
    
    /**
     * Runs the show, creating windows, attaching the panels, and getting everything rolling
     * 
     * @param   args    a list of command line arguments
     */
    public static void main (String[] args) {

        canvasListModel = new DefaultListModel<>();
        drawListModel = new DefaultListModel<>();

        canvasViewFrame = new JFrame("Canvas View");
        canvasViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvasViewFrame.setResizable(false);
        canvasView = new CanvasView();
        JPanel canvasViewPanel = canvasView;
        canvasViewFrame.getContentPane().add(canvasViewPanel);
        canvasViewFrame.pack();
        canvasViewFrame.setVisible(true);

        drawViewFrame = new JFrame("Draw View");
        drawViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawViewFrame.setResizable(false);
        drawView = new DrawView();
        JPanel drawViewPanel = drawView;
        drawViewFrame.getContentPane().add(drawViewPanel);
        drawViewFrame.pack();
        drawViewFrame.setVisible(false);
    }

    /**
     * Retrieves the list model for canvas instructions
     * 
     * @return      the list model for the canvas instructions
     */
    public static DefaultListModel<String> getCanvasListModel() {
        return canvasListModel;
    }

    /**
     * Retrieves the list model for draw instructions
     * 
     * @return      the list model for the draw instructions
     */
    public static DefaultListModel<String> getDrawListModel() {
        return drawListModel;
    }

    /**
     * Supplies the available view switch directions
     */
    public enum ViewSwitchDirection {
        CANVAS_TO_DRAW, 
        DRAW_TO_CANVAS
    }

    /**
     * Switches between available views
     * 
     * @param   dir     the direction of the view switch
     */
    public static boolean switchViews(ViewSwitchDirection dir) {
        if (dir == null) {
            return false;
        } else {
            if (dir == ViewSwitchDirection.CANVAS_TO_DRAW) {
                drawViewFrame.setLocation(canvasViewFrame.getLocation());
                drawViewFrame.setVisible(true);
                canvasViewFrame.setVisible(false);
            } else {
                canvasViewFrame.setLocation(drawViewFrame.getLocation());
                canvasViewFrame.setVisible(true);
                drawViewFrame.setVisible(false);
            }
            return true;
        }
    }

    /**
     * Writes all instructions to a user-selectable file
     * 
     * @param   owner       the panel that is calling the file chooser dialog
     */
    public static void writeToFile(JPanel owner) {

        boolean proceed = true;
        JFileChooser fileChooser = new JFileChooser(new File("."));
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showSaveDialog(owner);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            
            // Need to add extension; not done automatically
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave + ".txt");
            }
            
            // Handle overwrite scenario
            if (fileToSave.exists()) {
                proceed = false;
                int confirm = JOptionPane.showConfirmDialog(owner, "File exists, overwrite?", "File exists", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    proceed = true;
                }
            }

            try {
                // Collect information to write
                String contents = "";
                if (canvasListModel.getSize() != 0) {
                    contents += canvasListModel.get(0) + "\n";    // only one item
                }
                if (drawListModel.getSize() != 0) {
                    for(int instrIdx = 0; instrIdx < drawListModel.size(); instrIdx++) {
                        contents += drawListModel.get(instrIdx) + "\n";
                    }
                }

                // Write information to the file
                PrintStream ps = new PrintStream(fileToSave);
                ps.print(contents);
                ps.close();
            }
            catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(owner, "File could not be written; check permissions");
            }
        }
    }

    /**
     * Turns a Color into an instruction-file-appropriate string, with a prefix prepended as requested
     * 
     * @param   color   the color to turn into a string
     * @param   prefix  the prefix to prepend onto each resulting color command
     * @return          the instruction-file-ready string; empty if color is null
     */
    public static String colorToRgb(Color color, String prefix) {
        if (color == null) {
            return "";
        }
        
        String rgb;
        if (prefix.isEmpty()) {
            rgb =    ", red=" +    color.getRed() 
            +  ", green=" +  color.getGreen()
            +  ", blue=" +   color.getBlue();
        } else {
            rgb =    ", " + prefix + "Red=" +    color.getRed() 
            +  ", " + prefix + "Green=" +  color.getGreen()
            +  ", " + prefix + "Blue=" +   color.getBlue();
        }
        return rgb;
    }

}
