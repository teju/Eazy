package eazi.com.eazi;

import java.util.List;

import eazi.com.eazi.RadialMenuWidget.RadialMenuEntry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Toast;

public class RadialMenuActivity extends Activity {

    private RadialMenuWidget PieMenu;
    private LinearLayout ll;
    //private TextView tv;
    public  int [] drawable  = {R.drawable.connect,R.drawable.settings,R.drawable.plus,R.drawable.search};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //------------------------------------------------------------------------------------------
        // Generating Pie view
        //------------------------------------------------------------------------------------------
        setContentView(R.layout.activity_main_dial);
        try {


            ll = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
            addContentView(ll, params);


            PieMenu = new RadialMenuWidget(getBaseContext());

           // PieMenu.setSourceLocation(xSource, ySource);
            PieMenu.setShowSourceLocation(true);
            PieMenu.setSourceLocation(100,100);
            //PieMenu.setCenterLocation(240,400);
            PieMenu.setInnerRingRadius(110, 160);
            PieMenu.setSelectedColor(R.color.orange,255);
            PieMenu.setCenterCircleRadius(105);
            PieMenu.setOuterRingColor(R.color.blue,255);
            //PieMenu.setHeader("Menu Header", 20);

            int xLayoutSize = ll.getWidth();
            int yLayoutSize = ll.getHeight();
            PieMenu.setSourceLocation(xLayoutSize, yLayoutSize);
            PieMenu.setIconSize(15, 30);
            PieMenu.setTextSize(13);

            PieMenu.setCenterCircle(new Close());
            PieMenu.addMenuEntry(new Connect());
            PieMenu.addMenuEntry(new Settings());
            PieMenu.addMenuEntry(new Plus());
            PieMenu.addMenuEntry(new Search());

            ll.addView(PieMenu);
            RelativeLayout search = (RelativeLayout)findViewById(R.id.search);


        } catch (Exception e){
            System.out.println("EXCEPTIONIS122 "+e.toString());
        }
    }
    public boolean onTouchEvent(MotionEvent e) {
        drawable  = new int[]{R.drawable.settings, R.drawable.search, R.drawable.search, R.drawable.search};

        int state = e.getAction();
        int eventX = (int) e.getX();
        int eventY = (int) e.getY();
        if (state == MotionEvent.ACTION_DOWN) {


            System.out.println( "Button Pressed");
            Toast.makeText(RadialMenuActivity.this, "Screen Touched!",
                    Toast.LENGTH_SHORT).show();

            //Screen Sizes
            int xScreenSize = (getResources().getDisplayMetrics().widthPixels);
            int yScreenSize = (getResources().getDisplayMetrics().heightPixels);
            int xLayoutSize = ll.getWidth();
            int yLayoutSize = ll.getHeight();
            int xCenter = xScreenSize/2;
            int xSource = eventX;
            int yCenter = yScreenSize/2;
            int ySource = eventY;

            if (xScreenSize != xLayoutSize) {
                xCenter = xLayoutSize/2;
                xSource = eventX-(xScreenSize-xLayoutSize);
            }
            if (yScreenSize != yLayoutSize) {
                yCenter = yLayoutSize/2;
                ySource = eventY-(yScreenSize-yLayoutSize);
            }
            PieMenu = new RadialMenuWidget(getBaseContext());

            // PieMenu.setSourceLocation(xSource, ySource);
            PieMenu.setShowSourceLocation(true);
            PieMenu.setSourceLocation(100,100);
            //PieMenu.setCenterLocation(240,400);
            PieMenu.setInnerRingRadius(110, 160);
            PieMenu.setSelectedColor(R.color.orange,255);
            PieMenu.setCenterCircleRadius(105);
            PieMenu.setOuterRingColor(R.color.blue,255);
            //PieMenu.setHeader("Menu Header", 20);


            PieMenu.setSourceLocation(xLayoutSize, yLayoutSize);
            PieMenu.setIconSize(15, 30);
            PieMenu.setTextSize(13);

            PieMenu.setCenterCircle(new Close());
            PieMenu.addMenuEntry(new Connect());
            PieMenu.addMenuEntry(new Settings());
            PieMenu.addMenuEntry(new Plus());
            PieMenu.addMenuEntry(new Search());

            ll.addView(PieMenu);

        }
        return true;
    }




    public class Close implements RadialMenuEntry
    {

        public String getName() { return "Close"; }
        public String getLabel() { return "Close"; }
        public int getIcon() { return 0; }
        public List<RadialMenuEntry> getChildren() { return null; }
        public void menuActiviated()
        {

            System.out.println( "Close Menu Activated");
            //Need to figure out how to to the layout.removeView(PieMenu)
            //ll.removeView(PieMenu);
            ((LinearLayout)PieMenu.getParent()).removeView(PieMenu);


        }
    }


    public  class Connect implements RadialMenuEntry
    {
        public String getName() { return "Connect"; }
        public String getLabel() { return null; }
        public int getIcon() { return drawable[0]; }
        public List<RadialMenuEntry> getChildren() { return null; }
        public void menuActiviated()
        {
            System.out.println( "Connect12345 Menu #1 Activated - No Children");
        }
    }

    public  class Search implements RadialMenuEntry
    {
        public String getName() { return "Search"; }
        public String getLabel() { return null; }
        public int getIcon() { return drawable[1]; }
        public List<RadialMenuEntry> getChildren() { return null; }
        public void menuActiviated()
        {
            System.out.println( "Connect12345 Menu #2 Activated - Children");
        }
    }

    public  class Settings implements RadialMenuEntry
    {
        public String getName() { return "Settings"; }
        public String getLabel() { return null; }
        public int getIcon() { return drawable[2]; }
        public List<RadialMenuEntry> getChildren() { return null; }
        public void menuActiviated()
        {
            System.out.println( "Connect12345 New Test Menu Activated");
        }
    }

    public  class Plus implements RadialMenuEntry
    {
        public String getName() { return "Plus"; }
        public String getLabel() { return null ; }
        public int getIcon() { return drawable[3]; }
        public List<RadialMenuEntry> getChildren() { return null; }
        public void menuActiviated()
        {
            System.out.println( "Connect12345 Circle Options Activated");
        }
    }

}
