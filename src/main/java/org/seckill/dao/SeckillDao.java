package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;


/**
 *
 */
public interface SeckillDao {

	/*
	 * �����
	 * 
	 * @param seckillId
	 * 
	 * @param killTime
	 * 
	 * @return ���Ӱ���������ڵ���1����ʾ���µļ�¼����
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

	/*
	 * ����id��ѯ��ɱ����
	 * 
	 * @param sdckillId
	 * 
	 * @return
	 */
	Seckill queryById(@Param("seckillId") long seckillId);

	/*
	 * ��ѯ��ɱ��Ʒ�б���ҳ
	 *
	 * �˴���һ��Լ����Map�б������һ�� ����Ϊ 'pagination' �� org.seckill.entity.Pagination
	 * ��ʵ������ŷ�ҳ��Ϣ ���Ҫʹ�� @Param��ע�����Map����,��ô�������Ʊ�������ص�xml�еķ���IDͬ����Ҳ���뱾����ͬ��
	 * 
	 * @param queryByPage
	 * @return
	 */
	List<Seckill> queryByPage(@Param("queryByPage") Map<String, Object> queryByPage);

	/*
	 * ʹ�ô洢���� ִ����ɱ
	 */
	void seckillViaStoredProcedure(Map<String, Object> paramMap);

}
