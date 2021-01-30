package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String APP_BAR_TITLE = "Notas";
    private ListaNotasAdapter adapter;
    private List<Nota> todasNotas;
    private NotaDAO notaDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(APP_BAR_TITLE);
        setContentView(R.layout.lista_notas_activity);

        todasNotas = notasDeExemplo();

        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                 //startActivity(iniciaFormularioNota);
                //criado uma activity enviando um resultado
                startActivityForResult(iniciaFormularioNota, 1);
            }
        });
    }

    @Override //verifica se a requisicao pedida recebeu os parametros esperados
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == 2 && data.hasExtra("nota")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            adapter.adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private List<Nota> notasDeExemplo() {
        NotaDAO todasNotas = populaNotaDaoTeste();

        return todasNotas.todos();
    }

    private NotaDAO populaNotaDaoTeste() {
        notaDao = new NotaDAO();
        for(int i = 0 ; i <= 2; i++){
            notaDao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i + " pequena descricao"));
            notaDao.insere(new Nota("Nota de numero: "+i, "descricao de numero: "+i + " segunda descricao e bem maior do que a primeir "));
        }
        return notaDao;
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
        adapter = new ListaNotasAdapter(todasNotas, this);
        listaNotas.setAdapter(adapter);
    }

    public void adiciona(Nota nota){
        this.todasNotas.add(nota);
        notifyAll();
    }
}