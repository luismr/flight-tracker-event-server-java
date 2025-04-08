package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

/**
 * Thread-local holder for database context.
 * 
 * @author Luis Machado Reis
 */
public class DbContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static final String READ = "READ";
    public static final String WRITE = "WRITE";

    public static void setDbType(String dbType) {
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }

}
