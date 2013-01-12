/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core;

/**
 *
 * @author dgmv
 */
public class ConfBaseDeDatos {

    private String host;
    private String port;
    private String user;
    private String password;
    private String esquema;

    public ConfBaseDeDatos() {
        host = null;
        port = null;
        user = null;
        password = null;
        esquema = null;
    }

    public ConfBaseDeDatos(String host, String port, String user, String password, String esquema) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.esquema = esquema;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
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

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the esquema
     */
    public String getEsquema() {
        return esquema;
    }

    /**
     * @param esquema the esquema to set
     */
    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }
}
