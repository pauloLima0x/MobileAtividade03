package br.com.mobileatividade03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location locationInitial;
    private Location locationCurrent;
    private int gpsCode = 1001;
    private  Chronometer chronometer;
    private TextView travelledDistance_textView;
    private  boolean isRunning = false;
    private double distanceTotal = 0;
    FloatingActionButton fab;
    private double latitudeAtual;
    private double longitudeAtual;
    private EditText search_textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        travelledDistance_textView = findViewById(R.id.travelledDistance_textView);
        chronometer = findViewById(R.id.route_chronometer);
        fab = findViewById(R.id.fab);
        search_textView = findViewById(R.id.search_textView);
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
               travelledDistance_textView.setText(Float.toString(locationInitial.distanceTo(locationCurrent)));
               distanceTotal +=  locationInitial.distanceTo(locationCurrent);


            }
        });




        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitudeAtual = lat;
                longitudeAtual = lon;

                  if(isRunning) {
                       locationCurrent = location;
                   }


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri =
                        Uri.parse(String.format("geo:%f,%f?q="+ search_textView.getText(),
                                latitudeAtual, longitudeAtual));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

    }

       public void grantPermission(View v) {

        if((ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) )   {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, gpsCode);
        }


       }



        public void turnOnGps(View v) {
            if ( ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED  ) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        0, 0, locationListener);
            } else {
                Toast.makeText(this ,getString(R.string.permission_needed), Toast.LENGTH_SHORT).show();


            }
        }





      public void turnOffGps( View v) {



          if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

              locationManager.removeUpdates(locationListener);


          }
          else {

              Toast.makeText(getApplicationContext(), getString(R.string.gps_needed), Toast.LENGTH_SHORT).show();

          }
      }

      public void startRoute(View v) {

        if (  (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) ) {

               locationInitial = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               locationCurrent = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
               chronometer.start();
               isRunning = true;

        } else {
            Toast.makeText(this, getString(R.string.gps_needed),
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void finishRoute(View v) {

        if(isRunning) {

            chronometer.stop();
            Toast.makeText(this ,getString(R.string.total_distance) + distanceTotal + " \n" +
                    getString(R.string.total_time) + chronometer.getText(), Toast.LENGTH_SHORT).show();
            isRunning = false;
            chronometer.setBase(SystemClock.elapsedRealtime());



        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == gpsCode) {
            if (grantResults.length > 0
                    && grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,  getString(R.string.gps_negate),
                            Toast.LENGTH_SHORT).show();

                }
            }
        }







    }








/*


package br.com.ciclodevidagpsemapas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    private int gpsCode = 1001;
    private double latitudeAtual;
    private double longitudeAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationTextView = findViewById(R.id.locationTextView);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitudeAtual = lat;
                longitudeAtual = lon;
                locationTextView.setText(String.format("Lat: %f, Long: %f" ,
                        lat, lon));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };



            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri gmmIntentUri =
                            Uri.parse(String.format("geo:%f,%f?q=restaurantes",
                                    latitudeAtual, longitudeAtual));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
             0, 0, locationListener );

        }else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, gpsCode);

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == gpsCode) {
             if(grantResults.length > 0
             && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 if (ActivityCompat.checkSelfPermission(
                         this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                         PackageManager.PERMISSION_GRANTED) {
                     locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                             0, 0, locationListener);


                 }
             } else {
                 Toast.makeText(this, getString(R.string.no_gps_no_app),
                         Toast.LENGTH_SHORT).show();

             }
         }


    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);

    }
}





 */













