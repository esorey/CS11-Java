import java.net.MalformedURLException;
import java.net.URL;

public class URLDepthPair {
	// URL for this pair
	URL url;
	// Search depth for this pair
	int depth;
	// URL prefix
	public static final String URL_PREFIX = "http://";
	
	// Basic constructor
	public URLDepthPair(String url, int depth) throws MalformedURLException {
		this.url = new URL(url);
		this.depth = depth;
	}
	
	// Return a string representation of the pair
	public String toString(){
		String out = url + "\t" + depth;
		return out;
	}
	
	// Returns the hostname for the pair.
	public String getHost() {
		return url.getHost();
	}
	
	// Returns the path for the pair.
	public String getPath() {
			return url.getPath();
		}
	
	// Returns the search depth of the pair.
	public int getDepth() {
		return depth;
	}
	
	// Returns the url string of the pair.
		public String getURLString() {
			return url.toString();
		}
	
}
