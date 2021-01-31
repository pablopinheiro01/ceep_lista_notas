package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CODIGO_RESULTADO_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    private int posicaoRecebida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);

        Intent dadosRecebidos = getIntent();

        if(dadosRecebidos.hasExtra(CHAVE_NOTA) && dadosRecebidos.hasExtra("posicao")){
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            // -1 para o valor default no caso de receber vazio
            posicaoRecebida = dadosRecebidos.getIntExtra("posicao", -1);

            TextView titulo = findViewById(R.id.formulario_nota_titulo);
            titulo.setText(notaRecebida.getTitulo());

            TextView descricao = findViewById(R.id.formulario_nota_descricao);
            descricao.setText(notaRecebida.getDescricao());

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int idDoItem = item.getItemId();
        if(isMenuSalvaNota(idDoItem)){
            Nota nota = criaNota();
            retornaNota(nota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void retornaNota(Nota nota) {
        //crio uma activity para aguardar um resultado.
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        resultadoInsercao.putExtra("posicao", posicaoRecebida);

        setResult(CODIGO_RESULTADO_NOTA_CRIADA, resultadoInsercao);
    }

    private Nota criaNota() {
        //populo os EditTexts para criar a nota
        EditText titulo = findViewById(R.id.formulario_nota_titulo);
        EditText descricao = findViewById(R.id.formulario_nota_descricao);

       return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean isMenuSalvaNota(int idDoItem) {
        return idDoItem == R.id.menu_formulario_salva;
    }
}