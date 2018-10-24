package org.seckill.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/*
 * 
 * ����spring��junit���ϣ�junit����ʱ���� springIOC����
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml" })
public class SeckillDaoTest {

	// ע��DAOʵ��������
	/*
	 * �˴��޸�ΪAutowired��עҲ��
	 */
	@Resource
	private SeckillDao seckillDao;

	@Test
	public void testReduceNumber() throws Exception {
		long seckillId = 1003;
		Date killTime = new Date();
		// long userPhone = 15921880352l;
		int ret = seckillDao.reduceNumber(seckillId, killTime);
		if (ret > 0) {
			System.out.println("�����ɹ�");
		} else {
			System.out.println("�����ʧ��");
		}

	}

	@Test
	public void testQueryById() throws Exception {

		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
		/*
		 * 1000Ԫ��ɱ iphone6 Seckill{seckillId=1000, name='1000Ԫ��ɱ iphone6', number=100,
		 * startTime=Sun Jul 01 00:00:00 CST 2018, endTime=Mon Jul 02 00:00:00 CST 2018,
		 * createTime=Wed Jun 27 20:34:44 CST 2018}
		 */
	}

	@Test
	public void queryByPage() throws Exception {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("pagination", new org.seckill.entity.Pagination());
		List<Seckill> seckills = seckillDao.queryByPage(paraMap);
		for (Seckill seckill : seckills) {
			System.out.println(seckill);
		}
	}
}
