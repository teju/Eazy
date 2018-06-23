package eazi.com.eazi;


import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.*;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

public class RadialMenuWidget extends View {

	private  Context context;

	//Defines the interface
	public interface RadialMenuEntry {
		public String getName();
		public String getLabel();
		public int getIcon();
		public List<RadialMenuEntry> getChildren();
		public void menuActiviated();
	}


	private List<RadialMenuEntry> menuEntries = new ArrayList<RadialMenuEntry>();
	private RadialMenuEntry centerCircle = null;

	private float screen_density = getContext().getResources().getDisplayMetrics().density;

	private int defaultColor = Color.rgb(255, 255, 255); 	//default color of wedge pieces
	private int defaultAlpha = 180;  						//transparency of the colors, 255=Opague, 0=Transparent
	private int wedge2Color = Color.rgb(50, 50, 50); 	//default color of wedge pieces
	private int wedge2Alpha = 210;
	private int outlineColor = Color.rgb(211, 211, 211);  	//color of outline
	private int outlineColorInner = Color.rgb(133, 180, 248);  	//color of outline
	private int outlineAlpha = 255;							//transparency of outline
	private int selectedColor = Color.rgb(70, 130, 180);  	//color to fill when something is selected
	private int selectedAlpha = 210;						//transparency of fill when something is selected

	private int disabledColor = Color.rgb(34, 96, 120);  	//color to fill when something is selected
	private int disabledAlpha = 100;						//transparency of fill when something is selected

	private int pictureAlpha = 255;							//transparency of images

	private int textColor = R.color.orange;  	//color to fill when something is selected
	private int textAlpha = 255;						//transparency of fill when something is selected

	private int headerTextColor = Color.rgb(255, 255, 255);  	//color of header text
	private int headerTextAlpha = 255;							//transparency of header text
	private int headerBackgroundColor =  Color.rgb(0, 0, 0);	//color of header background
	private int headerBackgroundAlpha =  180;					//transparency of header background

	private int wedgeQty = 1;				//Number of wedges
	private int wedgeborderQty = 1;				//Number of wedges
	private Wedge[] Wedges = new Wedge[wedgeQty];
	private WedgeBorder[] Wedgesborders = new WedgeBorder[wedgeQty];
	private Wedge selected = null;			//Keeps track of which wedge is selected
	private int selectedWedge = 0;			//Keeps track of which wedge is selected
	private Wedge enabled = null;			//Keeps track of which wedge is enabled for outer ring
	private Rect[] iconRect = new Rect[wedgeQty];


	private int wedgeQty2 = 1;				//Number of wedges
	private Wedge[] Wedges2 = new Wedge[wedgeQty2];
	private Wedge selected2 = null;			//Keeps track of which wedge is selected
	private Rect[] iconRect2 = new Rect[wedgeQty2];
	private RadialMenuEntry wedge2Data = null;		//Keeps track off which menuItem data is being used for the outer ring

	private int MinSize = scalePX(35);				//Radius of inner ring size
	private int MaxSize = scalePX(90);				//Radius of outer ring size
	private int r2MinSize = MaxSize+scalePX(5);		//Radius of inner second ring size
	private int r2MaxSize = r2MinSize+scalePX(45);	//Radius of outer second ring size
	private int MinIconSize = scalePX(15);					//Min Size of Image in Wedge
	private int MaxIconSize = scalePX(35);			//Max Size of Image in Wedge
	//private int BitmapSize = scalePX(40);			//Size of Image in Wedge
	private int cRadius = MinSize; 	 	//Inner Circle Radius
	private int textSize = scalePX(55);				//TextSize
	private int animateTextSize = textSize;

	private int xPosition = scalePX(120);			//Center X location of Radial Menu
	private int yPosition = scalePX(120);			//Center Y location of Radial Menu

	private int xSource = 0;			//Source X of clicked location
	private int ySource = 0;			//Center Y of clicked location
	private boolean showSource = false;	//Display icon where at source location

	private boolean inWedge = false;		//Identifies touch event was in first wedge
	private boolean inWedgeBorder = false;		//Identifies touch event was in first wedge
	private boolean inWedge2 = false;		//Identifies touch event was in second wedge
	private boolean inCircle = false;		//Identifies touch event was in middle circle

	private boolean Wedge2Shown = false;		//Identifies 2nd wedge is drawn
	private boolean HeaderBoxBounded = false;	//Identifies if header box is drawn

	private String headerString = null;
	private int headerTextSize = textSize;				//TextSize
	private int headerBuffer = scalePX(8);
	private Rect textRect = new Rect();
	private RectF textBoxRect = new RectF();
	private int headerTextLeft;
	private int headerTextBottom;


	String center_label = "Connect";


	public RadialMenuWidget(Context context) {
		super(context);
		try {
			this.context =context;
			// Gets screen specs and defaults to center of screen
			this.xPosition = (getResources().getDisplayMetrics().widthPixels) / 2;
			this.yPosition = (getResources().getDisplayMetrics().heightPixels) / 2;

			determineWedges();

		} catch (Exception e){
			System.out.println("EXCEPTIONIS122 "+e.toString());

		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent e) {
		int state = e.getAction();
		int eventX = (int) e.getX();
		int eventY = (int) e.getY();
		if (state == MotionEvent.ACTION_DOWN) {
			//selected = null;
			//selected2 = null;
			inWedge = false;
			inWedge2 = false;
			inCircle = false;


			//Checks if a pie slice is selected in first Wedge
			for (int i = 0; i < Wedges.length; i++) {
				Wedge f = Wedges[i];
				double slice = (2*Math.PI) / wedgeQty;
				double start = (2*Math.PI)*(0.75) - (slice/2);		//this is done so top slice is the centered on top of the circle

				inWedge = pntInWedge(eventX, eventY,
						xPosition, yPosition,
						MinSize, MaxSize,
						(i* slice)+start, slice);

				if (inWedge == true) {
					selected = f;
					break;
				}
			}
			for (int i = 0; i < Wedgesborders.length; i++) {
				WedgeBorder f = Wedgesborders[i];
				double slice = (2*Math.PI) / wedgeborderQty;
				double start = (2*Math.PI)*(0.75) - (slice/2);		//this is done so top slice is the centered on top of the circle

				inWedgeBorder = pntInWedge(eventX, eventY,
						xPosition, yPosition,
						320, 320,
						(i* slice)+start, slice);
				if (inWedgeBorder == true) {
					selectedWedge = i;
					break;
				}
			}
		} else if (state == MotionEvent.ACTION_UP) {
			//execute commands...
			//put in stuff here to "return" the button that was pressed.
			if (selected != null){
				for (int i = 0; i < Wedges.length; i++) {
					Wedge f = Wedges[i];
					if (f == selected) {

						//Checks if a inner ring is enabled if so closes the outer ring an
						if (enabled != null) {

						} else {
							menuEntries.get(i).menuActiviated();
							if(menuEntries.get(i).getName().equals("Connect")) {
								Intent intent  = new Intent(context,Invite.class);
								context.startActivity(intent);
								selectedWedge = 0;
								center_label = "Connect";
							} else if(menuEntries.get(i).getName().equals("Settings")) {
								selectedWedge = i;
								center_label = "Plus";

							} else if(menuEntries.get(i).getName().equals("Plus")) {
								selectedWedge = i;
								center_label = "Search";

							} else if(menuEntries.get(i).getName().equals("Search")) {
								selectedWedge = i;
								center_label = "Settings";
							}
							//Figures out how many outer rings
							if (menuEntries.get(i).getChildren() != null) {
								enabled = f;

							} else {
								Wedge2Shown = false;
							}

						}
						selected = null;
					}
				}
			}
			//selected = null;
			selected2 = null;
			inCircle = false;
		}
		invalidate();
		return true;
	}

	private boolean pntInWedge(double px, double py,
							   float xRadiusCenter, float yRadiusCenter,
							   int innerRadius, int outerRadius,
							   double startAngle, double sweepAngle) {
		double diffX = px-xRadiusCenter;
		double diffY = py-yRadiusCenter;

		double angle = Math.atan2(diffY,diffX);
		if (angle < 0)
			angle += (2*Math.PI);

		if (startAngle >= (2*Math.PI)) {
			startAngle = startAngle-(2*Math.PI);
		}

		//checks if point falls between the start and end of the wedge
		if ((angle >= startAngle && angle <= startAngle + sweepAngle) ||
				(angle+(2*Math.PI) >= startAngle && (angle+(2*Math.PI)) <= startAngle + sweepAngle)) {

			// checks if point falls inside the radius of the wedge
			double dist = diffX*diffX + diffY*diffY;
			if (dist < outerRadius*outerRadius && dist > innerRadius*innerRadius) {
				return true;
			}
		}
		return false;
	}


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onDraw(Canvas c) {

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);


		for (int i = 0; i < Wedges.length; i++) {
			Wedge f = Wedges[i];
			paint.setColor(outlineColor);
			paint.setAlpha(outlineAlpha);
			paint.setStyle(Paint.Style.STROKE);
			c.drawPath(f, paint);
			if (f == selected) {
				paint.setColor(defaultColor);
				paint.setAlpha(defaultAlpha);
				paint.setStyle(Paint.Style.FILL);
				c.drawPath(f, paint);
			} else {
				paint.setColor(defaultColor);
				paint.setAlpha(defaultAlpha);
				paint.setStyle(Paint.Style.FILL);
				c.drawPath(f, paint);
			}

			if(iconRect[i] != null) {
				Rect rf = iconRect[i];

				if (menuEntries.get(i).getIcon() != 0) {
					//Puts in the Icon
					if (i != selectedWedge) {
						System.out.println("ONDERADCSDCSdc "+selectedWedge);

						Drawable drawable = getResources().getDrawable(menuEntries.get(i).getIcon());
						drawable.setBounds(rf);
						//drawable.setTint(getResources().getColor(R.color.gray_txt_clor3));
						drawable.setColorFilter(this.getResources().getColor(R.color.gray_txt_clor3), PorterDuff.Mode.SRC_ATOP);

						if (f != enabled && Wedge2Shown == true) {
							drawable.setAlpha(disabledAlpha);
						} else {
							drawable.setAlpha(pictureAlpha);
						}
						drawable.draw(c);
					}  else {
						Drawable drawable = getResources().getDrawable(menuEntries.get(i).getIcon());
						drawable.setBounds(rf);
						drawable.clearColorFilter();

						if (f != enabled && Wedge2Shown == true) {
							drawable.setAlpha(disabledAlpha);
						} else {
							drawable.setAlpha(pictureAlpha);
						}
						drawable.draw(c);
					}

				}
			}
		}
		for (int i = 0; i < Wedgesborders.length; i++) {

			if(i == selectedWedge) {
				WedgeBorder f = Wedgesborders[i];
				paint.setColor(getResources().getColor(R.color.orange));
				paint.setAlpha(outlineAlpha);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(30);
				c.drawPath(f, paint);
			} else {
				WedgeBorder f = Wedgesborders[i];
				paint.setColor(getResources().getColor(R.color.blue));
				paint.setAlpha(outlineAlpha);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(30);
				c.drawPath(f, paint);
			}
//			if (f == selectedWedge) {
//				System.out.println("selectedWedge1234 selectedWedge");
//				paint.setColor(getResources().getColor(R.color.pink));
//				c.drawPath(f, paint);
//			} else {
//				System.out.println("selectedWedge1234 not selectedWedge");
//				paint.setColor(getResources().getColor(R.color.black));
//				c.drawPath(f, paint);
//			}
		}

//		//Draws the Middle Circle
//		paint.setColor(getResources().getColor(R.color.blue));
//    	//paint.setAlpha(outlineAlpha);
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setStrokeWidth(30);
//
//		c.drawCircle(xPosition, yPosition, cRadius, paint);
//		if (inCircle == true) {
//		    paint.setColor(selectedColor);
//	    	paint.setAlpha(selectedAlpha);
//	    	paint.setStyle(Paint.Style.FILL);
//		    c.drawCircle(xPosition, yPosition, cRadius, paint);
//		} else {
//	    	//paint.setColor(defaultColor);
//	    	//paint.setAlpha(defaultAlpha);
//	    	//paint.setStyle(Paint.Style.FILL);
//		    c.drawCircle(xPosition, yPosition, cRadius, paint);
//		}

		if (centerCircle.getIcon() != 0) {

			Rect rect = new Rect();

			Drawable drawable = getResources().getDrawable(centerCircle.getIcon());

			int h = getIconSize(drawable.getIntrinsicHeight(),MinIconSize,MaxIconSize);
			int w = getIconSize(drawable.getIntrinsicWidth(),MinIconSize,MaxIconSize);
			rect.set(xPosition-w/2, yPosition-h/2, xPosition+w/2, yPosition+h/2);

			drawable.setBounds(rect);
			drawable.setAlpha(pictureAlpha);
			drawable.draw(c);

		} else {

				final float scale = getResources().getDisplayMetrics().density;

				int textSizeactual = (int) (textSize * scale + 0.5f);
				//Puts in the Text if no Icon
				paint.setColor(getResources().getColor(textColor));
				paint.setStyle(Paint.Style.FILL);
				paint.setTextSize(textSizeactual);

				//This will look for a "new line" and split into multiple lines
				String menuItemName = center_label;
				String[] stringArray = menuItemName.split("\n");

				//gets total height
				Rect rect = new Rect();
				float textHeight = 20;
				for (int j = 0; j < stringArray.length; j++)
				{
					paint.getTextBounds(stringArray[j],0,stringArray[j].length(),rect);
					textHeight = textHeight+(rect.height()+3);
				}

				float textBottom = yPosition-(textHeight/2);
				for (int j = 0; j < stringArray.length; j++)
				{
					paint.getTextBounds(stringArray[j],0,stringArray[j].length(),rect);
					float textLeft = xPosition - rect.width()/2;
					textBottom = textBottom + (rect.height()+3);
					System.out.println("stringArray1234 "+rect.width()+"textLeft "+textLeft+" textBottom "+textBottom);

					c.drawText(stringArray[j], textLeft , textBottom, paint);
				}
			}

	}


	private int getIconSize(int iconSize, int minSize, int maxSize) {

		if (iconSize > minSize) {
			if (iconSize > maxSize) {
				return maxSize;
			} else {	//iconSize < maxSize
				return iconSize;
			}
		} else {  //iconSize < minSize
			return minSize;
		}

	}






	public boolean addMenuEntry( RadialMenuEntry entry )
	{
		menuEntries.add( entry );
		determineWedges();
		determineWedgesBorder();

		return true;
	}

	public boolean setCenterCircle( RadialMenuEntry entry )
	{
		centerCircle = entry;
		return true;
	}


	public void setInnerRingRadius( int InnerRadius, int OuterRadius )
	{
		this.MinSize = scalePX(InnerRadius);
		this.MaxSize = scalePX(OuterRadius);
		determineWedges();
		determineWedgesBorder();

	}

	public void setOuterRingRadius( int InnerRadius, int OuterRadius )
	{
		this.r2MinSize = scalePX(InnerRadius);
		this.r2MaxSize = scalePX(OuterRadius);
		determineWedges();
		determineWedgesBorder();

	}

	public void setCenterCircleRadius( int centerRadius )
	{
		this.cRadius = scalePX(centerRadius);
		determineWedges();
		determineWedgesBorder();

	}

	public void setTextSize( int TextSize )
	{
		this.textSize = scalePX(TextSize);
		this.animateTextSize = this.textSize;
	}

	public void setIconSize( int minIconSize, int maxIconSize )
	{
		this.MinIconSize = scalePX(minIconSize);
		this.MaxIconSize = scalePX(maxIconSize);
		determineWedges();
	}



	public void setSourceLocation( int x, int y )
	{
		this.xSource = x;
		this.ySource = y;
	}

	public void setShowSourceLocation( boolean showSourceLocation )
	{
		this.showSource = showSourceLocation;
	}


	public void setInnerRingColor( int color, int alpha )
	{
		this.defaultColor = color;
		this.defaultAlpha = alpha;
	}
	public void setOuterRingColor( int color, int alpha )
	{
		this.wedge2Color = color;
		this.wedge2Alpha = alpha;
	}
	public void setOutlineColor( int color, int alpha )
	{
		this.outlineColor = color;
		this.outlineAlpha = alpha;
	}
	public void setSelectedColor( int color, int alpha )
	{
		this.selectedColor = color;
		this.selectedAlpha = alpha;
	}

	public void setDisabledColor( int color, int alpha )
	{
		this.disabledColor = color;
		this.disabledAlpha = alpha;
	}

	public void setTextColor( int color, int alpha )
	{
		this.textColor = color;
		this.textAlpha = alpha;
	}

	public void setHeader( String header, int TextSize )
	{
		this.headerString = header;
		this.headerTextSize = scalePX(TextSize);
		HeaderBoxBounded = false;
	}
	public void setHeaderColors( int TextColor, int TextAlpha, int BgColor, int BgAlpha )
	{
		this.headerTextColor = TextColor;
		this.headerTextAlpha = TextAlpha;
		this.headerBackgroundColor =  BgColor;
		this.headerBackgroundAlpha =  BgAlpha;

	}


	private int scalePX( int dp_size )
	{
		int px_size = (int) (dp_size * screen_density + 0.5f);
		return px_size;
	}



	private void determineWedges() {

		int entriesQty = menuEntries.size();
		if ( entriesQty > 0) {
			wedgeQty = entriesQty;

			float degSlice = 360 / wedgeQty;
			float start_degSlice = 270 - (degSlice/2);
			//calculates where to put the images
			double rSlice = (2*Math.PI) / wedgeQty;
			double rStart = (2*Math.PI)*(0.75) - (rSlice/2);

			this.Wedges = new Wedge[wedgeQty];
			this.iconRect = new Rect[wedgeQty];

			for (int i = 0; i < Wedges.length; i++) {
				this.Wedges[i] = new Wedge(xPosition, yPosition, MinSize, MaxSize, (i
						* degSlice)+start_degSlice, degSlice);
				float xCenter = (float)(Math.cos(((rSlice*i)+(rSlice*0.5))+rStart) * (MaxSize+MinSize)/2)+xPosition;
				float yCenter = (float)(Math.sin(((rSlice*i)+(rSlice*0.5))+rStart) * (MaxSize+MinSize)/2)+yPosition;

				int h = MaxIconSize;
				int w = MaxIconSize;
				if ( menuEntries.get(i).getIcon() != 0 ) {
					Drawable drawable = getResources().getDrawable(menuEntries.get(i).getIcon());
					h = getIconSize(drawable.getIntrinsicHeight(),MinIconSize,MaxIconSize);
					w = getIconSize(drawable.getIntrinsicWidth(),MinIconSize,MaxIconSize);
				}

				this.iconRect[i] = new Rect( (int) xCenter-w/2, (int) yCenter-h/2, (int) xCenter+w/2, (int) yCenter+h/2);
			}

			invalidate();  //re-draws the picture
		}
	}

	private void determineWedgesBorder() {

		int entriesQty = menuEntries.size();
		if ( entriesQty > 0) {
			wedgeborderQty = entriesQty;

			float degSlice = 360 / wedgeQty;
			float start_degSlice = 270 - (degSlice/2);
			//calculates where to put the images
			double rSlice = (2*Math.PI) / wedgeQty;
			double rStart = (2*Math.PI)*(0.75) - (rSlice/2);

			this.Wedgesborders = new WedgeBorder[wedgeborderQty];

			for (int i = 0; i < Wedges.length; i++) {
				this.Wedgesborders[i] = new WedgeBorder(xPosition, yPosition, 320, 320, (i
						* degSlice)+start_degSlice, degSlice);


			}

			invalidate();  //re-draws the picture
		}
	}


	public class Wedge extends Path {
		private int x, y;
		private int InnerSize, OuterSize;
		private float StartArc;
		private float ArcWidth;

		private Wedge(int x, int y, int InnerSize, int OuterSize, float StartArc, float ArcWidth) {
			super();

			if (StartArc >= 360) {
				StartArc = StartArc-360;
			}

			this.x = x; this.y = y;
			this.InnerSize = InnerSize;
			this.OuterSize = OuterSize;
			this.StartArc = StartArc;
			this.ArcWidth = ArcWidth;
			this.buildPath();
		}

		private void buildPath() {

			final RectF rect = new RectF();
			final RectF rect2 = new RectF();

			//Rectangles values
			rect.set(this.x-this.InnerSize, this.y-this.InnerSize, this.x+this.InnerSize, this.y+this.InnerSize);
			rect2.set(this.x-this.OuterSize, this.y-this.OuterSize, this.x+this.OuterSize, this.y+this.OuterSize);

			this.reset();
			//this.moveTo(100, 100);
			this.arcTo(rect2, StartArc, ArcWidth);
			this.arcTo(rect, StartArc+ArcWidth, -ArcWidth);

			this.close();


		}
	}
	public class WedgeBorder extends Path {
		private int x, y;
		private int InnerSize, OuterSize;
		private float StartArc;
		private float ArcWidth;

		private WedgeBorder(int x, int y, int InnerSize, int OuterSize, float StartArc, float ArcWidth) {
			super();

			if (StartArc >= 360) {
				StartArc = StartArc-360;
			}

			this.x = x; this.y = y;
			this.InnerSize = InnerSize;
			this.OuterSize = OuterSize;
			this.StartArc = StartArc;
			this.ArcWidth = ArcWidth;
			this.buildPath();
		}

		private void buildPath() {

			final RectF rect = new RectF();
			final RectF rect2 = new RectF();

			//Rectangles values
			rect.set(this.x-this.InnerSize, this.y-this.InnerSize, this.x+this.InnerSize, this.y+this.InnerSize);
			rect2.set(this.x-this.OuterSize, this.y-this.OuterSize, this.x+this.OuterSize, this.y+this.OuterSize);

			this.reset();
			//this.moveTo(100, 100);
			this.arcTo(rect2, StartArc, ArcWidth);
			this.arcTo(rect, StartArc+ArcWidth, -ArcWidth);

			this.close();


		}
	}

}