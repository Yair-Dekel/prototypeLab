package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.List;
import javafx.collections.ObservableList;


public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}



	@Override
	protected void handleMessageFromServer(Object msg) throws IOException {
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
			System.out.println("recognized massage as a list of tasks");
		}



	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}