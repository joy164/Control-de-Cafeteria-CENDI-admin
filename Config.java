import java.util.prefs.*;

public class Config{
    
    private String[] llaves = {"servidor", "usuario", "claveBD", "precio"};

    public Preferences config = Preferences.userNodeForPackage(Config.class);

    public Config(){}
    
    public void cambiarIPservidor(String IPservidor) throws Exception{
        String pattern = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
        
        if(IPservidor.matches(pattern)){
            config.put(llaves[0], IPservidor);
        }else{
            throw new Exception("La IP ingresada no tiene un formato valido");
        }
    }
    public void cambiarUsuario(String usuario){
        config.put(llaves[1], usuario);
    }
    public void cambiarClaveBD(String contraseña){
        config.put(llaves[2], contraseña);
    }
    public void cambiarPrecio(Float precio){
        config.putFloat(llaves[3], precio);
    }


    public String obtenerIPservidor(){
        return config.get(llaves[0], "127.0.0.1");
    }

    public String obtenerUsuario(){
        return config.get(llaves[1], "root");
    }

    public String obtenerClaveBD(){
        return config.get(llaves[2], "2019601919jJ+");
    }
    public float obtenerPrecio(){
        return config.getFloat(llaves[3], 2.5f);
    }

    public void reiniciarValores(){
        config.put(llaves[0], "127.0.0.1");
        config.put(llaves[1], "root");
        config.put(llaves[2], "2019601919jJ+");
        config.putFloat(llaves[3], 2.5f);
    }

}
