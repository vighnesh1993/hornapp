package com.horn.workshop;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.manuelpeinado.fadingactionbar.FadingActionBarHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class ScheduledMaintenanceDetail extends Activity {
    public SMLocalStore smLocalStore;
    private ProgressDialog pDialog;
    public String phone, name, category, address, workshopid, rating, profilepic, coordinates, offers, offdays;
    Integer pic;
    TextView ratings, workshopdetail_phone, workshopdetail_name, workshopdetail_address, abTitle;
    final ColorDrawable cd = new ColorDrawable(Color.rgb(68, 74, 83));
    private ImageButton call_dialer, location_redirect, share_detail, favorite;
    LatLng latLng1, latLng2;
    private double coordLatitude = 0.0;
    private double coordLongitude = 0.0;
    ScheduledMaintenanceWorkshoplist sw;
    String dist;
    float price_detail;
    ActionBar tAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.scheduled_maintenance_detail);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.drawable.ab_background)
                .headerLayout(R.layout.header)
                .parallax(false)
                .contentLayout(R.layout.scheduled_maintenance_detail);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        tAction = getActionBar();
        int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
        abTitle = (TextView) findViewById(titleId);
        abTitle.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.icons));

        smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);

        call_dialer = (ImageButton) findViewById(R.id.call_dialer);
        location_redirect = (ImageButton) findViewById(R.id.location_redirect);
        share_detail = (ImageButton) findViewById(R.id.share_detail);
        favorite = (ImageButton) findViewById(R.id.favorite);
        workshopdetail_phone = (TextView) findViewById(R.id.workshopdetail_phone);
        workshopdetail_name = (TextView) findViewById(R.id.workshopdetail_name);
        workshopdetail_address = (TextView) findViewById(R.id.workshopdetail_address);

        UserLocalStore userLocalStore = new UserLocalStore(this);
        String latlng1 = userLocalStore.getMylocationLatlog();

        String[] ltlg = latlng1.split(",");
        String ltlg1 = ltlg[0]; // 004
        String ltlg2 = ltlg[1];

        double d1 = Double.parseDouble(ltlg1);
        double d2 = Double.parseDouble(ltlg2);


        latLng1 = new LatLng(d1, d2);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading ...");
        pDialog.show();
        // String workshopid = "608";

        String workshopid = smLocalStore.getSMworkshopdetail_id();
        Button booknow = (Button) findViewById(R.id.book_now);
        booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smLocalStore.setSMworkshopname(name);

                String offer_sm = (String.valueOf(price_detail)).replaceAll("[^\\ds.]", "");
                smLocalStore.setOffer_total(offer_sm);
                startActivity(new Intent(ScheduledMaintenanceDetail.this, ScheduledMaintenanceAppointment.class));

            }
        });

        getdetailFromDb(workshopid);
        call_dialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence dialer_no = workshopdetail_phone.getText();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + String.valueOf(dialer_no)));
                startActivity(intent);
            }
        });
        share_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence wName = workshopdetail_name.getText();
                CharSequence wAddress = workshopdetail_address.getText();
                CharSequence dialer_no = workshopdetail_phone.getText();

                //create the send intent
                Intent shareIntent =
                        new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "Checkout Horn App..!");
                String shareMessage = String.valueOf(wName+"\n"+ wAddress +"\n" + dialer_no);
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        shareMessage);
                startActivity(Intent.createChooser(shareIntent,
                        "Choose an App to share with"));

            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "" +
                        "Workshop added to favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getdetailFromDb(final String workshopid) {
        String strreq = "workshop_detail";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.URL_SM_WORKSHOPDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject != null) {
                        int len = jsonObject.length();
                        Log.d("details:::", jsonObject.getString("number"));
                        phone = jsonObject.getString("number");
                        name = jsonObject.getString("name");
                        category = jsonObject.getString("category");
                        address = jsonObject.getString("address");
                        String workshopid = jsonObject.getString("workshopid");
                        rating = jsonObject.getString("rating");
                        profilepic = jsonObject.getString("profilepic");
                        coordinates = jsonObject.getString("coordinates");
                        offers = jsonObject.getString("offer");
                        offdays = "0";
                        String[] parts = coordinates.split(",");
                        String part1 = parts[0]; // 004
                        String part2 = parts[1];

                        smLocalStore.setSmdcoordinates(part1, part2);

                        coordLatitude = Double.parseDouble(part1);
                        coordLongitude = Double.parseDouble(part2);
                        latLng2 = new LatLng(coordLatitude, coordLongitude);
                        sw = new ScheduledMaintenanceWorkshoplist();
                        dist = sw.getDistance(latLng1, latLng2);


                        pic = R.drawable.workshop_sample;
                        workshopdisplay_detail();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScheduledMaintenanceDetail.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);

                Map<String, String> params = new HashMap<String, String>();
                params.put("workshopid_detail", workshopid);
                return params;
            }

        };

        AppController.getInstance().cancelPendingRequests("REQTAG");
        stringRequest.setTag("REQTAG");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, strreq);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void workshopdisplay_detail() {
        pDialog.dismiss();
        TextView workshopname = (TextView) findViewById(R.id.workshopdetail_name);
        TextView workshopphone = (TextView) findViewById(R.id.workshopdetail_phone);
        TextView workshopaddress = (TextView) findViewById(R.id.workshopdetail_address);
        TextView workshopcategory = (TextView) findViewById(R.id.workshopdetail_category);

        TextView offday = (TextView) findViewById(R.id.workshopdetail_Offday);
        TextView ws_distance = (TextView) findViewById(R.id.ws_distance);
        final ImageView workshopimage = (ImageView) findViewById(R.id.workshopdetail_photo);
        ratings = (TextView) findViewById(R.id.rating);
        String url = "http://blueripples.org/horn/ajax-data/profilepics/" + profilepic;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e(TAG, "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    workshopimage.setImageBitmap(response.getBitmap());
                }
            }
        });
        if ((category).equals("Authorised")) {
            category = "Exclusive";
        }
        workshopname.setText(name);
        String lName = name.toLowerCase();
        char alphabet = lName.charAt(0);
        setactionBarIconwithAlphabet(alphabet);
        abTitle.setText(name);
        workshopaddress.setText(address);
        workshopcategory.setText(category);
        workshopphone.setText(phone);

        int start = Integer.parseInt(offdays);
        int end = Integer.parseInt(offdays) + 1;
        final SpannableStringBuilder sb = new SpannableStringBuilder("SMTWTFS");

// Span to set text color to some RGB value
        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);

// Span to make text bold
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

// Set the text color for first 4 characters
        sb.setSpan(fcs, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

// make them also bold
        sb.setSpan(bss, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        offday.setText(sb);
        offday.setLetterSpacing(1);

        smLocalStore = new SMLocalStore(ScheduledMaintenanceDetail.this);
        float total = Float.parseFloat(smLocalStore.getService_total());
        float Offer = Float.parseFloat(offers);
        price_detail = total - ((Offer / 100) * total);
        // offer.setText("₹ "+String.valueOf(price));

        ratings.setText(rating);
        setRatingBackround(rating);
        ws_distance.setText(dist);
        ws_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ob = new Intent(ScheduledMaintenanceDetail.this, MapsActivity.class);
                ob.putExtra("workshop", name + "##!###" + address + "##!###" + rating + "##!###");
                startActivity(ob);
            }
        });

    }

    private void setRatingBackround(String ratingValue) {
        switch (ratingValue) {
            case "0":
                ratings.setBackgroundResource(R.drawable.rating_bg_0);
                break;
            case "0.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_0_5);
                break;
            case "1":
                ratings.setBackgroundResource(R.drawable.rating_bg_1);
                break;
            case "1.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_1_5);
                break;
            case "2":
                ratings.setBackgroundResource(R.drawable.rating_bg_2);
                break;
            case "2.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_2_5);
                break;
            case "3":
                ratings.setBackgroundResource(R.drawable.rating_bg_3);
                break;
            case "3.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_3_5);
                break;
            case "4":
                ratings.setBackgroundResource(R.drawable.rating_bg_4);
                break;
            case "4.5":
                ratings.setBackgroundResource(R.drawable.rating_bg_4_5);
                break;
            case "5":
                ratings.setBackgroundResource(R.drawable.rating_bg_5);
                break;
        }
    }

    private void setactionBarIconwithAlphabet(char alphabet) {
        char a = 'a', b = 'b', c = 'c', d = 'd', e = 'e', f = 'f', g = 'g', h = 'h', i = 'i', j = 'j', k = 'k', l = 'l', m = 'm',
                n = 'n', o = 'o', p = 'p', q = 'q', r = 'r', s = 's', t = 't', u = 'u', v = 'v', w = 'w', x = 'x', y = 'y', z = 'z';
        if (alphabet == a) {
            tAction.setIcon(R.drawable.a);
        } else if (alphabet == b) {
            tAction.setIcon(R.drawable.b);
        } else if (alphabet == c) {
            tAction.setIcon(R.drawable.c);
        } else if (alphabet == d) {
            tAction.setIcon(R.drawable.d);
        } else if (alphabet == e) {
            tAction.setIcon(R.drawable.e);
        } else if (alphabet == f) {
            tAction.setIcon(R.drawable.f);
        } else if (alphabet == g) {
            tAction.setIcon(R.drawable.g);
        } else if (alphabet == h) {
            tAction.setIcon(R.drawable.h);
        } else if (alphabet == i) {
            tAction.setIcon(R.drawable.i);
        } else if (alphabet == j) {
            tAction.setIcon(R.drawable.j);
        } else if (alphabet == k) {
            tAction.setIcon(R.drawable.k);
        } else if (alphabet == l) {
            tAction.setIcon(R.drawable.l);
        } else if (alphabet == m) {
            tAction.setIcon(R.drawable.m);
        } else if (alphabet == n) {
            tAction.setIcon(R.drawable.n);
        } else if (alphabet == o) {
            tAction.setIcon(R.drawable.o);
        } else if (alphabet == p) {
            tAction.setIcon(R.drawable.p);
        } else if (alphabet == q) {
            tAction.setIcon(R.drawable.q);
        } else if (alphabet == r) {
            tAction.setIcon(R.drawable.r);
        } else if (alphabet == s) {
            tAction.setIcon(R.drawable.s);
        } else if (alphabet == t) {
            tAction.setIcon(R.drawable.t);
        } else if (alphabet == u) {
            tAction.setIcon(R.drawable.u);
        } else if (alphabet == v) {
            tAction.setIcon(R.drawable.v);
        } else if (alphabet == w) {
            tAction.setIcon(R.drawable.w);
        } else if (alphabet == x) {
            tAction.setIcon(R.drawable.x);
        } else if (alphabet == y) {
            tAction.setIcon(R.drawable.y);
        } else {
            tAction.setIcon(R.drawable.z);
        }

    }
}
