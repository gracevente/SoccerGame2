package edu.augustana.csc490.soccergame;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gracevente11 on 3/31/2015.
 */
public class SoccerShootoutFragment extends Fragment {
    private SoccerShootoutView soccerShootoutView;

    // called when Fragment's view needs to be created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =
                inflater.inflate(R.layout.fragment_game, container, false);

        // get the SoccerShootoutView
        soccerShootoutView = (SoccerShootoutView) view.findViewById(R.id.soccerShootoutView);
        return view;
    }

    //when MainActivity is paused, soccerShootoutFragment terminates the game
    @Override
    public void onPause() {
        super.onPause();
        soccerShootoutView.stopGame();

    }

    // when MainActivity is paused, soccerShootoutFragment releases resources
    @Override
    public void onDestroy() {
        super.onDestroy();
        soccerShootoutView.releaseResources();

    }


}

