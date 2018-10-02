package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christopher.landsat3.Networking.LandsatModel;
import christopher.landsat3.Networking.RetrofitClient;
import christopher.landsat3.Networking.RetrofitInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private List<LandsatModel> satelliteList;

    @BindView(R.id.fabSatelliteImage)
    FloatingActionButton satFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);



        parseJson();
    }

    private void parseJson() {

        RetrofitClient client = new RetrofitClient();

        RetrofitInterface apiService = client.getClient().create(RetrofitInterface.class);

        Call<String> call = apiService.geLandsatData(BuildConfig.NASA_API);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String sat = response.body();
                Toast.makeText(getApplicationContext(), sat, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "It didn't work! :(", Toast.LENGTH_LONG).show();
            }
        });

    }


    @OnClick(R.id.fabSatelliteImage)
    public void obtainSatelliteImage(View v) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        startActivity(intent);

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
            Intent intent = new Intent(MainActivity.this, Tutorial.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.imgs_saved) {
            Intent intent = new Intent(MainActivity.this, Bookmarks.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
