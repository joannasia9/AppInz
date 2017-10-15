package com.example.asia.jmpro.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by asia on 15/10/2017.
 */

public class Note extends RealmObject {
    @PrimaryKey
    private int id;

    private String noteContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }
}
