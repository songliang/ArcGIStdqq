package Code;
import java.util.Random;

/**
 *
 * @author Jeefer590
 */
public class serial {

    private String nombre;
    private String serialNumberEncoded;
    private static final Byte NUMERO_DE_DIGITOS_SERIAL = 18;
    
    public serial(String nombre) throws java.security.NoSuchAlgorithmException{
        this.nombre = nombre;
        this.serialNumberEncoded = calcularHashSeguridad(this.nombre, "SHA-256").substring(12,48 )+ calcularHashSeguridad(this.nombre, "MD5")
                      + calcularHashSeguridad(this.nombre, "SHA1");
    }
    
    private String calcularHashSeguridad(String stringInput, String nombreAlgoritmo) throws java.security.NoSuchAlgorithmException{
        String hexMessageCode = "";
        byte[] buffer = stringInput.getBytes();
        java.security.MessageDigest messageDigest = java.security.MessageDigest.getInstance(nombreAlgoritmo);
        messageDigest.update(buffer);
        byte[] messageDigestBytes = messageDigest.digest();
        for(int i = 0; i < messageDigestBytes.length; i++){
            int countEnccode = messageDigestBytes[i] & 0xff;
            
            if(Integer.toHexString(countEnccode).length() == 1){
                hexMessageCode = hexMessageCode + "0";
            }            
            hexMessageCode = hexMessageCode + Integer.toHexString(countEnccode);
        }
        
        
        return hexMessageCode.toUpperCase();
    }
    
    public String generador18digitosEstatico(){
        String serialNumber = ""
        + serialNumberEncoded.charAt(32)
        + serialNumberEncoded.charAt(76)
        + serialNumberEncoded.charAt(100)
        + serialNumberEncoded.charAt(50)
        + "-"
        + serialNumberEncoded.charAt(2)
        + serialNumberEncoded.charAt(91)
        + serialNumberEncoded.charAt(73)
        + serialNumberEncoded.charAt(72)
        + serialNumberEncoded.charAt(98)
        + "-"
        + serialNumberEncoded.charAt(47)
        + serialNumberEncoded.charAt(65)
        + serialNumberEncoded.charAt(18)
        + serialNumberEncoded.charAt(85)
        + "-"
        + serialNumberEncoded.charAt(27)
        + serialNumberEncoded.charAt(53)
        + serialNumberEncoded.charAt(102)
        + serialNumberEncoded.charAt(15)
        + serialNumberEncoded.charAt(99); 
        return serialNumber;
    }
    
    public String generador18DigitosDinamico(){
        String serialLong = serialNumberEncoded;        
        Random r = new Random();
        Integer[] espaciosOcupados = new Integer[NUMERO_DE_DIGITOS_SERIAL];
        Boolean noTerminado = true;
        String serialShort = "";
        Byte espacio = 0;
        Integer temp = r.nextInt(103)+1;
                    
        serialShort = serialShort + serialLong.charAt(temp);
        espaciosOcupados[0] = temp;
        
        while(noTerminado){
            Random r2 = new Random();
            Integer temp2 = r2.nextInt(103)+1;
            
            if(temp2 != espaciosOcupados[espacio]){
                if(espacio < (NUMERO_DE_DIGITOS_SERIAL-1)){
                    serialShort = serialShort + serialLong.charAt(temp2);
               
                    espacio++;         
                    espaciosOcupados[espacio] = temp2;
                }else{
                noTerminado = false;
                }
            } else{
                noTerminado = false;
             }
        }
        
        serialShort = serialShort.substring(0, 4) + "-"
                       +serialShort.substring(4, 9) + "-"
                       +serialShort.substring(9, 13) + "-"
                       +serialShort.substring(13, 18);
        
        return serialShort;
    }   
    
    public Boolean autentificador(String nombre, String serial){
        serial = serial.substring(0, 4) + 
                 serial.substring(5, 10) + 
                 serial.substring(11, 15) + 
                 serial.substring(16, 20);
        
        //if(serial != ){
        //    return false;
        //}

        return true;
    }
    
    public String getNombre(){
        return nombre;
    }
}
