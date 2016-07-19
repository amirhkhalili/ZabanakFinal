package ir.armaani.hv.zabanak.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ir.armaani.hv.zabanak.R;
import ir.armaani.hv.zabanak.models.Word;

public class MessageActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        TextView textView = (TextView)findViewById(R.id.title_txt);
        TextView textView1 = (TextView)findViewById(R.id.content_txt);
        textView.setText(title);
        textView1.setText(content);
        Button button = (Button)findViewById(R.id.openActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MessageActivity.this, MainActivity.class);
                MessageActivity.this.startActivity(myIntent);
                finish();
            }
        });

    }
}
