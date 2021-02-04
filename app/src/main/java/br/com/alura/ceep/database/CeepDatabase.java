package br.com.alura.ceep.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

@Database(entities = {Nota.class}, version = 1, exportSchema = false)
public abstract class CeepDatabase extends RoomDatabase {

    public static final String NOME_BASE_DADOS = "ceep.db";

    public abstract NotaDAO getNotaDao();

    public static CeepDatabase getInstance(Context context){

        return Room.databaseBuilder(context, CeepDatabase.class, NOME_BASE_DADOS)
                .build();
    }

}
