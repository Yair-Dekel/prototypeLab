/*
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;



	private SimpleClient(String host, int port) {
		super(host, port);
	}



	@Override
	protected ManagerClient handleMessageFromServer(Object msg) throws IOException {
		System.out.println("got into handleMessageFromServer ");

//		Message message = (Message) msg;
		if(msg instanceof MessageOfStatus) {
			MessageOfStatus message1 = (MessageOfStatus) msg;
			if (message1.getChangeStatus().equals("the change completed")) {
				EventBus.getDefault().post(new NewDetailsEvent(message1));
				getClient().sendToServer("display tasks");
			}
		}

		else if (msg instanceof DisplayTasksMassage) {
			DisplayTasksMassage dis = (DisplayTasksMassage) msg;
			EventBus.getDefault().post(new TasksMessageEvent(dis));

		} else if (msg instanceof Message) {
			System.out.println("in client/handlefrom serverr /in message inst");
			Message message=(Message)msg;
			System.out.println(message.getMessage());
			EventBus.getDefault().post(new NewVerifiedInformationEvent(message));
		}
		else if (msg instanceof Message) {
			System.out.println("in client/handlefrom serverr /in message inst");
			Message message=(Message)msg;
			System.out.println(message.getMessage());
			EventBus.getDefault().post(new NewVerifiedInformationEvent(message));
		}

		return null;
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}*/
