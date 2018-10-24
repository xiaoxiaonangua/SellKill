package org.seckill.exception;

/**
 * @author Daniel.Zhang
 * @email daniel.zhang.china@hotmail.com
 * @created on 2018-06-28
 * 
 *
 */
public class DuplicatedKillException extends SeckillException {

	public DuplicatedKillException(String message, Throwable cause) {
		super(message, cause);

	}

	public DuplicatedKillException(String message) {
		super(message);

	}

}
