package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LogInOutMessage implements Serializable {
    private Registered_user user;
    private String loginORout;

    public LogInOutMessage(Registered_user user, String loginORout) {
        this.user = user;
        this.loginORout = loginORout;
    }

    public Registered_user getUser() {
        return user;
    }

    public void setUser(Registered_user user) {
        this.user = user;
    }

    public String getLoginORout() {
        return loginORout;
    }

    public void setLoginORout(String loginORout) {
        this.loginORout = loginORout;
    }
}
