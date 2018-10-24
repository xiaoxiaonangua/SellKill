package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *          ��װ��ɱִ�н��
 */
public class SeckillExecution {

	private long seckillId;

	// ��ɱִ�н��״̬
	private int state;

	// ��ɱִ�н��״̬˵��
	private String stateInfo;

	// ��ɱ�ɹ�����
	private SuccessKilled successKilled;

	public long getSeckillId() {
		return seckillId;
	}

	public void setSeckillId(long seckillId) {
		this.seckillId = seckillId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public SuccessKilled getSuccessKilled() {
		return successKilled;
	}

	public void setSuccessKilled(SuccessKilled successKilled) {
		this.successKilled = successKilled;
	}

	public SeckillExecution(long seckillId, SeckillStateEnum state) {
		// super();
		this.seckillId = seckillId;
		this.state = state.getState();
		this.stateInfo = state.getStateInfo();
	}

	public SeckillExecution(long seckillId, SeckillStateEnum state, SuccessKilled successKilled) {
		// super();
		this.seckillId = seckillId;
		this.state = state.getState();
		this.stateInfo = state.getStateInfo();
		this.successKilled = successKilled;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "SeckillExecution={seckillId=" + seckillId + ",	state=" + state + ",	stateInfo='" + stateInfo
				+ "',	successKilled(" + successKilled + ")}";
	}
}
