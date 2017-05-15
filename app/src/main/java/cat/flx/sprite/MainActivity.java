package cat.flx.sprite;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button pause;
    Game game;
    TextView puntuacion;
    public int punt;
    ImageView iv;
    public TextView tv;
    public Button reiniciar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_main);
        reiniciar = (Button) findViewById(R.id.reset);
        reiniciar.setVisibility(View.GONE);
        //reiniciar.setVisibility(View.GONE);
        final GameView gameView = (GameView) findViewById(R.id.view);
        game = new Game(this, gameView);
        gameView.setGame(game);
        puntuacion = (TextView) findViewById(R.id.puntuacion);
        tv = (TextView) findViewById(R.id.perdido);
        pause = (Button) findViewById(R.id.pausa);
        iv = (ImageView) findViewById(R.id.imageView);
        iv.setVisibility(View.GONE);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!game.isDie()) {
                    if (gameView.isPausa()) {
                        gameView.setPausa(false);
                        iv.setVisibility(View.GONE);
                    } else {
                        gameView.setPausa(true);
                        iv.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        reiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GameView gameView = (GameView) findViewById(R.id.view);

                game = new Game(MainActivity.this, gameView);
                gameView.setGame(game);
                reiniciar.setVisibility(View.GONE);
            }
        });

    }

    @Override public void onResume() {
        super.onResume();
        game.getAudio().startMusic();
    }

    @Override public void onPause() {
        game.getAudio().stopMusic();
        super.onPause();
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        boolean down = (event.getAction() == KeyEvent.ACTION_DOWN);
        switch(event.getKeyCode()) {
            case KeyEvent.KEYCODE_Z:
                game.keyLeft(down); break;
            case KeyEvent.KEYCODE_X:
                game.keyRight(down); break;
            case KeyEvent.KEYCODE_M:
                game.keyJump(down); break;
        }
        return true;
    }
}
