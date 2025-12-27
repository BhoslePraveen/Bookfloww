package org.praveenit.bookfloww.entity;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class CustomerIdGenerator implements IdentifierGenerator {
    private static final String SEQUENCE_SQL = "SELECT nextval('user_customer_seq')";

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Long seq = ((Number) session
                .createNativeQuery(SEQUENCE_SQL)
                .getSingleResult())
                .longValue();

        return "CUST_" + String.format("%05d", seq);
    }
}
