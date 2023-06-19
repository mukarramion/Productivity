package com.example.productivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TodoAdapter textAdapter;
    private List<TodoModel> textList;
    private EditText editText;
    private FloatingActionButton floatingActionButton;
    private ImageView imageView;
    private static final String SHARED_PREFS_KEY = "MyTodoList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        recyclerView = findViewById(R.id.todo_RV);
        editText = findViewById(R.id.todo_EditMessage);
        floatingActionButton = findViewById(R.id.todo_FABAdd);
        imageView = findViewById(R.id.todoL_back);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        textList = retrieveTodoList();
        textAdapter = new TodoAdapter(textList);
        recyclerView.setAdapter(textAdapter);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(TodoListActivity.this, "Please enter the task", Toast.LENGTH_SHORT).show();
                    return;
                }
                textList.add(new TodoModel(editText.getText().toString()));
                textAdapter.notifyItemInserted(textList.size());
                recyclerView.scrollToPosition(textAdapter.getItemCount() - 1);
                editText.setText("");
                saveTodoList(textList);
            }
        });

        textAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showConfirmationDialog(position);
            }
        });
    }

    private void saveTodoList(List<TodoModel> textList) {
        Gson gson = new Gson();
        String jsonTodoList = gson.toJson(textList);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("todoList", jsonTodoList);
        editor.apply();
    }

    private void showConfirmationDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to delete the task?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked the Confirm button
                        removeItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked the Cancel button
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void removeItem(int position) {
        textList.remove(textList.get(position));
        textAdapter.notifyDataSetChanged();
        saveTodoList(textList);
    }

    private ArrayList<TodoModel> retrieveTodoList() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        String jsonTodoList = sharedPreferences.getString("todoList", "");

        if (!jsonTodoList.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<TodoModel>>() {}.getType();
            return gson.fromJson(jsonTodoList, type);
        }

        return new ArrayList<>();
    }
}