package com.nobrain.devfest.reactiveandroid.list;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.nobrain.devfest.reactiveandroid.R;
import com.nobrain.devfest.reactiveandroid.detail.TodoDetailActivity;
import com.nobrain.devfest.reactiveandroid.list.adapter.TodoAdapter;
import com.nobrain.devfest.reactiveandroid.repository.TodoRepository;
import com.nobrain.devfest.reactiveandroid.repository.domain.Todo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoListActivity extends AppCompatActivity {

    public static final int REQ_MODIFY = 102;
    private static final int REQ_ADD = 101;
    @BindView(R.id.list_todo_list)
    RecyclerView listTodo;

    private TodoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_todo_list);

        ButterKnife.bind(this);

        initListView();
        initTodoDatas();

        TodoRepository.getRepository().getTodos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_add) {

            startActivityForResult(new Intent(this, TodoDetailActivity.class), REQ_ADD);

            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQ_ADD) {
            initTodoDatas();
        } else if (requestCode == REQ_MODIFY) {
            long id = data.getLongExtra("id", -1);
            if (id > 0) {
                updateItem(id);
            }
        }
    }

    private void updateItem(long id) {
        int itemPosition = -1;
        for (int idx = 0, size = adapter.getItemCount(); idx < size; idx++) {
            if (adapter.getItem(idx).id == id) {
                itemPosition = idx;
                break;
            }
        }

        if (itemPosition < 0) {
            return;
        }

        if (TodoRepository.getRepository().hasItem(id)) {

            Todo item = adapter.getItem(itemPosition);
            item.content = TodoRepository.getRepository().get(id).content;
        } else {
            adapter.remove(itemPosition);
        }

        adapter.notifyDataSetChanged();

    }

    private void initListView() {
        adapter = new TodoAdapter();
        listTodo.setAdapter(adapter);
        listTodo.setLayoutManager(new LinearLayoutManager(TodoListActivity.this));

        adapter.setOnRecyclerItemClickListener((adapter1, position) -> {
            long id = adapter.getItem(position).id;
            Intent intent = new Intent(this, TodoDetailActivity.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, REQ_MODIFY);
        });
    }

    private void initTodoDatas() {

        adapter.clear();

        List<Todo> todos = TodoRepository.getRepository().getTodos();
        for (Todo todo : todos) {
            adapter.add(todo);
        }

        adapter.notifyDataSetChanged();
    }
}
