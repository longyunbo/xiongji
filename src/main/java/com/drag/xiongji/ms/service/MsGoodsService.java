package com.drag.xiongji.ms.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.ms.dao.MsGoodsDao;
import com.drag.xiongji.ms.dao.MsOrderDao;
import com.drag.xiongji.ms.dao.MsRemindDao;
import com.drag.xiongji.ms.entity.MsGoods;
import com.drag.xiongji.ms.entity.MsOrder;
import com.drag.xiongji.ms.entity.MsRemind;
import com.drag.xiongji.ms.form.MsGoodsForm;
import com.drag.xiongji.ms.resp.MsGoodsResp;
import com.drag.xiongji.ms.vo.MsGoodsDetailVo;
import com.drag.xiongji.ms.vo.MsGoodsVo;
import com.drag.xiongji.ms.vo.MsRemindVo;
import com.drag.xiongji.user.dao.UserDao;
import com.drag.xiongji.user.dao.UserTicketDao;
import com.drag.xiongji.user.dao.UserTicketTemplateDao;
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.user.entity.UserTicket;
import com.drag.xiongji.user.entity.UserTicketTemplate;
import com.drag.xiongji.user.service.DragGoodsService;
import com.drag.xiongji.user.vo.UserVo;
import com.drag.xiongji.utils.BeanUtils;
import com.drag.xiongji.utils.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MsGoodsService {

	@Autowired
	private MsGoodsDao msGoodsDao;
	@Autowired
	private MsOrderDao msOrderDao;
	@Autowired
	private MsRemindDao msRemindDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserTicketTemplateDao userTicketTemplateDao;
	@Autowired
	private UserTicketDao userTicketDao;
	@Autowired
	private DragGoodsService dragGoodsService;

	/**
	 * 查询所有的秒杀商品(秒杀列表)
	 * @return
	 */
	public List<MsGoodsVo> listGoods() {
		List<MsGoodsVo> goodsResp = new ArrayList<MsGoodsVo>();
		List<MsGoods> goodsList = msGoodsDao.findAll();
		if (goodsList != null && goodsList.size() > 0) {
			for (MsGoods msgoods : goodsList) {
				MsGoodsVo resp = new MsGoodsVo();
				BeanUtils.copyProperties(msgoods, resp,new String[]{"createTime", "updateTime","startTime","endTime"});
				resp.setCreateTime((DateUtil.format(msgoods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setUpdateTime((DateUtil.format(msgoods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setStartTime((DateUtil.format(msgoods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setEndTime((DateUtil.format(msgoods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	
	/**
	 * 查询秒杀详情商品
	 * @return
	 */
	public MsGoodsDetailVo goodsDetail(int goodsId) {
		log.info("【查询秒杀详情商品】传入参数:{}",goodsId);
		List<UserVo> grouperList = new ArrayList<UserVo>();
		MsGoodsDetailVo detailVo = new MsGoodsDetailVo();
		MsGoods goods = msGoodsDao.findGoodsDetail(goodsId);
		if(goods != null) {
			this.copyProperties(goods, detailVo);
			List<MsOrder> msList = msOrderDao.findByMsgoodsId(goodsId);
			if(msList != null && msList.size() > 0) {
				
				Map<Integer,User> userMap = new HashMap<Integer,User>();
				Set<Integer> ids = new HashSet<Integer>();
				for(MsOrder pu : msList) {
					ids.add(pu.getUid());
				}
				//把用户存在缓存中，不用去循环查询
				if(ids != null && ids.size() > 0) {
					List<User> userList = userDao.findByIdIn(ids);
					for(User us : userList) {
						userMap.put(us.getId(), us);
					}
				}
				for(MsOrder pu : msList) {
					UserVo userVo = new UserVo();
					int uid = pu.getUid();
					User user = userMap.get(uid);
					userVo.setPrice(pu.getPrice());
					userVo.setNumber(pu.getNumber());
					if(user != null) {
						BeanUtils.copyProperties(user, userVo,new String[]{"createTime","updateTime"});
						userVo.setCreateTime(DateUtil.format(pu.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						grouperList.add(userVo);
					}
				}
			}
			detailVo.setGroupers(grouperList);
		}
		return detailVo;
	}
	
	/**
	 * 查询秒杀活动是否结束
	 * @param goodsId
	 * @return
	 */
	public Boolean checkEnd(int goodsId) {
		boolean endFlag = false;
		MsGoods goods = msGoodsDao.findGoodsDetail(goodsId);
		if(goods != null) {
			int isEnd = goods.getIsEnd();
			if(isEnd == 1) {
				endFlag = true;
			}else {
				endFlag = false;
			}
		}
		return endFlag;
	}
	
	
	/**
	 * 发起秒杀
	 * @return
	 */
	@Transactional
	public MsGoodsResp collage(MsGoodsForm form) {
		log.info("【秒杀,传入参数】form:{}",JSON.toJSONString(form));
		MsGoodsResp baseResp = new MsGoodsResp();
		try {
			//商品编号
			int msgoodsId = form.getMsgoodsId();
			MsGoods goods = msGoodsDao.findGoodsDetail(msgoodsId);
			
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【本人发起秒杀,用户不存在】openid:{}",openid);
				return baseResp;
			}
			//获取系统用户编号
			int uid = user.getId();
			
			//购买数量
			int number = form.getNumber();
			if(goods != null) {
				//减库存
				Boolean flag = this.delStock(goods,number);
				if(!flag) {
					baseResp.setReturnCode(Constant.STOCK_FAIL);
					baseResp.setErrorMessage("库存不足");
					log.error("【该商品库存不足】,msgoodsId:{}",msgoodsId);
					return baseResp;
				}
			}else {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				log.error("【本人发起秒杀,商品编号不存在】msgoodsId:{}",msgoodsId);
				return baseResp;
			}
			
			//点击秒杀，需要在秒杀订单表中插入一条数据
			MsOrder msOrder = new MsOrder();
			BeanUtils.copyProperties(form, msOrder);
			msOrder.setMsgoodsName(goods.getMsgoodsName());
			msOrder.setUid(uid);
			msOrder.setPerPrice(goods.getMsPrice());
			msOrder.setPrice(goods.getPrice());
			msOrder.setOrderstatus(MsOrder.ORDERSTATUS_SUCCESS);
			msOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
			msOrderDao.save(msOrder);
			
			log.info("【秒杀,订单插入成功】msOrder:{}",JSON.toJSONString(msOrder));
			
			//新增秒杀次数
			this.addMsTimes(goods);
			
			//新增恐龙骨
			dragGoodsService.addDragBone(user,goods.getMsgoodsId(),goods.getMsgoodsName(),Constant.TYPE_MS,goods.getDragBone(), goods.getExp());
			
			String type = Constant.TYPE_MS;
			UserTicketTemplate  template = userTicketTemplateDao.findByGoodsIdAndType(msgoodsId, type);
			//发送卡券
			if(template != null) {
				for(int i = 0;i < number; i++) {
					UserTicket ticket = new UserTicket();
					BeanUtils.copyProperties(template, ticket);
					ticket.setId(ticket.getId());
					ticket.setUid(uid);
					ticket.setNumber(1);
					ticket.setStatus(UserTicket.STATUS_NO);
					ticket.setCreateTime((new Timestamp(System.currentTimeMillis())));
					userTicketDao.save(ticket);
					log.info("【秒杀,发送卡券成功】ticket:{}",JSON.toJSONString(ticket));
				}
			}
			
			//返回参数
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("秒杀成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
			throw AMPException.getException("系统异常!");
		}
		
		return baseResp;
	}
	
	/**
	 * 秒杀提醒
	 * @param form
	 * @return
	 */
	@Transactional
	public MsGoodsResp remind(MsGoodsForm form) {
		log.info("【秒杀提醒,传入参数】form:{}",JSON.toJSONString(form));
		MsGoodsResp baseResp = new MsGoodsResp();
		try {
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【秒杀提醒,用户不存在】openid:{}",openid);
				return baseResp;
			}
			int msgoodsId = form.getMsgoodsId();
			MsRemind remind = msRemindDao.findByMsgoodsIdAndOpenid(msgoodsId, openid);
			if(remind != null) {
				int remindType = form.getRemindType();
				if(remindType == 0) {
					remind.setStatus(MsRemind.STATUS_YES);
				}else {
					remind.setStatus(MsRemind.STATUS_NO);
				}
				msRemindDao.saveAndFlush(remind);
			}else {
				MsRemind remd = new MsRemind();
				remd.setId(remd.getId());
				remd.setOpenid(openid);
				remd.setMsgoodsId(msgoodsId);
				remd.setMsgoodsName(form.getMsgoodsName());
				remd.setStatus(MsRemind.STATUS_YES);
				remd.setFormId(form.getFormId());
				remd.setCreateTime(new Timestamp(System.currentTimeMillis()));
				msRemindDao.save(remd);
			}
			//返回参数
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("秒杀提醒设置成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("系统异常!");
		}
		return baseResp;
	}
	
	/**
	 * 查询秒杀提醒
	 * @param openid
	 * @return
	 */
	public List<MsRemindVo> remindList(String openid) {
		List<MsRemindVo> resp = new  ArrayList<MsRemindVo>();
		List<MsRemind> msList =  msRemindDao.findByOpenid(openid);
		if(msList != null && msList.size() > 0) {
			for(MsRemind ms : msList) {
				MsRemindVo vo = new MsRemindVo();
				BeanUtils.copyProperties(ms, vo,new String[]{"createTime"});
				vo.setCreateTime((DateUtil.format(ms.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.add(vo);
			}
		}
		return resp;
	}
	
	/**
	 * 秒杀商品减库存
	 * @param goods
	 * @param number
	 * @return
	 */
	public Boolean delStock(MsGoods goods, int number) {
		boolean flag = false;
		int msgoodsNumber = goods.getMsgoodsNumber();
		if (msgoodsNumber - number < 0) {
			// 库存不足
			flag = false;
		} else {
			flag = true;
			int nowGoodsNum = msgoodsNumber - number;
			goods.setMsgoodsNumber(nowGoodsNum);
			msGoodsDao.saveAndFlush(goods);
		}
		return flag;
	}
	
	/**
	 * 增加秒杀次数
	 * @param goods
	 * @param number
	 */
	public void addMsTimes(MsGoods goods) {
		int succTime = goods.getMsSuccTimes();
		goods.setMsSuccTimes(succTime + 1);
		msGoodsDao.saveAndFlush(goods);
	}
	
	/**
	 * 提取copy方法
	 * @param goods
	 * @param detailVo
	 */
	public void copyProperties(MsGoods goods,MsGoodsDetailVo detailVo) {
		BeanUtils.copyProperties(goods, detailVo,new String[]{"createTime", "updateTime","startTime","endTime"});
		detailVo.setCreateTime((DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setUpdateTime((DateUtil.format(goods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setStartTime((DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setEndTime((DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
	}
}
