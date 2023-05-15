package client;

import common.Utils;
import java.io.IOException;
import java.net.Socket;

public class ClientThread implements Runnable {

    private boolean running;
    private Socket socket;
    private Home home;
    private boolean chatOpen;
    private String conecInfo;
    private Conversa chat;

    public ClientThread(Home home, Socket conexao) {
        chatOpen = false;
        this.home = home;
        running = false;
        this.socket = conexao;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isChatOpen() {
        return chatOpen;
    }

    public void setChatOpen(boolean chatOpen) {
        this.chatOpen = chatOpen;
    }

    public Conversa getChat() {
        return chat;
    }

    public void setChat(Conversa chat) {
        this.chat = chat;
    }

    @Override
    public void run() {
        running = true; 
        String mensagem;
        while (running) {
                // enquanto running for true
            mensagem = Utils.mensagemRecebida(socket); 
                //caso o chat fechado
            if (mensagem == null || mensagem.equals("CHAT_CLOSE")) {
                if (chatOpen) {
                    home.getOpenChats().remove(conecInfo);
                    home.getCliChats().remove(conecInfo);
                    chatOpen = false;
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        System.err.println("[ClientThread:RUN] -> " + ex.getMessage());
                    }
                    chat.dispose();
                }
                running = false;
            } else {
                    //ou chat seja aberto
                String[] fields = mensagem.split(";");
                if (fields.length > 1) {
                    if (fields[0].equals("OPEN_CHAT")) {
                        String[] splited = fields[1].split(":");
                        conecInfo = fields[1];
                        if (!chatOpen) {
                            home.getOpenChats().add(conecInfo);
                            home.getCliChats().put(conecInfo, this);
                            chatOpen = true;
                            chat = new Conversa(home, socket, conecInfo, home.getConecInfo().split(":")[0]);
                        }
                    } else if (fields[0].equals("MESSAGE")) {
                        chat.atualizaChat(fields[1]);
                    }
                }
            }
            System.out.println("Mensagem: " + mensagem);
        }
    }
}