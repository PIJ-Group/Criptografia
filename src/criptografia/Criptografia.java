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

	/* 
	 * JORGE
	 * private static ArrayList<Usuario> usuarios; 
	 * Me he dado cuenta que al tener el array dentro del primer try, no sirve para nada declararlo static.
	 * Lo he declarado en la propia línea donde creamos el ArrayList, si lo veis bien, borrad todo este comentario y la declaración.
	*/

	public static void main(String[] args) {

		/*
		 * JORGE
		 * KeyGenerator generador;
		 * Lo mismo que en la declaración anterior, al tenterlo en el primer try, y solo utilizarlo ahí, no hace falta ponerlo fuera.
		 * Igual que en el anterior, si lo veis bien, borradlo.
		 */

		try {

			/*
			 * JORGE
			 * He reordenado la declaración de las variables, teníamos creado el KeyGenerador y el SecretKey,
			 * y después solo declarábamos las variables para volver a crear después más objetos.
			 * He dejado todas las declaraciones arriba del KeyGenerator.
			 * También he borrado la declaración de las dos siguientes de las he declarado donde creamos el objeto,
			 * ya que solo son de ámbito del bloque donde trabaja y para que podamos entenderlo mejor para repasar,
			 * así vemos donde se ha ido creando todo, solo dejo las que hay que declararlas ahí porque se utilizan en más sitios.
			 * Lo mismo de antes, si lo veis bien, se borra.
			 * byte[] bytesMensajeOriginal = null;
			 * String frase = null;
			 */
			
			Cipher cifrador = null;
			byte[] bytesMensajeOriginalCifrado = null;
			String mensaje = "";
			int intentos = 0;
			
			// Creamos el generador de la clave
			KeyGenerator generador = KeyGenerator.getInstance("AES");

			// Generamos la clase simétrica
			SecretKey Eternal = generador.generateKey();

			// Creamos los objetos Usuario añadidos directamente a la lista
			ArrayList<Usuario> usuarios = new ArrayList<>();
			usuarios.add(new Usuario("Conan", "pass1"));
			usuarios.add(new Usuario("Arthur", "pass2"));
			usuarios.add(new Usuario("Mike", "pass3"));

			// Recorremos el array para llamar a la función hashear de cada usuario de la
			// lista

			for (Usuario us : usuarios)
				hash(us);
			
			try {
				
				do {
					
					String usuario = JOptionPane.showInputDialog("Introduzca su usuario");
					if(usuario == null) { // ISRA PARA SALIR AL PULSAR CANCELAR
						intentos=4;
					}

					String contraseña = JOptionPane.showInputDialog("Introduzca su contraseña");
					
					if(contraseña == null) { // ISRA PARA SALIR AL PULSAR CANCELAR
						intentos = 4;
					}

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

									String frase = JOptionPane.showInputDialog("Digite la frase a encriptar");

									// Ciframos la clave con el Objeto Cipher
									cifrador = Cipher.getInstance("AES");

									// Ahora configuramos el cifrador para que use la clave simetrica
									cifrador.init(Cipher.ENCRYPT_MODE, Eternal);

									// Ciframos el mensaje que ha escrito el usuario a bytes
									byte[] bytesMensajeOriginal = frase.getBytes();

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

			if (intentos == 3) {// ISRA PARA SALIR AL PULSAR CANCELAR

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

		// Con el método digest obtenemos el resumen de la cadena hash
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

			// Con el método digest obtenemos el resumen de la cadena hash
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
