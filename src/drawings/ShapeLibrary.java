package drawings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.lang.ClassNotFoundException;

/**
 * Maintains a collection of shapes read in from the shapes folder, serving them up when requested
 * 
 * @author             Viktoryia Simakova
 * @version            2020-07-12
 */
public class ShapeLibrary {

    /** collection of shapes maintained by the library */
    private ArrayList<Shape> shapes;

    /**
     * Creates collection and populates it from folder
     */
    public ShapeLibrary() throws FileNotFoundException, IOException, ClassNotFoundException {
        shapes = new ArrayList<Shape>();
        loadShapes();
    }

    /**
     * Retrieves a count of shapes in the collection
     * 
     * @return      shape count
     */
    public int getShapeCount() {
        return shapes.size();
    }

    /**
     * Retrieves a specific shape, by index
     * 
     * @param       index       position of the shape in the collection
     * @return                  specified shape
     */
    public Shape getShape(int index) {
        return shapes.get(index);
    }

    /**
     * Retrieves a specified shape, by name
     * 
     * @param       shapeName   name of the sought shape
     * @return                  the requested shape, or null if there is no such shape
     */
    public Shape getShapeByName(String shapeName) {
        boolean flag = true;
        int idx = 0;
        for (int i = 0; i < shapes.size() && flag; i++){
            if (shapes.get(i).getName().equals(shapeName)) {
                idx = i;
                flag = false;
            }
        }
        return shapes.get(idx);
    }

    /**
     * Adds a new shape to the collection
     * 
     * @param       shape       the new shape to add; must not be null
     */
    public void addShape(Shape shape) {
        if (shape == null) {
            throw new IllegalArgumentException("shape cannot be null");
        }
        shapes.add(shape);
    }

    /**
     * Loads the shapes from the shapes folder, deserializing them and storing them in the list
     * 
     * @return                  count of shapes loaded
     */
    private int loadShapes() throws FileNotFoundException, IOException, ClassNotFoundException {
        Utility.createShapeFiles();
        File folderToScan = new File("shapes");
        File[] folderContent = folderToScan.listFiles();

        if (folderContent == null) {
            //no files/folders found
        } else {
            for (int idx = 0; idx < folderContent.length; idx++) {
                if(!(folderContent[idx].getName().equals("README.txt"))) {
                    shapes.add(loadShape("" + folderContent[idx]));
                }
            }
        }
        return shapes.size();
    }

    /**
     * Reads a shape from a file
     * 
     * @param          filePath         the path to the file containing the shape
     * @return                          the read shape, or null if the shape can't be read
     */
    private Shape loadShape(String filePath) {
        Shape readShape = null;
        try {
            FileInputStream shapeFile = new FileInputStream(filePath);
            ObjectInputStream objIn = new ObjectInputStream(shapeFile);
            readShape = (Shape)objIn.readObject();
            objIn.close();
            shapeFile.close();
        } catch (FileNotFoundException e) {
            // No shape read/added
        } catch (IOException e) {
            // No shape read/added
        } catch (ClassNotFoundException e) {
            // No shape read/added
        }
        //System.out.println(readShape.toString());
        return readShape;        
    }
    
    /**
     * Retrieves state of this object
     * 
     * @return                          the state of this object
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < shapes.size(); i++) {
            result += shapes.get(i).toString() + "\n";
        }
        return result;
    }
}
