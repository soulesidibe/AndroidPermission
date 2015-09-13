package com.soulesidibe.permission;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CONTACTS = 1002;

    private Cursor mProfileCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button_get_contacts);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToGetProfile();
            }
        });
    }

    private void tryToGetProfile() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Permission to read contact is required", Toast.LENGTH_SHORT)
                        .show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_REQUEST_CONTACTS);
        } else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            getProfile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
            int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Toast.makeText(this, "Contact permission was granted. Starting preview.",
                        Toast.LENGTH_SHORT)
                        .show();
                getProfile();
            } else {
                // Permission request was denied.
                Toast.makeText(this, "Permission request was denied.", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void getProfile() {

        // Sets the columns to retrieve for the user profile
        String[] mProjection = new String[]{
                ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
        };

        // Retrieves the profile from the Contacts Provider
        mProfileCursor = getContentResolver().query(
                ContactsContract.Profile.CONTENT_URI,
                mProjection,
                null,
                null,
                null);

        if (mProfileCursor == null) {
            return;
        }
        mProfileCursor.moveToFirst();
        while (!mProfileCursor.isAfterLast()) {
            Log.d("Permission Test", "Display Name >> " + mProfileCursor.getString(0));
            mProfileCursor.moveToNext();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
