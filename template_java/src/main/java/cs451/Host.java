package cs451;


import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Host {

    private static final String IP_START_REGEX = "/";
    private int id;
    private String ip;
    private int port = -1;
    private List<Host> hosts;
    private int numMessages = -1;
    private PerfectLink link;
    public List<Message> delivered = new ArrayList<>();


    public boolean init(List<Host> hosts, int numMessages) {
        this.hosts = new ArrayList<>(hosts);
        this.hosts.remove(this);
        this.numMessages = numMessages;
        this.link = new PerfectLink(port, ip);


        return true;
    }

    public boolean populate(String idString, String ipString, String portString) {
        try {
            id = Integer.parseInt(idString);

            String ipTest = InetAddress.getByName(ipString).toString();
            if (ipTest.startsWith(IP_START_REGEX)) {
                ip = ipTest.substring(1);
            } else {
                ip = InetAddress.getByName(ipTest.split(IP_START_REGEX)[0]).getHostAddress();
            }

            port = Integer.parseInt(portString);
            if (port <= 0) {
                System.err.println("Port in the hosts file must be a positive number!");
                return false;
            }


        } catch (NumberFormatException e) {
            if (port == -1) {
                System.err.println("Id in the hosts file must be a number!");
            } else {
                System.err.println("Port in the hosts file must be a number!");
            }
            return false;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return true;
    }


    public void start()  {
        for(int i = 1 ; i <= numMessages; i ++) {
            for (Host h : hosts) {
                link.send(String.valueOf(i), h.getPort(), h.getIp());
            }
        }

    }



    public int getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

}
