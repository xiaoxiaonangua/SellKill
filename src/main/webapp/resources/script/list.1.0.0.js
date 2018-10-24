/**
 * 修改当前页码，调用后台重新查询
 */
function changeCurrentPage(currentPage) {
	if (currentPage && !isNaN(currentPage)) {
		if (currentPage == 1 || currentPage < 1)
			window.location.href = '/seckill/list';
		else
			window.location.href = '/seckill/' + currentPage + '/list';
	} else {
		window.location.href = '/seckill/list';

	}
}