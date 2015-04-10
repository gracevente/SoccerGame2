package edu.augustana.csc490.soccergame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class pressPlayButton extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    View.OnClickListener playButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent playIntent = new Intent(pressPlayButton.this, pressPlayButton.class);
            startActivity(playIntent);
        }
    };
}
