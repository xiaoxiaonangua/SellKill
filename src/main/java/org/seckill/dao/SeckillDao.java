package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

public interface SeckillDao {

	/*
	 * 减库存
	 * 
	 * @param seckillId
	 * 
	 * @param killTime
	 * 
	 * @return 如果影响行数大于等于1，表示更新的记录行数
	 */
	int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

	/*
	 * 根据id查询秒杀对象
	 * 
	 * @param sdckillId
	 * 
	 * @return
	 */
	Seckill queryById(@Param("seckillId") long seckillId);

	/*
	 * 查询秒杀商品列表，分页
	 *
	 * 此处有一个约定：Map中必须存在一个 键名为 'pagination' 的 org.seckill.entity.Pagination
	 * 的实体来存放分页信息 如果要使用 @Param来注解这个Map参数,那么它的名称必须与相关的xml中的方法ID同名，也即与本方法同名
	 * 
	 * @param queryByPage
	 * @return
	 */
	List<Seckill> queryByPage(@Param("queryByPage") Map<String, Object> queryByPage);

	/*
	 * 使用存储过程 执行秒杀
	 */
	void seckillViaStoredProcedure(Map<String, Object> paramMap);

}
