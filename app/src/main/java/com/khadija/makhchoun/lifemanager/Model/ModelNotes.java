package com.khadija.makhchoun.lifemanager.Model;

/**
 * created by pc on 01/03/2021.
 **/
public class ModelNotes {
    String title,description;
    Object timestamp;
    String NoteID;

    public ModelNotes() {
    }

    public ModelNotes(String title, String description, Object timestamp, String noteID) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        NoteID = noteID;
    }

    public String getNoteID() {
        return NoteID;
    }

    public void setNoteID(String noteID) {
        NoteID = noteID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
