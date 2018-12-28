package miguel.collectioncomics;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Main extends AppCompatActivity {
    private ImageButton ibCrear;
    private ImageButton ibListar;
    private ImageButton ibInfo;
    private ImageButton ibSalir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        ibCrear = (ImageButton)findViewById(R.id.ibCrear);
        ibListar = (ImageButton)findViewById(R.id.ibListar);
        ibInfo = (ImageButton) findViewById(R.id.ibInfo);
        ibSalir = (ImageButton)findViewById(R.id.ibSalir);

        ibCrear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, CrearComic.class);
                startActivity(i);
            }
        });

        ibListar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main.this, ListarComics.class);
                startActivity(i);
            }
        });

        ibInfo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        ibSalir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.crearComic:
                intent.setClass(Main.this, CrearComic.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.listarComics:
                intent.setClass(Main.this, ListarComics.class);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.aceptar));
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.aceptar), new OkOnClickListener());
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