package com.example.segundoparcial;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, Handler.Callback {
    TextView txtVwUsuarios;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    View view;
    //String stringUsuarios = "[{'id':1,'username':'dtwigg','rol':'Supervisor','admin':true},{'id':2,'username':'ygagen','rol':'Supervisor','admin':false},{'id':3,'username':'ssutor','rol':'Construction Manager','admin':false},{'id':4,'username':'rdyter','rol':'Supervisor','admin':true},{'id':5,'username':'ahellwich','rol':'Project Manager','admin':false}][{'id':1,'username':'dtwigg','rol':'Supervisor','admin':true},{'id':2,'username':'ygagen','rol':'Supervisor','admin':false},{'id':3,'username':'ssutor','rol':'Construction Manager','admin':false},{'id':4,'username':'rdyter','rol':'Supervisor','admin':true},{'id':5,'username':'ahellwich','rol':'Project Manager','admin':false}]";
    JSONArray jsonUsuarios = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler handler = new Handler(Looper.myLooper(), this);
        HiloConexion hiloConn = new HiloConexion(handler);
        hiloConn.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        MenuItem menuItem = menu.findItem(R.id.mFiltrarUsuario);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mAgregarUsuario) {
            Log.d("Click", "Agregar");
            AlertDialog dialog = this.crearDialog();
            dialog.show();
            ListenerDialog ld = new ListenerDialog(this, this.view, dialog, this.pref, this.editor);
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(ld);
        } else if (item.getItemId() == R.id.mFiltrarUsuario) {
            Log.d("Click", "Buscar");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            String rol = "";
            String nombre = "";
            this.jsonUsuarios = new JSONArray(this.pref.getString("usuarios", null));
            for (int i = 0; i < this.jsonUsuarios.length(); i++) {
                JSONObject contacto = this.jsonUsuarios.getJSONObject(i);
                if (query.equals(contacto.getString("username"))) {
                    rol = contacto.getString("rol");
                    nombre = contacto.getString("username");
                    break;
                }
            }
            AlertDialog dialog;
            if (!"".equals(rol)) {
                dialog = this.lanzarAlertDialog("Usuario Encontrado", "El rol del usuario es " + rol);
            } else {
                dialog = this.lanzarAlertDialog("Usuario no encontrado", "El usuario "+query+" no esta dentro de la lista");
            }
            dialog.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public AlertDialog crearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Contacto");
        this.view = LayoutInflater.from(this).inflate(R.layout.dialog_nuevo_usuario, null);
        builder.setView(this.view);
        builder.setNeutralButton("Cancelar", null);
        builder.setPositiveButton("Guardar", null);
        return builder.create();
    }

    public AlertDialog lanzarAlertDialog(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        return builder.create();
    }

    @Override
    public boolean handleMessage(Message msg) {
        String usuarios = (String) msg.obj;
        this.txtVwUsuarios = super.findViewById(R.id.principalTxtVwUsuarios);
        this.pref = getPreferences(Context.MODE_PRIVATE);
        this.editor = this.pref.edit();
        //this.editor.remove("usuarios");
        //editor.commit();
        try {
            if (!this.pref.contains("usuarios")) {
                Log.d("usuarios","no existe el valor en pref");
                this.editor.putString("usuarios", usuarios);
                this.jsonUsuarios = new JSONArray(usuarios);
                this.txtVwUsuarios.setText(jsonUsuarios.toString());
            } else {
                Log.d("usuarios","si existe el valor en pref");
                Log.d("usuarios",this.pref.getString("usuarios",null));
                editor.putString("usuarios", usuarios);
                this.jsonUsuarios = new JSONArray(this.pref.getString("usuarios", usuarios));
                Log.d("array", jsonUsuarios.toString());
                this.txtVwUsuarios.setText(this.jsonUsuarios.toString());
            }
            this.editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }
}