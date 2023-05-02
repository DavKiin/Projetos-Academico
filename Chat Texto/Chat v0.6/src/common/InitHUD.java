package common;

import javax.swing.JFrame;


    //Classe abstrata para iniciar componentes 
public abstract class InitHUD extends JFrame{
    public InitHUD(String title){
        super(title);
        initComponents();
        configComponents();
        insertComponents();
        insertActions();
        start();
    }
    protected abstract void initComponents();
    protected abstract void configComponents();
    protected abstract void insertComponents();
    protected abstract void insertActions();
    protected abstract void start();
}