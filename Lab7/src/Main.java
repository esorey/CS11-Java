import java.net.MalformedURLException;
import java.util.LinkedList;

// Driver class for the multithreaded webcrawler
public class Main {

	public static void main(String[] args) throws MalformedURLException {
		if (args.length != 3) {
			System.out.println("usage: java Crawler <URL> <maximum_depth> <num_threads>");
			return;
		}
		// Parse command-line parameters
		String startURL = args[0];
		int maxDepth = Integer.parseInt(args[1]);
		int numThreads = Integer.parseInt(args[2]);
		
		// Set up our pool of urls and add the starting url to it
		UrlPool pool = new UrlPool(maxDepth);
		UrlDepthPair firstPair = new UrlDepthPair(startURL, 0);
		pool.addPair(firstPair);
		
		// Get our threads started
		for (int i = 0; i < numThreads; i++) {
			CrawlerTask c = new CrawlerTask(pool);
			Thread t = new Thread(c);
			t.start();
		}
		
		// Continue crawling until all threads are waiting on the url pool.
		// This implies that we've crawled all pages of depth less than or
		// equal to maxDepth.
		while (pool.getWaitCount() != numThreads) {
			try {
				Thread.sleep(100); // 0.1 second
			} catch (InterruptedException ie) {
				System.out.println("Caught unexpected " +
				"InterruptedException, ignoring...");
			}
		}
		// Print out all found urls
		LinkedList<UrlDepthPair> foundUrls = pool.getSeenUrls();
		for (UrlDepthPair pair : foundUrls) {
			System.out.println(pair.toString());
		}
		// Exit the program
		System.exit(0);
	}
}
