package criptografia;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

public class Criptografia {

	private static ArrayList<Usuario> usuarios;

	public static void main(String[] args) {

		KeyGenerator generador;

		try {

			// Creamos el generador de la clave
			generador = KeyGenerator.getInstance("AES");

			// Generamos la clase simétrica
			SecretKey Eternal = generador.generateKey();

			Cipher cifrador = null;
			byte[] bytesMensajeOriginal = null;
			byte[] bytesMensajeOriginalCifrado = null;

			String frase = null;
			String mensaje = "";

			// Creamos los objetos Usuario añadidos directamente a la lista
			usuarios = new ArrayList<>();
			usuarios.add(new Usuario("Conan", "pass1"));
			usuarios.add(new Usuario("Arthur", "pass2"));
			usuarios.add(new Usuario("Mike", "pass3"));

			// Recorremos el array para llamar a la función hashear de cada usuario de la
			// lista

			for (Usuario us : usuarios)
				hash(us);

			int intentos = 0;
			
			try {
				
				do {
					
					String usuario = JOptionPane.showInputDialog("Introduzca su usuario");

					String contraseña = JOptionPane.showInputDialog("Introduzca su contraseña");

					hash(contraseña);

					intentos++;

					for (Usuario us : usuarios) {

						if (us.getNombre().equals(usuario) && us.getContraseña().equals(hash(contraseña))) {

							do {
			
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

									bytesMensajeOriginalCifrado = cifrador.doFinal(bytesMensajeOriginal);
									String mensajeCifrado = new String(bytesMensajeOriginalCifrado);
									JOptionPane.showMessageDialog(null,
											"Mensaje cifrado correctamente:\n " + bytesMensajeOriginalCifrado);

									break;

								case "2":
									
									try {
										
										// Configuramos el cifrador para que use la clave para desencriptar
										cifrador.init(Cipher.DECRYPT_MODE, Eternal);

										// El cifrador devuelve una cadena de bytes
										byte[] bytesMensajeCifrado = cifrador.doFinal(bytesMensajeOriginalCifrado);
										mensajeCifrado = new String(bytesMensajeCifrado);

										// Mostramos el mensaje cifrado
										JOptionPane.showMessageDialog(null,
												"Mensaje cifrado: " + bytesMensajeOriginalCifrado);
										// Mostramos el mensaje descifrado
										JOptionPane.showMessageDialog(null,
												"Mensaje Descifrado: " + new String(mensajeCifrado));

										break;
										
									} catch (NullPointerException er) {
										
										JOptionPane.showMessageDialog(null, "Primero tienes que cifrar una frase");
									}
									
								default:

									JOptionPane.showMessageDialog(null,
											"Opción no valida, \nVuelva a escoger una opción valida", "Advertencia", 2);
									break;

								}
								
							} while (!mensaje.equals("0"));

						}

					}

				} while (intentos < 3 && !mensaje.equals("0"));
				
			} catch (NullPointerException er) {
				
				JOptionPane.showMessageDialog(null, "Has pulsado cancelar");
				
			}

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
		
		// Creamos los bytes de las contraseñas
		byte[] password = usuario.getContraseña().getBytes();

		// Creamos el objeto con el algotirmo que vamos a utilizar y le pasamos los
		// bytes de las contraseñas
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		md.update(password);

		// Con el método digest obtenemos el resumen de la casena hash
		byte[] passwordHash = md.digest();

		// Lo pasamos a String para poder almacenarlo en la pass del usuario y poder
		// comparar

		String passwordUserHash = new String(passwordHash);

		// Setteamos el hash de la contraseña
		usuario.setContraseña(passwordUserHash);
		
	}

	public static String hash(String contraseña) throws NoSuchAlgorithmException {
		
		try {
			
			// Creamos los bytes de las contraseñas
			byte[] password = contraseña.getBytes();

			// Creamos el objeto con el algotirmo que vamos a utilizar y le pasamos los
			// bytes de las contraseñas
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(password);

			// Con el método digest obtenemos el resumen de la casena hash
			byte[] passwordHash = md.digest();

			// Lo pasamos a String para poder almacenarlo en la pass del usuario y poder
			// comparar
			String passwordUserHash = new String(passwordHash);
			return passwordUserHash;
			
		} catch (NullPointerException n) {
			
			JOptionPane.showMessageDialog(null, "Has pulsado cancelar");
			return null;
			
		}

	}
}
