package com.zylbaby.app.ui;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.zylbaby.app.R;
import com.zylbaby.app.bean.BabyBean;
import com.zylbaby.app.global.AppContext;
import com.zylbaby.app.util.AppException;
import com.zylbaby.app.util.AppManager;
import com.zylbaby.app.util.FileUtils;
import com.zylbaby.app.util.UIHelper;
import com.zylbaby.app.view.BootstrapCircleThumbnail;
import com.zylbaby.app.global.Regional;

/**
 * 添加宝贝布局界面
 * 
 * @author Micheal zhu 朱育梁 （zhuyuliang0@126.com）
 * @version 1.0
 * @created 2014-3-6
 */
public class CopyOfAddbabyActivity extends Activity {
	
	// 进入标示
	private String tag = "1";

	// 籍贯相关
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Spinner county_spinner;
	 //籍贯
	private Integer provinceId, cityId;
	private String strProvince, strCity, strCounty;
	private Regional regional= new Regional();
//	//市，自治区集合
//  	private int[] city = { R.array.beijin_province_item,
//  			R.array.tianjin_province_item, R.array.heibei_province_item,
//  			R.array.shanxi1_province_item, R.array.neimenggu_province_item,
//  			R.array.liaoning_province_item, R.array.jilin_province_item,
//  			R.array.heilongjiang_province_item, R.array.shanghai_province_item,
//  			R.array.jiangsu_province_item, R.array.zhejiang_province_item,
//  			R.array.anhui_province_item, R.array.fujian_province_item,
//  			R.array.jiangxi_province_item, R.array.shandong_province_item,
//  			R.array.henan_province_item, R.array.hubei_province_item,
//  			R.array.hunan_province_item, R.array.guangdong_province_item,
//  			R.array.guangxi_province_item, R.array.hainan_province_item,
//  			R.array.chongqing_province_item, R.array.sichuan_province_item,
//  			R.array.guizhou_province_item, R.array.yunnan_province_item,
//  			R.array.xizang_province_item, R.array.shanxi2_province_item,
//  			R.array.gansu_province_item, R.array.qinghai_province_item,
//  			R.array.linxia_province_item, R.array.xinjiang_province_item,
//  			R.array.hongkong_province_item, R.array.aomen_province_item,
//  			R.array.taiwan_province_item };
//  	private int[] countyOfBeiJing = { R.array.beijin_city_item };
//  	private int[] countyOfTianJing = { R.array.tianjin_city_item };
//  	private int[] countyOfHeBei = { R.array.shijiazhuang_city_item,
//  			R.array.tangshan_city_item, R.array.qinghuangdao_city_item,
//  			R.array.handan_city_item, R.array.xingtai_city_item,
//  			R.array.baoding_city_item, R.array.zhangjiakou_city_item,
//  			R.array.chengde_city_item, R.array.cangzhou_city_item,
//  			R.array.langfang_city_item, R.array.hengshui_city_item };
//  	private int[] countyOfShanXi1 = { R.array.taiyuan_city_item,
//  			R.array.datong_city_item, R.array.yangquan_city_item,
//  			R.array.changzhi_city_item, R.array.jincheng_city_item,
//  			R.array.shuozhou_city_item, R.array.jinzhong_city_item,
//  			R.array.yuncheng_city_item, R.array.xinzhou_city_item,
//  			R.array.linfen_city_item, R.array.lvliang_city_item };
//  	private int[] countyOfNeiMengGu = { R.array.huhehaote_city_item,
//  			R.array.baotou_city_item, R.array.wuhai_city_item,
//  			R.array.chifeng_city_item, R.array.tongliao_city_item,
//  			R.array.eerduosi_city_item, R.array.hulunbeier_city_item,
//  			R.array.bayannaoer_city_item, R.array.wulanchabu_city_item,
//  			R.array.xinganmeng_city_item, R.array.xilinguolemeng_city_item,
//  			R.array.alashanmeng_city_item };
//  	private int[] countyOfLiaoNing = { R.array.shenyang_city_item,
//  			R.array.dalian_city_item, R.array.anshan_city_item,
//  			R.array.wushun_city_item, R.array.benxi_city_item,
//  			R.array.dandong_city_item, R.array.liaoning_jinzhou_city_item,
//  			R.array.yingkou_city_item, R.array.fuxin_city_item,
//  			R.array.liaoyang_city_item, R.array.panjin_city_item,
//  			R.array.tieling_city_item, R.array.zhaoyang_city_item,
//  			R.array.huludao_city_item };
//  	private int[] countyOfJiLin = { R.array.changchun_city_item,
//  			R.array.jilin_city_item, R.array.siping_city_item,
//  			R.array.liaoyuan_city_item, R.array.tonghua_city_item,
//  			R.array.baishan_city_item, R.array.songyuan_city_item,
//  			R.array.baicheng_city_item, R.array.yanbian_city_item };
//  	private int[] countyOfHeiLongJiang = { R.array.haerbing_city_item,
//  			R.array.qiqihaer_city_item, R.array.jixi_city_item,
//  			R.array.hegang_city_item, R.array.shuangyashan_city_item,
//  			R.array.daqing_city_item, R.array.heilongjiang_yichun_city_item,
//  			R.array.jiamusi_city_item, R.array.qitaihe_city_item,
//  			R.array.mudanjiang_city_item, R.array.heihe_city_item,
//  			R.array.suihua_city_item, R.array.daxinganling_city_item };
//  	private int[] countyOfShangHai = { R.array.shanghai_city_item };
//
//  	private int[] countyOfJiangSu = { R.array.nanjing_city_item,
//  			R.array.wuxi_city_item, R.array.xuzhou_city_item,
//  			R.array.changzhou_city_item, R.array.nanjing_suzhou_city_item,
//  			R.array.nantong_city_item, R.array.lianyungang_city_item,
//  			R.array.huaian_city_item, R.array.yancheng_city_item,
//  			R.array.yangzhou_city_item, R.array.zhenjiang_city_item,
//  			R.array.jiangsu_taizhou_city_item, R.array.suqian_city_item };
//  	private int[] countyOfZheJiang = { R.array.hangzhou_city_item,
//  			R.array.ningbo_city_item, R.array.wenzhou_city_item,
//  			R.array.jiaxing_city_item, R.array.huzhou_city_item,
//  			R.array.shaoxing_city_item, R.array.jinhua_city_item,
//  			R.array.quzhou_city_item, R.array.zhoushan_city_item,
//  			R.array.zejiang_huzhou_city_item, R.array.lishui_city_item };
//  	private int[] countyOfAnHui = { R.array.hefei_city_item,
//  			R.array.wuhu_city_item, R.array.bengbu_city_item,
//  			R.array.huainan_city_item, R.array.maanshan_city_item,
//  			R.array.huaibei_city_item, R.array.tongling_city_item,
//  			R.array.anqing_city_item, R.array.huangshan_city_item,
//  			R.array.chuzhou_city_item, R.array.fuyang_city_item,
//  			R.array.anhui_suzhou_city_item, R.array.chaohu_city_item,
//  			R.array.luan_city_item, R.array.haozhou_city_item,
//  			R.array.chizhou_city_item, R.array.xuancheng_city_item };
//  	private int[] countyOfFuJian = { R.array.huzhou_city_item,
//  			R.array.xiamen_city_item, R.array.putian_city_item,
//  			R.array.sanming_city_item, R.array.quanzhou_city_item,
//  			R.array.zhangzhou_city_item, R.array.nanp_city_item,
//  			R.array.longyan_city_item, R.array.ningde_city_item };
//  	private int[] countyOfJiangXi = { R.array.nanchang_city_item,
//  			R.array.jingdezhen_city_item, R.array.pingxiang_city_item,
//  			R.array.jiujiang_city_item, R.array.xinyu_city_item,
//  			R.array.yingtan_city_item, R.array.ganzhou_city_item,
//  			R.array.jian_city_item, R.array.jiangxi_yichun_city_item,
//  			R.array.jiangxi_wuzhou_city_item, R.array.shangrao_city_item };
//  	private int[] countyOfShanDong = { R.array.jinan_city_item,
//  			R.array.qingdao_city_item, R.array.zaobo_city_item,
//  			R.array.zaozhuang_city_item, R.array.dongying_city_item,
//  			R.array.yantai_city_item, R.array.weifang_city_item,
//  			R.array.jining_city_item, R.array.taian_city_item,
//  			R.array.weihai_city_item, R.array.rizhao_city_item,
//  			R.array.laiwu_city_item, R.array.linxi_city_item,
//  			R.array.dezhou_city_item, R.array.liaocheng_city_item,
//  			R.array.shandong_bingzhou_city_item, R.array.heze_city_item };
//  	private int[] countyOfHeNan = { R.array.zhenshou_city_item,
//  			R.array.kaifang_city_item, R.array.luoyang_city_item,
//  			R.array.kaipingshan_city_item, R.array.anyang_city_item,
//  			R.array.hebi_city_item, R.array.xinxiang_city_item,
//  			R.array.jiaozuo_city_item, R.array.buyang_city_item,
//  			R.array.xuchang_city_item, R.array.leihe_city_item,
//  			R.array.sanmenxia_city_item, R.array.nanyang_city_item,
//  			R.array.shangqiu_city_item, R.array.xinyang_city_item,
//  			R.array.zhoukou_city_item, R.array.zhumadian_city_item };
//  	private int[] countyOfHuBei = { R.array.wuhan_city_item,
//  			R.array.huangshi_city_item, R.array.shiyan_city_item,
//  			R.array.yichang_city_item, R.array.xiangpan_city_item,
//  			R.array.erzhou_city_item, R.array.jinmen_city_item,
//  			R.array.xiaogan_city_item, R.array.hubei_jinzhou_city_item,
//  			R.array.huanggang_city_item, R.array.xianning_city_item,
//  			R.array.suizhou_city_item, R.array.enshi_city_item,
//  			R.array.shenglongjia_city_item };
//
//  	private int[] countyOfHuNan = { R.array.changsha_city_item,
//  			R.array.zhuzhou_city_item, R.array.xiangtan_city_item,
//  			R.array.hengyang_city_item, R.array.shaoyang_city_item,
//  			R.array.yueyang_city_item, R.array.changde_city_item,
//  			R.array.zhangjiajie_city_item, R.array.yiyang_city_item,
//  			R.array.hunan_bingzhou_city_item, R.array.yongzhou_city_item,
//  			R.array.huaihua_city_item, R.array.loudi_city_item,
//  			R.array.xiangxi_city_item };
//  	private int[] countyOfGuangDong = { R.array.guangzhou_city_item,
//  			R.array.shaoguan_city_item, R.array.shenzhen_city_item,
//  			R.array.zhuhai_city_item, R.array.shantou_city_item,
//  			R.array.foshan_city_item, R.array.jiangmen_city_item,
//  			R.array.zhangjiang_city_item, R.array.maoming_city_item,
//  			R.array.zhaoqing_city_item, R.array.huizhou_city_item,
//  			R.array.meizhou_city_item, R.array.shanwei_city_item,
//  			R.array.heyuan_city_item, R.array.yangjiang_city_item,
//  			R.array.qingyuan_city_item, R.array.dongguan_city_item,
//  			R.array.zhongshan_city_item, R.array.chaozhou_city_item,
//  			R.array.jiyang_city_item, R.array.yunfu_city_item };
//  	private int[] countyOfGuangXi = { R.array.nanning_city_item,
//  			R.array.liuzhou_city_item, R.array.guilin_city_item,
//  			R.array.guangxi_wuzhou_city_item, R.array.beihai_city_item,
//  			R.array.fangchenggang_city_item, R.array.qinzhou_city_item,
//  			R.array.guigang_city_item, R.array.yuelin_city_item,
//  			R.array.baise_city_item, R.array.hezhou_city_item,
//  			R.array.hechi_city_item, R.array.laibing_city_item,
//  			R.array.chuangzuo_city_item };
//  	private int[] countyOfHaiNan = { R.array.haikou_city_item,
//  			R.array.sanya_city_item };
//  	private int[] countyOfChongQing = { R.array.chongqing_city_item };
//  	private int[] countyOfSiChuan = { R.array.chengdu_city_item,
//  			R.array.zigong_city_item, R.array.panzhihua_city_item,
//  			R.array.luzhou_city_item, R.array.deyang_city_item,
//  			R.array.mianyang_city_item, R.array.guangyuan_city_item,
//  			R.array.suining_city_item, R.array.neijiang_city_item,
//  			R.array.leshan_city_item, R.array.nanchong_city_item,
//  			R.array.meishan_city_item, R.array.yibing_city_item,
//  			R.array.guangan_city_item, R.array.dazhou_city_item,
//  			R.array.yaan_city_item, R.array.bazhong_city_item,
//  			R.array.ziyang_city_item, R.array.abei_city_item,
//  			R.array.ganmu_city_item, R.array.liangshan_city_item };
//  	private int[] countyOfGuiZhou = { R.array.guiyang_city_item,
//  			R.array.lupanshui_city_item, R.array.zhunyi_city_item,
//  			R.array.anshun_city_item, R.array.tongren_city_item,
//  			R.array.qingxinan_city_item, R.array.biji_city_item,
//  			R.array.qingdongnan_city_item, R.array.qingnan_city_item };
//  	private int[] countyOfYunNan = { R.array.kunming_city_item,
//  			R.array.qujing_city_item, R.array.yuexi_city_item,
//  			R.array.baoshan_city_item, R.array.zhaotong_city_item,
//  			R.array.lijiang_city_item, R.array.simao_city_item,
//  			R.array.lingcang_city_item, R.array.chuxiong_city_item,
//  			R.array.honghe_city_item, R.array.wenshan_city_item,
//  			R.array.xishuangbanna_city_item, R.array.dali_city_item,
//  			R.array.dehuang_city_item, R.array.nujiang_city_item,
//  			R.array.diqing_city_item };
//  	private int[] countyOfXiZang = { R.array.lasa_city_item,
//  			R.array.changdu_city_item, R.array.shannan_city_item,
//  			R.array.rgeze_city_item, R.array.naqu_city_item,
//  			R.array.ali_city_item, R.array.linzhi_city_item };
//
//  	private int[] countyOfShanXi2 = { R.array.xian_city_item,
//  			R.array.tongchuan_city_item, R.array.baoji_city_item,
//  			R.array.xianyang_city_item, R.array.weinan_city_item,
//  			R.array.yanan_city_item, R.array.hanzhong_city_item,
//  			R.array.yulin_city_item, R.array.ankang_city_item,
//  			R.array.shangluo_city_item };
//  	private int[] countyOfGanSu = { R.array.lanzhou_city_item,
//  			R.array.jiayuguan_city_item, R.array.jinchang_city_item,
//  			R.array.baiyin_city_item, R.array.tianshui_city_item,
//  			R.array.wuwei_city_item, R.array.zhangyue_city_item,
//  			R.array.pingliang_city_item, R.array.jiuquan_city_item,
//  			R.array.qingyang_city_item, R.array.dingxi_city_item,
//  			R.array.longnan_city_item, R.array.linxia_city_item,
//  			R.array.gannan_city_item };
//  	private int[] countyOfQingHai = { R.array.xining_city_item,
//  			R.array.haidong_city_item, R.array.haibai_city_item,
//  			R.array.huangnan_city_item, R.array.hainan_city_item,
//  			R.array.guluo_city_item, R.array.yushu_city_item,
//  			R.array.haixi_city_item };
//  	private int[] countyOfNingXia = { R.array.yinchuan_city_item,
//  			R.array.shizuishan_city_item, R.array.wuzhong_city_item,
//  			R.array.guyuan_city_item, R.array.zhongwei_city_item };
//  	private int[] countyOfXinJiang = { R.array.wulumuqi_city_item,
//  			R.array.kelamayi_city_item, R.array.tulyfan_city_item,
//  			R.array.hami_city_item, R.array.changji_city_item,
//  			R.array.boertala_city_item, R.array.bayinguolen_city_item,
//  			R.array.akesu_city_item, R.array.kemuleisu_city_item,
//  			R.array.geshen_city_item, R.array.hetian_city_item,
//  			R.array.yili_city_item, R.array.tacheng_city_item,
//  			R.array.aleitai_city_item, R.array.shihezi_city_item,
//  			R.array.alaer_city_item, R.array.tumushihe_city_item,
//  			R.array.wujiaqu_city_item };
//  	private int[] countyOfHongKong = {};
//  	private int[] countyOfAoMen = {};
//  	private int[] countyOfTaiWan = {};
  	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	private ArrayAdapter<CharSequence> county_adapter;
    
	
	//出生相关
	// 显示日期控件按键
	private ImageButton add_task_date_btn;
	// 日期控件
	private DatePickerDialog datePickerDialog;
	// 时间控件
	private TimePickerDialog timePickerDialog;
	// 获取时间
	private Calendar calendar;
	
	//添加头像
	private BootstrapCircleThumbnail add_baby_image;
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
    Bitmap bitmaptemp = null;
    
    //返回按键
    private ImageView add_baby_back_btn;
    //跳过添加
    private Button titleRightButton;
    
    //宝贝名称
    private EditText add_baby_name_edit;
    //宝贝小名
    private EditText add_baby_name1_edit;
    //出生日期
    private EditText task_time_edit;
    //性别
    private RadioGroup radiogroup1;
    private int sex = 1;
    //血型
    private Spinner blood_types_list;
    //宝贝描述
    private EditText add_baby_beizhu_edit;
    
    //保存宝贝
    private Button add_baby_save_btn;
    
    // 定义一个ProgressDialog
 	private ProgressDialog pd;
 	// 设置标记
 	final int SIGN = 0x11;
 	
 	private BabyBean babybean;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_baby_activity);
		
		// 将此视图添加到Activity管理当中
		AppManager.getInstance().addActivity(this);
		// 初始化
		init();
		// 加载数据
		initData();
		
		//加载联动
		loadSpinner();
	}

	// 初始化
	private void init() {
		
		tag = getIntent().getExtras().getString("tag");
		
		province_spinner = (Spinner) findViewById(R.id.spinner1);
		city_spinner = (Spinner) findViewById(R.id.spinner2);
		county_spinner = (Spinner) findViewById(R.id.spinner3);
		province_spinner.setPrompt("省");
		city_spinner.setPrompt("城市");
		county_spinner.setPrompt("地区");
		//出生日期
		add_task_date_btn = (ImageButton) findViewById(R.id.add_task_date_btn);
		task_time_edit = (EditText) findViewById(R.id.task_time_edit);
		task_time_edit.setFocusable(false);
		
		add_task_date_btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showDate(0);
			}
		});
		
		//添加头像
		add_baby_image = (BootstrapCircleThumbnail) findViewById(R.id.add_baby_image);
		add_baby_image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog();
			}
		});
		
		//返回按键
		add_baby_back_btn = (ImageView) findViewById(R.id.add_baby_back_btn);
		add_baby_back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(tag.equals("0")){
					AppContext appContext = (AppContext) getApplication();
					appContext.Logout();
					UIHelper.showLogin(CopyOfAddbabyActivity.this);
					finish();
				}else{
					finish();
				}
			}
		});
		
		add_baby_name_edit = (EditText) findViewById(R.id.add_baby_name_edit);
		add_baby_name1_edit = (EditText) findViewById(R.id.add_baby_name1_edit);
		radiogroup1 = (RadioGroup) findViewById(R.id.radiogroup1);
		blood_types_list = (Spinner) findViewById(R.id.blood_types_list);
		add_baby_beizhu_edit = (EditText) findViewById(R.id.add_baby_beizhu_edit);
		add_baby_save_btn = (Button) findViewById(R.id.add_baby_save_btn);
		titleRightButton = (Button) findViewById(R.id.titleRightButton);
		
		if(!tag.equals("0")){
			titleRightButton.setVisibility(8);
		}
	}

	// 加载数据
	private void initData() {
		radiogroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == R.id.man){
					sex = 0;
				}
				if(checkedId == R.id.woman){
					sex = 1;
				}
			}
		});
		
		titleRightButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UIHelper.showhomepage(CopyOfAddbabyActivity.this);
				finish();
			}
		});
		
		add_baby_save_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (add_baby_name_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(CopyOfAddbabyActivity.this, "baby名字不能为空！");
				} else if (add_baby_name1_edit.getText().toString().equals("")) {
					UIHelper.ToastMessage(CopyOfAddbabyActivity.this, "baby昵称不能为空！");
				} else {
					showDialog(SIGN);
					pd.setOnKeyListener(new OnKeyListener() {
						@Override
						public boolean onKey(DialogInterface dialog, int keyCode,
								KeyEvent event) {
							boolean onkey = false;
							int keyCode1 = event.getKeyCode();
							if (keyCode1 == KeyEvent.KEYCODE_BACK
									&& event.getAction() != KeyEvent.ACTION_UP) {
								if (event.getRepeatCount() == 0) {
									pd.dismiss();
									pd.cancel();
									onkey = true;
								}
							}
							return onkey;
						}
					});
					BabyBean baby = new BabyBean();
					baby.setBname(add_baby_name_edit.getText().toString());
					baby.setBnickname(add_baby_name1_edit.getText().toString());
					baby.setBbirth(task_time_edit.getText().toString());
					if(sex == 1){
						baby.setBsex("男");
					}else{
						baby.setBsex("女");
					}
					baby.setBblood(blood_types_list.getSelectedItem().toString());
					baby.setBnativeplace1(strProvince);
					baby.setBnativeplace2(strCity);
					baby.setBnativeplace3(strCounty);
					baby.setBbeizhu(add_baby_beizhu_edit.getText().toString());
					if(bitmaptemp != null){
						FileUtils fileutils = new FileUtils(CopyOfAddbabyActivity.this);
						File file = null;
						try {
							file = fileutils.getSaveFile("imagtemp",bitmaptemp);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						baby.setImageurl(file);
					}else{
						baby.setImageurl(null);
					}
					updateData(baby);
				}
			}
		});
		
	}
	
	// 登录验证
	private void updateData(final BabyBean baby) {
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					BabyBean user = (BabyBean) msg.obj;
					pd.dismiss();
					if (user != null) {
						UIHelper.ToastMessage(CopyOfAddbabyActivity.this,
								"添加成功");
						UIHelper.showhomepage(CopyOfAddbabyActivity.this);
						finish();
					}
				} else if (msg.what == 0) {
					pd.dismiss();
					UIHelper.ToastMessage(CopyOfAddbabyActivity.this,
						   "添加失败" + msg.obj);
				} else if (msg.what == -1) {
					pd.dismiss();
					((AppException) msg.obj).makeToast(CopyOfAddbabyActivity.this);
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					AppContext ac = (AppContext) getApplication();
					Map<String, Object> paramMap = new HashMap<String, Object>();
					if(baby.getBsex().equals("男")){
						paramMap.put("Sex", "0");
					}else{
						paramMap.put("Sex", "1");
					}
					paramMap.put("BabyName", baby.getBname());
					paramMap.put("Nickname", baby.getBnickname());
					paramMap.put("Birthday", baby.getBbirth());
					paramMap.put("Country", baby.getBnativeplace1());
					paramMap.put("Province", baby.getBnativeplace2());
					paramMap.put("City", baby.getBnativeplace3());
					paramMap.put("BloodType", baby.getBblood());
					paramMap.put("Introduction", baby.getBbeizhu());
					Map<String, File> paramMap1 = new HashMap<String, File>();
					if(baby.getImageurl().length()==0){
						paramMap1 = null;
					}else{
						paramMap1.put("HeadImg", baby.getImageurl());
					}
					BabyBean baby = ac.addBabyInfo(paramMap, paramMap1);
					String res = baby.getValidate();
					if (res.equals("200")) {
						msg.what = 1;// 成功
						msg.obj = baby;
					} else {
						ac.cleanLoginInfo();// 清除登录信息
						msg.what = 0;// 失败
						msg.obj = baby.getBbeizhu2();
					}
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	
	private void loadSpinner() {
	    //绑定省份的数据
		province_spinner.setPrompt("请选择省份");
		province_adapter = ArrayAdapter.createFromResource(this,
				R.array.province_item, android.R.layout.simple_spinner_item);
		province_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		province_spinner.setAdapter(province_adapter);
		// select(province_spinner, province_adapter, R.array.province_item);
	    //添加监听，一开始的时候城市，县区的内容是不显示的而是根据省的内容进行联动
		province_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						provinceId = province_spinner.getSelectedItemPosition();
						strProvince = province_spinner.getSelectedItem()
								.toString();//得到选择的内容，也就是省的名字
						
						if (true) {
							city_spinner.setPrompt("请选择城市");//设置标题
							select(city_spinner, city_adapter, regional.city[provinceId]);//城市一级的数据绑定
							
							/*通过这个city[provinceId]指明了该省市的City集合
							 * R。array.beijing*/
							city_spinner
									.setOnItemSelectedListener(new OnItemSelectedListener() {

										@Override
										public void onItemSelected(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											cityId = city_spinner
													.getSelectedItemPosition();//得到city的id
											strCity = city_spinner
													.getSelectedItem()
													.toString();//得到city的内容
											Log.v("test", "city: "
													+ city_spinner
															.getSelectedItem()
															.toString()//输出测试一下
													+ cityId.toString());
											if (true) {
												//这里开始设置县区一级的内容
												county_spinner
														.setPrompt("请选择县区");
												switch (provinceId) {
												case 0:
													select(county_spinner,
															county_adapter,
															regional.countyOfBeiJing[cityId]);
													break;
												case 1:
													select(county_spinner,
															county_adapter,
															regional.countyOfTianJing[cityId]);
													break;
												case 2:
													select(county_spinner,
															county_adapter,
															regional.countyOfHeBei[cityId]);
													break;
												case 3:
													select(county_spinner,
															county_adapter,
															regional.countyOfShanXi1[cityId]);
													break;
												case 4:
													select(county_spinner,
															county_adapter,
															regional.countyOfNeiMengGu[cityId]);
													break;
												case 5:
													select(county_spinner,
															county_adapter,
															regional.countyOfLiaoNing[cityId]);
													break;
												case 6:
													select(county_spinner,
															county_adapter,
															regional.countyOfJiLin[cityId]);
													break;
												case 7:
													select(county_spinner,
															county_adapter,
															regional.countyOfHeiLongJiang[cityId]);
													break;
												case 8:
													select(county_spinner,
															county_adapter,
															regional.countyOfShangHai[cityId]);
													break;
												case 9:
													select(county_spinner,
															county_adapter,
															regional.countyOfJiangSu[cityId]);
													break;
												case 10:
													select(county_spinner,
															county_adapter,
															regional.countyOfZheJiang[cityId]);
													break;
												case 11:
													select(county_spinner,
															county_adapter,
															regional.countyOfAnHui[cityId]);
													break;
												case 12:
													select(county_spinner,
															county_adapter,
															regional.countyOfFuJian[cityId]);
													break;
												case 13:
													select(county_spinner,
															county_adapter,
															regional.countyOfJiangXi[cityId]);
													break;
												case 14:
													select(county_spinner,
															county_adapter,
															regional.countyOfShanDong[cityId]);
													break;
												case 15:
													select(county_spinner,
															county_adapter,
															regional.countyOfHeNan[cityId]);
													break;
												case 16:
													select(county_spinner,
															county_adapter,
															regional.countyOfHuBei[cityId]);
													break;
												case 17:
													select(county_spinner,
															county_adapter,
															regional.countyOfHuNan[cityId]);
													break;
												case 18:
													select(county_spinner,
															county_adapter,
															regional.countyOfGuangDong[cityId]);
													break;
												case 19:
													select(county_spinner,
															county_adapter,
															regional.countyOfGuangXi[cityId]);
													break;
												case 20:
													select(county_spinner,
															county_adapter,
															regional.countyOfHaiNan[cityId]);
													break;
												case 21:
													select(county_spinner,
															county_adapter,
															regional.countyOfChongQing[cityId]);
													break;
												case 22:
													select(county_spinner,
															county_adapter,
															regional.countyOfSiChuan[cityId]);
													break;
												case 23:
													select(county_spinner,
															county_adapter,
															regional.countyOfGuiZhou[cityId]);
													break;
												case 24:
													select(county_spinner,
															county_adapter,
															regional.countyOfYunNan[cityId]);
													break;
												case 25:
													select(county_spinner,
															county_adapter,
															regional.countyOfXiZang[cityId]);
													break;
												case 26:
													select(county_spinner,
															county_adapter,
															regional.countyOfShanXi2[cityId]);
													break;
												case 27:
													select(county_spinner,
															county_adapter,
															regional.countyOfGanSu[cityId]);
													break;
												case 28:
													select(county_spinner,
															county_adapter,
															regional.countyOfQingHai[cityId]);
													break;
												case 29:
													select(county_spinner,
															county_adapter,
															regional.countyOfNingXia[cityId]);
													break;
												case 30:
													select(county_spinner,
															county_adapter,
															regional.countyOfXinJiang[cityId]);
													break;
												case 31:
													select(county_spinner,
															county_adapter,
															regional.countyOfHongKong[cityId]);
													break;
												case 32:
													select(county_spinner,
															county_adapter,
															regional.countyOfAoMen[cityId]);
													break;
												case 33:
													select(county_spinner,
															county_adapter,
															regional.countyOfTaiWan[cityId]);
													break;

												default:
													break;
												}

												county_spinner
														.setOnItemSelectedListener(new OnItemSelectedListener() {

															@Override
															public void onItemSelected(
																	AdapterView<?> arg0,
																	View arg1,
																	int arg2,
																	long arg3) {
																strCounty = county_spinner
																		.getSelectedItem()
																		.toString();
//																display.setText(strProvince
//																		+ "-"
//																		+ strCity
//																		+ "-"
//																		+ strCounty);
																
															}

															@Override
															public void onNothingSelected(
																	AdapterView<?> arg0) {

															}

														});
											}
										}

										@Override
										public void onNothingSelected(
												AdapterView<?> arg0) {
											// TODO Auto-generated method stub

										}

									});
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

	}
    
	
	
	/*通过方法动态的添加适配器*/
	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter,
			int arry) {
		//注意这里的arry不仅仅但是一个整形，他代表了一个数组！
		adapter = ArrayAdapter.createFromResource(this, arry,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		// spin.setSelection(0,true);
	}

	// 加载日期
	private void showDate(final int sign) {

		/**
		 * 内部匿名类，实现DatePickerDialog.OnDateSetListener接口，重写onDateSet()方法
		 * 当弹出DatePickerDialog并设置完Date以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onDateSet()方法。
		 */
		DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				showTime(sign, year, monthOfYear, dayOfMonth);
			}
		};
		calendar = Calendar.getInstance();
		datePickerDialog = new DatePickerDialog(this, onDateSetListener,
				calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		datePickerDialog.show();

	}

	// 加载日期
	private void showTime(final int sign, final int year,
			final int monthOfYear, final int dayOfMonth) {

		/**
		 * 内部匿名类，实现TimePickerDialog.OnTimeSetListener接口，重写onTimeSet()方法
		 * 当弹出TimePickerDialog并设置完Time以后，左下方有个“Set”按钮，表示确定设置。当这个
		 * 按钮被点击的时候，就执行这里的onTimeSet()方法。
		 */
		TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
				Log.d("test", "" + year + "年" + (monthOfYear + 1) + "月"
						+ dayOfMonth + "日" + arg1 + ":" + arg2);
				if (sign == 0) {
					task_time_edit.setText(year + "年" + (monthOfYear + 1) + "月"
							+ dayOfMonth + "日" + " " + arg1 + ":" + arg2);
				}
			}

		};
		calendar.setTimeInMillis(System.currentTimeMillis());
		timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
				calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), true);
		timePickerDialog.show();
	}

	// 提示对话框方法
	private void showDialog() {
		new AlertDialog.Builder(this)
				.setTitle("头像设置")
				.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// 调用系统的拍照功能
						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						// 指定调用相机拍照后照片的储存路径
						intent.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(tempFile));
						startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
					}
				})
				.setNegativeButton("相册", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");
						startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:
			if(data.getExtras() != null)
				startPhotoZoom(Uri.fromFile(tempFile), 150);		
			break;

		case PHOTO_REQUEST_GALLERY:
			if (data != null)
				startPhotoZoom(data.getData(), 150);
			break;

		case PHOTO_REQUEST_CUT:
			if (data.getExtras() != null)
				setPicToView(data);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if(photo != null){
				bitmaptemp = photo;
				Drawable drawable = new BitmapDrawable(photo);
				add_baby_image.setImage(drawable);
			}
		}
	}

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	// 加载界面
	// 调用onPrepareDialog()方法之后
	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		pd.setProgressStyle(ProgressDialog.THEME_HOLO_DARK);
		pd.setCancelable(false);
		return pd;
	}

	// 调用showDialog()之后和调用onCreateDialog之前
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args) {
		switch (id) {
		case SIGN:
			break;
		}
	}

}
