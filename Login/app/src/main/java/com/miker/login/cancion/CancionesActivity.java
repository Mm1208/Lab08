package com.miker.login.cancion;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.miker.login.Helper.RecyclerItemTouchHelper;
import com.miker.login.Model;
import com.miker.login.NavDrawerActivity;
import com.miker.login.R;

import java.util.ArrayList;
import java.util.List;

public class CancionesActivity extends AppCompatActivity implements CancionesAdapter.CancionAdapterListener {
    private RecyclerView recyclerView;
    private CancionesAdapter adapter;
    private List<Cancion> carreraList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canciones);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.item_canciones));

        recyclerView = findViewById(R.id.recycler_view);
        carreraList = new ArrayList<>();
        model = new Model();
        carreraList = model.getCanciones();
        adapter = new CancionesAdapter(carreraList, this);
        coordinatorLayout = findViewById(R.id.main_content_canciones);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //should use database info

        //refresh view
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cursoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.putExtra("usuario", getIntent().getSerializableExtra("usuario"));
        startActivity(a);
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onSelected(Cancion cancion) { //TODO get the select item of recycleView
        Intent intent = new Intent(CancionesActivity.this, CancionActivity.class);
        model.setCancionSeleccionada(cancion);
        intent.putExtra("model", model);
        startActivityForResult(intent, 0);
    }
}
