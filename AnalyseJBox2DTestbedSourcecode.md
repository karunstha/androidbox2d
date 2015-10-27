# Introduction #

JBox2d uses the open source Processing library in the testbed. To port JBox2D testbed, we should analyse JBox2D testbed source code.

# Structure of a simple process application #

A simple Process app:

```

// The statements in the setup() function 
// execute once when the program begins
void setup() 
{
  size(200, 200);  // Size should be the first statement
  stroke(255);     // Set line drawing color to white
  frameRate(30);
}

float y = 100;

// The statements in draw() are executed until the 
// program is stopped. Each statement is executed in 
// sequence and after the last line is read, the first 
// line is executed again.
void draw() 
{ 
  background(0);   // Set the background to black
  y = y - 1; 
  if (y < 0) { y = height; } 
  line(0, y, width, y);  
} 

```

http://dev.processing.org/reference/core/javadoc/processing/core/PApplet.html

# JBox2D Testbed class diagram #

![http://www.linuxgraphics.cn/images/physics/jbox2d_testbed_class_diagram.png](http://www.linuxgraphics.cn/images/physics/jbox2d_testbed_class_diagram.png)

  * DebugDraw is an abstract class which define some drawing methods and coordinate maping functions such as worldToScreen(), ScreenToWorld(), drawCircle(),etc.
  * ProcessingDebugDraw extends from DebugDraw. It use processing library to implement the methods of DebugDraw.
  * AbstrcatExample is an abstract class which define the common vars and methods of all test.
  * Extending from AbstractExample, pyramid and other classes implement many examples such as BipedTest, Bridge, VaryingRestitution, VaryingFriction and so on.
  * TestbedMain is the application class which implement a processing app.