package com.mycode.pathpilotserver.user.repository;

import com.mycode.pathpilotserver.user.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo  extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths={"driver", "customer"}, type = EntityGraph.EntityGraphType.LOAD)
    User findByUsername(String username);

    @EntityGraph(attributePaths={"driver", "customer"}, type = EntityGraph.EntityGraphType.LOAD)
    User findByEmail(String email);
}
