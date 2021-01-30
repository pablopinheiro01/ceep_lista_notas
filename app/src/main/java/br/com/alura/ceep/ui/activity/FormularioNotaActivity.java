package br.com.alura.ceep.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idDoItem = item.getItemId();
        if(idDoItem == R.id.menu_formulario_salva){
            EditText titulo = findViewById(R.id.formulario_nota_titulo);
            EditText descricao = findViewById(R.id.formulario_nota_descricao);

            Nota nota = new Nota(titulo.getText().toString(), descricao.getText().toString());
            new NotaDAO().insere(nota);

            //crio uma activity para aguardar um resultado.
            Intent resultadoInsercao = new Intent();
            resultadoInsercao.putExtra("nota", nota);
            setResult(2, resultadoInsercao);

            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}