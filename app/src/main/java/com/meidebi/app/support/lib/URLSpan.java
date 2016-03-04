package com.meidebi.app.support.lib;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.style.ClickableSpan;
import android.view.View;

import com.meidebi.app.support.utils.component.IntentUtil;
import com.meidebi.app.ui.browser.BrowserWebActivity;

/**
 * User: qii
 * Date: 12-8-20
 */
public class URLSpan extends ClickableSpan implements ParcelableSpan {

    private final String mURL;

    public URLSpan(String url) {
        mURL = url;
    }

    public URLSpan(Parcel src) {
        mURL = src.readString();
    }

    public int getSpanTypeId() {
        return 11;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mURL);
    }

    public String getURL() {
        return mURL;
    }

    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        if (uri.getScheme().startsWith("http")) {
            String url = uri.toString();
            Context context = widget.getContext();
//            if (Utility.isWeiboAccountIdLink(url)) {
//                Intent intent = new Intent(context, UserInfoActivity.class);
//                intent.putExtra("id", Utility.getIdFromWeiboAccountLink(url));
//                context.startActivity(intent);
//            } else if (Utility.isWeiboAccountDomainLink(url)) {
//                Intent intent = new Intent(context, UserInfoActivity.class);
//                intent.putExtra("domain", Utility.getDomainFromWeiboAccountLink(url));
//                context.startActivity(intent);
//            } else {
        	IntentUtil.start_activity((Activity)context, BrowserWebActivity.class,  new BasicNameValuePair(
                "url", url));   
        }
//        } else {
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
//            context.startActivity(intent);
//        }
    }


//    public void onLongClick(View widget) {
//        Uri data = Uri.parse(getURL());
//        if (data != null) {
//            String d = data.toString();
//            String newValue = "";
//            if (d.startsWith("org.qii.weiciyuan")) {
//                int index = d.lastIndexOf("/");
//                newValue = d.substring(index + 1);
//            } else if (d.startsWith("http")) {
//                newValue = d;
//            }
//            if (!TextUtils.isEmpty(newValue)) {
//                Utility.vibrate(widget.getContext(), widget);
//                LongClickLinkDialog dialog = new LongClickLinkDialog(data);
//                dialog.show(((Activity) widget.getContext()).getFragmentManager(), "");
//
//            }
//
//        }
//    }

//    @Override
//    public void updateDrawState(TextPaint tp) {
//        int[] attrs = new int[]{R.attr.link_color};
//        TypedArray ta = XApplication.getInstance().getActivity().obtainStyledAttributes(attrs);
//        int drawableFromTheme = ta.getColor(0, 430);
//        tp.setColor(drawableFromTheme);
////        tp.setUnderlineText(true);
//    }
}