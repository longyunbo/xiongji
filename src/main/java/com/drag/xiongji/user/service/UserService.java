package com.drag.xiongji.user.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.drag.xiongji.kj.dao.KjGoodsDao;
import com.drag.xiongji.kj.dao.KjUserDao;
import com.drag.xiongji.kj.entity.KjGoods;
import com.drag.xiongji.kj.entity.KjUser;
import com.drag.xiongji.ms.dao.MsGoodsDao;
import com.drag.xiongji.ms.dao.MsOrderDao;
import com.drag.xiongji.ms.entity.MsGoods;
import com.drag.xiongji.ms.entity.MsOrder;
import com.drag.xiongji.pt.dao.PtGoodsDao;
import com.drag.xiongji.pt.dao.PtUserDao;
import com.drag.xiongji.pt.entity.PtGoods;
import com.drag.xiongji.pt.entity.PtUser;
import com.drag.xiongji.user.dao.UserDao;
import com.drag.xiongji.user.dao.UserRankLevelDao;
import com.drag.xiongji.user.dao.UserReceivingAddressDao;
import com.drag.xiongji.user.entity.User;
import com.drag.xiongji.user.entity.UserRankLevel;
import com.drag.xiongji.user.entity.UserReceivingAddress;
import com.drag.xiongji.user.form.UserForm;
import com.drag.xiongji.user.resp.UserResp;
import com.drag.xiongji.user.vo.ActivityVo;
import com.drag.xiongji.user.vo.UserReceivingAddressVo;
import com.drag.xiongji.user.vo.UserVo;
import com.drag.xiongji.utils.BeanUtils;
import com.drag.xiongji.utils.DateUtil;
import com.drag.xiongji.wm.form.OrderInfoForm;
import com.drag.xiongji.zl.dao.ZlGoodsDao;
import com.drag.xiongji.zl.dao.ZlUserDao;
import com.drag.xiongji.zl.entity.ZlGoods;
import com.drag.xiongji.zl.entity.ZlUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private PtUserDao ptUserDao;
	@Autowired
	private ZlUserDao zlUserDao;
	@Autowired
	private KjUserDao kjUserDao;
	@Autowired
	private PtGoodsDao ptGoodsDao;
	@Autowired
	private ZlGoodsDao zlGoodsDao;
	@Autowired
	private KjGoodsDao kjGoodsDao;
	@Autowired
	private MsOrderDao msOrderDao;
	@Autowired
	private MsGoodsDao msGoodsDao;
	@Autowired
	private UserRankLevelDao userRankLevelDao;
	@Autowired
	private UserReceivingAddressDao userReceivingAddressDao;

	/**
	 * 检查权限
	 * @return
	 */
	public Boolean checkAuth(User user,String authIds) {
		boolean authFlag = false;
		try {
			if(user != null) {
				int rankLevel = user.getRankLevel();
				UserRankLevel userRankLevel  = userRankLevelDao.findByLevel(rankLevel);
				String auth = userRankLevel.getAuth();
				if(auth.contains(authIds)) {
					authFlag = true;
				}else {
					authFlag = false;
				}
			}
		} catch (Exception e) {
			log.error("检查权限异常,{}",e);
		}
		return authFlag;
	}
    
	
	/**
	 * 新增用户信息
	 * @param form
	 * @return
	 */
	@Transactional
	public UserResp userAdd(UserForm form) {
		UserResp baseResp = new UserResp();
		try {
			User user = new User();
			String openid = form.getOpenid();
			User us = userDao.findByOpenid(openid);
			if(us != null) {
				baseResp.setReturnCode(Constant.FAIL);
				baseResp.setErrorMessage("该用户已存在!");
				return baseResp;
			}
			
			BeanUtils.copyProperties(form, user);
			user.setCreateTime(new Timestamp(System.currentTimeMillis()));
			user.setRankLevel(0);
			userDao.save(user);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("新增用户成功!");
		} catch (Exception e) {
			log.error("新增用户信息异常{}",e);
			baseResp.setReturnCode(Constant.FAIL);
			baseResp.setErrorMessage("系统异常!");
		}
		return baseResp;
	}
	
	
	/**
	 * 更新用户接口
	 * @param form
	 * @return
	 */
	@Transactional
	public UserResp userUpdate(UserForm form) {
		log.info("【更新用户传入参数】form = {}",JSON.toJSONString(form));
		UserResp baseResp = new UserResp();
		try {
			String openid = form.getOpenid();
			User us = userDao.findByOpenid(openid);
			if(us == null) {
				baseResp.setReturnCode(Constant.FAIL);
				baseResp.setErrorMessage("该用户不存在!");
				return baseResp;
			}
			BeanUtils.copyProperties(form, us);
			userDao.saveAndFlush(us);
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("更新用户成功!");
			return baseResp;
		} catch (Exception e) {
			log.error("【更新用户信息异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
	}
	
	/**
	 * 更新用户地址接口
	 * @param form
	 * @return
	 */
	@Transactional
	public UserResp userAddress(OrderInfoForm form) {
		log.info("【更新地址传入参数】form = {}",JSON.toJSONString(form));
		UserResp baseResp = new UserResp();
		try {
			String openid = form.getOpenid();
			User us = userDao.findByOpenid(openid);
			if(us == null) {
				baseResp.setReturnCode(Constant.FAIL);
				baseResp.setErrorMessage("该用户不存在!");
				return baseResp;
			}
			int uid = us.getId();
			//收货人
			String receiptName  = form.getReceiptName();
			//收货人联系方式
			String receiptTel = form.getReceiptTel();
			//所在区域
			String region = form.getRegion();
			//邮政编码
			String postalcode = form.getPostalcode();
			//地址
			String receiptAddress = form.getReceiptAddress();
			UserReceivingAddress address = userReceivingAddressDao.findByUid(uid);
			if(address != null) {
				BeanUtils.copyProperties(form, address);
				address.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				userReceivingAddressDao.saveAndFlush(address);
			}else {
				address = new UserReceivingAddress();
				address.setId(address.getId());
				address.setPostalcode(postalcode);
				address.setReceiptAddress(receiptAddress);
				address.setReceiptName(receiptName);
				address.setReceiptTel(receiptTel);
				address.setRegion(region);
				address.setUid(uid);
				address.setCreateTime(new Timestamp(System.currentTimeMillis()));
				address.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				userReceivingAddressDao.save(address);
			}
			baseResp.setReturnCode(Constant.SUCCESS);
			baseResp.setErrorMessage("更新地址成功!");
			return baseResp;
		} catch (Exception e) {
			log.error("【更新用户地址信息异常】{}",e);
			throw AMPException.getException("系统异常!");
		}
	}
	
	/**
	 * 查询地址信息
	 * @param openid
	 * @return
	 */
	public UserReceivingAddressVo queryAddressByOpenid(String openid) {
		log.info("【根据openid获取用户地址信息】openid = {}",openid);
		UserReceivingAddressVo addressVo = new UserReceivingAddressVo();
		try {
			User user = userDao.findByOpenid(openid);
			if(user != null) {
				int uid = user.getId();
				UserReceivingAddress address = userReceivingAddressDao.findByUid(uid);
				if(address != null) {
					BeanUtils.copyProperties(address, addressVo,new String[]{"createTime", "updateTime"});
					addressVo.setCreateTime((DateUtil.format(user.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
					addressVo.setUpdateTime((DateUtil.format(user.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")));
				}
				
			}
		} catch (Exception e) {
			log.error("获取用户地址异常,{}",e);
		}
		return addressVo;
	}
	
	/**
	 * 根据openid获取用户信息
	 * @param openid
	 * @return
	 */
	public UserVo queryUserByOpenid(String openid) {
		UserVo userVo = new UserVo();
		try {
			User user = userDao.findByOpenid(openid);
			if(user != null) {
				BeanUtils.copyProperties(user, userVo,new String[]{"createTime","updateTime"});
			}
		} catch (Exception e) {
			log.error("检查权限异常,{}",e);
		}
		return userVo;
	}
	
	
	public List<ActivityVo> queryActivityByOpenid(String openid) {
		List<ActivityVo> actList = new ArrayList<ActivityVo>();
		User user = userDao.findByOpenid(openid);
		if(user != null) {
			
			int uid = user.getId();
			List<PtUser> ptList = ptUserDao.findByUid(uid);
			List<KjUser> kjList = kjUserDao.findByUid(uid);
			List<ZlUser> zlList = zlUserDao.findByUid(uid);
			List<MsOrder> msList = msOrderDao.findByUid(uid);
			
			Map<Integer,String> userMap = new HashMap<Integer,String>();
			Set<Integer> ids = new HashSet<Integer>();
			
			Map<Integer,PtGoods> ptGoodsMap = new HashMap<Integer,PtGoods>();
			Map<Integer, KjGoods> kjGoodsMap = new HashMap<Integer, KjGoods>();
			Map<Integer, ZlGoods> zlGoodsMap = new HashMap<Integer, ZlGoods>();
			Map<Integer, MsGoods> msGoodsMap = new HashMap<Integer, MsGoods>();
			Set<Integer> ptGoodsIds = new HashSet<Integer>();
			Set<Integer> kjGoodsIds = new HashSet<Integer>();
			Set<Integer> zlGoodsIds = new HashSet<Integer>();
			Set<Integer> msGoodsIds = new HashSet<Integer>();
			Set<String> ptCodes = new HashSet<String>();
			Set<String> kjCodes = new HashSet<String>();
			Set<String> zlCodes = new HashSet<String>();
			Map<String, Date> ptGroupTimeMap = new HashMap<String, Date>();
			Map<String, Date> zlGroupTimeMap = new HashMap<String, Date>();
			Map<String, Date> kjGroupTimeMap = new HashMap<String, Date>();
			
			if(ptList != null && ptList.size() > 0) {
				for(PtUser us : ptList) {
					ids.add(us.getUid());
					ids.add(us.getGrouperId());
					ptGoodsIds.add(us.getPtgoodsId());
					ptCodes.add(us.getPtcode());
				}
			}
			if(zlList != null && zlList.size() >0) {
				for(ZlUser us : zlList) {
					ids.add(us.getUid());
					ids.add(us.getGrouperId());
					zlGoodsIds.add(us.getZlgoodsId());
					zlCodes.add(us.getZlcode());
				}
			}
			if(kjList != null && kjList.size() > 0) {
				for(KjUser us : kjList) {
					ids.add(us.getUid());
					ids.add(us.getGrouperId());
					kjGoodsIds.add(us.getKjgoodsId());
					kjCodes.add(us.getKjcode());
				}
			}
			if(msList != null && msList.size() > 0) {
				for(MsOrder ms : msList) {
					ids.add(ms.getUid());
					msGoodsIds.add(ms.getMsgoodsId());
				}
			}
			//把用户存在缓存中，不用去循环查询
			if(ids != null && ids.size() > 0) {
				List<User> userList = userDao.findByIdIn(ids);
				for(User us : userList) {
					userMap.put(us.getId(), us.getOpenid());
				}
			}
			//把查询的商品存在map中，也不用循环查询
			if(ptGoodsIds != null && ptGoodsIds.size() > 0) {
				List<PtGoods> ptGoodsList = ptGoodsDao.findByIdIn(ptGoodsIds);
				for(PtGoods pt : ptGoodsList) {
					ptGoodsMap.put(pt.getPtgoodsId(), pt);
				}
			}
			if (kjGoodsIds != null && kjGoodsIds.size() > 0) {
				List<KjGoods> kjGoodsList = kjGoodsDao.findByIdIn(kjGoodsIds);
				for (KjGoods kj : kjGoodsList) {
					kjGoodsMap.put(kj.getKjgoodsId(), kj);
				}
			}
			if (zlGoodsIds != null && zlGoodsIds.size() > 0) {
				List<ZlGoods> zlGoodsList = zlGoodsDao.findByIdIn(zlGoodsIds);
				for (ZlGoods zl : zlGoodsList) {
					zlGoodsMap.put(zl.getZlgoodsId(), zl);
				}
			}
			if (msGoodsIds != null && msGoodsIds.size() > 0) {
				List<MsGoods> msGoodsList = msGoodsDao.findByIdIn(msGoodsIds);
				for (MsGoods ms : msGoodsList) {
					msGoodsMap.put(ms.getMsgoodsId(), ms);
				}
			}
			
			//查询团长的有效时间
			if (ptCodes != null && ptCodes.size() > 0) {
				List<PtUser> users  = ptUserDao.findByPtCodeInAndHead(ptCodes);
				for (PtUser us : users) {
					ptGroupTimeMap.put(us.getPtcode(), us.getCreateTime());
				}
			}
			if (zlCodes != null && zlCodes.size() > 0) {
				List<ZlUser> users  = zlUserDao.findByZlCodeInAndHead(zlCodes);
				for (ZlUser us : users) {
					zlGroupTimeMap.put(us.getZlcode(), us.getCreateTime());
				}
			}
			if (kjCodes != null && kjCodes.size() > 0) {
				List<KjUser> users  = kjUserDao.findByKjCodeInAndHead(kjCodes);
				for (KjUser us : users) {
					kjGroupTimeMap.put(us.getKjcode(), us.getCreateTime());
				}
			}
			
			
			if(ptList != null && ptList.size() > 0) {
				for(PtUser pt : ptList) {
					int goodsId = pt.getPtgoodsId();
					PtGoods goods = ptGoodsMap.get(goodsId);
					ActivityVo vo = new ActivityVo(); 
					vo.setGoodsId(goodsId);
					vo.setGoodsName(goods.getPtgoodsName());
					vo.setType(Constant.TYPE_PT);
					vo.setStatus(pt.getPtstatus());
					vo.setPrice(goods.getPrice());
					vo.setDefPrice(goods.getPtPrice());
					vo.setSize(goods.getPtSize());
					vo.setStartTime(DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setEndTime(DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setGoodsNumber(goods.getPtgoodsNumber());
					vo.setDescription(goods.getDescription());
					vo.setContent(goods.getContent());
					vo.setDragBone(goods.getDragBone());
					vo.setExp(goods.getExp());
					vo.setGoodsThumb(goods.getPtgoodsThumb());
					vo.setCreateTime(DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setIsEnd(goods.getIsEnd());
					vo.setTimes(goods.getPtTimes());
					vo.setSuccTimes(goods.getPtSuccTimes());
					vo.setValidhours(goods.getPtValidhours());
					vo.setCode(pt.getPtcode());
					vo.setUid(userMap.get(pt.getUid()));
					vo.setGrouperId(userMap.get(pt.getGrouperId()));
					vo.setGrouperCreateTime(DateUtil.format(ptGroupTimeMap.get(pt.getPtcode()), "yyyy-MM-dd HH:mm:ss"));
					actList.add(vo);
				}
			}
			
			if(kjList != null && kjList.size() > 0) {
				for(KjUser kj : kjList) {
					int goodsId = kj.getKjgoodsId();
					KjGoods goods = kjGoodsMap.get(goodsId);
					ActivityVo vo = new ActivityVo(); 
					vo.setGoodsId(goodsId);
					vo.setGoodsName(goods.getKjgoodsName());
					vo.setType(Constant.TYPE_KJ);
					vo.setStatus(kj.getKjstatus());
					vo.setPrice(goods.getPrice());
					vo.setDefPrice(goods.getKjPrice());
					vo.setSize(goods.getKjSize());
					vo.setStartTime(DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setEndTime(DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setGoodsNumber(goods.getKjgoodsNumber());
					vo.setDescription(goods.getDescription());
					vo.setContent(goods.getContent());
					vo.setDragBone(goods.getDragBone());
					vo.setExp(goods.getExp());
					vo.setGoodsThumb(goods.getKjgoodsThumb());
					vo.setCreateTime(DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setIsEnd(goods.getIsEnd());
					vo.setTimes(goods.getKjTimes());
					vo.setSuccTimes(goods.getKjSuccTimes());
					vo.setValidhours(goods.getKjValidhours());
					vo.setCode(kj.getKjcode());
					vo.setUid(userMap.get(kj.getUid()));
					vo.setGrouperId(userMap.get(kj.getGrouperId()));
					vo.setGrouperCreateTime(DateUtil.format(kjGroupTimeMap.get(kj.getKjcode()), "yyyy-MM-dd HH:mm:ss"));
					actList.add(vo);
				}
			}
			
			if(zlList != null && zlList.size() >0) {
				for(ZlUser zl : zlList) {
					int goodsId = zl.getZlgoodsId();
					ZlGoods goods = zlGoodsMap.get(goodsId);
					ActivityVo vo = new ActivityVo(); 
					vo.setGoodsId(goodsId);
					vo.setGoodsName(goods.getZlgoodsName());
					vo.setType(Constant.TYPE_ZL);
					vo.setStatus(zl.getZlstatus());
					vo.setPrice(BigDecimal.ZERO);
					vo.setDefPrice(goods.getZlPrice());
					vo.setSize(goods.getZlSize());
					vo.setStartTime(DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setEndTime(DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setGoodsNumber(goods.getZlgoodsNumber());
					vo.setDescription(goods.getDescription());
					vo.setContent(goods.getContent());
					vo.setDragBone(goods.getDragBone());
					vo.setExp(goods.getExp());
					vo.setGoodsThumb(goods.getZlgoodsThumb());
					vo.setCreateTime(DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setIsEnd(goods.getIsEnd());
					vo.setTimes(goods.getZlTimes());
					vo.setSuccTimes(goods.getZlSuccTimes());
					vo.setValidhours(goods.getZlValidhours());
					vo.setCode(zl.getZlcode());
					vo.setUid(userMap.get(zl.getUid()));
					vo.setGrouperId(userMap.get(zl.getGrouperId()));
					vo.setGrouperCreateTime(DateUtil.format(zlGroupTimeMap.get(zl.getZlcode()), "yyyy-MM-dd HH:mm:ss"));
					actList.add(vo);
				}
			}
			
			if(msList != null && msList.size() > 0) {
				for(MsOrder ms : msList) {
					int goodsId = ms.getMsgoodsId();
					MsGoods goods = msGoodsMap.get(goodsId);
					ActivityVo vo = new ActivityVo();
					vo.setGoodsId(ms.getMsgoodsId());
					vo.setGoodsName(ms.getMsgoodsName());
					vo.setType(Constant.TYPE_MS);
					vo.setStatus(ms.getOrderstatus());
					vo.setPrice(ms.getPrice());
					vo.setDefPrice(ms.getPerPrice());
					vo.setSize(1);
					vo.setStartTime(DateUtil.format(goods.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setEndTime(DateUtil.format(goods.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setGoodsNumber(goods.getMsgoodsNumber());
					vo.setDescription(goods.getDescription());
					vo.setContent(goods.getContent());
					vo.setDragBone(goods.getDragBone());
					vo.setExp(goods.getExp());
					vo.setGoodsThumb(goods.getMsgoodsThumb());
					vo.setCreateTime(DateUtil.format(goods.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
					vo.setIsEnd(goods.getIsEnd());
					vo.setSuccTimes(goods.getMsSuccTimes());
					vo.setUid(userMap.get(ms.getUid()));
					vo.setGrouperId(userMap.get(ms.getUid()));
					actList.add(vo);
				}
			}
		}
		return actList;
	}
	
}
