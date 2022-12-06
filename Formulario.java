import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import javax.swing.JTabbedPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Formulario extends JFrame implements ActionListener{


    //elementos generales de ventana
    private JLabel imagen;
    public JPanel recargasPan, registrosPan, altasPan, editarPan;

    //elementos de ventana panel 1
    
    private  JLabel label1, id1, nom1, saldoA1, saldoN1;
    public JTextField campoBusquedaRecarga;
    private TitledBorder title, title8;
    private JPanel panel1, panel8;
    public JTabbedPane opcionesPanel;

    //elementos de ventana panel 2
    private JPanel panel2;
    private TitledBorder title2;
    private DefaultTableModel modelo;
    private JTable tablaRegistros;
    private JScrollPane scroll1;
    private JButton botonActualizar, botonExportar;

    //elementos de ventana panel 3
    private JLabel label3, label4, label5, label6;
    private JTextField campoNom, campoAP, campoAM, campoSaldo;
    private JPanel panel3, panel4;
    private TitledBorder title3, title4;
    private DefaultTableModel modelo2;
    private JTable tablaUsuarios;
    private JScrollPane scroll2;
    private JButton botonAlta, botonActualizar2;

    //elementos de ventana panel 4
    public JTextField campoBusqedaEdit, campoNomE, campoAPE, campoAME, campoSaldoE, campoIDE;
    private JLabel label7, label8, label9, label10, label11, label12;
    private JButton botonEliminar, botonEditar, botonGuardarE;
    private TitledBorder title5, title6, title7;
    private JPanel panel5, panel6, panel7;

    //base de datos
    Conn conn = new Conn();
    Connection c = null;
    
    public Formulario(){
        initComponentes();
        
        this.setSize(750, 500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Control de Cafeteria CC CENDI 1 Administrador");
        
    }

    public void initComponentes() {

        setLayout(null);
        setIconImage(new ImageIcon(getClass().getResource("/img/escudo.jpg")).getImage());

        imagen = new JLabel();
        imagen.setIcon(new ImageIcon(getClass().getResource("/img/logo.png")));
        imagen.setBounds(25, 5, 690, 83);
        this.add(imagen);

        //pestañas
        opcionesPanel = new JTabbedPane();
        opcionesPanel.setBounds(40, 100, 660, 350);

        //panel 1 de recargas
        recargasPan = new JPanel();
        recargasPan.setLayout(null);

        //elementos de panel 8
        panel8 = new JPanel();
        panel8.setBounds(10, 20, 635, 100);

        title8 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Busqueda");
        title8.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel8.setBorder(title8);
        panel8.setLayout(null);
        
        //elementos de panel 1
        label1 = new JLabel();
        label1.setBounds(10, 20, 200, 30);
        label1.setText("Ingrese codigo de barras");

        campoBusquedaRecarga = new JTextField();
        campoBusquedaRecarga.setBounds(10, 50, 200, 20);

        panel8.add(label1);
        panel8.add(campoBusquedaRecarga);

        campoBusquedaRecarga.addKeyListener(
            new KeyListener(){
                @Override
                public void keyPressed(KeyEvent e){
                    
                    if(e.getKeyCode() == KeyEvent.VK_ENTER){

                        Connection conexion;
                        int codigo = 0;
                        float saldoRecarga = 0;
                        PreparedStatement stmt;
                        
                        if(!campoBusquedaRecarga.getText().trim().isEmpty()){
                            try{
                                codigo = Integer.parseInt(campoBusquedaRecarga.getText());    
                            }catch(NumberFormatException errorConv1) {
                                JOptionPane.showMessageDialog(null, "dato ingresado no valido en el campo de codigo: " + errorConv1.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            
                            conexion = conn.initConexion();
                
                            if(conexion != null){
                                
                                try{
                                    stmt = conexion.prepareStatement("SELECT * FROM usuarios WHERE id_usuario= ?");
                                    stmt.setInt(1, codigo);
                                    ResultSet res = stmt.executeQuery();      
                                    
                                    if(res.next()){
                                        try {
                                            String saldo = JOptionPane.showInputDialog("ingrese el saldo a recargar");

                                            saldoRecarga = Float.parseFloat(saldo)  + Float.parseFloat(res.getString("saldo"));
                                        } catch (NumberFormatException errorConv2) {
                                            JOptionPane.showMessageDialog(null, "dato ingresado no valido en el campo de saldo: " + errorConv2.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                                            
                                        }
                
                                        stmt = conexion.prepareStatement("UPDATE usuarios set saldo = ? WHERE id_usuario = ?");
                                        stmt.setFloat(1, saldoRecarga);
                                        stmt.setInt(2, codigo);
                                        stmt.executeUpdate();
                
                                        id1.setText("ID: " + res.getString("id_usuario"));
                                        nom1.setText("Nombre: " + res.getString("nombre") + " " + res.getString("apellidoP"));
                                        saldoA1.setText("Saldo Actual: $" + res.getString("saldo"));
                                        saldoN1.setText("Saldo Nuevo: $" + saldoRecarga);

                                        stmt.close();
                                        conexion.close();
                
                                    }else{
                                        JOptionPane.showMessageDialog(null, "No se encontro al usuario con id: " + codigo, "Usuario no encontrado", JOptionPane.INFORMATION_MESSAGE);
                                        
                                        stmt.close();
                                        conexion.close();
                                    }
                
                                }catch(SQLException errorSQL){
                                    JOptionPane.showMessageDialog(null, "Error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Ingrese un ID para recargar", "Formulario incompleto", JOptionPane.INFORMATION_MESSAGE);
                        }
                        campoBusquedaRecarga.setText("");
                    }
                }

                @Override
                public void keyTyped(KeyEvent e) {             
                
                }

                @Override
                public void keyReleased(KeyEvent e) {
               
                }
            }
        );
        
        //panel de resultados panel 1
        panel1 = new JPanel();
        panel1.setBounds(10, 130, 635, 190);
        title = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Resultado");
        title.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel1.setBorder(title);
        panel1.setLayout(null);

        //subpanel de resultados para agrupamiento de datos de resultados
        id1 = new JLabel();
        id1.setBounds(20, 20, 200, 30);
        id1.setText("ID:");

        nom1 = new JLabel();
        nom1.setBounds(20, 50, 200, 30);
        nom1.setText("Nombre:");

        saldoA1 = new JLabel();
        saldoA1.setBounds(20, 80, 200, 30);
        saldoA1.setText("Saldo Actual: $");

        saldoN1 = new JLabel();
        saldoN1.setBounds(230, 80, 200, 30);
        saldoN1.setText("Saldo Nuevo: $");

        panel1.add(id1);
        panel1.add(nom1);
        panel1.add(saldoA1);
        panel1.add(saldoN1);

        recargasPan.add(panel8);
        recargasPan.add(panel1);
        
        //panel 2 de registros
        registrosPan = new JPanel();
        registrosPan.setLayout(null);

        //elementos de panel 2
        panel2 = new JPanel();
        panel2.setBounds(10, 10, 500, 300);
        title2 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Registros de cafeteria");
        title2.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel2.setBorder(title2);
        panel2.setLayout(null);

        modelo = new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        };
        modelo.addColumn("# REGISTRO");
        modelo.addColumn("ID de usuario");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha");

        incializarTabla();

        tablaRegistros = new JTable(modelo);
        scroll1 = new JScrollPane(tablaRegistros);
        tablaRegistros.setBounds(5, 20, 490, 280);
        scroll1.setBounds(5,20, 490, 275);

        panel2.add(scroll1);

        botonActualizar = new JButton("Actualizar");
        botonActualizar.setBounds(530, 25, 100, 30);
        botonActualizar.addActionListener(this);

        botonExportar = new JButton("Exportar");
        botonExportar.setBounds(530, 75, 100, 30);
        botonExportar.addActionListener(this);
        
        registrosPan.add(panel2);
        registrosPan.add(botonActualizar);
        registrosPan.add(botonExportar);

        //panel 3 de registros
        altasPan = new JPanel();
        altasPan.setLayout(null);

        //elementos de panel 4
        panel4 = new JPanel();
        panel4.setBounds(10, 20, 635, 150);

        title4 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Datos de usuario");
        title4.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel4.setBorder(title4);
        panel4.setLayout(null);

        label3 = new JLabel("Nombre(s)");
        label3.setBounds(20, 30, 100, 20);

        campoNom = new JTextField();
        campoNom.setBounds(20, 50, 150, 20);

        label4 = new JLabel("Apellido P.");
        label4.setBounds(210, 30, 100, 20);

        campoAP = new JTextField();
        campoAP.setBounds(210, 50, 150, 20);

        label5 = new JLabel("Apellido M.");
        label5.setBounds(20, 90, 100, 20);

        campoAM = new JTextField();
        campoAM.setBounds(20, 110, 150, 20);

        label6 = new JLabel("Saldo");
        label6.setBounds(210, 90, 100, 20);

        campoSaldo = new JTextField();
        campoSaldo.setBounds(210, 110, 150, 20);

        botonAlta = new JButton("Registrar");
        botonAlta.setBounds(450, 100, 100, 30);
        botonAlta.addActionListener(this);

        panel4.add(label3);
        panel4.add(campoNom);
        panel4.add(label4);
        panel4.add(campoAP);
        panel4.add(label5);
        panel4.add(campoAM);
        panel4.add(label6);
        panel4.add(campoSaldo);
        panel4.add(botonAlta);

        //elementos de panel 3
        panel3 = new JPanel();
        panel3.setBounds(10, 170, 500, 150);
        
        title3 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Registros de usuarios");
        title3.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel3.setBorder(title3);
        panel3.setLayout(null);

        modelo2 = new DefaultTableModel(){
            public boolean isCellEditable(int rowIndex,int columnIndex){return false;}
        };
        
        modelo2.addColumn("ID de usuario");
        modelo2.addColumn("Nombre");
        modelo2.addColumn("Apellido P.");
        modelo2.addColumn("Apellido M.");
        modelo2.addColumn("Saldo");

        incializarTabla2();

        tablaUsuarios = new JTable(modelo2);
        scroll2 = new JScrollPane(tablaUsuarios);
        tablaUsuarios.setBounds(5, 20, 490, 125);
        scroll2.setBounds(5,20, 490, 125);

        panel3.add(scroll2);


        botonActualizar2 = new JButton("Actualizar");
        botonActualizar2.setBounds(530, 200, 100, 30);
        botonActualizar2.addActionListener(this);

        altasPan.add(panel3);
        altasPan.add(panel4);
        altasPan.add(botonActualizar2);

        //panel 4 de edicion
        editarPan = new JPanel();
        editarPan.setLayout(null);

        //elementos de panel 5
        panel5 = new JPanel();
        panel5.setBounds(10, 20, 635, 100);

        title5 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Busqueda");
        title5.setTitlePosition(TitledBorder.ABOVE_TOP);
        panel5.setBorder(title5);
        panel5.setLayout(null);

        label7 = new JLabel();
        label7.setText("Ingrese codigo de barras");
        label7.setBounds(10, 20, 200, 30);

        campoBusqedaEdit = new JTextField();
        campoBusqedaEdit.setBounds(10, 50, 200, 20);
        campoBusqedaEdit.addKeyListener(
            new KeyListener(){
                @Override
                public void keyPressed(KeyEvent e){

                    if(e.getKeyCode() == KeyEvent.VK_ENTER){
                       
                        Connection conexion;
                        int codigo = 0;
                        PreparedStatement stmt;
            
                        if(!campoBusqedaEdit.getText().trim().isEmpty()){
                            try{
                                codigo = Integer.parseInt(campoBusqedaEdit.getText());    
                            }catch(NumberFormatException errorConv1) {
                                JOptionPane.showMessageDialog(null, "dato ingresado no valido en el campo de codigo: " + errorConv1.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                                campoBusqedaEdit.setText("");
                                return;
                            }
                            
                            conexion = conn.initConexion();
                
                            if(conexion != null){
                                
                                try{
                                    stmt = conexion.prepareStatement("SELECT * FROM usuarios WHERE id_usuario= ?");
                                    stmt.setInt(1, codigo);
                                    ResultSet res = stmt.executeQuery();      
                                    
                                    if(res.next()){
                                        
                                        campoBusqedaEdit.setText("");
                                        campoIDE.setText(res.getString("id_usuario"));
                                        campoIDE.setEnabled(false);
                                        campoNomE.setText(res.getString("nombre"));
                                        campoNomE.setEnabled(true);
                                        campoAPE.setText(res.getString("apellidoP"));
                                        campoAPE.setEnabled(true);
                                        campoAME.setText(res.getString("apellidoM"));
                                        campoAME.setEnabled(true);
                                        campoSaldoE.setText(res.getString("saldo"));
                                        
                                        campoSaldoE.setEnabled(true);
                                        botonEditar.setEnabled(true);
                                        botonEliminar.setEnabled(true);
                                        botonGuardarE.setEnabled(true);
            
                                        stmt.close();
                                        conexion.close();
                
                                    }else{
                                        JOptionPane.showMessageDialog(null, "No se encontro al usuario con id: " + codigo, "Usuario no encontrado", JOptionPane.INFORMATION_MESSAGE);
                                        campoBusqedaEdit.setText("");
                                        stmt.close();
                                        conexion.close();
                                    }
                
                                }catch(SQLException errorSQL){
                                    JOptionPane.showMessageDialog(null, "Error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null, "Complete el campo de codigo de barras", "Formulario incompleto", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
                @Override
                public void keyTyped(KeyEvent e) {
                    

                }

                @Override
                public void keyReleased(KeyEvent e) {
                

                }
            }
        );


        botonEditar = new JButton("Actualizar");
        botonEditar.setBounds(280, 40, 100, 30);
        botonEditar.setEnabled(false);
        botonEditar.addActionListener(this);

        botonEliminar = new JButton("Eliminar");
        botonEliminar.setBounds(410, 40, 100, 30);
        botonEliminar.setEnabled(false);
        botonEliminar.addActionListener(this);


        panel5.add(label7);
        panel5.add(campoBusqedaEdit);
        panel5.add(botonEditar);
        panel5.add(botonEliminar);
        //elementos de panel 6
        panel6 = new JPanel();
        panel6.setBounds(10, 130, 635, 190);

        title6 = BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Resultados");
        title6.setTitlePosition(TitledBorder.ABOVE_TOP);

        panel6.setBorder(title6);
        panel6.setLayout(null);
        
        label8 = new JLabel("Nombre(s)");
        label8.setBounds(10, 20, 100, 30);

        campoNomE = new JTextField();
        campoNomE.setBounds(10, 50, 150, 20);
        campoNomE.setEnabled(false);

        label9 = new JLabel("Apellido P.");
        label9.setBounds(10, 70, 100, 30);
        
        campoAPE = new JTextField();
        campoAPE.setBounds(10, 100, 150, 20);
        campoAPE.setEnabled(false);

        label10 = new JLabel("Apellido M.");
        label10.setBounds(10, 120, 150, 30);

        campoAME = new JTextField();
        campoAME.setBounds(10, 150, 150, 20);
        campoAME.setEnabled(false);

        label11 = new JLabel("Saldo");
        label11.setBounds(180, 20, 100, 30);

        campoSaldoE = new JTextField();
        campoSaldoE.setBounds(180, 50, 150, 20);
        campoSaldoE.setEnabled(false);

        label12 = new JLabel("ID");
        label12.setBounds(180, 70, 100, 30);

        campoIDE = new JTextField();
        campoIDE.setBounds(180, 100, 150, 20);
        campoIDE.setEnabled(false);
        
        panel6.add(label8);
        panel6.add(campoNomE);
        panel6.add(label9);
        panel6.add(campoAPE);
        panel6.add(label10);
        panel6.add(campoAME);
        panel6.add(label11);
        panel6.add(campoSaldoE);
        panel6.add(label12);
        panel6.add(campoIDE);

        editarPan.add(panel5);
        editarPan.add(panel6);
        

        //creacion de pestañas del panel principal
        opcionesPanel.addTab("Recagar",recargasPan);
        opcionesPanel.addTab("Registros de cafeteria",registrosPan);
        opcionesPanel.addTab("Registrar usuario",altasPan);
        opcionesPanel.addTab("Admin. usuarios", editarPan);
        
        this.add(opcionesPanel);
    }

    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == botonActualizar){
            String[] resRow = {"", "", "", ""};
            PreparedStatement stmt;
            Connection conexion;

            conexion = conn.initConexion();

            if(conexion != null){
                try{
                    stmt = conexion.prepareStatement("SELECT * FROM registros ORDER BY id_registro ASC");
                    ResultSet res = stmt.executeQuery();      
                    
                    limpiarTabla(tablaRegistros);

                    while(res.next()){

                        resRow[0] = padLeftZeros(res.getString("id_registro"), 10);
                        resRow[1] = res.getString("id_usuario");
                        resRow[2] = res.getString("nombre");
                        resRow[3] = res.getString("fecha");

                        modelo.addRow(resRow);
                    }
                    
                    tablaRegistros.setModel(modelo);


                    conexion.close();
                    stmt.close();

                }catch(SQLException errorSQL){
                    JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
            }
            
        }

        if(e.getSource() == botonExportar){
            
            if(this.tablaRegistros.getRowCount() == 0){
                
                JOptionPane.showMessageDialog(this, "La tabla a exportar esta vacia", "Tabla vacia", JOptionPane.INFORMATION_MESSAGE);

            }else{
                
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de excel","xls");
                
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Guardar archivo");
                chooser.setMultiSelectionEnabled(false);
                chooser.setAcceptAllFileFilterUsed(false);
                
                if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
                    
                    List<JTable> tb = new ArrayList<>();
                    List<String> nom = new ArrayList<>();
                    
                    tb.add(tablaRegistros);
                    nom.add("Registro de cafeteria");

                    String file = chooser.getSelectedFile().toString().concat(".xls");
                    try {
                        Exporter exp = new Exporter(new File(file), tb, nom);
                        if (exp.export()) {
                            borrarRegistros();
                            JOptionPane.showMessageDialog(null, "La tabla fue exportado a un archvo de excel con exito","Datos exportados", JOptionPane.INFORMATION_MESSAGE);
                            limpiarTabla(tablaRegistros);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al generar archivo " + ex.getMessage(), "Error al exportar tabla", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
        if(e.getSource() == botonAlta){
            PreparedStatement stmt;
            Connection conexion;

            float saldo = 0;
            int IDTarjeta = 0;
            
            //evaluamos si todos los campos ya estan llenos 
            if(!campoNom.getText().trim().isEmpty() && !campoAP.getText().trim().isEmpty() 
            && !campoAM.getText().trim().isEmpty() && !campoSaldo.getText().trim().isEmpty()){
                
                //evaluamos si el campo de saldo tiene el formato correcto
                try{
                    saldo = Float.parseFloat(campoSaldo.getText().trim());
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(this, "dato ingresado no valido en el campo de saldo: " + ex.getMessage(), "Error de argumento ", JOptionPane.ERROR_MESSAGE);
                    campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                    return;
                    
                }
                //evaluamos si la ventana de dialogo tiene el formato correcto para el ID de tarjeta 
                try{
                    IDTarjeta = Integer.parseInt(JOptionPane.showInputDialog("Presente tarjeta en el sensor"));
                }catch(NumberFormatException ex){
                    JOptionPane.showMessageDialog(this, "dato ingresado no valido en el campo de saldo: " + ex.getMessage(), "Error de argumento ", JOptionPane.ERROR_MESSAGE);
                    campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                    return;
                }
                //inciamos la conexion con el servidor 
                conexion = conn.initConexion();

                if(conexion == null){
                    JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
                    campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                    return;
                }
                //evaluamos si el ID de tarjeta ya esta relacionada con algun usuario 
                try{
                    stmt = conexion.prepareStatement("SELECT * FROM usuarios WHERE id_usuario = ?");
                    stmt.setInt(1, IDTarjeta);
                    
                    ResultSet res = stmt.executeQuery();   
                    //en caso de que ei ID esta registrado NO creamos el usuario y regresamos 
                    int NumFilas = 0;
                    
                    while(res.next()){
                        NumFilas++;
                    }
                    if(NumFilas > 0){
                        JOptionPane.showMessageDialog(this, "ID de tarjeta duplicado", "ID duplicado", JOptionPane.ERROR_MESSAGE);
                        campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                        return;  
                    }

                }catch(SQLException errorSQL){
                    JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                    campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                    return;
                }
                //creamos un nuevo usuario con el ID de la tarjeta 
                try{
                    stmt = conexion.prepareStatement("SELECT insertarUsuario(?, ?, ?, ?, ?)");
                    stmt.setInt(1, IDTarjeta);
                    stmt.setString(2, campoNom.getText());
                    stmt.setString(3, campoAP.getText());
                    stmt.setString(4, campoAM.getText());
                    stmt.setFloat(5, saldo);
                    
                    
                    if(!stmt.execute()){
                        JOptionPane.showMessageDialog(this, "error al crear usuario", "Error de creacion de usuario", JOptionPane.ERROR_MESSAGE);
                        campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                        return;  
                    }
                    JOptionPane.showMessageDialog(null, "Usuario creado con exito", "Proceso completado", JOptionPane.INFORMATION_MESSAGE);

                }catch(SQLException errorSQL){
                    JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                    campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                    return;
                }
            }else{
                JOptionPane.showMessageDialog(this, "Deben de ingresarse todos los datos", "Error de argumentos", JOptionPane.ERROR_MESSAGE);
                campoNom.setText("");campoAP.setText("");campoAM.setText("");campoSaldo.setText("");
                
            }

        }

        if(e.getSource() == botonActualizar2){
            String[] resRow = {"", "", "", "", ""};
            PreparedStatement stmt;
            Connection conexion;

            conexion = conn.initConexion();

            if(conexion != null){
                try{
                    stmt = conexion.prepareStatement("SELECT * FROM usuarios ORDER BY id_usuario ASC");
                    ResultSet res = stmt.executeQuery();      
                    
                    limpiarTabla(tablaUsuarios);

                    while(res.next()){

                        resRow[0] = padLeftZeros(res.getString("id_usuario"), 10);
                        resRow[1] = res.getString("nombre");
                        resRow[2] = res.getString("apellidoP");
                        resRow[3] = res.getString("apellidoM");
                        resRow[4] = res.getString("saldo");

                        modelo2.addRow(resRow);
                    }
                    
                    tablaUsuarios.setModel(modelo2);


                    conexion.close();
                    stmt.close();

                }catch(SQLException errorSQL){
                    JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
            }
        }
     
        if(e.getSource() == botonEditar){

            Connection conexion;
            int codigo = 0;
            float saldoRecarga = 0;
            PreparedStatement stmt;
            
            if(!campoNomE.getText().trim().isEmpty() && !campoAPE.getText().trim().isEmpty() 
            && !campoAME.getText().trim().isEmpty() && !campoSaldoE.getText().trim().isEmpty()){
                try{
                    codigo = Integer.parseInt(campoIDE.getText());    
                }catch(NumberFormatException errorConv1) {
                    JOptionPane.showMessageDialog(this, "dato ingresado no valido en el campo de codigo: " + errorConv1.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                conexion = conn.initConexion();
    
                if(conexion != null){
                    
                    try{        
                
                        try {

                            saldoRecarga = Float.parseFloat(campoSaldoE.getText());

                        } catch (NumberFormatException errorConv2) {
                            JOptionPane.showMessageDialog(this, "dato ingresado no valido en el campo de saldo: " + errorConv2.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        stmt = conexion.prepareStatement("UPDATE usuarios set nombre = ?, apellidoM = ?, apellidoP = ?, saldo = ? WHERE id_usuario = ?");
                        stmt.setString(1, campoNomE.getText());
                        stmt.setString(2, campoAME.getText());
                        stmt.setString(3, campoAPE.getText());
                        stmt.setFloat(4, saldoRecarga);
                        stmt.setInt(5, codigo);

                        if(stmt.executeUpdate() == 1){
                            JOptionPane.showMessageDialog(this, "Actualizacion realizada con exito", "Actualizacion satisfactoria", JOptionPane.INFORMATION_MESSAGE);            
                        }else{
                            JOptionPane.showMessageDialog(this, "Error al actualizar registro", "Actualizacion Fallida", JOptionPane.ERROR_MESSAGE);        
                        }

                        stmt.close();
                        conexion.close();
    
                    }catch(SQLException errorSQL){
                        JOptionPane.showMessageDialog(this, "Error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(this, "Ingrese un ID para recargar", "Formulario incompleto", JOptionPane.INFORMATION_MESSAGE);
            }

            campoBusqedaEdit.setText("");
            campoNomE.setText("");
            campoAME.setText("");
            campoAPE.setText("");
            campoIDE.setText("");
            campoSaldoE.setText("");

            botonEditar.setEnabled(false);
            botonEliminar.setEnabled(false);

            campoBusqedaEdit.requestFocus();
        }
        
        if(e.getSource() == botonEliminar){

            int opc = JOptionPane.showConfirmDialog(this, "Esta seguro que quiere eliminar este usuario?", "eliminar usuario", JOptionPane.ERROR_MESSAGE);

            if(opc == 0){
                Connection conexion;
                int codigo = 0;
                PreparedStatement stmt;
            
                try{
                    codigo = Integer.parseInt(campoIDE.getText());    
                }catch(NumberFormatException errorConv1) {
                    JOptionPane.showMessageDialog(this, "dato ingresado no valido en el campo de codigo: " + errorConv1.getMessage(), "Error de argumento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                conexion = conn.initConexion();
    
                if(conexion != null){
                    
                    try{        
                
                        stmt = conexion.prepareStatement("DELETE FROM usuarios WHERE id_usuario = ?");
                        stmt.setInt(1, codigo);
    
                        if(stmt.executeUpdate() == 1){
                            JOptionPane.showMessageDialog(this, "Operacion realizada con exito", "Actualizacion satisfactoria", JOptionPane.INFORMATION_MESSAGE);            
                        }else{
                            JOptionPane.showMessageDialog(this, "Error al eliminar registro", "Actualizacion Fallida", JOptionPane.ERROR_MESSAGE);        
                        }
    
                        stmt.close();
                        conexion.close();
    
                    }catch(SQLException errorSQL){
                        JOptionPane.showMessageDialog(this, "Error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
                }
            }
            

            campoBusqedaEdit.setText("");
            campoNomE.setText("");
            campoAME.setText("");
            campoAPE.setText("");
            campoIDE.setText("");
            campoSaldoE.setText("");

            botonEditar.setEnabled(false);
            botonEliminar.setEnabled(false);

            campoBusqedaEdit.requestFocus();
        }
    
    }

    public void incializarTabla(){
        String[] resRow = {"", "", "", ""};
        PreparedStatement stmt;
        Connection conexion;


        conexion = conn.initConexion();

        if(conexion != null){
            try{
                stmt = conexion.prepareStatement("SELECT * FROM registros");
                ResultSet res = stmt.executeQuery();      
                
                while(res.next()){

                    resRow[0] = padLeftZeros(res.getString("id_registro"), 10);
                    resRow[1] = res.getString("id_usuario");
                    resRow[2] = res.getString("nombre");
                    resRow[3] = res.getString("fecha");
                    modelo.addRow(resRow);
                }

                conexion.close();
                stmt.close();

            }catch(SQLException errorSQL){
                JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void incializarTabla2(){
        String[] resRow = {"", "", "", "", ""};
        PreparedStatement stmt;
        Connection conexion;


        conexion = conn.initConexion();

        if(conexion != null){
            try{
                stmt = conexion.prepareStatement("SELECT * FROM usuarios");
                ResultSet res = stmt.executeQuery();      
                
                while(res.next()){

                    resRow[0] = padLeftZeros(res.getString("id_usuario"), 10);
                    resRow[1] = res.getString("nombre");
                    resRow[2] = res.getString("apellidoP");
                    resRow[3] = res.getString("apellidoM");
                    resRow[4] = res.getString("saldo");
                    modelo2.addRow(resRow);
                }

                conexion.close();
                stmt.close();

            }catch(SQLException errorSQL){
                JOptionPane.showMessageDialog(this, "error al consultar con la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void limpiarTabla(JTable tabla){
        try {
            DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
            int filas=tabla.getRowCount();
            for (int i = 0;filas>i; i++) {
                modelo.removeRow(0);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al limpiar la tabla.");
        }
    }

    public void borrarRegistros(){
        PreparedStatement stmt;
        Connection conexion;

        conexion = conn.initConexion();

        if(conexion != null){
            try{
                stmt = conexion.prepareStatement("TRUNCATE registros");   

                if(stmt.executeUpdate() < 0){
                    JOptionPane.showMessageDialog(this, "Error al limpiar los registros de la cafeteria", "Error de consulta", JOptionPane.ERROR_MESSAGE);
                }

                conexion.close();
                stmt.close();

            }catch(SQLException errorSQL){
                JOptionPane.showMessageDialog(this, "error al limpiar registros en la base de datos: " + errorSQL.getMessage(), "Error de consulta", JOptionPane.ERROR_MESSAGE);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Error al conectar con el dispositivo", "Conexion Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
    
        return sb.toString();
    }


}
