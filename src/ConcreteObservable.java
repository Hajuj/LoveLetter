import java.util.HashSet;
import java.util.Set;

public class ConcreteObservable<T> implements Observable<T> {

    private Set<Observer<T>> observers = new HashSet<Observer<T>>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notify(T arg) {
        for (Observer<T> o : observers) {
            o.update(arg);
        }
    }

}
