package org.seckill.entity;

/**
 * 分页对应的实体类
 */
public class Pagination {
	/**
	 * 总条数
	 */
	private int totalNumber;
	/**
	 * 当前第几页
	 */
	private int pageIndex = 1;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 每页显示条数
	 */
	private int pageSize = 5;
	/**
	 * 数据库中limit的参数，从第几条开始取
	 */
	private int dbIndex;
	/**
	 * 数据库中limit的参数，一共取多少条
	 */
	private int dbNumber;

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber >= 0 ? totalNumber : 0;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex >= 1 ? pageIndex : 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize > 1 ? pageSize : 1;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public int getDbNumber() {
		return dbNumber;
	}

	/**
	 * 根据当前对象中属性值计算并设置相关属性值
	 */
	public void count() {
		// 计算总页数
		int totalPageTemp = this.getTotalNumber() / this.getPageSize();
		int plus = (this.getTotalNumber() % this.getPageSize()) == 0 ? 0 : 1;
		totalPageTemp = totalPageTemp + plus;
		if (totalPageTemp <= 0) {
			totalPageTemp = 1;
		}
		this.totalPage = totalPageTemp;

		// 设置当前页数
		// 总页数小于当前页数，应将当前页数设置为总页数
		if (this.totalPage < this.getPageIndex()) {
			this.pageIndex = this.totalPage;
		}
		
		// 当前页数小于1设置为1
		 if (this.getPageIndex() < 1) {
		    this.setPageIndex(1);
		 }

		// 设置limit的参数
		this.dbIndex = (this.getPageIndex() - 1) * this.getPageSize();
		this.dbNumber = this.getPageSize();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Pagination={totalNumber=" + totalNumber + ",	pageSize=" + pageSize + ",	totalPage=" + totalPage
				+ ",	pageIndex=" + pageIndex + ",	dbIndex=" + dbIndex + ",	dbNumber=" + dbNumber + "}";
	}

}
