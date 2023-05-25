package client;

import java.net.Socket;
import common.InitHUD;
import common.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import javax.swing.*;
import java.util.*;

public class Conversa extends InitHUD {

    private JLabel title;
    private JEditorPane mensagem;
    private JTextField areaTexto;
    private JButton enviar;
    private JPanel panel;
    private JScrollPane scroll;

    private ArrayList<String> historico;
    private Home home;
    private Socket coneS;
    private String conecInfo;

    public Conversa(Home home, Socket connection, String conecInfo, String title) {
        super("Chat " + title);
        this.home = home;
        this.conecInfo = conecInfo;
        historico = new ArrayList<String>();
        this.coneS = connection;
        this.title.setText(conecInfo.split(":")[0]);
        this.title.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        mensagem = new JEditorPane();
        scroll = new JScrollPane(mensagem);
        areaTexto = new JTextField();
        enviar = new JButton("Enviar");
        panel = new JPanel(new BorderLayout());
    }

    @Override
    protected void configComponents() {
        this.setMinimumSize(new Dimension(480, 720));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mensagem.setContentType("text/html");
        mensagem.setEditable(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        enviar.setSize(100, 40);
    }

    @Override
    protected void insertComponents() {
        this.add(title, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        panel.add(areaTexto, BorderLayout.CENTER);
        panel.add(enviar, BorderLayout.EAST);
    }

    @Override
    protected void insertActions() {
        areaTexto.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                    //ao apertar enter envia a mensagem
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    enviar();
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });
        enviar.addActionListener(event -> enviar());
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                    //ao fechar a tela de chat 
                Utils.enviaMensagens(coneS, "CHAT_CLOSE");
                home.getOpenChats().remove(conecInfo);
                home.getCliChats().get(conecInfo).setChatOpen(false);
                home.getCliChats().get(conecInfo).setRunning(false);
                home.getCliChats().remove(conecInfo);
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
    }
        //Ao realizar o envio de uma mensagem o chat atualiza o historico de mensagens enviadas 
    public void atualizaChat(String recebe) {
        historico.add(recebe);
        String msg = "";
        for (String s : historico) {
            msg += s;
        }
        mensagem.setText(msg);
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }
        //Forma de envio de mensagens formatação
    private void enviar() {
        DateFormat df = new SimpleDateFormat("hh:mm:ss"); // horario hora-minuto-segundo
        historico.add("<b>[" + df.format(new Date()) + "] Eu: </b><i>" + areaTexto.getText() + "</i><br>"); //mensagens aparecendo no historico de mensagens 
        Utils.enviaMensagens(coneS, "MESSAGE;<b>[" + df.format(new Date()) + "] " + home.getConecInfo().split(":")[0] + ": </b><i>" + areaTexto.getText() + "</i><br>"); // historico de mensagens para o outro user
        String msg = "";
        for (String s : historico) {
            msg += s;
        }
        mensagem.setText(msg);
        areaTexto.setText("");

    }

}