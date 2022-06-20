package com.mlf.demonongo;

import com.mlf.demonongo.entity.User;
import com.mlf.demonongo.repository.UserRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@SpringBootTest
class DemonogoApplicationTests1 {

	/**
	 * 两种方式
	 *  MongoTemplate   MongoRepository
	 */
	@Autowired
	private UserRepository userRepository;

	//mongoDB的添加操作
	@Test
	public void create() {
		User user = new User();
		user.setAge(24);
		user.setName("lucy");
		user.setEmail("12312@qq.com");
		User saveUser = userRepository.save(user);
		System.out.println(saveUser);
	}

	//mongoDB的查询操作
	@Test
	public void find() {
		List<User> allUser = userRepository.findAll();
		System.out.println(allUser);
	}

	//mongoDB的查询操作   根据id进行查询
	@Test
	public void findId() {
		User user = userRepository.findById("5ffc11da92327487dsa13434").get();
		System.out.println(user);
	}

	//mongoDB的查询操作  条件查询 =
	@Test
	public void findUserList() {
		User user = new User();
		user.setAge(24);
		user.setName("lucy");
		//Example代表封装条件查询的对象
		Example<User> userExample = Example.of(user);
		List<User> all = userRepository.findAll(userExample);
		System.out.println(all);
	}

	//mongoDB的查询操作  模糊查询
	@Test
	public void findLikeUserList() {

		//设置模糊查询匹配规则  withStringMatcher字符串的模糊查询 withIgnoreCase忽略大小写
		//固定写法
		ExampleMatcher matcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
				.withIgnoreCase(true);

		User user = new User();
		user.setAge(24);
		user.setName("y");
		//Example代表封装条件查询的对象
		Example<User> userExample = Example.of(user);
		List<User> all = userRepository.findAll(userExample);
		System.out.println(all);
	}

	//mongoDB的查询操作  分页查询
	@Test
	public void findUsersPage() {
		//设置分页
		//0代表第一页
		//PageRequest of = PageRequest.of(0, 3);
		Pageable pageable = PageRequest.of(0, 3);

		User user = new User();
		user.setName("lucy");
		//Example代表封装条件查询的对象
		Example<User> userExample = Example.of(user);
		Page<User> page = userRepository.findAll(userExample, pageable);
		System.out.println(page);
	}

	//mongoDB的修改操作
	@Test
	public void updateUser() {
		User user = userRepository.findById("5ffc11da92327487dsa13434").get();
		user.setAge(100);
		user.setName("tom");
		user.setEmail("111@qq.com");
		//没有id做添加，有id做修改
		User save = userRepository.save(user);
		System.out.println(save);
	}

	//mongoDB的查询操作  删除操作
	@Test
	public void deleteUser() {
		userRepository.deleteById("5ffc11da92327487dsa13434");
	}

}
