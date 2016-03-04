package com.meidebi.app.support.utils.content;

import java.util.LinkedHashMap;
import java.util.Map;

import com.meidebi.app.base.config.AppConfigs;

public class CategoryUtils {
	private static CategoryUtils instance = new CategoryUtils();
	private Map<String, String> map = new LinkedHashMap<String, String>();

	public static CategoryUtils getInstance() {
		return instance;
	}

	public Map<String, String> get() {
		return map;
	}

	public CategoryUtils() {
//		map.put("0", "有货区域");
		map.put("1", "3C数码");
		map.put("3", "家用电器");
		map.put("2", "服饰鞋包");
		map.put("48", "个护化妆");
		map.put("4", "家居生活");
		map.put("52", "食品保健");
		map.put("6", "母婴玩具");
		map.put("54", "图书APP");
 		map.put("55", "钟表镜饰");
 		map.put("56", "办公汽车");
 		map.put("57", "其它");
//		map.put("79", "口腔护理");
//		map.put("78", "生理卫生");
//		map.put("77", "影音设备");
//		map.put("35", "卫浴用品");
//		map.put("33", "大家电");
//		map.put("34", "生活护理电器");
//		map.put("39", "实用工具");
//		map.put("37", "卧室用具");
//		map.put("38", "厨房用品");
//		map.put("82", "游戏软件");
//		map.put("83", "APP");
//		map.put("86", "孕妇服装");
//		map.put("87", "办公设备");
//		map.put("84", "精品眼镜");
//		map.put("85", "五金建材");
//		map.put("67", "电子书");
//		map.put("66", "报纸杂志");
//		map.put("69", "金银珠宝");
//		map.put("68", "音像制品");
//		map.put("23", "数码配件");
//		map.put("24", "女装");
//		map.put("25", "男装");
//		map.put("26", "内衣");
//		map.put("27", "童装");
//		map.put("28", "鞋袜");
//		map.put("29", "箱包");
//		map.put("3", "家用电器");
//		map.put("2", "服饰鞋包");
//		map.put("1", "3C数码");
//		map.put("30", "配饰");
//		map.put("7", "电脑硬件");
//		map.put("6", "母婴玩具");
//		map.put("31", "厨卫电器");
//		map.put("4", "家居生活");
//		map.put("70", "挂钟手表");
//		map.put("71", "配件工具");
//		map.put("9", "平板电脑");
//		map.put("72", "养护用品");
//		map.put("8", "智能手机");
//		map.put("73", "信用卡");
//		map.put("74", "服务");
//		map.put("59", "保健滋补");
//		map.put("58", "饮品");
//		map.put("57", "其它");
//		map.put("56", "办公汽车");
//		map.put("55", "钟表镜饰");
//		map.put("21", "外设网络");
//		map.put("65", "图书");
//		map.put("60", "零食特产");
//		map.put("61", "新鲜菜市");
//		map.put("49", "彩妆香水");
//		map.put("48", "个护化妆");
//		map.put("94", "孕妇用品");
//		map.put("93", "鲜花礼品");
//		map.put("44", "婴儿用品");
//		map.put("92", "运动户外");
//		map.put("47", "游戏玩具");
//		map.put("91", "其它");
//		map.put("46", "奶粉辅食");
//		map.put("90", "酒店机票");
//		map.put("10", "数码影像");
//		map.put("51", "护肤品");
//		map.put("52", "食品保健");
//		map.put("54", "图书APP");
//		map.put("88", "办公耗材");
//		map.put("89", "宠物用品");
//		map.put("50", "洗发护发");
	}
	
	public String getWBShareTag(String key){
		return "#"+AppConfigs.APP_NAME+map.get(key)+"#";
	}
}
