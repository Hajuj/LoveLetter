import java.util.LinkedList;
import java.util.List;

public interface Observer<T> {
    public abstract void update(T arg);
}
/*
abstract class Subject {
    private List<Observer> observers = new LinkedList<Observer>();

    protected void notifyObserver() {
        for (Observer o : observers)
            o.update();
    }

    public void register(Observer o) {
        observers.add(o);
    }

    public void remove(Observer o) {
        observers.remove(o);
    }
}

class Student extends Subject {
    private int id;
    private String name;
    private String birth;

    public Student(int id, String name, String birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }

    public void setId(int id) {
        this.id = id;
        notifyObserver();
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
        // there's change, notify observers
        notifyObserver();
    }

    public String getName() {
        return name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
        // there's change, notify observers
        notifyObserver();
    }

    public String getBirth() {
        return birth;
    }
}

class View1 implements Observer {
    private Student student;

    public View1(Student s) {
        this.student = s;
    }

    public void update() {
        System.out.println("View1:" + student.getId() + student.getName());
    }
}

class View2 implements Observer {
    private Student student;

    public View2(Student s) {
        this.student = s;
    }

    public void update() {
        System.out.println("View1:" + student.getId() + student.getBirth());
    }
}

class View3 {
    private Student student;

    public View3(Student s) {
        this.student = s;
    }

    public void changeStudent() {
        // change the student
        student.setId(2);
    }
}*/


