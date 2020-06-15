package tk.divesdk.nutrifood;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TelaLogin extends AppCompatActivity {
    private String email;
    private String senha;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int GOOGLE_SIGN_IN = 123;

    //VALIDAÇÃO
    private boolean ErroSaveuser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (isOnline()) {
            firebaseAuth = FirebaseAuth.getInstance();

            // Configure Google
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            //Verificar se o usuário está logado
            verificarUsuarioLogado(firebaseAuth);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para efetuar o login.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void btn_login_google(View view) {
        if (isOnline()) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para efetuar o login.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final ProgressDialog pd = new ProgressDialog(TelaLogin.this);
        pd.setTitle("NutriFood");
        pd.setMessage("Carregando...Por favor aguarde!");
        pd.show();

        ErroSaveuser = false;

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(TelaLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //SUCESSO AO LOGAR COM O GOOGLE - FIREBASEAUTH
                            ValueEventListener DatabaseListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String laprovado = dataSnapshot.getValue() != null ? dataSnapshot.getValue(Boolean.class).toString() : dataSnapshot.getValue(String.class);
                                    if (laprovado == null) { //NUNCA LOGOU NA APLICAÇÂO

                                        //PEGA REFERÊNCIA USUÁRIO
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if (user != null) {
                                            //CRIA OBJETO USER
                                            User NovoUsuario = new User(user.getDisplayName(), user.getEmail(), false, false, user.getUid());

                                            //SALVA USUÁRIO
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(user.getUid()).setValue(NovoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (!task.isSuccessful()) {
                                                        ErroSaveuser = true;
                                                    }
                                                }
                                            });

                                            if (ErroSaveuser) {
                                                pd.dismiss();
                                                Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                user.delete();
                                                pd.dismiss();
                                                Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                                                toast.show();
                                            }
                                            pd.dismiss();
                                            Intent it = new Intent(getApplicationContext(), TelaTermos.class);
                                            startActivity(it);
                                        } else {
                                            pd.dismiss();
                                            Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                                            toast.show();
                                        }

                                    } else { // JÁ LOGOU
                                        //CASO LAPROVADO SEJA FALSE REDIRECT TERMO, SENÃO TELA PRINCIPAL
                                        Intent it = laprovado.equals("false") ? new Intent(getApplicationContext(), TelaTermos.class) : new Intent(getApplicationContext(), TelaPrincipal.class);
                                        startActivity(it);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    pd.dismiss();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            };
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("aprovado").addValueEventListener(DatabaseListener);
                        } else {
                            pd.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no login, tente novamente.", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

    public void abrirCadastroUsuario(View view) {
        Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
        startActivity(intent);
    }

    private void verificarUsuarioLogado(FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() != null) { // USUÁRIO LOGADO
            ValueEventListener DatabaseListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String laprovado = dataSnapshot.getValue() != null ? dataSnapshot.getValue(Boolean.class).toString() : dataSnapshot.getValue(String.class);
                    //CASO LAPROVADO SEJA FALSE REDIRECT TERMO, SENÃO TELA PRINCIPAL
                    Intent it = laprovado != null && laprovado.equals("false") ? new Intent(getApplicationContext(), TelaTermos.class) : new Intent(getApplicationContext(), TelaPrincipal.class);
                    startActivity(it);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha, tente novamente.", Toast.LENGTH_LONG);
                    toast.show();
                }
            };
            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("aprovado").addValueEventListener(DatabaseListener);
        } else {
            setContentView(R.layout.activity_login);
        }
    }

    public void forgetpassword(View v) {
        if (isOnline()) {
            Intent it = new Intent(getApplicationContext(), TelaRecuperaSenha.class);
            startActivity(it);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para efetuar o login.", Toast.LENGTH_LONG);
            toast.show();
        }
    }


    public void btn_login(View v) {
        final ProgressDialog pd = new ProgressDialog(TelaLogin.this);
        pd.setTitle("NutriFood");
        pd.setMessage("Carregando...Por favor aguarde!");

        if (isOnline()) {
            //DADOS DA TELA
            EditText Editemail = (EditText) findViewById(R.id.input_emaillogin);
            email = Editemail.getText().toString();
            EditText Editsenha = (EditText) findViewById(R.id.input_passwordlogin);
            senha = Editsenha.getText().toString();
            pd.show();


            firebaseAuth.signInWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(TelaLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ValueEventListener DatabaseListener = new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String laprovado = dataSnapshot.getValue() != null ? dataSnapshot.getValue(Boolean.class).toString() : dataSnapshot.getValue(String.class);
                                        Intent it = laprovado == null || laprovado.equals("false") ? new Intent(getApplicationContext(), TelaTermos.class) : new Intent(getApplicationContext(), TelaPrincipal.class);
                                        startActivity(it);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Login falhou!", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                };
                                FirebaseDatabase.getInstance()
                                        .getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("aprovado").addValueEventListener(DatabaseListener);
                            } else {
                                pd.dismiss();
                                Toast toast = Toast.makeText(getApplicationContext(), "E-mail ou Senha inválidos", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
        } else {
            pd.dismiss();
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para efetuar o login.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}