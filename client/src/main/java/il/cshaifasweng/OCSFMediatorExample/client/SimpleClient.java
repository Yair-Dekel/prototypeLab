package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayTasksMassage;
import il.cshaifasweng.OCSFMediatorExample.entities.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.MessageOfStatus;
import il.cshaifasweng.OCSFMediatorExample.entities.Task;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}



	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("got into handleMessageFromServer ");

//		Message message = (Message) msg;
		if(msg instanceof MessageOfStatus) {
			MessageOfStatus message1 = (MessageOfStatus) msg;
			if (message1.getChangeStatus().equals("the change completed")) {
				EventBus.getDefault().post(new NewDetailsEvent(message1));
			}
		}
//		if(message1.getChangeStatus().equals("update submitters IDs")){
//			EventBus.getDefault().post(new UpdateMessageEvent(message));
//		}else if(message.getMessage().equals("client added successfully")){
//			EventBus.getDefault().post(new NewSubscriberEvent(message));
//		}else if(message.getMessage().equals("Error! we got an empty message")){
//			EventBus.getDefault().post(new ErrorEvent(message));
		else if (msg instanceof DisplayTasksMassage) {
			DisplayTasksMassage dis = (DisplayTasksMassage) msg;
			EventBus.getDefault().post(new TasksMessageEvent(dis));
			System.out.println("recognized massage as a list of tasks");
		}



//		else {
//			EventBus.getDefault().post(new MessageEvent(message));
//		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			System.out.println("Rinaaaa");
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

}