package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;

public class UnknownUserClient extends AbstractClient {
    private static UnknownUserClient client = null;

    /**
     * Constructs the client.
     *
     * @param host the server's host name.
     * @param port the port number.
     */
    public UnknownUserClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected void handleMessageFromServer(Object msg) throws IOException {

    }
    public static UnknownUserClient getClient() {
        if (client == null) {
            System.out.println("client created");
            client = new UnknownUserClient("localhost", 3000);
        }
        return client;
    }
}
