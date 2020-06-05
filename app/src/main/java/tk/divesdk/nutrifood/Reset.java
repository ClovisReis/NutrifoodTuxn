package tk.divesdk.nutrifood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Reset extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void reset(View view) {
        if (isOnline()) {
            EditText edit = (EditText) findViewById(R.id.editreset);
            email = edit.getText().toString();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Reset.this, "Recuperação de accesso, e-mail enviado", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Reset.this, "Tente novamente", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para recuperar a senha.", Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
