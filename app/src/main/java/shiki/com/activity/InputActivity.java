package shiki.com.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import shiki.com.weathertest.R;

/**
 * Created by wnh50 on 2015/12/12.
 */
public class InputActivity extends Activity {
    EditText inputBox;
    Button btCommit;
    Button btCencel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.input_activity);


        inputBox = (EditText) findViewById(R.id.input_Box);
        btCommit = (Button) findViewById(R.id.commit);
        btCencel = (Button) findViewById(R.id.cencel);
        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("cityName", inputBox.getText().toString());
                Log.d("TAG", "onClick: "+inputBox.getText());
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        btCencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

    }
}
