package Bot;

public class RockFactory implements GestureFactory{
    @Override
    public Gesture create() {
        return new Rock();
    }
}
