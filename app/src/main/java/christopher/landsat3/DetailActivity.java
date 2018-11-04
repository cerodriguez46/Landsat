package christopher.landsat3;

import android.app.Dialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

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

    @BindView(R.id.filter_button)
    ImageView filterButton;


    LandsatModel model;


    ArrayList<String> satImages = new ArrayList<String>();

    private AppDatabase mDb;


    private Bitmap imageBmp;
    private Bitmap filterBright;

    private AppWidgetTarget appWidgetTarget;


    String satImage;
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
        tvLon.setText("Longitude" + "\n\n" + detailLong);
        tvLat.setText("Latitude" + "\n\n" + detailLat);


        String detailImage = model.url;
        Log.v("imageUrl", detailImage);



        try {

            RequestOptions options = new RequestOptions()
                    .override(Target.SIZE_ORIGINAL)
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
        updateWidget();

    }

    boolean isFilterPressed = true;
    public void filterImage(View v) {


        satImage = model.url;


        if (isFilterPressed) {
            Toast.makeText(this, R.string.filter_message, Toast.LENGTH_LONG).show();
            filterButton.setImageResource(R.drawable.check);


            Glide.with(DetailActivity.this)
                    .asBitmap()
                    .load(satImage)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageBmp = resource;
                            new Task2().execute();


                        }
                    });

        } else {
            Toast.makeText(this, "Removing filter from image", Toast.LENGTH_LONG).show();
            filterButton.setImageResource(R.drawable.photofilter);

            RequestOptions options = new RequestOptions()
                    .override(Target.SIZE_ORIGINAL)
                    .placeholder(R.drawable.placeholder);


            Glide.with(this)
                    .load(satImage)
                    .transition(withCrossFade())
                    .apply(options)
                    .into(image);


        }

        isFilterPressed = !isFilterPressed;

    }

    class Task2 extends AsyncTask<String, Void, Void>

    {
        @Override
        protected Void doInBackground(String... strings) {

            filterBright = Bitmap.createBitmap(imageBmp.getWidth(), imageBmp.getHeight(), imageBmp.getConfig());

            for (int i = 0; i < imageBmp.getWidth(); i++) {
                for (int j = 0; j < imageBmp.getHeight(); j++) {
                    int p = imageBmp.getPixel(i, j);
                    int r = Color.red(p);
                    int g = Color.green(p);
                    int b = Color.blue(p);
                    int alpha = Color.alpha(p);

                    r = 100 + r;
                    g = 100 + g;
                    b = 100 + b;
                    alpha = 150;
                    filterBright.setPixel(i, j, Color.argb(alpha, r, g, b));
                }
            }
            image.setImageBitmap(filterBright);


            return null;
        }


    }



    public void shareImage(View v) {

        Toast.makeText(this, R.string.share_message, Toast.LENGTH_SHORT).show();

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


                //save.setImageResource(R.drawable.save);

                // mDb.landsatDao().deleteRecord(landsatModel);
                Log.v("DatabaseDelete", "deleting satellite image from database");


                save.setImageResource(R.drawable.check);


                mDb.landsatDao().insertRecord(landsatModel);
                Log.v("DatabaseInsert", "inserting satellite image into the database");


            }
        });
    }

    public void openMenuDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom);
        //dialog.setTitle("Landsat Satellite Tutorial");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        ImageView image = (ImageView) dialog.findViewById(R.id.image);


        dialog.show();


    }

    public void updateWidget() {

        String widgetImage;
        widgetImage = model.url;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtra("passedLat", model.getLatitude());
        //intent.putExtra("passedLong", model.getLongitude());
        //intent.putExtra("passedDate", model.getDate());
        //intent.putExtra("landsatParcel", widgetImage);
        //intent.putExtra("passedTitle", model.getTitle());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.landsat_widget);
        remoteViews.setOnClickPendingIntent(R.id.widget_image, pendingIntent);
        ComponentName thisWidget = new ComponentName(this, LandsatWidget.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        appWidgetTarget = new AppWidgetTarget(this, R.id.widget_image, remoteViews, thisWidget) {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
            }
        };

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder);

        Glide
                .with(this.getApplicationContext())
                .asBitmap()
                .load(widgetImage)
                .apply(options)
                .into(appWidgetTarget);

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
            openMenuDialog();
            return true;
        } else if (id == R.id.imgs_saved) {
            Intent intent = new Intent(DetailActivity.this, Bookmarks.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
