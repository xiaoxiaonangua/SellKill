package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Pagination;
import org.seckill.entity.Seckill;
import org.seckill.exception.DuplicatedKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *          业务接口：站在“使用者”角度 设计接口 三个方面： 1.方法定义粒度 2.参数（简洁） 3.返回类型(return 类型友好 )
 */
public interface SeckillService {

	/*
	 * 查询所有记录
	 * 
	 * @param pageIndex
	 */
	List<Seckill> getSeckillList(Pagination page);

	/*
	 * 查询单个秒杀记录
	 * 
	 * @param seckillId
	 */
	Seckill getById(long seckillId);

	/*
	 * 秒杀开启时输出秒杀接口地址 否则输出系统时间和秒杀时间
	 * 
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);

	/*
	 * 秒杀开启时输出秒杀接口地址 否则输出系统时间和秒杀时间(使用了redis)
	 */

	Exposer exportSeckillUrlOptimized(long seckillId);

	/*
	 * 执行秒杀
	 * 
	 * @param seckillId
	 * 
	 * @param userPhone
	 * 
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, DuplicatedKillException, SeckillCloseException;

	/*
	 * 执行秒杀
	 * 
	 * @param seckillId
	 * 
	 * @param userPhone
	 * 
	 * @param md5
	 */
	SeckillExecution executeSeckillOptimized(long seckillId, long userPhone, String md5)
			throws SeckillException, DuplicatedKillException, SeckillCloseException;

	/*
	 * 执行秒杀(使用存储过程)
	 * 
	 * @param seckillId
	 * 
	 * @param userPhone
	 * 
	 * @param md5
	 */
	SeckillExecution executeSeckillStoredProcedure(long seckillId, long userPhone, String md5);

}
