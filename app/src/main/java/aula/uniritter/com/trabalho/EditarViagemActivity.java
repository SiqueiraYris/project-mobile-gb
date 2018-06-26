package aula.uniritter.com.trabalho;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;


public class EditarViagemActivity extends AppCompatActivity {

    private ViagemInfo viagem;

    private View layout;

    private EditText nomeLocal;
    private EditText dataIda;
    private EditText dataVolta;
    private boolean edit;

    private Button salvar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_viagem);

        viagem = getIntent().getParcelableExtra("viagem");

        edit = getIntent().getBooleanExtra("edit", false);

        layout = findViewById(R.id.viagem);

        nomeLocal = findViewById(R.id.nomeLocal);
        dataIda = findViewById(R.id.dataIda);
        dataVolta = findViewById(R.id.dataVolta);

        nomeLocal.setText(viagem.getNomeLocal());
        dataIda.setText(viagem.getDataIda());
        dataVolta.setText(viagem.getDataVolta());

        salvar = findViewById(R.id.btnSalvarViagem);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viagem.setNomeLocal(nomeLocal.getText().toString());
                viagem.setDataIda(dataIda.getText().toString());
                viagem.setDataVolta(dataVolta.getText().toString());

                if (viagem.getNomeLocal().equals("")) {
                    Toast.makeText(EditarViagemActivity.this, "É necessário um local para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (viagem.getDataIda().equals("")) {
                    Toast.makeText(EditarViagemActivity.this, "É necessário uma data de partida para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }


                int diaPar = Integer.parseInt(viagem.getDataIda().substring(0,2));
                int diaVol = Integer.parseInt(viagem.getDataVolta().substring(0,2));
                int mesPar = Integer.parseInt(viagem.getDataIda().substring(3,5));
                int mesVol = Integer.parseInt(viagem.getDataVolta().substring(3,5));
                int anoPar = Integer.parseInt(viagem.getDataIda().substring(6,10));
                int anoVol = Integer.parseInt(viagem.getDataVolta().substring(6,10));

                if (diaPar > 31 || diaVol > 31 || diaPar < 1 || diaVol < 1) {
                    Toast.makeText(EditarViagemActivity.this, "Dia inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mesPar > 12 || mesVol > 12 || mesPar < 1 || mesVol < 1) {
                    Toast.makeText(EditarViagemActivity.this, "Mês inválido!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if((mesPar > mesVol) || (mesPar == mesVol && diaPar > diaVol) || (anoVol < anoPar)){
                    Toast.makeText(EditarViagemActivity.this, "Data de partida não pode ser após a data de retorno", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("viagem", viagem);
                setResult(RESULT_OK, i);
                if (edit) {
                    Toast.makeText(EditarViagemActivity.this, "Viagem alterada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditarViagemActivity.this, "Viagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
    }

}

