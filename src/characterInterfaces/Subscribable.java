package characterInterfaces;

public interface Subscribable<T> {
	public void addSubscriber(Listener<T> sub);
	public void removeSubscriber(Listener<T> sub);
	public void notifySubscribers();
}
