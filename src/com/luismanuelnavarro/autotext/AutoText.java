package com.luismanuelnavarro.autotext;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * @author Ing. Luis Manuel Navarro Rangel.
 * @version 0.1
 * @since 05/11/2014
 */
public class AutoText {
    private List<String> elementos = new ArrayList<String>();
    private JTextComponent txTexto;
    private JPopupMenu pop;
    private boolean caseSensitive = false;

    private AutoText() {
        
    }

    /**
    Agrega la funcionalidad de autocompletar al componente de texto indicado
    @param txTexto El componente de texto (JTextComponent)
    */
    public AutoText(JTextComponent txTexto) {
        this.txTexto = txTexto;
        inicializar();
    }
    
    /**
    Agrega la funcionalidad de autocompletar al componente de texto indicado
    @param txTexto El componente de texto (JTextComponent)
    @param elementos Los elementos disponibles para autocompletar
    */
    public AutoText(JTextComponent txTexto, List<String> elementos) {
        this.txTexto = txTexto;
        this.elementos = elementos;
        inicializar();
    }
    
    private void inicializar(){
        pop = new JPopupMenu();
        pop.setBorderPainted(false);
        
        txTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txTextoKeyPressed(evt,txTexto,pop);
            }
        });
    }

    /**
    Obtiene la lista de elementos disponibles para autocompletar
    @return 
    */
    public List<String> getElementos() {
        return elementos;
    }

    /**
    Establece la lista de elementos disponibles para autocompletar
    @param elementos 
    */
    public void setElementos(List<String> elementos) {
        this.elementos = elementos;
    }
    
    /**
    Agrega un nuevo elemento a la lista de autocompletado
    @param element 
    */
    public void addElement(String element){
        elementos.add(element);
    }
    
    /**
    Quita el elemento a la lista de autocompletado
    @param element El elemento a eliminar
    */
    public void removeElement(String element){
        elementos.remove(element);
    }

    public JTextComponent getTxTexto() {
        return txTexto;
    }

    public void setTxTexto(JTextComponent txTexto) {
        this.txTexto = txTexto;
    }

    public JPopupMenu getPop() {
        return pop;
    }

    public void setPop(JPopupMenu pop) {
        this.pop = pop;
    }

    /**
    Indica si el autocompletado distingue entre mayúsculas y minúsculas
    @return
    */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
    Establece si el autocompletado distingue entre mayúsculas y minúsculas
    */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    private void poblarMenuPop(final JTextComponent txTexto, String criterio){
        
        ActionListener menuListener = new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent event) {
                txTexto.setText(event.getActionCommand());
          }
        };
        
        for (String op : elementos) {
            if(isCaseSensitive()){
                if(op.contains(criterio)){
                    JMenuItem item = new JMenuItem(op);
                    item.addActionListener(menuListener);
                    pop.add(item);
                }
            }else{
                if(op.toUpperCase().contains(criterio.toUpperCase())){
                    JMenuItem item = new JMenuItem(op);
                    item.addActionListener(menuListener);
                    pop.add(item);
                }
            }
        }
    }
    
    private void txTextoKeyPressed(KeyEvent evt, JTextComponent txTexto, JPopupMenu pop) {       
        if(pop.isVisible())pop.setVisible(false);
        pop.removeAll();
        
        //Se arman las opciones cuando la tecla presionada no es Enter ni Escape
        if(evt.getKeyCode() != 27 && evt.getKeyCode() != 10){

            String texto = txTexto.getText();
            poblarMenuPop(txTexto,texto);
            if(pop.getComponentCount()>0){
                pop.show(txTexto, 2, txTexto.getHeight());
            }

            //Si se escribió un caracter (aA0-zZ9) se establece el foco en el campo de texto
            if((evt.getKeyChar()>='0' && evt.getKeyChar()<='9') || (evt.getKeyChar()>='a' && evt.getKeyChar()<='z') || (evt.getKeyChar()>='A' && evt.getKeyChar()<='Z') 
                || evt.getKeyCode() == 8 || evt.getKeyCode() == 37 || evt.getKeyCode() == 39 || evt.getKeyCode() == 16 || evt.getKeyCode() == 32 || evt.getKeyCode() == 20 
                || evt.getKeyCode() == 36 || evt.getKeyCode() == 35 || evt.getKeyCode() == 127 || evt.getKeyCode() == 17 || evt.getKeyCode() == 18){
                txTexto.requestFocus();
            }

        }
    }    
    
}
