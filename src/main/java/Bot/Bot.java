package Bot;

import  org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        System.out.println(message.getText());
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
            case ("стипендия"):
            case ("стипуха"):
                sendMsg(message, Icon.DOLLAR.get() + " До стипендии осталось " + scholarship() + ' ' + Icon.DOLLAR.get());
                break;
            case ("заметка"):
                sendMsg(message, new Notes().getNote(message.getChatId()) + "");
                break;
            case ("создать"):
                if (lst.size() > 1 && lst.get(1).equals("заметку")) {
                    if (lst.size() > 2) {
                        StringBuilder noteText = new StringBuilder(lst.get(2));
                        for (int i = 3; i < lst.size(); i++) noteText.append(" ").append(lst.get(i));
                        sendMsg(message, new Notes().createNote(message.getChatId(), noteText.toString()) + "");
                        break;
                    } else sendMsg(message, "Заметка не может быть пустой");
                } else sendMsg(message, "Эм, не понял");
                break;
            case ("удалить"):
                if (lst.size() > 1 && lst.get(1).equals("заметку"))
                    sendMsg(message, new Notes().removeNote(message.getChatId()) + "");
                else sendMsg(message, "Эм, не понял");
                break;
            case ("выражение"):
                Expression expression = randomExpression();
                sendMsg(message, expression.send() + "");
                break;
            case ("совет"):
                sendMsg(message, new Advice().send() + "");
                break;
            case ("цитата"):
                sendMsg(message, new Quote().send() + "");
                break;
            case ("факт"):
                System.out.println(message.getFrom().getId());
                sendMsg(message, new Fact().send() + "");
                break;
            case ("/report"):
                if (lst.size() > 1) {
                    StringBuilder noteText = new StringBuilder(lst.get(1));
                    for (int i = 2; i < lst.size(); i++) noteText.append(" ").append(lst.get(i));
                    sendMsg("1984385382", noteText + "");
                    sendMsg("841196670", noteText + "");
                    sendMsg("456755500", noteText + "");
                    sendMsg(message, "Репорт доставлен");
                    break;
                } else sendMsg(message, "Жалоба не может быть пустой");
                break;
            case ("/pm"):
                if (lst.size() > 1) {
                    String id_user = lst.get(1);
                    if (lst.size() > 2) {
                        StringBuilder noteText = new StringBuilder(lst.get(2));
                        for (int i = 3; i < lst.size(); i++) noteText.append(" ").append(lst.get(i));
                        sendMsg(id_user, noteText + "");
                        sendMsg(message, "Сообщение доставлено");
                        break;
                    } else sendMsg(message, "Сообщение не должно быть пустым");
                } else sendMsg(message, "Эм, не понял");
                break;
            default:
                sendMsg(message, "Эм, не понял");
                break;
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        ReplyKeyboardMarkup replyKeyboardMarkup = setButtons(sendMessage);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, List<Textable> lessons) {
        if ("!".equals(lessons.get(0).safeTextForm().substring(0, 1))) {
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

    public static Expression randomExpression() {
        Random random = new Random();
        int i = random.nextInt(3);
        System.out.println(i);
        if (i == 0) return new Advice();
        else if (i == 1) return new Quote();
        else return new Fact();
    }

    public ReplyKeyboardMarkup setButtons(SendMessage sendMessage){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardRow keyboardRow2 = new KeyboardRow();
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow1.add("расписание");
        keyboardRow1.add("анекдот");
        keyboardRow1.add("погода");
        keyboardRow1.add("стипендия");
        keyboardRow2.add("заметка");
        keyboardRow2.add("создать");
        keyboardRow2.add("удалить");
        keyboardRow2.add("выражение");
        keyboardRow3.add("совет");
        keyboardRow3.add("цитата");
        keyboardRow3.add("факт");
        keyboard.add(keyboardRow1);
        keyboard.add(keyboardRow2);
        keyboard.add(keyboardRow3);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
