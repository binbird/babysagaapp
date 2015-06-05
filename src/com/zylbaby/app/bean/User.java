package com.zylbaby.app.bean;


/**
 * 登录用户实体类
 * @author Micheal zhu (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2014-3-11
 */
public class User{
	
	// 用户id
	private String uid;
	// 用户名
	private String uname;
	// 昵称
	private String unickname;
	// 真实姓名
	private String urealname;
	// 性别
	private String usex;
	// 邮箱
	private String umail;
	// 移动电话
	private String utelephone;
	// 固定电话
	private String uphone;
	// 证件类型
	private String ucerttype;
	// 证件号
	private String ucertnum;
	// 生日
	private String ubirthday;
	// 地址
	private String uaddr;
	// 密码
	private String upassword;
	// 密码确认
	private String upasswords;
	// 是否记住我
	private boolean isRememberMe;
	// 加入时间
	private String jointime;
	// 验证
	private String validate;
    // 是否首次登录
	private int isfirst;
	
	private String shenfen;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUnickname() {
		return unickname;
	}
	public void setUnickname(String unickname) {
		this.unickname = unickname;
	}
	public String getUrealname() {
		return urealname;
	}
	public void setUrealname(String urealname) {
		this.urealname = urealname;
	}
	public String getUsex() {
		return usex;
	}
	public void setUsex(String usex) {
		this.usex = usex;
	}
	public String getUmail() {
		return umail;
	}
	public void setUmail(String umail) {
		this.umail = umail;
	}
	public String getUtelephone() {
		return utelephone;
	}
	public void setUtelephone(String utelephone) {
		this.utelephone = utelephone;
	}
	public String getUphone() {
		return uphone;
	}
	public void setUphone(String uphone) {
		this.uphone = uphone;
	}
	public String getUcerttype() {
		return ucerttype;
	}
	public void setUcerttype(String ucerttype) {
		this.ucerttype = ucerttype;
	}
	public String getUcertnum() {
		return ucertnum;
	}
	public void setUcertnum(String ucertnum) {
		this.ucertnum = ucertnum;
	}
	public String getUbirthday() {
		return ubirthday;
	}
	public void setUbirthday(String ubirthday) {
		this.ubirthday = ubirthday;
	}
	public String getUaddr() {
		return uaddr;
	}
	public void setUaddr(String uaddr) {
		this.uaddr = uaddr;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getUpasswords() {
		return upasswords;
	}
	public void setUpasswords(String upasswords) {
		this.upasswords = upasswords;
	}
	public boolean isRememberMe() {
		return isRememberMe;
	}
	public void setRememberMe(boolean isRememberMe) {
		this.isRememberMe = isRememberMe;
	}
	public String getJointime() {
		return jointime;
	}
	public void setJointime(String jointime) {
		this.jointime = jointime;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	public int getIsfirst() {
		return isfirst;
	}
	public void setIsfirst(int isfirst) {
		this.isfirst = isfirst;
	}
	public String getShenfen() {
		return shenfen;
	}
	public void setShenfen(String shenfen) {
		this.shenfen = shenfen;
	}
	
}
