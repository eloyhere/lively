package structure;

import java.util.AbstractList;
import java.util.Collection;
import java.util.LinkedList;

public class Circle<E> extends LinkedList<E> {

    public Circle() {
    }

    public Circle(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public E get(int index) {
        int limited = ((index % this.size()) + this.size()) % this.size();
        return super.get(limited);
    }
}
