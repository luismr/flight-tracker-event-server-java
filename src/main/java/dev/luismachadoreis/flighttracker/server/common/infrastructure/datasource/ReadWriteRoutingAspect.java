package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.transaction.annotation.Transactional;

/**
 * Aspect for read-write routing.
 * 
 * @author Luis Machado Reis
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = "app.read-write-routing", name = "enabled", havingValue = "true")
public class ReadWriteRoutingAspect {

    @Before("@annotation(tx)")
    public void before(Transactional tx) {
        if (tx.readOnly()) {
            DbContextHolder.setDbType(DbContextHolder.READ);
        } else {
            DbContextHolder.setDbType(DbContextHolder.WRITE);
        }
    }

    @After("@annotation(tx)")
    public void after(Transactional tx) {
        DbContextHolder.clearDbType();
    }

}
