package com.example.contactos;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txt_nombre;
    private EditText txt_telefono;
    private EditText txt_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_nombre= findViewById(R.id.txt_nombre);
        txt_telefono=findViewById(R.id.txt_telefono);
        txt_email=findViewById(R.id.txt_email);
    }
    public void Guardar(View view) {
        String nombre = txt_nombre.getText().toString();
        String telefono = txt_telefono.getText().toString();
        String email = txt_email.getText().toString();

        SharedPreferences preferences = getSharedPreferences("agenda", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String contactosJson = preferences.getString("lista_contactos", "[]");
        List<Map<String, String>> contactos = new ArrayList<>();
        try {
            contactos = new Gson().fromJson(contactosJson, new TypeToken<List<Map<String, String>>>() {}.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        Map<String, String> nuevoContacto = new HashMap<>();
        nuevoContacto.put("nombre", nombre);
        nuevoContacto.put("telefono", telefono);
        nuevoContacto.put("email", email);

        contactos.add(nuevoContacto);

        String nuevaListaContactosJson = new Gson().toJson(contactos);
        editor.putString("lista_contactos", nuevaListaContactosJson);
        editor.apply();

        Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show();
    }

    public void Leer(View view) {
        String nombre = txt_nombre.getText().toString();

        SharedPreferences preferences = getSharedPreferences("agenda", Context.MODE_PRIVATE);
        String contactosJson = preferences.getString("lista_contactos", "[]");

        try {
            List<Map<String, String>> contactos = new Gson().fromJson(contactosJson, new TypeToken<List<Map<String, String>>>() {}.getType());
            boolean contactoEncontrado = false;
            String telefono = "";
            String email = "";
            for (Map<String, String> contacto : contactos) {
                String nombreContacto = contacto.get("nombre");
                if (nombreContacto.equalsIgnoreCase(nombre)) {
                    telefono = contacto.get("telefono");
                    email = contacto.get("email");
                    contactoEncontrado = true;
                    break;
                }
            }

            if (contactoEncontrado) {
                txt_telefono.setText(telefono);
                txt_email.setText(email);
                Toast.makeText(this, "Datos leídos correctamente", Toast.LENGTH_LONG).show();
            } else {
                txt_telefono.setText("");
                txt_email.setText("");
                Toast.makeText(this, "Datos no encontrados", Toast.LENGTH_LONG).show();
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer los datos", Toast.LENGTH_LONG).show();
        }
    }

}