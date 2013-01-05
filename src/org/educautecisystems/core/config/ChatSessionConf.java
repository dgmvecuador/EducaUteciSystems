/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core.config;

/**
 *
 * @author dgmv
 */
public class ChatSessionConf {
	private String nickname;
	private String realName;
	
	public ChatSessionConf( String nickname, String realName ) {
		this.nickname = nickname;
		this.realName = realName;
	}
	
	public ChatSessionConf () {
		
	}

	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}
}
