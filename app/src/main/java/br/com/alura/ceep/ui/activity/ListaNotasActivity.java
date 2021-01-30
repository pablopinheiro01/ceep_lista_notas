package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_RESULTADO_NOTA_CRIADA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String APP_BAR_TITLE = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(APP_BAR_TITLE);
        setContentView(R.layout.lista_notas_activity);

        List<Nota> notas = pegaTodasNotas();

        configuraRecyclerView(notas);

        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivity();
            }
        });
    }

    private void vaiParaFormularioNotaActivity() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        //startActivity(iniciaFormularioNota);
        //criado uma activity enviando um resultado
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        return dao.todos();
    }

    @Override //verifica se a requisicao pedida recebeu os parametros esperados
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(isResultadoComNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adapter.adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isResultadoComNota(int requestCode, int resultCode, @Nullable Intent data) {
        return isCodigoRequisicaoInsereNota(requestCode) &&
        isCodigoResultadoNotaCriada(resultCode) &&
        temNota(data);
    }

    private boolean isCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private boolean isCodigoResultadoNotaCriada(int resultCode){
        return resultCode == CODIGO_RESULTADO_NOTA_CRIADA;
    }

    private boolean temNota(Intent data){
        return data.hasExtra(CHAVE_NOTA);
    }
    
    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(todasNotas, this);
        listaNotas.setAdapter(adapter);
    }

}