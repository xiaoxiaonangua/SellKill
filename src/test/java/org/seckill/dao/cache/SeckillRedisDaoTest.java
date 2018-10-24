package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-30
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillRedisDaoTest {

	@Autowired
	private SeckillRedisDao seckillRedisDao;

	@Autowired
	private SeckillDao seckillDao;

	@Test
	public void test() throws Exception {

	}

	@Test
	public void testSeckill() throws Exception {
		long seckillId = 1035;
		Seckill seckill = seckillRedisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill != null) {
				String result = seckillRedisDao.putSeckill(seckill);
				System.out.println("result to put into redis => " + result);
				seckill = seckillRedisDao.getSeckill(seckillId);
				System.out.println("seckill from redis => " + seckill);
			}
		} else {
			System.out.println("seckill from redis dirrectly => " + seckill);

		}
	}
}
