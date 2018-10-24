package org.seckill.entity;

/**
 * ��ҳ��Ӧ��ʵ����
 */
public class Pagination {
	/**
	 * ������
	 */
	private int totalNumber;
	/**
	 * ��ǰ�ڼ�ҳ
	 */
	private int pageIndex = 1;
	/**
	 * ��ҳ��
	 */
	private int totalPage;
	/**
	 * ÿҳ��ʾ����
	 */
	private int pageSize = 5;
	/**
	 * ���ݿ���limit�Ĳ������ӵڼ�����ʼȡ
	 */
	private int dbIndex;
	/**
	 * ���ݿ���limit�Ĳ�����һ��ȡ������
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
	 * ���ݵ�ǰ����������ֵ���㲢�����������ֵ
	 */
	public void count() {
		// ������ҳ��
		int totalPageTemp = this.getTotalNumber() / this.getPageSize();
		int plus = (this.getTotalNumber() % this.getPageSize()) == 0 ? 0 : 1;
		totalPageTemp = totalPageTemp + plus;
		if (totalPageTemp <= 0) {
			totalPageTemp = 1;
		}
		this.totalPage = totalPageTemp;

		// ���õ�ǰҳ��
		// ��ҳ��С�ڵ�ǰҳ����Ӧ����ǰҳ������Ϊ��ҳ��
		if (this.totalPage < this.getPageIndex()) {
			this.pageIndex = this.totalPage;
		}
		
		// ��ǰҳ��С��1����Ϊ1
		 if (this.getPageIndex() < 1) {
		    this.setPageIndex(1);
		 }

		// ����limit�Ĳ���
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
