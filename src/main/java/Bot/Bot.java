package Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static Bot.Anecdotes.sayJoke;
import static Bot.Parser.parse;

public class Bot extends TelegramLongPollingBot {
    private boolean groupMessage = false;
    private Message message;

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "LazyBulbasaurusBot";
    }

    @Override
    public String getBotToken() {
        return "5823957267:AAHiMkAcrkQ_PWd0OTDLDxkISkEcbMIpYBY";
    }

    @Override
    public void onUpdateReceived(Update update) {
        message = update.getMessage();
        if (message != null && message.hasText()) registerMessage();
    }

    public void registerMessage() {
        WeatherModel model = new WeatherModel();
        List<String> lst = new ArrayList<>(Arrays.asList(message.getText().split(" ")));
        if (groupMessage) {
            lst.add(0, "расписание");
            groupMessage = false;
        }
        switch (lst.get(0).toLowerCase(Locale.ROOT)) {
            case (("/help")):
                sendMsg(message, "Чем могу помочь?");
                break;
            case ("/start"):
                sendMsg(message, "Привествую");
                break;
            case ("расписание"):
                if (lst.size() > 1) sendMsg(message, parse(lst.get(1)));
                else {
                    sendMsg(message, "Расписание какой группы хотите увидеть?");
                    groupMessage = true;
                }
                break;
            case ("анекдот"):
                sendMsg(message, sayJoke());
                break;
            case ("погода"):
                try {
                    sendMsg(message, Weather.getWeather(model));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ("мем"):
//                sendImage("https://fikiwiki.com/uploads/posts/2022-02/1644965605_9-fikiwiki-com-p-kartinki-priroda-na-zastavku-telefona-9.jpg" ,message.getChatId().toString());
//                sendImage("src/main/resources/test4.jpeg",message.getChatId().toString());
                sendImage(message);
                break;
            case ("стипендия"):
            case ("стипуха"):
                sendMsg(message, Icon.DOLLAR.get() + " До стипендии осталось " + scholarship() + ' ' + Icon.DOLLAR.get());
                break;
            default:
                sendMsg(message, "Эм, не понял");
                break;
        }
    }

//    public void sendImage(String url, String chatId) {
//        // Create send method
//        SendPhoto sendPhotoRequest = new SendPhoto();
//        // Set destination chat id
//        sendPhotoRequest.setChatId(chatId);
//        // Set the photo url as a simple photo
//        sendPhotoRequest.setPhoto(new InputFile(url));
//        try {
//            // Execute the method
//            execute(sendPhotoRequest);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }

    public void sendImage(String filePath, String chatId) {
        // Create send method
        SendPhoto sendPhotoRequest = new SendPhoto();
        // Set destination chat id
        sendPhotoRequest.setChatId(chatId);
        // Set the photo file as a new photo (You can also use InputStream with a constructor overload)
        sendPhotoRequest.setPhoto(new InputFile(new File(filePath)));
        try {
            // Execute the method
            execute(sendPhotoRequest);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendImage(Message message){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        try {
            sendPhoto.setPhoto(ImgParser.imageParser());
//            sendPhoto.setPhoto(new InputFile(("src/main/resources/test4.jpeg")));
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, List<Textable> lessons) {
        if ("!".equals(lessons.get(0).safeTextForm().substring(0,1))) {
            sendMsg(message, lessons.get(0).safeTextForm().substring(1));
            return;
        }
        String str = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
        String str2 = Icon.CALENDAR.get() + ' ' + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM")) + ' ' + str.substring(0, 1).toUpperCase() + str.substring(1);
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            sendMessage.setText(str2);
            execute(sendMessage);
            if (lessons.size() == 0) {
                sendMsg(message, "Сегодня у вас выходной");
                return;
            }
            for (Textable l : lessons) {
                sendMessage.setText(l.safeTextForm());
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String scholarship() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime payDay = now.withDayOfMonth(25);
        if (payDay.getDayOfWeek().getValue() > 5) payDay = payDay.minusDays(payDay.getDayOfWeek().getValue() - 5);
        long diff = ChronoUnit.DAYS.between(now, payDay);
        if (diff <= 0) {
            if (payDay.getMonthValue() + 1 > 12) payDay = payDay.withMonth(1).withYear(payDay.getYear() + 1);
            else payDay = payDay.withMonth(payDay.getMonthValue() + 1);
        }
        diff = ChronoUnit.DAYS.between(now, payDay);
        String str = Long.toString(diff);
        if (diff % 10 == 1 && diff % 100 != 1) str += " день";
        else if ((diff % 10 == 2 || diff % 10 == 3 || diff % 10 == 4) && (diff % 100 != 1)) str += " дня";
        else str += " дней";
        return str;

    }

}
