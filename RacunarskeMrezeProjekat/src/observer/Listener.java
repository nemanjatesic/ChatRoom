package observer;

public interface Listener {
	void addListener(Observer observer);
	void removeListener(Observer observer);
	void notify(Object o);
}
