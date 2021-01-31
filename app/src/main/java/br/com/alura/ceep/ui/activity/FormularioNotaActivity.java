package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.alura.ceep.R;
import br.com.alura.ceep.model.Nota;

import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.IConstantesActivity.POSICAO_INVALIDA;

public class FormularioNotaActivity extends AppCompatActivity {

    public static final String INSERIR_NOTA = "Inserir Nota";
    public static final String ALTERA_NOTA = "Alterar Nota";
    //inicializado com uma posicao invalida
    private int posicaoRecebida = POSICAO_INVALIDA;
    private TextView titulo;
    private TextView descricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
        setTitle(INSERIR_NOTA);
        inicializaCampos();

        Intent dadosRecebidos = getIntent();

        if(dadosRecebidos.hasExtra(CHAVE_NOTA)){
            setTitle(ALTERA_NOTA);
            Nota notaRecebida = (Nota) dadosRecebidos.getSerializableExtra(CHAVE_NOTA);
            // -1 para o valor default no caso de receber vazio
            posicaoRecebida = dadosRecebidos.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

            preencheCampos(notaRecebida);

        }
    }
    private void inicializaCampos(){
        titulo = findViewById(R.id.formulario_nota_titulo);
        descricao = findViewById(R.id.formulario_nota_descricao);
    }

    private void preencheCampos(Nota nota) {
        titulo.setText(nota.getTitulo());
        descricao.setText(nota.getDescricao());
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
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida);

        setResult(Activity.RESULT_OK, resultadoInsercao);
    }

    private Nota criaNota() {
        //populo os EditTexts para criar a nota
         titulo = findViewById(R.id.formulario_nota_titulo);
         descricao = findViewById(R.id.formulario_nota_descricao);

       return new Nota(titulo.getText().toString(), descricao.getText().toString());
    }

    private boolean isMenuSalvaNota(int idDoItem) {
        return idDoItem == R.id.menu_formulario_salva;
    }
}