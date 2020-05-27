package tk.divesdk.nutrifood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void reset(View view){
        EditText edit = (EditText) findViewById(R.id.editreset);
        email = edit.getText().toString();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Reset.this,"Recuperação de accesso, e-mail enviado",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Reset.this,"Tente novamente",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
