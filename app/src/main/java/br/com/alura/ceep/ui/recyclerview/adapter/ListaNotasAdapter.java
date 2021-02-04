package br.com.alura.ceep.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private Context context;
    //contador de teste para analise da quantidade de view criada
    private static int quantidadeViewCriada = 0;
    //contador de teste para quantidade de bind realizado
    private static int quantidadeBindView = 0;

    private OnItemClickListener onItemClickListener;

    //criado o evento a partir da interface para que possa ser acessado e imnplementado por outros fora do adapter
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ListaNotasAdapter(List<Nota> notas, Context contexto){
        this.context = contexto;
        this.notas = notas;
    }

    @NonNull
    @Override //cria a view de acordo com o layout e prepara para a exibicao do viewholder
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        Log.i("RecyclerView adapter", "onCreateViewHolder: view criada meninim -> "+quantidadeViewCriada++);

        return new NotaViewHolder(viewCriada);
    }

    @Override //realiza a vinculacao (reutilzando as views criadas no oncreateviewholder) para cada view criada durante o scroll
    //aqui dentro so fica os processos que precisam ser feitos a cada vinculação a view
    public void onBindViewHolder(@NonNull ListaNotasAdapter.NotaViewHolder holder, int position) {
        Nota nota = notas.get(position);
        Log.i("ONBINDVIEWHOLDER", "==============================================================================");
        Log.i("ONBINDVIEWHOLDER", nota.toString());
        holder.vincula(nota);
        Log.i("ONBINDVIEWHOLDER", "==============================================================================");
        Log.i("recyclerView adapter", "bindViewHolder"+ "posicao: "+position + " quantidade : " + quantidadeBindView++);
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    public void adiciona(Nota notaRecebida) {
        this.notas.add(notaRecebida);
        //notifico o proprio adapter que foi feito uma alteração
        notifyDataSetChanged();
    }

    //altera o componente na tela
    public void altera(int posicao, Nota nota) {
        this.notas.set(posicao, nota);
        notifyItemChanged(posicao);

    }

    //remove do componente da tela
    public void remove(int posicao) {
        notas.remove(posicao);
        notifyItemRemoved(posicao);
    }

    //troca do componente da tela
    public void troca(int posicaoDaNotaInicial, int posicaoDaNotaFinal) {
        Collections.swap(notas, posicaoDaNotaInicial, posicaoDaNotaFinal);

        notifyItemMoved(posicaoDaNotaInicial, posicaoDaNotaFinal);
    }

    public void atualiza(List<Nota> notas) {
        if(notas != null){
            for(Nota nota: this.notas){
                Log.i("LISTARESULTADOONRES", nota.toString());
            }
            if(this.notas.size() <= 0){
                this.notas.addAll(notas);
            }
            notifyDataSetChanged();
        }

    }

    //classe interna para o uso da viewHolder, como este sera usado somente dentro deste adapter nao criamos uma classe externa
    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;
        private Nota nota;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
             this.titulo = itemView.findViewById(R.id.item_nota_titulo);
             this.descricao = itemView.findViewById(R.id.item_nota_descricao);

             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     //o metodo getAdapterPosition e do proprio viewHolder que conhece a posicao clicada do item na tela
                     onItemClickListener.onItemClick(nota, getAdapterPosition());
                 }
             });

             //caso queiramos implementar um novo tipo de clique no item.
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //implementação do clique longo
                    return false;
                }
            });


        }

        public void vincula(Nota nota){
            //preenche a nota que foi instanciada na innerClass NotaViewHolder
            this.nota = nota;

            Log.i("VINCULANDO AS NOTAS", "=============================================================================");
            Log.i("ViewHolder:Vincula", nota.toString());
            Log.i("ViewHolder:Vincula", this.nota.toString());
            Log.i("VINCULANDO AS NOTAS", "=============================================================================");

            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }

    }



}


