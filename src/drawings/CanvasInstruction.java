package drawings;

import java.awt.Color;
import java.util.Scanner;

/**
 * Code for the CanvasInstruction class, managing a single canvas instruction
 *
 * @author      Bill Barry
 * @version     2017-09-07
 */
public class CanvasInstruction {

    //**********************************************************************************************
    //          CONSTANTS
    //**********************************************************************************************
    /** the lower boundary for valid gradient direction */
    public static int MIN_GRADIENT_DIRECTION = 0;
    /** the upper boundary for valid gradient direction */
    public static int MAX_GRADIENT_DIRECTION = 3;

    /** the width of the canvas */
    private int width;
    /** the height of the canvas */
    private int height;
    /** the solid Color of the canvas */
    private Color colorSolid;
    /** the starting gradient Color of the canvas */
    private Color colorStart;
    /** the ending gradient Color of the canvas */
    private Color colorEnd;
    /** the direction  of the gradient fill, between MIN_GRADIENT_DIRECTION and MAX_GRADIENT_DIRECTION */
    private int gradDirection;
    /** whether the canvas will use a gradient fill; false for solid color fills */
    private boolean isGradient;

    //**********************************************************************************************
    //          CONSTRUCTORS
    //**********************************************************************************************
    
    /**
     * Constructor for the CanvasInstruction class
     *
     * @param   width           the desired width of the drawing canvas
     * @param   height          the desired height of the drawing canvas
     * @param   colorSolid      the desired background Color for the canvas; use null for gradient fills
     * @param   colorStart      for gradient fills, the color to be drawn at the gradient origin area; use null for solid fills
     * @param   colorEnd        for gradient fills, the color to be drawn at the gradient end area; use null for solid fills
     * @param   gradDirection   the gradient direction for the gradient fill, between MIN_GRADIENT_DIRECTION and MAX_GRADIENT_DIRECTION 
     */
    private CanvasInstruction(int width, int height, Color colorSolid, 
                              Color colorStart, Color colorEnd, int gradDirection) {
        this.width = width;
        this.height = height;
        this.colorSolid = colorSolid;
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.gradDirection = gradDirection;
        isGradient = false;

        // If top or bottom is null, set a solid color instead
        if (this.colorStart == null && this.colorEnd != null) {
            this.colorSolid = this.colorEnd;
            this.colorStart = this.colorEnd = null;
        } else if (this.colorEnd == null && this.colorStart != null) {
            this.colorSolid = this.colorStart;
            this.colorStart = this.colorEnd = null;
        } 
        
        // With non-null top and bottom, it appears they want a gradient
        if (this.colorStart != null && this.colorEnd != null) {
            isGradient = true;
        } 
        
        // If top and bottom are the same color, set a solid color instead
        if (isGradient && this.colorStart.equals(colorEnd)) {
            this.colorSolid = this.colorStart;
            this.colorStart = this.colorEnd = null;
            isGradient = false;
        }
        
        // If no gradient, and no solid, set a white solid fill
        if (!isGradient && this.colorSolid == null) {
            this.colorSolid = new Color(255, 255, 255);
        }
        validateOrDefault(this);
    }

    //**********************************************************************************************
    //          INSTANCE METHODS
    //**********************************************************************************************
    /**
     * Private, empty constructor for CanvasInstruction class, useful for reading instructions from files, for example.  Sets all values to initial defaults as indicated in documentation
     */
    private CanvasInstruction() {
        this(100, 100, new Color(255, 255, 255), null, null, 0);
    }

    /**
     * Retrieves the canvas width
     *
     * @return      the canvas width
     */
    public int getWidth() { 
        return width; 
    }

    /**
     * Retrieves the canvas height
     *
     * @return      the canvas height
     */
    public int getHeight() {
        return height; 
    }

    
    /**
     * Retrieves the gradient direction for the canvas
     * 
     * @return      the gradient direction
     */
    public int getGradientDirection() {
        return gradDirection;
    }
    
    /**
     * Retrieves the background Color of the canvas
     *
     * @return      the Canvas's background color
     */
    public Color getColorSolid() {
        return colorSolid; 
    }
    
    /**
     * Retrieves the gradient Color used at the top of the canvas
     *
     * @return      the Canvas's top gradient color
     */
    public Color getColorStart() {
        return colorStart;
    }

    /**
     * Retrieves the gradient Color used at the bottom of the canvas
     *
     * @return      the Canvas's bottom gradient color
     */
    public Color getColorEnd() {
        return colorEnd;
    }
    
    /**
     * Retrieves whether the canvas will be drawn with a gradient color scheme
     * 
     * @return      true, if a gradient will be used; false, if not
     */
    public boolean getIsGradient() {
        return isGradient;
    }
    
    //**********************************************************************************************
    //          STATIC METHODS
    //**********************************************************************************************
    
    /**
     * Reads a canvas instruction from the supplied Scanner, and returns a corresponding CanvasInstruction object
     *
     * @param   fileIn  the Scanner object to use for retrieving instruction information
     * @return          an CanvasInstruction object containing the read instructions (plus defaults)
     */
    public static CanvasInstruction readFromFile(Scanner fileIn) {
        CanvasInstruction instruction = new CanvasInstruction();

        // Grab next instruction and split it up
        String instructLine = fileIn.nextLine();
        String[] instructFields = instructLine.split(",");
        int      red = 255,      green = 255, blue = 255;
        int startRed = 255, startGreen = 255, startBlue = 255;
        int   endRed = 255,   endGreen = 255, endBlue = 255;
        int gradDir = 0;
        boolean attemptGradient = false;

        // Separate out the individual instructions and interpret them
        for (String field : instructFields) {
            String[] pieces = field.split("=");
            String cmd   = pieces[0] = pieces[0].trim().toLowerCase();
            String value = pieces[1] = pieces[1].trim().toLowerCase();

            switch (cmd) {
                case "width"            : instruction.width = Integer.parseInt(value);                    break;
                case "height"           : instruction.height = Integer.parseInt(value);                   break;
                case "red"              : red   = Integer.parseInt(value);                                break;
                case "green"            : green = Integer.parseInt(value);                                break;
                case "blue"             : blue  = Integer.parseInt(value);                                break;
                case "graddir"          : gradDir = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradstartred"     : startRed = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradstartgreen"   : startGreen = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradstartblue"    : startBlue = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradendred"       : endRed = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradendgreen"     : endGreen = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                case "gradendblue"      : endBlue = Integer.parseInt(value);
                                          attemptGradient = true;                                         break;
                default                 : /* do nothing  */                                               break;
            }
        }

        // Validate RGB value ranges and create Color objects
        red   = Utility.rgbRangeLimit(red);
        green = Utility.rgbRangeLimit(green);
        blue  = Utility.rgbRangeLimit(blue);
        instruction.colorSolid = new Color(red, green, blue);
        if (attemptGradient) {
            startRed   = Utility.rgbRangeLimit(startRed);
            startGreen = Utility.rgbRangeLimit(startGreen);
            startBlue  = Utility.rgbRangeLimit(startBlue);
            if (gradDir < MIN_GRADIENT_DIRECTION || gradDir > MAX_GRADIENT_DIRECTION) {
                gradDir = 0;
            }
            //gradDir = Math.min(Math.max(gradDir, MIN_GRADIENT_DIRECTION), MAX_GRADIENT_DIRECTION);                                   
            
            instruction.colorStart = new Color(startRed, startGreen, startBlue);
            endRed   = Utility.rgbRangeLimit(endRed);
            endGreen = Utility.rgbRangeLimit(endGreen);
            endBlue  = Utility.rgbRangeLimit(endBlue);
            instruction.colorEnd = new Color(endRed, endGreen, endBlue);
            instruction.gradDirection = gradDir;
            instruction.isGradient = true;
        } else {
            instruction.colorStart = null;
            instruction.colorEnd = null;
            instruction.isGradient = false;
        }
        validateOrDefault(instruction);
        return instruction;
    }

    /**
     * Validates the instruction information in an existing Instruction, setting any invalid data to defaults
     *
     * @param   instruction     an existing Instruction to validate
     * @throws                  IllegalArgumentException if the Instruction reference is null
     */
    private static void validateOrDefault(CanvasInstruction instruction) {
        if (instruction == null) {
            throw new IllegalArgumentException("Instruction references must not be null");
        }
        
        // Width must be >= 1
        instruction.width  = Math.max(1, instruction.width);

        // Height  must be >= 1
        instruction.height = Math.max(1, instruction.height);
    }
    
    /**
     * Retrieves state of this object
     * 
     * @return                          the state of this object
     */
    public String toString() {
        String result = "Canvas:\n";
        result += "Width: " + width + " " + "Height: " + height + "\n" + "colorSolid: " + colorSolid.toString() + " " + "colorStart: " + colorStart + "\n";
        result += "colorEnd: " + colorEnd + " " + "gradDirection: " + gradDirection + "\n" + "isGradient: " + isGradient + "\n";
        return result;
    }
}
