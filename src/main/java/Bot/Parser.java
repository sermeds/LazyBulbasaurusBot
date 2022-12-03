package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static List<Sendable> parse(String nameGroup) {
        Document doc1 = null;
        List<Sendable> lessons = new ArrayList<>();
        try {
            doc1 = Jsoup.connect("https://rasp.sstu.ru/rasp/").get();
            Elements el1 = doc1.getElementsByClass("group");
            String url = null;
            for (Element el : el1) {
                if (el.text().equalsIgnoreCase(nameGroup)) {
                    url = el.child(0).absUrl("href");
                }
            }
            if (url == null){
                lessons.add(()->"!Группа не найдена");
                return lessons;
            }
            Document doc2 = Jsoup.connect(url).get();
            Elements element = doc2.getElementsByClass("day-current");
            if (element.size() == 0) {
                lessons.add(()->"!Расписание не найдено");
                return lessons;
            }
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lessons;
    }

}
