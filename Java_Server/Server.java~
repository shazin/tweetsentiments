import java.net.*;
import java.util.*;
import java.io.*;
import redis.clients.jedis.Jedis;

/**
 * Simple Server to listen and process requests from Arduino over ESP8266
 *
 * @author shazin
*/

public class Server {
	public static void main(String... args) throws Exception {
		Jedis jedisClient = new Jedis("localhost");
		ServerSocket ss = new ServerSocket(8090);

		System.out.println("Server listening on 8090...");
		while(true) {
			Socket s = ss.accept();

			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String line = null;

			line = br.readLine();
			System.out.println(line);

			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			out.println(jedisClient.blpop("sentiments", "0").get(1));

			br.close();
			out.close();
			s.close();
		}
		
	}
}
