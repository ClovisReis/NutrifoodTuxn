package tk.divesdk.nutrifood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelaInternet extends Activity {
    private FirebaseAuth firebaseAuth;
    private boolean login;
    private boolean termo;
    public boolean conectado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(!conectado) {
                        sleep(2000);
                        conectado = isOnline();
                        Log.v("RESULTADO", conectado ? "true" : "false");
                        if(isOnline()){
                            validalogin();
                            Intent it = new Intent(getApplicationContext(), TelaLogin.class);
                            startActivity(it);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public void validalogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        login = verificarUsuarioLogado(firebaseAuth = FirebaseAuth.getInstance()) ?
                true : false;
        if(login){
            ValueEventListener DatabaseListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String laprovado = dataSnapshot.getValue() != null ? dataSnapshot.getValue(Boolean.class).toString() : dataSnapshot.getValue(String.class);
                    termo = laprovado == null || laprovado.equals("false") ? false : true ;
                    if(!termo){
                        startActivity(new Intent(TelaInternet.this, TelaTermos.class));
                        finish();
                    }
                    else{
                        startActivity(new Intent(TelaInternet.this, TelaPrincipal.class));
                        finish();
                    }
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

    public boolean verificarUsuarioLogado(FirebaseAuth firebaseAuth) {
        return firebaseAuth.getCurrentUser() != null ?  true :  false;
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}