package com.base.basicjwt.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.basicjwt.model.BlacklistToken;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

    boolean existsByToken(String token);
    void deleteByToken(String token);
}

