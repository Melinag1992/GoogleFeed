package nyc.c4q.googlefeed.ToDo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nyc.c4q.googlefeed.R;

public class ToDoActivity extends AppCompatActivity {
    @BindView(R.id.floatingActionButton)
    FloatingActionButton add;
    @BindView(R.id.todo_title)
    EditText title;
    @BindView(R.id.todo_description)
    EditText detail;
    @BindView(R.id.rv)
    RecyclerView rv;

    private static final String TAG = "MainActivity";
    private final static String KEY_SHAREDPREF = "sharedpreferences";

    private SharedPreferences sharepref;

    private NoteAdapter noteAdapter;

    private List<Note> notelist = new ArrayList <>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        ButterKnife.bind(this);

        //Instanciate the sharepref before retrieving data.
        sharepref = getSharedPreferences(KEY_SHAREDPREF, MODE_PRIVATE);

        hideSoftKeyboard();
        retriveSharedValue();

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        noteAdapter = new NoteAdapter(notelist);
        rv.setAdapter(noteAdapter);

        ItemTouchHelper.Callback callback = new SwipeHelper(noteAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);
    }

    @OnClick(R.id.floatingActionButton)
    public void onClick(View view) {
        if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(detail.getText().toString())) {
            Log.e(TAG, "onClick: " + title.getText().toString() + " " + detail.getText().toString());
            notelist.add(new Note(title.getText().toString(), detail.getText().toString()));
            title.setText("");
            detail.setText("");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(title.getWindowToken(), 0);
            inputMethodManager.hideSoftInputFromWindow(detail.getWindowToken(), 0);

        } else {
            Toast.makeText(ToDoActivity.this, "Enter the missing field", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void save() {
        SharedPreferences.Editor editor= sharepref.edit();
        Gson gson = new Gson();
        // serializes noteList to Json
        String jsonText= gson.toJson(notelist);
        editor.putString("NoteList", jsonText);
        editor.apply();
    }

    private void retriveSharedValue(){
        Gson gson= new Gson();
        String jsonText= sharepref.getString("NoteList", null);
        Type type = new TypeToken<ArrayList<Note>>() {}.getType();
        //deserializes json into notelist
        notelist= gson.fromJson(jsonText, type);
        if (notelist == null) {
            notelist = new ArrayList <>();
        }
    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }
}
