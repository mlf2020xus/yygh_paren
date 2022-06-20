package com.mlf.demonongo.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Administrator on 2021/11/25.
 * @Document("User")   User代表数据库里面的集合（表），操作这个对象相当于操作mongodb里面的集合（表）
 */
@Data
@Document("User")
public class User {

    /**
     * 两种方式
     *  MongoTemplate   MongoRepository
     */
    @Autowired
   private MongoTemplate mongoTemplate;
    // @Id该注解表示  _id 的生成策略
    @Id
    private String id;
    private String name;
    private Integer age;
    private String email;
    private String createDate;
}
