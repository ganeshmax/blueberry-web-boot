package com.blueberry.framework.validation

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * Validation implementation for Url
 *
 * @author Ganeshji Marwaha
 * @since 9/8/14
 */
class UrlValidator implements ConstraintValidator<URL, String> {

    String protocol
    String host
    int port

    public void initialize(URL url) {
        this.protocol = url.protocol()
        this.host = url.host()
        this.port = url.port()
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.length() == 0) {
            return true
        }

        URL url
        try {
            // Transforms it to a java.net.URL to see if it has a valid format
            url = new URL(value)
        } catch (MalformedURLException e) {
            return false
        }

        // Checks if the protocol attribute has a valid value
        if (protocol != null && protocol.length() > 0 && !url.getProtocol().equals(protocol)) {
            return false
        }

        if (host != null && host.length() > 0 && !url.getHost().startsWith(host)) {
            return false
        }

        if (port != -1 && url.getPort() != port) {
            return false
        }

        return true
    }
}
