package Bot;

public class Lesson {
    private String room;
    private String name;
    private String type;
    private String teacher;
    private String time;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return Icon.CLOCK.get() + time + '\n'
                + Icon.PLACE.get() + room + '\n'
                + Icon.BOOKS.get() + name + ' '
                + type + '\n'
                + Icon.TEACHER.get() + teacher;
    }
}
