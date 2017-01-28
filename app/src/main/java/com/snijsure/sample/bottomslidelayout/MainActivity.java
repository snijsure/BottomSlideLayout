package com.snijsure.sample.bottomslidelayout;

import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.snijsure.sample.bottomslidelayout.ui.adapter.CuratedCollectionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.snijsure.sample.bottomslidelayout.R.id.recycler_curate_images;
import static com.snijsure.sample.bottomslidelayout.R.id.recycler_curate_images_second;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener,ObservableScrollViewCallbacks {

    @BindView(recycler_curate_images) RecyclerView mCuratedImagesRecycleView;
    @BindView(recycler_curate_images_second) RecyclerView mCuratedImagesRecycleView2;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private View mOverlayView;
    private int mFlexibleSpaceImageHeight;
    private int mActionBarSize = 147;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View mapView;
    private TextView title_text1;
    ObservableScrollView slidingContainer;
    View headerView;
    Button showMe;
    int displayHeight;
    public static String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayHeight = size.y;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();

        mapFragment.getMapAsync(this);
        headerView = (View)findViewById(R.id.headerView);
        headerView.getLayoutParams().height = displayHeight;

        showMe = (Button)findViewById(R.id.showme);
        slidingContainer = (ObservableScrollView) findViewById(R.id.slidingContainer);
        title_text1 = (TextView) findViewById(R.id.title_text1);

        slidingContainer.setScrollViewCallbacks(this);

        LinearLayoutManager horizontalScroller = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        mCuratedImagesRecycleView.setLayoutManager(horizontalScroller);
        String[] curatedImages = getResources().getStringArray(R.array.curated_collection_images);
        CuratedCollectionAdapter curatedCollectionAdapter = new CuratedCollectionAdapter(
                curatedImages,onCuratedItemClicked);
        mCuratedImagesRecycleView.setAdapter(curatedCollectionAdapter);


        LinearLayoutManager horizontalScroller2 = new LinearLayoutManager(MainActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        mCuratedImagesRecycleView2.setLayoutManager(horizontalScroller2);
        CuratedCollectionAdapter curatedCollectionAdapter2 = new CuratedCollectionAdapter(
                curatedImages,onCuratedItemClicked);
        mCuratedImagesRecycleView2.setAdapter(curatedCollectionAdapter2);

        ScrollUtils.addOnGlobalLayoutListener(slidingContainer, new Runnable() {
            @Override
            public void run() {
                slidingContainer.scrollTo(0, mFlexibleSpaceImageHeight - mActionBarSize);

            }
        });
        showMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        headerView.setClickable(false);
        headerView.setLongClickable(false);
        mapView.setClickable(true);
        mapView.setLongClickable(true);
    }

    private CuratedCollectionAdapter.OnRecyclerItemClick onCuratedItemClicked =
            new CuratedCollectionAdapter.OnRecyclerItemClick() {
                @Override
                public void onItemClick(int position) {

                }
            };

    //TODO: request permissions.
    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setNumUpdates(1);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
        catch(Exception e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        mMap = map;
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int minOverlayTransitionY = displayHeight/2;
        int[] loc = new int[2];
        headerView.getLocationOnScreen(loc);

        Rect rectf = new Rect();
        slidingContainer.getLocalVisibleRect(rectf);
        if(loc[1] > 0 ) {
            showMe.setVisibility(View.VISIBLE);
            showMe.setY(displayHeight-showMe.getHeight()-200);
            showMe.requestLayout();
        }
        else {
            showMe.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }
    boolean mScrollable = false;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (mScrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return mScrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    private void setupTouchableFrame() {
        ViewGroup v = (ViewGroup)headerView.getParent();
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


    }

    /*
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!mScrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    */
}
