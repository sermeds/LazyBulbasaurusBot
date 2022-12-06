package Bot;

public enum RPS {
    ROCK(0),
    PAPER(1),
    SCISSORS(2);

    private int value;

    public int getValue() {
        return value;
    }

    RPS(int value) {
        this.value = value;
    }

    public static String getEmoji(int x) {
        for (RPS v : values()) if (v.getValue()==x) return Icon.valueOf(v.toString()).get();
        return null;
    }
}
