package tk.divesdk.nutrifood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Tela_termos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_termos);
    }

    public void OK(View view){
        CheckBox checkcontinuar = (CheckBox) findViewById(R.id.aceito1);
        CheckBox checkofertas = (CheckBox) findViewById(R.id.aceito2);

       if(checkcontinuar.isChecked()){
           DatabaseReference user_ref_uid = FirebaseDatabase.getInstance().
                   getReference().child("Users").
                   child(FirebaseAuth.getInstance().getCurrentUser().getUid());

           Map<String, Object> childUpdates = new HashMap<>();
           childUpdates.put("/aprovado/", checkcontinuar.isChecked());
           childUpdates.put("/ofertas/", checkofertas.isChecked());
           user_ref_uid.updateChildren(childUpdates);

            Intent it = new Intent(Tela_termos.this, TelaPrincipal.class);
            startActivity(it);
            finish();
        }
        else{
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_inflate_termos, (ViewGroup) findViewById(R.id.toast_root));
            TextView toastText = layout.findViewById(R.id.toast_text);
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }
}
