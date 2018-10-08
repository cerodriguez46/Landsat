package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    String detailLong;
    String detailLat;
    String detailDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intentFromMainActivity = getIntent();

        detailLong = intentFromMainActivity.getStringExtra("passedLong");
        detailLat = intentFromMainActivity.getStringExtra("passedLat");
        detailDate = intentFromMainActivity.getStringExtra("selectedDate");
    }
}
