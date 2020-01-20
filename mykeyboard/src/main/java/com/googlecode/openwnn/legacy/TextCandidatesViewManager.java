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

package com.googlecode.openwnn.legacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.mykeyboard.R;

import java.util.ArrayList;

/**
 * The default candidates view manager class using {@link EditText}.
 * 
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD. All Rights Reserved.
 */
public class TextCandidatesViewManager implements CandidatesViewManager, GestureDetector.OnGestureListener {
	/** Height of a line */
	public static final int LINE_HEIGHT = 34;
	/** Number of lines to display (Portrait) */
	public static final int LINE_NUM_PORTRAIT = 2;
	/** Number of lines to display (Landscape) */
	public static final int LINE_NUM_LANDSCAPE = 1;

	/** Maximum lines */
	private static final int DISPLAY_LINE_MAX_COUNT = 1000;
	/** Width of the view */
	private static final int CANDIDATE_MINIMUM_WIDTH = 48;
	/** Height of the view */
	private static final int CANDIDATE_MINIMUM_HEIGHT = 35;
	/**
	 * Align the candidate left if the width of the string exceeds this
	 * threshold
	 */
	private static final int CANDIDATE_LEFT_ALIGN_THRESHOLD = 120;
	/** Maximum number of displaying candidates par one line (full view mode) */
	private static final int FULL_VIEW_DIV = 4;

	/** Body view of the candidates list */
//	private ViewGroup mViewBody;
	/** Scroller of {@code mViewBodyText} */
//	private ScrollView mViewBodyScroll;
	/** Base of {@code mViewCandidateList1st}, {@code mViewCandidateList2nd} */
//	private ViewGroup mViewCandidateBase;
	/** Button displayed bottom of the view when there are more candidates. */
//	private ImageView mReadMoreButton;
	/** The view of the scaling up candidate */
	private View mViewScaleUp;
	/** Layout for the candidates list on normal view */
	private LinearLayout mViewCandidateList1st;
	/** Layout for the candidates list on full view */
//	private RelativeLayout mViewCandidateList2nd;
	/** {@link OpenWnn} instance using this manager */
	private OpenWnn mWnn;
	/** View type (VIEW_TYPE_NORMAL or VIEW_TYPE_FULL or VIEW_TYPE_CLOSE) */
	private int mViewType;
	/** Portrait display({@code true}) or landscape({@code false}) */
	private boolean mPortrait;

	/** Width of the view */
	private int mViewWidth;
	/** Height of the view */
	private int mViewHeight;
	/** Whether hide the view if there is no candidates */
	private boolean mAutoHideMode;
	/**
	 * The converter to be get candidates from and notice the selected candidate
	 * to.
	 */
	private WnnEngine mConverter;
	/** Limitation of displaying candidates */
	private int mDisplayLimit;

	/** Vibrator for touch vibration */
	private Vibrator mVibrator = null;
	/** MediaPlayer for click sound */
	private MediaPlayer mSound = null;

	/** Number of candidates displaying */
	private int mWordCount;
	/** List of candidates */
	private ArrayList<WnnWord> mWnnWordArray;

	/** Gesture detector */
	private GestureDetector mGestureDetector;
	/** The word pressed */
	private WnnWord mWord;
	/** Character width of the candidate area */
	private int mLineLength = 0;
	/** Number of lines displayed */
	private int mLineCount = 1;

	/** {@code true} if the candidate delete state is selected */
	private boolean mIsScaleUp = false;

	/** {@code true} if the full screen mode is selected */
	private boolean mIsFullView = false;

	/** The event object for "touch" */
	private MotionEvent mMotionEvent = null;

	/** The offset when the candidates is flowed out the candidate window */
	private int mDisplayEndOffset = 0;
	/** {@code true} if there are more candidates to display. */
	private boolean mCanReadMore = false;
	/** Width of {@code mReadMoreButton} */
	private int mReadMoreButtonWidth = 0;
	/** Color of the candidates */
	private int mTextColor = 0;
	/** Template object for each candidate and normal/full view change button */
	private TextView mViewCandidateTemplate;
	/** Number of candidates in full view */
	private int mFullViewWordCount;
	/** Number of candidates in the current line (in full view) */
	private int mFullViewOccupyCount;
	/** View of the previous candidate (in full view) */
	private TextView mFullViewPrevView;
	/** Id of the top line view (in full view) */
	private int mFullViewPrevLineTopId;
	/** Layout of the previous candidate (in full view) */
	private RelativeLayout.LayoutParams mFullViewPrevParams;
	/** Whether all candidates is displayed */
	private boolean mCreateCandidateDone;
	/** Number of lines in normal view */
	private int mNormalViewWordCountOfLine;
	/** general infomation about a display */
	private final DisplayMetrics mMetrics = new DisplayMetrics();

	/** Event listener for touching a candidate */
	private OnTouchListener mCandidateOnTouch = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			if (mMotionEvent != null) {
				return true;
			}

			if ((event.getAction() == MotionEvent.ACTION_UP) && (v instanceof TextView)) {
				Drawable d = v.getBackground();
				if (d != null) {
					d.setState(new int[] {});
				}
			}

			mMotionEvent = event;
			boolean ret = mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CANDIDATE_VIEW_TOUCH));
			mMotionEvent = null;
			return ret;
		}
	};

	/** Event listener for clicking a candidate */
	private OnClickListener mCandidateOnClick = new OnClickListener() {
		public void onClick(View v) {
			if (!v.isShown()) {
				return;
			}

			if (v instanceof TextView) {
				TextView text = (TextView) v;
				int wordcount = text.getId();
				WnnWord word = null;
				word = mWnnWordArray.get(wordcount);
				selectCandidate(word);
			}
		}
	};

	/** Event listener for long-clicking a candidate */
	private OnLongClickListener mCandidateOnLongClick = new OnLongClickListener() {
		public boolean onLongClick(View v) {
			if (mViewScaleUp == null) {
				return false;
			}

			if (!v.isShown()) {
				return true;
			}

			Drawable d = v.getBackground();
			if (d != null) {
				if (d.getState().length == 0) {
					return true;
				}
			}

			int wordcount = ((TextView) v).getId();
			mWord = mWnnWordArray.get(wordcount);
			setViewScaleUp(true, mWord);

			return true;
		}
	};

	/**
	 * Constructor
	 */
	public TextCandidatesViewManager() {
		this(-1);
	}

	/**
	 * Constructor
	 * 
	 * @param displayLimit
	 *            The limit of display
	 */
	public TextCandidatesViewManager(int displayLimit) {
		this.mDisplayLimit = displayLimit;
		this.mWnnWordArray = new ArrayList<WnnWord>();
		this.mAutoHideMode = true;
		mMetrics.setToDefaults();
	}

	/**
	 * Set auto-hide mode.
	 * 
	 * @param hide
	 *            {@code true} if the view will hidden when no candidate exists;
	 *            {@code false} if the view is always shown.
	 */
	public void setAutoHide(boolean hide) {
		mAutoHideMode = hide;
	}

	/** @see CandidatesViewManager */
	public View initView(OpenWnn parent, int width, int height) {
		mWnn = parent;
		mViewWidth = width;
		mViewHeight = height;
		mPortrait = (parent.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE);

		Resources r = mWnn.getResources();




		createNormalCandidateView();


//		mTextColor = r.getColor(R.color.candidate_text);


//


		setViewType(CandidatesViewManager.VIEW_TYPE_CLOSE);

		mGestureDetector = new GestureDetector(this);


		/* select button */
//

		return null;
	}

	/**
	 * Create the normal candidate view
	 */
	private void createNormalCandidateView() {

	}

	/** @see CandidatesViewManager#getCurrentView */
	public View getCurrentView() {
		return null;
	}

	/** @see CandidatesViewManager#setViewType */
	public void setViewType(int type) {

	}

	/**
	 * Set the view layout
	 *
	 * @param type
	 *            View type
	 * @return {@code true} if display is updated; {@code false} if otherwise
	 */
	private boolean setViewLayout(int type) {
		// mViewType = type;
		// setViewScaleUp(false, null);
		//
		// switch (type) {
		// case CandidatesViewManager.VIEW_TYPE_CLOSE:
		// mViewCandidateBase.setMinimumHeight(-1);
		// return false;
		//
		// case CandidatesViewManager.VIEW_TYPE_NORMAL:
		// mViewBodyScroll.scrollTo(0, 0);
		// mViewCandidateList1st.setVisibility(View.VISIBLE);
		// mViewCandidateList2nd.setVisibility(View.GONE);
		// mViewCandidateBase.setMinimumHeight(-1);
		// int line = (mPortrait) ? LINE_NUM_PORTRAIT : LINE_NUM_LANDSCAPE;
		// mViewCandidateList1st.setMinimumHeight(getCandidateMinimumHeight() *
		// line);
		// return false;
		//
		// case CandidatesViewManager.VIEW_TYPE_FULL:
		// default:
		// mViewCandidateList2nd.setVisibility(View.VISIBLE);
		// mViewCandidateBase.setMinimumHeight(mViewHeight);
		// return true;
		// }
		return true;
	}

	/** @see CandidatesViewManager#getViewType */
	public int getViewType() {
		return mViewType;
	}

	/** @see CandidatesViewManager#displayCandidates */
	public void displayCandidates(WnnEngine converter) {

		mCanReadMore = false;
		mDisplayEndOffset = 0;
		mIsFullView = false;
		mFullViewWordCount = 0;
		mFullViewOccupyCount = 0;
		mFullViewPrevLineTopId = 0;
		mCreateCandidateDone = false;
		mNormalViewWordCountOfLine = 0;

		clearCandidates();
		mConverter = converter;
		setViewLayout(CandidatesViewManager.VIEW_TYPE_NORMAL);

		// mViewCandidateTemplate.setVisibility(View.VISIBLE);
		// mViewCandidateTemplate.setBackgroundResource(R.drawable.cand_back);

		displayCandidates(converter, true, getMaxLine());
	}


	private int getMaxLine() {
		int maxLine = (mPortrait) ? LINE_NUM_PORTRAIT : LINE_NUM_LANDSCAPE;
		return maxLine;
	}

	/**
	 * Display the candidates.
	 *
	 * @param converter
	 *            {@link WnnEngine} which holds candidates.
	 * @param dispFirst
	 *            Whether it is the first time displaying the candidates
	 * @param maxLine
	 *            The maximum number of displaying lines
	 */
	synchronized private void displayCandidates(WnnEngine converter, boolean dispFirst, int maxLine) {

		return;
	}

	/**
	 * Add a candidate into the list.
	 *
	 * @param isCategory
	 *            {@code true}:caption of category, {@code false}:normal word
	 * @param word
	 *            A candidate word
	 */
	private void setCandidate(boolean isCategory, WnnWord word) {

	}



	/**
	 * Display {@code mReadMoreText} if there are more candidates.
	 */
	private void setReadMore() {

	}

	/**
	 * Clear the list of the normal candidate view.
	 */
	private void clearNormalViewCandidate() {

	}

	/** @see CandidatesViewManager#clearCandidates */
	public void clearCandidates() {
		// clearNormalViewCandidate();
		//
		// RelativeLayout layout = mViewCandidateList2nd;
		// int size = layout.getChildCount();
		// for (int i = 0; i < size; i++) {
		// View v = layout.getChildAt(i);
		// v.setVisibility(View.GONE);
		// }
		//
		// mLineCount = 1;
		// mWordCount = 0;
		// mWnnWordArray.clear();
		//
		// mLineLength = 0;
		//
		// mIsFullView = false;
		// setViewLayout(CandidatesViewManager.VIEW_TYPE_NORMAL);
		// if (mAutoHideMode) {
		// setViewLayout(CandidatesViewManager.VIEW_TYPE_CLOSE);
		// }
		//
		// if (mAutoHideMode && mViewBody.isShown()) {
		// mWnn.setCandidatesViewShown(false);
		// }
		// mCanReadMore = false;
		// setReadMore();
	}

	/** @see CandidatesViewManager#setPreferences */
	public void setPreferences(SharedPreferences pref) {
		try {
			if (pref.getBoolean("key_vibration", false)) {
				mVibrator = (Vibrator) mWnn.getSystemService(Context.VIBRATOR_SERVICE);
			} else {
				mVibrator = null;
			}
		} catch (Exception ex) {
			Log.d("iwnn", "NO VIBRATOR");
		}
	}

	/**
	 * Process {@code OpenWnnEvent.CANDIDATE_VIEW_TOUCH} event.
	 *
	 * @return {@code true} if event is processed; {@code false} if otherwise
	 */
	public boolean onTouchSync() {
		return mGestureDetector.onTouchEvent(mMotionEvent);
	}

	/**
	 * Select a candidate. <br>
	 * This method notices the selected word to {@link OpenWnn}.
	 *
	 * @param word
	 *            The selected word
	 */
	private void selectCandidate(WnnWord word) {
		setViewLayout(CandidatesViewManager.VIEW_TYPE_NORMAL);
		if (mVibrator != null) {
			try {
				mVibrator.vibrate(30);
			} catch (Exception ex) {
			}
		}
		if (mSound != null) {
			try {
				mSound.seekTo(0);
				mSound.start();
			} catch (Exception ex) {
			}
		}
		mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.SELECT_CANDIDATE, word));
	}

	/** @see android.view.GestureDetector.OnGestureListener#onDown */
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	/** @see android.view.GestureDetector.OnGestureListener#onFling */
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {

		return false;
	}

	/** @see android.view.GestureDetector.OnGestureListener#onLongPress */
	public void onLongPress(MotionEvent arg0) {
		return;
	}

	/** @see android.view.GestureDetector.OnGestureListener#onScroll */
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		return false;
	}

	/** @see android.view.GestureDetector.OnGestureListener#onShowPress */
	public void onShowPress(MotionEvent arg0) {
	}

	/** @see android.view.GestureDetector.OnGestureListener#onSingleTapUp */
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	/**
	 * Retrieve the width of string to draw.
	 * 
	 * @param text
	 *            The string
	 * @param start
	 *            The start position (specified by the number of character)
	 * @param end
	 *            The end position (specified by the number of character)
	 * @return The width of string to draw
	 */
	public int measureText(CharSequence text, int start, int end) {
		TextPaint paint = mViewCandidateTemplate.getPaint();
		return (int) paint.measureText(text, start, end);
	}

	/**
	 * Switch list/enlarge view mode.
	 * 
	 * @param up
	 *            {@code true}:enlarge, {@code false}:list
	 * @param word
	 *            The candidate word to be enlarged.
	 */
	private void setViewScaleUp(boolean up, WnnWord word) {

	}

	/**
	 * Create a layout for the next line.
	 */
	private void createNextLine() {
		int lineCount = mLineCount;
		if (mIsFullView || getMaxLine() < lineCount) {
			/* Full view */
			mFullViewOccupyCount = 0;
			mFullViewPrevLineTopId = mFullViewPrevView.getId();
		} else {
			/* Normal view */
			LinearLayout lineView = (LinearLayout) mViewCandidateList1st.getChildAt(lineCount - 1);
			float weight = 0;
			if (mLineLength < CANDIDATE_LEFT_ALIGN_THRESHOLD) {
				if (lineCount == 1) {
					mViewCandidateTemplate.setVisibility(View.GONE);
				}
			} else {
				weight = 1.0f;
			}

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, weight);

			int child = lineView.getChildCount();
			for (int i = 0; i < child; i++) {
				View view = lineView.getChildAt(i);

				if (view != mViewCandidateTemplate) {
					view.setLayoutParams(params);
				}
			}

			mLineLength = 0;
			mNormalViewWordCountOfLine = 0;
		}
		mLineCount++;
	}

	/**
	 * @return the minimum width of a candidate view.
	 */
	private int getCandidateMinimumWidth() {
		return (int) (CANDIDATE_MINIMUM_WIDTH * mMetrics.density);
	}

	/**
	 * @return the minimum height of a candidate view.
	 */
	private int getCandidateMinimumHeight() {
		return (int) (CANDIDATE_MINIMUM_HEIGHT * mMetrics.density);
	}
}
