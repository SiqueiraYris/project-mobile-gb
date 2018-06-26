package aula.uniritter.com.trabalho;

import android.os.Parcel;
import android.os.Parcelable;

public class UsuarioInfo implements Parcelable{

    private String nome = "";
    private String sobrenome = "";
    private String email = "";
    private String senha = "";
    private String endereco = "";
    private String foto = "";

    private Long id = -1L;

    UsuarioInfo(){

    }

    private UsuarioInfo(Parcel in){
        String[] data = new String[7];
        in.readStringArray(data);
        setNome(data[0]);
        setSobrenome(data[1]);
        setEmail(data[2]);
        setSenha(data[3]);
        setEndereco(data[4]);
        setFoto(data[5]);
        setId(Long.parseLong(data[6]));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                getNome(), getSobrenome(), getEmail(), getSenha(), getEndereco(), getFoto(), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<UsuarioInfo> CREATOR = new Parcelable.Creator<UsuarioInfo>(){

        @Override
        public UsuarioInfo createFromParcel(Parcel parcel) {
            return new UsuarioInfo(parcel);
        }

        @Override
        public UsuarioInfo[] newArray(int i) {
            return new UsuarioInfo[i];
        }

    };
}
