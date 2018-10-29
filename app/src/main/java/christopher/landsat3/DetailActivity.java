package christopher.landsat3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import christopher.landsat3.Data.AppDatabase;
import christopher.landsat3.Data.AppExecutors;
import christopher.landsat3.Networking.LandsatModel;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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

    @BindView(R.id.save_button)
    ImageView save;


    LandsatModel model;

    Bitmap satImaeBitmap;
    Bitmap bmOut;

    ArrayList<String> satImages = new ArrayList<String>();

    private AppDatabase mDb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDb = AppDatabase.getInstance(getApplicationContext());

        ButterKnife.bind(this);


        Intent intent = getIntent();

        model = intent.getParcelableExtra("landsatParcel");

        detailLong = intent.getStringExtra("passedLong");
        detailLat = intent.getStringExtra("passedLat");
        detailDate = intent.getStringExtra("passedDate");
        detailTitle = intent.getStringExtra("passedTitle");




        ((AppCompatActivity) this).getSupportActionBar().setTitle(detailTitle);

        tvDate.setText(detailDate);
        tvLon.setText("Lon: " + detailLong);
        tvLat.setText("Lat: " + detailLat);


        String detailImage = model.url;



        try {

            RequestOptions options = new RequestOptions()
                    .dontAnimate()
                    .fitCenter()
                    .placeholder(R.drawable.placeholder);


            Glide.with(this)
                    .load(detailImage)
                    .transition(withCrossFade())
                    .apply(options)
                    .into(image);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.no_sat_image, Toast.LENGTH_SHORT).show();
        }
    }

    public void filterImage(View v) {
        try {
            URL url = new URL("http://....");
            satImaeBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            adjustedContrast(satImaeBitmap, 5.00);

            Glide.with(this)
                    .load(satImaeBitmap)
                    .into(image);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private Bitmap adjustedContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
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

    boolean isPressed = true;
    public void saveImage(View v) {

        Toast.makeText(DetailActivity.this, R.string.database_save, Toast.LENGTH_SHORT).show();

        //insert double cloudscore, string date, string id, string service version, string url, string lat, string long
        final LandsatModel landsatModel = new LandsatModel(model.cloudScore, detailDate, model.id, model.serviceVersion, model.url,
                detailLat, detailLong, detailTitle);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isPressed) {

                    save.setImageResource(R.drawable.save);

                    // mDb.landsatDao().deleteRecord(landsatModel);
                    Log.v("DatabaseDelete", "deleting satellite image from database");

                } else {

                    save.setImageResource(R.drawable.check);


                    mDb.landsatDao().insertRecord(landsatModel);
                    Log.v("DatabaseInsert", "inserting satellite image into the database");


                }
            }
        });
        isPressed = !isPressed;
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
