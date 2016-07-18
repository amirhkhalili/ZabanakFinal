package ir.armaani.hv.zabanak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.exceptions.AlreadyStartedLearningException;
import ir.armaani.hv.zabanak.exceptions.DependedPackageNotLearnedYetException;
import ir.armaani.hv.zabanak.models.Package;
import ir.armaani.hv.zabanak.models.Series;

public class PakageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pakage);
        Intent intent = getIntent();
        Series seriesItem = (Series) intent.getSerializableExtra("series");
        if (seriesItem.getId()==null)
            seriesItem.setId(intent.getLongExtra("seriesId" , 0));
        ImageView packagePoster = (ImageView)findViewById(R.id.packagePoster);
        packagePoster.setImageBitmap(seriesItem.getImage());
//        packagePoster.setImageResource(R.mipmap.gladiator);
        TextView seriesCaption = (TextView)findViewById(R.id.seriesCaptionInPackageActivity);
        TextView seriesPackageCount = (TextView)findViewById(R.id.seriesPackageCountInPackageActivity);
        seriesPackageCount.setText(String.valueOf(seriesItem.getPackageCount())+" بسته");
        seriesCaption.setText(seriesItem.getCaption());
        final PackageListViewAdapter adapter = new PackageListViewAdapter(this,seriesItem.getPackages());
        ListView listView = (ListView)findViewById(R.id.pakageListView);
        listView.setAdapter(adapter);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.down_from_top);
        listView.setLayoutAnimation(new LayoutAnimationController(animation));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(PakageActivity.this, VideoFlashCardActivity.class);
                Package aPackage = adapter.getItem(position);
                try {
                    aPackage.startLearning();
                } catch (AlreadyStartedLearningException e) {
                    //e.printStackTrace();
                } catch (DependedPackageNotLearnedYetException e) {
                    //e.printStackTrace();
                }

                myIntent.putExtra("package", adapter.getItem(position)); //Optional parameters
                myIntent.putExtra("packageId" , adapter.getItem(position).getId());
                PakageActivity.this.startActivity(myIntent);
            }
        });

    }
}