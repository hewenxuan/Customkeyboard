/*
 * Copyright (C) 2008,2009  OMRON SOFTWARE Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2008-2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.googlecode.openwnn.legacy.ZH.CN;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.googlecode.openwnn.legacy.OpenWnnZHCN;
import com.googlecode.openwnn.legacy.ZH.DefaultSoftKeyboardZH;
import com.mykeyboard.R;

import java.util.ArrayList;
import java.util.List;

public class TutorialZHCN implements OnTouchListener {

	private List<Bubble> mBubbles = new ArrayList<Bubble>();
	private View mInputView;
	private OpenWnnZHCN mIme;
	private int[] mLocation = new int[2];
	private static final int MSG_SHOW_BUBBLE = 0;

	private int mBubbleIndex;
	private boolean mEnableKeyTouch = false;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_BUBBLE:
				Bubble bubba = (Bubble) msg.obj;
				bubba.show(mLocation[0], mLocation[1]);
				break;
			}
		}
	};

	class Bubble {
		Drawable bubbleBackground;
		int x;
		int y;
		int width;
		int gravity;
		CharSequence text;
		boolean dismissOnTouch;
		boolean dismissOnClose;
		PopupWindow window;
//		TextView textView;
		View inputView;

		Bubble(Context context, View inputView, int backgroundResource, int bx, int by, int description, int guide) {

			CharSequence text = context.getResources().getText(description);
			init(context, inputView, backgroundResource, bx, by, text, guide, false);
		}

		Bubble(Context context, View inputView, int backgroundResource, int bx, int by, CharSequence description, int guide, boolean leftAlign) {
			init(context, inputView, backgroundResource, bx, by, description, guide, leftAlign);
		}

		void init(Context context, View inputView, int backgroundResource, int bx, int by, CharSequence description, int guide, boolean leftAlign) {
			bubbleBackground = context.getResources().getDrawable(backgroundResource);
			x = bx;
			y = by;
			width = (int) (inputView.getWidth() * 0.9);
			this.gravity = Gravity.TOP | Gravity.LEFT;
			text = new SpannableStringBuilder().append(description).append("\n").append(context.getResources().getText(guide));
			this.dismissOnTouch = true;
			this.dismissOnClose = false;
			this.inputView = inputView;
			window = new PopupWindow(context);
			window.setBackgroundDrawable(null);

			window.setFocusable(false);
			window.setTouchable(true);
			window.setOutsideTouchable(false);
		}

		private int chooseSize(PopupWindow pop, View parentView, CharSequence text, TextView tv) {
			int wid = tv.getPaddingLeft() + tv.getPaddingRight();
			int ht = tv.getPaddingTop() + tv.getPaddingBottom();

			/*
			 * Figure out how big the text would be if we laid it out to the
			 * full width of this view minus the border.
			 */
			int cap = width - wid;

			Layout l = new StaticLayout(text, tv.getPaint(), cap, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
			float max = 0;
			for (int i = 0; i < l.getLineCount(); i++) {
				max = Math.max(max, l.getLineWidth(i));
			}

			/*
			 * Now set the popup size to be big enough for the text plus the
			 * border.
			 */
			pop.setWidth(width);
			pop.setHeight(ht + l.getHeight());
			return l.getHeight();
		}

		void show(int offx, int offy) {


		}

		void hide() {
			if (window.isShowing()) {

				window.dismiss();
			}
		}

		boolean isShowing() {
			return window.isShowing();
		}
	}

	/** Constructor */
	public TutorialZHCN(OpenWnnZHCN ime, View inputView, DefaultSoftKeyboardZH inputManager) {
		mInputView = inputView;
		mIme = ime;

		Context context = inputView.getContext();
		int inputWidth = inputView.getWidth();
		Resources r = inputView.getContext().getResources();
		final int x = inputWidth / 20;
		r.getDimensionPixelOffset(22);
	}

	private void setSpan(SpannableStringBuilder spannable, String marker, int imageResourceId) {
		String text = spannable.toString();
		int target = text.indexOf(marker);
		while (0 <= target) {
			ImageSpan span = new ImageSpan(mIme, imageResourceId, DynamicDrawableSpan.ALIGN_BOTTOM);
			spannable.setSpan(span, target, target + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			target = text.indexOf(marker, target + 1);
		}
	}

	public void start() {
		mInputView.getLocationInWindow(mLocation);
		mBubbleIndex = -1;
		mInputView.setOnTouchListener(this);
		next();
	}

	boolean next() {
		if (mBubbleIndex >= 0) {
			if (!mBubbles.get(mBubbleIndex).isShowing()) {
				return true;
			}
			for (int i = 0; i <= mBubbleIndex; i++) {
				mBubbles.get(i).hide();
			}
		}
		mBubbleIndex++;
		if (mBubbleIndex >= mBubbles.size()) {
			mEnableKeyTouch = true;
			mIme.sendDownUpKeyEvents(-1);
			mIme.tutorialDone();
			return false;
		}

		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SHOW_BUBBLE, mBubbles.get(mBubbleIndex)), 500);
		return true;
	}

	void hide() {
		for (int i = 0; i < mBubbles.size(); i++) {
			mBubbles.get(i).hide();
		}
		mInputView.setOnTouchListener(null);
	}

	public boolean close() {
		mHandler.removeMessages(MSG_SHOW_BUBBLE);
		hide();
		return true;
	}

	public boolean onTouch(View v, MotionEvent event) {
		boolean ret = !mEnableKeyTouch;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mBubbleIndex >= mBubbles.size()) {
				mInputView.setOnTouchListener(null);
			}
		}
		return ret;
	}
}
