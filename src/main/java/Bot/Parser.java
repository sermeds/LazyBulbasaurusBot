package Bot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public static List<Textable> parseRasp(String nameGroup, String date) {
        if (date == null) date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM"));
        Document doc1 = null;
        List<Textable> lessons = new ArrayList<>();
        try {
            doc1 = Jsoup.connect("https://rasp.sstu.ru/rasp/").get();
            Elements el1 = doc1.getElementsByClass("group");
            String url = null;
            for (Element el : el1) {
                if (el.text().equalsIgnoreCase(nameGroup)) {
                    url = el.child(0).absUrl("href");
                }
            }
            if (url == null) {
                lessons.add(0, () -> "!Группа не найдена");
                return lessons;
            }
            Document doc2 = Jsoup.connect(url).get();
            Elements element = null;
            element = doc2.getElementsContainingOwnText(date).parents().parents();
            if (element.size() == 0) {
                lessons.add(0, () -> "!Расписание не найдено");
                return lessons;
            }
            String str = LocalDate.parse(date + ".22", DateTimeFormatter.ofPattern("dd.MM.yy")).getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
            String str2 = Icon.CALENDAR.get() + ' ' + date + ' ' + str.substring(0, 1).toUpperCase() + str.substring(1);
            lessons.add(() -> str2);
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

    public static List<Textable> parseExams(String nameGroup) {
        Document doc1 = null;
        List<Textable> lessons = new ArrayList<>();
        try {
            doc1 = Jsoup.connect("https://rasp.sstu.ru/rasp/").get();
            Elements el1 = doc1.getElementsByClass("group");
            String url = null;
            for (Element el : el1) {
                if (el.text().equalsIgnoreCase(nameGroup)) {
                    url = el.child(0).absUrl("href");
                }
            }
            if (url == null) {
                lessons.add(0, () -> "!Группа не найдена");
                return lessons;
            }
            Document doc2 = Jsoup.connect(url).get();
            Elements element = null;
            element = doc2.getElementsByClass("lesson-warnings");
            if (element.size() == 0) {
                lessons.add(0, () -> "!Экзамены не найдены");
                return lessons;
            }
            String sign = Icon.BOOK.get();
            for (Element e : element.first().children()) {
                if (e.text().equals("Зачеты:")) {
                    String finalSign = sign;
                    lessons.add(() -> finalSign + ' ' + e.text());
                    sign = Icon.SIGN.get();
                }
                else if (e.text().equals("Экзамены:")) {
                    sign = Icon.COMPUTER.get();
                    String finalSign1 = sign;
                    lessons.add(() -> finalSign1 + ' ' + e.text());
                    sign = Icon.DOUBLE.get();
                }
                else {
                    String finalSign2 = sign;
                    lessons.add(() -> finalSign2 + ' ' + e.text());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lessons;
    }

}
