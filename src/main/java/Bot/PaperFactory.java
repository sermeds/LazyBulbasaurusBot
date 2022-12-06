package Bot;

public class PaperFactory implements GestureFactory{
    @Override
    public Gesture create() {
        return new Paper();
    }
}
