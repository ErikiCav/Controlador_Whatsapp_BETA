package com.beta.controladorwhatsappbeta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        definirDados();
    }

    public void definirDados(){
        SharedPreferences preferenciasCompartilhadas = getSharedPreferences("dados_salvos", MODE_PRIVATE);
        EditText exibirApiLink = findViewById(R.id.editTextTextApiLink);
        EditText exibirPacotes = findViewById(R.id.editTextTextPacotesPermitidos);

        View permissaoResponderBoolean = findViewById(R.id.permitirResponder);

        exibirApiLink.setText(preferenciasCompartilhadas.getString("api_link", ""));
        exibirPacotes.setText(preferenciasCompartilhadas.getString("pacotes_permitidos",""));

        if(preferenciasCompartilhadas.getString("permitidoResponder","").contains("não")){
            permissaoResponderBoolean.setActivated(false);
        }else{
            permissaoResponderBoolean.setActivated(true);
        }
    }

    public void botaoAtalhoPermissaoNotificaticacao(View view){
        view.setClickable(true);
        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        Toast.makeText(this, String.format("Na lista, selecione o aplicativo com o nome de %s e permita-o a acessar suas notificações. Isto é necessário para que o aplicativo possa receber e responder suas notificacções do whatsapp.", getString(R.string.app_name)), Toast.LENGTH_LONG).show();
    }

    public void gerenciamentoApiLink(View view){
        EditText name = (EditText) view;
        SharedPreferences preferenciasCompartilhadas = getSharedPreferences("dados_salvos", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenciasCompartilhadas.edit();
        editor.putString("api_link",name.getText().toString()).apply();
        name.setText(preferenciasCompartilhadas.getString("api_link",""));
    }

    public void gerenciamentoPacotes(View view){
        EditText name = (EditText) view;
        SharedPreferences preferenciasCompartilhadas = getSharedPreferences("dados_salvos", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenciasCompartilhadas.edit();
        editor.putString("pacotes_permitidos",name.getText().toString()).apply();
        name.setText(preferenciasCompartilhadas.getString("pacotes_permitidos",""));
    }

    public void gerenciamentoPermissaoResponder(View view){
        SharedPreferences preferenciasCompartilhadas = getSharedPreferences("dados_salvos", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferenciasCompartilhadas.edit();

        if(view.isActivated()){
            view.setActivated(false);
            editor.putString("permitidoResponder", "O aplicativo não está permitido a responder suas notificações.").apply();
        }else{
            view.setActivated(true);
            editor.putString("permitidoResponder", "O aplicativo está permitido a responder suas notificações.").apply();
        }
        Toast.makeText(this, String.format("%s", preferenciasCompartilhadas.getString("permitidoResponder","")), Toast.LENGTH_SHORT).show();
    }

}