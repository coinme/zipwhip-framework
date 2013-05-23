package com.zipwhip.util;

/**
 * Created by IntelliJ IDEA.
 * User: gregm
 * Date: 12/7/11
 * Time: 3:44 PM
 *
 * Fake one so you can satisfy your generics without having to create a class of your own.
 */
public class VoidValidatable implements Validatable {

    private VoidValidatable() {

    }

    @Override
    public boolean validate() throws Exception {
        return true;
    }

}
