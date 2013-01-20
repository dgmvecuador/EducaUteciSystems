/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core.chat.cliente;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.core.chat.AtenderClienteServidor;
import org.educautecisystems.core.chat.elements.ChatConstants;
import org.educautecisystems.core.chat.elements.FileChat;
import org.educautecisystems.core.chat.elements.MessageHeaderParser;
import org.educautecisystems.core.chat.elements.UserChat;
import org.educautecisystems.intefaz.Chat;

/**
 *
 * @author Shadow2013
 */
public class ClienteServidorChat extends Thread {
    private Chat pantallaChat;
    private boolean continuar;
    
    /* Informaciòn del cliente. */
    private String clienteToken = null;
    private String clienteUsuarioId = null;
    
    public ClienteServidorChat( Chat pantallaChat ) {
        this.pantallaChat = pantallaChat;
        continuar = true;
    }

    @Override
    public void run() {
        if ( pantallaChat == null ) {
            System.err.println("No se ha encontrado una ventana de chat al cual hacer referencia.");
            return;
        }
        
        pantallaChat.mostrarInfo("Iniciando chat..");
        
        /* Conectarse con el servidor. */
        try {
            pantallaChat.mostrarInfo("Conectando con el servidor..");
            Socket socket = new Socket(Sistema.getChatServerConf().getIp(), 
                Integer.parseInt(Sistema.getChatServerConf().getPort()));
            pantallaChat.mostrarInfo("Conectado con ["+Sistema.getChatServerConf().getIp()+
                    ":"+Sistema.getChatServerConf().getPort()+"]");
            
            pantallaChat.activarBotones(true);
            cicloUsuario(socket);
            
            pantallaChat.mostrarInfo("Cerrando conexión..");
            socket.close();
        } catch ( Exception ex ) {
            if ( ex instanceof NumberFormatException ) {
                pantallaChat.mostrarError("El puerto ingresado no es un nùmero.");
                return;
            }
            pantallaChat.mostrarError("MAIN - "+ex.getLocalizedMessage());
            return;
        }
        
        pantallaChat.mostrarInfo("Cerrando chat..");
    }
    
    private void cicloUsuario( Socket socket ) throws Exception {
        OutputStream salida = socket.getOutputStream();
        InputStream entrada = socket.getInputStream();
        StringBuilder mensaje = new StringBuilder();
        
        mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
        mensaje.append(ChatConstants.CHAT_END_HEADER);
        mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND, ChatConstants.COMMAND_LOGIN));
        mensaje.append(generateHeaderValue(ChatConstants.LABEL_REAL_NAME, Sistema.getChatSessionConf().getRealName()));
        mensaje.append(generateHeaderValue(ChatConstants.LABEL_NICKNAME, Sistema.getChatSessionConf().getNickname()));
        mensaje.append(ChatConstants.CHAT_END_HEADER);

        salida.write(mensaje.toString().getBytes());
        salida.flush();
        
        MessageHeaderParser header = MessageHeaderParser.parseMessageHeader(entrada, true);
        if (!header.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
            pantallaChat.mostrarError("No se pudo negociar con el servidor.");
            return;
        }

        clienteToken = header.getVar(ChatConstants.LABEL_USER_TOKEN);
        clienteUsuarioId = header.getVar(ChatConstants.LABEL_USER_ID);
        
		/* Actualizar las listas de los usuarios. */
        actualizarUsuarios();
		actualizarArchivos();
		actualizarPantallaProfesor();
        
        while ( continuar ) {
            MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
            if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
                pantallaChat.mostrarError("No se pudo negociar con el servidor.");
                continue;
            }
            
            String contentLength = headerMessage.getVar(ChatConstants.LABEL_CONTENT_LENGHT);
            String userId = headerMessage.getVar(ChatConstants.LABEL_USER_ID);
            
            long contentLengthLong = -1;
			
			try {
				contentLengthLong = Long.parseLong(contentLength);
			} catch ( NumberFormatException nfe ) {
				pantallaChat.mostrarError("Error al recibir el mensaje.");
				return;
			}
            
			byte [] messageBytes = new byte[(int)contentLengthLong];
			entrada.read(messageBytes);
            
            pantallaChat.recibirMensaje(userId, new String(messageBytes, "UTF-8"));
        }
    }
	
	private void actualizarPantallaProfesor() {
		Thread hiloPantallaProfesor = new Thread() {
			@Override
			public void run() {
				while (continuar) {
					try {
						Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
								Integer.parseInt(Sistema.getChatServerConf().getPort()));
						OutputStream salida = socket.getOutputStream();
						InputStream entrada = socket.getInputStream();

						StringBuilder mensaje = new StringBuilder();
						mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
						mensaje.append(ChatConstants.CHAT_END_HEADER);
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND,
								ChatConstants.COMMAND_GET_SCREEN_SHOT));
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN,
								"" + clienteToken));
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_FORMAT,
								"PNG"));
						mensaje.append(ChatConstants.CHAT_END_HEADER);

						salida.write(mensaje.toString().getBytes());
						salida.flush();

						MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
						if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
							pantallaChat.mostrarError("Error obteniendo pantalla.");
						}
						String contentLength = headerMessage.getVar(ChatConstants.LABEL_CONTENT_LENGHT);

						int contentLengthInt = -1;

						try {
							contentLengthInt = Integer.parseInt(contentLength);
						} catch (NumberFormatException nfe) {
							pantallaChat.mostrarError("Error al recibir el mensaje.");
							return;
						}

						/* Leer imagen */
						ByteArrayOutputStream bufferImageBytes = new ByteArrayOutputStream(contentLengthInt);
						
						for ( int i=0; i<contentLengthInt;i++ ) {
							int byteRemoto = entrada.read();
							if (byteRemoto != -1) {
                                bufferImageBytes.write(byteRemoto);
                            } else {
                                pantallaChat.mostrarError("Not Enought bytes read.");
                                return;
                            }
						}
						
						BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bufferImageBytes.toByteArray()));
						pantallaChat.nuevaPantallaProfesor(bufferedImage);

						/* Cerrar la sessión. */
						salida.close();
						entrada.close();
						socket.close();
						Thread.sleep(2000);
					} catch (Exception ex) {
						pantallaChat.mostrarError("Hilo PantallaProfesor - " + ex);
						ex.printStackTrace();
					}
				}
			}
		};
		hiloPantallaProfesor.start();
	}
	
	private void actualizarArchivos () {
		Thread hiloListaArchivos = new Thread() {
			@Override
			public void run() {
				while ( continuar ) {
					try {
						Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
                                Integer.parseInt(Sistema.getChatServerConf().getPort()));
                        OutputStream salida = socket.getOutputStream();
                        InputStream entrada = socket.getInputStream();

                        StringBuilder mensaje = new StringBuilder();
                        mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
                        mensaje.append(ChatConstants.CHAT_END_HEADER);
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND,
                                ChatConstants.COMMAND_GET_FILES));
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN,
                                ""+clienteToken));
						mensaje.append(generateHeaderValue(ChatConstants.LABEL_FORMAT,
                                "XML"));
						mensaje.append(ChatConstants.CHAT_END_HEADER);
						
						salida.write(mensaje.toString().getBytes());
                        salida.flush();
						
						MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
                        if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
                            pantallaChat.mostrarError("Error al obtener archivo.");
                        }
                        
                        String contentLength = headerMessage.getVar(ChatConstants.LABEL_CONTENT_LENGHT);

                        long contentLengthLong = -1;

                        try {
                            contentLengthLong = Long.parseLong(contentLength);
                        } catch (NumberFormatException nfe) {
                            pantallaChat.mostrarError("Error al recibir el mensaje.");
                            return;
                        }

                        StringBuilder xmlArchivos = new StringBuilder();

                        while (xmlArchivos.length() < contentLengthLong) {
                            int byteRead = entrada.read();
                            if (byteRead != -1) {
                                xmlArchivos.append((char) byteRead);
                            } else {
                                pantallaChat.mostrarError("Not Enought bytes read.");
                                return;
                            }
                        }
						
						ArrayList <FileChat> archivos = FileChat.generateListFromXML(new String(xmlArchivos.toString().getBytes("latin1"), "UTF-8"));
						pantallaChat.nuevaListaArchivos(archivos);
						
						/* Cerrar la sessión. */
                        salida.close();
                        entrada.close();
                        socket.close();
                        Thread.sleep(500);
					} catch ( Exception e ) {
						 pantallaChat.mostrarError("Hilo Archivos - " + e);
					}
				}
			}
		};
		hiloListaArchivos.start();
	}
    
    private void actualizarUsuarios() {
        Thread hiloListaUsuarios = new Thread() {
            @Override
            public void run() {
                while ( continuar ) {
                    try {

                        Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
                                Integer.parseInt(Sistema.getChatServerConf().getPort()));
                        OutputStream salida = socket.getOutputStream();
                        InputStream entrada = socket.getInputStream();

                        StringBuilder mensaje = new StringBuilder();
                        mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
                        mensaje.append(ChatConstants.CHAT_END_HEADER);
                        mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND,
                                ChatConstants.COMMAND_GET_USERS));
                        mensaje.append(generateHeaderValue(ChatConstants.LABEL_FORMAT,
                                "XML"));
                        mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN,
                                clienteToken));
                        mensaje.append(ChatConstants.CHAT_END_HEADER);

                        salida.write(mensaje.toString().getBytes());
                        salida.flush();

                        MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
                        if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
                            pantallaChat.mostrarError("Error obtenido lista de usuarios.");
                        }
                        
                        String contentLength = headerMessage.getVar(ChatConstants.LABEL_CONTENT_LENGHT);

                        long contentLengthLong = -1;

                        try {
                            contentLengthLong = Long.parseLong(contentLength);
                        } catch (NumberFormatException nfe) {
                            pantallaChat.mostrarError("Error al recibir el mensaje.");
                            return;
                        }

                        StringBuilder xmlUsuarios = new StringBuilder();

                        while (xmlUsuarios.length() < contentLengthLong) {
                            int byteRead = entrada.read();
                            if (byteRead != -1) {
                                xmlUsuarios.append((char) byteRead);
                            } else {
                                pantallaChat.mostrarError("Not Enought bytes read.");
                                return;
                            }
                        }
                        
                        ArrayList <UserChat> usuarios = UserChat.generateListFromXML(new String(xmlUsuarios.toString().getBytes("latin1"), "UTF-8"));
                        pantallaChat.nuevaLista(usuarios);

                        /* Cerrar la sessión. */
                        salida.close();
                        entrada.close();
                        socket.close();
                        Thread.sleep(500);
                    } catch (Exception ex) {
                        pantallaChat.mostrarError("Hilo XML - " + ex);
                    }
                }
            }
        };
        hiloListaUsuarios.start();
    }
	
	public void descargarArchivo ( final FileChat fileChat, final File destino ) {
		Thread hiloDescargarArchivo = new Thread() {
			@Override
			public void run() {
				try {
					Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
                            Integer.parseInt(Sistema.getChatServerConf().getPort()));
                    OutputStream salida = socket.getOutputStream();
                    InputStream entrada = socket.getInputStream();
					
					/* Generar salida. */
					StringBuilder mensaje = new StringBuilder();
                    mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
                    mensaje.append(ChatConstants.CHAT_END_HEADER);
					mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND, 
							ChatConstants.COMMAND_GET_FILE));
					mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN, 
							""+clienteToken));
					mensaje.append(generateHeaderValue(ChatConstants.LABEL_FILE_NAME, 
							fileChat.getName()));
					mensaje.append(ChatConstants.CHAT_END_HEADER);
					
					salida.write(mensaje.toString().getBytes());
					salida.flush();
					
					MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
					if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
						pantallaChat.mostrarError("Error obtenido lista de usuarios.");
					}
					
					String contentLength = headerMessage.getVar(ChatConstants.LABEL_CONTENT_LENGHT);

					long contentLengthLong = -1;

					try {
						contentLengthLong = Long.parseLong(contentLength);
					} catch (NumberFormatException nfe) {
						pantallaChat.mostrarError("Error al recibir el mensaje.");
						return;
					}

					/* Borra el archivo */
					if (destino.exists() && !destino.delete()) {
						pantallaChat.mostrarError("No se pudo escribir el archivo:."+destino.getName());
						return;
					}
					
					FileOutputStream fos = new FileOutputStream(destino);
					pantallaChat.mostrarInfo("Descargando archivo: "+fileChat.getName());
					
					for ( long i=0; i<contentLengthLong; i++ ) {
						int byteRead = entrada.read();
						if ( byteRead == -1 ) {
							destino.delete();
							pantallaChat.mostrarError("Se ha cortado la conexión,\n"
									+ "por favor intente de nuevo más tarde.");
							return;
						}
						fos.write(byteRead);
					}
					
					fos.close();
					pantallaChat.mostrarInfo("Se ha terminado de descargar archivo: "+fileChat.getName());
					/* Cerrar la sessión. */
					salida.close();
					entrada.close();
					socket.close();
					Thread.sleep(500);
				} catch ( Exception e ) {
					pantallaChat.mostrarError("No se pudo enviar el mensaje: " + e);
				}
			}
		};
		hiloDescargarArchivo.start();
	}
	
	public void cerrarSesion() {
		continuar = false;
		Thread hiloCerrarSesion = new Thread() {
			@Override
			public void run() {
				try {
					Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
                            Integer.parseInt(Sistema.getChatServerConf().getPort()));
                    OutputStream salida = socket.getOutputStream();
                    InputStream entrada = socket.getInputStream();
					
					/* Generar salida. */
					StringBuilder mensaje = new StringBuilder();
                    mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
                    mensaje.append(ChatConstants.CHAT_END_HEADER);
					mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND, 
							ChatConstants.COMMAND_LOGOUT));
					mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN, 
							""+clienteToken));
					mensaje.append(ChatConstants.CHAT_END_HEADER);
					
					salida.write(mensaje.toString().getBytes());
					salida.flush();
					
					MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
                    if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
                        pantallaChat.mostrarError("El servidor no recibió el mensaje.");
                    }
                    
                    /* Cerrar la sessiòn adecudamente. */
                    salida.close();
                    entrada.close();
                    socket.close();
				} catch ( Exception ex ) {
					pantallaChat.mostrarError("No se pudo enviar el mensaje: " + ex);
				}
			}
		};
		hiloCerrarSesion.start();
	}
    
    public void enviarMensaje( final String txt ) {
        final StringBuilder mensaje = new StringBuilder();
        
        Thread hiloMensajes = new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(Sistema.getChatServerConf().getIp(),
                            Integer.parseInt(Sistema.getChatServerConf().getPort()));
                    OutputStream salida = socket.getOutputStream();
                    InputStream entrada = socket.getInputStream();

                    /* Generar salida. */
                    mensaje.append(ChatConstants.CHAT_HEADER_MAIN_COMMAND);
                    mensaje.append(ChatConstants.CHAT_END_HEADER);
                    mensaje.append(generateHeaderValue(ChatConstants.LABEL_COMMAND, ChatConstants.COMMAND_SEND_MESSAGE));
                    mensaje.append(generateHeaderValue(ChatConstants.LABEL_TO, "all"));
                    mensaje.append(generateHeaderValue(ChatConstants.LABEL_USER_TOKEN, clienteToken));
                    mensaje.append(generateHeaderValue(ChatConstants.LABEL_CONTENT_LENGHT, "" + txt.getBytes("UTF-8").length));
                    mensaje.append(ChatConstants.CHAT_END_HEADER);
                    mensaje.append(txt);

                    salida.write(mensaje.toString().getBytes());
                    salida.flush();

                    MessageHeaderParser headerMessage = MessageHeaderParser.parseMessageHeader(entrada, true);
                    if (!headerMessage.getVar(ChatConstants.CHAT_HEADER_RESPONSE_COMMAND).equals(ChatConstants.RESPONSE_OK)) {
                        pantallaChat.mostrarError("El servidor no recibió el mensaje.");
                    }
                    
                    /* Cerrar la sessiòn adecudamente. */
                    salida.close();
                    entrada.close();
                    socket.close();
                } catch (Exception e) {
                    pantallaChat.mostrarError("No se pudo enviar el mensaje: " + e);
                }
            }
        };
        hiloMensajes.start();
    }
    
    private String generateHeaderValue( String name, String val ) {
		return name + ": " + val + "\r\n";
	}
}
