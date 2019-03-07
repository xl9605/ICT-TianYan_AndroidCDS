package ac.ict.humanmotion.abracadabra.Bean;

public class Info {
    private int cmd;
    private String device_code;
    private String time;
    public Info(int cmd,String device_code,String time){
        this.cmd=cmd;
        this.device_code=device_code;
        this.time=time;
    }
}
