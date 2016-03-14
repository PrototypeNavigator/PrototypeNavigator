package se.jolo.prototypenavigator.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.deserializers.StopItemViewAdapter;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.task.ImageLoader;
import se.jolo.prototypenavigator.utils.SimpleDividerItemDecoration;


public class RouteDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

    ImageView image;
    private RouteItem routeItem;
    private Route route;
    private CollapsingToolbarLayout appBarLayout;
    private Activity activity;
    private ImageLoader imageLoader;
    private View recyclerView;
 //   private final FragmentTransaction fragmentTransaction;

    public RouteDetailFragment() {}

    @SuppressLint("ValidFragment")
    public RouteDetailFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey("route")) {
                route = getArguments().getParcelable("route");
            }
        }else {
            route = savedInstanceState.getParcelable("route");
        }
        List<RouteItem> routeItems = route.getRouteItems();
        int uuid = getArguments().getInt(ARG_ITEM_ID);
        for (RouteItem r : routeItems) {
            if (uuid == (r.getPrimaryStopPointItemUuid())) {
                routeItem = r;
            }
        }


        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(routeItem.getStopPointItems().get(0).getDeliveryAddress());


        }

               /* imageLoader = new ImageLoader();
                imageLoader.execute(new UrlBuilderStreetview(routeItem.getStopPointItems().get(0).getEasting(), routeItem.getStopPointItems().get(0).getNorthing()).getUrl());
                image = (ImageView) activity.findViewById(R.id.image);

                try {
                    Bitmap photo = imageLoader.get();
                    if (photo != null) {
                        image.setImageBitmap(photo);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/

        if (savedInstanceState==null){
            recyclerView = getActivity().findViewById(R.id.route_list);
        }else{
            recyclerView = savedInstanceState.getParcelable("recycler");
        }

        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, appBarLayout, getContext());
        appBarLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        appBarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        //setPalette();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, CollapsingToolbarLayout collapsingToolbarLayout, Context context) {
        Context context1 = context;
        RecyclerView recyclerView1 = recyclerView;
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setAdapter(new StopItemViewAdapter(routeItem.getStopPointItems(), collapsingToolbarLayout, getActivity()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    //    fragmentTransaction.add(this, "fragment");
        outState.putParcelable("route", route);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}