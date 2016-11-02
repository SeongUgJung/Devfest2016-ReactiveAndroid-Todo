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
import com.nobrain.devfest.reactiveandroid.repository.observer.DataObserver;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;

public class TodoListActivity extends AppCompatActivity {

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

        DataObserver.getInstance().register(this, Todo.class, observable -> {
            return observable.filter(it -> it.added())
                    .map(todo -> todo.data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(todo -> {
                        adapter.add(todo);
                        adapter.notifyDataSetChanged();
                    });
        });
        DataObserver.getInstance().register(this, Todo.class, observable -> {
            return observable.filter(it -> it.updated())
                    .map(todo -> todo.data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(todo -> {
                        for (int idx = 0; idx < adapter.getItemCount(); idx++) {
                            if (adapter.getItem(idx).id == todo.id) {
                                adapter.getItem(idx).content = todo.content;
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    });
        });
        DataObserver.getInstance().register(this, Todo.class, observable -> {
            return observable.filter(it -> it.deleted())
                    .map(todo -> todo.data)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(todo -> {
                        for (int idx = 0; idx < adapter.getItemCount(); idx++) {
                            if (adapter.getItem(idx).id == todo.id) {
                                adapter.remove(idx);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    });
        });
    }

    @Override
    protected void onDestroy() {
        DataObserver.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_menu_add) {

            startActivity(new Intent(this, TodoDetailActivity.class));

            return true;
        }
        return false;
    }

    private void initListView() {
        adapter = new TodoAdapter();
        listTodo.setAdapter(adapter);
        listTodo.setLayoutManager(new LinearLayoutManager(TodoListActivity.this));

        adapter.setOnRecyclerItemClickListener((adapter1, position) -> {
            long id = adapter.getItem(position).id;
            Intent intent = new Intent(this, TodoDetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
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
