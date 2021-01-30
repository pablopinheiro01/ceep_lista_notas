package br.com.alura.ceep.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.adapter.ListaNotasAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notas");
        setContentView(R.layout.lista_notas_activity);

        ListView listaNotas = findViewById(R.id.listView);

        NotaDAO dao = new NotaDAO();
        for(int i = 0 ; i <= 20000; i++){
            dao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i));
        }

        List<Nota> todasNotas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));



    }
}