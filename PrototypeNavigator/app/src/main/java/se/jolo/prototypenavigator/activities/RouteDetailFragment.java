package se.jolo.prototypenavigator.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.deserializers.StopItemViewAdapter;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.model.RouteItem;
import se.jolo.prototypenavigator.task.ImageLoader;
import se.jolo.prototypenavigator.utils.SimpleDividerItemDecoration;
import se.jolo.prototypenavigator.utils.UrlBuilder;


public class RouteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    ImageView image;
    private RouteItem routeItem;
    private Route route;
    private CollapsingToolbarLayout appBarLayout;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RouteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey("route")) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            route = getArguments().getParcelable("route");
            List<RouteItem> routeItems = route.getRouteItems();
            int uuid = getArguments().getInt(ARG_ITEM_ID);
            for(RouteItem r : routeItems){
                if (uuid == (r.getPrimaryStopPointItemUuid())){
                    routeItem = r;
                }
            }



            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(routeItem.getStopPointItems().get(0).getDeliveryAddress());


            }

            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(new UrlBuilder(routeItem.getStopPointItems().get(0).getEasting(),routeItem.getStopPointItems().get(0).getNorthing()).getUrl());
            image = (ImageView) activity.findViewById(R.id.image);

            try {
                Bitmap photo = imageLoader.get();
                if(photo!=null){
                image.setImageBitmap(photo);}
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }



            View recyclerView = activity.findViewById(R.id.route_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView,appBarLayout,getContext());
            appBarLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            appBarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
            //setPalette();
        }
    }

    private void setPalette() {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
                int primary = getResources().getColor(R.color.colorPrimary);
                appBarLayout.setContentScrimColor(palette.getMutedColor(primary));
                appBarLayout.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, CollapsingToolbarLayout collapsingToolbarLayout,Context context) {
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setAdapter(new StopItemViewAdapter(routeItem.getStopPointItems(), collapsingToolbarLayout));
    }


}
