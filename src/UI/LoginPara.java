/*
 * @(#)LoginPara.java    1.0 2015/12/08
 */
package src.UI;

/**
 * store the flag that decides whether login has ended
 * and parameters that are needed after pressing login button
 *
 * @version 1.0 08 Dec 2015
 */
public class LoginPara {
    /* whether the login has done */
    private Boolean flag;
    /* input ip address */
    private String ip;
    /* input port number */
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
