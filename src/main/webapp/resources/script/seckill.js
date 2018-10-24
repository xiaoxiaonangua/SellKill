// 存放主要 交互逻辑js代码
// javascript模块化
// seckill.detail.init(param);
var seckill = {
	// 封装秒杀相关ajax的url
	URL : {
		now : function() {
			return '/seckill/time/now';
		},
		exposer : function(seckillId) {
			return '/seckill/' + seckillId + '/exposer';
		},
		execution : function(seckillId, md5) {
			return '/seckill/' + seckillId + '/' + md5 + '/execution';
		}
	},
	// 验证手机号
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	countdown : function(seckillId, nowTime, startTime, endTime) {
		var seckillBox = $('#seckill-box');
		// 时间判断
		// 秒杀结束
		if (nowTime > endTime) {
			seckillBox.html('秒杀结束！');
		} else if (nowTime < startTime) {
			seckillBox.html('秒杀未开始！');
			// 开始计时
			var killTime = new Date(startTime + 1000);
			seckillBox.countdown(killTime, function(event) {
				var format = event.strftime('秒杀计时: %D天  %H时 %M分 %S秒');
				seckillBox.html(format);
			}).on('finish.countdown', function() {
				// 获取秒杀地址 ，控制实现逻辑，执行秒杀
				seckill.handleSeckill(seckillId, seckillBox);
			});
		} else {
			seckill.handleSeckill(seckillId, seckillBox);
		}
	},
	handleSeckill : function(seckillId, node) {
		// 获取秒杀地址 ，控制实现逻辑，执行秒杀
		node
				.hide()
				.html(
						'<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');

		$
				.post(
						seckill.URL.exposer(seckillId),
						{},
						function(result) {
							// 在回调函数中执行交互流程
							if (result && result['success']) {
								var exposer = result['data'];
								if (exposer['exposed']) {
									// 已经开启秒杀
									// 获取秒杀的地址
									var md5 = exposer['md5'];
									var killUrl = seckill.URL.execution(
											seckillId, md5);
									// console.log("killUrl:" + killUrl);
									$('#killBtn')
											.one(
													'click',
													function() {
														// 执行秒杀请求
														// 1.禁用按钮
														$(this).addClass(
																'disabled');
														// 2.发送请求
														$
																.post(
																		killUrl,
																		{},
																		function(
																				result) {
																			console
																					.log(result);
																			if (result
																					&& result['data']) {
																				var killResult = result['data'];
																				var state = killResult['state'];
																				var stateInfo = killResult['stateInfo'];
																				node
																						.html('<span class="label label-success">'
																								+ stateInfo
																								+ '</span>');
																			} else if (result) {
																				var stateInfo = result['error'];
																				node
																						.html('<span class="label label-success">'
																								+ stateInfo
																								+ '</span>');
																			}
																		});
													});
									node.show();
								} else {
									// 未开启秒杀
									var now = exposer['now'];
									var start = exposer['start'];
									var end = exposer['end'];
									// 重新计算计时逻辑
									seckill.countdown(seckillId, now, start,
											end);
								}
							} else {
								console.log("result:" + result);
							}
						});

	},
	// 详情页秒杀逻辑
	detail : {
		// 详情页初始化
		init : function(params) {
			// 手机验证和登录，计时交互
			// 规划交互流程
			// 在cookie中查找手机号
			var killPhone = $.cookie('killPhone');
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];

			// 验证手机号
			if (!seckill.validatePhone(killPhone)) {
				// 绑定phone
				// 控制输出
				var killPhoneModal = $('#killPhoneModal');
				killPhoneModal.modal({
					// 显示弹出层
					show : true,
					// 禁止位置关闭
					backdrop : 'static',
					// 关闭键盘事件
					keyboard : false

				});
				$('#killPhoneBtn')
						.click(
								function() {
									var inputPhone = $('#killPhoneKey').val();
									if (seckill.validatePhone(inputPhone)) {
										$.cookie('killPhone', inputPhone, {
											expires : 7,
											path : '/seckill'
										});
										// 刷新页面
										window.location.reload();
									} else {
										$('#killPhoneMessage')
												.hide()
												.html(
														'<label class="label label-danger" >手机号错误</label>')
												.show(300);
									}
								});

			}

			// 已经登录
			// 计时交互
			$.get(seckill.URL.now(), {}, function(result) {
				if (result && result['success']) {
					var nowTime = result['data'];
					seckill.countdown(seckillId, nowTime, startTime, endTime);
				} else {
					console.log('seckill.URL.Now() result:' + result);

				}
			});

		}
	}
}