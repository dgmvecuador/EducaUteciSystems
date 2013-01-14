/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author dgmv
 */
public class FileChat {
	private String name;
	private long size;
	private boolean hidden;
	
	public FileChat( String name, long size, boolean hidden ) {
		this.name = name;
		this.size = size;
		this.hidden = hidden;
	}
	
	public FileChat() {
		this.name = "";
		this.size = -1;
		this.hidden = false;
	}
	
	public static String generateXMLFromList( ArrayList <FileChat> files ) {
		Document document = new Document();
		
		Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
		Element root = new Element("files", baseNamespace);
		document.addContent(root);
		
		for ( FileChat fileChat:files ) {
			Element fileXML = new Element("file", baseNamespace);
			
			/* Añadir elementos. */
			fileXML.addContent(new Element("name").setText(fileChat.getName()));
			fileXML.addContent(new Element("size").setText(""+fileChat.getSize()));
			fileXML.addContent(new Element("hidden").setText(""+fileChat.isHidden()));
			
			root.addContent(fileXML);
		}
		
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		
		return outputter.outputString(document);
	}
	
	public static ArrayList <FileChat> generateListFromXML ( String xml ) {
		ArrayList <FileChat> files = new ArrayList<FileChat>();
		StringReader xmlRL = new StringReader(xml);
		
		SAXBuilder builder = new SAXBuilder();
		
		try {
			Document document = builder.build(xmlRL);
			
			Element root = document.getRootElement();
			Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
			
			List <Element> filesE = root.getChildren("file", baseNamespace);
			
			for ( Element file:filesE ) {
				FileChat fileChat = new FileChat();
				
				fileChat.setName(file.getChildText("name"));
				fileChat.setSize(Long.parseLong(file.getChildText("size")));
				
				String hiddenElement = file.getChildText("hidden");
				
				if ( hiddenElement.equals("true") ) {
					fileChat.setHidden(true);
				} else if ( hiddenElement.equals("false") ) {
					fileChat.setHidden(false);
				} else {
					return null;
				}
				
				files.add(fileChat);
			}
		} catch ( IOException ioe ) {
			System.out.println("I/O Problem: "+ioe);
			return null;
		} catch ( JDOMException jdome ) {
			System.out.println("JDOM Problem: "+jdome);
			return null;
		} catch ( NumberFormatException nfe ) {
			System.out.println("Incorrect number format parsing file size in XML.");
			return null;
		}
		
		return files;
	}
	
	public String toString () {
		return name + " ("+size+")";
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}	
}
