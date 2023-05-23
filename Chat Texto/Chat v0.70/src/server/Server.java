package server;

import common.Utils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static final String HOST = "127.0.0.1"; // IP servidor 
    public static final int PORT = 1000;    // Porta Servidor

    private ServerSocket servidor;
    private Map<String, ServerThread> clientes;

    public Server() {
        try {
            String requisicao;
                //Solicitacao de abertura client
            clientes = new HashMap<String, ServerThread>();
            servidor = new ServerSocket(PORT);
                // Mensagens iniciais
            System.out.println("Porta SERVIDOR " + PORT + " foi aberta com sucesso");
            System.out.println("SERVIDOR iniciado no Host: " + HOST + " com sucesso");
            System.out.println("Servidor, aguardando recebimento de mensagens...");

            while (true) {
                    //Socket cli accept
                Socket client = servidor.accept();
                requisicao = Utils.mensagemRecebida(client); // import ddo Util
                if (checkLogin(requisicao)) {
                        //Verificacao de client repetido
                    ServerThread escuta = new ServerThread(requisicao, client, this);
                    clientes.put(requisicao, escuta);
                    Utils.enviaMensagens(client, "SUCESS"); // Em caso de sucesso retorna a utils SUCESS
                    new Thread(escuta).start();
                }else {
                    Utils.enviaMensagens(client, "ERROR"); // Em caso de erro retorna a utils ERROR
                }
            }
        } catch (IOException ex) {
            System.err.println("[Erro: SERVIDOR] " + ex.getMessage());
        }
    }

    public Map<String, ServerThread> getClientes() {
        return clientes;
    }

    private boolean checkLogin(String request) {
        String[] splited = request.split(":");
            //Para evitar que usuarios entrem no chat com mesmos dados
        for (Map.Entry<String, ServerThread> p : clientes.entrySet()) {
            String[] parts = p.getKey().split(":");
            if (parts[0].toLowerCase().equals(splited[0].toLowerCase())) {
                return false;
            } else if ((parts[1] + parts[2]).toLowerCase().equals((splited[1] + splited[2]).toLowerCase())) {
                return false;
            }
        }
        return true;  //retorna positivo caso não haja semelhança alguma entre usuarios
    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}