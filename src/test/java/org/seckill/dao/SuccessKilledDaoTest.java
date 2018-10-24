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

	// ע��DAOʵ��������
	/*
	 * �˴��޸�ΪAutowired��עҲ��
	 */
	@Resource
	private SuccessKilledDao successKilledDao;

	@Test
	public void testInsertSuccessKilled() throws Exception {
		long seckillId = 1003;
		long userPhone = 15921880352l;
		int ret = successKilledDao.insertSuccessKilled(seckillId, userPhone);
		if (ret > 0) {
			System.out.println("��ɱ�ɹ�");
		} else {
			System.out.println("��ɱʧ�ܣ��������ظ���ɱ��");
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
