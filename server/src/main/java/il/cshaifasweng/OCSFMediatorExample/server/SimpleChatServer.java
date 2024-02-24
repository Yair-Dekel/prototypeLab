
package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.SimpleServer; // Add this import statement
import java.io.IOException;


public class SimpleChatServer
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        System.out.println("server is listening");
        server.listen();
    }
}
