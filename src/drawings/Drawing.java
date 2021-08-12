package drawings;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.awt.Graphics;
import java.awt.Color;

/**
 * Represents a drawing rendered from a drawing file
 * 
 * @author             Viktoryia Simakova
 * @version            2020-07-12
 */
public class Drawing {
    //**********************************************************************************************
    //          CONSTANTS
    //**********************************************************************************************
    /** Minimum value for x or y which means a random number should be generated */
    public static final int MIN_VALUE = -2147483648;  
    
    //**********************************************************************************************
    //          INSTANCE DATA
    //**********************************************************************************************
    /** reference to collection of shapes maintained by the library */
    private ShapeLibrary shapeLibrary;
    /** reference to canvas instructions */
    private CanvasInstruction canvasInstruction;
    /** collection of draw instructions */
    private ArrayList<DrawInstruction> drawInstructions;
    
    //**********************************************************************************************
    //          CONSTRUCTORS
    //**********************************************************************************************
    
    /**
     * Constructor; sets up a drawing so it may be drawn later upon request
     * 
     * @param       shapeLib        the shape library from which to draw shapes
     * @param       fileToDraw      the instruction file that will be read and interpreted to render drawings
     */
    public Drawing(ShapeLibrary shapeLib, File fileToDraw) throws FileNotFoundException{
        this.shapeLibrary = shapeLib;
        Scanner sc = new Scanner(fileToDraw);
        this.canvasInstruction = CanvasInstruction.readFromFile(sc);
        drawInstructions = new ArrayList<DrawInstruction> ();
        while(sc.hasNext()){
            drawInstructions.add(DrawInstruction.readFromFile(sc));
        }
    }

    /**
     * Renders the current drawing
     */
    public void draw() {
        DrawingPanel dp = new DrawingPanel(canvasInstruction.getWidth(), canvasInstruction.getHeight());
        Graphics g = dp.getGraphics();
        if (canvasInstruction.getIsGradient()){
            gradient ( g, dp );
        } else {
            dp.setBackground(canvasInstruction.getColorSolid());
        }
        for(int i = 0; i < drawInstructions.size(); i++){//foreach
            Shape shape = shapeLibrary.getShapeByName(drawInstructions.get(i).getShapeName());
            int [] xs = new int [shape.getPointCount()];
            int [] ys = new int [shape.getPointCount()];
            double scalePercent = drawInstructions.get(i).getScalePercent()/100.0;
            for(int j = 0; j < shape.getPointCount(); j++){
                xs[j] = (int)(shape.getPoint(j).getX() * scalePercent);
                ys[j] = (int)(shape.getPoint(j).getY() * scalePercent);
            }
            g.setColor(drawInstructions.get(i).getColor());
            int startX = drawInstructions.get(i).getStartingX();
            int startY = drawInstructions.get(i).getStartingY();
            if (startX != MIN_VALUE || startY != MIN_VALUE) {
                for(int k=0; k < xs.length; k++){
                    xs[k] += startX;
                    ys[k] += startY;
                }
            }
            drawFigure(g, xs, ys, i);  
            int numRepeat = drawInstructions.get(i).getRepeats();
            if (numRepeat > 1) {
                for(int d = 0; d < numRepeat-1; d++){
                    if (drawInstructions.get(i).getRepeatRotate() > 0){
                        rotate(startX,startY,xs,ys,g,i);
                    }
                    repeat(g, startX, startY, xs, ys, i);
                }
            }
            if (drawInstructions.get(i).getRotate() > 1) {
                rotate(startX,startY,xs,ys,g,i);
            }
        }
    }
    
    /**
     * Rotates figure around the origin
     *
     * @param   startX      the starting x coordinate at which the shape will be drawn
     * @param   startY      the starting y coordinate at which the shape will be drawn
     * @param   xs          the array of x-coordinates
     * @param   ys          the array of y-coordinates
     * @param   g           the Graphics object renefence
     * @param   i           the index of figure in draw instruction list
     */
    public void rotate(int startX, int startY,int [] xs,int [] ys, Graphics g,int i){
        double angle;
        if(drawInstructions.get(i).getRotate() > 1){
            angle = drawInstructions.get(i).getRotate();
        }else{
            angle = drawInstructions.get(i).getRepeatRotate();
        }
        angle = (angle ) * (Math.PI/180);
        int centerX = startX + drawInstructions.get(i).getScalePercent() / 2;
        int centerY = startY + drawInstructions.get(i).getScalePercent() / 2;
        for(int k=0; k < xs.length; k++){
            int newX = (int)(Math.cos(angle)*(xs[k]-centerX)-Math.sin(angle)*(ys[k]-centerY)+centerX);
            int newY = (int)(Math.sin(angle)*(xs[k]-centerX)+Math.cos(angle)*(ys[k]-centerY)+centerY);
            xs[k] = newX;
            ys[k] = newY;
        }
        drawFigure(g, xs, ys, i);
    }
    
    /**
     * Draws figure based on filled or not figure type
     *
     * @param   xs          the array of x-coordinates
     * @param   ys          the array of y-coordinates
     * @param   g           the Graphics object renefence
     * @param   i           the index of figure in draw instruction list
     */
    public void drawFigure(Graphics g, int [] xs, int [] ys, int i){
        if (drawInstructions.get(i).getFilled()) {
            g.fillPolygon(xs,ys,xs.length);
        } else {
            g.drawPolygon(xs,ys,xs.length);
        }
    }
    
    /**
     * Repeats figure on canvas with either certain offsets or random ones
     *
     * @param   startX      the starting x coordinate at which the shape will be drawn
     * @param   startY      the starting y coordinate at which the shape will be drawn
     * @param   xs          the array of x-coordinates
     * @param   ys          the array of y-coordinates
     * @param   g           the Graphics object renefence
     * @param   i           the index of figure in draw instruction list
     */
    public void repeat(Graphics g, int startX, int startY,int [] xs, int [] ys, int i ){
        int repeatOffSetX = drawInstructions.get(i).getRepeatOffsetX();
        int repeatOffSetY = drawInstructions.get(i).getRepeatOffsetY();
        if (startX == MIN_VALUE) {
            repeatOffSetX = (int)(Math.random() * canvasInstruction.getWidth());
        }
        if (startY == MIN_VALUE) {
            repeatOffSetY = (int)(Math.random() * canvasInstruction.getHeight());
        }
        for(int k=0; k < xs.length; k++){
            xs[k] += repeatOffSetX;
            ys[k] += repeatOffSetY;
        }
        drawFigure(g, xs, ys, i);
        if (startX == MIN_VALUE || startY == MIN_VALUE) {
            for(int k=0; k < xs.length; k++){
                xs[k] -= repeatOffSetX;
                ys[k] -= repeatOffSetY;
            }
        }
    }
    
    /**
     * Filles canvas with gradient using direction
     *
     * @param   g           the Graphics object renefence
     * @param   dp          the DrawingPanel object renefence
     */
    public void gradient (Graphics g, DrawingPanel dp ) {
        Color colorStart = canvasInstruction.getColorStart();
        Color colorEnd = canvasInstruction.getColorEnd();
        int steps = 100;
        for (int i = 0; i < steps; i++) {
            double ratio = (double) i / (double) steps;
            int red = (int) (colorEnd.getRed() * ratio + colorStart.getRed() * (1 - ratio));
            int green = (int) (colorEnd.getGreen() * ratio + colorStart.getGreen() * (1 - ratio));
            int blue = (int) (colorEnd.getBlue() * ratio + colorStart.getBlue() * (1 - ratio));
            Color stepColor = new Color (red, green, blue);
            g.setColor(stepColor);
            /* direction */
            switch (canvasInstruction.getGradientDirection()) {
                case 0  : 
                g.fillRect(0,canvasInstruction.getHeight()*(i-1)/100,canvasInstruction.getWidth(),canvasInstruction.getHeight());
                break;
                case 1  : 
                g.fillRect(canvasInstruction.getWidth()*(i-1)/100,0,canvasInstruction.getWidth(),canvasInstruction.getHeight());
                break;
                case 2  :
                steps = 150;
                double squaredSum = Math.pow(canvasInstruction.getWidth(),2) + Math.pow(canvasInstruction.getHeight(),2);
                double hypotenuse = Math.sqrt(squaredSum);
                int [] xs = {0, (int)hypotenuse, (int)hypotenuse, 0};
                int height = canvasInstruction.getHeight();
                int [] ys = { height*(i-40)/100,  height*(i-40)/100,  height*2,  height*2};
                double angle = -15;
                angle = (angle ) * (Math.PI/180);
                int centerX = 0 + (int)hypotenuse / 2;
                int centerY =  height*(i-40)/100 +  height / 2;
                for(int k=0; k < xs.length; k++){
                    int newX = (int)(Math.cos(angle)*(xs[k]-centerX)-Math.sin(angle)*(ys[k]-centerY)+centerX);
                    int newY = (int)(Math.sin(angle)*(xs[k]-centerX)+Math.cos(angle)*(ys[k]-centerY)+centerY);
                    xs[k] = newX;
                    ys[k] = newY;
                }
                g.fillPolygon(xs, ys,xs.length);
                break;
                case 3  : 
                steps = 110;
                squaredSum = Math.pow(canvasInstruction.getWidth(),2) + Math.pow(canvasInstruction.getHeight(),2);
                hypotenuse = Math.sqrt(squaredSum);
                int [] xs1 = {0, (int)hypotenuse, (int)hypotenuse, 0};
                int [] ys1 = {canvasInstruction.getHeight()*(i-5)/100, canvasInstruction.getHeight()*(i-5)/100, canvasInstruction.getHeight()*2, canvasInstruction.getHeight()*2};
                angle = 110;
                angle = (angle ) * (Math.PI/180);
                centerX = 0 + (int)hypotenuse / 2;
                centerY = canvasInstruction.getHeight()*(i-5)/100 + canvasInstruction.getHeight() / 2;
                for(int k=0; k < xs1.length; k++){
                    int newX = (int)(Math.cos(angle)*(xs1[k]-centerX)-Math.sin(angle)*(ys1[k]-centerY)+centerX);
                    int newY = (int)(Math.sin(angle)*(xs1[k]-centerX)+Math.cos(angle)*(ys1[k]-centerY)+centerY);
                    xs1[k] = newX;
                    ys1[k] = newY;
                }
                g.fillPolygon(xs1, ys1,xs1.length);
                break;
                default : /* do nothing */
            }
        }
        dp.setBackground(canvasInstruction.getColorSolid());
    } 
    
    /**
     * Retrieves state of this object
     * 
     * @return                          the state of this object
     */
    public String toString () {
        String result = canvasInstruction.toString() + "\n";
        for (int i = 0; i < drawInstructions.size(); i++) {
            result += drawInstructions.get(i).toString();
        }
        return result;
    }
}
