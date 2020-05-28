package tk.divesdk.nutrifood;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private String email;
    private String senha;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private int GOOGLE_SIGN_IN = 123;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Verificar se o usuário está logado
        verificarUsuarioLogado(firebaseAuth);
    }

    public void btn_login_google(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){ //Sucesso ao fazer login do usuario
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("nome")
                            .setValue(user.getDisplayName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent it = new Intent(getApplicationContext(), TelaPrincipal.class);
                                startActivity(it);
                            }
                            else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Cadastro falhou!",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                finish();
                            }
                        }
                    });
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Login falhou!", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    public void abrirCadastroUsuario(View view){
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity( intent );
    }

    private void verificarUsuarioLogado(FirebaseAuth firebaseAuth){
        if(firebaseAuth.getCurrentUser() != null){ // Usuario logado
            abrirAreaPrincipal();
        }
        else {
            setContentView(R.layout.activity_login);
        }
    }

    private void abrirAreaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, TelaPrincipal.class);
        startActivity( intent );
        finish();
    }

    public void forgetpassword(View v){
        Intent it = new Intent(getApplicationContext(),Reset.class);
        startActivity(it);
    }


    public void btn_login(View v){
        EditText Editemail = (EditText) findViewById(R.id.input_emaillogin);
        email = Editemail.getText().toString();
        EditText Editsenha = (EditText) findViewById(R.id.input_passwordlogin);
        senha = Editsenha.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ //Sucesso ao fazer login do usuario
                            ValueEventListener postListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String laprovado = dataSnapshot.getValue(String.class);
                                    Intent it;
                                    it = laprovado != null ? new Intent(getApplicationContext(), TelaPrincipal.class) : new Intent(getApplicationContext(), Tela_termos.class);
                                    startActivity(it);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.w("ERRO", "loadPost:onCancelled", databaseError.toException());
                                }
                            };
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("aprovado").addValueEventListener(postListener);
                        }
                        else{
                            Toast toast = Toast.makeText(getApplicationContext(), "E-mail ou Senha inválidos", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }
                });
    }

}