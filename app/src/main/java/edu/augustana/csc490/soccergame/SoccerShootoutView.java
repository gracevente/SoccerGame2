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
    private int numberSoccerBalls;
    private Rect goal;
    private int soccerBallRadius;

    private int screenWidth;
    private int screenHeight;

    private int goalLeft;
    private int goalTop;
    private int goalBottom;
    private int goalRight;

    private Paint soccerBallPaint;
    private Paint goaliePaint;
    private Paint goalPaint;
    private Paint fieldPaint;

    private int numberOfGoals;

    private int numGoals;

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

        numberSoccerBalls = 3;
        numberOfGoals = 0;

        goalLeft = screenWidth * 15 / 16;
        goalTop = screenHeight / 3;
        goalRight = screenWidth;
        goalBottom = screenHeight * 2 / 3;



        //Create Soccer Balls
        Random random = new Random(); //random object to pick random points
        soccerBallRadius = screenWidth / 50 ;
        for(int i= 1; i<= numberSoccerBalls; i++) {
            soccerBalls.add(newSoccerBall());
        }

        // create the goal
        goal = new Rect(goalLeft, goalTop, goalRight, goalBottom);
        startNewGame();
    }


    // stops the game; called by SoccerShootoutFragment's onPause method
    public void stopGame(){
        if(soccerShootoutThread != null)
            soccerShootoutThread.setRunning(false);
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

    //up date where the soccer balls are and check if they are any goals or balls that have gone off of the screen
    public void moveSoccerBalls(){
        //for (SoccerBall ball: soccerBalls){
          // ball.moveBall();
        //}

        for (int i = 0; i<soccerBalls.size(); i ++){
            soccerBalls.get(i).moveBall();

            //check if the ball is in the goal
            if (soccerBalls.get(i).getX() >= goalLeft && soccerBalls.get(i).getX() < goalLeft+1 && soccerBalls.get(i).getY() >= goalTop && soccerBalls.get(i).getY() <= goalBottom){
                numberOfGoals ++;
                Log.d("numGoals", "" + numberOfGoals);
            }
            //check if a ball goes off of the screen
            if(soccerBalls.get(i).getX() > screenWidth){
                soccerBalls.remove(i);
                soccerBalls.add(newSoccerBall());
            }
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

    public SoccerBall newSoccerBall(){
        Random random = new Random(); //random object to pick random points
        //pick a random y coordinate for the ball to start at
        int startY = random.nextInt(screenHeight);

        //random point in the goal for the ball to travel towards
        int destinationY = random.nextInt(goalBottom - goalTop + 1) + goalTop;
        Point destination = new Point(screenWidth , destinationY);

        //pick a random velocity for the ball to travel at
        int randomVelocity = (random.nextInt(200 - 75 + 1) + 75);
        int vx = destination.x / randomVelocity;
        int vy = (destination.y - startY) / randomVelocity;
        return new SoccerBall(0, startY, vx, vy);
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
