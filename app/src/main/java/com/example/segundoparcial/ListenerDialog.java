package com.example.segundoparcial;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListenerDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Activity a;
    View v;
    AlertDialog dialog;
    EditText EdtTxtNombreUsuario;
    Spinner SpnrRolUsuario;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String valorSpinner;
    ArrayAdapter aa;
    ToggleButton tglBnAdminUsuario;

    public ListenerDialog(Activity a, View v, AlertDialog dialog, SharedPreferences pref, SharedPreferences.Editor editor) {
        this.a = a;
        this.v = v;
        this.dialog = dialog;
        this.pref = pref;
        this.editor = editor;

        this.tglBnAdminUsuario = this.v.findViewById(R.id.TglBtnAminUsuario);
        this.EdtTxtNombreUsuario = this.v.findViewById(R.id.EdtTxtNombreUsuario);
        this.SpnrRolUsuario = this.v.findViewById(R.id.SpnrRolUsuario);
        this.aa = ArrayAdapter.createFromResource(a, R.array.valueSpinner, R.layout.spinner_txt_view);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpnrRolUsuario.setAdapter(aa);
        SpnrRolUsuario.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        if (!"".equals(this.EdtTxtNombreUsuario.getText().toString())) {
            try {
                String stringContacto = "{'username':'" + this.EdtTxtNombreUsuario.getText() + "','rol':'" + valorSpinner + "', 'admin': '" + tglBnAdminUsuario.isChecked() + "'}";
                JSONObject usuario = new JSONObject(stringContacto);
                JSONArray jsonUsuarios = new JSONArray(this.pref.getString("usuarios", null));
                jsonUsuarios.put(usuario);
                this.editor.putString("usuarios", jsonUsuarios.toString());
                this.editor.commit();
                TextView txtVwUsuarios = this.a.getWindow().getDecorView().findViewById(R.id.principalTxtVwUsuarios);
                txtVwUsuarios.setText(this.pref.getString("usuarios", null));
                this.dialog.dismiss();
                Toast.makeText(this.a, "Contacto " + usuario.getString("nombre") + " agregado!", Toast.LENGTH_LONG).show();
                Log.d("nuevo usuario:", usuario.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this.a, "Se deben llenar todos los campos.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        valorSpinner = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
}
