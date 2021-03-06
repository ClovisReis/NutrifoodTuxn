package tk.divesdk.nutrifood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class TelaListaAlimentos extends AppCompatActivity  {
    public String Aba;
    public String Item;

    public ArrayList<String> lListIds = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_lista_alimentos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle =  getIntent().getExtras();
        if(bundle != null){
            Aba = bundle.getString("ABA_SELECIONADA");
            Item = bundle.getString("ITEM_SELECIONADO");
        }
        CarregaList();

        ListView t = (ListView) findViewById(R.id.lv);
        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent it = new Intent(TelaListaAlimentos.this, TelaAlimento.class);
                String lId = (String )lListIds.get(position);
                it.putExtra("ID_SELECIONADO", lId);
                startActivity(it);
            }
        });
    }

    private void CarregaList(){
       new TelaListaAlimentos.ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(TelaListaAlimentos.this);
            pd.setTitle("NutriFood");
            pd.setMessage("Carregando...Por favor aguarde!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String retornoAba = TelaListaAlimentos.this.Aba;
                String retornopagina = TelaListaAlimentos.this.Item;

                String $url_json = "https://nutrifoodapi.herokuapp.com/alimentos/"+ URLEncoder.encode (retornoAba, "UTF-8") + "/" + URLEncoder.encode (retornopagina, "UTF-8");
                URL url = new URL($url_json);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                Log.d("FOR_LOG", resultJson);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        protected void onPostExecute(String strJson) {
            pd.dismiss();
            super.onPostExecute(strJson);
            final ListView lView = (ListView) findViewById(R.id.lv);
            String[] from = {"name_item"};
            int[] to = {R.id.name_item};
            ArrayList<Alimento> arrayList = new ArrayList<Alimento>();
            HashMap<String, String> hashmap;

            try {
                JSONObject json = new JSONObject(strJson);
                JSONArray jArray = json.getJSONArray("data");

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject friend = jArray.getJSONObject(i);

                    String id = friend.getString("id");

                    lListIds.add(id);

                    String nameOS = friend.getString("nome");
                    String imageOS = friend.getString("imagem");

                    Alimento lAli =  new Alimento(nameOS,imageOS);

                    hashmap = new HashMap<String, String>();
                    hashmap.put("name_item", "" + nameOS);
                    arrayList.add(lAli);
                }

                ArrayAdapter adapter = new AlimentoAdapter(TelaListaAlimentos.this, arrayList);
                lView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Id correspondente ao bot�o Up/Home da actionbar
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){ //Botão BACK padrão do android
         //O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }

}
