package Bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import javax.xml.soap.MessageFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
                default:
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

    public void sendMsg(Message message, List<Lesson> lessons) {
        String str = LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("ru", "RU"));
        String str2 = Icon.CALENDAR.get() + ' ' + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM")) + ' ' + str.substring(0, 1).toUpperCase() + str.substring(1);
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        try {
            sendMessage.setText(str2);
            execute(sendMessage);
            for (Lesson l : lessons) {
                sendMessage.setText(l.toString());
                execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
