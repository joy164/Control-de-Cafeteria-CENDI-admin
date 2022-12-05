import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Conn{
    /* Declaramos 4 variables con el driver, la bbdd, usuario y contraseña*/
    private static final String driver="com.mysql.cj.jdbc.Driver";
    private static final String bbdd="jdbc:mysql://localhost:3306/CENDI";
    //private static final String bbdd="jdbc:mysql://192.168.1.73:3306/CENDI";
    private static final String usuario ="root";
    //private static final String usuario ="contaduriaCENDI";
    private static final String clave="2019601919jJ+";

    public Conn(){
        
    }
    /* Creamos el método para conectarnos a la base de datos. Este método
    devolverá un objeto de tipo Connection.*/
    public Connection initConexion(){
        /*Declaramos una variable para almacenar la cadena de conexión.
        Primero la iniciamos en null.*/
        Connection conex = null;
         
        //Controlamos la excepciones que puedan surgir al conectarnos a la BBDD
        try {
            //Registrar el driver
            Class.forName(driver);
            //Creamos una conexión a la Base de Datos
            conex = DriverManager.getConnection(bbdd, usuario, clave);
         
        // Si hay errores informamos al usuario. 
        } catch (Exception e) {
            System.out.println("Error: "
                    + e.getMessage());
        }
        // Devolvemos la conexión.
        return conex;
    }

    public void endConexion(Connection con){
        try{
            // Cerramos la conexión
            con.close();    
        }catch(SQLException e){
           /* Controlamos excepción en caso de que se pueda producir
            a la hora de cerrar la conexión*/
            System.out.println(e.getMessage().toString());
        }
    }

}
