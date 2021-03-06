package ir.armaani.hv.zabanak.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.models.Series;

/**
 * Created by Siamak on 18/07/2016.
 */
public class TranslateListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final List<String> itemsArrayList;
    public TranslateListAdapter(Context context, List<String> itemsArrayList) {

        super(context, R.layout.series_listview_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.translate_listview_layout, parent, false);

        TextView listText = (TextView) rowView.findViewById(R.id.textView);

        listText.setText(itemsArrayList.get(position));

        return rowView;
    }
}