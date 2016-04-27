---------
How to Use
---------

Run Controller and use the buttons to:
	1. Start / Stop
	2. Reset
	3. Change color
	4. Slider to change speed
	5. Change reflections

---------
How It Works
---------

1. All 6 initial objects start on top left quadrant with randomized location, color and size. But all 6 objects have different colors.

2. I decided to do symmetry about the center of the frame. See "Design" part 4.

3. Change color is just a toggle for object colors.

4. Speed slider sets speed as % of max speed I hard coded.

5. Inital reflection set at 16. Use the button to toggle to 24 or 8.

---------
Design
---------

1. I purposedly packed all reflections of each object into model. This way it handles all computations. There is a helper function in model to get the parameter necessary for View to draw every reflection. 

2. The final program contains one View, one Controller, and multiple Models

3. Randomization is mostly done in Controller, but also in Model. Parts are done in Controller to make sure object has different colors.

4. I changed setLimits to make sure the object doesn't get "stuck" when resizing puts it into negative area

5. Symmetry about the center point means we only need to do some simple sin/cos/tan. I personally find this symmetry the most elegant and efficient.



