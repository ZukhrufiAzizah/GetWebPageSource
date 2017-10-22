package com.zukhrufi.getwebpagesourcecode;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = (Spinner) findViewById(R.id.spinnerProtokol);
        textUrl = (EditText) findViewById(R.id.inputLink);
        resultHTML = (TextView) findViewById(R.id.sumberHTML);

        listSpinner = ArrayAdapter.createFromResource(this, R.array.protokol, android.R.layout.simple_spinner_item);
        listSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(listSpinner);

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
                Log.e("Error" + Thread.currentThread().getStackTrace()[2], paramThrowable.getLocalizedMessage());
            }
        });

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    TextView resultHTML;
    Spinner sp;
    EditText textUrl;
    ArrayAdapter<CharSequence> listSpinner;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Really Exit")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void doSomething(View view) {
        String linkUrl, protokol, url;
        protokol = sp.getSelectedItem().toString();
        url = textUrl.getText().toString();

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        if (!url.isEmpty()) {
            if (url.contains(".")) {
                if (checkConnection()) {
                    resultHTML.setText("Loading....");

                    linkUrl = protokol + url;

                    Bundle bundle = new Bundle();
                    bundle.putString("urlLink", linkUrl);
                    getSupportLoaderManager().restartLoader(0, bundle, this);

                } else {
                    Toast.makeText(this, "check your internet connection", Toast.LENGTH_SHORT).show();
                    resultHTML.setText("No Internet Connection");

                }
            } else {
                resultHTML.setText("Invalid URL");

            }

        } else {
            resultHTML.setText("URL can\'t empty");
        }


    }

    public boolean checkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new GetWebPageSource(this, args.getString("urlLink"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        resultHTML.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
