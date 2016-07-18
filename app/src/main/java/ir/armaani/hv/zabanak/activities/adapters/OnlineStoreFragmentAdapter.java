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
import ir.armaani.hv.zabanak.helpers.ImageDownloader;
import ir.armaani.hv.zabanak.rest.responses.SeriesSummary;

/**
 * Created by Amirhossein on 08/07/2016.
 */
public class OnlineStoreFragmentAdapter extends ArrayAdapter<SeriesSummary> {

    private final Context context;
    private final List<SeriesSummary> itemsArrayList;
    public OnlineStoreFragmentAdapter(Context context, List<SeriesSummary> itemsArrayList) {

        super(context, R.layout.series_listview_layout, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SeriesSummary thisItem = itemsArrayList.get(position);
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.series_listview_layout, parent, false);

        // 3. Get the two text view from the rowView
        final ImageView seriesImage = (ImageView) rowView.findViewById(R.id.seriesImage);
        final ImageView seriesImageBackground = (ImageView) rowView.findViewById(R.id.seriesImageBack);
        TextView seriesCaption = (TextView) rowView.findViewById(R.id.seriesCaptipn);
        TextView pakageCount = (TextView)rowView.findViewById(R.id.packageCount);

        // 4. Set the text for textView

        ImageDownloader.downloadImageOnTheFly(thisItem.image , seriesImage);
        ImageDownloader.downloadImageOnTheFly(thisItem.image , seriesImageBackground);
        pakageCount.setText(String.valueOf(thisItem.download_count)+ " بار دریافت شده");
        seriesCaption.setText(thisItem.name);

        // 5. retrn rowView
        return rowView;
    }
}

