package tk.divesdk.nutrifood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ShowAlimento extends AppCompatActivity {
    public String ID;
    public Alimento lAl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_alimento);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle bundle =  getIntent().getExtras();
        if(bundle != null){
            ID = bundle.getString("ID_SELECIONADO");
        }

        CarregaElemento();
    }

    private void CarregaElemento(){
        new ShowAlimento.ParseTask().execute();
    }

    private class ParseTask extends AsyncTask<Void, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ShowAlimento.this);
            pd.setTitle("NutriFood");
            pd.setMessage("Carregando...Por favor aguarde!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String lid = ShowAlimento.this.ID;
                String $url_json = "https://nutrifoodapi.herokuapp.com/alimentos/"+ lid;
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
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> hashmap;

            try {
                    JSONObject json = new JSONObject(strJson);

                    JSONObject friend = json.getJSONObject("data");

                    Alimento lAlimento = new Alimento();
                    lAlimento.setNome(friend.getString("nome"));
                    lAlimento.setNome_cientifico(friend.getString("nome_cientifico"));
                    lAlimento.setNome_popular(friend.getString("nome_popular"));
                    lAlimento.setOrigem(friend.getString("origem"));
                    lAlimento.setRegiao(friend.getString("regiao"));
                    lAlimento.setCategoria(friend.getString("categoria"));
                    lAlimento.setCaracteristicas(friend.getString("caracteristicas"));
                    lAlimento.setCulinaria(friend.getString("culinaria"));
                    lAlimento.setCuriosidade(friend.getString("curiosidade"));
                    lAlimento.setEnergia_kcal(friend.getString("energia_kcal"));
                    lAlimento.setProteinas_g(friend.getString("proteinas_g"));
                    lAlimento.setLipideos_g(friend.getString("lipideos_g"));
                    lAlimento.setCarboidratos_g(friend.getString("carboidratos_g"));
                    lAlimento.setFibra_g(friend.getString("fibra_g"));
                    lAlimento.setCalcio_mg(friend.getString("calcio_mg"));
                    lAlimento.setFosforo_mg(friend.getString("fosforo_mg"));
                    lAlimento.setFerro_mg(friend.getString("ferro_mg"));
                    lAlimento.setRetinol_mg(friend.getString("retinol_mg"));
                    lAlimento.setVitb1_mg(friend.getString("vitb1_mg"));
                    lAlimento.setVitb2_mg(friend.getString("vitb2_mg"));
                    lAlimento.setNiacina_mg(friend.getString("niacina_mg"));
                    lAlimento.setVitc_mg(friend.getString("vitc_mg"));

                    lAl = lAlimento;

                    TextView NomeTextView = (TextView)findViewById(R.id.txtnome);
                    NomeTextView.setText(lAl.getNome());
                    TextView NomeCientifico = (TextView)findViewById(R.id.txtnome_cientifico);
                    NomeCientifico.setText(lAl.getNome_cientifico());
                    TextView NomePopular = (TextView)findViewById(R.id.txtnome_popular);
                    NomePopular.setText(lAl.getNome_popular());
                    TextView Origem = (TextView)findViewById(R.id.txtorigem);
                    Origem.setText(lAl.getOrigem());
                    TextView Regiao = (TextView)findViewById(R.id.txtregiao);
                    Regiao.setText(lAl.getRegiao());
                    TextView Categoria = (TextView)findViewById(R.id.txtcategoria);
                    Categoria.setText(lAl.getCategoria());
                    TextView Culinaria = (TextView)findViewById(R.id.txtculinaria);
                    Culinaria.setText(lAl.getCulinaria());
                    TextView Caracteristicas = (TextView)findViewById(R.id.txtcaracteristicas);
                    Caracteristicas.setText(lAl.getCaracteristicas());
                    TextView Curiosidade = (TextView)findViewById(R.id.txtcuriosidade);
                    Curiosidade.setText(lAl.getCuriosidade());


                if(lAlimento.getEnergia_kcal().equals("0")){
                    lAlimento.setEnergia_kcal("--");
                }
                TextView Caloria = (TextView)findViewById(R.id.txtcaloria);
                Caloria.setText(lAl.getEnergia_kcal());

                if(lAlimento.getProteinas_g().equals("0")) {
                    lAlimento.setProteinas_g("--");
                }
                TextView Proteina = (TextView)findViewById(R.id.txtproteina);
                Proteina.setText(lAl.getProteinas_g());

                if(lAlimento.getCarboidratos_g().equals("0")) {
                    lAlimento.setCarboidratos_g("--");
                }
                TextView Carboidrato = (TextView)findViewById(R.id.txtcarboidrato);
                Carboidrato.setText(lAl.getCarboidratos_g());

                if(lAlimento.getLipideos_g().equals("0")) {
                    lAlimento.setLipideos_g("--");
                }
                TextView Lipideo = (TextView)findViewById(R.id.txtlipideos);
                Lipideo.setText(lAl.getProteinas_g());

                if(lAlimento.getFibra_g().equals("0")) {
                    lAlimento.setFibra_g("--");
                }
                TextView Fibra = (TextView)findViewById(R.id.txtfibra);
                Fibra.setText(lAl.getFibra_g());


                if(lAlimento.getCalcio_mg().equals("0")) {
                    lAlimento.setCalcio_mg("--");
                }
                TextView Calcio = (TextView)findViewById(R.id.txtcalcio);
                Calcio.setText(lAl.getCalcio_mg());

                if(lAlimento.getFosforo_mg().equals("0")) {
                    lAlimento.setFosforo_mg("--");
                }
                TextView Fosforo = (TextView)findViewById(R.id.txtfosforo);
                Fosforo.setText(lAl.getFosforo_mg());

                if(lAlimento.getFerro_mg().equals("0")) {
                    lAlimento.setFerro_mg("--");
                }
                TextView Ferro = (TextView)findViewById(R.id.txtferro);
                Ferro.setText(lAl.getFerro_mg());

                if(lAlimento.getRetinol_mg().equals("0")) {
                    lAlimento.setRetinol_mg("--");
                }
                TextView Retinol = (TextView)findViewById(R.id.txtretinol);
                Retinol.setText(lAl.getRetinol_mg());

                if(lAlimento.getVitb1_mg().equals("0")) {
                    lAlimento.setVitb1_mg("--");
                }
                TextView VitaminaB1 = (TextView)findViewById(R.id.txtvitb1);
                VitaminaB1.setText(lAl.getVitb1_mg());

                if(lAlimento.getVitb2_mg().equals("0")) {
                    lAlimento.setVitb2_mg("--");
                }
                TextView VitaminaB2 = (TextView)findViewById(R.id.txtvitb2);
                VitaminaB2.setText(lAl.getVitb2_mg());

                if(lAlimento.getNiacina_mg().equals("0")) {
                    lAlimento.setNiacina_mg("--");
                }
                TextView Niacina = (TextView)findViewById(R.id.txtniacina);
                Niacina.setText(lAl.getNiacina_mg());

                if(lAlimento.getVitc_mg().equals("0")) {
                    lAlimento.setVitc_mg("--");
                }
                TextView VitaminaC = (TextView)findViewById(R.id.txtvitc);
                VitaminaC.setText(lAl.getVitc_mg());


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
        // O efeito ao ser pressionado do botão (no caso abre a activity)
        finish(); //Método para matar a activity e não deixa-lá indexada na pilhagem
        return;
    }
}
