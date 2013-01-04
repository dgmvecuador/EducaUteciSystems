/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core.config;

/**
 *
 * @author dgmv
 */
public class ChatServerConf {
	private String ip;
	private String port;
	
	public ChatServerConf( String ip, String port ) {
		this.ip =	ip;
		this.port = port;
	}
	
	public ChatServerConf () {
		
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
}
