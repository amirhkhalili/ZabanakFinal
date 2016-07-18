package ir.armaani.hv.zabanak.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ir.armaani.hv.zabanak.App;
import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.activities.adapters.TranslateListAdapter;
import ir.armaani.hv.zabanak.activities.adapters.TranslateTabAdapter;
import ir.armaani.hv.zabanak.exceptions.DoesNotStartedAlreadyException;
import ir.armaani.hv.zabanak.models.Word;
import ir.armaani.hv.zabanak.rest.RestClient;
import ir.armaani.hv.zabanak.rest.responses.Sentence;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateActivity extends ActionBarActivity {
    ImageView okWord_btn,nkWord_btn;
    TextView word,wordTranslate;

    public Word getWordItem() {
        return wordItem;
    }

    Word wordItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        Intent intent = getIntent();
        wordItem = (Word) intent.getSerializableExtra("word");
        if (wordItem.getId() == null)
            wordItem.setId(intent.getLongExtra("wordId" , 0));



        TabLayout t = (TabLayout) findViewById(R.id.tab_layout_translate2);
        t.addTab(t.newTab().setText("ترجمه فارسی"));
        t.addTab(t.newTab().setText("ترجمه انگلیسی"));
        t.addTab(t.newTab().setText("مثال ها"));
        t.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_translate2);
        final TranslateTabAdapter adapter = new TranslateTabAdapter(getSupportFragmentManager(), t.getTabCount());
        viewPager.setAdapter(adapter);

        okWord_btn = (ImageView)findViewById(R.id.okWord);
        nkWord_btn = (ImageView)findViewById(R.id.nkWord);

        okWord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wordItem.doSuccess();
                } catch (DoesNotStartedAlreadyException e) {
                    e.printStackTrace();
                }
                Intent resultIntent = new Intent();
//                resultIntent.putExtra(...);  // put data that you want returned to activity A
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        nkWord_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    wordItem.doFailure();
                } catch (DoesNotStartedAlreadyException e) {
                    e.printStackTrace();
                }
                Intent resultIntent = new Intent();
//                resultIntent.putExtra(...);  // put data that you want returned to activity A
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        word = (TextView)findViewById(R.id.word_txt);
        wordTranslate = (TextView)findViewById(R.id.wordTranslate_txt);
        word.setText(wordItem.getWord());
        wordTranslate.setText(wordItem.getTranslate());



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(t));
        t.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}


