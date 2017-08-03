package com.example.naru.testip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Validate_IP extends Activity {
    private AutoCompleteTextView txt_ip;
    private AutoCompleteTextView txt_bits;
    private Button btn_eval ;
    private String ip , bits ;
    private EIP _ip = new EIP() ;

    private TextView txt_ipbits , txt_nature , txt_mask , txt_idnet , txt_broadcast , txt_firsthost , txt_nrohost , lasthost , txt_typenet ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate__ip);
        txt_ipbits = (TextView) findViewById( R.id.txt_ipbits ) ;
        txt_nature = (TextView) findViewById( R.id.txt_nature ) ;
        txt_mask = (TextView) findViewById( R.id.txt_mask );
        txt_idnet = (TextView) findViewById( R.id.txt_idnet ) ;
        txt_broadcast = (TextView) findViewById( R.id.txt_broadcast )  ;
        txt_firsthost = (TextView) findViewById( R.id.txt_firsthost ) ;
        txt_nrohost = (TextView) findViewById( R.id.txt_nrohost ) ;
        lasthost = (TextView) findViewById( R.id.txt_lasthost ) ;
        txt_typenet = (TextView) findViewById( R.id.txt_typenet ) ;
        _start();
    }

    public void onClickbtn_eval(View view){
        try {
            txt_ip = (AutoCompleteTextView) findViewById( R.id.txt_ip ) ;
            ip = txt_ip.getText().toString();
            txt_bits = (AutoCompleteTextView) findViewById( R.id.txt_bits ) ;
            bits = txt_bits.getText().toString() ;
            if ( _ip.evalip( ip , "0" + bits ) ) {
                txt_nature.setText( ": " + _ip.getNature() );
                txt_ipbits.setText( _ip.getIpBits());
                txt_bits.setText( _ip.getBits() ) ;
                txt_mask.setText( ": " + _ip.getMask() );
                txt_idnet.setText( ": " + _ip.getIdNet() );
                txt_broadcast.setText( ": " + _ip.getBroadcast() );
                txt_firsthost.setText( ": " + _ip.getFirstHost() );
                txt_nrohost.setText( ": " + _ip.getNroHost() );
                lasthost.setText( ": " + _ip.getLastHost() );
                txt_typenet.setText( ": " + _ip.getTypeNet() );
            } else {
                _start();
                Toast.makeText(getApplicationContext(), "IP Incorrect", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e) {
            _start();
        }
    }

    public void _start() {
        txt_ipbits.setText( "a.b.c.d / e" ) ;
        txt_nature.setText( ": " );
        txt_mask.setText( ": " ) ;
        txt_idnet.setText( ": " ) ;
        txt_broadcast.setText( ": " ) ;
        txt_firsthost.setText( ": " ) ;
        txt_nrohost.setText( ": "  ) ;
        lasthost.setText( ": "  ) ;
        txt_typenet.setText( ": " ) ;
    }
}

