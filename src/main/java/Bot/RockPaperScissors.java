package Bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.Locale;
import java.util.Random;

public class RockPaperScissors {

    public static void play(CallbackQuery query, Bot bot) {
        int AI = new Random().nextInt(3);
        String test1 = RPS.valueOf(query.getData().toUpperCase(Locale.ROOT)).toString();
        int player = (RPS.valueOf(query.getData().toUpperCase(Locale.ROOT))).getValue();
        bot.sendMsg(query.getMessage(), RPS.getEmoji(AI));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (player == AI) bot.sendMsg(query.getMessage(), "У нас ничья");
        else if (player - AI == 1 || player - AI == -2) bot.sendMsg(query.getMessage(), "Поздравляю, вы выиграли!");
        else bot.sendMsg(query.getMessage(), "Ха, в этот раз я победитель!");
    }

}
