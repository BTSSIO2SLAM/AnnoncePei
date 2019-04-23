package com.example.annoncepei.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.annoncepei.Models.Utilisateur;
import com.example.annoncepei.R;
import com.example.annoncepei.Util.UserSession;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String DEMO_EMAIL = "espresso@spoon.com";
    private static final String DEMO_PASSWORD = "lemoncake";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;


    private String username;
    private String password;

    // User Session Manager Class
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        session = new UserSession(getApplicationContext());

        Button mEmailSignInButton = (Button) findViewById(R.id.submit);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        // User Session Manager
        sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);
        // get editor to edit in file
        editor = sharedPreferences.edit();
    }

    private void validateFields() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmailView.getText().toString()).matches()) {
            mEmailView.setError(getString(R.string.msg_email_error));
        } else {
            mEmailView.setError(null);
        }

        if (mPasswordView.getText().toString().isEmpty()) {
            mPasswordView.setError(getString(R.string.msg_password_error));
        } else {
            mPasswordView.setError(null);
        }
    }

    private void userLogin() {
        username = mEmailView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:59825/api/authenticate",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().contains("error")){
                            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject result = new JSONObject(response);
                                JSONObject user = (JSONObject) result.get("user");
                                int id = Integer.parseInt(user.getString("Id"));
                                session.createUserLoginSession(id, username,password);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setUsername(username);
                utilisateur.setPassword(password);
                Gson gson = new Gson();
                try {
                    return gson.toJson(utilisateur) == null ? null : gson.toJson(utilisateur).getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", gson.toJson(utilisateur), "utf-8");
                    return null;
                }
            }

            @Override
            public byte[] getPostBody() throws AuthFailureError {
                return super.getPostBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            userLogin();
            showProgress(false);
        }
    }
/*
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }*/

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 1;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
/*
        private HashMap<String, String> signIn() {

            HashMap<String, String> sessionTokens = null;

            EditText mEmailField = (EditText) findViewById(R.id.email);
            EditText mPasswordField = (EditText) findViewById(R.id.password);

            String email = mEmailField.getText().toString();
            String password = mPasswordField.getText().toString();

            DefaultHttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("http://yoursite.com/sessions");
            JSONObject holder = new JSONObject();
            JSONObject userObj = new JSONObject();

            try {
                userObj.put("password", password);
                userObj.put("email", email);
                holder.put("user", userObj);
                StringEntity se = new StringEntity(holder.toString());
                post.setEntity(se);
                post.setHeader("Accept", "application/json");
                post.setHeader("Content-Type", "application/json");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException js) {
                js.printStackTrace();
            }

            String response = null;
            try {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = client.execute(post, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ParsedLoginDataSet parsedLoginDataSet = new ParsedLoginDataSet();
            //ParsedLoginDataSet parsedLoginDataSet = myLoginHandler.getParsedLoginData();
            try {

			/*if (response == null) {
				System.out.println("response is null " + response);
				Exception e = new Exception();
				throw e;
			}*/
/*
                sessionTokens = parseToken(response);
                mSignInDbHelper.createSession(mEmailField.getText().toString(),
                        sessionTokens.get("auth_token"));
                // now = Long.valueOf(System.currentTimeMillis());
                // mSignInDbHelper.createSession(mEmailField.getText().toString(),mAuthToken,now);
            } catch (Exception e) {
                e.printStackTrace();
            }
            parsedLoginDataSet.setExtractedString(sessionTokens.get("error"));
            if (parsedLoginDataSet.getExtractedString().equals("Success")) {
                // Store the username and password in SharedPreferences after the successful login
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.putString("UserName", email);
                editor.putString("PassWord", password);
                editor.commit();
                Message myMessage=new Message();
                myMessage.obj="SUCCESS";
                handler.sendMessage(myMessage);
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginError.class);
                intent.putExtra("LoginMessage", "Invalid Login");
                startActivity(intent);
                removeDialog(0);
            }

            return sessionTokens;

        }*/
    }
}

