package tk.divesdk.nutrifood;

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

public class Tela_termos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_termos);
    }


    public void OK(View view){

        CheckBox checkcontinuar = (CheckBox) findViewById(R.id.aceito1);
        CheckBox checkofertas = (CheckBox) findViewById(R.id.aceito2);

        if (checkcontinuar.isChecked() && checkofertas.isChecked()) {
            Intent it = new Intent(this, TelaPrincipal.class);
            startActivity(it);
            finish();

        }

        else if(checkcontinuar.isChecked()){
            Intent it = new Intent(this, TelaPrincipal.class);
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
