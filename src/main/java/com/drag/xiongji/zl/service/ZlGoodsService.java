package com.drag.xiongji.zl.service;

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
import com.drag.xiongji.pt.entity.PtUser;
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
import com.drag.xiongji.zl.dao.ZlGoodsDao;
import com.drag.xiongji.zl.dao.ZlUserDao;
import com.drag.xiongji.zl.entity.ZlGoods;
import com.drag.xiongji.zl.entity.ZlUser;
import com.drag.xiongji.zl.form.ZlGoodsForm;
import com.drag.xiongji.zl.resp.ZlGoodsResp;
import com.drag.xiongji.zl.vo.ZlGoodsDetailVo;
import com.drag.xiongji.zl.vo.ZlGoodsVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZlGoodsService {

	@Autowired
	private ZlGoodsDao zlGoodsDao;
	@Autowired
	private ZlUserDao zlUserDao;
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
	@Value("${weixin.url.zl.templateid}")
	private String templateid;

	/**
	 * 查询所有的助力商品(助力列表)
	 * @return
	 */
	public List<ZlGoodsVo> listGoods() {
		List<ZlGoodsVo> goodsResp = new ArrayList<ZlGoodsVo>();
		List<ZlGoods> goodsList = zlGoodsDao.findAll();
		if (goodsList != null && goodsList.size() > 0) {
			for (ZlGoods zlgoods : goodsList) {
				ZlGoodsVo resp = new ZlGoodsVo();
				BeanUtils.copyProperties(zlgoods, resp,new String[]{"createTime", "updateTime","startTime","endTime"});
				resp.setCreateTime((DateUtil.format(zlgoods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setUpdateTime((DateUtil.format(zlgoods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setStartTime((DateUtil.format(zlgoods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
				resp.setEndTime((DateUtil.format(zlgoods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
				goodsResp.add(resp);
			}
		}
		return goodsResp;
	}
	
	
	/**
	 * 查询助力详情商品(查询所有发起助力的用户)
	 * @return
	 */
	public ZlGoodsDetailVo goodsDetail(int goodsId) {
		List<UserVo> grouperList = new ArrayList<UserVo>();
		ZlGoodsDetailVo detailVo = new ZlGoodsDetailVo();
		List<ZlUser> groupers = new ArrayList<ZlUser>();
		ZlGoods goods = zlGoodsDao.findGoodsDetail(goodsId);
		if(goods != null) {
			this.copyProperties(goods, detailVo);
			//根据商品编号查询助力团长
			groupers = zlUserDao.findByZlGoodsIdAndIsHead(goodsId, ZlUser.ISHEADER_YES);
			if(groupers != null && groupers.size() > 0) {
				Map<Integer,User> userMap = new HashMap<Integer,User>();
				Set<Integer> ids = new HashSet<Integer>();
				for(ZlUser pu : groupers) {
					ids.add(pu.getGrouperId());
				}
				//把用户存在缓存中，不用去循环查询
				if(ids != null && ids.size() > 0) {
					List<User> userList = userDao.findByIdIn(ids);
					for(User us : userList) {
						userMap.put(us.getId(), us);
					}
				}
				for(ZlUser pu : groupers) {
					UserVo userVo = new UserVo();
					int groupId = pu.getGrouperId();
					User user = userMap.get(groupId);
					userVo.setCode(pu.getZlcode());
					userVo.setIsHeader(pu.getIsHeader());
					userVo.setStatus(pu.getZlstatus());
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
	 * 查询助力活动是否结束
	 * @param goodsId
	 * @return
	 */
	public Boolean checkEnd(int goodsId) {
		boolean endFlag = false;
		ZlGoods goods = zlGoodsDao.findGoodsDetail(goodsId);
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
	 * 本人发起助力
	 * @return
	 */
	@Transactional
	public ZlGoodsResp collage(ZlGoodsForm form) {
		log.info("【本人发起助力,传入参数】form:{}",JSON.toJSONString(form));
		ZlGoodsResp baseResp = new ZlGoodsResp();
		try {
			int zlgoodsId = form.getZlgoodsId();
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【本人发起助力,用户不存在】openid:{}",openid);
				return baseResp;
			}
			ZlGoods goods = zlGoodsDao.findGoodsDetail(zlgoodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				log.error("【本人发起助力,商品编号不存在】ptgoodsId:{}",zlgoodsId);
				return baseResp;
			}
			
			Boolean authFlag =  userService.checkAuth(user, goods.getAuth());
			if(!authFlag) {
				baseResp.setReturnCode(Constant.AUTH_OVER);
				baseResp.setErrorMessage("该用户权限不够!");
				log.error("【本人发起助力,用户权限不够】user:{}",user);
				return baseResp;
			}
			
			//获取系统用户编号
			int uid = user.getId();
			
			List<ZlUser> zlList = zlUserDao.findByUidAndZlgoodsIdAndIsHeadAndZlstatus(uid, zlgoodsId, ZlUser.ISHEADER_YES, ZlUser.PTSTATUS_MIDDLE);
			if(zlList != null && zlList.size() > 0) {
				baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
				baseResp.setErrorMessage("该用户已经助力过此商品，请完成活动后再发起!");
				log.error("【本人发起助力,该用户已经发起此商品，请完成活动后再发起!】kjgoodsId:{},uid:{}",zlgoodsId,uid);
				return baseResp;
			}
			
			
			//购买数量(默认1份)
			int number = 1;
			
			if(goods != null) {
				//减库存
				Boolean flag = this.delStock(goods,number);
				if(!flag) {
					baseResp.setReturnCode(Constant.STOCK_FAIL);
					baseResp.setErrorMessage("库存不足");
					log.error("该商品库存不足,zlgoodsId:{}",zlgoodsId);
					log.error("【该商品库存不足】zlgoodsId:{}",zlgoodsId);
					return baseResp;
				}
			}
			
//			//生成一个助力编号
			String zlCode = StringUtil.uuid();
			//助力用户表中也插入一条数据
			ZlUser zlUser = new ZlUser();
			zlUser.setUid(uid);
			zlUser.setGrouperId(uid);
			zlUser.setZlgoodsId(form.getZlgoodsId());
			zlUser.setZlgoodsName(goods.getZlgoodsName());
			zlUser.setZlcode(zlCode);
			zlUser.setIsHeader(ZlUser.ISHEADER_YES);
			zlUser.setZlSize(goods.getZlSize());
			zlUser.setZlstatus(ZlUser.PTSTATUS_MIDDLE);
			zlUser.setFormId(form.getFormId());
			zlUser.setZlPrice(goods.getZlPrice());
			//付款金额，随机生成一个数字，
//			zlUser.setPrice(BigDecimal.ZERO);
			
			zlUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			zlUserDao.save(zlUser);
			
			//新增助力次数
			this.addZlTimes(goods);
			
			//返回参数
			baseResp.setZlgoodsId(zlgoodsId);
			baseResp.setZlcode(zlCode);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("助力成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
			throw AMPException.getException("系统异常!");
		}
		
		return baseResp;
	}
	
	/**
	 * 本人查询助力详情
	 * @param zlcode
	 * @return
	 */
	public ZlGoodsDetailVo myDetail(String zlcode) {
		List<UserVo> grouperList = new ArrayList<UserVo>();
		ZlGoodsDetailVo detailVo = new ZlGoodsDetailVo();
		List<ZlUser> groupers = new ArrayList<ZlUser>();
		
		List<ZlUser> zlUserList = zlUserDao.findByZlCodeAndIsHead(zlcode,ZlUser.ISHEADER_YES);
		if(zlUserList != null && zlUserList.size() > 0) {
			//团长
			ZlUser grouper = zlUserList.get(0);
			if(grouper != null) {
				int zlgoodsId = grouper.getZlgoodsId();
				ZlGoods goods = zlGoodsDao.findGoodsDetail(zlgoodsId);
				if(goods != null ) {
					this.copyProperties(goods, detailVo);
					detailVo.setGroupStartTime(DateUtil.format(grouper.getCreateTime(), "yyyy-MM-dd HH:mm:ss")); 
					//根据商品编号，助力code，查询好友助力信息
					groupers = zlUserDao.findByZlCode(zlcode);
					if(groupers != null && groupers.size() > 0) {
						Map<Integer,User> userMap = new HashMap<Integer,User>();
						Set<Integer> ids = new HashSet<Integer>();
						for(ZlUser pu : groupers) {
							ids.add(pu.getUid());
						}
						//把用户存在缓存中，不用去循环查询
						if(ids != null && ids.size() > 0) {
							List<User> userList = userDao.findByIdIn(ids);
							for(User us : userList) {
								userMap.put(us.getId(), us);
							}
						}
						for(ZlUser pu : groupers) {
							UserVo userVo = new UserVo();
							int uid = pu.getUid();
							User user = userMap.get(uid);
							userVo.setCode(pu.getZlcode());
							userVo.setIsHeader(pu.getIsHeader());
							userVo.setStatus(pu.getZlstatus());
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
		}
		return detailVo;
	}
	
	/**
	 * 分享给好友，好友助力
	 * @param form
	 * @return
	 */
	@Transactional
	public ZlGoodsResp friendcollage(ZlGoodsForm form) {
		log.info("【好友助力,传入参数】form:{}",JSON.toJSONString(form));
		ZlGoodsResp baseResp = new ZlGoodsResp();
		try {
			//助力规模
			int zlSize = 0;
			//已经助力的人数
			int grouperSize = 0;
			//助力编号
			String zlCode = form.getZlCode();
			//商品编号
			int zlgoodsId = form.getZlgoodsId();
			String openid = form.getOpenid();
			User user = userDao.findByOpenid(openid);
			if(user == null) {
				baseResp.setReturnCode(Constant.USERNOTEXISTS);
				baseResp.setErrorMessage("该用户不存在!");
				log.error("【好友助力,用户不存在】openid:{}",openid);
				return baseResp;
			}
			//获取系统用户编号
			int uid = user.getId();
			
			
			ZlGoods goods = zlGoodsDao.findGoodsDetail(zlgoodsId);
			if(goods == null) {
				baseResp.setReturnCode(Constant.PRODUCTNOTEXISTS);
				baseResp.setErrorMessage("该商品编号不存在!");
				log.error("【好友助力,商品编号不存在】zlgoodsId:{}",zlgoodsId);
				return baseResp;
			}
			
//			Boolean authFlag =  userService.checkAuth(user, goods.getAuth());
//			if(!authFlag) {
//				baseResp.setReturnCode(Constant.AUTH_OVER);
//				baseResp.setErrorMessage("该用户权限不够!");
//				log.error("【好友助力,用户权限不够】user:{}",user);
//				return baseResp;
//			}
			//同一个用户助力校验
//			ZlUser zluser = zlUserDao.findByZlGoodsIdAndUidAndZlCode(zlgoodsId, uid, zlCode);
			List<ZlUser> zluser = zlUserDao.findByZlGoodsIdAndUidAndIsHeader(zlgoodsId, uid, ZlUser.ISHEADER_NO);
			if(zluser != null  && zluser.size() > 0) {
				baseResp.setReturnCode(Constant.USERALREADYIN_FAIL);
				baseResp.setErrorMessage("该用户已经助力过此商品，不能再助力!");
				log.error("【好友助力,该用户已经助力过此商品，不能再助力】zlgoodsId:{},uid:{},zlCode:{}",zlgoodsId,uid,zlCode);
				return baseResp;
			}
			//根据助力编号查询
			List<ZlUser> zlList = zlUserDao.findByZlCode(zlCode);
			//获取团长
			ZlUser grouper = null;
			for(ZlUser us: zlList) {
				if(us.getIsHeader() == PtUser.ISHEADER_YES) {
					grouper = us;
				}
			}
			
			//助力已完成校验
			if(zlList != null && zlList.size() > 0) {
				zlSize = goods.getZlSize();
				//已经助力的人数，本人发起助力不算，减去1
				grouperSize = zlList.size() - 1;
				if(grouperSize >= zlSize) {
					baseResp.setReturnCode(Constant.ACTIVITYALREADYDOWN_FAIL);
					baseResp.setErrorMessage("该团助力已完成，不能再助力!");
					log.error("【好友助力,该团助力已完成，不能再助力】zlCode:{}",zlCode);
					return baseResp;
				}
			}else {
				baseResp.setReturnCode(Constant.ACTIVITYNOTEXISTS);
				baseResp.setErrorMessage("该助力编号不存在!");
				log.error("【好友助力,该助力编号不存在】zlCode:{}",zlCode);
				return baseResp;
			}
			
			//购买数量
//			int number = 1;
//			if(goods != null) {
//				//减库存
//				Boolean flag = this.delStock(goods,number);
//				if(!flag) {
//					baseResp.setReturnCode(Constant.STOCK_FAIL);
//					baseResp.setErrorMessage("库存不足");
//					log.error("【该商品库存不足】,zlgoodsId:{}",zlgoodsId);
//					return baseResp;
//				}
//			}
			
			//助力用户表中也插入一条数据
			ZlUser zlUser = new ZlUser();
			zlUser.setId(zlUser.getId());
			zlUser.setUid(uid);
			zlUser.setGrouperId(grouper.getGrouperId());
			zlUser.setZlgoodsId(form.getZlgoodsId());
			zlUser.setZlgoodsName(goods.getZlgoodsName());
			zlUser.setZlcode(zlCode);
			zlUser.setIsHeader(ZlUser.ISHEADER_NO);
			zlUser.setZlSize(goods.getZlSize());
			zlUser.setZlPrice(goods.getZlPrice());
			zlUser.setFormId(form.getFormId());
			
			zlUser.setZlstatus(ZlUser.PTSTATUS_MIDDLE);
			zlUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
			zlUserDao.save(zlUser);
			zlList.add(zlUser);
			//新增助力次数
			this.addZlTimes(goods);
			this.updateSuccess(zlList,goods,zlCode);
			
			baseResp.setZlgoodsId(zlgoodsId);
			baseResp.setZlcode(zlCode);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("助力成功！");
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
			throw AMPException.getException("系统异常!");
		}
		
		return baseResp;
	}
	
	
	/**
	 * 更新完成助力人数
	 * 发送卡券
	 * @param form
	 */
	@Transactional
	public void updateSuccess(List<ZlUser> zlList,ZlGoods goods,String zlCode) {
		try {
			int zlSize = 0;
			int grouperSize = 0;
			if(zlList != null && zlList.size() > 0) {
				zlSize = goods.getZlSize();
				//已经助力的人数，本人发起助力不算，减去1
				grouperSize = zlList.size() - 1;
				if(grouperSize == zlSize) {
					//如果人数达到助力人数规模，更新助力状态为助力成功
					zlUserDao.updateZlstatus(zlCode);
					int zlSuccTimes = goods.getZlSuccTimes();
					goods.setZlSuccTimes(zlSuccTimes + zlSize);
					zlGoodsDao.saveAndFlush(goods);
					
					String type = Constant.TYPE_ZL;
					UserTicketTemplate  template = userTicketTemplateDao.findByGoodsIdAndType(goods.getZlgoodsId(), type);
					
					Map<String,User> userMap = new HashMap<String,User>();
					Set<Integer> ids = new HashSet<Integer>();
					
					ZlUser grouper = null;
					for(ZlUser user : zlList) {
						if(ZlUser.ISHEADER_YES == user.getIsHeader()) {
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
					
					for(ZlUser user : zlList) {
						int isHeader = user.getIsHeader();
//						User us = userDao.findOne(user.getUid());
						String uid = String.valueOf(user.getUid());
						//给团长发送卡券
						if(ZlUser.ISHEADER_YES == isHeader) {
							//新增恐龙骨
							dragGoodsService.addDragBone(userMap.get(uid),goods.getZlgoodsId(),goods.getZlgoodsName(),Constant.TYPE_ZL,goods.getDragBone(), goods.getExp());
							if(template != null) {
								UserTicket ticket = new UserTicket();
								BeanUtils.copyProperties(template, ticket);
								ticket.setId(ticket.getId());
								ticket.setUid(user.getUid());
								ticket.setNumber(1);
								ticket.setStatus(UserTicket.STATUS_NO);
								ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
								userTicketDao.save(ticket);
								log.info("【助力发送卡券插入成功】ticket:{}",JSON.toJSONString(ticket));
							}
							
							JSONObject json = new JSONObject();
							//openid
							json.put("touser", userMap.get(uid).getOpenid());
							json.put("template_id", templateid);
							json.put("page", "pages/help/helpdetail/helpdetail?shopid=" + user.getZlgoodsId() + "&isFinish=1");
							json.put("form_id", user.getFormId());
							//商品名称
							JSONObject keyword1 = new JSONObject();
							keyword1.put("value", goods.getZlgoodsName());
							keyword1.put("color", "#000000");
							//助力结果
							JSONObject keyword2 = new JSONObject();
							keyword2.put("value", "助力成功！");
							keyword2.put("color", "#000000");
							
							JSONObject data = new JSONObject();
							data.put("keyword1", keyword1);
							data.put("keyword2", keyword2);
							json.put("data", data);
							boolean result =  WxUtil.sendTemplateMsg(json);
							if(result) {
								user.setSendstatus(Constant.SENDSTATUS_SUCC);
							}else {
								user.setSendstatus(Constant.SENDSTATUS_FAIL);
							}
							zlUserDao.saveAndFlush(user);
							
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("系统异常,{}",e);
			throw AMPException.getException("系统异常!");
		}
		
	}
	
	
	/**
	 * 助力商品减库存
	 * @param goods
	 * @param number
	 * @return
	 */
	public Boolean delStock(ZlGoods goods, int number) {
		boolean flag = false;
		int zlgoodsNumber = goods.getZlgoodsNumber();
		if (zlgoodsNumber - number < 0) {
			// 库存不足
			flag = false;
		} else {
			flag = true;
			int nowGoodsNum = zlgoodsNumber - number;
			goods.setZlgoodsNumber(nowGoodsNum);
			zlGoodsDao.saveAndFlush(goods);
		}
		return flag;
	}
	
	/**
	 * 增加助力次数
	 * @param goods
	 * @param number
	 */
	public void addZlTimes(ZlGoods goods) {
		int zlTimes = goods.getZlTimes();
		goods.setZlTimes(zlTimes + 1);
		zlGoodsDao.saveAndFlush(goods);
	}
	
	/**
	 * 提取copy方法
	 * @param goods
	 * @param detailVo
	 */
	public void copyProperties(ZlGoods goods,ZlGoodsDetailVo detailVo) {
		BeanUtils.copyProperties(goods, detailVo,new String[]{"createTime", "updateTime","startTime","endTime"});
		detailVo.setCreateTime((DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setUpdateTime((DateUtil.format(goods.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setStartTime((DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss")));
		detailVo.setEndTime((DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss")));
	}
}
