package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

public class ListaNotasAdapter extends RecyclerView.Adapter {

    private final List<Nota> notas;
    private Context context;
    //contador de teste para analise da quantidade de view criada
    private static int quantidadeViewCriada = 0;
    //contador de teste para quantidade de bind realizado
    private static int quantidadeBindView = 0;

    public ListaNotasAdapter(List<Nota> notas, Context contexto){
        this.context = contexto;
        this.notas = notas;
    }

    @NonNull
    @Override //cria a view de acordo com o layout e prepara para a exibicao do viewholder
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        Log.i("RecyclerView adapter", "onCreateViewHolder: view criada meninim -> "+quantidadeViewCriada++);

        return new NotaViewHolder(viewCriada);
    }

    @Override //realiza a vinculacao (reutilzando as views criadas no oncreateviewholder) para cada view criada durante o scroll
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Nota nota = notas.get(position);
        //faco o cast do objeto holder para delegar a responsabilidade para o mesmo fazer o vinculo
        NotaViewHolder notaViewHolder = (NotaViewHolder) holder; //podemos fazer um cast ou receber no generics da classe

        Log.i("recyclerView adapter", "bindViewHolder"+ "posicao: "+position + " quantidade : " + quantidadeBindView++);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    //classe interna para o uso da viewHolder, como este sera usado somente dentro deste adapter nao criamos uma classe externa
    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
             this.titulo = itemView.findViewById(R.id.item_nota_titulo);
             this.descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        public void vincula(Nota nota){
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }

    }



}


