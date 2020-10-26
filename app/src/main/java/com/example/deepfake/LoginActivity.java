package com.example.deepfake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailEdit;
    private EditText mPasswordEdit;

    private Button mLoginButton;
    private Button mSignUpButton;

    private String mRequestUrl = "https://us-central1-rational-lambda-293611.cloudfunctions.net/request_handler";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEdit = (EditText) findViewById(R.id.email);
        mPasswordEdit = (EditText) findViewById(R.id.password);

        mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginRequest();
            }
        });

        mSignUpButton = (Button) findViewById(R.id.sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        if (AppHelper.requestQueue != null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
    }

    private JSONObject getLoginBody() {
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("flag_1", "account");
        jsonBody.put("flag_2", "login");
        jsonBody.put("email", mEmailEdit.getText().toString());
        jsonBody.put("password", mPasswordEdit.getText().toString());

        return jsonBody;
    }

    public void sendLoginRequest() {
        final String mRequestBody = getLoginBody().toString();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                mRequestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONParser parser = new JSONParser();
                            Object obj = parser.parse(response);
                            JSONObject jsonObj = (JSONObject) obj;

                            Long status = (Long) jsonObj.get("status");
                            String msg = (String) jsonObj.get("msg");

                            if (status == 200 && msg.equals("success")) {
                                Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_LONG).show();
                        Log.d("onErrorResponse", "에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        request.setShouldCache(false); // 이전 결과 있어도 새로 요청하여 응답을 보여준다.
        AppHelper.requestQueue = Volley.newRequestQueue(this); // requestQueue 초기화 필수
        AppHelper.requestQueue.add(request);
    }
}