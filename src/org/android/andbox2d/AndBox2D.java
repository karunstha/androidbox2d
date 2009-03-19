package org.android.andbox2d;

import org.jbox2d.dynamics.DebugDraw;

import android.app.Activity;
import android.os.Bundle;

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
}