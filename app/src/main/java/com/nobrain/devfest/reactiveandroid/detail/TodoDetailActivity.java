package com.nobrain.devfest.reactiveandroid.detail;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.nobrain.devfest.reactiveandroid.R;
import com.nobrain.devfest.reactiveandroid.repository.TodoRepository;
import com.nobrain.devfest.reactiveandroid.repository.domain.Todo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoDetailActivity extends AppCompatActivity {

    @BindView(R.id.et_todo_detail)
    EditText etTodo;

    private long id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_todo_detail);
        ButterKnife.bind(this);

        setUpActionbar();
        initExtras();
        initTodo(id);


    }

    private void setUpActionbar() {

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.edit_todo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_menu_save:
                String text = etTodo.getText().toString();
                save(id, text);
                break;
            case R.id.action_menu_delete:
                delete(id);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void delete(long id) {
        if (id > 0) {
            TodoRepository.getRepository().remove(id);
            Intent data = new Intent();
            data.putExtra("id", id);
            setResult(RESULT_OK, data);
        }
        finish();
    }

    private void save(long id, String text) {
        long extraId;
        if (id > 0) {
            TodoRepository.getRepository().updateTodo(id, text);
            extraId = id;
        } else {
            extraId = System.currentTimeMillis();
            TodoRepository.getRepository().addTodo(extraId, text);
        }

        Intent data = new Intent();
        data.putExtra("id", extraId);
        setResult(RESULT_OK, data);
        finish();

    }

    private void initTodo(long id) {
        if (id > 0) {
            Todo todo = TodoRepository.getRepository().get(id);
            etTodo.setText(todo.content);
        }
    }

    private void initExtras() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("id")) {
                id = intent.getLongExtra("id", -1);
            }
        }
    }
}
