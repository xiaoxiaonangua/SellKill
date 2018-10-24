package org.seckill.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.seckill.entity.Pagination;

/*
 * ��ҳ������
 * 
 * MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

		// �����ļ���SQL����ID
		String sqlId = mappedStatement.getId();
		if (sqlId.matches(pagePattern)) {
			BoundSql boundSql = statementHandler.getBoundSql();
			// ԭʼ��SQL���
			String sql = boundSql.getSql().trim();
			if (sql.endsWith(";")) {
				sql = sql.substring(0, sql.length() - 1);
			}
			// ��ѯ��������SQL���
			String countSql = "select count(1) from (" + sql + ") a ";
			Connection conn = (Connection) invocation.getArgs()[0];
			PreparedStatement countStatement = conn.prepareStatement(countSql);
			ParameterHandler parameterHandler = (ParameterHandler) metaStatementHandler
					.getValue("delegate.parameterHandler");
			parameterHandler.setParameters(countStatement);
			ResultSet rs = countStatement.executeQuery();

			@SuppressWarnings("unchecked")
			Map<String, Object> parameter = (Map<String, Object>) boundSql.getParameterObject();
			// ��������ҳ��SQL
			Pagination pagination = (Pagination) parameter.get("pagination");
			if (rs.next()) {
				pagination.setTotalNumber(rs.getInt(1));
			}

			pagination.count();
			// �ʺ� mysql ,mariadb
			String paginatedSql = sql + " limit " + pagination.getDbIndex() + " , " + pagination.getDbNumber();
			metaStatementHandler.setValue("delegate.boundSql.sql", paginatedSql);

			// ���������ҳ�󣬾Ͳ���Ҫmybatis���ڴ��ҳ�ˣ����������������������
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

		}
		��ǰ�����ݣ���DAO�ӿ��еĲ�������@Param("name")��ע��ȡ������ʧ��
 */
@SuppressWarnings("unchecked")
@Intercepts(value = { @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class PaginationInterceptor implements Interceptor {

	private static final Log logger = LogFactory.getLog(PaginationInterceptor.class);
	private static final DefaultReflectorFactory DEFAULT_REFLECTOR_RACTORY = new DefaultReflectorFactory();
	private static String defaultDialect = "mysql"; // ���ݿ�����(Ĭ��Ϊmysql)
	private static String defaultPageSqlId = ".+ByPage$"; // ��Ҫ���ص�ID(����ƥ��)
	private static String dialect = ""; // ���ݿ�����(Ĭ��Ϊmysql)
	private static String pageSqlId = ""; // ��Ҫ���ص�ID(����ƥ��)

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler,
				SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
				DEFAULT_REFLECTOR_RACTORY);

		// ������������(����Ŀ������ܱ�������������أ��Ӷ��γɶ�δ���ͨ�����������ѭ�����Է������ԭʼ�ĵ�Ŀ����)
		while (metaStatementHandler.hasGetter("h")) {
			Object object = metaStatementHandler.getValue("h");
			metaStatementHandler = MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
					SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_RACTORY);
		}
		// �������һ����������Ŀ����
		while (metaStatementHandler.hasGetter("target")) {
			Object object = metaStatementHandler.getValue("target");
			metaStatementHandler = MetaObject.forObject(object, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
					SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_RACTORY);
		}

		Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
		Properties properties = configuration.getVariables();
		if (properties != null) {
			dialect = properties.getProperty("dialect");
		}
		if (null == dialect || "".equals(dialect)) {
			logger.warn("Property dialect is not setted,use default 'mysql' ");
			dialect = defaultDialect;
		}

		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		// �����ļ���SQL����ID
		String sqlId = mappedStatement.getId();
		String simplifiedSqlId = sqlId.substring(sqlId.lastIndexOf(".") + 1);
		// ֻ��д��Ҫ��ҳ��sql��䡣ͨ��MappedStatement��IDƥ�䣬Ĭ����д��Page��β��MappedStatement��sql
		if (sqlId.matches(pageSqlId)) {
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			Object parameterObject = boundSql.getParameterObject();
			if (parameterObject == null) {
				throw new NullPointerException("parameterObject is null!");
			} else {
				Map<String, Object> parameter = (Map<String, Object>) parameterObject;
				Pagination page = new Pagination();
				if (parameter.containsKey("pagination")) {
					page = (Pagination) parameter.get("pagination");
				} else if (parameter.containsKey(simplifiedSqlId)) {
					Map<String, Object> queryByPage = (Map<String, Object>) parameter.get(simplifiedSqlId);
					if (queryByPage.containsKey("pagination")) {
						page = (Pagination) queryByPage.get("pagination");
					}
				}

				String sql = boundSql.getSql();

				Connection connection = (Connection) invocation.getArgs()[0];

				// �����ҳ���������ҳ����
				setPageParameter(sql, connection, mappedStatement, boundSql, page);

				// ��дsql
				String pageSql = buildPageSql(sql, page);
				metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);
				// if (parameter.containsKey("pagination")) {
				// metaStatementHandler.setValue("delegate.boundSql.parameterObject.pagination",
				// page);
				// }
				// ���������ҳ�󣬾Ͳ���Ҫmybatis���ڴ��ҳ�ˣ����������������������
				metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
				metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

			}
		}
		return invocation.proceed();
	}

	/*
	 * ֻ����StatementHandler����
	 * 
	 * @see org.apache.ibatis.plugin.Interceptor#plugin(java.lang.Object)
	 * ���target����ǩ�����򷵻ذ�װ����ʵ����������ǣ�ԭ������
	 */
	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}

	}

	@Override
	public void setProperties(Properties properties) {
		String paginationPattern = properties.getProperty("pageSqlIdPattern");
		if (paginationPattern != null && !"".equals(paginationPattern)) {
			pageSqlId = paginationPattern;
		} else {
			pageSqlId = defaultPageSqlId;
		}
	}

	/**
	 * �������ݿ����ͣ������ض��ķ�ҳsql
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	private String buildPageSql(String sql, Pagination page) {
		if (page != null) {
			StringBuilder pageSql = new StringBuilder();

			if ("mysql".equals(dialect)) {
				pageSql = buildPageSqlForMysql(sql, page);
			} else if ("oracle".equals(dialect)) {
				pageSql = buildPageSqlForOracle(sql, page);
			} else {
				return sql;
			}
			return pageSql.toString();
		} else {
			return sql;
		}
	}

	/**
	 * mysql�ķ�ҳ���
	 * 
	 * @param sql
	 * @param page
	 * @return String
	 */
	public StringBuilder buildPageSqlForMysql(String sql, Pagination page) {

		StringBuilder pageSql = new StringBuilder(100);
		String beginrow = String.valueOf((page.getPageIndex() - 1) * page.getPageSize());
		pageSql.append(sql);
		pageSql.append(" limit " + beginrow + "," + page.getPageSize());
		return pageSql;
	}

	/**
	 * �ο�hibernate��ʵ�����oracle�ķ�ҳ
	 * 
	 * @param sql
	 * @param page
	 * @return String
	 */
	public StringBuilder buildPageSqlForOracle(String sql, Pagination page) {
		StringBuilder pageSql = new StringBuilder(100);
		String beginrow = String.valueOf((page.getPageIndex() - 1) * page.getPageSize());
		String endrow = String.valueOf(page.getPageIndex() * page.getPageSize());

		pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
		pageSql.append(sql);
		pageSql.append(" ) temp where rownum <= ").append(endrow);
		pageSql.append(") where row_id > ").append(beginrow);
		return pageSql;
	}

	/**
	 * �����ݿ����ѯ�ܵļ�¼����������ҳ������д����ҳ����<code>PageParameter</code>,���������߾Ϳ���ͨ�� ��ҳ����
	 * <code>PageParameter</code>��������Ϣ��
	 * 
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql,
			Pagination page) {
		// ��¼�ܼ�¼��
		String countSql = "select count(0) from (" + sql + ") as total";
		PreparedStatement countStmt = null;
		ResultSet rs = null;
		try {
			countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
					boundSql.getParameterMappings(), boundSql.getParameterObject());
			setParameters(countStmt, mappedStatement, countBS, boundSql.getParameterObject());
			rs = countStmt.executeQuery();
			int totalCount = 0;
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
			page.setTotalNumber(totalCount);
			page.count();

		} catch (SQLException e) {
			logger.error("Ignore this exception", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
			try {
				countStmt.close();
			} catch (SQLException e) {
				logger.error("Ignore this exception", e);
			}
		}

	}

	/**
	 * ��SQL����(?)��ֵ
	 * 
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql,
			Object parameterObject) throws SQLException {
		ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
		parameterHandler.setParameters(ps);
	}

}
