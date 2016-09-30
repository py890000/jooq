package org.wefine.spring.jpa.jooq;

import org.jooq.ResultQuery;

public interface JooqQueryExecutor<T> {

    T findOne(ResultQuery q);
}
