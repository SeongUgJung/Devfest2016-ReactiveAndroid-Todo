package com.nobrain.devfest.reactiveandroid.list.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nobrain.devfest.reactiveandroid.R;
import com.nobrain.devfest.reactiveandroid.repository.domain.Todo;
import com.nobrain.devfest.reactiveandroid.view.OnRecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private final List<Todo> todoList;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public TodoAdapter() {
        todoList = new ArrayList<>();
    }


    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo item = getItem(position);

        holder.bind(item);

        holder.itemView.setOnClickListener(v -> {
            if (onRecyclerItemClickListener != null) {
                onRecyclerItemClickListener.onItemClick(TodoAdapter.this, position);
            }
        });
    }

    public Todo getItem(int position) {
        return todoList.get(position);
    }

    public void update(int position, Todo item) {
        todoList.set(position, item);
    }

    public void add(Todo todo) {
        todoList.add(todo);
    }

    public void remove(int position) {
        todoList.remove(position);
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void clear() {
        todoList.clear();
    }

    static class TodoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_todo_summary)
        TextView tvTitle;

        public TodoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Todo todo) {
            tvTitle.setText(todo.content);
        }
    }

}
