package src;

import java.net.InetAddress;

public class IP {
    public static String getIp() throws Exception {
        InetAddress IP = InetAddress.getLocalHost();
        return IP.getHostAddress();
    }    
}
