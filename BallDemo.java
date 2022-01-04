import java.awt.*;
import java.awt.geom.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * Class BallDemo - a short demonstration showing animation with the 
 * Canvas class. 
 *
 * @author Michael Kölling and David J. Barnes, modified by Jay Ganji
 * @version 2021.02.22
 */

public class BallDemo   
{
    private Canvas myCanvas;

    /**
     * Create a BallDemo object. Creates a fresh canvas and makes it visible.
     */
    public BallDemo()
    {
        myCanvas = new Canvas("Ball Demo", 600, 500);
    }

    /**
     * Simulate two bouncing balls
     */
    public void bounce()
    {
        int ground = 400;   // position of the ground line

        myCanvas.setVisible(true);

        // draw the ground
        myCanvas.setForegroundColor(Color.BLACK);
        myCanvas.drawLine(50, ground, 550, ground);

        // create and show the balls
        BouncingBall ball = new BouncingBall(50, 50, 16, Color.BLUE, ground, myCanvas);
        ball.draw();
        BouncingBall ball2 = new BouncingBall(70, 80, 20, Color.RED, ground, myCanvas);
        ball2.draw();

        // make them bounce
        boolean finished =  false;
        while (!finished) {
            myCanvas.wait(50);           // small delay
            ball.move();
            ball2.move();
            // stop once ball has travelled a certain distance on x axis
            if(ball.getXPosition() >= 550 || ball2.getXPosition() >= 550) {
                finished = true;
            }
        }
    }
    
    /**
     * Draw a rectangle on the screen and some balls,
     * at random starting position and speed.
     * @param num The number of balls to create.
     */
    public void boxBounce(int num){
        myCanvas.setSize(1400,900); // make the window a bit larger
        myCanvas.setVisible(true);
        
        ArrayList<BoxBall> balls = new ArrayList<>();
        
        // draw the box
        myCanvas.setForegroundColor(Color.BLACK);
        myCanvas.drawLine(50, 400, 550, 400);
        myCanvas.drawLine(50, 700, 550, 700);
        myCanvas.drawLine(50, 700, 50, 400);
        myCanvas.drawLine(550, 700, 550, 400);
        
        //Create a random number generator to generate the properties of the random balls
        Random rnGen = new Random();
        
        // create and show the balls
        for (int i = 0; i<num; i++){
            balls.add(new BoxBall(rnGen.nextInt(500)+50,rnGen.nextInt(300)+400,rnGen.nextInt(15)+10,rnGen.nextInt(8)+1,rnGen.nextInt(8)+1,rnGen.nextInt(10),myCanvas));
        }
        for (BoxBall ball : balls){
            ball.draw();
        }
        
         // make them bounce
        boolean finished =  false;
        int moveCounter = 0;
        while (!finished) {
            myCanvas.wait(50);           // small delay
            for (BoxBall ball : balls){
                ball.move();
            }
            moveCounter++;
            // stop once ball has travelled a certain distance
            if(moveCounter > 400) {
                finished = true;
            }
        }
    }
    
    /**
     * Class BoxBall - a graphical ball that observes the effect of gravity. The ball
     * has the ability to move. Details of movement are determined by the ball itself. It
     * will fall downwards, accelerating with time due to the effect of gravity, and bounce
     * off when hitting the constraint of the box.
     *
     * This movement can be initiated by repeated calls to the "move" method.
     * 
     * @author Michael Kölling (mik)
     * @author David J. Barnes
     * @author Bruce Quig
     * @author Jay Ganji
     *
     * @version 2021.02.22
     */
    
    private class BoxBall
    {
        private int ballDegradation = 2;
        private Ellipse2D.Double circle;
        private Color color;
        private int diameter;
        private int xPosition;
        private int yPosition;
        private final int bottomBox=700;      // y position of floor
        private final int topBox=400;        // y position of ceiling
        private final int rightBox=550;        // x position of right wall
        private final int leftBox=50;        // x position of left wall
        private Canvas canvas;
        private int ySpeed;                // initial downward speed
        private int xSpeed;                // initial horizontal speed
    
        /**
         * Constructor for objects of class BoxBall
         *
         * @param xPos  the horizontal coordinate of the ball
         * @param yPos  the vertical coordinate of the ball
         * @param ballDiameter  the diameter (in pixels) of the ball
         * @param xSpd the horizontal speed of the ball
         * @param ySpd the vertical speed of the ball
         * @param ballColor the int value to map to a Color
         * @param drawingCanvas  the canvas to draw this ball on
         */
        public BoxBall(int xPos, int yPos, int ballDiameter, int xSpd, int ySpd, int ballColor, Canvas drawingCanvas)
        {
            xPosition = xPos;
            yPosition = yPos;
            xSpeed = xSpd;
            ySpeed = ySpd;
            if (ballColor == 0)
                color = Color.black;
            if (ballColor == 1)
                color = Color.blue;
            if (ballColor == 2)
                color = Color.cyan;
            if (ballColor == 3)
                color = Color.gray;
            if (ballColor == 4)
                color = Color.green;
            if (ballColor == 5)
                color = Color.magenta;
            if (ballColor == 6)
                color = Color.orange;
            if (ballColor == 7)
                color = Color.pink;
            if (ballColor == 8)
                color = Color.red;  
            if (ballColor == 9)
                color = Color.yellow; 
            diameter = ballDiameter;
            canvas = drawingCanvas;
        }
    
        /**
         * Draw this ball at its current position onto the canvas.
         **/
        public void draw()
        {
            canvas.setForegroundColor(color);
            canvas.fillCircle(xPosition, yPosition, diameter);
        }
    
        /**
         * Erase this ball at its current position.
         **/
        public void erase()
        {
            canvas.eraseCircle(xPosition, yPosition, diameter);
        }    
    
        /**
         * Move this ball according to its position and speed and redraw.
         **/
        public void move()
        {
            // remove from canvas at the current position
            erase();
                
            // compute new position
            yPosition += ySpeed;
            xPosition += xSpeed;
    
            // check if it has hit the bottom of box
            if (yPosition >= (bottomBox - diameter) && ySpeed > 0) {
                yPosition = (int)(bottomBox - diameter);
                ySpeed = -ySpeed + ballDegradation; 
            }
            // check if it has hit the top of box
            if (yPosition <= (topBox + diameter)) {
                yPosition = (int)(topBox + diameter);
                ySpeed = -ySpeed; 
            }
            // check if it has hit the left or right of of box
            if (xSpeed>0){
                if (xPosition >= (rightBox - diameter) && xSpeed > 0) {
                    xPosition = (int)(rightBox - diameter);
                    xSpeed = -xSpeed; 
                }
            }
            else if (xSpeed<0){
                if (xPosition <= (leftBox + diameter) && xSpeed < 0) {
                xPosition = (int)(leftBox + diameter);
                xSpeed = -xSpeed; 
                }
            }            
    
            // draw again at new position
            draw();
        }    
    
        /**
         * return the horizontal position of this ball
         */
        public int getXPosition()
        {
            return xPosition;
        }
    
        /**
         * return the vertical position of this ball
         */
        public int getYPosition()
        {
            return yPosition;
        }
    }
}
