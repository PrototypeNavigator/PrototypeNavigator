package se.jolo.prototypenavigator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import se.jolo.prototypenavigator.R;
import se.jolo.prototypenavigator.adapters.RouteViewAdapter;
import se.jolo.prototypenavigator.model.Route;
import se.jolo.prototypenavigator.singleton.RouteHolder;


public class RouteListActivity extends AppCompatActivity {

    private Route route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        route = RouteHolder.INSTANCE.getRoute();

        setContentView(R.layout.activity_route_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.route_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);


    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RouteViewAdapter());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

            navigateUpTo(new Intent(this, RouteListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}