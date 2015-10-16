package fr.wenfeng.maplocation;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.textservice.SpellCheckerInfo;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    // These variables are shorthand aliases for data items in Contacts-related database tables
    private static final String DATA_MIMETYPE = ContactsContract.Data.MIMETYPE;
    private static final Uri DATA_CONTAENT_URI = ContactsContract.Data.CONTENT_URI;
    private static final String DATA_CONTACT_ID = ContactsContract.Data.CONTACT_ID;

    private static final String CONTACTS_ID = ContactsContract.Contacts._ID;
    private static final Uri CONTACTS_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

    private static final String STRUCTURE_POSTAL_CONTENT_ITEM_TYPE
            = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE;
    private static final String STRUCTURE_POSTAL_FORMATTED_ADDRESS
            = ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS;
    private static final int PICK_CONTACT_REQUEST = 0;

    private final String TAG = "MainActivity";

    private EditText editTextInput;
    private Button btnOpenMap;
    private Button btnChooseContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // get editText and button widgets
        editTextInput = (EditText) findViewById(R.id.editTextInputLocation);
        btnOpenMap = (Button) findViewById(R.id.btnOpenMap);
        btnChooseContact = (Button) findViewById(R.id.btnChooseContact);
    }

    /**
     * Callback method when user clicks the openMapButton.
     * Action is to open the Map and show user the location he/she inputed.
     * @param view button
     */
    public void btnOpenMapHandler(View view) {
        // get input location
        String location = editTextInput.getText().toString();
        Log.d(TAG, "The input locaton is: " + location);

        // create Intent object for starting Google Maps application
        Intent geoItent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + location));
        startActivity(geoItent);
    }

    /**
     * Callback method when user clicks the ChooseFromContact button.
     * Action is to choose location from contact and then show in maps.
     * @param view
     */
    public void btnChooseContactHandler(View view) {
        Log.d(TAG, "btnChooseContactHandler");

        try {
            // create Intent object for picking data from Contacts database
            Intent intent = new Intent(Intent.ACTION_PICK, CONTACTS_CONTENT_URI);

            // use intent to start Contacts application
            // variable PICK_CONTACT_REQUEST identifies this operation
            startActivityForResult(intent, PICK_CONTACT_REQUEST);
        } catch (Exception e) {
            // log any error messages to LogCat
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // ensure that this call is the result of a successful PICK_CONTACT_REQUEST request
        if (requestCode == PICK_CONTACT_REQUEST && resultCode == Activity.RESULT_OK) {

            Log.d(TAG, "request has been sent");

            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(data.getData(), null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                String id = cursor.getString(cursor.getColumnIndex(CONTACTS_ID));
                String where = DATA_CONTACT_ID + " = ? AND " + DATA_MIMETYPE + " = ?";

                String[] whereParameters = new String[] {id, STRUCTURE_POSTAL_CONTENT_ITEM_TYPE};

                Log.d(TAG, "cursor is good");

                // TODO: This line of code ensults problem when picking the contact, need to resolve later
                Cursor addrCur = contentResolver
                        .query(DATA_CONTAENT_URI, null, where, whereParameters, null);

                Log.d(TAG, "cursor is good???");

                if (addrCur != null && addrCur.moveToFirst()) {

                    Log.d(TAG, "addrCur is good");

                    String formattedAddress = addrCur
                            .getString(addrCur.getColumnIndex(STRUCTURE_POSTAL_FORMATTED_ADDRESS));

                    if (formattedAddress != null) {

                        Log.d(TAG, "formattedAddress is good");

                        // process text for network transmission
                        formattedAddress = formattedAddress.replace(' ', '+');

                        // create Intent object for starting Google Maps application
                        Intent geoIntent = new Intent(
                                Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + formattedAddress));

                        // use the Intent to start Google Maps application
                        startActivity(geoIntent);
                    }

                }
            }
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
