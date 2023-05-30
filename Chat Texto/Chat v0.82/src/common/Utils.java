package common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Utils {
        
    public static boolean enviaMensagens(Socket sock, String message)
    {     
        try {
            ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
            output.flush();
            output.writeObject(message);
            return true;
        } catch (IOException ex) {
            System.err.println("[ERROR:Utils.enviouMensagem.IOExeception]: " + ex.getMessage());
        }
        return false;
    }
    
    public static String mensagemRecebida(Socket s)
    {
        String resp = null;
        try {
            ObjectInputStream input = new ObjectInputStream(s.getInputStream());
            resp = (String) input.readObject();
        } catch (IOException ex) {
            System.err.println("[ERROR:Utils.mensagemRecebida.IOExeception]" + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.err.println("[ERROR:Utils.mensagemRecebida.ClassNotFoundExeception]" + ex.getMessage());
        }
         return resp;
    }
}