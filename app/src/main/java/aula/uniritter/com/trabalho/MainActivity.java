package aula.uniritter.com.trabalho;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Database helper;

    private RecyclerView viagensRecy;
    private ViagemAdapter adapter;
    private TextView name;
    private ImageView photo;
    private Button editProfile;

    private List<ViagemInfo> listaViagens;
    private UsuarioInfo usuarioAtual;

    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;
    private final int REQUEST_ALTER_USER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditarViagemActivity.class);
                i.putExtra("viagem", new ViagemInfo());
                startActivityForResult(i, REQUEST_NEW);
            }
        });

        usuarioAtual = getIntent().getExtras().getParcelable("usuario");

        name = findViewById(R.id.name);
        name.setText("Bem-vindo(a), " + usuarioAtual.getNome());

        photo = findViewById(R.id.imageFoto);
        File imgFile = new File(usuarioAtual.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            photo.setImageBitmap(bitmap);
        }

        editProfile = findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                i.putExtra("usuario", usuarioAtual);
                i.putExtra("edit", true);
                startActivityForResult(i, REQUEST_ALTER_USER);
            }
        });

        helper = new Database(this);
        listaViagens = helper.getListViagens(usuarioAtual);

        viagensRecy = findViewById(R.id.viagensRecy);
        viagensRecy.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        viagensRecy.setLayoutManager(llm);

        adapter = new ViagemAdapter(listaViagens);
        viagensRecy.setAdapter(adapter);

        viagensRecy.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        abrirOpcoes(listaViagens.get(position));
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW && resultCode == RESULT_OK) {
            ViagemInfo viagemInfo = data.getParcelableExtra("viagem");
            viagemInfo.setIdUser(usuarioAtual.getId());
            helper.inserirViagem(viagemInfo);
            listaViagens = helper.getListViagens(usuarioAtual);
            adapter = new ViagemAdapter(listaViagens);
            viagensRecy.setAdapter(adapter);
        } else if (requestCode == REQUEST_ALTER && resultCode == RESULT_OK) {
            ViagemInfo viagemInfo = data.getParcelableExtra("viagem");
            helper.alteraViagem(viagemInfo);
            listaViagens = helper.getListViagens(usuarioAtual);
            adapter = new ViagemAdapter(listaViagens);
            viagensRecy.setAdapter(adapter);
        } else if(requestCode == REQUEST_ALTER_USER && resultCode == RESULT_OK){
            UsuarioInfo usuarioInfo = data.getParcelableExtra("usuario");
            helper.alterarUsuario(usuarioInfo);
            usuarioAtual.setNome(usuarioInfo.getNome());
            usuarioAtual.setSobrenome(usuarioInfo.getSobrenome());
            usuarioAtual.setEmail(usuarioInfo.getEmail());
            usuarioAtual.setSenha(usuarioInfo.getSenha());
            usuarioAtual.setEndereco(usuarioInfo.getEndereco());
            usuarioAtual.setFoto(usuarioInfo.getFoto());
            photo = findViewById(R.id.imageFoto);
            File imgFile = new File(usuarioAtual.getFoto());
            if(imgFile.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                photo.setImageBitmap(bitmap);
            }
        }
    }

    private void abrirOpcoes(final ViagemInfo viagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(viagem.getNomeLocal());
        builder.setItems(new CharSequence[]{"Editar", "Excluir"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                Intent intent1 = new Intent(MainActivity.this, EditarViagemActivity.class);
                                intent1.putExtra("viagem", viagem);
                                intent1.putExtra("edit", true);
                                startActivityForResult(intent1, REQUEST_ALTER);
                                break;
                            case 1:
                                listaViagens.remove(viagem);
                                helper.apagarViagem(viagem);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Viagem excluída com sucesso!", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            finish();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        // block back button after user is logged, to back to login you need logout
    }
}
