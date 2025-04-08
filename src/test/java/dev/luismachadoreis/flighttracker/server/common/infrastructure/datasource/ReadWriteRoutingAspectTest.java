package dev.luismachadoreis.flighttracker.server.common.infrastructure.datasource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Annotation;
import static org.assertj.core.api.Assertions.assertThat;

class ReadWriteRoutingAspectTest {

    private ReadWriteRoutingAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new ReadWriteRoutingAspect();
        DbContextHolder.clearDbType();
    }

    @Test
    void shouldSetReadContext_ForReadOnlyTransaction() {
        // Given
        Transactional tx = new TestTransactional(true);

        // When
        aspect.before(tx);

        // Then
        assertThat(DbContextHolder.getDbType()).isEqualTo(DbContextHolder.READ);
    }

    @Test
    void shouldSetWriteContext_ForWriteTransaction() {
        // Given
        Transactional tx = new TestTransactional(false);

        // When
        aspect.before(tx);

        // Then
        assertThat(DbContextHolder.getDbType()).isEqualTo(DbContextHolder.WRITE);
    }

    @Test
    void shouldClearContext_AfterTransaction() {
        // Given
        Transactional tx = new TestTransactional(true);
        aspect.before(tx);
        assertThat(DbContextHolder.getDbType()).isNotNull();

        // When
        aspect.after(tx);

        // Then
        assertThat(DbContextHolder.getDbType()).isNull();
    }

    // Helper class to create Transactional annotations for testing
    private static class TestTransactional implements Transactional {
        private final boolean readOnly;

        TestTransactional(boolean readOnly) {
            this.readOnly = readOnly;
        }

        @Override
        public Class<? extends Throwable>[] rollbackFor() { return new Class[0]; }

        @Override
        public Class<? extends Throwable>[] noRollbackFor() { return new Class[0]; }

        @Override
        public String[] rollbackForClassName() { return new String[0]; }

        @Override
        public String[] noRollbackForClassName() { return new String[0]; }

        @Override
        public String value() { return ""; }

        @Override
        public String transactionManager() { return ""; }

        @Override
        public String[] label() { return new String[0]; }

        @Override
        public Propagation propagation() { return Propagation.REQUIRED; }

        @Override
        public Isolation isolation() { return Isolation.DEFAULT; }

        @Override
        public int timeout() { return -1; }

        @Override
        public String timeoutString() { return ""; }

        @Override
        public boolean readOnly() { return readOnly; }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Transactional.class;
        }
    }
} 