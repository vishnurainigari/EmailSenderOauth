package com.vishnurainigari.emailsenderoauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Emailsend extends Activity {

    EditText mainEmail;
    EditText mainSubject;
    EditText mainBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailsend);

        mainEmail = (EditText)findViewById(R.id.edittextemail);
        mainSubject= (EditText)findViewById(R.id.edittextsubject);
        mainBody= (EditText) findViewById(R.id.edittextbody);


    }

    public void onClickMain(View view){

        Intent i = new Intent("EmailDetails");
        i.putExtra("mail",mainEmail.getText().toString());
        i.putExtra("subject",mainSubject.getText().toString());
        i.putExtra("body",mainBody.getText().toString());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_emailsend, menu);
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
