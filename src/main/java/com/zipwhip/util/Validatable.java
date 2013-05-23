package com.zipwhip.util;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 2:47 PM
 *
 *
 */
public interface Validatable {

    /**
     *
     * @return if valid or not
     * @throws Exception will throw if it's impossible to determine if this is valid or not.
     */
    boolean validate() throws Exception;

}
