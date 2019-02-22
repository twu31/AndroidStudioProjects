package com.example.tonywu.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.EditText;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    //private EditText editText;
    private TextView history;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        history = findViewById(R.id.history);
        history.setMovementMethod(new ScrollingMovementMethod());
        textView = findViewById(R.id.tView);
        //editText = findViewById(R.id.editText);
    }


    public void temperature(View view){
        //TextView textView = (TextView)findViewById(R.id.tView);
        String historytext = history.getText().toString();
        EditText editText=(EditText)findViewById(R.id.editText);
        RadioButton c2f=(RadioButton)findViewById(R.id.c2fbutton);
        RadioButton f2c=(RadioButton)findViewById(R.id.f2cbutton);
        Button but=(Button)findViewById(R.id.button);
        double number=Double.parseDouble(String.valueOf(editText.getText()));
        String foo;


        if(c2f.isChecked()){
            foo = String.format("%.1f", c2f(number));
            foo = foo + " F°";
            textView.setText(foo);
            history.setText(number + " F° -> " + foo + "\n" + historytext);
            c2f.setChecked(true);
            f2c.setChecked(false);

        }
        if(f2c.isChecked()){
            foo = String.format("%.1f", f2c(number));
            foo = foo + " C°";
            textView.setText(foo);
            history.setText(number + " C° -> " + foo + "\n" + historytext);
            c2f.setChecked(false);
            f2c.setChecked(true);

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("HISTORY", history.getText().toString());
        outState.putString("value", textView.getText().toString());
        //outState.putString("OUTPUT2", output2.getText().toString());
        // Call super last
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // Call super first
        super.onRestoreInstanceState(savedInstanceState);

        history.setText(savedInstanceState.getString("HISTORY"));
        textView.setText(savedInstanceState.getString("value"));
        //output2.setText(savedInstanceState.getString("OUTPUT2"));
    }

    //celsius to farrenhiet method
    private double c2f(double c)
    {
        return (c*1.8)+32;
    }
    //fahrenhiet to celcius method
    private double f2c(double f)
    {
        return (f-32)/1.8;
    }
}
