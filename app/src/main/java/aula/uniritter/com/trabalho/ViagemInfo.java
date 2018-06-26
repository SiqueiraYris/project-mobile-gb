package aula.uniritter.com.trabalho;

import android.os.Parcel;
import android.os.Parcelable;

public class ViagemInfo implements Parcelable {

    private String nomeLocal = "";
    private String dataIda = "";
    private String dataVolta = "";
    private Long idUser = -1L;

    private Long id = -1L;

    ViagemInfo(){

    }

    private ViagemInfo(Parcel in){
        String[] data = new String[5];
        in.readStringArray(data);
        setNomeLocal(data[0]);
        setDataIda(data[1]);
        setDataVolta(data[2]);
        setIdUser(Long.parseLong(data[3]));
        setId(Long.parseLong(data[4]));
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getDataIda() {
        return dataIda;
    }

    public void setDataIda(String dataIda) {
        this.dataIda = dataIda;
    }

    public String getDataVolta() {
        return dataVolta;
    }

    public void setDataVolta(String dataVolta) {
        this.dataVolta = dataVolta;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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
                getNomeLocal(), getDataIda(), getDataVolta(), String.valueOf(getIdUser()), String.valueOf(getId())
        });
    }

    public static final Parcelable.Creator<ViagemInfo> CREATOR = new Parcelable.Creator<ViagemInfo>(){

        @Override
        public ViagemInfo createFromParcel(Parcel parcel) {
            return new ViagemInfo(parcel);
        }

        @Override
        public ViagemInfo[] newArray(int i) {
            return new ViagemInfo[i];
        }

    };
}
