package com.vishnurainigari.emailsenderoauth;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.BASE64EncoderStream;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by vishnurainigari on 7/22/15.
 */
public class OAuth2Authenticator {

    private static Session mSession;
    public final String emailId;
    public final String subjectid;

    private String mToken;
    private static String TEST_ACCOUNT_EMAIL = "testmailproject86@gmail.com";

    public OAuth2Authenticator(Activity activity,String emailID,String Subject) {
        this.emailId= emailID;
        this.subjectid = Subject;
        AccountManager mAccountManager = AccountManager.get(activity);

        Account[] accounts = mAccountManager.getAccounts();

        // For this tutorial, we manually set the email account(TEST_ACCOUNT_EMAIL). However, you should create some kind of UI for users to select the Gmail account
        Account mAccount = null;
        for (Account a : accounts) {
            if (a.name.equals(TEST_ACCOUNT_EMAIL) && a.type.equals("com.google")) {
                mAccount = a;
            }
        }
        if (mAccount != null) {
            AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(mAccount, "oauth2:https://mail.google.com/", null, activity, new OnTokenAcquired(), null);
        }

    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();
                mToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                new SendMailTask().execute();
            } catch (Exception e) {
                Log.e("OnTokenAcquired", e.getMessage());
            }
        }
    }

    private class SendMailTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                sendMail(subjectid,"body message", TEST_ACCOUNT_EMAIL, mToken, emailId);
            } catch (Exception e) {
                Log.e("SendMailTask", "error:" + e.getMessage());
            }
            return null;
        }
    }

    private SMTPTransport connectToSmtp(String host, int port, String userEmail,
                                        String oauthToken, boolean debug) throws Exception {

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.sasl.enable", "false");
        mSession = Session.getInstance(props);
        mSession.setDebug(debug);
        final URLName unusedUrlName = null;
        SMTPTransport transport = new SMTPTransport(mSession, unusedUrlName);
        // If the password is non-null, SMTP tries to do AUTH LOGIN.
        final String emptyPassword = null;
        transport.connect(host, port, userEmail, emptyPassword);
        byte[] response = String.format("user=%s\1auth=Bearer %s\1\1", userEmail, oauthToken).getBytes();
        response = BASE64EncoderStream.encode(response);
        transport.issueCommand("AUTH XOAUTH2 " + new String(response), 235);
        return transport;
    }

    public synchronized void sendMail(String subject, String body, String user,
                                      String oauthToken, String recipients) {
        try {

            SMTPTransport smtpTransport = connectToSmtp("smtp.gmail.com",
                    587,
                    user,
                    oauthToken,
                    true);
            MimeMessage message = new MimeMessage(mSession);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(user));
            message.setSubject(subject);
            message.setDataHandler(handler);
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            smtpTransport.sendMessage(message, message.getAllRecipients());
        } catch (Exception e) {
            Log.e("sendMail", e.getMessage());
        }
    }
}
