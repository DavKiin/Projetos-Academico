package server;

import common.Utils;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



public class ServerThread implements Runnable {

    private boolean running;
    private Socket socket;
    private String apelido;
    private Server servidor;

    public ServerThread(String nickname, Socket socket, Server server) {
        this.servidor = server;
        running = false;
        this.socket = socket;
        this.apelido = nickname;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run(){
        running = true;
        String msg;

        while(running){
            msg = Utils.mensagemRecebida(socket);
                if (msg.toLowerCase().equals("quit")) {
                    servidor.getClientes().remove(apelido);
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            System.err.println("[ServerThread:RUN] " + ex.getMessage());
                        }
                        running = false; // retorna false para fechamento da execução 
                    } else if (msg.equals("GET_CONNECTED_USERS")) {
                            //processo de atualizacao contatos 
                        System.out.println("Solicitado atualização de contatos.");
                        String resp = "";
                        for (Map.Entry<String, ServerThread> par : servidor.getClientes().entrySet()) {
                            resp += (par.getKey() + ";");
                        }
                        Utils.enviaMensagens(socket, resp);
                    }
                    System.out.println("[Mensagem: SERVIDOR] " + msg);

                }
                
            };

        }

    /*@Override
    public void run() {
        running = true;
        String msg;

        while (running) {
                //Condicao de retorno do metodo run
            msg = Utils.mensagemRecebida(socket);
            if (msg.toLowerCase().equals("quit")) {
                servidor.getClientes().remove(apelido);
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.err.println("[ServerThread:RUN] " + ex.getMessage());
                }
                running = false; // retorna false para fechamento da execução 
            } else if (msg.equals("GET_CONNECTED_USERS")) {
                    //processo de atualizacao contatos 
                System.out.println("Solicitado atualização de contatos.");
                String resp = "";
                for (Map.Entry<String, ServerThread> par : servidor.getClientes().entrySet()) {
                    resp += (par.getKey() + ";");
                }
                Utils.enviaMensagens(socket, resp);
            }
            System.out.println("[Mensagem: SERVIDOR] " + msg);
        }
    }*/

