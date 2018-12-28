package miguel.collectioncomics;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CrearComic extends AppCompatActivity {

    private EditText txtNombre;
    private EditText txtAutor;
    private Spinner spEditorial;
    private EditText txtDescripcion;
    private Spinner spEstado;
    private RatingBar txtValoracion;

    private Button btnAceptar;

    public static final String URL_INSERT_COMIC = "http://172.19.179.79/comics/InsertComic.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_comic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Crear");
        setSupportActionBar(toolbar);

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtAutor = (EditText) findViewById(R.id.txtAutor);
        spEditorial = (Spinner) findViewById(R.id.spEditorial);
        txtDescripcion = (EditText) findViewById(R.id.txtDescripcion);
        spEstado = (Spinner) findViewById(R.id.spEstado);
        txtValoracion = (RatingBar) findViewById(R.id.txtValoracion);

        ArrayAdapter adapterEditorial = ArrayAdapter.createFromResource(this, R.array.editorial, android.R.layout.simple_spinner_item);
        adapterEditorial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEditorial.setAdapter(adapterEditorial);

        ArrayAdapter adapterEstado = ArrayAdapter.createFromResource(this, R.array.estado, android.R.layout.simple_spinner_item);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapterEstado);

        btnAceptar = (Button) findViewById(R.id.btnAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("Crear", "btnAceptar");
                if (getServicio()) {
                    insertComic(
                            txtNombre.getText().toString(),
                            txtAutor.getText().toString(),
                            spEditorial.getSelectedItem().toString(),
                            txtDescripcion.getText().toString(),
                            spEstado.getSelectedItem().toString(),
                            String.valueOf(txtValoracion.getRating())
                    );
                } else {
                    //Si no dispone de internet, mostramos el mensaje:
                    msg("No dispone de conexion a internet en estos momentos.");
                    //Ademas, devolvemos al Main al usuario:
                    Intent intento = new Intent(CrearComic.this, Menu.class);
                    startActivity(intento);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.listarComics:
                intent.setClass(CrearComic.this, ListarComics.class);
                startActivity(intent);
                finish(); // finaliza la actividad
                return true;
            case R.id.info:
                showDialog(1);
                return true;
            case R.id.salir:
                finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean comprobar() {
        if (txtNombre.getText().length() > 0 && txtDescripcion.getText().length() > 0)
            return true; //Se puede
        else
            return false; //No se puede
    }

    public void msg(String msg) {
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean getServicio() {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isAvailable() && ni.isConnected())
            return true;
        else
            return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.aceptar));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.aceptar), new CrearComic.OkOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();
        }
        return super.onCreateDialog(id);
    }

    private final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    private void insertComic(final String nombre, final String autor, final String editorial,
                                   final String descripcion, final String estado, final String valoracion) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.espera));
        progressDialog.show();
        Log.e("GestorComics", "insert");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_INSERT_COMIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            Log.e("Gestor", "response" + response);
                            JSONObject obj = new JSONObject(response);
                            if (Objects.equals(obj.getString("error"), "false")) {
                                Intent intent = new Intent();
                                intent.setClass(CrearComic.this, Main.class);
                                startActivity(intent);
                                finish(); // finaliza la actividad
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombre", nombre);
                params.put("autor", autor);
                params.put("editorial", editorial);
                params.put("descripcion", descripcion);
                params.put("estado", estado);
                params.put("valoracion", valoracion);
                return params;
            }
        };
        Log.e("Gestor", "stringRequest" + stringRequest);
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}