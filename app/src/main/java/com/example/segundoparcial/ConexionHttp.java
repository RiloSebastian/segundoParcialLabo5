package com.example.segundoparcial;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConexionHttp {
    public String ObtenerRespuesta(String urlS) {
        try {
            URL url = new URL(urlS);
            HttpURLConnection httpConn = null;
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resp = httpConn.getResponseCode();
            if (resp == 200) {
                InputStream is = httpConn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int cant = 0;
                while ((cant = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, cant);
                }
                is.close();
                return baos.toString();

            } else {
                throw new RuntimeException("no se pudo establecer una connexion con el servidor");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
