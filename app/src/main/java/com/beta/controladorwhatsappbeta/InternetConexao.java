package com.beta.controladorwhatsappbeta;

import android.os.AsyncTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InternetConexao extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... link) {
        try {
        OkHttpClient cliente = new OkHttpClient();

        Request request = new Request.Builder()
                .url(link[0])
                .build();
            Response resposta = cliente.newCall(request).execute();
            return resposta.body().string();
        } catch (Exception e) {
            String erro = "*Houve um erro ao tentar completar a solicitação, verifique se o link da API foi digitado corretamente!*";
            return erro;
        }
    }

}
