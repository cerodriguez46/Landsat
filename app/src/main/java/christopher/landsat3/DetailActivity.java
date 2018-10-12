package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import christopher.landsat3.Networking.LandsatModel;

public class DetailActivity extends AppCompatActivity {

    String detailLong;
    String detailLat;
    String detailDate;
    String detailTitle;


    @BindView(R.id.tv_detail_date)
    TextView tvDate;

    @BindView(R.id.tv_detail_long)
    TextView tvLon;

    @BindView(R.id.tv_detail_lat)
    TextView tvLat;

    @BindView(R.id.imageView)
    ImageView image;

    LandsatModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);


        Intent intentFromMainActivity = getIntent();

        model = intentFromMainActivity.getParcelableExtra("landsatParcel");

        detailLong = intentFromMainActivity.getStringExtra("passedLong");
        detailLat = intentFromMainActivity.getStringExtra("passedLat");
        detailDate = intentFromMainActivity.getStringExtra("passedDate");
        detailTitle = intentFromMainActivity.getStringExtra("passedTitle");
        model = intentFromMainActivity.getParcelableExtra("landsatParcel");



        ((AppCompatActivity) this).getSupportActionBar().setTitle(detailTitle);

        tvDate.setText(detailDate);
        tvLon.setText("Lon: " + detailLong);
        tvLat.setText("Lat: " + detailLat);


        try {


            Glide.with(this)
                    .load(model.url)
                    .into(image);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
