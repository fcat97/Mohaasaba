package com.example.mohaasaba.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Todo implements Serializable {
    private List<Boolean> states;
    private List<String> todoTexts;

    public Todo() {
        this.states = new ArrayList<>();
        this.todoTexts = new ArrayList<>();
    }
    public Todo(List<Boolean> states, List<String> todoTexts) {
        this.states = states;
        this.todoTexts = todoTexts;
    }

    public void addNew(String text) {
        states.add(false);
        todoTexts.add(text);
    }
    public void removeTodo(int index) {
        states.remove(index);
        todoTexts.remove(index);
    }
    public void editTodo(int index, String text) {
        todoTexts.set(index, text);
    }
    public void markComplete(int index) {
        states.set(index, true);
    }
    public void markIncomplete(int index) {
        states.set(index, false);
    }
    public void toggleState(int index) {
        if (states.get(index)) states.set(index, false);
        else states.set(index, true);
    }
    public String getText(int index) {
        return todoTexts.get(index);
    }
    public boolean getState(int index) {
        return states.get(index);
    }
    public List<Boolean> getStates() {
        return states;
    }
    public List<String> getTodoTexts() {
        return todoTexts;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
