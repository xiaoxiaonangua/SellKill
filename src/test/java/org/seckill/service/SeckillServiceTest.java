package org.seckill.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Pagination;
import org.seckill.entity.Seckill;
import org.seckill.exception.DuplicatedKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() throws Exception {
		Pagination page = new Pagination();
		page.setPageIndex(4);
		logger.info("page(before interceptor)==============================>{}", page);
		List<Seckill> list = seckillService.getSeckillList(page);
		logger.info("list={}", list);
		logger.info("page(after interceptor)==============================>{}", page);
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1001l;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}", seckill);
	}

	@Test
	public void testExportSeckillUrl() throws Exception {
		long id = 1000;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		logger.info("exposer={}", exposer);
	}

	@Test
	public void testExecuteSeckill() throws Exception {
		long id = 1000;
		long phone = 15967212513l;
		String md5 = "e330f37f83ab51b748746c7e47051020";
		SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);
		logger.info("result={}", execution);
	}

	@Test
	public void testExecuteSeckillComplete() throws Exception {
		long id = 1002;
		long phone = 15921880352l;
		// String md5 = "e330f37f83ab51b748746c7e47051020";
		try {
			Exposer exposer = seckillService.exportSeckillUrl(id);
			if (exposer.isExposed()) {
				logger.info("exposer={}", exposer);
				String md5 = exposer.getMd5();
				SeckillExecution execution = seckillService.executeSeckill(id, phone, md5);

			} else {
				logger.warn("exposer={}", exposer);

			}
		} catch (DuplicatedKillException dke) {
			logger.error(dke.getMessage());
		} catch (SeckillCloseException ske) {
			logger.error(ske.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Test
	public void testExecuteSeckillCompleteViaStoredProcedure() throws Exception {
		long seckillId = 1002;
		long phone = 15967212514l;
		Exposer exposer = seckillService.exportSeckillUrlOptimized(seckillId);
		if (exposer.isExposed()) {
			String md5 = exposer.getMd5();
			SeckillExecution execution = seckillService.executeSeckillStoredProcedure(seckillId, phone, md5);
			logger.info(execution.getStateInfo());
		} else {

			logger.info(exposer.toString());
		}

	}

}
