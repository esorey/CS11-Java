import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;

public class Crawler {
	// HTML href tag
	static final String HREF_TAG = "<a href=\"http";
	// List of all sites seen so far.
	static LinkedList<URLDepthPair> allSitesSeen = 
			new LinkedList<URLDepthPair>();
	// List of sites not yet processed
	static LinkedList<URLDepthPair> toVisit = new LinkedList<URLDepthPair>();
	
	/**
	 * This function crawls the web, starting at the given source and 
	 * exploring to a depth of maxDepth.
	 * @param startURL - starting URL
	 * @param maxDepth - maximum crawl depth
	 * @throws MalformedURLException - if a URL is malformed
	 */
	public static void crawl(String startURL, int maxDepth)
			throws MalformedURLException {
		
		URLDepthPair urlPair = new URLDepthPair(startURL, 0);
		toVisit.add(urlPair);
		int depth;
		HashSet<String> seenURLs = new HashSet<String>();
		seenURLs.add(startURL);
		
		// Continue until all pages within maxDepth links of the source
		// are crawled
		while (!toVisit.isEmpty()) {
			URLDepthPair currPair = toVisit.removeFirst();
			depth = currPair.getDepth();
			// Set up socket connection
			try {
				Socket sock = new Socket();
				sock.connect(new InetSocketAddress(currPair.getHost(), 80), 3000);
				sock.setSoTimeout(3000);
				System.out.println("Connected to " + currPair.getURLString());
				PrintWriter out =
				        new PrintWriter(sock.getOutputStream(), true);
			    BufferedReader in =
			        new BufferedReader(
			            new InputStreamReader(sock.getInputStream()));
			    // Send the HTTP request
			    out.println("GET " + currPair.getPath() + " HTTP/1.1");
			    out.println("Host: " + currPair.getHost());
			    out.println("Connection: close");
			    out.println();
			    out.flush();

			 // Extract links from the pages
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
			    			if (foundFullLink && depth < maxDepth && 
			    					!seenURLs.contains(newUrl)) {
			    				URLDepthPair newPair = 
					    				new URLDepthPair(newUrl, depth + 1);
					    		// Add the new pair to the pool
					    		toVisit.add(newPair);
					    		seenURLs.add(newUrl);
			    			}
				    	}
			    	}	

			    // Mark this page as seen and close the socket.
			    sock.close();
			    allSitesSeen.add(currPair);
			}
			catch (IOException e) {
			}
		}
		// Print out all visited pages.
		for (URLDepthPair pair : allSitesSeen) {
			System.out.println(pair.toString());
		}
	}
	
	/**
	 * This function crawls the web, starting from the specified web page,
	 * to the specified depth. args[0] is the start page, arg[1] is the max
	 * depth.
	 * @param args
	 * @throws MalformedURLException
	 */
	public static void main(String[] args) throws MalformedURLException {
		if (args.length != 2) {
			System.out.println("usage: java Crawler <URL> <maximum_depth>");
			return;
		}
		String startURL = args[0];
		int maxDepth = Integer.parseInt(args[1]);
		crawl(startURL, maxDepth);	
	}
}
