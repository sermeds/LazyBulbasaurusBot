package Bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomFactory implements GestureFactory{
    List<GestureFactory> factories = new ArrayList<GestureFactory>();
    Random r = new Random();

    public RandomFactory(List<GestureFactory> factories) {
        this.factories = factories;
    }

    @Override
    public Gesture create() {
        int ind = r.nextInt(factories.size());
        return factories.get(ind).create();
    }
}
