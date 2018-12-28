package miguel.collectioncomics;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorListaComics extends ArrayAdapter<Comic> {

    private Activity context;
    private ArrayList<Comic> comics;

    public AdaptadorListaComics(Activity context, ArrayList<Comic> comics) {
        super(context, R.layout.comic, comics);
        this.context = context;
        this.comics = comics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(R.layout.comic, null);

        CardView card = (CardView) v.findViewById(R.id.card);
        String estado = comics.get(position).getEstado();
        switch (estado) {
            case "Pendiente":
                card.setCardBackgroundColor(Color.CYAN);
                break;
            case "Acabado":
                card.setCardBackgroundColor(Color.GREEN);
                break;
            case "Empezado":
                card.setCardBackgroundColor(Color.GRAY);
                break;
        }


        TextView lblNombre = (TextView) v.findViewById(R.id.lblNombre);
        lblNombre.setText(comics.get(position).getNombre());

        RatingBar lblValoracion = (RatingBar) v.findViewById(R.id.lblValoracion);
        float rating = Float.parseFloat(comics.get(position).getValoracion());
        lblValoracion.setRating(rating);

        ImageView ivImagen = (ImageView) v.findViewById(R.id.ivImagen);
        String editorial = comics.get(position).getEditorial();
        switch (editorial) {
            case "Boom!":
                ivImagen.setImageResource(R.drawable.boom);
                break;
            case "Dark Horse":
                ivImagen.setImageResource(R.drawable.dark_horse);
                break;
            case "DC":
                ivImagen.setImageResource(R.drawable.dc);
                break;
            case "Image":
                ivImagen.setImageResource(R.drawable.image);
                break;
            case "Marvel":
                ivImagen.setImageResource(R.drawable.marvel);
                break;
            case "Valiant":
                ivImagen.setImageResource(R.drawable.valiant);
                break;
            case "Vertigo":
                ivImagen.setImageResource(R.drawable.vertigo);
                break;
            case "Zenescope":
                ivImagen.setImageResource(R.drawable.zenescope);
                break;
        }

        return v;
    }

}

