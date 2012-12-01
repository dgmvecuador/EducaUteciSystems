/*
 *  AtenderClienteServidor.java
 *  Copyright (C) 2012  Diego Estévez <dgmvecuador@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.educautecisystems.core.chat.elements;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author dgmv
 */
public class UserChat {
	private int id;
	private String realName;
	private String nickName;
	
	public UserChat() {
		this.id = 0;
		this.realName = "";
		this.nickName = "";
	}
	
	public UserChat( int id, String realName, String nickName ) {
		this.id = id;
		this.realName = realName;
		this.nickName = nickName;
	}
	
	public static String generateXMLFromList ( ArrayList<UserChat> users ) {
		Document xmlDocument = new Document();
		
		Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
		Element root = new Element("users", baseNamespace);
		
		for ( UserChat user:users ) {
			Element userXml = new Element("user", baseNamespace);
			
			userXml.addContent(new Element("id").setText(""+user.getId()));
			userXml.addContent(new Element("real_name").setText(user.getRealName()));
			userXml.addContent(new Element("nickname").setText(user.getNickName()));
			
			root.addContent(userXml);
		}
		
		xmlDocument.setRootElement(root);
		XMLOutputter xmlOutputter = new XMLOutputter();
		
		return xmlOutputter.outputString(xmlDocument);
	}
	
	public static ArrayList<UserChat> generateListFromXML( String xml ) {
		StringReader xmlSR = new StringReader(xml);
		ArrayList<UserChat> response = new ArrayList<UserChat>();
		
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document documentXML = builder.build(xmlSR);
			
			Element root = documentXML.getRootElement();
			Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
			
			/* Get all users */
			List<Element> users = root.getChildren("user", baseNamespace);
			
			for ( Element user:users ) {
				String id_string = user.getChildText("id");
				int id = 0;
				String realName = user.getChildText("real_name");
				String nickName = user.getChildText("nickname");
				
				try {
					id = Integer.parseInt(id_string);
				} catch ( NumberFormatException nfe ) {
					return null;
				}
				
				UserChat newUserChat = new UserChat(id, realName, nickName);
				response.add(newUserChat);
			}
		} catch( JDOMException jdome ) {
			return null;
		} catch ( IOException ioe ) {
			return null;
		}
		
		return response;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
