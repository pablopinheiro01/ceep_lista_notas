package br.com.alura.ceep.ui.activity;

import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notas");
        setContentView(R.layout.lista_notas_activity);

        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);

        NotaDAO dao = new NotaDAO();
        for(int i = 0 ; i <= 20000; i++){
            dao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i));
        }

        List<Nota> todasNotas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(todasNotas, this));
        //precisamos de um gerenciador de layout para carregar as views.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        listaNotas.setLayoutManager(layoutManager);

    }
}