package com.example.tonywu.multinotes;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity{
    private EditText textTitle;
    private EditText textNote;
    private String title;
    private String contents;
    private Intent intent;
    private notedetails note1;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        textTitle = (EditText) findViewById(R.id.textTitle);
        textNote = (EditText) findViewById(R.id.textNote);
        intent = getIntent();
        note1 = (notedetails) intent.getSerializableExtra("note");
        textTitle.setText(note1.getTitle());
        textNote.setText(note1.getNote());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //if(item.getItemId() == R.id.menuSave){
        switch(item.getItemId()) {
            case R.id.menuSave:
                title = textTitle.getText().toString();
                contents = textNote.getText().toString();
                noTitle();
               /* if(title.length()==0){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    CharSequence text = "Can't s2ave without title";
                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                    //finish();
                }*/

                note1.setTitle(title);
                note1.setNote(contents);

                //Calendar cal = Calendar.getInstance();
                //SimpleDateFormat time = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                note1.setDate(getTime());


                intent.putExtra("note", note1);
                setResult(RESULT_OK, intent);
                finish();


                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //else{
          //  return super.onOptionsItemSelected(item);
        //}

    }
    @Override
    public void onBackPressed(){
        if (note1.getNote().equals(textNote.getText().toString()) && note1.getTitle().equals(textTitle.getText().toString())){
            super.onBackPressed();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("exit");
        builder.setMessage("Save note?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                title = textTitle.getText().toString();
                contents = textNote.getText().toString();
                noTitle();
                //textTitle.setText("what theheck");
                note1.setTitle(title);
                //note1.setNote("testd");
                note1.setNote(contents);
                note1.setDate(getTime());


                /*if(title.length()==0){
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    CharSequence text = "Can't save without title";
                    Toast toast = Toast.makeText(context,text,duration);
                    toast.show();
                    finish();
                }*/

                //Calendar cal = Calendar.getInstance();
                //SimpleDateFormat time = new SimpleDateFormat("MM-dd-yyyy HH:mm");
                //notedetails.setDate(time.format(cal));

                Log.d("noteString", "" + note1.getId() + note1.getTitle() + note1.getNote() + note1.getDate());

                intent.putExtra("note", note1);
                setResult(RESULT_OK, intent);
                EditActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void noTitle(){
        title = textTitle.getText().toString();
        if(title.length() == 0){

            Toast toast = Toast.makeText(getApplicationContext(),"Can't save without title",Toast.LENGTH_LONG);
            toast.show();
            finish();
        }
    }

    public String getTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        return date.format(cal.getTime());
    }
}

