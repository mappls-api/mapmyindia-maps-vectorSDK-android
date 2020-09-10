package com.mapmyindia.sdk.demo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.mapmyindia.sdk.demo.java.fragments.AnimationsFragment;
import com.mapmyindia.sdk.demo.java.fragments.ApiCallsFragment;
import com.mapmyindia.sdk.demo.java.fragments.CameraFeatureFragment;
import com.mapmyindia.sdk.demo.java.fragments.CustomWidgetsFragment;
import com.mapmyindia.sdk.demo.java.fragments.LocationFeatureFragment;
import com.mapmyindia.sdk.demo.java.fragments.MapFeaturesFragment;
import com.mapmyindia.sdk.demo.java.fragments.MapLayersFragment;
import com.mapmyindia.sdk.demo.java.fragments.MarkersFeaturesFragment;
import com.mapmyindia.sdk.demo.java.fragments.PolylineFeaturesFragment;
import com.mapmyindia.sdk.demo.kotlin.fragments.ApiCallFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.CustomWidgetFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.LocationFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.MapFeaturesFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.MapLayerFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.MarkerFeatureFragmentKt;
import com.mapmyindia.sdk.demo.kotlin.fragments.PolylineFeatureFragmentKt;

import java.util.Objects;

/**
 * Created by CEINFO on 19-07-2018.
 */

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Type selectedType = Type.JAVA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigate_view);
        //  toolbar.setTitle("MapSdkDemo");
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.open, // nav drawer open - description for accessibility
                R.string.close);// nav drawer close - description for accessibility


        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView.setCheckedItem(R.id.maps);
        selectItem(selectedType, navigationView.getCheckedItem());
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItem(selectedType, item);
                return true;
            }
        });


    }

    private void selectItem(Type type, MenuItem item) {
        if (type == Type.JAVA) {
            if (item.getItemId() == R.id.maps) {

                MapFeaturesFragment mapFeaturesFragment = new MapFeaturesFragment();
                replaceFragment(mapFeaturesFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("MapsEvent");
                drawerLayout.closeDrawer(GravityCompat.START);

            } else if (item.getItemId() == R.id.camera) {

                CameraFeatureFragment cameraFeatureFragment = new CameraFeatureFragment();
                replaceFragment(cameraFeatureFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Camera");

                drawerLayout.closeDrawer(GravityCompat.START);

            }
            else if (item.getItemId() == R.id.markers) {

                MarkersFeaturesFragment markersFeaturesFragment = new MarkersFeaturesFragment();
                replaceFragment(markersFeaturesFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Marker");

                drawerLayout.closeDrawer(GravityCompat.START);

            }
            else if (item.getItemId() == R.id.locations) {

                LocationFeatureFragment locationFeatureFragment = new LocationFeatureFragment();
                replaceFragment(locationFeatureFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Location");


            }
           else  if (item.getItemId() == R.id.polylines) {

                PolylineFeaturesFragment polylineFeaturesFragment = new PolylineFeaturesFragment();
                replaceFragment(polylineFeaturesFragment);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Polyline");

                drawerLayout.closeDrawer(GravityCompat.START);

            }
            else if (item.getItemId() == R.id.apiCalls) {

                ApiCallsFragment apiCallsFragment = new ApiCallsFragment();
                replaceFragment(apiCallsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Rest APIs");


            } else if(item.getItemId() == R.id.maps_layer) {
                MapLayersFragment animationsFragment = new MapLayersFragment();
                replaceFragment(animationsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Map Layers");
            }
           else  if (item.getItemId() == R.id.animations) {

                AnimationsFragment animationsFragment = new AnimationsFragment();
                replaceFragment(animationsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Animation");

            }
            else if (item.getItemId() == R.id.customWidgets) {

                CustomWidgetsFragment customWidgetsFragment = new CustomWidgetsFragment();
                replaceFragment(customWidgetsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Custom Widgets");


            }


        } else if (type == Type.KOTLIN) {
            if (item.getItemId() == R.id.maps) {

                MapFeaturesFragmentKt mapFeaturesFragmentKt = new MapFeaturesFragmentKt();
                replaceFragment(mapFeaturesFragmentKt);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Map Events");


            }
           else  if (item.getItemId() == R.id.camera) {

                CameraFeatureFragmentKt cameraFeatureFragment = new CameraFeatureFragmentKt();
                replaceFragment(cameraFeatureFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Camera");


            }
            else if (item.getItemId() == R.id.markers) {

                MarkerFeatureFragmentKt markersFeaturesFragment = new MarkerFeatureFragmentKt();
                replaceFragment(markersFeaturesFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Marker");


            } else if(item.getItemId() == R.id.maps_layer) {
                MapLayerFragmentKt markersFeaturesFragment = new MapLayerFragmentKt();
                replaceFragment(markersFeaturesFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Map Layers");
            }
            else  if (item.getItemId() == R.id.locations) {

                LocationFragmentKt locationFeatureFragment = new LocationFragmentKt();
                replaceFragment(locationFeatureFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("LocationEvent");


            }
            else if (item.getItemId() == R.id.polylines) {

                PolylineFeatureFragmentKt polylineFeaturesFragment = new PolylineFeatureFragmentKt();
                replaceFragment(polylineFeaturesFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Polyline");

            }
            else if (item.getItemId() == R.id.apiCalls) {

                ApiCallFragmentKt apiCallsFragment = new ApiCallFragmentKt();
                replaceFragment(apiCallsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Rest APIs");


            }
            else if (item.getItemId() == R.id.animations) {

                /*AnimationFragmentkt animationsFragment = new AnimationFragmentkt();
                replaceFragment(animationsFragment);*/
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Animations");


            }
            else if (item.getItemId() == R.id.customWidgets) {

                CustomWidgetFragmentKt customWidgetsFragment = new CustomWidgetFragmentKt();
                replaceFragment(customWidgetsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                Objects.requireNonNull(getSupportActionBar()).setTitle("Custom Widgets");

            }
        }

    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_view, fragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        if (item.getItemId() == R.id.kotlin) {
            selectedType = Type.KOTLIN;
        } else if (item.getItemId() == R.id.java) {
            selectedType = Type.JAVA;
        }
        selectItem(selectedType, navigationView.getCheckedItem());
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }


    enum Type {

        KOTLIN(" Kt"),
        JAVA(" Java");

        String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
