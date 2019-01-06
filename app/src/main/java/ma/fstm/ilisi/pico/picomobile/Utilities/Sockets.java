package ma.fstm.ilisi.pico.picomobile.Utilities;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import ma.fstm.ilisi.pico.picomobile.Repository.PicoWebRestClient;

public class Sockets {

    private static Socket mysocket;

    public synchronized static Socket getInstance(){
        if(mysocket == null){
            try {
                mysocket = IO.socket("http://"+PicoWebRestClient.IPAddr+":" +
                        "9090?userType=CITIZEN_SOCKET_TYPE");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        return mysocket;
    }

}
