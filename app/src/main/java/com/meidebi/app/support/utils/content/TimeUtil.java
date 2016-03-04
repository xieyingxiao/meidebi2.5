package com.meidebi.app.support.utils.content;

import com.meidebi.app.R;
import com.meidebi.app.XApplication;
import com.meidebi.app.base.config.AppConfigs;

import java.util.Calendar;

/**
 * User: qii Date: 12-8-28
 */
public class TimeUtil {

	private static int MILL_MIN = 1000 * 60;
	private static int MILL_HOUR = MILL_MIN * 60;
	private static int MILL_DAY = MILL_HOUR * 24;

	private static String SECOND = XApplication.getInstance().getString(
			R.string.sec);
	private static String MIN = XApplication.getInstance().getString(
			R.string.min);
	private static String HOUR = XApplication.getInstance().getString(
			R.string.hour);
	private static String DAY = XApplication.getInstance().getString(
			R.string.day);
	private static String MONTH = XApplication.getInstance().getString(
			R.string.month);
	private static String YEAR = XApplication.getInstance().getString(
			R.string.year);

	private static String YESTER_DAY = XApplication.getInstance().getString(
			R.string.yesterday);
	private static String THE_DAY_BEFORE_YESTER_DAY = XApplication
			.getInstance().getString(R.string.the_day_before_yesterday);
	private static String TODAY = XApplication.getInstance().getString(
			R.string.today);

	private static String DATE_FORMAT = XApplication.getInstance().getString(
			R.string.date_format);
	private static String YEAR_FORMAT = XApplication.getInstance().getString(
			R.string.year_format);

	private static Calendar msgCalendar = null;
	private static java.text.SimpleDateFormat dayFormat = null;
	private static java.text.SimpleDateFormat dateFormat = null;
	private static java.text.SimpleDateFormat yearFormat = null;

	private TimeUtil() {

	}

	// public static String getListTime(long msg) {
	// long msg = 0L;
	//
	// if (bean.getMills() != 0) {
	// msg = bean.getMills();
	// } else {
	// TimeUtil.dealMills(bean);
	// msg = bean.getMills();
	// }
	// return getListTime(msg);
	// }

	public static String getListTime(int time) {

		long now = System.currentTimeMillis();
		long msg = IntToLong(time);

		Calendar nowCalendar = Calendar.getInstance();

		if (msgCalendar == null)
			msgCalendar = Calendar.getInstance();

		msgCalendar.setTimeInMillis(msg);

		long calcMills = now - msg;
		long calSeconds = calcMills / 1000;

		if (calSeconds < 60) {
			return new StringBuilder().append(calSeconds).append(SECOND)
					.toString();
		}

		long calMins = calSeconds / 60;

		if (calMins < 60) {

			return new StringBuilder().append(calMins).append(MIN).toString();
		}

		long calHours = calMins / 60;

		if (calHours < 24 && isSameDay(nowCalendar, msgCalendar)) {
			if (dayFormat == null)
				dayFormat = new java.text.SimpleDateFormat("HH:mm");

			String result = dayFormat.format(msgCalendar.getTime());
			return new StringBuilder().append(calHours).append(HOUR).toString();
		}

		long calDay = calHours / 24;

		if (calDay < 31) {
			if (isYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder(YESTER_DAY).append(" ").append(result)
						.toString();

			} else if (isTheDayBeforeYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder(THE_DAY_BEFORE_YESTER_DAY).append(" ")
						.append(result).toString();

			} else if(calDay<7){
				return new StringBuilder().append(calDay).append(DAY).toString();
			}else {
				if (dateFormat == null)
					dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);

				String result = dateFormat.format(msgCalendar.getTime());
				return new StringBuilder(result).toString();
			}
		}

		long calMonth = calDay / 31;

        if (calMonth < 12 && isSameYear(nowCalendar, msgCalendar)) {
            if (dateFormat == null)
                dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);

            String result = dateFormat.format(msgCalendar.getTime());
            return new StringBuilder().append(result).toString();

        }
        if (yearFormat == null)
            yearFormat = new java.text.SimpleDateFormat(YEAR_FORMAT);
        String result = yearFormat.format(msgCalendar.getTime());
        return new StringBuilder().append(result).toString();


	}
	
	public static String getListTime(long msg) {

		long now = System.currentTimeMillis();

		Calendar nowCalendar = Calendar.getInstance();

		if (msgCalendar == null)
			msgCalendar = Calendar.getInstance();

		msgCalendar.setTimeInMillis(msg);

		long calcMills = now - msg;
		long calSeconds = calcMills / 1000;

		if (calSeconds < 60) {
			return new StringBuilder().append(calSeconds).append(SECOND)
					.toString();
		}

		long calMins = calSeconds / 60;

		if (calMins < 60) {

			return new StringBuilder().append(calMins).append(MIN).toString();
		}

		long calHours = calMins / 60;

		if (calHours < 24 && isSameDay(nowCalendar, msgCalendar)) {
			if (dayFormat == null)
				dayFormat = new java.text.SimpleDateFormat("HH:mm");

			String result = dayFormat.format(msgCalendar.getTime());
			return new StringBuilder().append(calHours).append(HOUR).toString();
		}

		long calDay = calHours / 24;

		if (calDay < 31) {
			if (isYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder(YESTER_DAY).append(" ").append(result)
						.toString();

			} else if (isTheDayBeforeYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder(THE_DAY_BEFORE_YESTER_DAY).append(" ")
						.append(result).toString();

			} else if(calDay<7){
				return new StringBuilder().append(calDay).append(DAY).toString();
			}else {
				if (dateFormat == null)
					dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);

				String result = dateFormat.format(msgCalendar.getTime());
				return new StringBuilder(result).toString();
			}
		}

		long calMonth = calDay / 31;

        if (calMonth < 12 && isSameYear(nowCalendar, msgCalendar)) {
            if (dateFormat == null)
                dateFormat = new java.text.SimpleDateFormat(DATE_FORMAT);

            String result = dateFormat.format(msgCalendar.getTime());
            return new StringBuilder().append(result).toString();

        }
        if (yearFormat == null)
            yearFormat = new java.text.SimpleDateFormat(YEAR_FORMAT);
        String result = yearFormat.format(msgCalendar.getTime());
        return new StringBuilder().append(result).toString();


	}
	
	
	
	

	private static boolean isSameHalfDay(Calendar now, Calendar msg) {
		int nowHour = now.get(Calendar.HOUR_OF_DAY);
		int msgHOur = msg.get(Calendar.HOUR_OF_DAY);

		if (nowHour <= 12 & msgHOur <= 12) {
			return true;
		} else if (nowHour >= 12 & msgHOur >= 12) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isSameDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return nowDay == msgDay;
	}

	private static boolean isYesterDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return (nowDay - msgDay) == 1;
	}

	private static boolean isTheDayBeforeYesterDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return (nowDay - msgDay) == 2;
	}

	private static boolean isSameYear(Calendar now, Calendar msg) {
		int nowYear = now.get(Calendar.YEAR);
		int msgYear = msg.get(Calendar.YEAR);

		return nowYear == msgYear;
	}

	// java Timestamp构造函数需传入Long型
	public static long IntToLong(int i) {
		long result = (long) i;
		result *= 1000;
		return result;
	}

	public static String getDate(int time) {
		if (msgCalendar == null)
			msgCalendar = Calendar.getInstance();
		msgCalendar.setTimeInMillis(IntToLong(time));
		if (dateFormat == null) {
			dateFormat = new java.text.SimpleDateFormat(YEAR_FORMAT);
		}
		String result = dateFormat.format(msgCalendar.getTime());
		return new StringBuilder(result).toString();
	}

	// public static void dealMills(ItemBean bean) {
	// Date date = new Date(bean.getCreated_at());
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(date);
	// bean.setMills(calendar.getTimeInMillis());
	// }
	public static Boolean isInHours(int starthour,int endhour){
	Calendar nowCalendar = Calendar.getInstance();
	nowCalendar.setTimeInMillis(System.currentTimeMillis());
 	if (msgCalendar == null){
		msgCalendar = Calendar.getInstance();
	}
	int nowHour = nowCalendar.get(Calendar.HOUR_OF_DAY);
 		if(starthour<=nowHour&&nowHour<=endhour){
			return true;
		}else if(endhour<starthour){
			if(nowHour>=starthour||nowHour<=endhour){
				return true;
			}
		}
 		return false;
	}
	
    public   static void addCommentTimeMap(String id){
		long now = System.currentTimeMillis();
    	XApplication.getInstance().getCommentTimeMap().put(id, now);
     }
    
    public static boolean isCommenttimeInLimit(String id){
    	long sendtime = 0;
    	sendtime = XApplication.getInstance().getCommentTimeMap().get(id) == null?0:XApplication.getInstance().getCommentTimeMap().get(id);
    	if(sendtime==0){
    		return false;
    	}else{
    	return isInLimitSec(sendtime,AppConfigs.COMMENT_TIME_LIMIT);
    	}
     }
    
	private static boolean isInLimitSec(long time,long Range) {
		long now = System.currentTimeMillis();
		if(now-time>Range){
			return false;
		}else{
			return true;
		}
 	}


}
