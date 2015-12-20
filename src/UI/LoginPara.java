package src.UI;

public class LoginPara {
    private Boolean flag;
    private String ip;
    private int portNum;

    public LoginPara() {
        flag = true;
        ip = "";
        portNum = 0;
    }

    public void setFlag(Boolean flag_) { flag = flag_; }
    public Boolean getFlag() { return flag; }

    public void setIp(String ip_) { ip = ip_; }
    public String getIp() { return ip; }

    public void setPortNum(int portNum_) { portNum = portNum_; }
    public int getPortNum() { return portNum; }
}
