package se.jolo.prototypenavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.deserializers.RouteViewAdapter;
import se.jolo.prototypenavigator.model.Route;


public class RouteListActivity extends AppCompatActivity {

    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState !=null){
            route = savedInstanceState.getParcelable("route");
        }else{
            route = getIntent().getParcelableExtra("route");
        }

        setContentView(R.layout.activity_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.route_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RouteViewAdapter(route));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("route", route);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //

            navigateUpTo(new Intent(this, Map.class).putExtra("route", route));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
