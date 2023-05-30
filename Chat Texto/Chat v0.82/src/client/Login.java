package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.*;
import common.InitHUD;
import common.Utils;
import server.Server;

public class Login extends InitHUD {

    private JButton jbLoogin; // botao de login
    private JLabel inc, ver, title, por, user ; // labels visuais
    private JTextField jtUser, jtPort; // zona de texto com informações de login
    

    public Login() {
        super("Login");
    }

    protected void initComponents() {
        user = new JLabel("Usuario");
        inc = new JLabel("FAZ CHAT");
        por    = new JLabel("por");
        title = new JLabel("CONNECTTO");
        ver = new JLabel("Beta v0.82");
        jbLoogin = new JButton("Entrar");
        jtUser = new JTextField();
        jtPort = new JTextField();
    }

    @Override
    protected void configComponents() {
        //Jframe
        this.setLayout(null); 
        this.setMinimumSize(new Dimension(260, 260));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.decode("#D4D9D9"));
        //Titulos
        Font fonInc = inc.getFont();
            //Fonte Empresa 
        Font font1 = new Font("Arial", fonInc.getStyle(), fonInc.getSize());
        Font fontPor = new Font("Arial", fonInc.getStyle(), fonInc.getSize()-3);
        Font fontUsuario = new Font("Bahnschrift", fonInc.getStyle(), fonInc.getSize()+2);
            //Fonte Titulo
        Font fontLogo = title.getFont();
        Font font2 = new Font("Unispace", fontLogo.getStyle(), fontLogo.getSize() + 15);
            //Fonte Infos inferiores e titulo.
        Font fontInfo = new Font("Arial", fonInc.getStyle(), fonInc.getSize());
        //Background
            //Nome empresa 
        inc.setBounds(140,43,80,18);
        inc.setFont(font1);
        por.setBounds(122,45,40,10);
        por.setFont(fontPor);
            // Titulo do Chat
        title.setBounds(45,20,200,28);
        title.setFont(font2);
            // Botão de login exibido em tela
        jbLoogin.setBounds(160,150,70,24);
            //Botão e caixa de usuario
        user.setForeground(Color.DARK_GRAY);
        user.setFont(fontUsuario);
        user.setBounds(15,75,75,50);
        jtUser.setBounds(15,110,220,30);
            //Label de versão, Updatees da versão.
        ver.setBounds(180,200,70,24);
        ver.setFont(fontInfo);
        ver.setForeground(Color.DARK_GRAY);    
    }

    @Override
    protected void insertComponents() {
        this.add(title);
        this.add(inc);
        this.add(por);
        this.add(user);
        this.add(ver);
        this.add(jbLoogin);
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