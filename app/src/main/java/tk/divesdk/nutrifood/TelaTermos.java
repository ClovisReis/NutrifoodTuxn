package tk.divesdk.nutrifood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class TelaTermos extends AppCompatActivity {

    //VALIDAÇÃO
    private boolean ErroSaveUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_termos);

        WebView webView = (WebView) findViewById(R.id.wv_content);
        webView.loadUrl("https://blogapptestblog.wordpress.com/politica-de-privacidade/");
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void OK(View view) {

        if (isOnline()) {

            CheckBox checkcontinuar = (CheckBox) findViewById(R.id.aceito1);
            CheckBox checkofertas = (CheckBox) findViewById(R.id.aceito2);

            ErroSaveUser = false;
            if (checkcontinuar.isChecked()) {
                DatabaseReference user_ref_uid = FirebaseDatabase.getInstance().
                        getReference().child("Users").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/aprovado/", checkcontinuar.isChecked());
                childUpdates.put("/ofertas/", checkofertas.isChecked());
                user_ref_uid.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            ErroSaveUser = true;
                        }
                    }
                });

                if (ErroSaveUser) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha, tente novamente.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Intent it = new Intent(TelaTermos.this, TelaPrincipal.class);
                    startActivity(it);
                    finish();
                }
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_inflate_termos, (ViewGroup) findViewById(R.id.toast_root));
                TextView toastText = layout.findViewById(R.id.toast_text);
                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para concluir o cadastro.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
