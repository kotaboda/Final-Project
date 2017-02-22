package publisherSubscriberInterfaces;

public interface Subscribable<T> {
	
	public void addSubscriber(Listener<T> listener);
	public void notifySubscribers();

}
