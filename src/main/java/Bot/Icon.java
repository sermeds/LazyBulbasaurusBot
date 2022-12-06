package Bot;

import com.vdurmont.emoji.EmojiParser;

public enum Icon {
    PLUS(":heavy_plus_sign:"),
    MINUS(":heavy_minus_sign:"),
    CHECK(":white_check_mark:"),
    NOT(":x:"),
    DOUBT(":zzz:"),
    FLAG(":checkered_flag:"),
    TEACHER(":man_teacher:"),
    BOOKS(":books:"),
    CLOCK(":alarm_clock:"),
    PLACE(":house:"),
    HI(":wave:"),
    OFFICE(":office:"),
    SUN(":sunny:"),
    DROP(":droplet:"),
    CALENDAR(":calendar:"),
    DOLLAR(":dollar:"),
    ROCK(":fist:"),
    PAPER(":raised_hand:"),
    SCISSORS(":v:");

    private String value;

    public String get() {
        return EmojiParser.parseToUnicode(value);
    }

    Icon(String value) {
        this.value = value;
    }
}