package com.meidebi.app.support.utils;


import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

public class ListViewUtils {
	private ListViewUtils() {

	}

	/**
	 * 滚动列表到顶端
	 * 
	 * @param listView
	 */
	public static void smoothScrollListViewToTop(final ListView listView) {
		if (listView == null) {
			return;
		}
		smoothScrollListView(listView, 0);
		listView.postDelayed(new Runnable() {

			@Override
			public void run() {
				listView.setSelection(0);
			}
		}, 200);
	}

    /**
     * 滚动列表到顶端
     *
     * @param listView
     */
    public static void smoothScrollListViewToTop(final RecyclerView listView) {
        if (listView == null) {
            return;
        }

        listView.smoothScrollToPosition(0);

    }

    /**
     * 滚动列表到position
     *
     * @param listView
     * @param position
     */
    public static void smoothScrollListView(RecyclerView listView, int position) {
        if (listView == null) {
            return;
        }
        listView.smoothScrollToPosition(position);
    }


	/**
	 * 滚动列表到position
	 * 
	 * @param listView
	 * @param position
	 */
	public static void smoothScrollListView(ListView listView, int position) {
		if (VERSION.SDK_INT > 13) {
			listView.smoothScrollToPositionFromTop(position, position);
		} else {
			int firstVisible = listView.getFirstVisiblePosition();
			int lastVisible = listView.getLastVisiblePosition();
			if (position < firstVisible)
				listView.smoothScrollToPosition(position);
			else
				listView.smoothScrollToPosition(position + lastVisible
						- firstVisible - 2);
		}
	}

	public static void smoothScrollToPositionFromTop(final AbsListView view,
			final int position) {
		View child = getChildAtPosition(view, position);
		// There's no need to scroll if child is already at top or view is
		// already scrolled to its end
		if ((child != null)
				&& ((child.getTop() == 0) || ((child.getTop() > 0)))) {
			if (VERSION.SDK_INT > 13) {
				if (!view.canScrollVertically(1))
					return;
			} else {
				return;
			}
		}

		view.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(final AbsListView view,
					final int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					view.setOnScrollListener(null);

					// Fix for scrolling bug
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							view.setSelection(position);
						}
					});
				}
			}

			@Override
			public void onScroll(final AbsListView view,
					final int firstVisibleItem, final int visibleItemCount,
					final int totalItemCount) {
			}
		});

		// Perform scrolling to position
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				ListViewUtils.smoothScrollListView((ListView) view, position);
			}
		});
	}

	public static View getChildAtPosition(AdapterView view, int position) {
		final int numVisibleChildren = view.getChildCount();
		final int firstVisiblePosition = view.getFirstVisiblePosition();

		for (int i = 0; i < numVisibleChildren; i++) {
			int positionOfView = firstVisiblePosition + i;
			if (positionOfView == position) {
				return view.getChildAt(i);
			}
		}
		return null;
	}
}
