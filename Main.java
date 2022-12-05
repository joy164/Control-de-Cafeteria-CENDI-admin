import java.sql.Connection;

import javax.swing.JOptionPane;
import javax.swing.JFrame;

public class Main{
    public static void main(String[] args) {
        Conn conn = new Conn();
        Connection c = null;

        c = conn.initConexion();

        if(c != null){
            Formulario form1 = new Formulario();

            form1.setVisible(true);
            form1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            if(form1.editarPan.isShowing()){
                form1.campoBusqedaEdit.requestFocus();
            }
            if(form1.recargasPan.isShowing()){
                form1.campoBusquedaRecarga.requestFocus();
            }

        }else{
            JOptionPane.showMessageDialog(null, "Error al conectar con el dispositivo al inciar programa", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }


    }
}