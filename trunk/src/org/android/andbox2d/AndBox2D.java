package org.android.andbox2d;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class AndBox2D extends Activity {
	
    Box2DView mBox2DView;
    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mBox2DView = (Box2DView) findViewById (R.id.box2dview);
        mBox2DView.requestFocus ();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        switch (keyCode) {
        case KeyEvent.KEYCODE_DPAD_RIGHT:
        	mBox2DView.nextTest();
            break;
            
        case KeyEvent.KEYCODE_DPAD_LEFT:
        	mBox2DView.preTest();
            break;
            
        default:
            break;
            
        }
        
    	return super.onKeyDown(keyCode, event);
    	
    }
}