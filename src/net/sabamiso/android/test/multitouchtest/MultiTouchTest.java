package net.sabamiso.android.test.multitouchtest;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MultiTouchTest extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // fullscreen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
       
        // Screen Orientation
        setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);  
        
        MultiTouchView canvas = new MultiTouchView(this);
        setContentView(canvas);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	Toast.makeText(this, "MultiTouchTest finished...", Toast.LENGTH_SHORT).show();
    	this.finish();
    }
}