package Bot;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardRPS {
    public static InlineKeyboardMarkup getInlineKeyboardRPS() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();

        InlineKeyboardButton rock = new InlineKeyboardButton();
        rock.setText(Icon.ROCK.get());
        rock.setCallbackData("rock");
        InlineKeyboardButton paper = new InlineKeyboardButton();
        paper.setText(Icon.PAPER.get());
        paper.setCallbackData("paper");
        InlineKeyboardButton scissors = new InlineKeyboardButton();
        scissors.setText(Icon.SCISSORS.get());
        scissors.setCallbackData("scissors");

        row.add(rock);
        row.add(paper);
        row.add(scissors);
        rows.add(row);

        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

}
