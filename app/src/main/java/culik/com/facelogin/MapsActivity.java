package culik.com.facelogin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener ,LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker marker;
    private ClusterManager mClusterManager;
    private LatLng[] posicoes = new LatLng[10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        posicoes[0] = new LatLng(-30,-56);
        posicoes[1] = new LatLng(-30,-57);
        posicoes[2] = new LatLng(-30,-60);
        posicoes[3] = new LatLng(-30,-54);
        posicoes[4] = new LatLng(-30,-35);
        posicoes[5] = new LatLng(-30,-55);
        posicoes[6] = new LatLng(-29,-23);
        posicoes[7] = new LatLng(-29,-67);
        posicoes[8] = new LatLng(-37,-64);
        posicoes[9] = new LatLng(-38,-56);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
                      mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
                      mGoogleApiClient.connect();
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        for (LatLng posicao : posicoes){
            MyItem offsetItem = new MyItem(posicao.latitude, posicao.longitude);
            mClusterManager.addItem(offsetItem);
        }

        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    }



    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LatLng eu = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        if (marker == null)
        marker = mMap.addMarker(new MarkerOptions().position(eu).title("Estou aqui"));
        else
           marker.setPosition(eu);

   //     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eu, 16));

        for (LatLng posicao : posicoes){
            MarkerOptions mOpt = new MarkerOptions();
            mOpt.position(posicao);
            mMap.addMarker(mOpt);
        }

    }


    @Override
      public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location) {
        LatLng newPos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, 16));

    }
}
