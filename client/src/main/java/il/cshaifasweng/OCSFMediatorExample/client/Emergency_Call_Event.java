package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayCalls;
import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class Emergency_Call_Event {
    private DisplayCalls dis;



    public Emergency_Call_Event(DisplayCalls dis) {
        this.dis = dis;
    }

    public DisplayCalls getCalls() {
        return this.dis;
    }
}
