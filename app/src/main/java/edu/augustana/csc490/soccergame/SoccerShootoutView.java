package edu.augustana.csc490.soccergame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;
/**
 * Created by gracevente11 on 3/31/2015.
 */
public class SoccerShootoutView extends SurfaceView
implements SurfaceHolder.Callback
{
    private static final String TAG = "soccerTag";

    private SoccerShootoutThread soccerShootoutThread;
    private Activity mainActivity;
    private boolean gameOver = true;

    private ArrayList<SoccerBall> soccerBalls; // ArrayList to represent the soccer balls
    private Rect goal;
    private int soccerBallRadius;

    private int screenWidth;
    private int screenHeight;


    private Paint soccerBallPaint;
    private Paint goaliePaint;
    private Paint goalPaint;
    private Paint fieldPaint;

    //constructor
    public SoccerShootoutView (Context context, AttributeSet attrs){
        super(context, attrs);
        mainActivity = (Activity) context;

        getHolder().addCallback(this);

        soccerBalls = new ArrayList<SoccerBall>();

        soccerBallPaint = new Paint();
        soccerBallPaint.setColor(Color.BLACK);

        fieldPaint = new Paint();
        fieldPaint.setColor(Color.GREEN);

        goaliePaint = new Paint();
        goaliePaint.setColor(Color.WHITE);

        goalPaint = new Paint();
        goalPaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;



        //Create Soccer Balls
        Random random = new Random(); //random object to pick random points
        soccerBallRadius = screenWidth / 50 ;
        for(int i= 1; i<= 20; i++) {

            int startY = random.nextInt(screenHeight); //pick a random y coordinate for the ball to start at

            int destinationY = random.nextInt((screenHeight * 2 / 3) - (screenHeight / 3)+ 1) + screenHeight / 3; //random y coordinate in the goal for the ball to travel towards
            Point destination = new Point(screenWidth , destinationY);// create a point in the goal which the ball will travel towards

            int randomVelocity = (random.nextInt(200 - 75 + 1) + 75); //random velocity that the ball will travel at
            int vx = destination.x / randomVelocity;
            int vy = (destination.y - startY) / randomVelocity;

            //create the soccer ball and add it to the ArrayList
            SoccerBall ball = new SoccerBall(0, startY, vx, vy );
            soccerBalls.add(ball);
        }

        int left = screenWidth * 9 / 10;
        int top = screenHeight / 3;
        int right = screenWidth;
        int bottom = screenHeight * 2 / 3;

        goal = new Rect(left,top,right,bottom);
        startNewGame();
    }


    // stops the game; called by SoccerShootoutFragment's onPause method
    public void stopGame(){

    }

    //releases resources; called by SoccerShootoutFragment's onDestroy method
    public void releaseResources(){

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    public void startNewGame() {
        if(gameOver) {
            soccerShootoutThread = new SoccerShootoutThread(getHolder());
            soccerShootoutThread.start();
        }
    }
    public void moveSoccerBalls(){
        for (SoccerBall ball: soccerBalls){
            ball.moveBall();
        }
    }
    public void updateView (Canvas canvas){
        if (canvas != null) {
            canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), fieldPaint);
            canvas.drawRect(goal, goalPaint);

           for (SoccerBall ball: soccerBalls){
              canvas.drawCircle(ball.getX(), ball.getY(),soccerBallRadius, soccerBallPaint);

           }
        }
    }

    //Thread subclass
    private class SoccerShootoutThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public SoccerShootoutThread (SurfaceHolder holder) {
            surfaceHolder = holder;
            setName("SoccerShootoutThread");
        }

        public void setRunning(boolean running) {
            threadIsRunning = running;
        }
        @Override
        public void run()
        {
            Canvas canvas = null;

            while (threadIsRunning)
            {
                try
                {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null);

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        moveSoccerBalls();         // update game state
                        updateView(canvas); // draw using the canvas
                    }
                    Thread.sleep(10); // if you want to slow down the action...
                } catch (InterruptedException ex) {
                    Log.e(TAG,ex.toString());
                }
                finally  // regardless if any errors happen...
                {
                    // make sure we unlock canvas so other threads can use it
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
}
}
