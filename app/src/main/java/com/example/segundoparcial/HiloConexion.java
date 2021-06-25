package com.example.segundoparcial;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HiloConexion extends Thread {
    Handler handler;
    Message msg;

    public HiloConexion(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        ConexionHttp connHttp = new ConexionHttp();
        String s = connHttp.ObtenerRespuesta("http://10.0.2.2:3001/usuarios");
        msg = new Message();
        msg.obj = s;
        Log.d("mensaje", msg.obj.toString());
        handler.sendMessage(msg);
    }
}
