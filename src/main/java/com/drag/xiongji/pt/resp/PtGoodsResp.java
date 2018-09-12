package com.drag.xiongji.pt.resp;

import com.drag.xiongji.common.BaseResponse;

import lombok.Data;

@Data
public class PtGoodsResp extends BaseResponse{
	
	private static final long serialVersionUID = -4195525113654121659L;
	
	private int ptgoodsId;
	
	private String ptcode;
}
