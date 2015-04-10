package edu.augustana.csc490.soccergame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intro_main);

        Button playButton = (Button) findViewById((R.id.playButton));
        playButton.setOnClickListener((playButtonClickHandler));

        Button rulesButton = (Button) findViewById((R.id.rulesButton));
        rulesButton.setOnClickListener((rulesButtonClickHandler));
    }


    View.OnClickListener playButtonClickHandler = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent playIntent = new Intent(MainActivity.this, pressPlayButton.class);
            startActivity(playIntent);
        }
    };

    View.OnClickListener rulesButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent rulesIntent = new Intent(MainActivity.this, pressRulesButton.class);
            startActivity(rulesIntent);
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
