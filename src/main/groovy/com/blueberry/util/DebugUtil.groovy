package com.blueberry.util

import org.codehaus.groovy.runtime.StackTraceUtils

/**
 * TODO: Document Me
 *
 * @author Ganeshji Marwaha
 * @since 9/11/14
 */
public class DebugUtil {

    static String currentMethod(){
        def marker = new Throwable()
        return "<<" + StackTraceUtils.sanitize(marker).stackTrace[1].methodName + ">>"
    }
}
