import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

// A thread class for crawling urls
public class CrawlerTask implements Runnable {
	// Reference to the pool of urls to work with
	UrlPool pool;
	// HTML href tag
	static final String HREF_TAG = "<a href=\"http";
	
	// Constructor
	public CrawlerTask(UrlPool pool) {
		this.pool = pool;
	}
	
	// Get a url from the pool, scrape it for new urls, and add them to the
	// pool. 
	@Override
	public void run() {
		while (true) {
			UrlDepthPair pair = pool.getNextPair();
			int currDepth = pair.getDepth();
			try {
				Socket sock = new Socket();
				sock.connect(new InetSocketAddress(pair.getHost(), 80), 3000);
				sock.setSoTimeout(3000);
				System.out.println("Connected to " + pair.getURLString());
				PrintWriter out =
				        new PrintWriter(sock.getOutputStream(), true);
			    BufferedReader in =
			        new BufferedReader(
			            new InputStreamReader(sock.getInputStream()));
			    // Send the HTTP request
			    out.println("GET " + pair.getPath() + " HTTP/1.1");
			    out.println("Host: " + pair.getHost());
			    out.println("Connection: close");
			    out.println();
			    out.flush();

	    		// Extract links from the page and add them back to the
		    	// pool of urls
			    String line;
			    int lineLength;
			    int shiftIdx;
			    while ((line = in.readLine()) != null) {
			    	// Check if the current line has a link
				    boolean foundFullLink = false;
			    	int idx = line.indexOf(HREF_TAG);
			    	if (idx > 0) {
			    		// Extract the link
			    		StringBuilder sb = new StringBuilder();
			    		shiftIdx = idx + 9;
			    		char c = line.charAt(shiftIdx);
			    		lineLength = line.length();
			    		while (c != '"' && shiftIdx < lineLength - 1) {
			    			sb.append(c);
			    			shiftIdx++;
			    			c = line.charAt(shiftIdx);
			    			if (c == '"') {
			    				foundFullLink = true;
			    			}
			    		}
			    		// Create a new pair for this link
		    			String newUrl = sb.toString();
		    			if (foundFullLink) {
		    				UrlDepthPair newPair = 
				    				new UrlDepthPair(newUrl, currDepth + 1);
				    		// Add the new pair to the pool
				    		pool.addPair(newPair);
		    			}
			    	}
			    }

			    // Close the socket.
			    sock.close();
			}
			catch (IOException e) {
			}

		}
	}
}
