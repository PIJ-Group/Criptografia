package criptografia;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

public class Criptografia {

	static Scanner entrada = new Scanner(System.in);
	private static ArrayList<Usuario> usuarios;
	private static String passwordUserHash;

	public static void main(String[] args) {
		
		KeyGenerator generador;
		
		try {
			
			//Creamos el generador de la clave
			generador = KeyGenerator.getInstance("AES");
			
			//Generamos la clase simétrica
			SecretKey Eternal = generador.generateKey();

			Cipher cifrador = null;
			byte[] bytesMensajeOriginal = null;
			byte[] bytesMensajeOriginalCifrado = null;

			String frase = null;
			String mensaje = "";
			
			//Creamos los objetos Usuario añadidos directamente a la lista	
			usuarios = new ArrayList<>();
			usuarios.add(new Usuario("Conan", "pass1"));
			usuarios.add(new Usuario("Arthur", "pass2"));
			usuarios.add(new Usuario("Mike", "pass3"));

			//Recorremos el array para llamar a la función hashear de cada usuario de la lista
			for (Usuario us : usuarios)
				hash(us);

			int intentos = 0;

			String usuario = JOptionPane.showInputDialog("Introduzca su usuario");

			String contraseña = JOptionPane.showInputDialog("Introduzca su contraseña");

			hash(contraseña);
			
			intentos++;

			do {

				for (Usuario us : usuarios) {
					
					if (us.getNombre().equals(usuario) && us.getContraseña().equals(contraseña)) {
						
						do {
							
							intentos = 0;
							mensaje = menu();
	
							switch (mensaje) {
							
							case "0":
								
								JOptionPane.showMessageDialog(null, "¡Hasta luego!");
								break;
	
							case "1":
								
								frase = JOptionPane.showInputDialog("Digite la frase a encriptar");
								
								// Ciframos la clave con el Objeto Cipher
								cifrador = Cipher.getInstance("AES");
	
								// Ahora configuramos el cifrador para que use la clave simetrica
								cifrador.init(Cipher.ENCRYPT_MODE, Eternal);
	
								// Ciframos el mensaje que ha escrito el usuario a bytes
								bytesMensajeOriginal = frase.getBytes();
								//JORGE: aquí faltaría el mensaje ya cifrado, con el doFinal del cifrador
								//Compruébalo Pablo, en princpio debe estar en los dos csasos ya que son dos cifradores con métodos diferentes
								//uno sería encryp y otro decrypt.
								bytesMensajeOriginalCifrado = cifrador.doFinal(bytesMensajeOriginal);
								
								break;
	
							case "2":
								
								//JORGE: he metido esto, porque faltaba el modo desencriptar en el caso 2
								//Configuramos el cifrador para que use la clave para desencriptar
								cifrador.init(Cipher.DECRYPT_MODE, Eternal);
								
								// El cifrador devuelve una cadena de bytes
								//JORGE: aquí he cambiado la variable del argumento a la que he creado arriba del doFinal
								byte[] bytesMensajeCifrado = cifrador.doFinal(bytesMensajeOriginalCifrado);
								
								String mensajeCifrado = new String(bytesMensajeCifrado);
	
								JOptionPane.showMessageDialog(null, "Mensaje Original: " + frase);
								JOptionPane.showMessageDialog(null, "Mensaje cifrado: " + mensajeCifrado);
								
								break;
	
							default:
								
								JOptionPane.showMessageDialog(null,
										"Opción no valida, \nVuelva a escoger una opción valida", "Advertencia", 2);
								break;
								
							}

						} while (!mensaje.equals("0"));

					}

				}
			
			
			} while (intentos < 3);  //JORGE: comprobar que vale con esto quitado: && !mensaje.equals("0")

			if (intentos >= 3) {

				JOptionPane.showMessageDialog(null, "Número máximo de intentos permitidos\nPrograma terminado");
			}

		} catch (GeneralSecurityException sge) {
			JOptionPane.showMessageDialog(null, "Número máximo de intentos permitidos");
			sge.printStackTrace();
		}

	}

	public static String menu() {
		String mensaje = JOptionPane
				.showInputDialog("Bienvenido/a a Crypt \n" + "Elija una de las siguietes opciones: \n"
						+ "0. Salir del programa \n" + "1. Encriptar frase \n" + "2. Desencriptar frase \n ");

		return mensaje;

	}

	public static void hash(Usuario usuario) throws NoSuchAlgorithmException {
		//Creamos los bytes de las contraseñas
		byte[] password = usuario.getContraseña().getBytes();

		//Creamos el objeto con el algotirmo que vamos a utilizar y le pasamos los bytes de las contraseñas
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password);
		
		//Con el método digest obtenemos el resumen de la casena hash
		byte[] passwordHash = md.digest();
		
		//Lo pasamos a String para poder almacenarlo en la pass del usuario y poder comparar
		String passwordUserHash = new String(passwordHash);
		
		//Setteamos el hash de la contraseña
		usuario.setContraseña(passwordUserHash);

	}
	
	public static String hash(String contraseña) throws NoSuchAlgorithmException {
		
		//Creamos los bytes de las contraseñas
		byte[] password = contraseña.getBytes();
		
		//Creamos el objeto con el algotirmo que vamos a utilizar y le pasamos los bytes de las contraseñas
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password);
		
		//Con el método digest obtenemos el resumen de la casena hash
		byte[] passwordHash = md.digest();

		//Lo pasamos a String para poder almacenarlo en la pass del usuario y poder comparar
		passwordUserHash = new String(passwordHash);
		return passwordUserHash;

	}


}


