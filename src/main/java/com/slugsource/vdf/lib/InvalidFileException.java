/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.slugsource.vdf.lib;

/**
 *
 * @author Nathan Fearnley
 */
public class InvalidFileException extends Exception
{

    /**
     * Constructs an instance of
     * <code>InvalidFileException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidFileException(String msg)
    {
        super(msg);
    }
}