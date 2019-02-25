package com.example.sebastian.amuapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebastian.amuapp.Common.Common;
import com.example.sebastian.amuapp.Interface.ItemClickListener;
import com.example.sebastian.amuapp.Model.Restaurant;
import com.example.sebastian.amuapp.ViewHolder.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener {

    private  MapView mMapView;
    private static GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "AIzaSyAEwFg1KVI-lmNu1zodLue9uEqaboOvB0o";
    private FusedLocationProviderClient client;

    private LocationManager locationManager;
    private Location location;

    static ArrayList<LatLng>  markerPoints;
    static Polyline drawedRoute;

    private static double latitude;
    private static double longtitude;

    static NavigationView navigationView;

    RecyclerView recyclerRestaurant;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder> adapter;

    String[] restaurantName = {"Burger King", "Telepizza", "Adu-Dhabi", "BD King", "Nobo-Sushi", "Freetki"};
    String[] restaurantDescription = {"Najlepsze burgery", "Ciepła pizza, nowe promocje!", "Kebab, pizza, kurczak", "Najlepsze kebaby z baraniny", "Świeże sushi z dostawą do domu", "Frytki z 5 rodzajów ziemniaków!" };
    Integer[] imgId = {R.drawable.burger_king, R.drawable.telepizza_logo, R.drawable.abudhabi, R.drawable.kebabownia_logo, R.drawable.nobosushi, R.drawable.frytoland};
    LatLng[] restaurantLL = {new LatLng(53.124830, 18.017960), new LatLng(53.109295,18.053210),  new LatLng(53.159784,18.175251), new LatLng(53.156999,18.161727), new LatLng(53.121900,18.026271), new LatLng(53.121060,18.002044)};

    TextView firstNameTextView, helloTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        ArrayList<Restaurant> list;

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        if(Common.currentUser!=null)
        {
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_register).setVisible(false);
        }else{
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        //MapView
        mMapView = (MapView) findViewById(R.id.mapView1);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                floatingButtonAction(view);

            }
        });

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        recyclerRestaurant = (RecyclerView) findViewById(R.id.mRecyclerView);
        recyclerRestaurant.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerRestaurant.setLayoutManager(layoutManager);
        loadRestaurants();
    }

    private void loadRestaurants() {
        Query mRestaurant = FirebaseDatabase.getInstance().getReference("/Restaurant");
        FirebaseRecyclerOptions<Restaurant> options =
                new FirebaseRecyclerOptions.Builder<Restaurant>()
                        .setQuery(mRestaurant, Restaurant.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Restaurant, RestaurantViewHolder>(options) {
            @NonNull
            @Override
            public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_component, viewGroup, false);
                return new RestaurantViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RestaurantViewHolder holder, final int position, @NonNull Restaurant model) {
                holder.restuarantNameTextView.setText(model.getName());
                holder.restaurantDescTextView.setText(model.getDescription());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.restaurantImageView);

                LatLng resLatLng;
                String[] latLng = adapter.getItem(position).getLatLng().split(",");
                resLatLng =new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));

                mMap.addMarker(new MarkerOptions()
                        .position(resLatLng)
                        .title(model.getName())
                );

                final Restaurant clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(MainActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();
                        LatLng resLatLng;
                        String[] latLng = adapter.getItem(position).getLatLng().split(",");
                        resLatLng =new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(resLatLng, 15));
                    }
                });
                holder.pathButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LatLng resLatLng;
                        String[] latLng = clickItem.getLatLng().split(",");
                        resLatLng =new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
                        pathBetweenLatLngAndUser(resLatLng);
                        Toast.makeText(MainActivity.this, ""+clickItem.getName()+"PATH BTN", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.shopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get CategoryId and send to OrderActivity
                        Intent restaurantMenu = new Intent (MainActivity.this, OrderActivity.class);
                        restaurantMenu.putExtra("RestaurantId", adapter.getRef(position).getKey());
                        startActivity(restaurantMenu);
                    }
                });
            }
        };
        recyclerRestaurant.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_register) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.nav_logout) {
            Common.currentUser = null;
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_register).setVisible(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //MAPVIEW METHODS
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
    }

    public void changeMapPosition (LatLng latLng, int zoom)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public static void pathBetweenLatLngAndUser(LatLng point) {
        if(drawedRoute!=null)
        {
            drawedRoute.remove();
        }
        markerPoints = new ArrayList<LatLng>();
        if(markerPoints.size()>0){
            markerPoints.clear();
        }
        markerPoints.add(new LatLng(latitude, longtitude));
        MarkerOptions options = new MarkerOptions();

        options.position(new LatLng(latitude, longtitude));

        if(markerPoints.size()==1){
            options.icon(BitmapDescriptorFactory.
                    defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }

        mMap.addMarker(options);

        if(markerPoints.size() >= 1){
            LatLng origin = markerPoints.get(0);
            LatLng dest = point ;

            String url = getDirectionsUrl(origin, dest);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    private static String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //Mode type
        String mode = "mode=driving";

        //GoogleApiKey
        String apiKey = "key=" + MAPVIEW_BUNDLE_KEY;

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode+"&"+apiKey;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url =
                "https://maps.googleapis.com/maps/api/directions/"+output+"?"
                        +parameters;

        System.out.println(url);

        return url;
    }

    /** A method to download json data from url */
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new
                    InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Downloading url error", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private static class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private static class ParserTask extends AsyncTask<String, Integer,
                List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>>
        doInBackground(String...
                               jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String,
                                String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if(result.size()<1){
//                Toast.makeText(getBaseContext(), "No Points",
//                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            drawedRoute = mMap.addPolyline(lineOptions);
        }
    }

    public void floatingButtonAction(View view) {
        if (mMap == null) {
            Snackbar.make(view, "mMap = Null", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            onLocationChanged(location);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longtitude), 13));
        }
    }
}

