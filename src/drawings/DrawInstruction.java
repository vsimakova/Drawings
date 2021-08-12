package drawings;

import java.awt.Color;
import java.util.Scanner;

/**
 * Code for the DrawInstruction class, managing a single drawing instruction
 *
 * @author      Bill Barry
 * @version     2017-09-07
 */
public class DrawInstruction {

    //**********************************************************************************************
    //          INSTANCE DATA
    //**********************************************************************************************
    /** the symbolic name for the shape, e.g., "circle"  */
    private String shapeName;
    /** the scaling for the shape, e.g., 200 indicating 200% scaling */
    private int scalePercent;
    /** the x coordinate serving as the upper left-hand corner for the shape drawing */
    private int startingX;
    /** the y coordinate serving as the upper left-hand corner for the shape drawing */
    private int startingY;
    /** the number of times this shape should be drawn, e.g., 4 = 4 total shapes */
    private int repeats;
    /** the x offset at which repeated shapes should be drawn */
    private int repeatOffsetX;
    /** the y offset at which repeated shapes should be drawn */
    private int repeatOffsetY;
    /** whether the shape should be drawn filled; true = filled, false = outline only */
    private boolean filled;
    /** the color with which to draw the shape */
    private Color color;
    /** the rotation (around the center) at which the shape should be drawn */
    private int rotate;
    /** the additional rotation at which each repeated shape should be drawn */
    private int repeatRotate;

    //**********************************************************************************************
    //          CONSTRUCTORS
    //**********************************************************************************************
    
    /**
     * Constructor for the DrawInstruction class
     *
     * @param   shapeName       the friendly name assigned to the shape
     * @param   scalePercent    the percent to which the shape should be scaled, when drawn, e.g., 100 indicates 100% of original size.  If less than 1, will be reset to 1
     * @param   startingX       the x coordinate at which the shape should be drawn.  Use Integer.MIN_VALUE to request random placement on the canvas
     * @param   startingY       the y coordinate at which the shape should be drawn.  Use Integer.MIN_VALUE to request random placement on the canvas
     * @param   repeats         the number of times the drawing should be repeated.  Makes sense only with random placement or repeat offsets.  If less than 1, will be reset to 1.
     * @param   repeatOffsetX   for repeated shapes, the x offset for each subsequent shape
     * @param   repeatOffsetY   for repeated shapes, the y offset for each subsequent shape
     * @param   filled          whether the shape should be filled in (true) or drawn in outline only (false)
     * @param   color           the Color to use for drawing the shape
     * @param   rotate          the angle the shape is to be rotated, in degrees, clockwise from original position
     * @param   repeatRotate    the rotation to be applied to any repeated shapes drawn
     * 
     * @throws                  IllegalArgumentException if the shape name is null or empty
     * @throws                  IllegalArgumentException if the color is null
     */
    private DrawInstruction(String shapeName, int scalePercent, int startingX, int startingY, 
                       int repeats, int repeatOffsetX, int repeatOffsetY, boolean filled,
                       Color color, int rotate, int repeatRotate) {
        if (shapeName == null || shapeName.isEmpty()) {
            throw new IllegalArgumentException("Shape names must not be null or empty");
        }
        if (color == null) {            
            throw new IllegalArgumentException("Color must not be null");
        }
        this.shapeName     = shapeName;
        this.scalePercent  = scalePercent;
        this.startingX     = startingX;
        this.startingY     = startingY;
        this.repeats       = repeats;
        this.repeatOffsetX = repeatOffsetX;
        this.repeatOffsetY = repeatOffsetY;
        this.filled        = filled;
        this.color         = color;
        this.rotate        = rotate;
        this.repeatRotate  = repeatRotate;
        validateOrDefault(this);
    }

    //**********************************************************************************************
    //          INSTANCE METHODS
    //**********************************************************************************************
    /**
     * Private, empty constructor for Instruction class, useful for reading instructions from files, for example.  Sets all values to initial defaults
     *
     */
    private DrawInstruction() {
        this("none",                // shapeName
             100,                   // scalePercent 
             0,                     // startingX 
             0,                     // startingY
             1,                     // repeats
             0,                     // repeatOffsetX
             0,                     // repeatOffsetY
             true,                  // filled
             new Color(0, 0, 0),    // color
             0,                     // rotate   
             0);                    // repeatRotate
    }

    /**
     * Retrieves the name of the shape
     *
     * @return      the name of the shape
     */
    public String getShapeName() { 
        return shapeName; 
    }

    /**
     * Retrieves the percent at which the shape will be drawn
     *
     * @return      the scaling percent for the shape
     */
    public int getScalePercent() {
        return scalePercent; 
    }

    /**
     * Retrieves the starting x coordinate at which the shape will be drawn
     *
     * @return      the starting x coordinate at which the shape will be drawn
     */
    public int getStartingX() {
        return startingX; 
    }

    /**
     * Retrieves the starting y coordinate at which the shape will be drawn
     *
     * @return      the starting y coordinate at which the shape will be drawn
     */
    public int getStartingY() {
        return startingY; 
    }

    /**
     * Retrieves the number of requested repetitions of the shape
     *
     * @return      the requested number of repetitions of the shape
     */
    public int getRepeats() {
        return repeats; 
    }

    /**
     * Retrieves the x offset for subsequent drawings of repeated shapes
     *
     * @return      the x offset for subsequent shape drawings
     */
    public int getRepeatOffsetX() {
        return repeatOffsetX; 
    }

    /**
     * Retrieves the y offset for subsequent drawings of repeated shapes
     *
     * @return      the y offset for subsequent shape drawings
     */
    public int getRepeatOffsetY() { 
        return repeatOffsetY; 
    }

    /**
     * Retrieves the filled setting for the shape
     *
     * @return      whether fill is requested on the drawn shape
     */
    public boolean getFilled() {
        return filled; 
    }

    /**
     * Retrieves the Color to be used for drawing the shape
     *
     * @return      the shape's Color
     */
    public Color getColor() {
        return color; 
    }
    
    /**
     * Retrieves the angle of rotation for the shape
     * 
     * @return      the rotation to be applied to the shape
     */
    public int getRotate() {
        return rotate;
    }

    /**
     * Retrieves the repeat rotation angle for additional shapes; to be used with repeated shapes
     * 
     * @return      the rotation to be applied to each additional shape drawn
     */
    public int getRepeatRotate() {
        return repeatRotate;
    }
    
    //**********************************************************************************************
    //          STATIC METHODS
    //**********************************************************************************************
    
    /**
     * Reads an instruction from the supplied Scanner, and returns a corresponding Instruction object
     *
     * @param   fileIn  the Scanner object to use for retrieving instruction information
     * @return          an Instruction object containing the read instructions (plus defaults)
     */
    public static DrawInstruction readFromFile(Scanner fileIn) {
        DrawInstruction instruction = new DrawInstruction();

        // Grab next instruction and split it up
        String instructLine = fileIn.nextLine();
        String[] instructFields = instructLine.split(",");
        int red = 0, green = 0, blue = 0;

        // Separate out the individual instructions and interpret them
        for (String field : instructFields) {
            String[] pieces = field.split("=");
            String cmd   = pieces[0] = pieces[0].trim().toLowerCase();
            String value = pieces[1] = pieces[1].trim().toLowerCase();

            switch (cmd) {
                case "shape"   : instruction.shapeName = value;                                  break;
                case "scale"   : instruction.scalePercent = Integer.parseInt(value);             break;
                case "x"       : instruction.startingX = Integer.parseInt(value);                break;
                case "y"       : instruction.startingY = Integer.parseInt(value);                break;
                case "rep"     : instruction.repeats = Integer.parseInt(value);                  break;
                case "repoffx" : instruction.repeatOffsetX = Integer.parseInt(value);            break;
                case "repoffy" : instruction.repeatOffsetY = Integer.parseInt(value);            break;
                case "filled"  : instruction.filled = (value.equals("false")) ? false : true;    break;
                case "rotate"  : instruction.rotate = Integer.parseInt(value);                   break;
                case "reprot"  : instruction.repeatRotate = Integer.parseInt(value);             break;
                case "red"     : red   = Integer.parseInt(value);                                break;
                case "green"   : green = Integer.parseInt(value);                                break;
                case "blue"    : blue  = Integer.parseInt(value);                                break;
                default        : /* do nothing  */                                               break;
            }
        }

        // Ensure in-range RGB values
        red   = Utility.rgbRangeLimit(red);
        green = Utility.rgbRangeLimit(green);
        blue  = Utility.rgbRangeLimit(blue);
        instruction.color = new Color(red, green, blue);

        return instruction;
    }

    /**
     * Validates the instruction information in an existing Instruction, setting any invalid data to defaults
     *
     * @param   instruction     an existing Instruction to validate
     */
    private static void validateOrDefault(DrawInstruction instruction) {
        // Percent must be >= 1
        instruction.scalePercent = Math.max(1, instruction.scalePercent);

        // Repeats must be >= 1
        instruction.repeats      = Math.max(1, instruction.repeats);
    }
    
    /**
     * Retrieves state of this object
     * 
     * @return                          the state of this object
     */
    public String toString() {
        String result = "";
        result += "shapeName: " + shapeName + " " + "scalePercent: " + scalePercent + " " + "startingX: " + startingX + "\n";
        result += "startingY: " + startingY + " " + "repeats: " + repeats + " " + "repeatOffSetX: " + repeatOffsetX + "\n";
        result += "repeatOffSetY: " + repeatOffsetY + " " + "filled: " + filled + " " + "color: " + color + "\n";
        result += "rotate: " + rotate + " " + "repeatRotate: " + repeatRotate +"\n\n";
        return result;
    }
}
