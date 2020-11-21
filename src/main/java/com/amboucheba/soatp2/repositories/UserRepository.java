package com.amboucheba.soatp2.repositories;

import com.amboucheba.soatp2.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
