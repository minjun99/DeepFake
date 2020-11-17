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

public class RegisterActivity extends AppCompatActivity {
    private EditText mEmailEdit;
    private EditText mUsernameEdit;
    private EditText mPasswordEdit;

    private Button mLoginButton;
    private Button mSignUpButton;

    private String mRequestUrl = "https://us-central1-rational-lambda-293611.cloudfunctions.net/request_handler";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailEdit = (EditText) findViewById(R.id.email);
        mUsernameEdit = (EditText) findViewById(R.id.username);
        mPasswordEdit = (EditText) findViewById(R.id.password);

        mSignUpButton = (Button) findViewById(R.id.sign_up_complete);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignUpRequest();
            }
        });

        mLoginButton = (Button)findViewById(R.id.login);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private JSONObject getSignUpBody() {
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("email", mEmailEdit.getText().toString());
        jsonBody.put("name", mUsernameEdit.getText().toString());
        jsonBody.put("password", mPasswordEdit.getText().toString());

        return jsonBody;
    }

    public void sendSignUpRequest() {
        final String url = mRequestUrl + "?mode=account_signup";
        final String body = getSignUpBody().toString();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
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
                                Toast.makeText(getApplicationContext(), "회원가입 성공", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (status == 200 && msg.equals("using email")) {
                                Toast.makeText(getApplicationContext(), "이미 사용중인 이메일입니다", Toast.LENGTH_LONG).show();
                            } else if (status == 200 && msg.equals("using name")) {
                                Toast.makeText(getApplicationContext(), "이미 사용중인 이름입니다", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                            }
                        } catch (ParseException e) {
                            Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG).show();
                        Log.d("onErrorResponse", "에러 -> " + error.getMessage());
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", body, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mode", "account_signup");
                return params;
            }
        };

        request.setShouldCache(false);
        AppHelper.requestQueue = Volley.newRequestQueue(this);
        AppHelper.requestQueue.add(request);
    }
}