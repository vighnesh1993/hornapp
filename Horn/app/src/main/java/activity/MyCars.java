package activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeTransform;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.horn.workshop.MyCarDetail;
import com.horn.workshop.R;
import com.horn.workshop.RecyclerItemClickListener;
import com.horn.workshop.SMLocalStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adapters.MyCarAdapter;
import app.AppConfig;
import app.AppController;
import data.CarData;
import helper.SQLiteHandler;
import helper.SwipeToRemoveMycar;


/**
 * Created by user on 05-02-2016.
 */
public class MyCars extends AppCompatActivity  {

    private Toolbar toolbar;
    ProgressDialog pDialog;
    String [] nameArray,carImageArray,carIdArray;
    RecyclerView rCarView;
    SMLocalStore smLocalStore;
    public static ArrayList<CarData> carDatas;
    private MyCarAdapter adapter;
    SQLiteHandler sqLiteHandler;
    GestureDetectorCompat gestureDetector;
    ActionMode actionMode;
    public int car_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setAllowReturnTransitionOverlap(true);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setSharedElementExitTransition(new ChangeTransform());
        setContentView(R.layout.my_cars);

        toolbar = (Toolbar) findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loding...");
        pDialog.setCancelable(false);
        show_mycars();
    }
    public void show_mycars()
    {
        pDialog.show();
    /*
    *Datas from DB starts
    */
        String strreq = "req";

        //final ProgressDialog loading;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_ADDCARDETAIL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sdsdsd", "car Response: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        String status_car = jsonObject.getString("status");
                        Log.d("stat",status_car);
                        if(status_car.equals("Have_cars")) {
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

                            mycarsDisplay();
                        }
                        else
                        {

                            Toast.makeText(MyCars.this, "You have not yet added any cars", Toast.LENGTH_LONG).show();
                        }
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
                        Toast.makeText(MyCars.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                pDialog.dismiss();
                String get_mycars = "get_mycars";
                sqLiteHandler = new SQLiteHandler(MyCars.this);
                HashMap<String, String> user = sqLiteHandler.getUserDetails();
                String  apmnt_user_email = user.get("email");
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("get_mycars", get_mycars);
                params.put("get_mycars_email", apmnt_user_email);
                return params;
            }

        };


        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }
    public void mycarsDisplay()
    {
        rCarView = (RecyclerView) findViewById(R.id.my_car_rView);
        rCarView.setHasFixedSize(true);
        rCarView.setLayoutManager(new LinearLayoutManager(this));
        rCarView.setItemAnimator(new DefaultItemAnimator());
        car_count = nameArray.length;
        carDatas = new ArrayList<CarData>();
        for (int i = 0; i < nameArray.length; i++) {
            carDatas.add(new CarData(
                    nameArray[i],
                    carImageArray[i],
                    carIdArray[i]
            ));
        }
        adapter = new MyCarAdapter(carDatas);
        rCarView.setAdapter(adapter);

        rCarView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        CarData carData = carDatas.get(position);
                        String car_id = carData.getCarId();
                        smLocalStore = new SMLocalStore(MyCars.this);
                        smLocalStore.setProfileMyCar(car_id);
                        Intent intent1 = new Intent(MyCars.this, MyCarDetail.class);
                        startActivity(intent1);
                        //  Toast.makeText(getApplicationContext(), "" + car_id, Toast.LENGTH_LONG).show();

                    }
                })
        );
        ItemTouchHelper.Callback callback = new SwipeToRemoveMycar(adapter,rCarView);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rCarView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_car_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            if(car_count<2)
            startActivity(new Intent(MyCars.this, AddCar.class));
            else
                Toast.makeText(MyCars.this, "We are sorry. You can add only maximum of 2 cars", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

public void carcount()
{
    car_count = car_count-1;
}



}
