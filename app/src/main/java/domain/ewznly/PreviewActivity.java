package domain.ewznly;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewActivity extends AppCompatActivity {
    private TextView textView;
    private Button Ana_BTN;
    private ImageView imageView;
    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            file = Uri.parse(extra.getString("Image"));
        }
        imageView = findViewById(R.id.imageview);
        imageView.setImageURI(file);
        textView = findViewById(R.id.TxtView);
        Ana_BTN = findViewById(R.id.AnalysisBTN);
        textView.setText("Result is ");

    }
}
