package publisherSubscriberInterfaces;

import character.Character;

public interface Subscribable<T> {
	
	public void addSubscriber(Listener<T> listener);
	public void notifySubscribers();
	public void removeSubscriber(Listener<Character> sub);

}
