package com.drag.xiongji.pay.model;

public class PayReturnResultInfo {
	 	private String return_code;
	    private String return_msg;
	    private String result_code;
	    private String appid;
	    private String mch_id;
	    private String nonce_str;
	    private String out_trade_no;
	    private String out_refund_no;
	    private String sign;
	    private String prepay_id;
	    private String trade_type;
	    private String err_code;
	    private String err_code_des;
	    private String transaction_id;
	    private String refund_id;
	    private int refund_fee;
	    private int total_fee;
	    private int cash_fee;
	    
	    
		public String getReturn_code() {
			return return_code;
		}
		public void setReturn_code(String return_code) {
			this.return_code = return_code;
		}
		public String getReturn_msg() {
			return return_msg;
		}
		public void setReturn_msg(String return_msg) {
			this.return_msg = return_msg;
		}
		public String getResult_code() {
			return result_code;
		}
		public void setResult_code(String result_code) {
			this.result_code = result_code;
		}
		public String getAppid() {
			return appid;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}
		public String getMch_id() {
			return mch_id;
		}
		public void setMch_id(String mch_id) {
			this.mch_id = mch_id;
		}
		public String getNonce_str() {
			return nonce_str;
		}
		public void setNonce_str(String nonce_str) {
			this.nonce_str = nonce_str;
		}
		public String getSign() {
			return sign;
		}
		public void setSign(String sign) {
			this.sign = sign;
		}
		public String getPrepay_id() {
			return prepay_id;
		}
		public void setPrepay_id(String prepay_id) {
			this.prepay_id = prepay_id;
		}
		public String getTrade_type() {
			return trade_type;
		}
		public void setTrade_type(String trade_type) {
			this.trade_type = trade_type;
		}
		public String getOut_trade_no() {
			return out_trade_no;
		}
		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}
		public String getOut_refund_no() {
			return out_refund_no;
		}
		public void setOut_refund_no(String out_refund_no) {
			this.out_refund_no = out_refund_no;
		}
		public String getErr_code() {
			return err_code;
		}
		public void setErr_code(String err_code) {
			this.err_code = err_code;
		}
		public String getErr_code_des() {
			return err_code_des;
		}
		public void setErr_code_des(String err_code_des) {
			this.err_code_des = err_code_des;
		}
		public String getTransaction_id() {
			return transaction_id;
		}
		public void setTransaction_id(String transaction_id) {
			this.transaction_id = transaction_id;
		}
		public String getRefund_id() {
			return refund_id;
		}
		public void setRefund_id(String refund_id) {
			this.refund_id = refund_id;
		}
		public int getRefund_fee() {
			return refund_fee;
		}
		public void setRefund_fee(int refund_fee) {
			this.refund_fee = refund_fee;
		}
		public int getTotal_fee() {
			return total_fee;
		}
		public void setTotal_fee(int total_fee) {
			this.total_fee = total_fee;
		}
		public int getCash_fee() {
			return cash_fee;
		}
		public void setCash_fee(int cash_fee) {
			this.cash_fee = cash_fee;
		}
    
}
