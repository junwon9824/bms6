package com.alddeul.capstone;

import static android.speech.tts.TextToSpeech.ERROR;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class broadcastActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    String TAG = "broadcast";
    MainActivity m = new MainActivity();

    Button sttBtn;
    TextView textView, toolbar, nowtime;
    EditText fileid2;
    final int PERMISSION = 1;
    private RequestQueue queue;
    String userid, email, vid2, id, content = "", vname, name;
    Dialog dialog;
    Button listen_record;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_record);

        queue = Volley.newRequestQueue(this);

        Intent intent2 = getIntent();
        vid2 = intent2.getStringExtra("vid");

        id = intent2.getStringExtra("id");
        vname = intent2.getStringExtra("vname");
        name = intent2.getStringExtra("name");
        /////
        toolbar = findViewById(R.id.toolbar_title);
        toolbar.setText(vname + "????????????" + name + "???");

        dialog = new Dialog(this);       // Dialog ?????????
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
        dialog.setContentView(R.layout.dialog_play);             // xml ???????????? ????????? ??????


        if (Build.VERSION.SDK_INT >= 23) { // ????????? ??????

            ActivityCompat.requestPermissions
                    (this, new String[]{Manifest.permission.INTERNET,
                            Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        sttBtn = (Button) findViewById(R.id.play_button);
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        sttBtn.setOnClickListener
                (v -> {
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                    mRecognizer.setRecognitionListener(listener);
                    mRecognizer.startListening(intent);

                });


        listen_record = findViewById(R.id.listen_record);
        listen_record.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) // LOLLIPOP?????? ??????????????? ?????? ??????
            @Override
            public void onClick(View v) {
                Log.d("test","content"+content);
                speak(content);
            }
        });



        Button btn = findViewById(R.id.send);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = m.serverip+"api/admins/" + id + "/files";
                Log.d(TAG, "url" + url);

                addfiles(url);


            }
        });
    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "??????????????? ???????????????.",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "??????????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "???????????? ??????";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "?????? ??? ??????";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER??? ??????";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "????????? ?????????";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "????????? ????????????";
                    break;
                default:
                    message = "??? ??? ?????? ?????????";
                    break;
            }

            Toast.makeText(getApplicationContext(), "????????? ?????????????????????. : " + message, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // ?????? ?????? ArrayList??? ????????? ?????? textView??? ????????? ???????????????.

            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for (int i = 0; i < matches.size(); i++) {
/*
*/
                content += matches.get(i);
            }

        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

/*

    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
*/

    public void addfiles(String url) {
        String TAG = "broadcast";

        JSONObject js = new JSONObject();
        try {
            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String title2 = dateFormat.format(date);
            Long vid3 = Long.parseLong(vid2);
            nowtime = findViewById(R.id.now_time);
            nowtime.setText(title2);
            js.put("villageId", vid3);
            js.put("title", title2);
            js.put("contents", content);


            Log.d("broad", "contents " + content);
            Log.d("broad", "villageId " + vid2);
            Log.d("broad", "title " + title2);

            content = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make request for JSONObject
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString() + " i am queen");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Errornot send: " + error.getMessage());
            }
        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };


        Volley.newRequestQueue(this).add(jsonObjReq);


    }

    public void showDialog() {
        dialog.show(); // ??????????????? ?????????

    }

    private void speak(String text) {
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    int result = tts.setLanguage(Locale.KOREA); // ?????? ??????
                    if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                        Log.e("TTS", "This Language is not supported");
                    } else {
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });
    }


}

