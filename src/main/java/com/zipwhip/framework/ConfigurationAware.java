package com.zipwhip.framework;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 5/5/11
 * Time: 6:38 PM
 */
public interface ConfigurationAware<TConfiguration extends Configuration> {

    void load(TConfiguration configuration);

}
