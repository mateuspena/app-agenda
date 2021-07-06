package com.example.minhaagenda;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class NovoCompromisso extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_compromisso);


        Button button = (Button) findViewById(R.id.btn_confirmar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTitulo = (EditText) findViewById(R.id.edit_titulo);
                EditText editDescricao = (EditText) findViewById(R.id.edit_descricao);
                EditText editData = (EditText) findViewById(R.id.edit_data);
                EditText editHora = (EditText) findViewById(R.id.edit_hora);

                String titulo = editTitulo.getText().toString();
                String descricao = editDescricao.getText().toString();
                String data = editData.getText().toString();
                String hora = editHora.getText().toString();

                if (titulo.equals("") || descricao.equals("") || data.equals("") || hora.equals("")) {
                    Toast.makeText(
                            NovoCompromisso.this,
                            "Compromisso não foi salvo.",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Compromisso c = new Compromisso(NovoCompromisso.this);
                    c.salvar(titulo, descricao, data, hora);

                    int position = Calendario.retornaPosicaoData(LocalDate.parse(data));
                    Data objData = MainActivity.calendario.datas.get(position);
                    objData.setCompromissos( objData.getCompromissos() + 1);
                    MainActivity.calendario.datas.set(position, objData);

                    Toast.makeText(
                            NovoCompromisso.this,
                            "Compromisso salvo com sucesso!",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                finish();
            }
        });

    }
}