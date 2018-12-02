package com.example.sumi.googlemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText search;
    Button gobtn;
    Button satellitebtn;
    Button normalbtn;
    Button terrainbtn;
    LatLng clickLatLng;
    GoogleMap map;

    RequestQueue rq;
    String address;
    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search);
        gobtn = findViewById(R.id.go);
        satellitebtn = findViewById(R.id.satellite);
        normalbtn = findViewById(R.id.normal);
        terrainbtn = findViewById(R.id.terrain);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address = search.getText().toString();
                rq = Volley.newRequestQueue(MainActivity.this);
                StringRequest sq = new StringRequest(Request.Method.GET, url + address, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject js1 = new JSONObject(response);
                            JSONArray a1 = js1.getJSONArray("results");
                            JSONObject js2 = a1.getJSONObject(0);
                            JSONObject js3 = js2.getJSONObject("geometry");
                            JSONObject js4 = js3.getJSONObject("location");
                            Double mylat = js4.getDouble("lat");
                            Double mylng = js4.getDouble("lng");
                            LatLng l = new LatLng(mylat, mylng);
                            map.moveCamera(CameraUpdateFactory.newLatLng(l));
                            map.addMarker(new MarkerOptions().position(l));
                        } catch (Exception e) {


                            Toast.makeText(MainActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();

                    }
                });
                rq.add(sq);


            }
        });


        satellitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            }
        });
        terrainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });


        normalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map=googleMap;
        LatLng l = new LatLng(27.7172, 85.3240);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 10));//camera move gareko point out location dekhauna
        googleMap.addMarker(new MarkerOptions().position(l));//marker add garako point out location ma


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clickLatLng = latLng;
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));


                googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        PolylineOptions options = new PolylineOptions();
                        options.add(clickLatLng);
                        options.add(latLng);
                        googleMap.addPolyline(options);
                        googleMap.addMarker(new MarkerOptions().position(latLng));


                    }
                });
            }
        });


    }

}

