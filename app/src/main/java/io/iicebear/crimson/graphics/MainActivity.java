package io.iicebear.crimson.graphics;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Simple programmatic UI layout for mobile view efficiency
        TextView textView = new TextView(this);
        textView.setText("Crimson Graphics Unlocker\n\nStatus: Module Active\nTargeting: High-End Presets");
        textView.setTextSize(20);
        textView.setTextColor(Color.parseColor("#FF0033")); // Crimson Red
        textView.setGravity(Gravity.CENTER);
        
        setContentView(textView);
    }
}
