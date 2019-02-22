package com.example.tonywu.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;





public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerViewer;
    private List<notedetails> noteList = new ArrayList<>();
    private noteAdaptor noteAdapt;
    private String filesave = "file.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewer = findViewById(R.id.recyclerView);
        noteAdapt = new noteAdaptor(noteList,this);
        recyclerViewer.setAdapter(noteAdapt);
        recyclerViewer.setLayoutManager(new LinearLayoutManager(this));
        //noteList.clear();
        new AsyncNoteTask().execute();
        //new
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.menuAdd:

                intent = new Intent(this, EditActivity.class);
                notedetails newNote = new notedetails(noteList.size(),"","","");
                intent.putExtra("note", newNote);
                startActivityForResult(intent, 1);

                return true;
            case R.id.menuInfo:
                intent = new Intent(this, AboutActivity.class);
                startActivityForResult(intent,1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                notedetails note = (notedetails) data.getSerializableExtra("note");
                Log.d("noteStrMain", "" + note.getId() + note.getTitle() + note.getNote() + note.getDate());

                if (note.getId() < noteList.size()) {
                    // for(int z = 0; z<noteList.size()-1; z++ ){
                    //   notedetails temp = (notedetails)
                    //}
                    int z = 0;
                    for (Iterator iter = noteList.iterator(); iter.hasNext(); ) {
                        notedetails temp = (notedetails) iter.next();
                        if (temp.getId() == note.getId()) {
                            break;
                        }
                        z++;
                    }
                    noteList.remove(z);
                }


                noteList.add(0, note);
                noteAdapt.notifyDataSetChanged();
                //noteadaptor change
            } else {
                Log.d("main", "onResult: result Code: " + resultCode);
            }


        }else{
            Log.d("main", "onResult: request Code: " + requestCode);}
    }

    @Override
    public boolean onLongClick(final View view){
        final int p = recyclerViewer.getChildLayoutPosition(view);
        AlertDialog.Builder build = new AlertDialog.Builder(this);

        build.setTitle("Delete?");
        build.setMessage("Are you sure you want to delete note?");
        build.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noteList.remove(p);
                Toast toast = Toast.makeText(MainActivity.this, "note deleted", Toast.LENGTH_SHORT);
                toast.show();
                noteAdapt.notifyDataSetChanged();
            }
        });
        build.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alert = build.create();
        alert.show();
        return false;
    }


    @Override
    public void onClick(View view){
        int pos = recyclerViewer.getChildLayoutPosition(view);
        notedetails note2 = noteList.get(pos);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("note",note2);
        startActivityForResult(intent, 1);

    }



    @Override
    protected void onPause(){

        super.onPause();

    try{
        FileOutputStream fileOut = openFileOutput(filesave, MODE_PRIVATE);
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileOut, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();

        for(Iterator iter = noteList.iterator(); iter.hasNext();){
            notedetails noteWrite = (notedetails) iter.next();

            writer.beginObject();
            writer.name("id").value(noteWrite.getId());
            writer.name("title").value(noteWrite.getTitle());
            writer.name("note").value(noteWrite.getNote());
            writer.name("date").value(noteWrite.getDate());
            //writer.name("date").value("hellofromspace");
            //Toast toast = Toast.makeText(MainActivity.this, "json made", Toast.LENGTH_SHORT);
            //toast.show();
            writer.endObject();
            /*
            writer.close();
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("id").value(noteWrite.getId());
            writer.name("title").value(noteWrite.getTitle());
            writer.name("note").value(noteWrite.getNote());
            writer.name("date").value(noteWrite.getDate());
            writer.endObject();
            writer.close();
            Log.d("MainActivity", "savingproduct:json\n"+sw.toString());*/
        }
        writer.endArray();
        writer.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    class AsyncNoteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                FileInputStream fileIn = openFileInput(filesave);
                JsonReader reader = new JsonReader(new InputStreamReader((InputStream) fileIn, "UTF-8"));


                reader.beginArray();
                while (reader.hasNext()) {
                    reader.beginObject();
                    String title = "";
                    String note = "";
                    String date = "";
                    int id = 0;
                    while (reader.hasNext()) {
                        String name = reader.nextName();
                        if (name.equals("id")) {
                            id = reader.nextInt();
                        }
                        else if (name.equals("title"))
                        {
                            title = reader.nextString();
                        } else if (name.equals("note"))
                        {
                            note = reader.nextString();
                        } else if (name.equals("date"))
                        {
                            //date = "hellofriend";
                            date = reader.nextString();
                        } else {
                            reader.skipValue();
                        }
                    }
                    notedetails noteSync = new notedetails(id, title,note, date);
                    noteList.add(noteSync);
                    //Toast toast = Toast.makeText(MainActivity.this, "json saved", Toast.LENGTH_SHORT);
                    //toast.show();
                    reader.endObject();

                }
                reader.endArray();
                reader.close();
                noteAdapt.notifyDataSetChanged();

            } catch (FileNotFoundException e) {
                try {
                    FileOutputStream fileOutput = openFileOutput(filesave, 0);
                    fileOutput.close();
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

    public void updateD(ArrayList<notedetails> nlist){
        recyclerViewer.setAdapter(noteAdapt);
    }

}
