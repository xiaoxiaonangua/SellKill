package org.seckill.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SuccessKilledDaoTest {

	// 注入DAO实现类依赖
	/*
	 * 此处修改为Autowired标注也可
	 */
	@Resource
	private SuccessKilledDao successKilledDao;

	@Test
	public void testInsertSuccessKilled() throws Exception {
		long seckillId = 1003;
		long userPhone = 15921880352l;
		int ret = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		if (ret > 0) {
			System.out.println("秒杀成功");
		} else {
			System.out.println("秒杀失败（不允许重复秒杀）");
		}

	}

	@Test
	public void testQueryByIdWithSeckill() throws Exception {
		long seckillId = 1003;
		long userPhone = 15921880352l;
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);

		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill());
	}
}
