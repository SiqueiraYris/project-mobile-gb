package aula.uniritter.com.trabalho;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditActivity extends AppCompatActivity {

    private UsuarioInfo usuario;
    private Database helper;

    private View layout;

    private ImageButton foto;
    private EditText nome;
    private EditText sobrenome;
    private EditText email;
    private EditText senha;
    private EditText endereco;
    private boolean edit;

    private Button salvar;

    private final int CAMERA = 1;
    private final int GALERIA = 2;

    private final String IMAGE_DIR = "/FotosUsuarios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        helper = new Database(this);

        usuario = getIntent().getParcelableExtra("usuario");

        edit = getIntent().getBooleanExtra("edit", false);

        layout = findViewById(R.id.singup);

        foto = findViewById(R.id.foto);
        nome = findViewById(R.id.nomeUsuario);
        sobrenome = findViewById(R.id.sobrenome);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        endereco = findViewById(R.id.endereco);

        nome.setText(usuario.getNome());
        sobrenome.setText(usuario.getSobrenome());
        email.setText(usuario.getEmail());
        senha.setText(usuario.getSenha());
        endereco.setText(usuario.getEndereco());

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaImagem();
            }
        });

        File imgFile = new File(usuario.getFoto());
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.btnSalvar);
        if (edit) {
            salvar.setText("Salvar");
        } else {
            salvar.setText("Criar conta");
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario.setNome(nome.getText().toString());
                usuario.setSobrenome(sobrenome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setSenha(senha.getText().toString());
                usuario.setEndereco(endereco.getText().toString());

                if (usuario.getNome().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário um nome para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (usuario.getSobrenome().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário um sobrenome para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (usuario.getEmail().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário um email para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!(helper.validaUsuario(usuario.getEmail())) && !(edit)) {
                    Toast.makeText(EditActivity.this, "Email já cadastrado! Faça login ou utilize outro email.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (usuario.getSenha().equals("")) {
                    Toast.makeText(EditActivity.this, "É necessário uma senha para salvar!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (usuario.getSenha().length() < 6) {
                    Toast.makeText(EditActivity.this, "A senha precisa de no mínimo 6 caracteres!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edit) {
                    if (!(helper.validaUsuario(usuario.getEmail()))) {
                        if (!(helper.validaEmail(usuario.getEmail(), usuario.getId()))) {
                            Toast.makeText(EditActivity.this, "Email já cadastrado! Faça login ou utilize outro email.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                CharSequence inputStr = usuario.getEmail();
                Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(inputStr);

                if (!(matcher.matches())){
                    Toast.makeText(EditActivity.this, "Insira um email válido! Exemplo: teste@teste.com", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("usuario", usuario);
                setResult(RESULT_OK, i);
                if (edit) {
                    Toast.makeText(EditActivity.this, "Usuário alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditActivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    private void alertaImagem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a fonte da imagem");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaTirarFoto();
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaCarregaImagem();
            }
        });
        builder.create().show();
    }

    private void clicaTirarFoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            showCamera();
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            Snackbar.make(layout, "É necessário permitir para utilizar a câmera!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA);
        }
    }

    private void showCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    private void clicaCarregaImagem() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestGaleriaPermission();
        } else {
            showGaleria();
        }
    }

    private void requestGaleriaPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Snackbar.make(layout, "É necessário permitir para utilizar a galeria!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            GALERIA);
                }
            }).show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    GALERIA);
        }
    }

    private void showGaleria() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALERIA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clicaTirarFoto();
                }
                break;
            case GALERIA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }
        if (requestCode == GALERIA) {
            Uri contentURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                usuario.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            usuario.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);
        }

    }

    private String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

