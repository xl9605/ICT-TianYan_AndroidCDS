package ac.ict.humanmotion.abracadabra.Interface;

public class URLAPI {
    private String dataurl="http://10.41.0.128:23456/";//后台的服务端通信URL
    private String roboturl= "http://10.41.0.128:23456/";//与智能机器人通信的URL
    public String getDataurl() {
        return dataurl;
    }

    public String getRoboturl() {
        return roboturl;
    }
}
