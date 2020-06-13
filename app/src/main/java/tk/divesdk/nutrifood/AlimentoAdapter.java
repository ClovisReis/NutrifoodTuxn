package tk.divesdk.nutrifood;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AlimentoAdapter extends ArrayAdapter<Alimento> {
    private final Context context;
    private final ArrayList<Alimento> elementos;

    public AlimentoAdapter(Context context, ArrayList<Alimento> elementos) {
        super(context, R.layout.item_list_view, elementos);
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list_view, parent, false);
        TextView nome = (TextView) rowView.findViewById(R.id.name_item);
        CircleImageView imagem = (CircleImageView) rowView.findViewById(R.id.profile_image);

        nome.setText(elementos.get(position).getNome());

        String imageUrl = elementos.get(position).getImagem();
        Picasso.get().load(imageUrl).into(imagem);

        return rowView;
    }
}