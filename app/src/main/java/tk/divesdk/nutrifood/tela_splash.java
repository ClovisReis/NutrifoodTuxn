package tk.divesdk.nutrifood;

/**
 * Created by clovisgrj on 12/02/15.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import java.util.logging.Handler;

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
        startActivity(new Intent(this, TelaPrincipal.class));
        finish();
    }
}
