package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

    public void shareImage(View v) {
        String combinedImageText = "Date is " + detailDate + "\n\n" + " Latitude is " + detailLat +
                "\n\n" + " Longitude is " + detailLong + "\n\n" + " Image url is " + "\n\n" + model.url;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, combinedImageText);
        sharingIntent.setType("image/*");
        sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tutorial_menu) {
            Intent intent = new Intent(DetailActivity.this, Tutorial.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.imgs_saved) {
            Intent intent = new Intent(DetailActivity.this, Bookmarks.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
