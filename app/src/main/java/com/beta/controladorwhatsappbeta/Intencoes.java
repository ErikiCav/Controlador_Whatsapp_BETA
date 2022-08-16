package com.beta.controladorwhatsappbeta;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import java.util.ArrayList;
import java.util.List;

public class Intencoes {

    //Responsável por extrair intenções, ignorar intenções vazias (intenções vazias são notificações repetidas em uma mesma chamada) , adicionar compatibilidade para responder notificações e salvar.
    public ArrayList extrair(StatusBarNotification sbn, String respostaApi) {

        NotificationCompat.Action armazenamentoAcoes = null;
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender(sbn.getNotification());
        List<NotificationCompat.Action> acoes = wearableExtender.getActions();

        Intent intencaoLocal = new Intent();
        intencaoLocal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle pacoteLocal = new Bundle();

        List<RemoteInput> entradasRemotas = new ArrayList<>(acoes.size());
        PendingIntent intencoesPendentes = null;
        ArrayList armazenamento = new ArrayList();

        for (NotificationCompat.Action acoesFinal : acoes) {
            if (acoesFinal.getRemoteInputs() != null) {
                for (int tamanho = 0; tamanho < acoesFinal.getRemoteInputs().length; tamanho++) {
                    RemoteInput entradaRemota = acoesFinal.getRemoteInputs()[tamanho];
                    entradasRemotas.add(entradaRemota);
                    intencoesPendentes = acoesFinal.actionIntent;
                    armazenamentoAcoes = acoesFinal;
                }
                armazenamento.add(intencoesPendentes);
                armazenamento.add(intencaoLocal);
            }
        }

        int vezesRodadas = 0;
        RemoteInput[] listaEntradas = new RemoteInput[armazenamentoAcoes.getRemoteInputs().length];
        for (RemoteInput entradaRemotaTemporaria : armazenamentoAcoes.getRemoteInputs()) {
            listaEntradas[vezesRodadas] = entradaRemotaTemporaria;
            pacoteLocal.putCharSequence(listaEntradas[vezesRodadas].getResultKey(), respostaApi);
            vezesRodadas++;
        }
        RemoteInput.addResultsToIntent(listaEntradas, intencaoLocal, pacoteLocal);
        return armazenamento;
    }
}