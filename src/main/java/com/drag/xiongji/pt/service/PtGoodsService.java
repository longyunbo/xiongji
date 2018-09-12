package com.drag.xiongji.pt.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.drag.xiongji.common.Constant;
import com.drag.xiongji.common.exception.AMPException;
import com.drag.xiongji.pt.dao.PtGoodsDao;
import com.drag.xiongji.pt.dao.PtOrderDao;
import com.drag.xiongji.pt.dao.PtUserDao;
import com.drag.xiongji.pt.entity.PtGoods;
import com.drag.xiongji.pt.entity.PtOrder;
import com.drag.xiongji.pt.entity.PtUser;
import com.drag.xiongji.pt.form.PtGoodsForm;
import com.drag.xiongji.pt.resp.PtGoodsResp;
import com.drag.xiongji.pt.vo.PtGoodsDetailVo;
import com.drag.xiongji.pt.vo.PtGoodsVo;
import com.drag.xiongji.user.dao.UserDao;
import com.drag.xiongji.user.dao.UserTicketDao;
import com.drag.xiongji.user.dao.UserTicketTemplateDao;
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.user.entity.UserTicket;
import com.drag.xiongji.user.entity.UserTicketTemplate;
import com.drag.xiongji.user.service.DragGoodsService;
import com.drag.xiongji.user.service.UserService;
import com.drag.xiongji.user.vo.UserVo;
import com.drag.xiongji.utils.BeanUtils;
import com.drag.xiongji.utils.DateUtil;
import com.drag.xiongji.utils.StringUtil;
import com.drag.xiongji.utils.WxUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PtGoodsService {

	@Autowired
	private PtGoodsDao ptGoodsDao;
	@Autowired
	private PtUserDao ptUserDao;
	@Autowired
	private PtOrderDao ptOrderDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserTicketTemplateDao userTicketTemplateDao;
	@Autowired
	private UserTicketDao userTicketDao;
	@Autowired
	private DragGoodsService dragGoodsService;
	@Autowired
	private UserService userService;
	@Value("${weixin.url.pt.templateid}")
	private String templateid;

	/**
	 * 查询所有的拼团商品(拼团列表)
	 * @return
	 */
	public List<PtGoodsVo> listGoods() {
		List<PtGoodsVo> goodsResp = new ArrayList<PtGoodsVo>();
		List<PtGoods> goodsList = ptGoodsDao.findAll();
		if (goodsList != null && goodsList.size() > 0) {
			for (PtGoods ptgoods : goodsList) {
				PtGoodsVo resp = new PtGoodsVo();
				BeanUtils.copyProperties(ptgoods, resp,new String[]{"createTime", "updateTime","startTime","endTime"});
				resp.setCreateTime((DateUtil.format(ptgoods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setUpdateTime((DateUtil.format(ptgoods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setStartTime((DateUtil.format(ptgoods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setEndTime((DateUtil.format(ptgoods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	
	/**
	 * 查询拼团详情商品
	 * @return
	 */
	public PtGoodsDetailVo goodsDetail(int goodsId) {
		List<UserVo> grouperList = new ArrayList<UserVo>();
		PtGoodsDetailVo detailVo = new PtGoodsDetailVo();
		List<PtUser> groupers = new ArrayList<PtUser>();
		PtGoods goods = ptGoodsDao.findGoodsDetail(goodsId);
		if(goods != null) {
			this.copyProperties(goods, detailVo);
			//根据商品编号查询拼团团长
			groupers = ptUserDao.findByPtGoodsIdAndIsHead(goodsId, PtUser.ISHEADER_YES);
			if(groupers != null && groupers.size() > 0) {
				Map<Integer,User> userMap = new HashMap<Integer,User>();
				Set<Integer> ids = new HashSet<Integer>();
				for(PtUser pu : groupers) {
					ids.add(pu.getGrouperId());
				}
				//把用户存在缓存中，不用去循环查询
				if(ids != null && ids.size() > 0) {
					List<User> userList = userDao.findByIdIn(ids);
					for(User us : userList) {
						userMap.put(us.getId(), us);
					}
				}
				for(PtUser pu : groupers) {
					UserVo userVo = new UserVo();
					int groupId = pu.getGrouperId();
					userVo.setCode(pu.getPtcode());
					userVo.setIsHeader(pu.getIsHeader());
					userVo.setStatus(pu.getPtstatus());
					User user = userMap.get(groupId);
					if(user != null) {
						BeanUtils.copyProperties(user, userVo,new String[]{"createTime"});
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
	 * 查询拼团活动是否结束
	 * @param goodsId
	 * @return
	 */
	public Boolean checkEnd(int goodsId) {
		boolean endFlag = false;
		PtGoods goods = ptGoodsDao.findGoodsDetail(goodsId);
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
	 * 本人发起拼团
	 * @return
	 */
	@Transactional
	public PtGoodsResp collage(PtGoodsForm form) {
		log.info("【本人发起拼团,传入参数】form:{}",JSON.toJSONString(form));
		PtGoodsResp baseResp = new PtGoodsResp();
		try {
			int ptgoodsId = form.getPtgoodsId();
			String openid = form.getOpenid();
			PtGoods goods = ptGoodsDao.findGoodsDetail(ptgoodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				log.error("【本人发起拼团,商品编号不存在】ptgoodsId:{}",ptgoodsId);
				return baseResp;
			}
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【本人发起拼团,用户不存在】openid:{}",openid);
				return baseResp;
			}
			
			Boolean authFlag =  userService.checkAuth(user, goods.getAuth());
			if(!authFlag) {
				baseResp.setReturnCode(Constant.AUTH_OVER);
				baseResp.setErrorMessage("该用户权限不够!");
				log.error("【本人发起拼团,用户权限不够】user:{}",user);
				return baseResp;
			}
			//获取系统用户编号
			int uid = user.getId();
			List<PtUser> ptList = ptUserDao.findByUidAndPtgoodsIdAndIsHeadAndPtstatus(uid, ptgoodsId, PtUser.ISHEADER_YES, PtUser.PTSTATUS_MIDDLE);
			if(ptList != null && ptList.size() > 0) {
				baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
				baseResp.setErrorMessage("该用户已经拼过此团，请完成后再拼团!");
				log.error("【本人发起拼团,该用户已经买此商品，请完成活动后再发起!】ptgoodsId:{},uid:{}",ptgoodsId,uid);
				return baseResp;
			}
			
			//购买数量
			int number = form.getNumber();
			if(goods != null) {
				//减库存
				Boolean flag = this.delStock(goods,number);
				if(!flag) {
					baseResp.setReturnCode(Constant.STOCK_FAIL);
					baseResp.setErrorMessage("库存不足");
					log.error("该商品库存不足,ptgoodsId:{}",ptgoodsId);
					log.error("【该商品库存不足】ptgoodsId:{}",ptgoodsId);
					return baseResp;
				}
			}
			
			//点击拼团，需要在拼团订单表中插入一条数据
			PtOrder ptOrder = new PtOrder();
			BeanUtils.copyProperties(form, ptOrder);
			//生成一个拼团编号
			String ptCode = StringUtil.uuid();
			ptOrder.setUid(uid);
			ptOrder.setPtcode(ptCode);
			ptOrder.setPtgoodsName(goods.getPtgoodsName());
			ptOrder.setPerPrice(goods.getPtPrice());
			ptOrder.setPrice(goods.getPrice());
			ptOrder.setOrderstatus(PtOrder.ORDERSTATUS_SUCCESS);
			ptOrder.setIsHeader(PtUser.ISHEADER_YES);
			ptOrder.setAddtime(new Timestamp(System.currentTimeMillis()));
			ptOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ptOrderDao.save(ptOrder);
			
			log.info("【拼团订单插入成功】ptOrder:{}",JSON.toJSONString(ptOrder));
			
			//拼团用户表中也插入一条数据
			PtUser ptUser = new PtUser();
			ptUser.setUid(uid);
			ptUser.setGrouperId(uid);
			ptUser.setPtgoodsId(ptgoodsId);
			ptUser.setPtcode(ptCode);
			ptUser.setNumber(number);
			ptUser.setIsHeader(PtUser.ISHEADER_YES);
			ptUser.setPtSize(goods.getPtSize());
			ptUser.setPtstatus(PtUser.PTSTATUS_MIDDLE);
			ptUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ptUser.setFormId(form.getFormId());
			ptUserDao.save(ptUser);
			
			log.info("【拼团用户插入成功】ptUser:{}",JSON.toJSONString(ptUser));
			
			//新增拼团次数
			this.addPtTimes(goods);
			
			//返回参数
			baseResp.setPtgoodsId(ptgoodsId);
			baseResp.setPtcode(ptCode);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("拼团成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
			throw AMPException.getException("系统异常!");
		}
		
		return baseResp;
	}
	
	/**
	 * 本人(好友)查询拼团详情
	 * @param ptcode
	 * @return
	 */
	public PtGoodsDetailVo myDetail(String ptcode) {
		List<UserVo> grouperList = new ArrayList<UserVo>();
		PtGoodsDetailVo detailVo = new PtGoodsDetailVo();
		List<PtUser> groupers = new ArrayList<PtUser>();
		PtOrder ptOrder = ptOrderDao.findByPtCodeAndIsHeader(ptcode,PtOrder.ISHEADER_YES);
		if(ptOrder != null) {
			int ptgoodsId = ptOrder.getPtgoodsId();
			PtGoods goods = ptGoodsDao.findGoodsDetail(ptgoodsId);
			if(goods != null ) {
				this.copyProperties(goods, detailVo);
				groupers = ptUserDao.findByPtCode(ptcode);
				if(groupers != null && groupers.size() > 0) {
					Map<Integer,User> userMap = new HashMap<Integer,User>();
					Set<Integer> ids = new HashSet<Integer>();
					for(PtUser pu : groupers) {
						ids.add(pu.getUid());
					}
					//把用户存在缓存中，不用去循环查询
					if(ids != null && ids.size() > 0) {
						List<User> userList = userDao.findByIdIn(ids);
						for(User us : userList) {
							userMap.put(us.getId(), us);
						}
					}
					for(PtUser pu : groupers) {
						UserVo userVo = new UserVo();
						int uid = pu.getUid();
						User user = userMap.get(uid);
						userVo.setStatus(pu.getPtstatus());
						userVo.setIsHeader(pu.getIsHeader());
						userVo.setCode(pu.getPtcode());
						if(user != null) {
							BeanUtils.copyProperties(user, userVo,new String[]{"createTime"});
							userVo.setCreateTime(DateUtil.format(pu.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
							grouperList.add(userVo);
						}
					}
				}
			}
			detailVo.setGroupers(grouperList);
		}
		return detailVo;
	}
	
	/**
	 * 分享给好友，好友参团
	 * @param form
	 * @return
	 */
	@Transactional
	public PtGoodsResp friendcollage(PtGoodsForm form) {
		log.info("【好友拼团,传入参数】form:{}",JSON.toJSONString(form));
		PtGoodsResp baseResp = new PtGoodsResp();
		try {
			//拼团规模
			int ptSize = 0;
			//已经拼团的人数
			int grouperSize = 0;
			//拼团编号
			String ptCode = form.getPtCode();
			//商品编号
			int ptgoodsId = form.getPtgoodsId();
			
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【好友发起拼团,用户不存在】openid:{}",openid);
				return baseResp;
			}
			//获取系统用户编号
			int uid = user.getId();
			//根据拼团编号查询
			PtGoods goods = ptGoodsDao.findGoodsDetail(ptgoodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				log.error("【好友发起拼团,商品编号不存在】ptgoodsId:{}",ptgoodsId);
				return baseResp;
			}
			
			Boolean authFlag =  userService.checkAuth(user, goods.getAuth());
			if(!authFlag) {
				baseResp.setReturnCode(Constant.AUTH_OVER);
				baseResp.setErrorMessage("该用户权限不够!");
				log.error("【好友发起拼团,用户权限不够】user:{}",user);
				return baseResp;
			}
			
			//同一个用户拼团校验
			PtUser ptuser = ptUserDao.findByPtGoodsIdAndUidAndPtCode(ptgoodsId, uid, ptCode);
			if(ptuser != null) {
				baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
				baseResp.setErrorMessage("该用户已经拼过此团，不能再拼团!");
				log.error("【好友发起拼团,该用户已经拼过此团，不能再拼团!】ptCode:{},ptgoodsId:{},uid:{}",ptCode,ptgoodsId,uid);
				return baseResp;
			}
			
			List<PtUser> ptList = ptUserDao.findByPtCode(ptCode);
			//获取团长
			PtUser grouper = null;
			for(PtUser us: ptList) {
				if(us.getIsHeader() == PtUser.ISHEADER_YES) {
					grouper = us;
				}
			}
			
			//拼团已完成校验
			if(ptList != null && ptList.size() > 0) {
				ptSize = goods.getPtSize();
				//已经拼团的人数
				grouperSize = ptList.size();
				if(grouperSize >= ptSize) {
					baseResp.setReturnCode(Constant.ACTIVITYALREADYDOWN_FAIL);
					baseResp.setErrorMessage("该团拼团已完成，不能再拼团!");
					log.error("【好友发起拼团,该团已完成，不能再拼团】ptCode:{}",ptCode);
					return baseResp;
				}
			}else {
				baseResp.setReturnCode(Constant.ACTIVITYNOTEXISTS);
				baseResp.setErrorMessage("该拼团编号不存在!");
				log.error("【好友发起拼团,该拼团编号不存在】ptCode:{}",ptCode);
				return baseResp;
			}
			
			
			//购买数量
			int number = form.getNumber();
			if(goods != null) {
				//减库存
				Boolean flag = this.delStock(goods,number);
				if(!flag) {
					baseResp.setReturnCode(Constant.STOCK_FAIL);
					baseResp.setErrorMessage("库存不足");
					log.error("【好友发起拼团,该商品库存不足】,ptgoodsId:{}",ptgoodsId);
					return baseResp;
				}
			}
			
			//点击拼团，需要在拼团订单表中插入一条数据
			PtOrder ptOrder = new PtOrder();
			ptOrder.setUid(uid);
			BeanUtils.copyProperties(form, ptOrder);
			ptOrder.setPtgoodsName(goods.getPtgoodsName());
			ptOrder.setPerPrice(goods.getPtPrice());
			ptOrder.setPrice(goods.getPrice());
			ptOrder.setPtcode(ptCode);
			ptOrder.setOrderstatus(PtOrder.ORDERSTATUS_SUCCESS);
			ptOrder.setIsHeader(PtUser.ISHEADER_NO);
			ptOrder.setAddtime(new Timestamp(System.currentTimeMillis()));
			ptOrder.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ptOrderDao.save(ptOrder);
			
			log.info("【好友拼团订单插入成功】ptOrder:{}",JSON.toJSONString(ptOrder));
			
			//拼团用户表中也插入一条数据
			PtUser ptUser = new PtUser();
			ptUser.setId(ptUser.getId());
			ptUser.setUid(uid);
			ptUser.setGrouperId(grouper.getGrouperId());
			ptUser.setPtgoodsId(form.getPtgoodsId());
			ptUser.setPtcode(ptCode);
			ptUser.setNumber(number);
			ptUser.setIsHeader(PtUser.ISHEADER_NO);
			ptUser.setPtSize(goods.getPtSize());
			ptUser.setPtstatus(PtUser.PTSTATUS_MIDDLE);
			ptUser.setFormId(form.getFormId());
			ptUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ptUserDao.save(ptUser);
			
			log.info("【好友拼团用户插入成功】ptUser:{}",JSON.toJSONString(ptUser));
			
			ptList.add(ptUser);
			//新增拼团次数
			this.addPtTimes(goods);
			//更新完成拼团人数，发送卡券
			this.updateSuccess(ptList,goods,ptCode);
			
			baseResp.setPtgoodsId(ptgoodsId);
			baseResp.setPtcode(ptCode);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("拼团成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
			throw AMPException.getException("系统异常!");
		}
		
		return baseResp;
	}
	
	
	/**
	 * 拼团完成后
	 * 更新完成拼团人数
	 * 发送卡券
	 * @param form
	 */
	@Transactional
	public void updateSuccess(List<PtUser> ptList,PtGoods goods,String ptCode) {
		try {
			int ptSize = 0;
			int grouperSize = 0;
			if(ptList != null && ptList.size() > 0) {
				ptSize = goods.getPtSize();
				//已经拼团的人数
				grouperSize = ptList.size();
				if(grouperSize == ptSize) {
					//如果人数达到拼团人数规模，更新拼团状态为拼团成功
					ptUserDao.updatePtstatus(ptCode);
					int ptSuccTimes = goods.getPtSuccTimes();
					goods.setPtSuccTimes(ptSuccTimes + ptSize);
					ptGoodsDao.saveAndFlush(goods);
					
					String type = Constant.TYPE_PT;
					UserTicketTemplate  template = userTicketTemplateDao.findByGoodsIdAndType(goods.getPtgoodsId(), type);
					
					Map<String,User> userMap = new HashMap<String,User>();
					Set<Integer> ids = new HashSet<Integer>();
					
					PtUser grouper = null;
					for(PtUser user : ptList) {
						if(PtUser.ISHEADER_YES == user.getIsHeader()) {
							grouper = user;
						}
						ids.add(user.getUid());
					}
					List<User> userList = userDao.findByIdIn(ids);
					if(userList != null && userList.size() > 0) {
						for(User user : userList) {
							userMap.put(String.valueOf(user.getId()), user);
						}
					}
					
					for(PtUser user : ptList) {
						//发送卡券
						if(template != null) {
							int number = user.getNumber();
							for(int i = 0;i < number; i++) {
								UserTicket ticket = new UserTicket();
								BeanUtils.copyProperties(template, ticket);
								ticket.setId(ticket.getId());
								ticket.setUid(user.getUid());
								ticket.setNumber(1);
								ticket.setStatus(UserTicket.STATUS_NO);
								ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
								userTicketDao.save(ticket);
								log.info("【拼团发送卡券插入成功】ticket:{}",JSON.toJSONString(ticket));
							}
						}
						//发送消息
						JSONObject json = new JSONObject();
//						User us = userDao.findOne(user.getUid());
						String uid = String.valueOf(user.getUid());
						//openid
						json.put("touser",userMap.get(uid).getOpenid());
						json.put("template_id", templateid);
						json.put("page", "pages/collage/collagedetail/collagedetail?shopid=" + user.getPtgoodsId() + "&isFinish=1");
						json.put("form_id", user.getFormId());
						//商品名称
						JSONObject keyword1 = new JSONObject();
						keyword1.put("value", goods.getPtgoodsName());
						keyword1.put("color", "#000000");
						//团长
						JSONObject keyword2 = new JSONObject();
//						User usGrouper = userDao.findOne(user.getGrouperId());
						keyword2.put("value",userMap.get(String.valueOf(grouper.getUid())).getNickname());
						keyword2.put("color", "#000000");
						//拼团人数
						JSONObject keyword3 = new JSONObject();
						keyword3.put("value", grouperSize);
						keyword3.put("color", "#000000");
						
						JSONObject data = new JSONObject();
						data.put("keyword1", keyword1);
						data.put("keyword2", keyword2);
						data.put("keyword3", keyword3);
						json.put("data", data);
						boolean result =  WxUtil.sendTemplateMsg(json);
						if(result) {
							user.setSendstatus(Constant.SENDSTATUS_SUCC);
						}else {
							user.setSendstatus(Constant.SENDSTATUS_FAIL);
						}
						ptUserDao.saveAndFlush(user);
						
						//新增恐龙骨
						dragGoodsService.addDragBone(userMap.get(uid), goods.getPtgoodsId(),goods.getPtgoodsName(),Constant.TYPE_PT,goods.getDragBone(), goods.getExp());
					}
				}
			}
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("系统异常!");
		}
	}
	
	
	/**
	 * 拼团商品减库存
	 * @param goods
	 * @param number
	 * @return
	 */
	public Boolean delStock(PtGoods goods, int number) {
		boolean flag = false;
		int ptgoodsNumber = goods.getPtgoodsNumber();
		if (ptgoodsNumber - number < 0) {
			// 库存不足
			flag = false;
		} else {
			flag = true;
			int nowGoodsNum = ptgoodsNumber - number;
			goods.setPtgoodsNumber(nowGoodsNum);
			ptGoodsDao.saveAndFlush(goods);
		}
		return flag;
	}
	
	/**
	 * 增加拼团次数
	 * @param goods
	 * @param number
	 */
	public void addPtTimes(PtGoods goods) {
		int ptTimes = goods.getPtTimes();
		goods.setPtTimes(ptTimes + 1);
		ptGoodsDao.saveAndFlush(goods);
	}
	
	/**
	 * 提取copy方法
	 * @param goods
	 * @param detailVo
	 */
	public void copyProperties(PtGoods goods,PtGoodsDetailVo detailVo) {
		BeanUtils.copyProperties(goods, detailVo,new String[]{"createTime", "updateTime","startTime","endTime"});
		detailVo.setCreateTime((DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setUpdateTime((DateUtil.format(goods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setStartTime((DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setEndTime((DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
	}
}
