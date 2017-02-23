import java.util.HashSet;
import java.util.LinkedList;

public class UrlPool {
	// List of pending urls to be crawled
	LinkedList<UrlDepthPair> urlsToCrawl;
	// List of all the urls we've seen
	LinkedList<UrlDepthPair> urlsSeen;
	// Maximum crawl depth
	int maxDepth;
	// Count of waiting threads
	int waitCount;
	// Set of all seen URLs
	HashSet<String> urlsSeenSet;
	
	// Constructor
	public UrlPool(int maxDepth) {
		this.maxDepth = maxDepth;
		urlsToCrawl = new LinkedList<UrlDepthPair>();
		urlsSeen = new LinkedList<UrlDepthPair>();
		waitCount = 0;
		urlsSeenSet = new HashSet<String>();
	}
	
	// Get the next UrlDepthPair to crawl
	public synchronized UrlDepthPair getNextPair() {
		// Suspend this thread until a pair is available
		while (urlsToCrawl.size() == 0) {
			try {
				waitCount++;
				wait();
				waitCount--;
			} catch (InterruptedException e) {
				System.out.println("Caught unexpected " +
						 "InterruptedException, ignoring...");
			}
		}
		UrlDepthPair nextPair = urlsToCrawl.removeFirst();
		return nextPair;
	}

	// Add a UrlDepthPair to the pool. First check if we've seen this url before.
	// If not, always add it to the seen list, and add
	// to the pending list if its depth is not too deep.
	public synchronized void addPair(UrlDepthPair pair) {
		if (urlsSeenSet.contains(pair.getURLString())) {
			return;
		}
		urlsSeen.add(pair);
		if (pair.getDepth() < maxDepth) {
			urlsToCrawl.add(pair);
			// Notify any suspended threads that a pair has been added
			notify();
		}
		// Mark this url as seen
		urlsSeenSet.add(pair.getURLString());
	}
	
	// Get the number of waiting threads
	public synchronized int getWaitCount() {
		return waitCount;
	}
	
	// Get all the urls seen
	public LinkedList<UrlDepthPair> getSeenUrls() {
		return urlsSeen;
	}
	
}
