package Bot;

import java.util.ArrayList;
import java.util.List;

public class Notes {
    static List<Note> notes = new ArrayList<>();

    public String createNote(long id, String text) {
        for (Note note : notes) {
            if (note.getId() == id) return "Заметка существует";
        }
        notes.add(new Note(id, text));
        return "заметка успешно создана";
    }

    public String removeNote(long id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                notes.remove(note);
                return "заметка успешно удалена";
            }
        }
        return "заметки не существует";
    }

    public String getNote(long id) {
        for (Note note : notes) {
            if (note.getId() == id) return note.getText() + "";
        }
        return "заметки не существует";
    }
}
