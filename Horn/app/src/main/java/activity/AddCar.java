package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.horn.workshop.MainActivity;
import com.horn.workshop.ProfileAddCar;
import com.horn.workshop.R;
import com.horn.workshop.RecyclerItemClickListener;
import com.horn.workshop.SMLocalStore;
import com.horn.workshop.ScheduledMaintenanceDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.AddCarAdapter;
import app.AppConfig;
import app.AppController;
import data.CarData;

/**
 * Created by user on 06-02-2016.
 */
public class AddCar extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static ArrayList<CarData> carDatas;
    public static String[] nameArray;
    public static String[] carImageArray;
    public static String[] carIdArray;
    private ProgressDialog pDialog;
    private RecyclerView rCarView;
    private AddCarAdapter adapter;
    private SMLocalStore smLocalStore;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);

        toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loding...");
        pDialog.setCancelable(false);
        search_cars();
    }

    public void carsDisplay() {

          rCarView = (RecyclerView) findViewById(R.id.add_car_rView);
    rCarView.setHasFixedSize(true);
    rCarView.setLayoutManager(new LinearLayoutManager(this));
    rCarView.setItemAnimator(new DefaultItemAnimator());

    carDatas = new ArrayList<CarData>();
    for (int i = 0; i < nameArray.length; i++) {
        carDatas.add(new CarData(
                nameArray[i],
                carImageArray[i],
                carIdArray[i]
        ));
    }
    adapter = new AddCarAdapter(carDatas);
    rCarView.setAdapter(adapter);

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_car_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_add);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        adapter.setFilter(carDatas);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;

    }
    @Override
    public boolean onQueryTextChange(String newText) {
        final List<CarData> filteredModelList = filter(carDatas, newText);
        adapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<CarData> filter(List<CarData> models, String query) {
        query = query.toLowerCase();

        final List<CarData> filteredModelList = new ArrayList<>();
        for (CarData model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void search_cars() {
        pDialog.show();
    /*
    *Datas from DB starts
    */
        String strreq = "req";

        //final ProgressDialog loading;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_CARLIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
Log.d("sdsdsd", "car Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        JSONArray nameArrayj = jsonObject.getJSONArray("car_names");
                        JSONArray carImageArrayj = jsonObject.getJSONArray("car_image");
                        JSONArray carIdArrayj = jsonObject.getJSONArray("car_id");
                        nameArray = new String[nameArrayj.length()];
                        carImageArray = new String[carImageArrayj.length()];
                        carIdArray = new String[carIdArrayj.length()];
                        for (int i = 0; i < nameArrayj.length(); i++) {
                            nameArray[i] = nameArrayj.getString(i);
                            carImageArray[i] = carImageArrayj.getString(i);
                            carIdArray[i] = carIdArrayj.getString(i);
                        }
                        pDialog.dismiss();
                        carsDisplay();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(AddCar.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                String get_cars = "get_cars";
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_cars", get_cars);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }
}
