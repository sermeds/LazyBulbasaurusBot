package Bot;

public class ScissorsFactory implements GestureFactory{
    @Override
    public Gesture create() {
        return new Scissors();
    }
}
