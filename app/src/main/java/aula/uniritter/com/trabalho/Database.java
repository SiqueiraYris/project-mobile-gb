package aula.uniritter.com.trabalho;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int VERSAO = 19;
    private final String TABELAUSUARIO = "Usuario";
    private final String TABELAVIAGEM = "Viagem";
    private static final String DATABASE = "Viagens";

    public Database(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlUsuario = "CREATE TABLE IF NOT EXISTS " + TABELAUSUARIO
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,  "
                + " nome TEXT NOT NULL, "
                + " sobrenome TEXT NOT NULL, "
                + " email TEXT NOT NULL, "
                + " senha TEXT NOT NULL, "
                + " endereco TEXT, "
                + " foto TEXT);";

        String sqlViagem = "CREATE TABLE IF NOT EXISTS " + TABELAVIAGEM
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,  "
                + " nomelocal TEXT NOT NULL, "
                + " dataida TEXT NOT NULL, "
                + " datavolta TEXT, "
                + " userid INTEGER, "
                + " CONSTRAINT FK_user FOREIGN KEY(userid) REFERENCES Usuarios);";

        sqLiteDatabase.execSQL(sqlUsuario);
        sqLiteDatabase.execSQL(sqlViagem);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<UsuarioInfo> getListUsuarios(String order) {
        List<UsuarioInfo> usuarios = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAUSUARIO + " ORDER BY nome " +
                order + ";", null);

        while (cursor.moveToNext()) {
            UsuarioInfo u = new UsuarioInfo();

            u.setId(cursor.getLong(cursor.getColumnIndex("id")));
            u.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            u.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
            u.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            u.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            u.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            u.setFoto(cursor.getString(cursor.getColumnIndex("foto")));

            usuarios.add(u);
        }

        cursor.close();

        return usuarios;
    }

    public List<ViagemInfo> getListViagens(UsuarioInfo usuarioAtual){
        List<ViagemInfo> viagens = new ArrayList<>();

        onCreate(getReadableDatabase());

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAVIAGEM + " WHERE userid == " + usuarioAtual.getId() + ";", null);

        while(cursor.moveToNext()){
            ViagemInfo c = new ViagemInfo();

            c.setId(cursor.getLong(cursor.getColumnIndex("id")));
            c.setNomeLocal(cursor.getString(cursor.getColumnIndex("nomelocal")));
            c.setDataIda(cursor.getString(cursor.getColumnIndex("dataida")));
            c.setDataVolta(cursor.getString(cursor.getColumnIndex("datavolta")));
            c.setIdUser(cursor.getLong(cursor.getColumnIndex("userid")));

            viagens.add(c);
        }

        cursor.close();

        return viagens;
    }

    public void inserirUsuario(UsuarioInfo u) {
        ContentValues values = new ContentValues();

        values.put("nome", u.getNome());
        values.put("sobrenome", u.getSobrenome());
        values.put("email", u.getEmail());
        values.put("senha", u.getSenha());
        values.put("endereco", u.getEndereco());
        values.put("foto", u.getFoto());

        getWritableDatabase().insert(TABELAUSUARIO, null, values);
    }

    public void inserirViagem(ViagemInfo c){
        ContentValues values = new ContentValues();

        values.put("nomelocal", c.getNomeLocal());
        values.put("dataida", c.getDataIda());
        values.put("datavolta", c.getDataVolta());
        values.put("userid", c.getIdUser());

        getWritableDatabase().insert(TABELAVIAGEM, null, values);
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAVIAGEM + ";", null);
    }

    public void alterarUsuario(UsuarioInfo u) {
        ContentValues values = new ContentValues();

        values.put("id", u.getId());
        values.put("nome", u.getNome());
        values.put("sobrenome", u.getSobrenome());
        values.put("email", u.getEmail());
        values.put("senha", u.getSenha());
        values.put("endereco", u.getEndereco());
        values.put("foto", u.getFoto());

        String[] idParaSerAlterado = {u.getId().toString()};
        getWritableDatabase().update(TABELAUSUARIO, values, "id=?", idParaSerAlterado);
    }

    public void alteraViagem(ViagemInfo c){
        ContentValues values = new ContentValues();

        values.put("id", c.getId());
        values.put("nomelocal", c.getNomeLocal());
        values.put("dataida", c.getDataIda());
        values.put("datavolta", c.getDataVolta());
        values.put("userid", c.getIdUser());

        String[] idParaSerAlterado = {c.getId().toString()};
        getWritableDatabase().update(TABELAVIAGEM, values, "id=?", idParaSerAlterado);
    }

    public void apagarUsuario(UsuarioInfo u) {
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {u.getId().toString()};
        db.delete(TABELAUSUARIO, "id=?", args);
    }

    public void apagarViagem(ViagemInfo c){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {c.getId().toString()};
        db.delete(TABELAVIAGEM, "id=?", args);
    }

    public UsuarioInfo getUsuarioAtual(String email, String senha) {
        UsuarioInfo usuario = null;

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAUSUARIO + " WHERE email == '"
                + email + "' AND senha == '" + senha + "'" + ";", null);

        while (cursor.moveToNext()) {
            usuario = new UsuarioInfo();
            usuario.setId(cursor.getLong(cursor.getColumnIndex("id")));
            usuario.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            usuario.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
            usuario.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            usuario.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            usuario.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            usuario.setFoto(cursor.getString(cursor.getColumnIndex("foto")));
        }

        cursor.close();

        return usuario;
    }

    public boolean validaUsuario(String email) {
        boolean valido = true;

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAUSUARIO + " WHERE email == '"
                + email + "'" + ";", null);


        if(cursor.getCount() > 0){
            valido = false;
        }

        cursor.close();

        return valido;
    }

    public boolean validaEmail(String email, Long id) {
        boolean valido = true;

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAUSUARIO + " WHERE email == '"
                + email + "' AND id == " + id + ";", null);

        if(cursor.getCount() == 0){
            valido = false;
        }

        cursor.close();

        return valido;
    }

    public UsuarioInfo getInfoUsuario(Long id) {
        UsuarioInfo usuario = new UsuarioInfo();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABELAUSUARIO + " WHERE id == " + id + ";", null);

        while (cursor.moveToNext()) {
            UsuarioInfo u = new UsuarioInfo();

            u.setId(cursor.getLong(cursor.getColumnIndex("id")));
            u.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            u.setSobrenome(cursor.getString(cursor.getColumnIndex("sobrenome")));
            u.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            u.setSenha(cursor.getString(cursor.getColumnIndex("senha")));
            u.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            u.setFoto(cursor.getString(cursor.getColumnIndex("foto")));

        }

        cursor.close();

        return usuario;
    }

}

