package com.beta.controladorwhatsappbeta;

import android.app.PendingIntent;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class ControleNotificacoes extends NotificationListenerService {

    //Dispara quando uma nova mensagem é recebida.
    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        if(!mensagemRepetida(sbn) && pacotePermitido(sbn) && permitidoResponder(sbn)){
            try {
                responderNotificacao(sbn);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    //Responsável por responder as notificações pendentes/novas.
    private void responderNotificacao(StatusBarNotification sbn) throws PendingIntent.CanceledException {
        ArrayList retorno = null;
        String retornoApi = null;
        try {
            retornoApi = new InternetConexao().execute(String.format(getSharedPreferences("dados_salvos", MODE_PRIVATE).getString("api_link", "")+"?numero=%s&&texto=%s", numeroTelefone(sbn),mensagemTexto(sbn))).get().replace("QUEBRALINHA","\n");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(!retornoApi.equals("sem_retorno_x01Bt4TY2d")){
            retorno = new Intencoes().extrair(sbn, retornoApi);
            PendingIntent intencoesPendentes = (PendingIntent) retorno.get(0);
            Intent pacoteLocal = (Intent) retorno.get(1);
            intencoesPendentes.send(this, 0, pacoteLocal);
        }
    }

    //Retorna a mensagem de texto que foi recebida.
    private String mensagemTexto(StatusBarNotification sbn){
        return sbn.getNotification().extras.getString("android.text");
    }

    //Retorna o número de telefone do usuário que enviou a mensagem.
    private String numeroTelefone(StatusBarNotification sbn){
        try{
            if(!mensagemRepetida(sbn)){
                return sbn.getNotification().extras.getString("android.title").split("\\+55", 2)[1];
            }
        } catch (Exception ignored){
        }
        return null;
    }

    //Checa se o pacote do aplicativo que notificou o usuário está na lista de permitidos.
    private Boolean pacotePermitido(StatusBarNotification sbn){
        String pacotename = sbn.getPackageName();
        String[] listaPacotes = getSharedPreferences("dados_salvos", MODE_PRIVATE).getString("pacotes_permitidos", "").split(" ");
        for(String pacote: listaPacotes){
            System.out.println(pacotename);
            if(pacote.toLowerCase().equals(pacotename)){
                return true;
            }
        }
        return false;
    }

    //Checa se a notificação está repetida.
    private Boolean mensagemRepetida(StatusBarNotification sbn){
        return sbn.getKey().split("\\|")[3].toLowerCase().equals("null");
    }

    //Checa se o bot pode responder as mensagens.
    private Boolean permitidoResponder(StatusBarNotification sbn){
        if(getSharedPreferences("dados_salvos", MODE_PRIVATE).getString("permitidoResponder", "").contains("não")){
            return false;
        }else{
            return true;
        }
    }
}
