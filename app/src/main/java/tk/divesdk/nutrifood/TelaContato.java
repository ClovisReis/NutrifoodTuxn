package tk.divesdk.nutrifood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelaContato extends AppCompatActivity {
    private String assunto;
    private String mensagem;

    private class SendTask extends AsyncTask<JSONObject, Void, String> {
        ProgressDialog pd;

        @Override
        protected String doInBackground(JSONObject... params) {
            try {
                JSONObject data = params[0];

                final URL url = new URL("https://nutrifoodapi.herokuapp.com/contacts");
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-type", "application/json");

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                final OutputStream outputStream = connection.getOutputStream();
                final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                writer.write(data.toString());
                writer.flush();
                writer.close();
                outputStream.close();
                connection.connect();
                final InputStream stream = connection.getInputStream();
                return new Scanner(stream, "UTF-8").next();
            } catch (Exception e) {
                Log.e("Your tag", "Error", e);
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(TelaContato.this);
            pd.setTitle("NutriFood");
            pd.setMessage("Carregando...Por favor aguarde!");
            pd.show();
        }
        protected void onPostExecute(String result) {
            retornoTask(result);
        }
    }

    public void retornoTask(String presult) {
        if (presult == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha ao enviar a mensagem, tente novamente.", Toast.LENGTH_LONG);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Mensagem enviada com sucesso.",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void btn_send(View v) throws JSONException {
        if (isOnline()) {
            //DADOS DA TELA
            EditText Editsubject = (EditText) findViewById(R.id.input_subject);
            assunto = Editsubject.getText().toString();
            EditText EditMessage = (EditText) findViewById(R.id.input_menssage);
            mensagem = EditMessage.getText().toString();

            //VALIDAÇÃO
            if (assunto.isEmpty()) {
                Editsubject.setError(getString(R.string.input_subject));
                Editsubject.requestFocus();
                return;
            }
            if (mensagem.isEmpty()) {
                EditMessage.setError(getString(R.string.input_message));
                EditMessage.requestFocus();
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                String email = user.getEmail();
                String nome = user.getDisplayName();

                Contato NovoContato = new Contato(nome, email, assunto, mensagem);

                JSONObject json_result = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("nome", NovoContato.getNome());
                item.put("email", NovoContato.getEmail());
                item.put("subject", NovoContato.getAssunto());
                item.put("message", NovoContato.getMensagem());
                json_result.put("contact", item);

                new SendTask().execute(json_result);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha ao enviar a mensagem, tente novamente.", Toast.LENGTH_LONG);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para enviar a mensagem.", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
