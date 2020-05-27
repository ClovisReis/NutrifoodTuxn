package tk.divesdk.nutrifood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class CadastroActivity extends AppCompatActivity {
    private String email;
    private String senha;
    private EditText editemail;
    private EditText editsenha;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
    }

    public void CriarCadastro(View v) {
        editemail = (EditText) findViewById(R.id.input_email);
        email = editemail.getText().toString();
        editsenha = (EditText) findViewById(R.id.input_password);
        senha = editsenha.getText().toString();

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){ //Sucesso ao cadastrar usuario
                            Toast toast = Toast.makeText(getApplicationContext(), "Usuario cadastrado com sucesso",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            finish();
                        }
                        else{
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            switch (errorCode) {
                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(CadastroActivity.this, "O formato do token personalizado está incorreto. Por favor, verifique a documentação.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(CadastroActivity.this, "O token personalizado corresponde a um público diferente.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(CadastroActivity.this, "A credencial de autenticação fornecida está incorreta ou expirou.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(CadastroActivity.this, "O endereço de email está mal formatado.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(CadastroActivity.this, "A senha é inválida ou o usuário não possui uma senha.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(CadastroActivity.this, "As credenciais fornecidas não correspondem ao usuário conectado anteriormente.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(CadastroActivity.this, "Esta operação é sensível e requer autenticação recente. Efetue login novamente antes de tentar novamente esta solicitação.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(CadastroActivity.this, "Já existe uma conta com o mesmo endereço de email, mas com credenciais de login diferentes. Faça login usando um provedor associado a este endereço de email.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(CadastroActivity.this, "O endereço de email já está sendo usado por outra conta.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(CadastroActivity.this, "Essa credencial já está associada a uma conta de usuário diferente.\n", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(CadastroActivity.this, "A conta do usuário foi desativada por um administrador.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_TOKEN_EXPIRED":
                                    Toast.makeText(CadastroActivity.this, "A credencial do usuário não é mais válida. O usuário deve entrar novamente.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(CadastroActivity.this, "Não há registro de usuário correspondente a esse identificador. O usuário pode ter sido excluído.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(CadastroActivity.this, "A credencial do usuário não é mais válida. O usuário deve entrar novamente.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(CadastroActivity.this, "Esta operação não é permitida. Você deve ativar este serviço no console.", Toast.LENGTH_LONG).show();
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(CadastroActivity.this, "A senha é inválida, deve conter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }
                });
    }

    private void abrirLoginUsuario(){
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
