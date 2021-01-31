package br.com.alura.ceep.ui.recyclerview.helper.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class NotaItemTouchHelperCallBack extends ItemTouchHelper.Callback {

    private final ListaNotasAdapter adapter;

    public NotaItemTouchHelperCallBack(ListaNotasAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //para deslizar sera permitido na direita e na esquerda
        int marcacoesDeDeslize = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        //dragFlags indica se queremos arrastar o objeto
        int marcacoesDeArrastar = ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        // o retorno indica a acao do usuario na tela ...
        return makeMovementFlags(marcacoesDeArrastar,marcacoesDeDeslize);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int posicaoDaNotaInicial = viewHolder.getAdapterPosition();
        int posicaoDaNotaFinal = target.getAdapterPosition();
        trocaNotas(posicaoDaNotaInicial, posicaoDaNotaFinal);

        return true;
    }

    private void trocaNotas(int posicaoDaNotaInicial, int posicaoDaNotaFinal) {
        new NotaDAO().troca(posicaoDaNotaInicial, posicaoDaNotaFinal);
        adapter.troca(posicaoDaNotaInicial, posicaoDaNotaFinal);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int posicaoDaNotaDeslizada = viewHolder.getAdapterPosition();
        removeNota(posicaoDaNotaDeslizada);
    }

    private void removeNota(int posicaoDaNotaDeslizada) {
        new NotaDAO().remove(posicaoDaNotaDeslizada);
        //remove do adapter
        adapter.remove(posicaoDaNotaDeslizada);
    }

}
