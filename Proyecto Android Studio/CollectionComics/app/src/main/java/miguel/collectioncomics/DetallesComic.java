package miguel.collectioncomics;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class DetallesComic extends AppCompatActivity {
    private TextView txtNombre;
    private TextView txtAutor;
    private TextView txtEditorial;
    private TextView txtDescripcion;
    private TextView txtEstado;
    private RatingBar txtValoracion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles_comic);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalles");
        setSupportActionBar(toolbar);

        //Asignamos los layout
        txtNombre = (TextView)findViewById(R.id.txtNombre);
        txtAutor = (TextView)findViewById(R.id.txtAutor);
        txtEditorial = (TextView)findViewById(R.id.txtEditorial);
        txtDescripcion=(TextView)findViewById(R.id.txtDescripcion);
        txtEstado = (TextView)findViewById(R.id.txtEstado);
        txtValoracion = (RatingBar)findViewById(R.id.txtValoracion);


        //Asignamos los datos
        Bundle bundle=getIntent().getExtras();
        txtNombre.setText(bundle.getString("nombre"));
        txtAutor.setText(bundle.getString("autor"));
        txtEditorial.setText(bundle.getString("editorial"));
        txtDescripcion.setText(bundle.getString("descripcion"));
        txtEstado.setText(bundle.getString("estado"));

        float rating = Float.parseFloat(bundle.getString("valoracion"));
        txtValoracion.setRating(rating);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.crearComic:
                intent.setClass(DetallesComic.this, CrearComic.class);
                startActivity(intent);
                finish(); // finaliza la actividad
                return true;
            case R.id.listarComics:
                intent.setClass(DetallesComic.this, ListarComics.class);
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

    //Este metodo recibe una cadena por parametro y la enseña por pantalla en forma de Toast
    public void msg(String msg){
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.aceptar));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.aceptar), new DetallesComic.OkOnClickListener());
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
}