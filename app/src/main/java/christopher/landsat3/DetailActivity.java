package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    String detailLong;
    String detailLat;
    String detailDate;
    String detailTitle;


    TextView tvDate;


    TextView tvLon;


    TextView tvLat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        tvDate = (TextView) findViewById(R.id.tv_detail_date);
        tvLon = (TextView) findViewById(R.id.tv_detail_long);
        tvLat = (TextView) findViewById(R.id.tv_detail_lat);

        Intent intentFromMainActivity = getIntent();

        detailLong = intentFromMainActivity.getStringExtra("passedLong");
        detailLat = intentFromMainActivity.getStringExtra("passedLat");
        detailDate = intentFromMainActivity.getStringExtra("selectedDate");
        detailTitle = intentFromMainActivity.getStringExtra("passedTitle");

        ((AppCompatActivity) this).getSupportActionBar().setTitle(detailTitle);

        tvDate.setText(detailDate);
        tvLon.setText(detailLong);
        tvLat.setText(detailLat);
    }
}
