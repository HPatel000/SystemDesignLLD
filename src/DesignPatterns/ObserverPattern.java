package DesignPatterns;

import java.util.ArrayList;
import java.util.List;

/**
 * Observer
 * Behavioral
 * Allows a subject to notify its observers about state changes,
 *  without knowing who or what those observers are.
 * When you have a one-to-many dependency and need automatic updates
 *  (e.g., event listeners, notifications).
 */
public class ObserverPattern {
    public static void main(String[] args) {
        Observable observable = new ObservableImpl();
        observable.addObserver(new ObserverImpl());
        observable.addObserver(new ObserverImpl());
        observable.setData(5);
    }
}

interface Observable {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void setData(int data);
}

class ObservableImpl implements Observable {
    int data;
    List<Observer> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(Observer observer: observers) {
            observer.update(this.data);
        }
    }

    @Override
    public void setData(int data) {
        this.data = data;
        this.notifyObservers();;
    }
}

interface Observer {
    void update(int data);
}

class ObserverImpl implements Observer {
    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    Observable observable;
    int data;
    void ObservableImpl(Observable observable) {
        this.observable = observable;
    }


    @Override
    public void update(int data) {
        System.out.println(data);
        this.data = data;
    }
}
