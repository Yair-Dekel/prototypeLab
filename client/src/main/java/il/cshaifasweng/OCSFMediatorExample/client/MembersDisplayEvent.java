package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class MembersDisplayEvent {
    private DisplayDataMessage dis;


    public MembersDisplayEvent(DisplayDataMessage dis) {
        this.dis = dis;
    }

    public DisplayDataMessage getDis() {
        return dis;
    }
}



