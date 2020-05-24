package tk.divesdk.nutrifood;

/**
 * Created by clovisgrj on 12/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

public class tela_splash extends Activity implements Runnable {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tela_splash);

        final ImageView animationImageView = (ImageView) findViewById(R.id.AnimationImageView);
        animationImageView.setBackgroundResource(R.drawable.android);

        final AnimationDrawable frameAnimation = (AnimationDrawable) animationImageView.getBackground();
        animationImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        android.os.Handler h = new android.os.Handler();
        h.postDelayed(this, 5040);
    }

    public void run() {
        startActivity(new Intent(tela_splash.this, TelaPrincipal.class));
        finish();
    }
}
