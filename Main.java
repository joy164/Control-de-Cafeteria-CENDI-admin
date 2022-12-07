import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args) {
        Conn conn = new Conn();
        Connection c = null;

        c = conn.initConexion();
        Formulario form1 = new Formulario();

        form1.setVisible(true);
        form1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if(c != null){
            
            if(form1.editarPan.isShowing()){
                form1.campoBusqedaEdit.requestFocus();
            }
            if(form1.recargasPan.isShowing()){
                form1.campoBusquedaRecarga.requestFocus();
            }

        }else{
            JOptionPane.showMessageDialog(null, "Error al conectar con el dispositivo al inciar programa, revise configuracion y reinicie programa", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
            form1.opcionesPanel.setSelectedComponent(form1.configPan);
        }


    }
}