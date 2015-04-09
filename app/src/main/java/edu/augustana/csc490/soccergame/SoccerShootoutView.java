//Grace Vente
//Individual App
//All soccer ball images are from pdclipart.org

package edu.augustana.csc490.soccergame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
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

    private Bitmap soccerBallBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.soccer_ball_2);
    private Bitmap resizedSoccerBallBitMap;

    private boolean dialogIsDisplayed = false;

    private SoccerShootoutThread soccerShootoutThread;
    private Activity mainActivity;
    private boolean gameOver = true;

    private ArrayList<SoccerBall> soccerBalls; // ArrayList to represent the soccer balls
    private int numberOfSoccerBalls; //number of soccer balls on the screen at any given time
    private Rect goal;


    private int goalieLeft;
    private int goalieTop;
    private int goalieRight;
    private int goalieBottom;
    private Rect goalie;
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
    private Paint numberOfGoalsPaint;

    private int numberOfGoals;
    private int numberOfShots;

    private int numberOfSaves;



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

        numberOfGoalsPaint = new Paint();
        numberOfGoalsPaint.setColor(Color.BLACK);

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        goalLeft = screenWidth * 15 / 16;
        goalTop = screenHeight / 4;
        goalRight = screenWidth;
        goalBottom = screenHeight * 3 / 4;

        goal = new Rect(goalLeft, goalTop, goalRight, goalBottom);

        soccerBallRadius = (goalBottom - goalTop)/40; //diameter of the soccer ball is 1/20 of the goal
        numberOfGoalsPaint.setTextSize(goalRight - goalLeft);

        resizedSoccerBallBitMap = Bitmap.createScaledBitmap(soccerBallBitMap, soccerBallRadius * 2, soccerBallRadius * 2, false);
        startNewGame();
    }// end onSizeChanged method



    public void startNewGame() {
        //clear soccerBalls arraylist
        for(int i = soccerBalls.size() - 1; i >=0 ; i -- ){
            soccerBalls.remove(i);
        }

        goalieLeft = 2 * goalLeft - goalRight;
        goalieTop = (goalBottom- (goalBottom-goalTop)/2) - ((goalBottom - goalTop) / 10) ;
        goalieRight = goalLeft - (goalRight - goalLeft) / 3;
        goalieBottom = (goalBottom- (goalBottom-goalTop)/2) + ((goalBottom - goalTop) / 10);
        goalie = new Rect(goalieLeft, goalieTop, goalieRight, goalieBottom);// goalie is twice as big as the ball?

        numberOfSoccerBalls = 5;
        numberOfGoals = 0;
        numberOfShots = 0;

        numberOfSaves = 0;

        for(int i= 1; i<= numberOfSoccerBalls; i++) {
            soccerBalls.add(newSoccerBall());
        }

        if(gameOver) {
            soccerShootoutThread = new SoccerShootoutThread(getHolder());
            soccerShootoutThread.start();
            gameOver = false;
        }
    }// end startNewGame method


    //update where the soccer balls are and check if they are any goals or balls that have gone off of the screen
    public void updateGame(){

        for (int i = 0; i<soccerBalls.size(); i ++){
            soccerBalls.get(i).moveBall();

            //check if the ball is in the goal
            if (goal.contains( (int)soccerBalls.get(i).getX(), (int) soccerBalls.get(i).getX())){
                soccerBalls.remove(i);
                soccerBalls.add(newSoccerBall());
                numberOfGoals ++;
                numberOfShots ++;
                Log.w(TAG, "shots: " +numberOfShots);

                Log.w(TAG, "numGoals: " + numberOfGoals);
            }
            //check if a ball goes off of the screen
            Rect background = new Rect(0,0,screenWidth, screenHeight);
            if(! background.contains((int)soccerBalls.get(i).getX(), (int) soccerBalls.get(i).getX())){
                soccerBalls.remove(i);
                soccerBalls.add(newSoccerBall());
            }

            // check if a ball hits the goalie
            if (goalie.contains( (int)soccerBalls.get(i).getX(), (int) soccerBalls.get(i).getY()) && !soccerBalls.get(i).getVelocityHasBeenChanged()) {
                //change the velocity to opposite x direction
                soccerBalls.get(i).setVX(-1 * soccerBalls.get(i).getVX());
                soccerBalls.get(i).setVelocityHasBeenChangedTrue();
                numberOfSaves++;
                numberOfShots ++;
                Log.w(TAG, "shots: " +numberOfShots);

                Log.w(TAG, "blocks: " + numberOfSaves);
            }
        }
        if ( numberOfGoals >= 10){
            soccerShootoutThread.setRunning(false);
            gameOver = true;
            showGameOverDialog();
        }
    }// end update game method


    //re-draw the updated game
    public void updateView (Canvas canvas){
        if (canvas != null) {
            canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), fieldPaint);
            canvas.drawRect(goal, goalPaint);
            canvas.drawRect(goalie, goaliePaint);
            canvas.drawText("Goals: " + numberOfGoals + "  Saves: " + numberOfSaves, screenWidth /16, screenHeight / 16 , numberOfGoalsPaint);


           for (SoccerBall ball: soccerBalls){
              canvas.drawCircle(ball.getX(), ball.getY(),soccerBallRadius, soccerBallPaint);
              canvas.drawBitmap(resizedSoccerBallBitMap, ball.getX() - soccerBallRadius, ball.getY() - soccerBallRadius, null);
           }
        }
    }// end updateView method


    private void showGameOverDialog(){
        // DialogFragment to display quiz stats and start new quiz
        final DialogFragment gameResult =
                new DialogFragment()
                {
                    @Override
                    public Dialog onCreateDialog(Bundle bundle)
                    {

                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(getActivity());
                        builder.setTitle("Game Over");
                        builder.setMessage("Shots taken: " + numberOfShots + "\nShots saved: " + numberOfSaves + "\nPlay again?");
                        builder.setNegativeButton("No.", endGameClickListener);
                        builder.setPositiveButton("Yes.", playAgainClickListener);

                     return builder.create();
                    }
                };
        mainActivity.runOnUiThread(
                new Runnable() {
                    public void run()
                    {
                        dialogIsDisplayed = true;
                        gameResult.setCancelable(false); // modal dialog
                        gameResult.show(mainActivity.getFragmentManager(), "results");
                    }
                }
        );
    } // end showGameDialog method

    DialogInterface.OnClickListener endGameClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            mainActivity.finish();
        }
    };

    DialogInterface.OnClickListener playAgainClickListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {

            startNewGame();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // get int representing the type of action which caused this event
        int action = e.getAction();

        // the user user touched the screen or dragged along the screen
        if (action == MotionEvent.ACTION_DOWN ||
                action == MotionEvent.ACTION_MOVE) {

            calculateGoalie(e);

        }
        return true;
    }

    public void calculateGoalie(MotionEvent event) {
        // gets location of user touch
        Point touchPoint = new Point ((int) event.getX(), (int) event.getY());

        goalieLeft = touchPoint.x - ((goalRight - goalLeft) / 3);
        goalieTop = touchPoint.y - ((goalBottom - goalTop) / 10) ;
        goalieRight = touchPoint.x + ((goalRight - goalLeft) / 3);
        goalieBottom = touchPoint.y + ((goalBottom - goalTop) / 10);
        goalie.set(goalieLeft, goalieTop, goalieRight, goalieBottom);
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

    //Thread subclass
    private class SoccerShootoutThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean threadIsRunning = true;

        public SoccerShootoutThread (SurfaceHolder holder) {
            Log.w(TAG, "created Thread");
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
                        updateGame();         // update game state
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
            Log.w(TAG, "Thread finished");
        }
}
}
