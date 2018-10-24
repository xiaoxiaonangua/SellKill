package org.seckill.enums;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *
 */
public enum SeckillStateEnum {

	SUCCESS(1, "��ɱ�ɹ�"), 
	END(0, "��ɱ����"), 
	DUPLICATED_KILL(-1, "�ظ���ɱ"), 
	INNER_ERROR(-2, "ϵͳ�쳣"), 
	DATA_REWRITE(-3, "���ݴ۸�");

	private int state;

	private String stateInfo;

	private SeckillStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static SeckillStateEnum stateOf(int index) {
		for (SeckillStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
