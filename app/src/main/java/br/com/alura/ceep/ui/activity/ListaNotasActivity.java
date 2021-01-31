package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_RESULTADO_NOTA_CRIADA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.ERRO_POSICAO_NOTA_INVALIDA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.POSICAO_INVALIDA;

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
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        //startActivity(iniciaFormularioNota);
        //criado uma activity enviando um resultado
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for(int i = 0; i <= 10; i++){
            dao.insere(new Nota("Titulo "+ i," Descricao de numero "+i));
        }
        return dao.todos();
    }

    @Override //verifica se a requisicao pedida recebeu os parametros esperados
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(isResultadoInsereNota(requestCode, resultCode, data)){
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
            adapter.adiciona(notaRecebida);
        }

        if(isResultadoAlteraNota(requestCode, data)){

            int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
            Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);

            if(isPosicaoValida(posicaoRecebida)){
                altera(posicaoRecebida, notaRecebida);
            }else{
                Toast.makeText(this, ERRO_POSICAO_NOTA_INVALIDA, Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void altera(int posicao, Nota nota) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean isPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean isResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicaoAlteraNota(requestCode)
                &&  isCodigoResultadoNotaCriada(CODIGO_RESULTADO_NOTA_CRIADA)
                && temNota(data);
    }

    private boolean isCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean isResultadoInsereNota(int requestCode, int resultCode, @Nullable Intent data) {
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
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        //caso esta chamada seja comentada nao receberemos um nota com o valor valido
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }

}