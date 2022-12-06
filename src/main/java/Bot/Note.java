package Bot;


public class Note{
    private final long id;
    private final String text;

    public Note(long id, String note) {
        this.id = id;
        this.text = note;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
