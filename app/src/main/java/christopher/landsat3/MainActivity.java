package christopher.landsat3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
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

    @BindView(R.id.tv_lng)
    TextView longitude;

    @BindView(R.id.buttonDate)
    Button selectDateButton;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheetLayout;

    String textFromDateEditText;

    Double latReturn;
    Double longReturn;

    String latCoord;
    String longCoord;

    BottomSheetBehavior sheetBehavior;

    DecimalFormat latlngFormatted = new DecimalFormat("###,###.##");

    LandsatModel model;

    static final String TAG = "101";

    String cloudScore = "true";

    String selectedDate;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int calendarYear;
    int calendarMonth;
    int calendarDay;


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

                onMapSearch(view);
                updateBottomSheetContents();
                parseJson();
            }
        });


    }

    private void parseJson() {


        RetrofitClient client = new RetrofitClient();

        final RetrofitInterface apiService = client.getClient().create(RetrofitInterface.class);

        Call<LandsatModel> call = apiService.geLandsatData(longCoord, latCoord, selectedDate, cloudScore, BuildConfig.NASA_API);

        call.enqueue(new Callback<LandsatModel>() {
            @Override
            public void onResponse(Call<LandsatModel> call, Response<LandsatModel> response) {

                model = response.body();
                Toast.makeText(getApplicationContext(), String.valueOf(response), Toast.LENGTH_LONG).show();
                Log.v(TAG, String.valueOf(response));

            }

            @Override
            public void onFailure(Call<LandsatModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "It didn't work! :(", Toast.LENGTH_LONG).show();
            }
        });

    }


    @OnClick(R.id.fabSatelliteImage)
    public void obtainSatelliteImage(View v) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("passedLong", longCoord);
        intent.putExtra("passedLat", latCoord);
        intent.putExtra("passedDate", selectedDate);
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
        float zoomLevel = 4.0f;

        // Add a marker in Sydney and move the camera

        LatLng sydney = new LatLng(-34, 151);
        
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }

    public void onMapSearch(View view) {

        String location = searchUserInput.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            latReturn = address.getLatitude();
            longReturn = address.getLongitude();


            latCoord = String.valueOf(latlngFormatted.format(latReturn));
            longCoord = String.valueOf(latlngFormatted.format(longReturn));


        }
    }

    public void openCalendar(View v) {
        final Calendar calendar = Calendar.getInstance();

        calendarYear = calendar.get(Calendar.YEAR);
        calendarMonth = calendar.get(Calendar.MONTH);
        calendarDay = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                android.R.style.Theme_Holo_Light,
                mDateSetListener,
                calendarYear, calendarDay, calendarMonth);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        //datePickerDialog.getDatePicker().setMinDate();

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                if (month < 10) {

                    month = Integer.parseInt("0" + month);
                }
                if (day < 10) {

                    day = Integer.parseInt("0" + day);


                }

                selectedDate = year + "-" + month + "-" + day;

                date.setText(selectedDate);


            }
        };


    }


    public void updateBottomSheetContents() {

        textFromDateEditText = searchUserInput.getText().toString();

        if (textFromDateEditText == "") {

            latitude.setText("N/A");

            longitude.setText("N/A");


        } else {
            latitude.setText(latCoord);


            longitude.setText(longCoord);
        }

    }

}
