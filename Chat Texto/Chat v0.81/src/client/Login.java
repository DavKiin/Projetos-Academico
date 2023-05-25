package client;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import common.ArchiveUtils;
import common.InitHUD;
import common.Utils;
import server.Server;

public class Login extends InitHUD {

    private JButton jbLoogin; // botao de login
    private JLabel title, att, titleAtt, attFazChat; // labels visuais
    private JTextField jtUser, jtPort; // zona de texto com informações de login
    private JTextArea attArea; //Area de Texto das atualizações
    private JPanel leftPanel, rightPanel, line; //Paineis

    public Login() {
        super("Login");
    }

    protected void initComponents() {
        title = new JLabel("FAZ CHAT");
        titleAtt = new JLabel("ATUALIZAÇÕES");
        att = new JLabel("Beta v0.81");
        attFazChat = new JLabel("Faz Chat Ltda. 2023");
        jbLoogin = new JButton("Entrar");
        jtUser = new JTextField();
        jtPort = new JTextField();
        attArea = new JTextArea(ArchiveUtils.ReadArchive("D:/Projetos/Projetos Academico/Chat Texto/Chat v0.81/lib/update/Update.txt"));
        leftPanel = new JPanel();   
        rightPanel = new JPanel();
        line = new JPanel();

    }

    @Override
    protected void configComponents() {
        //Jframe
        this.setLayout(null); 
        this.setMinimumSize(new Dimension(700, 450));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        //Background
            // Painel Esquerda 
        leftPanel.setBounds(0,0,250,450);
        leftPanel.setBackground(Color.decode("#D4D9D9"));
            // Painel Direita
        rightPanel.setBounds(252,0,450,450);
        rightPanel.setBackground(Color.decode("#F7FCFC"));
            //Line
        line.setBounds(250,0,2,450);
        line.setBackground(Color.GRAY);
        //Titulos
            //titulo grafico exibido em tela
        title.setBounds(20,220,70,24);
            //Titulo Atualizações
        titleAtt.setBounds(426, 40, 100,24);
            // Botão de login exibido em tela
        jbLoogin.setBounds(160,290,70,24);
            //Botão e caixa de usuario
        jtUser.setBounds(15,250,220,30);
            //Label de versão, Updatees da versão.
        att.setBounds(620,390,70,24);
        attArea.setBounds(264,70,410,320);
        attArea.setEditable(false);
        attArea.setLineWrap(true);
        attArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        attFazChat.setBounds(260, 390, 150,24);   
    }

    @Override
    protected void insertComponents() {
        this.add(titleAtt);
        this.add(attFazChat);
        this.add(title);
        this.add(att);
        this.add(jbLoogin);
        this.add(jtPort);
        this.add(jtUser);
        this.add(attArea); 
        this.add(leftPanel);
        this.add(rightPanel); 
        this.add(line);
    }

    @Override
    protected void insertActions() {
            //Inserindo ação de login pos pressionamento do botão
        jbLoogin.addActionListener(event -> {
            Socket coneS;
            //Gerador Randomico de porta client
            int randomPort = ThreadLocalRandom.current().nextInt(Server.PORT, 65535) - Server.PORT; //randomgenerator
            final String random = Integer.toString(randomPort); //String para JTport
            //Definição de porta ao campo
            jtPort.setText(random);
            System.out.println("[CLIENT:LOGIN.jtPort] Porta Randomica Cliente iniciada " + random);

            try {
                String nick = jtUser.getText(); // nickname do usuario
                int port = Integer.parseInt(jtPort.getText()); // porta pertencente ao usuario
                    //Jtext de inserção de usuario
                jtUser.setText("");
                    //iniciando socket client
                coneS = new Socket(Server.HOST, Server.PORT);
                String conecInfo = nick + ":" + coneS.getLocalAddress().getHostAddress() + ":" + port;
                Utils.enviaMensagens(coneS, conecInfo); // envia a Utils socket e informações de conexao
                if (Utils.mensagemRecebida(coneS).toLowerCase().equals("sucess")) {
                    new Home(coneS, conecInfo); // cria nova tela de home
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Porta Utilizada"); // caso o nick ou porta esteja utilizado
                }
            } catch (IOException ex) {
                System.err.println("[ERROR:LOGIN.IOException]: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Erro ao conectar ao servidor.");
            }
            
        });
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }
    public static void main(String[] args) {
        Login login = new Login();
    }
}