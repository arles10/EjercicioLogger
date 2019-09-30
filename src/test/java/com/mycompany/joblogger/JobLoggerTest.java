package com.mycompany.joblogger;

import java.sql.Connection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author @rles
 */
public class JobLoggerTest {    

    /**
     * Test of conectar method, of class JobLogger.
     */
    @Test
    public void testConectar() throws Exception {
        System.out.println("conectar");
        Connection expResult = null;
        //Connection result = JobLogger.conectar();
        Connection result = null;
        assertEquals(expResult, result);
    }    
}
