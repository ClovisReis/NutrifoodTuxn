package tk.divesdk.nutrifood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

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

public class TelaPrincipal extends AppCompatActivity  {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private String Aba;
    private String AbaConsulta;
    ListView lv;
    int a= 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    AbaConsulta = "categoria";
                    CarregaList("categorias");
                    return true;
                case R.id.navigation_dashboard:
                    AbaConsulta = "regiao";
                    CarregaList("regioes");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                Intent intent;
                switch (menuItem.getItemId()) {
                    case R.id.contato:
                        intent = new Intent(TelaPrincipal.this, TelaContato.class);
                        startActivity(intent);
                        break;
                    case R.id.sobre:
                        intent = new Intent(TelaPrincipal.this, TelaSobre.class);
                        startActivity(intent);
                        break;
                    case R.id.formulario:
                        Uri uri = Uri.parse("http://goo.gl/forms/QmLw25RDuQ");
                        intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        intent = new Intent(TelaPrincipal.this, TelaLogin.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.googleplay:
                        break;
                }

                return false;
            }
                });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();


        AbaConsulta = "categoria";
        CarregaList("categorias");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ListView t = (ListView) findViewById(R.id.lv);
        t.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Intent it = new Intent(TelaPrincipal.this, TelaListaAlimentos.class);
                it.putExtra("ABA_SELECIONADA", AbaConsulta);
                String lItem = selectedItem.replace("{", "").replace("}","").replace("name_item=","");
                it.putExtra("ITEM_SELECIONADO", lItem);
                startActivity(it);
            }
        });
    }

    private void CarregaList(String tipoItem){
        Aba = tipoItem;
        new ParseTask().execute();
    }

        private class ParseTask extends AsyncTask<Void, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(TelaPrincipal.this);
            pd.setTitle("NutriFood");
            pd.setMessage("Carregando...Por favor aguarde!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String $url_json = "https://nutrifoodapi.herokuapp.com/" + Aba;
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
                JSONArray jArray = json.getJSONArray("data");

                for (int i = 0; i < jArray.length(); i++) {
                    String nameOS = jArray.get(i).toString();
                    Log.d("FOR_LOG", nameOS);

                    hashmap = new HashMap<String, String>();
                    hashmap.put("name_item", "" + nameOS);
                    arrayList.add(hashmap);
                }
                final SimpleAdapter adapter = new SimpleAdapter(TelaPrincipal.this, arrayList, R.layout.item_list_view_menu, from, to);
                lView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
