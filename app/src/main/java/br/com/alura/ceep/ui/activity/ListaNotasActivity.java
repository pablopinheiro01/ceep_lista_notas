package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.asynctask.BasicAsyncTask;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.database.CeepDatabase;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallBack;

import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.ERRO_POSICAO_NOTA_INVALIDA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String APP_BAR_TITLE = "Notas";
    private ListaNotasAdapter adapter;
    private NotaDAO notaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(APP_BAR_TITLE);
        setContentView(R.layout.lista_notas_activity);

        notaDAO = CeepDatabase.getInstance(this).getNotaDao();

        List<Nota> resultado = pegaTodasNotas();

        for(Nota nota: resultado){
            Log.i("NOTAONCREATE", nota.toString());
        }

        configuraRecyclerView(resultado);
        configuraBotaoInsereNota();
    }

    @Override
    protected void onResume() {
        Log.i("CHAMANDOONRESUME","CHAMANDOONRESUME");
        super.onResume();
//        adapter.atualiza(this.notaDAO.todos());
        new BasicAsyncTask<List<Nota>>(new BasicAsyncTask.ExecutaListener<List<Nota>>() {
            @Override
            public List<Nota> quandoExecuta() {
                Log.i("ASYNC ONRESUME", "executando...");
                List<Nota> notas =  notaDAO.todos();
                for(Nota n: notas){
                    Log.i("LISTAONR", n.toString());
                }
                return notas;
            }
        }, (resultado) -> {
            Log.i("ASYNC ONRESUME", "devolvendo na tela...");
            adapter.atualiza(resultado);
        }).execute();


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

        List<Nota> retorno = new ArrayList<>();

        new BasicAsyncTask<List<Nota>>( () -> {

             if(notaDAO.todos().size() == 0){
                 Log.i("Chamada assync", "Tamanho da nota: "+ notaDAO.todos().size());
                 return new ArrayList<Nota>();
             }else{
                 return notaDAO.todos();
             }

        }, resultado -> {
            retorno.addAll(resultado);
        }).execute();

        return retorno;
    }

    @Override //verifica se a requisicao pedida recebeu os parametros esperados
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(isResultadoInsereNota(requestCode, data)){
            Log.i("notarecebida: ", "Entrou em isResultadoInsereNota");
            if(isResultOk(resultCode)){
                Log.i("notarecebida: ", "Entrou em isResultOk");
                Nota notaRecebidaDoFormulario = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                Nota notaRecebida = new Nota(notaRecebidaDoFormulario.getTitulo(), notaRecebidaDoFormulario.getDescricao());
                Log.i("notarecebida: ", notaRecebida.toString());

                new BasicAsyncTask<Nota>(() -> {
                    Log.i("AsyncTask nota:", notaRecebida.toString());
                    long id = notaDAO.insere(notaRecebida);

                    notaDAO.atualizaOrdemDaNota(id, id);
                    Log.i("AsyncTask nota:", "Id da nota inserida: "+id);
                    Log.i("AsyncTask notainserida:", notaDAO.pesquisaNotaPorTitulo(notaRecebida.getTitulo()).toString() );
                    return notaDAO.pesquisaNotaPorPosicao(id);
                }, new BasicAsyncTask.FinalizadaListener<Nota>() {
                    @Override
                    public void quandoFinalizada(Nota resultado) {
                        adapter.adiciona(resultado);
                    }
                }).execute();

            }else if (resultCode == Activity.RESULT_CANCELED){
                //podemos tomar uma decisao para desfazer algo que foi realizado
            }
        }

        if(isResultadoAlteraNota(requestCode, data)){
            if(isResultOk(resultCode)){

                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                Log.i("NotaRecebida",notaRecebida.toString());
                if(isPosicaoValida(posicaoRecebida)){
                    altera(posicaoRecebida, notaRecebida);
                }else{
                    Toast.makeText(this, ERRO_POSICAO_NOTA_INVALIDA, Toast.LENGTH_LONG).show();
                }
            }else if( resultCode == Activity.RESULT_CANCELED){
                //toma uma acao caso o resultado nao seja positivo
            }
        }

    }

    private void altera(int posicao, Nota nota) {
//        new NotaDAO().altera(posicao, nota);

        new BasicAsyncTask<Nota>(new BasicAsyncTask.ExecutaListener<Nota>() {
            @Override
            public Nota quandoExecuta() {
                Log.i("AsyncTask","Nota recebida: "+nota.toString());
                notaDAO.altera(nota);
                List<Nota> notas = notaDAO.todos();
                Log.i("ITERANDO ASYNKTASK", "========================================================================");
                for(Nota nota: notas){
                    Log.i("AsyncTask",nota.toString());
                }
                Log.i("ITERANDO ASYNKTASK", "========================================================================");

                return null;
            }
        }, new BasicAsyncTask.FinalizadaListener<Nota>() {
            @Override
            public void quandoFinalizada(Nota resultado) {
                adapter.altera(posicao, nota);
            }
        }).execute();

    }

    private boolean isPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean isResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicaoAlteraNota(requestCode)
                && temNota(data);
    }

    private boolean isCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean isResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return isCodigoRequisicaoInsereNota(requestCode) &&
        temNota(data);
    }

    private boolean isCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private boolean isResultOk(int resultCode){
        return resultCode == Activity.RESULT_OK;
    }

    private boolean temNota(Intent data){
        return  data != null && data.hasExtra(CHAVE_NOTA);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        //configurando animacao
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallBack(adapter, notaDAO));
        itemTouchHelper.attachToRecyclerView(listaNotas);
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