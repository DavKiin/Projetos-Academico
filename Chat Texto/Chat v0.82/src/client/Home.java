package client;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

import common.ArchiveUtils;
import common.InitHUD;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;


public class Home extends InitHUD {
        //Front
    private JButton getConection, startChat;
    private JList lista;
    private JScrollPane scroll;
    private JLabel title, ver, titleAtt; // labels visuais
    private JPanel leftPanel, rightPanel, line, userText; //Paineis
    private JTextArea attArea; //Area de Texto das atualizações  
        //Back
    private ServerSocket servidor;
    private final Socket conexao;
    private final String conecInfo;
    private ArrayList<String> userConectados;
    private ArrayList<String> chatAberto;
    private Map<String, ClientThread> cliChats;

    

    public Home(Socket connection, String conecInfo) {
            //titulo
        super("Home");
        title.setText(" < Usuário : " + conecInfo.split(":")[0] + " >"); // Mostra o nome do usuario 
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
                //Run TimerTaks
            @Override
            public void run() {
                Utils.enviaMensagens(conexao, "GET_CONNECTED_USERS");
            String resp = Utils.mensagemRecebida(conexao);
            lista.removeAll();
            userConectados.clear();
            for (String user : resp.split(";")) {
                if (!user.equals(conecInfo)) {
                int delimiter = user.indexOf(':'); //Delimitador
                String listUser = user.substring(0, delimiter); //Exclusor
                //userConectados.add(listUser); //User na lista.
                userConectados.add(user);
                }

        }
        lista.setListData(userConectados.toArray());
            };
        };
    
        timer.scheduleAtFixedRate(task, 0, (5000)); //Timer de atualização.
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        titleAtt = new JLabel("ATUALIZAÇÕES");
        attArea = new JTextArea(
            ArchiveUtils.ReadArchive(
                "D:/Projetos/Projetos Academico/Chat Texto/Chat v0.82/lib/update/Update.txt"
                ));
        getConection = new JButton("REFRESH");
        lista = new JList();
        scroll = new JScrollPane(lista);
        startChat = new JButton("Abrir Conversa");
        leftPanel = new JPanel();   
        rightPanel = new JPanel();
        line = new JPanel();
        ver = new JLabel("Beta v0.82");
        userText = new JPanel();
    }

    @Override
    protected void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(700, 450));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.white);
        //Fontes
        Font font1 = lista.getFont();
            //Interface [ pendente ]
            //Botões [ pendente ]
            //Lista Users
        Font fontLista = new Font("Arial Black", font1.getStyle(), font1.getSize() + 2);
            //Atualizações
        Font fontPainel = new Font("Bahnschrift", font1.getStyle(), font1.getSize());
        Font fontAtt = new Font("Bahnschrift", font1.getStyle(), font1.getSize() + 10);
        //Background
            //Fundo Usuario
        userText.setBounds(80,10,160,40);
        userText.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            //Titulo do Usuario
        title.setBounds(84,10,160,40);
            // Painel Esquerda 
        leftPanel.setBounds(0,0,250,450);
        leftPanel.setBackground(Color.decode("#D4D9D9"));
            // Painel Direita
        rightPanel.setBounds(252,0,450,450);
        rightPanel.setBackground(Color.decode("#F7FCFC"));
            //Line
        line.setBounds(250,0,2,450);
        line.setBackground(Color.GRAY);
            //Versão 
        ver.setBounds(620,388,70,24);
            //Botão de conexão
        getConection.setBounds(10,345,230,40);
        getConection.setFocusable(false);
            //Começar conversa
        startChat.setBounds(10,295,230,40);
        startChat.setFocusable(false);
            //Lista 
        lista.setBorder(BorderFactory.createTitledBorder("Usuarios Online"));
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(fontLista);
        scroll.setBounds(10,65,230,220);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);
            //Titulo Atualizações
        titleAtt.setFont(fontAtt);
        titleAtt.setBounds(380, 24, 180,24);
            //Area de atualização.
        attArea.setBounds(264,50,410,320);
        attArea.setEditable(false);
        attArea.setLineWrap(true);
        attArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        attArea.setFont(fontPainel);
    }

    @Override
    protected void insertComponents() {
        this.add(title);
        this.add(userText);
        this.add(ver);
        this.add(getConection);
        this.add(scroll);
        this.add(startChat);
        this.add(attArea);
        this.add(titleAtt);
        this.add(leftPanel);
        this.add(rightPanel); 
        this.add(line);
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
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

            //pega o usuario conectado
        getConection.addActionListener(event -> {getConnectedUsers();
        System.out.println("[CLIENT:HOME.getConnectionUsers] Solicitada Atualização manual. ");
        });
             // realiza a abertura do chat
        startChat.addActionListener(event -> openChat());            
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
            //Refresh Loop
        for (String user : resp.split(";")) {
            if (!user.equals(conecInfo)) { 
                int delimiter = user.indexOf(':'); //Delimitador
                String listUser = user.substring(0, delimiter); //Exclusor
                //userConectados.add(listUser); //User na lista.
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
                    System.err.println("[ERRO:HOME.startChat]: Chat não pode ser inicializado." + ex.getMessage());
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

//Metods 
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