package br.com.alura.ceep.dao;

import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import br.com.alura.ceep.model.Nota;

@Dao
public abstract class NotaDAO {

    @Query("SELECT * FROM nota ORDER BY nota.ordem ASC")
    public abstract List<Nota> todos();

    @Insert
    public abstract long insere(Nota nota);

    @Update
    public abstract void altera(Nota nota);

    @Update
    public abstract void alteraLista(List<Nota> notas);

//    @Delete
//    public abstract void remove(int notaId);
//
//    @Update
//    public abstract void alteraOrdem(Nota nota);

    @Query("UPDATE nota SET ordem = :ordem WHERE id = :id")
    public abstract void atualizaOrdemDaNota(Long id, Long ordem);

    @Query("SELECT * FROM nota WHERE ordem = :posicao")
    public abstract Nota pesquisaNotaPorPosicao(Long posicao);

    @Query("SELECT * FROM nota WHERE nota.titulo = :titulo")
    public abstract Nota pesquisaNotaPorTitulo(String titulo);

    @Query("SELECT * FROM nota WHERE nota.titulo = :id")
    public abstract Nota pesquisaNotaPorId(Long id);

    @Transaction
    public void troca(int posicaoInicio, int posicaoFim){
        Log.i("TROCA DAO","REALIZANDO A TROCA DAS POSICOES E REDEFINICAO DA LISTA");
        Long primeiraPosicao = Long.valueOf(posicaoInicio+1);
        Log.i("TROCA DAO","PRIMEIRA POSICAO: " + primeiraPosicao);
        Long segundaPosicao = Long.valueOf(posicaoFim+1);
        Log.i("TROCA DAO","SEGUNDA POSICAO: "+segundaPosicao);


        Nota primeiraNota = pesquisaNotaPorPosicao(primeiraPosicao);
        Log.i("PRIMEIRA NOTA","PRIMEIRA NOTA RECEBE A POSICAO: "+segundaPosicao);
        Log.i("PRIMEIRA NOTA", "ESSA NOTA E A : "+primeiraNota.toString());
        primeiraNota.setOrdem(segundaPosicao);
        Log.i("PRIMEIRA NOTA", "NOVA NOTA REPOSICIONADA : "+primeiraNota.toString());

        Nota segundaNota = pesquisaNotaPorPosicao(segundaPosicao);
        Log.i("SEGUNDA NOTA","SEGUNDA NOTA RECEBE A POSICAO: "+primeiraPosicao);
        Log.i("SEGUNDA NOTA", "ESSA NOTA E A : "+segundaNota.toString());
        segundaNota.setOrdem(primeiraPosicao);
        Log.i("SEGUNDA NOTA", "NOVA NOTA REPOSICIONADA : "+segundaNota.toString());


        altera(primeiraNota);
        altera(segundaNota);

        List<Nota> notas = todos();
        Log.i("ORDENANDO AS NOTAS", "===========================ORDEM ATUAL DAS NOTAS ====================================");
        for(Nota nota: notas){
            Log.i("NOTAS:", nota.toString());
        }
        Log.i("ORDENANDO AS NOTAS", "===========================ORDEM ATUAL DAS NOTAS ====================================");
    }

    @Delete
    public abstract void remove(Nota nota);

    @Query("SELECT * FROM nota WHERE nota.ordem = :posicaoDaNotaDeslizada")
    public void remove(int posicaoDaNotaDeslizada){

        List<Nota> notas = todos();

        notas = removeNotaDaLista(posicaoDaNotaDeslizada, notas);

        alteraOrdemDasNotas(notas);
    }

    private List<Nota> removeNotaDaLista(int posicaoDaNotaDeslizada, List<Nota> notas) {
        Log.i("REMOVE", "POSICAO RECEBIDA: "+posicaoDaNotaDeslizada);

        Log.i("ORDENANDO AS NOTAS", "===========================ORDEM ATUAL DAS NOTAS ====================================");
        for(Nota nota: notas){
            Log.i("NOTAS:", nota.toString());
        }
        Log.i("ORDENANDO AS NOTAS", "===========================ORDEM ATUAL DAS NOTAS ====================================");

        int posicaoDaTela = posicaoDaNotaDeslizada + 1;
        Nota notaDaPosicaoDeslizada = pesquisaNotaPorPosicao(Long.valueOf(posicaoDaTela));

        Log.i("REMOVE", "Nota capturada que sera removida "+notaDaPosicaoDeslizada.toString());

        notas.remove(posicaoDaNotaDeslizada);
        remove(notaDaPosicaoDeslizada);

        return notas;
    }

    private void alteraOrdemDasNotas(List<Nota> notas) {
        Collections.sort(notas, new Comparator<Nota>() {
            @Override
            public int compare(Nota o1, Nota o2) {
                if(o1.getOrdem() < o2.getOrdem()){
                    return -1;
                }else if(o1.getOrdem() > o2.getOrdem()){
                    return 1;
                }
                    return 0;
            }
        });

        for(int i = 0; i < notas.size(); i++){
            int novaOrdem = i + 1;
            notas.get(i).setOrdem(Long.valueOf(novaOrdem));
        }

        Log.i("NOTAS ORDENADAS", "===========================NOTAS REORDENADAS ====================================");
        for(Nota nota: notas){
            Log.i("NOTAS ORDENADAS:", nota.toString());
        }
        Log.i("ORDENANDO AS NOTAS", "=========================== NOTAS REORDENADAS ====================================");

        alteraLista(notas);
    }

//    @Query("DROP TABLE nota")
//    public abstract void removeTodos();

}
