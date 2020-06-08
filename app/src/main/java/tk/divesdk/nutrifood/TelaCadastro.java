package tk.divesdk.nutrifood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class TelaCadastro extends AppCompatActivity {
    private String email;
    private String senha;
    private String senha_confirm;
    private String nome;
    private EditText editemail;
    private EditText editsenha;
    private EditText editsenha_confirm;
    private EditText editnome;
    private FirebaseAuth firebaseAuth;

    //VALIDAÇÃO
    private boolean ErroProfile = false;
    private boolean ErroSaveUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void CriarCadastro(View v) {

        if (isOnline()) {

            //DADOS DA TELA
            editnome = (EditText) findViewById(R.id.input_name);
            nome = editnome.getText().toString().trim();

            editemail = (EditText) findViewById(R.id.input_email);
            email = editemail.getText().toString().trim();

            editsenha = (EditText) findViewById(R.id.input_password);
            senha = editsenha.getText().toString().trim();
            editsenha_confirm = (EditText) findViewById(R.id.input_password_confirm);
            senha_confirm = editsenha_confirm.getText().toString().trim();

            //VALIDAÇÃO DADOS
            if (nome.isEmpty()) {
                editnome.setError(getString(R.string.input_error_name));
                editnome.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                editemail.setError(getString(R.string.input_error_email));
                editemail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editemail.setError(getString(R.string.input_error_email_invalid));
                editemail.requestFocus();
                return;
            }

            if (senha.isEmpty()) {
                editsenha.setError(getString(R.string.input_error_password));
                editsenha.requestFocus();
                return;
            }

            if (senha.length() < 6) {
                editsenha.setError(getString(R.string.input_error_password_length));
                editsenha.requestFocus();
                return;
            }

            if (senha_confirm.isEmpty()) {
                editsenha.setError(getString(R.string.input_error_password_presence));
                editsenha.requestFocus();
                return;
            }

            if (!senha.equals(senha_confirm)) {
                editsenha.setError(getString(R.string.input_error_password_confirm));
                editsenha.requestFocus();
                return;
            }

            ErroProfile = false;
            ErroSaveUser = false;

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(TelaCadastro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) { //SUCESSO AO CADASTRAR EMAIL/SENHA FIREBASEAUTH

                                //PEGA REFERÊNCIA USUÁRIO
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                //ATUALIZA PROFILE (ASSÍCRONO)
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nome).build();
                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            ErroProfile = true;
                                        }
                                    }
                                });

                                //CRIA OBJETO USER
                                User NovoUsuario = new User(nome, user.getEmail(), false, false, user.getUid());

                                //SALVA USUÁRIO
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(user.getUid()).setValue(NovoUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (!task.isSuccessful()) {
                                            ErroSaveUser = true;
                                        }
                                    }
                                });

                                if (ErroProfile || ErroSaveUser) {
                                    user.delete();
                                    Toast toast = Toast.makeText(getApplicationContext(), "Ocorreu uma falha no cadastro, tente novamente.",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso.",
                                            Toast.LENGTH_SHORT);
                                    toast.show();
                                    finish();
                                }
                            } else {
                                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                switch (errorCode) {
                                    case "ERROR_INVALID_EMAIL":
                                        Toast.makeText(TelaCadastro.this, "O endereço de email está mal formatado.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_WRONG_PASSWORD":
                                        Toast.makeText(TelaCadastro.this, "A senha é inválida.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                        Toast.makeText(TelaCadastro.this, "Já existe uma conta com o mesmo endereço de email, mas com credenciais de login diferentes. Faça login usando um provedor associado a este endereço de e-mail.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_EMAIL_ALREADY_IN_USE":
                                        Toast.makeText(TelaCadastro.this, "O endereço de e-mail já está sendo usado por outra conta.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                        Toast.makeText(TelaCadastro.this, "Essa credencial já está associada a uma conta de usuário diferente.\n", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_USER_DISABLED":
                                        Toast.makeText(TelaCadastro.this, "A conta do usuário foi desativada por um administrador.", Toast.LENGTH_LONG).show();
                                        break;
                                    case "ERROR_WEAK_PASSWORD":
                                        Toast.makeText(TelaCadastro.this, "A senha é inválida, deve conter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(TelaCadastro.this, "Ocorreu uma falha no cadastro, tente novamente.", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }
                    });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Conecte-se a internet para efetuar o cadastro.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void abrirLoginUsuario(View v) {
        Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
        startActivity(intent);
        finish();
    }
}
