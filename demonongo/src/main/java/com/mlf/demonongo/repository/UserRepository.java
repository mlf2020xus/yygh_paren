package com.mlf.demonongo.repository;

import com.mlf.demonongo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2021/11/25.
 */
@Repository
public interface UserRepository extends MongoRepository<User,String> {


}
