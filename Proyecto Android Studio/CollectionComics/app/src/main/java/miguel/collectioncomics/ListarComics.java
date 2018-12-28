package miguel.collectioncomics;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListarComics extends AppCompatActivity {
    private ListView lstComics;
    private ArrayList<Comic> comics ;
    private AdaptadorListaComics adaptadorListaComics;
    private int comicSeleccionado;

    public static final String URL_GET_COMIC = "http://172.19.179.79/comics/GetComics.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_comics);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Listar");
        setSupportActionBar(toolbar);

        lstComics = (ListView) findViewById(R.id.lstComics);
        registerForContextMenu(lstComics);

        comics = new ArrayList<>();

        adaptadorListaComics = new AdaptadorListaComics(ListarComics.this, comics);
        lstComics.setAdapter(adaptadorListaComics);
        registerForContextMenu(lstComics);
        adaptadorListaComics.notifyDataSetChanged();

        lstComics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                Intent i = new Intent(ListarComics.this, DetallesComic.class);
                i.putExtra("nombre", comics.get(posicion).getNombre());
                i.putExtra("autor", comics.get(posicion).getAutor());
                i.putExtra("editorial", comics.get(posicion).getEditorial());
                i.putExtra("descripcion", comics.get(posicion).getDescripcion());
                i.putExtra("estado", comics.get(posicion).getEstado());
                i.putExtra("valoracion", comics.get(posicion).getValoracion());
                startActivity(i);
            }
        });

        getComics();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_listar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.crearComic:
                intent.setClass(ListarComics.this, CrearComic.class);
                startActivity(intent);
                finish();
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

    //Este metodo recibe una cadena por parametro y la enseña por pantalla en forma de Toast
    public void msg(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        comicSeleccionado = info.position;
    }

    public boolean onContextItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.verDetalles:
                Intent i = new Intent(this, DetallesComic.class);
                i.putExtra("nombre", comics.get(comicSeleccionado).getNombre());
                i.putExtra("autor", comics.get(comicSeleccionado).getAutor());
                i.putExtra("editorial",comics.get(comicSeleccionado).getEditorial());
                i.putExtra("descripcion", comics.get(comicSeleccionado).getDescripcion());
                i.putExtra("estado", comics.get(comicSeleccionado).getEstado());
                i.putExtra("valoracion",comics.get(comicSeleccionado).getValoracion());
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.aceptar));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.aceptar), new ListarComics.OkOnClickListener());
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

    private void getComics() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.espera));
        progressDialog.show();
        comics.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_COMIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (Objects.equals(obj.getString("error"), "false")) {
                                JSONArray arr = obj.getJSONArray("comic");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject jso = arr.getJSONObject(i);
                                    Comic comic = new Comic();

                                    comic.setNombre(jso.getString("nombre"));
                                    comic.setAutor(jso.getString("autor"));
                                    comic.setEditorial(jso.getString("editorial"));
                                    comic.setDescripcion(jso.getString("descripcion"));
                                    comic.setEstado(jso.getString("estado"));
                                    comic.setValoracion(jso.getString("valoracion"));
                                    Log.e("comicsGestor1", "comicssize"+comics.size());
                                    comics.add(comic);
                                    Log.e("comicsGestor2", "comicssize"+comics.size());

                                }
                                adaptadorListaComics = new AdaptadorListaComics(ListarComics.this, comics);
                                lstComics.setAdapter(adaptadorListaComics);
                                registerForContextMenu(lstComics);
                                adaptadorListaComics.notifyDataSetChanged();

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

                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
