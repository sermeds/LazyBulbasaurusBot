package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static List<Lesson> parse() {
        Document doc = null;
        List<Lesson> lessons = new ArrayList<>();
        try {
            doc = Jsoup.connect("https://rasp.sstu.ru/rasp/group/136").get();
            Elements element = doc.getElementsByClass("day-current");
            if (element.size() == 0) throw new RuntimeException("Расписание не найдено");
            Element el = null;
            for (int i = 1; i <= element.first().childrenSize() - 1; i++) {
                el = element.first().child(i);
                if (el.hasClass("day-lesson-empty")) continue;
                Lesson lesson = new Lesson();
                String time = el.child(0).getElementsByClass("lesson-hour").text();
                lesson.setTime(time.substring(0, time.length() - 1));
                lesson.setRoom(el.child(0).getElementsByClass("lesson-room").first().text());
                lesson.setName(el.child(0).getElementsByClass("lesson-name").text());
                lesson.setType(el.child(0).getElementsByClass("lesson-type").text());
                lesson.setTeacher(el.child(0).getElementsByClass("lesson-teacher").text());
                lessons.add(lesson);
            }
//            System.out.println(tmp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lessons;
    }

}
