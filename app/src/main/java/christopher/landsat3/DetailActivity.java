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

    String urlImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);


        Intent intentFromMainActivity = getIntent();

        detailLong = intentFromMainActivity.getStringExtra("passedLong");
        detailLat = intentFromMainActivity.getStringExtra("passedLat");
        detailDate = intentFromMainActivity.getStringExtra("passedDate");
        detailTitle = intentFromMainActivity.getStringExtra("passedTitle");

        ((AppCompatActivity) this).getSupportActionBar().setTitle(detailTitle);

        tvDate.setText(detailDate);
        tvLon.setText("Lon: " + detailLong);
        tvLat.setText("Lat: " + detailLat);


        try {
            urlImage = model.getUrl();

            Glide.with(this)
                    .load(urlImage)
                    .into(image);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
