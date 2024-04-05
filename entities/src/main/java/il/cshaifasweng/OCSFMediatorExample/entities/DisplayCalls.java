package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class DisplayCalls implements Serializable{

private List<Emergency_call> calls;


public DisplayCalls(List<Emergency_call> calls) {
    this.calls = calls;
}

public List<Emergency_call> getCalls()
{
    return calls;

}
public void setCalls(List<Emergency_call> calls){
    this.calls = calls;
}


}