package com.mlf.demonongo;

import com.mlf.demonongo.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class DemonogoApplicationTests {

	/**
	 * 两种方式
	 *  MongoTemplate   MongoRepository
	 */
	@Autowired
	private MongoTemplate mongoTemplate;

	//mongoDB的添加操作
	@Test
	public void create() {
		User user = new User();
		user.setAge(20);
		user.setName("张三");
		user.setEmail("123@qq.com");
		User user1 = mongoTemplate.insert(user);
		System.out.println(user1);
	}

	//mongoDB的查询操作
	@Test
	public void find() {
		List<User> all = mongoTemplate.findAll(User.class);
		System.out.println(all);
	}

	//mongoDB的查询操作   根据id进行查询
	@Test
	public void findId() {
		User userId = mongoTemplate.findById("5ffc11da92327487dsa13434",User.class);
		System.out.println(userId);
	}

	//mongoDB的查询操作  条件查询 =
	@Test
	public void findUserList() {
		//Query 就是来构建条件的对象  相当于mysql里面的 where name = "张三"
		Query query = new Query(Criteria.where("name").is("张三").and("age").is(20));
		List<User> users = mongoTemplate.find(query, User.class);
		System.out.println(users);
	}

	//mongoDB的查询操作  模糊查询
	@Test
	public void findLikeUserList() {
		String name = "三";
		String regex  = String.format("%s%s%s", "^.*", name, ".*$");
		//Pattern  构建匹配规则  相当于正则表达式
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		//Query 就是来构建条件的对象  相当于mysql里面的 where name like "张三"
		Query query = new Query(Criteria.where("name").regex(pattern));
		List<User> users = mongoTemplate.find(query, User.class);
		System.out.println(users);
	}

	//mongoDB的查询操作  分页查询   带条件的分页查询
	@Test
	public void findUsersPage() {
		int pageNo = 1;  //当前页
		int pageSize = 10;	//每一页的记录数

		String name = "三";
		//条件构建
		String regex = String.format("%s%s%s", "^.*", name, ".*$");
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Query query = new Query(Criteria.where("name").regex(pattern));
		//分页构建
		//查询记录数
		long count = mongoTemplate.count(query, User.class);
		//分页  skip表示跳过    开始位置的计算公式：(当前页-1) * 每页的记录数
		//							limit(pageSize) pageSize以这个记录数进行分页
		List<User> users = mongoTemplate.find(
				query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
		System.out.println(count);
		System.out.println(users);

	}

	//mongoDB  修改操作
	@Test
	public void updateUser() {
		//根据查询
		User user = mongoTemplate.findById("5ffc11da92327487dsa13434", User.class);
		//设置值
		user.setName("李四");
		user.setAge(19);
		user.setEmail("000@qq.com");
		Query query = new Query(Criteria.where("_id").is(user.getId()));

		Update update = new Update();
		update.set("name",user.getName());
		update.set("age",user.getAge());
		update.set("email",user.getEmail());
		//数据库入库  调用方法实现修改
		UpdateResult upper = mongoTemplate.upsert(query, update, User.class);
		//getModifiedCount 表示影响的行数  1，修改了 0 未修改
		long modifiedCount = upper.getModifiedCount();
		System.out.println(modifiedCount);
	}

	//mongoDB的查询操作  删除操作
	@Test
	public void deleteUser() {
		Query query = new Query(Criteria.where("_id").is("5ffc11da92327487dsa13434"));
		DeleteResult remove = mongoTemplate.remove(query, User.class);
		//操作是否成功 1表示影响了，0表示未影响
		long deletedCount = remove.getDeletedCount();
		System.out.println(deletedCount);
	}


}
