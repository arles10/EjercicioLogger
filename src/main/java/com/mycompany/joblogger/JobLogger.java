package com.mycompany.joblogger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author @rles
 */
public class JobLogger {

    private static boolean logToFile = true;//false
    private static boolean logToConsole = false;
    private static boolean logMessage = false;//false
    private static boolean logWarning = true;
    private static boolean logError = true;
    private static boolean logToDatabase = true;//false
    private boolean initialized = false;
    private static Map dbParams;
    private static Logger logger = Logger.getLogger(JobLogger.class.getName());
    
    private static int t = 0;
 
    public JobLogger(boolean logToFileParam,
            boolean logToConsoleParam,
            boolean logToDatabaseParam,
            boolean logMessageParam,
            boolean logWarningParam,
            boolean logErrorParam,
            Map dbParamsMap) {
        logger = Logger.getLogger("MyLog");
        logError = logErrorParam;
        logMessage = logMessageParam;
        logWarning = logWarningParam;
        logToDatabase = logToDatabaseParam;
        logToFile = logToFileParam;
        logToConsole = logToConsoleParam;
        dbParams = dbParamsMap;
    }

    public static void logMessages(String messageText, boolean message, boolean warning, boolean error) throws Exception {
        messageText.trim();
        if (messageText == null || messageText.length() == 0) {
            return;
        }
        if (!logToConsole && !logToFile && !logToDatabase) {
            throw new Exception("Invalid configuration @rles");
        }
        if ((!logError && !logMessage && !logWarning) || (!message && !warning && !error)) {
            throw new Exception("Error or Warning or Message must be specified @rles");
        }

        //Conexion a la bd
        Statement stmt = conectar().createStatement();

        //File logFile = new File(dbParams.get("logFileFolder") + "/logFile.txt");
        File logFile = new File("logFile.txt");
        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        //FileHandler fh = new FileHandler(dbParams.get("logFileFolder") + "/logFile.txt");
        FileHandler fh = new FileHandler("C:/Prueba/logFile.txt", true);
        ConsoleHandler ch = new ConsoleHandler();       
                
        String typeError = "error ";
        dateFormat(messageText, typeError, error, logError, 2);
        
        String typeWarning = "warning ";
        dateFormat(messageText, typeWarning, warning, logWarning, 3);
        
        String typeMessage = "message ";
        dateFormat(messageText, typeMessage, message, logMessage, 1);

        loggerFile(messageText, message, warning, error, fh);
        loggerConsole(messageText, message, warning, error, ch);
        loggerDatabase(messageText, message, warning, error, stmt, t);

    }
    
    public static Connection conectar() throws ClassNotFoundException, SQLException {

        try {            
            Connection connection = null;
 
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/prueba", "root", "admin");
            System.out.println("Conexión realizada");                          

            return connection;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public static void dateFormat(String messageText, String auxType, boolean type, boolean logType, int varT){
        
        String aux = ""; 
        
        if (type && logType) {                                   
            aux = aux + auxType + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()) + " " + messageText;            
            t = varT;
        }
    }  

    public static void loggerFile(String messageText, boolean message, boolean warning, boolean error, FileHandler fh) throws Exception {
        
        if (logToFile) {
            logger.addHandler(fh);
            logger.log(Level.INFO, messageText);            
        }
    }
    
    public static void loggerConsole(String messageText, boolean message, boolean warning, boolean error, ConsoleHandler ch) throws Exception {

        if (logToConsole) {
            logger.addHandler(ch);
            logger.log(Level.INFO, messageText);            
        }
    }
    
    public static void loggerDatabase(String messageText, boolean message, boolean warning, boolean error, Statement stmt, int t) throws Exception {
        
        if (logToDatabase) {            
            stmt.executeUpdate("insert into Log values(" + message + ", " + String.valueOf(t) + ");");            
        }
    }

    public static void main(String[] args) throws Exception {
        String messageText = "Hola Mundo";
        boolean message = true;
        boolean warning = true;
        boolean error = false;

        logMessages(messageText, message, warning, error);
    }
}
