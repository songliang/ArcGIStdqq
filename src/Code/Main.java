package Code;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Jeefer590
 */
public class Main {

    
    public static void main(String[] args){
        
        System.out.println("1) Generar Serial Key");
        System.out.println("2) Validar Serial Key");
        System.out.println("3) Generar Serial Key Dinamico (PRUEBAS)");
        try{
            Scanner s = new Scanner(System.in);
            int desicion;
            desicion = s.nextInt();
            
            switch(desicion){
                case 1:     
                    Scanner d = new Scanner(System.in);
                    String nombre;

                    System.out.print("Ingrese Su nombre: ");
                    nombre = d.nextLine();

                    try{
                        serial n = new serial(nombre);
                        System.out.println("Su codigo es: " + n.generador18digitosEstatico());
                        System.exit(0);
                    }catch(java.security.NoSuchAlgorithmException e){
                        e.printStackTrace();
                    }
                    break;                
                case 2:
                    Scanner f = new Scanner(System.in), g = new Scanner(System.in);
                    String nombreV;
                    String keyV;
                    
                    System.out.print("Ingrese Nombre Asociado: ");
                    nombreV = f.next();
                    
                    System.out.print("Ingrese Serial Key Asociado: ");
                    keyV = g.next().toUpperCase();
                    
                    if(keyV != null && keyV.length() == 21){
                        
                    } else{
                        System.err.println("ERROR CRITICO");
                        System.exit(1);
                    }
                    
                    break;

                case 3:
                    Scanner t = new Scanner(System.in);
                    String nombres = "";

                    System.out.print("Ingrese Su nombre: ");
                    nombre = nombres + t.nextLine();

                    try{
                        serial n = new serial(nombres);
                        String ss = n.generador18DigitosDinamico();
                        System.out.println("Su codigo es: " + ss);
                        System.out.println("Numero Caracters " + ss.length());
                        System.exit(0);
                    }catch(java.security.NoSuchAlgorithmException e){
                        e.printStackTrace();
                    }
                    break;
                    
                default:
                    System.err.println("ERROR CRITICO, PROGRAMA CERRADO");
                    System.exit(1);
            }
        }catch(InputMismatchException e){
            System.err.println("ERROR CRITICO, PROGRAMA CERRADO");
            System.exit(1);
        }
    }
    
}
