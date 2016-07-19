package ir.armaani.hv.zabanak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.adapters.SeriesListViewAdapter;
import ir.armaani.hv.zabanak.exceptions.AlreadyStartedLearningException;
import ir.armaani.hv.zabanak.exceptions.DependedPackageNotLearnedYetException;
import ir.armaani.hv.zabanak.models.Package;
import ir.armaani.hv.zabanak.models.Series;

public class MainActivity extends AppCompatActivity {

    RelativeLayout add;
    ImageView help;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar, null);
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        final SeriesListViewAdapter adapter = new SeriesListViewAdapter(this, Series.getListOfSeries());
        ListView listview = (ListView)findViewById(R.id.SerieslistView);
        listview.setAdapter(adapter);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.down_from_top);
        listview.setLayoutAnimation(new LayoutAnimationController(animation));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(MainActivity.this, PakageActivity.class);
                myIntent.putExtra("series", adapter.getItem(position)); //Optional parameters
                myIntent.putExtra("seriesId" , adapter.getItem(position).getId());
                MainActivity.this.startActivity(myIntent);
            }
        });

        add = (RelativeLayout)findViewById(R.id.AddSeriesLayout);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, StoreActivity.class);
                MainActivity.this.startActivity(myIntent);

            }
        });
        help = (ImageView) findViewById(R.id.help_btm);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, HelpActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }

}

