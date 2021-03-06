package se.jolo.prototypenavigator.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.adapters.StopItemViewAdapter;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.singleton.RouteHolder;
import se.jolo.prototypenavigator.task.ImageLoader;
import se.jolo.prototypenavigator.utils.SimpleDividerItemDecoration;
import se.jolo.prototypenavigator.utils.UrlBuilderStreetview;

/*The StopPointDetail class represents a stop point and holds a list with its StopPointItems.*/
public class StopPointDetail extends AppCompatActivity {
    private ImageView image;
    private Route route;
    private RouteItem routeItem;
    private CollapsingToolbarLayout appBarLayout;
    public static final String ARG_ITEM_ID = "item_id";

    public StopPointDetail() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        route = RouteHolder.INSTANCE.getRoute();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle.containsKey(ARG_ITEM_ID)) {
            List<RouteItem> routeItems = route.getRouteItems();
            String uuid = bundle.getString(ARG_ITEM_ID);
            for(RouteItem r : routeItems){
                if (uuid.equals(r.getPrimaryStopPointItemUuid())){
                    routeItem = r;
                }
            }

            appBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(StringUtils.capitalize(routeItem.getStopPointItems().get(0).getDeliveryAddress().toLowerCase()));
            }
            /*Loading street view image.*/
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(new UrlBuilderStreetview(routeItem.getStopPointItems().get(0).getEasting(), routeItem.getStopPointItems().get(0).getNorthing()).getUrl());
            image = (ImageView) this.findViewById(R.id.image);

            try {
                Bitmap photo = imageLoader.get();
                if(photo != null){
                    Drawable drawable = new BitmapDrawable(getResources(), photo);
                    image.setImageDrawable(drawable);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            View recyclerView = this.findViewById(R.id.route_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView, appBarLayout);
        }
    }

    /*Setting up the RecyclerView with StopPointItems*/
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, CollapsingToolbarLayout collapsingToolbarLayout) {
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setAdapter(new StopItemViewAdapter(routeItem.getStopPointItems(), collapsingToolbarLayout,this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, RouteList.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}