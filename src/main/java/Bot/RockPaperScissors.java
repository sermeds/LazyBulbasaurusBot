package Bot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.lang.reflect.Array;
import java.util.*;

import Bot.Gesture;

public class RockPaperScissors {

    public static void play(CallbackQuery query, Bot bot) {
        RandomFactory randomFactory = new RandomFactory((Arrays.asList(new RockFactory(), new PaperFactory(), new ScissorsFactory())));
        Gesture AI = randomFactory.create();
        Gesture player = null;
        if (query.getData().equalsIgnoreCase("rock")) player = new Rock();
        if (query.getData().equalsIgnoreCase("paper")) player = new Paper();
        if (query.getData().equalsIgnoreCase("scissors")) player = new Scissors();
        bot.sendMsg(query.getMessage(), AI.getEmoji());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (player.getValue() == AI.getValue()) bot.sendMsg(query.getMessage(), "У нас ничья");
        else if (player.getValue() - AI.getValue() == 1 || player.getValue() - AI.getValue() == -2) bot.sendMsg(query.getMessage(), "Поздравляю, вы выиграли!");
        else bot.sendMsg(query.getMessage(), "Ха, в этот раз я победитель!");
    }

}
