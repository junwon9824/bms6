package com.kplo.bms4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.sdk.user.UserApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class broadcast_notice_master extends AppCompatActivity /*implements AdapterView.OnItemClickListener*/ {

    ArrayList<HashMap<String, String>> ti_con;
    ArrayList<String> title, content;
    HashMap<String, String> map;
    String[] ti_data, con_data;
    Integer size;
    ListView listView;
    AlertDialog alertDialog;
    String vid2;
    ImageButton img, plus2;
    String title2,vname,name;
    TextView toolbar;
    private final static String TAG = "broadcast";

    ObjectMapper mapper = new ObjectMapper();
    ArrayList<HashMap<String, String>> map2;
    private RequestQueue mqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_notice);
        Intent intent=getIntent();
        vid2=intent.getStringExtra("vid");
        vname = intent.getStringExtra("vname");
        name = intent.getStringExtra("name");

        mqueue = Volley.newRequestQueue(this);

        toolbar = findViewById(R.id.toolbar_title);
        toolbar.setText(vname + "마을이장" + name + "님");

        img = findViewById(R.id.logout);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        return null;
                    }
                });

                Intent intent = new Intent(broadcast_notice_master.this, MainActivity.class);
                startActivity(intent);


                finish();


            }
        });


        listView = findViewById(R.id.broad_list);
/*

        String str = "[   \n" +
                "    {\n" +
                "        \"id\": \"200\",\n" +
                "        \"name\": \"Alexia Milano\",\n" +
                "        \"email\": \"minalo@gmail.com\",\n" +
                "        \"prenom\": \"xx-xx-xxxx,x - street, x - country\"\n" +
                "\n" +
                "    }, {\n" +
                "        \"id\": \"201\",\n" +
                "        \"name\": \"Johnny Depp\",\n" +
                "        \"email\": \"johnny_depp@gmail.com\",\n" +
                "        \"prenom\": \"xx-xx-xxxx,x - street, x - country\"\n" +
                "\n" +
                "    }\n" +
                "]";

        Log.d(" asdsadsad", " 11" + str);

*/







        String url = " http://10.0.2.2:8080/api/villages/"+vid2+"/files";
        Log.d(" ",""+url);

/*
        try {

            List<Map<String, Object>> paramMap = new ObjectMapper().readValue(str, new TypeReference<List<Map<String, Object>>>() {
            });
            Log.d(" asdsadsad", " 121212" + paramMap.get(1).get("name"));

        } catch (IOException e) {
            Log.d("error2222", "");
            e.printStackTrace();

        }*/


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.isEmpty()) {
                    System.out.println("no data");

                    return;
                } else {

                    try {

                        List<Map<String, Object>> paramMap = new ObjectMapper().readValue(response, new TypeReference<List<Map<String, Object>>>() {
                        });

                        // Class class = new ObjectMapper().readValue(response, Class.class);

                        Log.d(" asdsadsad", " prm" + paramMap);
                        Log.d(" asdsadsad", " size" + paramMap.size());
                        size = paramMap.size();

                        String[] ti_data = new String[paramMap.size()];
                        String[] con_data = new String[paramMap.size()];

                        for (int i = 0; i < paramMap.size(); i++) {

                            ti_data[i] = paramMap.get(i).get("title").toString();
                            Log.d(TAG, " tidata  " + ti_data[i]);

                            con_data[i] = paramMap.get(i).get("contents").toString();
                            Log.d(TAG, " condata  " + con_data[i]);

                        }

                        ArrayAdapter adapter = new ArrayAdapter(broadcast_notice_master.this, R.layout.broadcast_content, R.id.simple_title, ti_data);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                                Log.d(TAG, "flag2 ");
                                Log.d(TAG, " condata111  " + con_data[i]);
                                Log.d(TAG, " ta111  " + ti_data[i]);


                                AlertDialog.Builder builder = new AlertDialog.Builder(broadcast_notice_master.this);

                                builder.setTitle(ti_data[i] + " 방송 내용");
                                builder.setMessage(con_data[i]);
                                builder.setPositiveButton("확인", null);

                                alertDialog = builder.create();
                                alertDialog.show();

                            }
                        });


                    } catch (IOException e) {
                        Log.d("error2222", "");
                        e.printStackTrace();

                    }


                    Log.d(TAG, "handleSignInResult:size2 " + response);


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, " add users to village  " );


            }
        });

        stringRequest.setTag(TAG);
        mqueue.add(stringRequest);



/*

        try {
            map2=mapper.readValue(str, Map.class);
            title2=map2.get("title");

            Log.d(" "," "+title2);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/

    }


/*

    public static List<Map<String, Object>> getListMapFromJsonArray(JSONArray jsonArray )
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        if( jsonArray != null )
        {
            int jsonSize = jsonArray.size();
            for( int i = 0; i < jsonSize; i++ )
            {
                Map<String, Object> map = JsonUtil.getMapFromJsonObject( (JSONObject) jsonArray.get(i) );
                list.add( map );
            }
        }

        return list;
    }
*/


}