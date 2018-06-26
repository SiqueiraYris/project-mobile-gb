package aula.uniritter.com.trabalho;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ViagemAdapter extends RecyclerView.Adapter<ViagemAdapter.ContactViewHolder> {

    private List<ViagemInfo> listaViagens;

    ViagemAdapter(List<ViagemInfo> lista) {
        listaViagens = lista;
    }

    @Override
    public ViagemAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_viagem, parent, false);
        return new ViagemAdapter.ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViagemAdapter.ContactViewHolder holder, int position) {
        ViagemInfo viagem = listaViagens.get(position);
        holder.nomeLocal.setText(viagem.getNomeLocal());
        holder.dataIda.setText(viagem.getDataIda());
        holder.dataVolta.setText(viagem.getDataVolta());
    }

    @Override
    public int getItemCount() {
        return listaViagens.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView nomeLocal;
        TextView dataIda;
        TextView dataVolta;

        ContactViewHolder(View v) {
            super(v);
            nomeLocal = v.findViewById(R.id.textoNomeLocal);
            dataIda = v.findViewById(R.id.textoDataIda);
            dataVolta = v.findViewById(R.id.textoDataVolta);
        }
    }

}
