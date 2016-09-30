package org.wefine.spring.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wefine.spring.jpa.entity.User;
import org.wefine.spring.jpa.jooq.JooqQueryExecutor;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JooqQueryExecutor<User> {

    User findUserByName(String name);
}
