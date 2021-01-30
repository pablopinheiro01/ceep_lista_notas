package br.com.alura.ceep.ui.activity;

import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String APP_BAR_TITLE = "Notas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(APP_BAR_TITLE);
        setContentView(R.layout.lista_notas_activity);

        List<Nota> todasNotas = notasDeExemplo();

        configuraRecyclerView(todasNotas);

    }

    private List<Nota> notasDeExemplo() {
        NotaDAO dao = populaNotaDaoTeste();
        return dao.todos();
    }

    private NotaDAO populaNotaDaoTeste() {
        NotaDAO dao = new NotaDAO();
        for(int i = 0 ; i <= 2; i++){
            dao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i + " pequena descricao"));
            dao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i + " segunda descricao e bem maior do que a primeir "));
        }
        return dao;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);

        configuraAdapter(todasNotas, listaNotas);

        //podemos configurar o layout programaticamente ou via xml
       // listaNotas.setLayoutManager(configuraLayoutManager());
    }

    /*private LinearLayoutManager configuraLayoutManager() {
        //precisamos de um gerenciador de layout para carregar as views.
        return new LinearLayoutManager(this);
    }*/

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        listaNotas.setAdapter(new ListaNotasAdapter(todasNotas, this));
    }
}