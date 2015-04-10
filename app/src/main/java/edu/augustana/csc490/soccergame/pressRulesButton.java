package edu.augustana.csc490.soccergame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class pressRulesButton extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_press_rules_button);

        Button playButton = (Button) findViewById((R.id.play2Button));
        playButton.setOnClickListener((play2ButtonClickHandler));

    }
    View.OnClickListener play2ButtonClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent playIntent = new Intent(pressRulesButton.this, pressPlayButton.class);
            startActivity(playIntent);
            finish();
        }
    };

}
