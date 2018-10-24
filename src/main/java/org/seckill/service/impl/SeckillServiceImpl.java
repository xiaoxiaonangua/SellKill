package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.SeckillRedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Pagination;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.DuplicatedKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *
 */
@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String salt = "ksui^%%$#%_)()(BHGYAX5ffgh!~@$?w";

	@Autowired
	private SeckillRedisDao seckillRedisDao;

	@Autowired
	private SeckillDao seckillDao;

	@Autowired
	private SuccessKilledDao successKilledDao;

	@Override
	public List<Seckill> getSeckillList(Pagination page) {
		Map<String, Object> query = new HashMap<String, Object>();
		// Pagination page = new Pagination();
		// page.setPageIndex(pageIndex);
		query.put("pagination", page);
		return seckillDao.queryByPage(query);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if (seckill == null) {
			return new Exposer(false, seckillId);
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();

		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	@Override
	public Exposer exportSeckillUrlOptimized(long seckillId) {
		Seckill seckill = seckillRedisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill == null) {
				return new Exposer(false, seckillId);
			} else {
				String result = seckillRedisDao.putSeckill(seckill);
			}
		}

		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		Date nowTime = new Date();

		if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);

	}

	/*
	 * ʹ��ע��������񷽷����ŵ㣺 1.�����ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
	 * 2.��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ�������������������RPC/HTTP���󣬻��߰��뵽���񷽷��� 3.�������еķ�������Ҫ����,��
	 * ����һ���޸Ĳ�����ֻ����������Ҫ������ơ�
	 * 
	 * 
	 */

	@Transactional
	@Override
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, DuplicatedKillException, SeckillCloseException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		try {
			// ִ����ɱҵ���߼�������� + ��¼������Ϊ
			Date killTime = new Date();
			int updateCount = seckillDao.reduceNumber(seckillId, killTime);
			if (updateCount <= 0) {
				// û�и��µ���¼
				// ��ɱ����
				throw new SeckillCloseException("seckill is closed");
			} else {
				// ��¼������Ϊ
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				// Ψһ��seckillId,userPhone
				if (insertCount <= 0) {
					// �ظ���ɱ
					throw new DuplicatedKillException("duplicated seckill");
				} else {
					// ��ɱ�ɹ�
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException ce) {
			throw ce;
		} catch (DuplicatedKillException de) {
			throw de;
		} catch (SeckillException se) {
			throw se;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// ���б������쳣ת��Ϊ�������쳣
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + salt;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.seckill.service.SeckillService#executeSeckillOptimized(long, long,
	 * java.lang.String)
	 * 
	 * 
	 * ������ִ��˳���Ȳ��룬�ټ���棬�Լ��ٳ�������ʱ��
	 */
	@Transactional
	@Override
	public SeckillExecution executeSeckillOptimized(long seckillId, long userPhone, String md5)
			throws SeckillException, DuplicatedKillException, SeckillCloseException {

		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");
		}
		try {
			// ִ����ɱҵ���߼�������� + ��¼������Ϊ
			Date killTime = new Date();

			// ��¼������Ϊ
			int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
			// Ψһ��seckillId,userPhone
			if (insertCount <= 0) {
				// �ظ���ɱ
				throw new DuplicatedKillException("duplicated seckill");
			} else {

				int updateCount = seckillDao.reduceNumber(seckillId, killTime);
				if (updateCount <= 0) {
					// û�и��µ���¼
					// ��ɱ����
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��ɱ�ɹ�
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
				}

			}

		} catch (SeckillCloseException ce) {
			throw ce;
		} catch (DuplicatedKillException de) {
			throw de;
		} catch (SeckillException se) {
			throw se;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// ���б������쳣ת��Ϊ�������쳣
			throw new SeckillException("seckill inner error:" + e.getMessage());
		}
	}

	/*
	 * ִ����ɱ(ʹ�ô洢����)
	 * 
	 * @param seckillId
	 * 
	 * @param userPhone
	 * 
	 * @param md5
	 */
	@Override
	public SeckillExecution executeSeckillStoredProcedure(long seckillId, long userPhone, String md5) {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", killTime);
		map.put("result", null);
		try {
			// ִ�д洢����
			seckillDao.seckillViaStoredProcedure(map);
			// ��ȡresult
			int result = MapUtils.getInteger(map, "result", -2);
			if (result == 1) {
				SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, sk);
			} else {
				return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
	}
}
