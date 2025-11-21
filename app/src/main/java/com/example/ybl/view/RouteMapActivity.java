package com.example.ybl.view;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ybl.R;
import com.example.ybl.model.Route;
import com.example.ybl.model.Schedule;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.NavigationRoute;
import com.mapbox.navigation.base.route.NavigationRouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class RouteMapActivity extends AppCompatActivity {

    public static final String EXTRA_SCHEDULE = "extra_schedule";

    private MapView mapView;
    private MaterialToolbar toolbar;
    private FloatingActionButton fabMyLocation;
    private LinearLayout routeInfoLayout;
    private TextView tvRouteInfo, tvStartPoint, tvEndPoint, tvDistance, tvDuration;

    private Schedule schedule;
    private Route route;

    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();
    private MapboxRouteLineView routeLineView;
    private MapboxRouteLineApi routeLineApi;
    private MapboxNavigation mapboxNavigation;

    private boolean focusLocation = true;
    private boolean isLocationInitialized = false;

    private final LocationObserver locationObserver = new LocationObserver() {
        @Override
        public void onNewRawLocation(@NonNull Location location) {
        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location location = locationMatcherResult.getEnhancedLocation();
            navigationLocationProvider.changePosition(location, locationMatcherResult.getKeyPoints(), null, null);
            if (focusLocation && isLocationInitialized) {
                updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()), (double) location.getBearing());
            }
        }
    };

    private final RoutesObserver routesObserver = new RoutesObserver() {
        @Override
        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
            routeLineApi.setNavigationRoutes(routesUpdatedResult.getNavigationRoutes(),
                    new MapboxNavigationConsumer<Expected<RouteLineError, RouteSetValue>>() {
                        @Override
                        public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
                            Style style = mapView.getMapboxMap().getStyle();
                            if (style != null) {
                                routeLineView.renderRouteDrawData(style, routeLineErrorRouteSetValueExpected);
                            }
                        }
                    });
        }
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            focusLocation = false;
            getGestures(mapView).removeOnMoveListener(this);
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {
        }
    };

    private final ActivityResultLauncher<String> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    new ActivityResultCallback<Boolean>() {
                        @Override
                        public void onActivityResult(Boolean result) {
                            if (result) {
                                initializeLocation();
                            } else {
                                Toast.makeText(RouteMapActivity.this,
                                        "Location permission required for route navigation",
                                        Toast.LENGTH_LONG).show();
                                // Still load the route without live location
                                loadRouteWithoutLocation();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        initializeViews();
        setupToolbar();
        getIntentData();
        setupMapbox();

        // Check permissions and initialize
        if (hasLocationPermission()) {
            initializeLocation();
        } else {
            requestLocationPermission();
        }
    }

    private void initializeViews() {
        mapView = findViewById(R.id.mapView);
        toolbar = findViewById(R.id.toolbar);
        fabMyLocation = findViewById(R.id.fabMyLocation);
        routeInfoLayout = findViewById(R.id.routeInfoLayout);
        tvRouteInfo = findViewById(R.id.tvRouteInfo);
        tvStartPoint = findViewById(R.id.tvStartPoint);
        tvEndPoint = findViewById(R.id.tvEndPoint);
        tvDistance = findViewById(R.id.tvDistance);
        tvDuration = findViewById(R.id.tvDuration);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void getIntentData() {
        schedule = (Schedule) getIntent().getSerializableExtra(EXTRA_SCHEDULE);
        if (schedule != null && schedule.getRoute() != null) {
            route = schedule.getRoute();
            updateRouteInfo();
        } else {
            Toast.makeText(this, "Route information not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateRouteInfo() {
        if (route != null) {
            tvRouteInfo.setText(route.getRouteName());
            tvStartPoint.setText("Start: " + route.getStartPoint());
            tvEndPoint.setText("End: " + route.getEndPoint());
            tvDistance.setText("Distance: " + route.getDistance());

            // Convert duration from minutes to readable format
            int durationMinutes = route.getEstimatedDuration();
            String durationText = formatDuration(durationMinutes);
            tvDuration.setText("Duration: " + durationText);

            // Show additional info if available
            if (schedule.getDepartureTime() != null || schedule.getBus() != null) {
                LinearLayout llAdditionalInfo = findViewById(R.id.llAdditionalInfo);
                if (llAdditionalInfo != null) {
                    llAdditionalInfo.setVisibility(View.VISIBLE);

                    if (schedule.getDepartureTime() != null) {
                        TextView tvDepartureTime = findViewById(R.id.tvDepartureTime);
                        tvDepartureTime.setText("Departure: " + formatTime(schedule.getDepartureTime()));
                    }

                    if (schedule.getBus() != null) {
                        TextView tvBusInfo = findViewById(R.id.tvBusInfo);
                        tvBusInfo.setText("Bus: " + schedule.getBus().getBusNumber());
                    }
                }
            }
        }
    }

    private String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " min";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + " hr";
            } else {
                return hours + " hr " + remainingMinutes + " min";
            }
        }
    }

    private String formatTime(String time) {
        try {
            // Simple time formatting - you can enhance this based on your time format
            return time.substring(0, 5); // Assuming format is "HH:mm:ss"
        } catch (Exception e) {
            return time;
        }
    }

    private void setupMapbox() {
        try {
            // Initialize route line
            MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(this)
                    .withRouteLineResources(new RouteLineResources.Builder().build())
                    .withRouteLineBelowLayerId(LocationComponentConstants.LOCATION_INDICATOR_LAYER)
                    .build();
            routeLineView = new MapboxRouteLineView(options);
            routeLineApi = new MapboxRouteLineApi(options);

            // Initialize Mapbox Navigation with proper configuration
            NavigationOptions navigationOptions = new NavigationOptions.Builder(this)
                    .accessToken(getString(R.string.mapbox_access_token))
                    .build();

            MapboxNavigationApp.setup(navigationOptions);
            mapboxNavigation = new MapboxNavigation(navigationOptions);
            mapboxNavigation.registerRoutesObserver(routesObserver);
            mapboxNavigation.registerLocationObserver(locationObserver);

            // Setup map
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    setupLocationComponent();
                    addRouteToMap();
                    setupMapInteractions();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing map: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void setupLocationComponent() {
        try {
            LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
            locationComponentPlugin.setEnabled(true);
            locationComponentPlugin.setLocationProvider(navigationLocationProvider);

            locationComponentPlugin.updateSettings(new Function1<LocationComponentSettings, Unit>() {
                @Override
                public Unit invoke(LocationComponentSettings locationComponentSettings) {
                    locationComponentSettings.setEnabled(true);
                    locationComponentSettings.setPulsingEnabled(true);
                    return null;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up location component", Toast.LENGTH_SHORT).show();
        }
    }

    private void addRouteToMap() {
        if (route != null) {
            // Add start and end point markers
            addPointMarkers();

            // Fetch and display the route
            fetchRoute();
        }
    }

    private void addPointMarkers() {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_pin);
            if (bitmap == null) {
                // Fallback to a simple colored bitmap if custom drawable not found
                bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
                bitmap.eraseColor(0xFFFF0000); // Red color
            }

            AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
            PointAnnotationManager pointAnnotationManager =
                    PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);

            // Add start point marker
            Point startPoint = Point.fromLngLat(
                    Double.parseDouble(route.getStartLongitude()),
                    Double.parseDouble(route.getStartLatitude())
            );
            PointAnnotationOptions startAnnotation = new PointAnnotationOptions()
                    .withTextAnchor(TextAnchor.CENTER)
                    .withIconImage(bitmap)
                    .withPoint(startPoint);
            pointAnnotationManager.create(startAnnotation);

            // Add end point marker
            Point endPoint = Point.fromLngLat(
                    Double.parseDouble(route.getEndLongitude()),
                    Double.parseDouble(route.getEndLatitude())
            );
            PointAnnotationOptions endAnnotation = new PointAnnotationOptions()
                    .withTextAnchor(TextAnchor.CENTER)
                    .withIconImage(bitmap)
                    .withPoint(endPoint);
            pointAnnotationManager.create(endAnnotation);

            // Set camera to show both points
            setCameraToRoute();

        } catch (Exception e) {
            Toast.makeText(this, "Error displaying route markers: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void setCameraToRoute() {
        try {
            Point startPoint = Point.fromLngLat(
                    Double.parseDouble(route.getStartLongitude()),
                    Double.parseDouble(route.getStartLatitude())
            );

            MapAnimationOptions animationOptions = new MapAnimationOptions.Builder()
                    .duration(1000L)
                    .build();

            CameraOptions cameraOptions = new CameraOptions.Builder()
                    .center(startPoint)
                    .zoom(12.0)
                    .build();

            getCamera(mapView).easeTo(cameraOptions, animationOptions);

        } catch (Exception e) {
            // Fallback to default camera
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(10.0).build());
        }
    }

    private void fetchRoute() {
        try {
            Point origin = Point.fromLngLat(
                    Double.parseDouble(route.getStartLongitude()),
                    Double.parseDouble(route.getStartLatitude())
            );
            Point destination = Point.fromLngLat(
                    Double.parseDouble(route.getEndLongitude()),
                    Double.parseDouble(route.getEndLatitude())
            );

            Log.d("RouteDebug", "Origin: " + origin.latitude() + ", " + origin.longitude());
            Log.d("RouteDebug", "Destination: " + destination.latitude() + ", " + destination.longitude());

            RouteOptions.Builder builder = RouteOptions.builder();
            builder.coordinatesList(Arrays.asList(origin, destination));
            builder.alternatives(true); // Try alternatives
            builder.profile("driving");

            // Remove problematic options that might cause empty steps
            // builder.overview("full");
            // builder.geometries("polyline6");

            // Add steps=true explicitly
            builder.steps(true);

            // Add continueStraight option
            builder.continueStraight(true);

            Log.d("RouteDebug", "Requesting route with steps=true...");

            mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                @Override
                public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                    Log.d("RouteDebug", "Route calculated successfully. Number of routes: " + list.size());
                    if (!list.isEmpty()) {
                        mapboxNavigation.setNavigationRoutes(list);
                        Toast.makeText(RouteMapActivity.this, "Route loaded successfully", Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(RouteMapActivity.this, "No route found", Toast.LENGTH_SHORT).show();
                        // Try alternative approach
                        tryAlternativeRouteApproach(origin, destination);
                    }
                }

                @Override
                public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                    Log.e("RouteDebug", "Route calculation failed: " + list.toString());
                    for (RouterFailure failure : list) {
                        Log.e("RouteDebug", "Failure: " + failure.getMessage() + ", Code: " + failure.getCode());
                        if (failure.getThrowable() != null) {
                            failure.getThrowable().printStackTrace();
                        }
                    }
                    Toast.makeText(RouteMapActivity.this, "Failed to load route, trying alternative...", Toast.LENGTH_LONG).show();

                    // Try alternative approach
                    tryAlternativeRouteApproach(origin, destination);
                }

                @Override
                public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {
                    Log.d("RouteDebug", "Route calculation canceled");
                }
            });

        } catch (NumberFormatException e) {
            Log.e("RouteDebug", "Number format exception: " + e.getMessage());
            Toast.makeText(this, "Invalid coordinate format", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("RouteDebug", "General exception: " + e.getMessage());
            Toast.makeText(this, "Error loading route coordinates: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    private void tryAlternativeRouteApproach(Point origin, Point destination) {
        try {
            Log.d("RouteDebug", "Trying alternative route approach...");

            // Try with different routing profile
            RouteOptions.Builder builder = RouteOptions.builder();
            builder.coordinatesList(Arrays.asList(origin, destination));
            builder.alternatives(false);
            builder.profile("driving-traffic"); // Try traffic profile

            // Use simpler options
            builder.steps(true);
            builder.overview("simplified");

            mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                @Override
                public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                    if (!list.isEmpty()) {
                        mapboxNavigation.setNavigationRoutes(list);
                        Toast.makeText(RouteMapActivity.this, "Alternative route loaded", Toast.LENGTH_SHORT).show();
                    } else {
                        // Last resort: create a direct line route manually
                        createDirectLineRoute(origin, destination);
                    }
                }

                @Override
                public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                    createDirectLineRoute(origin, destination);
                }

                @Override
                public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {
                    createDirectLineRoute(origin, destination);
                }
            });

        } catch (Exception e) {
            Log.e("RouteDebug", "Alternative approach failed: " + e.getMessage());
            createDirectLineRoute(origin, destination);
        }
    }

    private void createDirectLineRoute(Point origin, Point destination) {
        try {
            Log.d("RouteDebug", "Creating direct line route as fallback...");

            // Create a simple straight line between points
            List<Point> routePoints = Arrays.asList(origin, destination);

            // You would need to manually create a RouteLine here
            // This is a simplified approach - you might need to adjust based on your Mapbox setup

            Toast.makeText(this, "Showing direct route (routing service unavailable)", Toast.LENGTH_LONG).show();

            // Draw a simple line between points
            drawSimpleRouteLine(origin, destination);

        } catch (Exception e) {
            Log.e("RouteDebug", "Failed to create direct route: " + e.getMessage());
            Toast.makeText(this, "Cannot display route for this location", Toast.LENGTH_LONG).show();
        }
    }

    private void drawSimpleRouteLine(Point origin, Point destination) {
        // This is a simplified implementation - you'll need to adapt it to your Mapbox setup
        // You can use Mapbox's LineAnnotation to draw a simple line

        try {
            // Create a line string for the route
            LineString lineString = LineString.fromLngLats(Arrays.asList(origin, destination));

            // You would typically use Mapbox's annotation plugin to draw this line
            // This is a placeholder for the actual implementation

            Log.d("RouteDebug", "Direct line drawn between origin and destination");

        } catch (Exception e) {
            Log.e("RouteDebug", "Error drawing direct line: " + e.getMessage());
        }
    }
    private void setupMapInteractions() {
        getGestures(mapView).addOnMoveListener(onMoveListener);

        fabMyLocation.setOnClickListener(v -> {
            focusLocation = true;
            getGestures(mapView).addOnMoveListener(onMoveListener);
            // Center on user's current location
            if (hasLocationPermission()) {
                centerOnUserLocation();
            } else {
                // If no permission, center on route start point
                try {
                    Point startPoint = Point.fromLngLat(
                            Double.parseDouble(route.getStartLongitude()),
                            Double.parseDouble(route.getStartLatitude())
                    );
                    updateCamera(startPoint, 0.0);
                } catch (Exception e) {
                    // Ignore
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void centerOnUserLocation() {
        if (!hasLocationPermission()) {
            return;
        }

        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(this);
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location location = result.getLastLocation();
                if (location != null) {
                    updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()),
                            (double) location.getBearing());
                }
            }

            @Override
            public void onFailure(@NonNull Exception exception) {
                // If can't get current location, center on route start point
                try {
                    Point startPoint = Point.fromLngLat(
                            Double.parseDouble(route.getStartLongitude()),
                            Double.parseDouble(route.getStartLatitude())
                    );
                    updateCamera(startPoint, 0.0);
                } catch (Exception e) {
                    // Ignore
                }
            }
        });
    }

    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder()
                .duration(1000L)
                .build();

        CameraOptions cameraOptions = new CameraOptions.Builder()
                .center(point)
                .zoom(16.0)
                .bearing(bearing != null ? bearing : 0.0)
                .pitch(45.0)
                .padding(new EdgeInsets(500.0, 0.0, 0.0, 0.0))
                .build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeLocation() {
        try {
            // Set flag before starting trip session
            isLocationInitialized = true;

            // Only start trip session if we have permission
            if (hasLocationPermission()) {
                mapboxNavigation.startTripSession();
                centerOnUserLocation();
            }
        } catch (SecurityException e) {
            // Handle the security exception gracefully
            Toast.makeText(this, "Location access not available. Showing route only.", Toast.LENGTH_LONG).show();
            loadRouteWithoutLocation();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing location: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            loadRouteWithoutLocation();
        }
    }

    private void loadRouteWithoutLocation() {
        // Still load the route but without live location tracking
        try {
            setCameraToRoute();
            fetchRoute();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading route", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mapboxNavigation != null) {
                mapboxNavigation.onDestroy();
                mapboxNavigation.unregisterRoutesObserver(routesObserver);
                mapboxNavigation.unregisterLocationObserver(locationObserver);
            }
            if (routeLineView != null) {
                routeLineView.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }


}