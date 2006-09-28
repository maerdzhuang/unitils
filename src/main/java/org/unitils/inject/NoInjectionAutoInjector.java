package org.unitils.inject;

import java.util.Map;

/**
 * @author Filip Neven
 */
public class NoInjectionAutoInjector implements AutoInjector {

    public void autoInject(Object object, Map<String, Object> toInject) {
        // This implementation doesn't do autoinjection
    }

}