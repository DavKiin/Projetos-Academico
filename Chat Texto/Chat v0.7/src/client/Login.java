package client;

import common.InitHUD;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import server.Server;


public class Login extends InitHUD {

    private JButton jbLoogin; // botao de login
    private JLabel jlUser, jlPort, title; // labels visuais
    private JTextField jtUser, jtPort; // zona de texto com infos

    public Login() {
        super("Login");
    }

    protected void initComponents() {
        title = new JLabel("CHAT", SwingConstants.CENTER);
        jbLoogin = new JButton("Entrar");
        jlUser = new JLabel("Apelido", SwingConstants.CENTER);
        jlPort = new JLabel("Porta", SwingConstants.CENTER);
        jtUser = new JTextField();
        jtPort = new JTextField();
    }

    @Override
    protected void configComponents() {
        this.setLayout(null); 
        this.setMinimumSize(new Dimension(400, 450));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);
        
            //titulo grafico exibido em tela
        title.setBounds(15,10,350,200);
        ImageIcon logo = new ImageIcon("../assets/Logo_Chat.png");
        title.setIcon(new ImageIcon(logo.getImage().getScaledInstance(375, 100, Image.SCALE_SMOOTH)));
            // Botão de login exibido em tela
        jbLoogin.setBounds(15,320,350,40);
            //Botão e caixa de usuario
        jlUser.setBounds(15,220,100,40);
        jlUser.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            //Botão e caixa da porta de acesso
        //jlPort.setBounds(15,270,100,40);
        //jlPort.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    
        jtUser.setBounds(120,220,250,40);
        //jtPort.setBounds(120,270,250,40);
    }

    @Override
    protected void insertComponents() {
        this.add(title);
        this.add(jbLoogin);
        this.add(jlPort);
        this.add(jlUser);
        this.add(jtPort);
        this.add(jtUser);
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
                
                
                jtUser.setText("");
                jtPort.setText("");

                    //iniciando socket client
                coneS = new Socket(Server.HOST, Server.PORT);
                String conecInfo = nick + ":" + coneS.getLocalAddress().getHostAddress() + ":" + port;
                Utils.enviaMensagens(coneS, conecInfo); // envia a Utils socket e informações de conexao
                if (Utils.mensagemRecebida(coneS).toLowerCase().equals("sucess")) {
                    new Home(coneS, conecInfo); // cria nova tela de home
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Apelido ou Porta Utilizada"); // caso o nick ou porta esteja utilizado
                }
            } catch (IOException ex) {
                System.err.println("[ERROR:LOGIN.IOException] " + ex.getMessage());
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