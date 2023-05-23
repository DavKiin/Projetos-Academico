package client;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import common.InitHUD;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;




public class Home extends InitHUD {

    private JLabel title;
    private ServerSocket servidor;
    private final Socket conexao;
    private final String conecInfo;
    private JButton getConection, startChat;
    private JList lista;
    private JScrollPane scroll;

    private ArrayList<String> userConectados;
    private ArrayList<String> chatAberto;
    private Map<String, ClientThread> cliChats;

    public Home(Socket connection, String conecInfo) {
        
        super("Home");
        title.setText("< Usuário : " + conecInfo.split(":")[0] + " >"); // Mostra o nome do usuario 
        this.conexao = connection;
        this.setTitle("Home - " + conecInfo.split(":")[0]); //informacao de conexao
        this.conecInfo = conecInfo;
            //iniciando arrays
        userConectados = new ArrayList<String>(); //Usuarios conectados no Servidor
        chatAberto = new ArrayList<String>(); //Registro de Conversa dos usuarios
        cliChats = new HashMap<String, ClientThread>();
            //inicia chatServer
        startServidor(this, Integer.parseInt(conecInfo.split(":")[2]));
    }

    public void UpdateList(){
        //Automação da atualização da lista de contatos
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                Utils.enviaMensagens(conexao, "GET_CONNECTED_USERS");
            String resp = Utils.mensagemRecebida(conexao);
            lista.removeAll();
            userConectados.clear();
            for (String user : resp.split(";")) {
                if (!user.equals(conecInfo)) {
                    userConectados.add(user);
                }

        }
        lista.setListData(userConectados.toArray());
            };
        };
    
        timer.scheduleAtFixedRate(task, 0, (1000)); //Timer de atualização.
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        getConection = new JButton("Atualizar contatos");
        lista = new JList();
        scroll = new JScrollPane(lista);
        startChat = new JButton("Abrir Conversa");
    }

    @Override
    protected void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(620,550));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.white);
            //Titulo 
        title.setBounds(10,10,370,40);
        title.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            //Botão de conexão
        getConection.setBounds(400,10,180,40);
        getConection.setFocusable(false);
            //Começar conversa
        startChat.setBounds(10,400,575,40);
        startChat.setFocusable(false);
            //Lista 
        lista.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scroll.setBounds(10,60,575,335);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
    }

    @Override
    protected void insertComponents() {
        this.add(title);
        this.add(getConection);
        this.add(scroll);
        this.add(startChat);
    }

    @Override
    protected void insertActions() {
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override // ao encerrar a conexao realizar fechamento da tela conjuntamente
            public void windowClosing(WindowEvent e) {
                System.out.println("Conexão encerrada...");
                Utils.enviaMensagens(conexao, "QUIT");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
            //pega o usuario conectado
        getConection.addActionListener(event -> getConnectedUsers());
        startChat.addActionListener(event -> openChat());   // realiza a abertura do chat
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }
        
        //retorna lista de contatos atualizada
    private void getConnectedUsers() {
        Utils.enviaMensagens(conexao, "GET_CONNECTED_USERS");
        String resp = Utils.mensagemRecebida(conexao);
        lista.removeAll();
        userConectados.clear();
        for (String user : resp.split(";")) {
            if (!user.equals(conecInfo)) {
                userConectados.add(user);
            }

        }
        lista.setListData(userConectados.toArray());
    }
        //abrir um chat 
    private void openChat() {
        int index = lista.getSelectedIndex();
            //Seleciona um chat a ser aberto, mensagens partilhadas para um user por vez
        if (index != -1) {
            String value = lista.getSelectedValue().toString();
            String[] splited = value.split(":");
            if (!chatAberto.contains(value)) {
                try {
                    Socket socket = new Socket(splited[1], Integer.parseInt(splited[2]));
                    Utils.enviaMensagens(socket, "OPEN_CHAT;" + conecInfo); // manda a mensagem para o outro lado da conversa abrir minha janela
                    ClientThread cli = new ClientThread(this, socket);
                    cli.setChat(new Conversa(this, socket, value, this.conecInfo.split(":")[0]));
                    cli.setChatOpen(true);
                    cliChats.put(value, cli);
                    chatAberto.add(value);
                    new Thread(cli).start();

                } catch (IOException ex) {
                }
            }

        }
    }

    private void startServidor(Home home, int port) {
        new Thread() {
            @Override
            public void run() {
                try {
                    servidor = new ServerSocket(port); //Inicia ClientThread pelo metodo this.port
                    UpdateList();
                    System.out.println("[CLIENT:HOME.servidor] Cliente iniciado na porta " + port + " ...");
                        // Enquanto cliente estiver rodando, cria conexao para rodar uma nova thread
                    while (true) {
                        Socket coneS = servidor.accept();
                        ClientThread cl = new ClientThread(home, coneS);
                        new Thread(cl).start();
                    }
                } catch (IOException ex) {
                    System.err.println("[ERROR:HOME.StartServidor] " + ex.getMessage());
                }
            }
        }.start();
    }
    
    

    public ArrayList<String> getOpenChats() {
        return chatAberto;
    }

    public String getConecInfo() {
        return conecInfo;
    }

    public Map<String, ClientThread> getCliChats() {
        return cliChats;
    }

}