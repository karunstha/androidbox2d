
package org.android.andbox2d;

import java.util.ArrayList;

import org.android.andbox2d.tests.Pyramid;
import org.android.andbox2d.tests.VaryingRestitution;
import org.android.andbox2d.tests.VaryingFriction;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.DebugDraw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class Box2DView extends View {
	
    //public Bitmap mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.RGB_565);
	
    private static final String TAG = "AndBox2D";
    
	/** The list of registered examples */
	protected ArrayList<AbstractExample> tests = new ArrayList<AbstractExample>(0);
	/** Currently running example */
	protected AbstractExample currentTest = null;
	/** 
	 * Index of current example in tests array.
	 * Assumes that the array structure does not change,
	 * though it's safe to add things on to the end. 
	 */
	protected int currentTestIndex = 0;
	/** Is the options window open? */
	protected boolean handleOptions = false;
	
	// Little bit of input stuff
	// TODO ewjordan: refactor into an input handler class
	/** Is the shift key held? */
	public boolean shiftKey = false;
    
	/** Was the mouse down last frame? */
    boolean pmousePressed = false;
    
    /** Our options handler - displays GUI and sets TestSettings for currentTest. */
    //public TestbedOptions options;
 
    // Processing handles fps pinning, but we
    // report fps on our own just to be sure.
    
    /** FPS that we want to achieve */
    final static float targetFPS = 60.0f;
    /** Number of frames to average over when computing real FPS */
    final int fpsAverageCount = 100; 
    /** Array of timings */
    long[] nanos;
    /** When we started the nanotimer */
    long nanoStart; //
    
    /** Number of frames since we started this example. */
    long frameCount = 0;    
    
    /** Drawing handler to use. */
    public DebugDraw g;
        
	private void init(Context context) {
            //mActivity = (Physics) context;
    	//size(640,480,P3D);
    	//frameRate(targetFPS);
		
    	//g = new AndroidDebugDraw(this);
    	g = new AndroidDebugDraw();
    	//smooth();
    	for (int i=0; i<100; ++i) {
    		this.requestFocus();
    	}	

    	registerExample (new VaryingFriction(this));
    	registerExample(new Pyramid(this));
    	registerExample(new VaryingRestitution(this));
    	
    	/* Set up the timers for FPS reporting */
    	nanos = new long[fpsAverageCount];
    	long nanosPerFrameGuess = (long)(1000000000.0 / targetFPS);
    	nanos[fpsAverageCount-1] = System.nanoTime();
    	for (int i=fpsAverageCount-2; i>=0; --i) {
    		nanos[i] = nanos[i+1] - nanosPerFrameGuess;
    	}
    	nanoStart = System.nanoTime();
    	
    }
        
    public Box2DView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init (context);

    }

    public Box2DView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init (context);
    }

    public Box2DView(Context context) {
        super(context);
        init (context);
    }
        
    public void onDraw(Canvas canvas) {
           
        Paint myPaint = new Paint();
        myPaint.setColor(0xFFDDDDDD);
        //canvas.drawCircle(50, 50, 20, myPaint);
/*        
		//canvas.drawText("aaa", 50, 100, myPaint);
		Bitmap bmp = ((AndroidDebugDraw)g).mBitmap;
		g.drawString(100.0f, 150.0f, "aaabbbcccddddd", AbstractExample.white);
*/
        
 /*       
    	if (handleOptions) {
    		//options.handleOptions();
    	} else{
    		
    	}*/

		Bitmap bmp = ((AndroidDebugDraw)g).mBitmap;
		bmp.eraseColor(0);
		

		Vec2.creationCount = 0;

		if (currentTest == null) {
			currentTestIndex = 0;
			currentTest = tests.get(currentTestIndex);
			nanoStart = System.nanoTime();
			frameCount = 0;
		}
		
		if (currentTest.needsReset) {
			//System.out.println("Resetting "+currentTest.getName());
			TestSettings s = currentTest.settings; //copy settings
			currentTest.initialize();
			if (s != null) currentTest.settings = s;
			nanoStart = System.nanoTime();
			frameCount = 0;
		} 

		
		currentTest.m_textLine = AbstractExample.textLineHeight;
		g.drawString(5, currentTest.m_textLine, currentTest.getName(),AbstractExample.white);
		currentTest.m_textLine += 2*AbstractExample.textLineHeight;


		currentTest.step();

		// If the user wants to move the canvas, do it 
		//handleCanvasDrag();
		
		// ==== Vec2 creation and FPS reporting ==== 
		if (currentTest.settings.drawStats) {
			g.drawString(5, currentTest.m_textLine, "Vec2 creations/frame: "+Vec2.creationCount, AbstractExample.white);
			currentTest.m_textLine += AbstractExample.textLineHeight;
		}

		for (int i=0; i<fpsAverageCount-1; ++i) {
			nanos[i] = nanos[i+1];
		}
		nanos[fpsAverageCount-1] = System.nanoTime();
		float averagedFPS = (float) ( (fpsAverageCount-1) * 1000000000.0 / (nanos[fpsAverageCount-1]-nanos[0]));
		++frameCount;
		float totalFPS = (float) (frameCount * 1000000000 / (1.0*(System.nanoTime()-nanoStart)));        
		if (currentTest.settings.drawStats) {
			g.drawString(5, currentTest.m_textLine, "Average FPS ("+fpsAverageCount+" frames): "+averagedFPS, AbstractExample.white);
			currentTest.m_textLine += AbstractExample.textLineHeight;
			g.drawString(5, currentTest.m_textLine, "Average FPS (entire test): "+totalFPS, AbstractExample.white);
			currentTest.m_textLine += AbstractExample.textLineHeight;
		}
		
		canvas.drawBitmap(bmp, 0, 0, myPaint);
		this.invalidate();
		
    }  
    
    /** Register an AbstractExample to the current list of examples. */
    public void registerExample(AbstractExample test) {
    	tests.add(test);
    }
    
    public void preTest (){
		--currentTestIndex;
		if (currentTestIndex < 0) currentTestIndex = tests.size()-1;
		//System.out.println(currentTestIndex);
		currentTest = tests.get(currentTestIndex);
		currentTest.needsReset = true;
    }
    
    public void nextTest (){
		++currentTestIndex;
		if (currentTestIndex >= tests.size()) currentTestIndex = 0;
		//System.out.println(currentTestIndex);
		currentTest = tests.get(currentTestIndex);
		currentTest.needsReset = true;

    }

}