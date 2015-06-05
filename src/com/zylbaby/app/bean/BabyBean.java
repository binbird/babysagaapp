package com.zylbaby.app.bean;

import java.io.File;


/**
 * Baby实体类
 * @author Micheal zhu (zhuyuliang0@126.com)
 * @version 1.0
 * @created 2014-3-11
 */
public class BabyBean{
	
	private int id;
	
	// baby姓名
	private String bname;
	// baby昵称
	private String bnickname;
	// 出生日期
	private String bbirth;
	// 性别
	private String bsex;
	// 血型
	private String bblood;
	// 籍贯
	private String bnativeplace1;
	private String bnativeplace2;
	private String bnativeplace3;
	// 备注
	private String bbeizhu;
	// 备注2
	private String bbeizhu2;
	// 验证
	private String validate;
	
	// 头像
	private File imageurl;
	private String imageurlfile;
	
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getBnickname() {
		return bnickname;
	}
	public void setBnickname(String bnickname) {
		this.bnickname = bnickname;
	}
	public String getBbirth() {
		return bbirth;
	}
	public void setBbirth(String bbirth) {
		this.bbirth = bbirth;
	}
	public String getBsex() {
		return bsex;
	}
	public void setBsex(String bsex) {
		this.bsex = bsex;
	}
	public String getBblood() {
		return bblood;
	}
	public void setBblood(String bblood) {
		this.bblood = bblood;
	}
	public String getBnativeplace1() {
		return bnativeplace1;
	}
	public void setBnativeplace1(String bnativeplace1) {
		this.bnativeplace1 = bnativeplace1;
	}
	public String getBnativeplace2() {
		return bnativeplace2;
	}
	public void setBnativeplace2(String bnativeplace2) {
		this.bnativeplace2 = bnativeplace2;
	}
	public String getBnativeplace3() {
		return bnativeplace3;
	}
	public void setBnativeplace3(String bnativeplace3) {
		this.bnativeplace3 = bnativeplace3;
	}
	public String getBbeizhu() {
		return bbeizhu;
	}
	public void setBbeizhu(String bbeizhu) {
		this.bbeizhu = bbeizhu;
	}
	public String getBbeizhu2() {
		return bbeizhu2;
	}
	public void setBbeizhu2(String bbeizhu2) {
		this.bbeizhu2 = bbeizhu2;
	}
	public String getValidate() {
		return validate;
	}
	public void setValidate(String validate) {
		this.validate = validate;
	}
	public File getImageurl() {
		return imageurl;
	}
	public void setImageurl(File imageurl) {
		this.imageurl = imageurl;
	}
	public String getImageurlfile() {
		return imageurlfile;
	}
	public void setImageurlfile(String imageurlfile) {
		this.imageurlfile = imageurlfile;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
