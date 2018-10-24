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
 *          ҵ��ӿڣ�վ�ڡ�ʹ���ߡ��Ƕ� ��ƽӿ� �������棺 1.������������ 2.��������ࣩ 3.��������(return �����Ѻ� )
 */
public interface SeckillService {

	/*
	 * ��ѯ���м�¼
	 * 
	 * @param pageIndex
	 */
	List<Seckill> getSeckillList(Pagination page);

	/*
	 * ��ѯ������ɱ��¼
	 * 
	 * @param seckillId
	 */
	Seckill getById(long seckillId);

	/*
	 * ��ɱ����ʱ�����ɱ�ӿڵ�ַ �������ϵͳʱ�����ɱʱ��
	 * 
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);

	/*
	 * ��ɱ����ʱ�����ɱ�ӿڵ�ַ �������ϵͳʱ�����ɱʱ��(ʹ����redis)
	 */

	Exposer exportSeckillUrlOptimized(long seckillId);

	/*
	 * ִ����ɱ
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
	 * ִ����ɱ
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
	 * ִ����ɱ(ʹ�ô洢����)
	 * 
	 * @param seckillId
	 * 
	 * @param userPhone
	 * 
	 * @param md5
	 */
	SeckillExecution executeSeckillStoredProcedure(long seckillId, long userPhone, String md5);

}
