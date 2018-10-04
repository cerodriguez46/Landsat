package christopher.landsat3;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private List<LandsatModel> satelliteList;

    @BindView(R.id.fabSatelliteImage)
    FloatingActionButton satFab;

    @BindView(R.id.searchInput)
    EditText searchUserInput;

    @BindView(R.id.userEnter)
    Button userInputButton;

    @BindView(R.id.tv_lat)
    TextView latitude;

    @BindView(R.id.day)
    TextView date;

    @BindView(R.id.lng)
    TextView longitude;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheetLayout;


    String textFromLatEditText;
    String textFromLongEditText;
    String textFromDateEditText;

    BottomSheetBehavior sheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ButterKnife.bind(this);


        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        userInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBottomSheetContents();
            }
        });



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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 9.0f;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void updateBottomSheetContents() {

        if (searchUserInput != null) {
            textFromDateEditText = searchUserInput.getText().toString();
            date.setText(textFromDateEditText);

            textFromLatEditText = searchUserInput.getText().toString();
            latitude.setText(textFromLatEditText);

            textFromLongEditText = searchUserInput.getText().toString();
            longitude.setText(textFromLongEditText);
        } else {
            date.setText("N/A");
            latitude.setText("N/A");
            longitude.setText("N/A");
        }

    }

}
