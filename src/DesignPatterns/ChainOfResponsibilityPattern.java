package DesignPatterns;

public class ChainOfResponsibilityPattern {
    public static void main(String[] args) {
        Logger logger = new ErrorLogger(new WarningLogger(new InfoLogger(null)));
        logger.log(Logger.ERROR, "Something went wrong");
        logger.log(Logger.WARN, "Something went wrong");
        logger.log(Logger.INFO, "Something went wrong");
        logger.log(5, "Something went wrong");
    }
}

class Logger {
    public static int INFO = 1;
    public static int WARN = 2;
    public static int ERROR = 3;
    private final Logger nxtLogger;
    Logger(Logger nxtLogger) {
        this.nxtLogger = nxtLogger;
    }

    public void log(int lvl, String message) {
        if(nxtLogger != null) nxtLogger.log(lvl, message);
        else System.out.println(message);
    }
}

class InfoLogger extends Logger {

    InfoLogger(Logger nxtLogger) {
        super(nxtLogger);
    }

    public void log(int lvl, String message) {
        if(lvl == Logger.INFO) System.out.println("Info: "+message);
        else super.log(lvl, message);
    }
}

class WarningLogger extends Logger {
    WarningLogger(Logger nxtLogger) {
        super(nxtLogger);
    }

    public void log(int lvl, String message) {
        if(lvl == Logger.WARN) System.out.println("Warning: "+message);
        else super.log(lvl, message);
    }
}

class ErrorLogger extends Logger {
    ErrorLogger(Logger nxtLogger) {
        super(nxtLogger);
    }

    public void log(int lvl, String message) {
        if(lvl == Logger.ERROR) System.out.println("Error: "+message);
        else super.log(lvl, message);
    }
}
