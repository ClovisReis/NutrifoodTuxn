package tk.divesdk.nutrifood;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
                        TextView Caloria = (TextView)findViewById(R.id.txtcaloria);
                        TextView Labelcaloria = (TextView)findViewById(R.id.textView28);
                        Caloria.setVisibility(View.GONE);
                        Labelcaloria.setVisibility(View.GONE);
                    }
                    else{
                        TextView Caloria = (TextView)findViewById(R.id.txtcaloria);
                        Caloria.setText(lAl.getEnergia_kcal());
                    }


                    if(lAlimento.getProteinas_g().equals("0")){
                        TextView Proteina = (TextView)findViewById(R.id.txtproteina);
                        TextView LabelProteina = (TextView)findViewById(R.id.textView30);
                        Proteina.setVisibility(View.GONE);
                        LabelProteina.setVisibility(View.GONE);;
                    }
                    else{
                        TextView Proteina = (TextView)findViewById(R.id.txtproteina);
                        Proteina.setText(lAl.getProteinas_g());
                    }


                    if(lAlimento.getLipideos_g().equals("0")){
                        TextView Lipideo = (TextView)findViewById(R.id.txtlipideos);
                        TextView LabelLipidio = (TextView)findViewById(R.id.textView32);
                        Lipideo.setVisibility(View.GONE);
                        LabelLipidio.setVisibility(View.GONE);;
                    }
                    else{
                        TextView Lipideo = (TextView)findViewById(R.id.txtlipideos);
                        Lipideo.setText(lAl.getProteinas_g());
                    }


                    if(lAlimento.getFibra_g().equals("0")){
                        TextView Fibra = (TextView)findViewById(R.id.txtfibra);
                        TextView LabelFibra = (TextView)findViewById(R.id.textView35);
                        Fibra.setVisibility(View.GONE);
                        LabelFibra.setVisibility(View.GONE);;
                    }
                    else{
                        TextView Fibra = (TextView)findViewById(R.id.txtfibra);
                        Fibra.setText(lAl.getFibra_g());
                    }


                    if(lAlimento.getCalcio_mg().equals("0")){
                        TextView Calcio = (TextView)findViewById(R.id.txtcalcio);
                        TextView LabelCalcio= (TextView)findViewById(R.id.textView36);
                        Calcio.setVisibility(View.GONE);
                        LabelCalcio.setVisibility(View.GONE);;
                    }
                    else{
                        TextView Calcio = (TextView)findViewById(R.id.txtcalcio);
                        Calcio.setText(lAl.getProteinas_g());
                    }


                if(lAlimento.getFosforo_mg().equals("0")){
                    TextView Fosforo = (TextView)findViewById(R.id.txtfosforo);
                    TextView LabelFosforo= (TextView)findViewById(R.id.textView37);
                    Fosforo.setVisibility(View.GONE);
                    LabelFosforo.setVisibility(View.GONE);;
                }
                else{
                    TextView Fosforo = (TextView)findViewById(R.id.txtfosforo);
                    Fosforo.setText(lAl.getFosforo_mg());
                }


                if(lAlimento.getFerro_mg().equals("0")){
                    TextView Ferro = (TextView)findViewById(R.id.txtferro);
                    TextView LabelFerro = (TextView)findViewById(R.id.textView45);
                    Ferro.setVisibility(View.GONE);
                    LabelFerro.setVisibility(View.GONE);
                }
                else{
                    TextView Ferro = (TextView)findViewById(R.id.txtferro);
                    Ferro.setText(lAl.getFerro_mg());
                }


                    if(lAlimento.getRetinol_mg().equals("0")){
                        TextView Retinol = (TextView)findViewById(R.id.txtretinol);
                        TextView LabelRetinol = (TextView)findViewById(R.id.textView45);
                        Retinol.setVisibility(View.GONE);
                        LabelRetinol.setVisibility(View.GONE);
                    }
                    else{
                        TextView Retinol = (TextView)findViewById(R.id.txtretinol);
                        Retinol.setText(lAl.getRetinol_mg());
                    }


                    if(lAlimento.getVitb1_mg().equals("0")){
                        TextView VitaminaB1 = (TextView)findViewById(R.id.txtvitb1);
                        TextView LabelVitB1 = (TextView)findViewById(R.id.textView40);
                        VitaminaB1.setVisibility(View.GONE);
                        LabelVitB1.setVisibility(View.GONE);
                    }
                    else{
                        TextView VitaminaB1 = (TextView)findViewById(R.id.txtvitb1);
                        VitaminaB1.setText(lAl.getVitb1_mg());
                    }


                if(lAlimento.getVitb2_mg().equals("0")){
                    TextView VitaminaB2 = (TextView)findViewById(R.id.txtvitb2);
                    TextView LabelVitB2 = (TextView)findViewById(R.id.textView41);
                    VitaminaB2.setVisibility(View.GONE);
                    LabelVitB2.setVisibility(View.GONE);
                }
                else{
                    TextView VitaminaB2 = (TextView)findViewById(R.id.txtvitb2);
                    VitaminaB2.setText(lAl.getVitb2_mg());
                }

                if(lAlimento.getNiacina_mg().equals("0")){
                    TextView Niacina = (TextView)findViewById(R.id.txtniacina);
                    TextView LabelNiacina = (TextView)findViewById(R.id.textView42);
                    Niacina.setVisibility(View.GONE);
                    LabelNiacina.setVisibility(View.GONE);
                }
                else{
                    TextView Niacina = (TextView)findViewById(R.id.txtniacina);
                    Niacina.setText(lAl.getNiacina_mg());
                }


                if(lAlimento.getVitc_mg().equals("0")){
                    TextView VitaminaC = (TextView)findViewById(R.id.txtvitc);
                    TextView LabelVitc = (TextView)findViewById(R.id.textView43);
                    VitaminaC.setVisibility(View.GONE);
                    LabelVitc.setVisibility(View.GONE);
                }
                else{
                    TextView VitaminaC = (TextView)findViewById(R.id.txtvitc);
                    VitaminaC.setText(lAl.getVitc_mg());
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
