package christopher.landsat3;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnKeyListener {

    private GoogleMap mMap;


    @BindView(R.id.fabSatelliteImage)
    FloatingActionButton satFab;

    @BindView(R.id.searchInput)
    EditText searchUserInput;

    @BindView(R.id.tv_lat)
    TextView latitude;

    @BindView(R.id.day)
    TextView date;

    @BindView(R.id.tv_lng)
    TextView longitude;


    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheetLayout;

    @BindView(R.id.placeTitle)
    TextView titleOfPlace;

    String textFromEditText;

    Double latReturn;
    Double longReturn;

    String latCoord;
    String longCoord;

    BottomSheetBehavior sheetBehavior;

    DecimalFormat latlngFormatted = new DecimalFormat("###,###.##");

    LandsatModel result;

    static final String TAG = "101";

    String cloudScore = "true";

    String selectedDate;

    String satImageSize = "0.075";


    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int calendarYear;
    int calendarMonth;
    int calendarDay;


    private FusedLocationProviderClient mFusedLocationClient;

    String formattedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        searchUserInput.setOnKeyListener(this);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);



    }


    public void parseJson() {


        RetrofitClient client = new RetrofitClient();

        RetrofitInterface apiService = client.getClient().create(RetrofitInterface.class);

        Call<LandsatModel> call = apiService.geLandsatData(longCoord, latCoord, satImageSize, selectedDate, cloudScore, BuildConfig.NASA_API);

        call.enqueue(new Callback<LandsatModel>() {
            @Override
            public void onResponse(Call<LandsatModel> call, Response<LandsatModel> response) {
                result = response.body();
                if (response.code() == 200) {
                    Toast.makeText(getApplicationContext(), R.string.connection_success, Toast.LENGTH_SHORT)
                            .show();
                } else if (response.code() == 500) {
                    Toast.makeText(getApplicationContext(), R.string.connection_no_servers, Toast.LENGTH_SHORT)
                            .show();
                } else if (response.code() == 400) {
                    Toast.makeText(getApplicationContext(), R.string.connection_invalid, Toast.LENGTH_SHORT)
                            .show();
                }


                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("passedLong", longCoord);
                intent.putExtra("passedLat", latCoord);
                intent.putExtra("passedDate", formattedDate);
                intent.putExtra("passedTitle", textFromEditText);
                intent.putExtra("landsatParcel", result);
                startActivity(intent);


            }

            @Override
            public void onFailure(Call<LandsatModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.unable_connect_internet, Toast.LENGTH_LONG).show();
            }
        });

    }


    @OnClick(R.id.fabSatelliteImage)
    public void obtainSatelliteImage(View v) {

        if (TextUtils.isEmpty(textFromEditText = searchUserInput.getText().toString()) || textFromEditText == null) {
            Toast.makeText(MainActivity.this, R.string.userinput_no_place, Toast.LENGTH_SHORT).show();
        } else if (date.getText().equals(R.string.not_applicable) || date == null) {
            Toast.makeText(MainActivity.this, R.string.userinput_no_date, Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(textFromEditText = searchUserInput.getText().toString()) &&
                date.getText().equals(R.string.not_applicable)) {
            Toast.makeText(MainActivity.this, R.string.userinput_no_place_date, Toast.LENGTH_SHORT).show();
        } else {

            parseJson();


        }
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


        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {

                    float zoomLevel = 13.0f;

                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());

                            mMap.addMarker(new MarkerOptions().position(myPosition).title("Current Position"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, zoomLevel));

                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        }
                    }
                });


    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
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

            if (addressList != null && addressList.size() != 0) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                mMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng)));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                latReturn = address.getLatitude();
                longReturn = address.getLongitude();


                latCoord = String.valueOf(latlngFormatted.format(latReturn));
                longCoord = String.valueOf(latlngFormatted.format(longReturn));


            } else {

                Toast.makeText(MainActivity.this, R.string.location_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openCalendar() {

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

//min date for satellite imagery from the API may be 2013
        Date min = new Date(2018 - 1905, 3, 01);
        Date max = new Date(2062 - 1946, 11, 31);


        datePickerDialog.getDatePicker().setMinDate(min.getTime());

        datePickerDialog.getDatePicker().setMaxDate(max.getTime());

        datePickerDialog.show();


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                formattedDate = String.valueOf(android.text.format.DateFormat.format("MMMM d, yyyy", calendar));
                date.setText(formattedDate);
                selectedDate = String.valueOf(android.text.format.DateFormat.format("yyyy-MM-dd", calendar));


                datePickerDialog.dismiss();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }, 1500);

            }


        };


    }


    public void updateBottomSheetContents() {


        latitude.setText(latCoord);


        longitude.setText(longCoord);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            String stateEditTextSaved = savedInstanceState.getString("saved_edit_text");
            String stateTitleSaved = savedInstanceState.getString("saved_title_place");
            String stateDateSaved = savedInstanceState.getString("saved_date");
            String stateLongSaved = savedInstanceState.getString("saved_long");
            String stateLatSaved = savedInstanceState.getString("saved_lat");

            savedInstanceState.getDouble("lat");
            savedInstanceState.getDouble("lon");
            savedInstanceState.getFloat("zoom");



            searchUserInput.setText(stateEditTextSaved);
            titleOfPlace.setText(stateTitleSaved);
            date.setText(stateDateSaved);
            longitude.setText(stateLongSaved);
            latitude.setText(stateLatSaved);


        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putString("saved_edit_text", searchUserInput.getText().toString());
        outState.putString("saved_title_place", titleOfPlace.getText().toString());
        outState.putString("saved_date", date.getText().toString());
        outState.putString("saved_long", longitude.getText().toString());
        outState.putString("saved_lat", latitude.getText().toString());

        outState.putDouble("lat", mMap.getCameraPosition().target.latitude);
        outState.putDouble("lon", mMap.getCameraPosition().target.longitude);
        outState.putFloat("zoom", mMap.getCameraPosition().zoom);

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER) {

            if (TextUtils.isEmpty(textFromEditText = searchUserInput.getText().toString())) {
                Toast.makeText(MainActivity.this, "Please enter a place", Toast.LENGTH_SHORT).show();
            } else {
                titleOfPlace.setText(textFromEditText);

                searchUserInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

                onMapSearch(view);


                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                openCalendar();

                updateBottomSheetContents();

            }

            return true;


        }
        return false;
    }

    }



