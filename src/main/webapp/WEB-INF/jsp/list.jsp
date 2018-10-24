<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>秒杀列表</title>
<%@include file="common/head.jsp"%>


</head>
<body>

	<!-- 页面显示部分 -->
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading text-center">
				<h2>秒杀列表</h2>
			</div>
			<div class="panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>详情页</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="sk" items="${list}">
							<tr>
								<td>${sk.name}</td>
								<td>${sk.number}</td>
								<td><fmt:formatDate value="${sk.startTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${sk.endTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><fmt:formatDate value="${sk.createTime}"
										pattern="yyyy-MM-dd HH:mm:ss" /></td>
								<td><a class="btn btn-info"
									href="/seckill/${sk.seckillId}/detail" target="_blank">link</a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="panel-body">

				<div class='page fix'>
					<ul class="pagination pagination-lg">
						<li class="disabled"><a href="javascript:void(0)">共 <b>${pagination.totalNumber}</b>
								条
						</a></li>
						<c:if test="${pagination.pageIndex > 1}">
							<li><a href="javascript:changeCurrentPage('1')"
								class='first'>首页</a></li>
							<li><a
								href="javascript:changeCurrentPage('${pagination.pageIndex-1}')"
								class='pre'>上一页</a></li>
						</c:if>
						<li class="disabled"><a href="javascript:void(0)">当前第<span>${pagination.pageIndex}/${pagination.totalPage}</span>页
						</a></li>
						<c:if test="${pagination.pageIndex != pagination.totalPage}">
							<li><a
								href="javascript:changeCurrentPage('${pagination.pageIndex+1}')"
								class='next'>下一页</a></li>
							<li><a
								href="javascript:changeCurrentPage('${pagination.totalPage}')"
								class='last'>末页</a></li>
						</c:if>
						<li><span>跳至&nbsp;<input id="currentPageText"
								type='text' value='${pagination.pageIndex}'
								class="input input-sm" style="height: 20px; padding: 2px 5px" />&nbsp;页&nbsp;
								<a
								href="javascript:changeCurrentPage($('#currentPageText').val())"
								class='go'>GO</a></span></li>
					</ul>

				</div>
			</div>
		</div>
	</div>
	<!-- jQuery (Bootstrap 的 JavaScript 插件需要引入 jQuery) -->
	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>

	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

	<script src="/resources/script/list.1.0.0.js"></script>
</body>
</html>