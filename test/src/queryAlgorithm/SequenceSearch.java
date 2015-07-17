package queryAlgorithm;

import java.util.Collection;
import java.util.Iterator;

public class SequenceSearch<T> {
	public boolean sequenceSearch(final Collection<T> collection, final T t) {
		boolean isExist = false;
		for (T items : collection) {
			if (items != null && t.equals(items)) {
				isExist = true;
				break;
			}
		}
		return isExist;
	}

	public boolean sequenceSearch(final Iterable<T> collection, final T t) {
		boolean isExist = false;
		Iterator<T> i = collection.iterator();
		while (!isExist && i.hasNext()) {
			T items = i.next();
			isExist = items != null && t.equals(items);

		}
		return isExist;
	}
}
