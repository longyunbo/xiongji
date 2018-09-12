package com.drag.xiongji.pay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drag.xiongji.common.BaseResponse;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.ms.dao.MsGoodsDao;
import com.drag.xiongji.ms.entity.MsGoods;
import com.drag.xiongji.pay.form.PayCheckForm;
import com.drag.xiongji.pt.dao.PtGoodsDao;
import com.drag.xiongji.pt.dao.PtUserDao;
import com.drag.xiongji.pt.entity.PtGoods;
import com.drag.xiongji.pt.entity.PtUser;
import com.drag.xiongji.user.dao.UserDao;
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.user.service.UserService;
import com.drag.xiongji.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PayService {

	@Autowired
	private MsGoodsDao msGoodsDao;
	@Autowired
	private PtGoodsDao ptGoodsDao;
	@Autowired
	private UserService userService;
	@Autowired
	private PtUserDao ptUserDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 查询秒杀活动是否结束
	 * @param goodsId
	 * @return
	 */
	public BaseResponse checkAll(PayCheckForm form) {
		BaseResponse baseResp = new BaseResponse();
		int goodsId = form.getGoodsId();
		int number = form.getNumber();
		String type = form.getType();
		String openid = form.getOpenid();
		User user = userDao.findByOpenid(openid);
		if(user == null) {
			baseResp.setReturnCode(Constant.USERNOTEXISTS);
			baseResp.setErrorMessage("该用户不存在!");
			return baseResp;
		}
		if(type.equals(Constant.TYPE_PT)) {
			PtGoods goods = ptGoodsDao.findGoodsDetail(goodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				return baseResp;
			}
			int isEnd = goods.getIsEnd();
			if(isEnd == 1) {
				baseResp.setReturnCode(Constant.ACTIVITYALREADYEND_FAIL);
				baseResp.setErrorMessage("该活动已结束!");
				return baseResp;
			}
			Boolean authFlag =  userService.checkAuth(user, goods.getAuth());
			if(!authFlag) {
				baseResp.setReturnCode(Constant.AUTH_OVER);
				baseResp.setErrorMessage("该用户权限不够!");
				return baseResp;
			}
			int ptgoodsNumber = goods.getPtgoodsNumber();
			if (ptgoodsNumber - number < 0) {
				baseResp.setReturnCode(Constant.STOCK_FAIL);
				baseResp.setErrorMessage("商品库存不足!");
				return baseResp;
			}
			//获取系统用户编号
			int uid = user.getId();
//			List<PtUser> ptList = ptUserDao.findByUidAndPtgoodsIdAndIsHeadAndPtstatus(uid, goodsId, PtUser.ISHEADER_YES, PtUser.PTSTATUS_MIDDLE);
//			if(ptList != null && ptList.size() > 0) {
//				baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
//				baseResp.setErrorMessage("该用户已经拼过此团，请完成后再拼团!");
//				return baseResp;
//			}
			
			String code = form.getCode();
			if(!StringUtil.isEmpty(code)) {
				//拼团规模
				int ptSize = 0;
				//已经拼团的人数
				int grouperSize = 0;
				List<PtUser> ptLi = ptUserDao.findByPtCode(code);
				//拼团已完成校验
				if(ptLi != null && ptLi.size() > 0) {
					ptSize = goods.getPtSize();
					//已经拼团的人数
					grouperSize = ptLi.size();
					if(grouperSize >= ptSize) {
						baseResp.setReturnCode(Constant.ACTIVITYALREADYDOWN_FAIL);
						baseResp.setErrorMessage("该团拼团已完成，不能再拼团!");
						return baseResp;
					}
				}else {
					baseResp.setReturnCode(Constant.ACTIVITYNOTEXISTS);
					baseResp.setErrorMessage("该拼团编号不存在!");
					return baseResp;
				}
				
				//同一个用户拼团校验
				PtUser ptuser = ptUserDao.findByPtGoodsIdAndUidAndPtCode(goodsId, uid, code);
				if(ptuser != null) {
					baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
					baseResp.setErrorMessage("该用户已经拼过此团，不能再拼团!");
					return baseResp;
				}
			}
		}else if(type.equals(Constant.TYPE_MS)) {
			MsGoods goods = msGoodsDao.findGoodsDetail(goodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				return baseResp;
			}
			int isEnd = goods.getIsEnd();
			if(isEnd == 1) {
				baseResp.setReturnCode(Constant.ACTIVITYALREADYEND_FAIL);
				baseResp.setErrorMessage("该活动已结束!");
				return baseResp;
			}
			int msgoodsNumber = goods.getMsgoodsNumber();
			if (msgoodsNumber - number < 0) {
				baseResp.setReturnCode(Constant.STOCK_FAIL);
				baseResp.setErrorMessage("商品库存不足!");
				return baseResp;
			}
		}
		baseResp.setReturnCode(Constant.SUCCESS);
		baseResp.setErrorMessage("验证通过!");
		return baseResp;
	}
	
	
}
