package tk.divesdk.nutrifood;

/**
 * Created by clovisgrj on 12/02/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelaSplash extends Activity {
    private boolean connectado;
    private boolean login;
    private boolean termo;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tela_splash);

        final ImageView animationImageView = (ImageView) findViewById(R.id.AnimationImageView);
        animationImageView.setBackgroundResource(R.drawable.android);

        final AnimationDrawable frameAnimation = (AnimationDrawable) animationImageView.getBackground();
        animationImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        connectado = isOnline();
        if(!connectado){
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet, para utilizar o aplicativo.", Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            firebaseAuth = FirebaseAuth.getInstance();
            login = verificarUsuarioLogado(firebaseAuth = FirebaseAuth.getInstance()) ?
                    true : false;
            if(login){
                ValueEventListener DatabaseListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String laprovado = dataSnapshot.getValue() != null ? dataSnapshot.getValue(Boolean.class).toString() : dataSnapshot.getValue(String.class);
                        termo = laprovado == null || laprovado.equals("false") ? false : true ;
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha, tente novamente.", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    }
                };
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("aprovado").addValueEventListener(DatabaseListener);
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!connectado){
                    startActivity(new Intent(TelaSplash.this, TelaInternet.class));
                    finish();
                }
                else{
                    if(!login){
                        startActivity(new Intent(TelaSplash.this, TelaLogin.class));
                        finish();
                    }
                    else{
                        if(!termo){
                            startActivity(new Intent(TelaSplash.this, TelaTermos.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(TelaSplash.this, TelaPrincipal.class));
                            finish();
                        }
                    }
                }
            }
        }, 5040);
    }

    public boolean verificarUsuarioLogado(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser() != null ?  true :  false;
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
