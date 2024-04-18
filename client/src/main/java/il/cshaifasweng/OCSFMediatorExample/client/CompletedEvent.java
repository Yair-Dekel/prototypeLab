package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.DisplayDataMessage;

public class CompletedEvent {


        private DisplayDataMessage dis;



        public CompletedEvent(DisplayDataMessage dis) {
            this.dis = dis;
        }

        public DisplayDataMessage getDis() {
            return this.dis;
        }

    }



