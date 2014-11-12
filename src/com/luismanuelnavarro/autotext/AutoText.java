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
    
    /**
    Inicializa el menú emergente agregando el evento que responderá al escribir los caracteres
    */
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

    /**
    Obtiene el componente de texto sobre el cual está funcionando el autocompletado
    @return 
    */
    public JTextComponent getTxTexto() {
        return txTexto;
    }

    /**
    Establece el componente de texto con el cual se mostrará el menu de autocompletado
    @param txTexto 
    */
    private void setTxTexto(JTextComponent txTexto) {
        this.txTexto = txTexto;
    }

    /**
    Obtiene el objeto JPopMenu (menú emergente) que se muestra al encontrar coincidencias en el texto escrito en el componente
    @param pop 
    */
    public JPopupMenu getPop() {
        return pop;
    }

    /**
    Establece el objeto JPopMenu (menú emergente) que se mostrará al encontrar coincidencias en el texto escrito en el componente
    @param pop 
    */
    private void setPop(JPopupMenu pop) {
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
    
    /**
    Pobla el menu emergente con las opciones que coincidan entre aquellos elementos agregados previamente
    @param txTexto El componente de texto
    @param criterio El criterio a buscar
    */
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
    
    /**
    Genera el comportamiento del evento al presionar las teclas
    @param evt El evento
    @param txTexto El componente de texto
    @param pop El menú emergente
    */
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
            if((evt.getKeyChar()>='0' && evt.getKeyChar()<='9') //si es número
            || (evt.getKeyChar()>='a' && evt.getKeyChar()<='z') //si es caracter en minúsculas...
            || (evt.getKeyChar()>='A' && evt.getKeyChar()<='Z') //o caracter en mayúsculas
            || isFocusableKey(evt.getKeyCode())){//o algún elemento tipo Retroceso, flechas a los lados, Suprimir, Ctrl, Alt, Shift, etc
                txTexto.requestFocus();
            }

        }
    }    
    
    /**
    Indica si alguna de las teclas presionadas requiere que se mantenga el foco en el componente de texto
    @param keyCode El código del caracter
    @return true si debe mantenerse el foco en el componente de texto, false si no
    */
    private boolean isFocusableKey(int keyCode){
        int[] focusables = {8,16,37,32,20,39,36,35,18,127,17};
        for (int focusable : focusables) {
            if(keyCode==focusable)return true;
        }
        return false;
    }
    
}
