package aula.uniritter.com.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Database helper;
    private final int REQUEST_NEW = 1;

    View singup;
    View login;
    EditText email;
    EditText senha;
    UsuarioInfo usuarioAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        singup = findViewById(R.id.link_signup);
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.input_email);
        senha = findViewById(R.id.input_password);

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, EditActivity.class);
                i.putExtra("usuario", new UsuarioInfo());
                startActivityForResult(i, REQUEST_NEW);
            }
        });
        helper = new Database(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("usuario", usuarioAtual);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW && resultCode == RESULT_OK) {
            UsuarioInfo usuarioInfo = data.getParcelableExtra("usuario");
            helper.inserirUsuario(usuarioInfo);
        }
    }

    protected boolean validate() {
        boolean valid = false;
        usuarioAtual = helper.getUsuarioAtual(email.getText().toString(), senha.getText().toString());

        if (usuarioAtual != null) {
            valid = true;
        } else if (email.getText().toString().isEmpty()) {
            valid = false;
            Toast.makeText(LoginActivity.this, "Digite um usuário válido!", Toast.LENGTH_SHORT).show();
        } else if (senha.getText().toString().isEmpty()) {
            valid = false;
            Toast.makeText(LoginActivity.this, "Digite uma senha válida!", Toast.LENGTH_SHORT).show();
        } else {
            valid = false;
            Toast.makeText(LoginActivity.this, "Usuário ou senha incorreta!", Toast.LENGTH_SHORT).show();
        }

        return valid;
    }
}
