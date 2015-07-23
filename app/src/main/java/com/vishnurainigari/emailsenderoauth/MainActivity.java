package com.vishnurainigari.emailsenderoauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import javax.security.auth.Subject;


public class MainActivity extends Activity {

    EditText email;
    EditText body;
    EditText subject;
    String emailid;
    String subjectid;
    private String mToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email =(EditText)findViewById(R.id.editText);
        subject =(EditText)findViewById(R.id.editText2);
        body =(EditText)findViewById(R.id.editText3);



    }

    //sendMail("subject", "body message", TEST_ACCOUNT_EMAIL, mToken, "v-virain@microsoft.com");
    public void onClickSend(View view){

        emailid=email.getText().toString();
        subjectid= subject.getText().toString();
        Log.e("Email id for edit text","Check : "+emailid);
        Log.e("Email id for edit text","Check : "+subjectid);


         new OAuth2Authenticator(this,email.getText().toString(),subject.getText().toString());
        //auth2Authenticator.sendMail(subject.getText().toString(),body.getText().toString(),"testmailProject86@gmail.com",mToken,email.getText().toString());

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
