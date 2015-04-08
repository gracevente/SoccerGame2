package edu.augustana.csc490.soccergame;

/**
 * Created by gracevente11 on 3/31/2015.
 */
public class SoccerBall {
    private float x; // x position
    private float y; // y position
    private float vx; // X velocity
    private float vy; // Y velocity
    //boolean to make sure that the goalie doesn't "catch" the ball
    private boolean velocityHasBeenChanged;

    //contructor
    public SoccerBall(float x, float y, float vx, float vy){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        velocityHasBeenChanged = false;
    }

    public void moveBall(){
        x = x + vx;
        y = y + vy;

    }

    public float getY() {
        return y;
    }

    public float getX() { return x; }

    public float getVX() { return vx; }

    public boolean getVelocityHasBeenChanged(){ return velocityHasBeenChanged;}

    public void setVelocityHasBeenChangedTrue() { velocityHasBeenChanged = true;}

    public void setVX(float newVX) { vx = newVX; }
}
