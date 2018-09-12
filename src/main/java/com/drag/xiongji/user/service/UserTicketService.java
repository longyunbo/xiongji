package com.drag.xiongji.user.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.user.dao.UserDao;
import com.drag.xiongji.user.dao.UserTicketDao;
import com.drag.xiongji.user.dao.UserTicketDetailDao;
import com.drag.xiongji.user.dao.UserTicketRecordDao;
import com.drag.xiongji.user.dao.UserTicketTemplateDao;
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.user.entity.UserTicket;
import com.drag.xiongji.user.entity.UserTicketDetail;
import com.drag.xiongji.user.entity.UserTicketRecord;
import com.drag.xiongji.user.entity.UserTicketTemplate;
import com.drag.xiongji.user.form.UserTicketForm;
import com.drag.xiongji.user.resp.UserTicketResp;
import com.drag.xiongji.user.vo.UserTicketDetailVo;
import com.drag.xiongji.user.vo.UserTicketTemplateVo;
import com.drag.xiongji.user.vo.UserTicketVo;
import com.drag.xiongji.utils.BeanUtils;
import com.drag.xiongji.utils.DateUtil;
import com.drag.xiongji.wm.dao.ProductInfoDao;
import com.drag.xiongji.wm.form.OrderDetailForm;
import com.drag.xiongji.wm.form.OrderInfoForm;
import com.drag.xiongji.wm.resp.OrderResp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserTicketService {

	@Autowired
	UserTicketDao userTicketDao;
	@Autowired
	UserTicketDetailDao userTicketDetailDao;
	@Autowired
	UserTicketRecordDao userTicketRecordDao;
	@Autowired
	UserTicketTemplateDao userTicketTemplateDao;
	@Autowired
	private UserDao userDao;
	
	/**
	 * 卡券列表
	 * @param openid
	 * @param type
	 * @return
	 */
	public List<UserTicketVo> listTicket(String openid) {
		List<UserTicketVo> ticketResp = new ArrayList<UserTicketVo>();
		User user = userDao.findByOpenid(openid);
		if(user != null) {
			int uid = user.getId();
			List<UserTicket> ticketList = userTicketDao.findByUid(uid);
			if (ticketList != null && ticketList.size() > 0) {
				for (UserTicket ticket : ticketList) {
					UserTicketVo resp = new UserTicketVo();
					BeanUtils.copyProperties(ticket, resp,new String[]{"createTime", "updateTime"});
					resp.setCreateTime((DateUtil.format(ticket.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
					ticketResp.add(resp);
				}
			}
		}
		return ticketResp;
	}
	
	/**
	 * 发送卡券
	 * @param form
	 */
	@Transactional
	public UserTicketResp sendTicket(UserTicketForm form) {
		UserTicketResp resp = new UserTicketResp();
		try {
			int goodsId = form.getGoodsId();
			String type= form.getType();
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				resp.setReturnCode(Constant.USERNOTEXISTS);
				resp.setErrorMessage("该用户不存在!");
				return resp;
			}
			//获取系统用户编号
			int uid = user.getId();
			UserTicketTemplate  template = userTicketTemplateDao.findByGoodsIdAndType(goodsId, type);
			if(template != null) {
				UserTicket ticket = new UserTicket();
				BeanUtils.copyProperties(template, ticket);
				ticket.setUid(uid);
				ticket.setNumber(1);
				ticket.setStatus(UserTicket.STATUS_NO);
				ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
				userTicketDao.save(ticket);
				resp.setReturnCode(Constant.SUCCESS);
				resp.setErrorMessage("卡券发送成功!");
			}
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
	}
	
	/**
	 * 核销卡券
	 * @param form
	 */
	@Transactional
	public UserTicketResp destoryTicket(int ticketId) {
		UserTicketResp resp = new UserTicketResp();
		try {
			UserTicket ticket = userTicketDao.findOne(ticketId);
			if(ticket != null) {
				
				if(ticket.getStatus() == UserTicket.STATUS_YES) {
					resp.setReturnCode(Constant.TICKET_DESTORY);
					resp.setErrorMessage("该卡券已经被核销!");
					return resp;
				}
				
				if(ticket.getStatus() == UserTicket.STATUS_OVER) {
					resp.setReturnCode(Constant.TICKET_OVER);
					resp.setErrorMessage("该卡券已经过期!");
					return resp;
				}
				
				//修改成已使用
				ticket.setStatus(UserTicket.STATUS_YES);
				userTicketDao.saveAndFlush(ticket);
				
				UserTicketRecord ticketRecord = new UserTicketRecord();
				ticketRecord.setId(ticketRecord.getId());
				ticketRecord.setTicketId(ticketId);
				ticketRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
				BeanUtils.copyProperties(ticket, ticketRecord);
				userTicketRecordDao.save(ticketRecord);
				
				resp.setReturnCode(Constant.SUCCESS);
				resp.setErrorMessage("卡券核销成功!");
			}else {
				resp.setReturnCode(Constant.TICKETNOTEXISTS);
				resp.setErrorMessage("卡券不存在");
			}
		} catch (Exception e) {
			log.error("系统异常,{}", e);
			throw AMPException.getException("系统异常!");
		}
		return resp;
		
	}
	
	
	/**
	 * 获取礼品卡模板
	 * @param type
	 * @return
	 */
	public List<UserTicketTemplateVo> listGift(String type) {
		List<UserTicketTemplateVo> ticketResp = new ArrayList<UserTicketTemplateVo>();
		List<UserTicketTemplate> templates = userTicketTemplateDao.findByType(type);
		if(templates != null) {
			for(UserTicketTemplate tem : templates) {
				UserTicketTemplateVo vo = new UserTicketTemplateVo();
				BeanUtils.copyProperties(tem, vo,new String[]{"createTime", "updateTime"});
				ticketResp.add(vo);
			}
		}
		return ticketResp;
	}
	
	
	/**
	 * 礼品卡购买
	 * @param form
	 * @return
	 */
	public OrderResp purchase(OrderInfoForm form) {
		log.info("【礼品卡购买传入参数】:{}",JSON.toJSONString(form));
		OrderResp resp = new OrderResp();
		try {
			int ticketid = 0;
			int goodsId = form.getGoodsId();
			String outTradeNo = form.getOutTradeNo();
			//消耗总金额
			BigDecimal price = form.getPrice();
			
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user != null) {
				int uid = user.getId();
				UserTicketTemplate  template = userTicketTemplateDao.findByGoodsIdAndType(goodsId, "lpk");
				if(template != null) {
					//插入卡券表
					UserTicket ticket = new UserTicket();
					BeanUtils.copyProperties(template, ticket,new String[]{"id"});
					ticket.setId(ticket.getId());
					ticket.setType(Constant.TYPE_LPK);
					ticket.setUid(uid);
					ticket.setFuid(uid);
					ticket.setDefprice(price);
					ticket.setPrice(price);
					ticket.setNumber(1);
					ticket.setStatus(UserTicket.STATUS_NO);
					ticket.setOutTradeNo(outTradeNo);
					ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
					userTicketDao.saveAndFlush(ticket);
					ticketid = ticket.getId();
					List<OrderDetailForm> orderList = form.getOrderDetail();
					if(orderList != null && orderList.size() > 0) {
						for(OrderDetailForm detail : orderList) {
							//插入礼品卡详情
							int dGoodsId = detail.getGoodsId();
							String dGoodsName = detail.getGoodsName();
							String goodsThumb = detail.getGoodsThumb();
							String type = detail.getType();
							String dNorms = detail.getNorms();
							int dNumber = detail.getNumber();
							BigDecimal dPrice  = detail.getPrice();
							//如果是现金券，发送多张券
							if(type.equals("cash")) {
								for(int i = 0; i < dNumber; i++) {
									UserTicketDetail ticketDetail = new UserTicketDetail();
									ticketDetail.setUid(uid);
									ticketDetail.setType(type);
									ticketDetail.setTicketId(ticketid);
									ticketDetail.setGoodsId(dGoodsId);
									ticketDetail.setGoodsName(dGoodsName);
									ticketDetail.setGoodsThumb(goodsThumb);
									ticketDetail.setNorms(dNorms);
									ticketDetail.setPrice(dPrice);
									ticketDetail.setNumber(1);
									ticketDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
									userTicketDetailDao.save(ticketDetail);
								}
							}else {
								UserTicketDetail ticketDetail = new UserTicketDetail();
								ticketDetail.setUid(uid);
								ticketDetail.setType(type);
								ticketDetail.setTicketId(ticketid);
								ticketDetail.setGoodsId(dGoodsId);
								ticketDetail.setGoodsName(dGoodsName);
								ticketDetail.setGoodsThumb(goodsThumb);
								ticketDetail.setNorms(dNorms);
								ticketDetail.setPrice(dPrice);
								ticketDetail.setNumber(dNumber);
								ticketDetail.setCreateTime(new Timestamp(System.currentTimeMillis()));
								userTicketDetailDao.save(ticketDetail);
							}
							resp.setTicketId(ticketid);
							resp.setReturnCode(Constant.SUCCESS);
							resp.setErrorMessage("礼品卡购买成功!");
						}
					}else {
						resp.setReturnCode(Constant.ORDERNOTEXISTS);
						resp.setErrorMessage("订单详情不存在，请添加商品!");
						log.error("【礼品卡购买参数错误】,{}",JSON.toJSONString(orderList));
						return resp;
					}
				}else {
					resp.setReturnCode(Constant.ORDERNOTEXISTS);
					resp.setErrorMessage("礼品卡模板不存在!");
					log.error("【礼品卡购买参数错误】,template:{}",JSON.toJSONString(template));
					return resp;
				}
			}
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("礼品卡购买异常!");
		}
		return resp;
	}
	
	
	/**
	 * 查询礼品卡详情
	 * @param ticketid
	 * @return
	 */
	public List<UserTicketDetailVo> listTicketDetail(int ticketid) {
		List<UserTicketDetailVo> ticketResp = new ArrayList<UserTicketDetailVo>();
		List<UserTicketDetail> ticketList = userTicketDetailDao.findByTicketId(ticketid);
		if(ticketList != null && ticketList.size() > 0) {
			for(UserTicketDetail ticket : ticketList) {
				UserTicketDetailVo vo = new UserTicketDetailVo();
				BeanUtils.copyProperties(ticket, vo,new String[]{"createTime"});
				vo.setCreateTime((DateUtil.format(ticket.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				ticketResp.add(vo);
			}
		}
		return ticketResp;
	}
	
	/**
	 * 根据openid获取卡券详情
	 * @param openid
	 * @return
	 */
	public List<UserTicketDetailVo> listTicketDetailByOpenid(String openid) {
		List<UserTicketDetailVo> ticketResp = new ArrayList<UserTicketDetailVo>();
		User user = userDao.findByOpenid(openid);
		if(user != null) {
			int uid = user.getId();
			List<UserTicketDetail> ticketList = userTicketDetailDao.findByUid(uid);
			if(ticketList != null && ticketList.size() > 0) {
				for(UserTicketDetail ticket : ticketList) {
					UserTicketDetailVo vo = new UserTicketDetailVo();
					BeanUtils.copyProperties(ticket, vo,new String[]{"createTime"});
					vo.setCreateTime((DateUtil.format(ticket.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
					ticketResp.add(vo);
				}
			}
		}
		return ticketResp;
	}
	
	/**
	 * 赠送卡券
	 * @param ticketid
	 * @return
	 */
	public OrderResp sendTicket(int ticketid,String content) {
		log.info("【礼品卡赠送】,ticketid:{}",ticketid);
		OrderResp resp = new OrderResp();
		try {
			UserTicket ticket = userTicketDao.findOne(ticketid);
			if(ticket != null) {
				ticket.setContent(content);
				ticket.setStatus(UserTicket.STATUS_SEND);
				userTicketDao.saveAndFlush(ticket);
				resp.setReturnCode(Constant.SUCCESS);
				resp.setErrorMessage("礼品卡赠送成功!");
			}else {
				resp.setReturnCode(Constant.TICKETNOTEXISTS);
				resp.setErrorMessage("卡券不存在!");
				log.error("【礼品卡赠送，编号不存在】,ticketid:{}",ticketid);
			}
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("礼品卡购买异常!");
		}
		return resp;
	}
	
	/**
	 * 获取礼品卡
	 * @param ticketid 卡券编号
	 * @param openid 获赠者
	 * @param sendOpenid 赠送者
	 * @return
	 */
	public OrderResp receiveTicket(int ticketid,String openid,String sendOpenid) {
		log.info("【礼品卡获取】,ticketid:{},获赠者openid:{},赠送者sendOpenid:{}",ticketid,openid,sendOpenid);
		OrderResp resp = new OrderResp();
		try {
			UserTicket ticket = userTicketDao.findOne(ticketid);
			User user = userDao.findByOpenid(openid);
			User sendUser = userDao.findByOpenid(sendOpenid);
			if(user != null) {
				int uid = user.getId();
				int fuid = sendUser.getId();
				if(ticket != null) {
					//赠送者编号
					ticket.setFuid(fuid);
					ticket.setUid(uid);
					ticket.setStatus(UserTicket.STATUS_NO);
					userTicketDao.saveAndFlush(ticket);
					//修改详情中卡券的拥有者
					List<UserTicketDetail> ticketList = userTicketDetailDao.findByTicketId(ticketid);
					for(UserTicketDetail detail : ticketList) {
						detail.setUid(uid);
						userTicketDetailDao.saveAndFlush(detail);
					}
					resp.setReturnCode(Constant.SUCCESS);
					resp.setErrorMessage("礼品卡获取成功!");
				}else {
					resp.setReturnCode(Constant.TICKETNOTEXISTS);
					resp.setErrorMessage("卡券不存在!");
					log.error("【礼品卡获赠，编号不存在】,ticketid:{}",ticketid);
				}
			}else {
				resp.setReturnCode(Constant.USERNOTEXISTS);
				resp.setErrorMessage("用户不存在!");
				log.error("【礼品卡获赠，用户不存在】,openid:{}",openid);
			}
			
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("礼品卡获取异常!");
		}
		return resp;
	}
}
