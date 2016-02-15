package tr.com.hacktusdynamics.android.silmi.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import tr.com.hacktusdynamics.android.silmi.R;

public class AlarmCardView extends CardView {
    private static final String TAG = AlarmCardView.class.getSimpleName();

    private static final String KEY_SUPER_STATE = "superState";
    private static final String KEY_DATE_TEXT = "dateText";
    private static final String KEY_TIME_TEXT = "timeText";
    private static final String KEY_STATE_TOGGLE = "stateToggle";

    private Button timeButton;
    private ToggleButton toggleButton;
    private TextView dateTextView, timeTextView;
    private ImageView boxNumberImageView;

    private String mDateString, mTimeString;
    private boolean mStateToggle; //keeps toggle button state
    private long mBoxAlarmDateTime; //keeps date and time
    private Drawable mImageDrawable;

    private CompoundButton.OnCheckedChangeListener onToggleButtonClickListener;
    private View.OnClickListener onTimeButtonClickListener;

    public AlarmCardView(Context context) {
        /*
        super(context);
        init();
        */
        this(context, null);
    }

    public AlarmCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.AlarmCardViewSTYLEABLE, 0, 0);
        try {
            mImageDrawable = typedArray.getDrawable(R.styleable.AlarmCardViewSTYLEABLE_box_number_image);
            mDateString = typedArray.getString(R.styleable.AlarmCardViewSTYLEABLE_date_text_view);
            mTimeString = typedArray.getString(R.styleable.AlarmCardViewSTYLEABLE_time_text_view);
        }finally {
            typedArray.recycle();
        }
        init();
    }

    private void init() {
        setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        setPreventCornerOverlap(false);
        setCardBackgroundColor(getContext().getResources().getColor(R.color.alarm_cardview_background));

        removeAllViews();

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.alarm_card_view, this);

        toggleButton = (ToggleButton) findViewById(R.id.toggle_btn);
        timeButton = (Button) findViewById(R.id.time_btn);
        dateTextView = (TextView) findViewById(R.id.date_tv);
        timeTextView = (TextView) findViewById(R.id.time_tv);
        boxNumberImageView = (ImageView) findViewById(R.id.box_number_iv);
/*
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleButton.setTextColor(isChecked ? Color.GREEN : Color.RED);
                setStateToggle(isChecked);

                Log.d(TAG, "inside toggle btn clicked");
                Log.d(TAG, "mBoxAlarmDateTime: " + mBoxAlarmDateTime);

                // Do work from Activty onClickListener
                if (onToggleButtonClickListener != null) {
                    onToggleButtonClickListener.onCheckedChanged(buttonView, isChecked);
                }
            }
        });
*/

        toggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButton.setTextColor(getStateToggle() ? Color.GREEN : Color.RED);
                setStateToggle(!getStateToggle());
                Log.d(TAG, "inside toggle btn clicked");
                Log.d(TAG, "mBoxAlarmDateTime1: " + mBoxAlarmDateTime);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "inside time btn clicked");
                //setTimeString("Morning");

                final View dialogView = View.inflate(getContext(), R.layout.custom_datetime_dialog, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

                dialogView.findViewById(R.id.my_date_time_set_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.my_date_picker);
                        TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.my_time_picker);
                        Calendar calendar = new GregorianCalendar(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth(),
                                timePicker.getCurrentHour(),
                                timePicker.getCurrentMinute());

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        String timeString = sdf.format(calendar.getTime());

                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        sdf = new SimpleDateFormat("d MMM yyyy");
                        String dateString = sdf.format(new Date(year - 1900, month, day));

                        setBoxAlarmDateTime(calendar.getTime().getTime());//after choosing date and time save datetime on member variable
                        setStateToggle(true);
                        setDateString(dateString);
                        setTimeString(timeString);
                        Log.d(TAG, "mBoxAlarmDateTime2: " + mBoxAlarmDateTime);

                        alertDialog.dismiss();
                    }
                });
                alertDialog.setView(dialogView);
                alertDialog.show();

                /** Do work from Activty onClickListener*/
                if (onTimeButtonClickListener != null) {
                    onTimeButtonClickListener.onClick(view);
                }
            }
        });

        refresh();
    }

    private void refresh() {
        handleBoxNumberImageView();
        handleTimeTextView();
        handleDateTextView();
        handleToggleButton();
    }

    private void handleBoxNumberImageView() {
        if(mImageDrawable != null){
            boxNumberImageView.setImageDrawable(mImageDrawable);
        }
    }

    private void handleTimeTextView() {
        if(mTimeString != null){
            timeTextView.setText(mTimeString);
        }
    }

    private void handleDateTextView() {
        if(mDateString != null){
            dateTextView.setText(mDateString);
        }
    }

    private void handleToggleButton(){
        toggleButton.setChecked(mStateToggle);
    }

    //getters setters
    public CompoundButton.OnCheckedChangeListener getOnToggleButtonClickListener() {
        return onToggleButtonClickListener;
    }
    public void setOnToggleButtonClickListener(CompoundButton.OnCheckedChangeListener onToggleButtonClickListener) {
        this.onToggleButtonClickListener = onToggleButtonClickListener;
    }

    public View.OnClickListener getOnTimeButtonClickListener() {
        return onTimeButtonClickListener;
    }
    public void setOnTimeButtonClickListener(View.OnClickListener onTimeButtonClickListener) {
        this.onTimeButtonClickListener = onTimeButtonClickListener;
    }

    public boolean getStateToggle() {
        return mStateToggle;
    }
    public void setStateToggle(boolean state) {
        mStateToggle = state;
        refresh();
    }

    public long getBoxAlarmDateTime() {
        return mBoxAlarmDateTime;
    }
    public void setBoxAlarmDateTime(long boxAlarmDateTime) {
        this.mBoxAlarmDateTime = boxAlarmDateTime;
    }

    public String getDateString() {
        return mDateString;
    }
    public void setDateString(String dateString) {
        mDateString = dateString;
        refresh();
    }

    public String getTimeString() {
        return mTimeString;
    }
    public void setTimeString(String timeString) {
        mTimeString = timeString;
        refresh();
    }

    public Drawable getImageDrawable() {
        return mImageDrawable;
    }
    public void setImageDrawable(Drawable imageDrawable) {
        mImageDrawable = imageDrawable;
        refresh();
    }
    public void setImageDrawable(int imageResource) {
        mImageDrawable = getContext().getResources().getDrawable(imageResource);
        refresh();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState());
        bundle.putString(KEY_DATE_TEXT, mDateString);
        bundle.putString(KEY_TIME_TEXT, mTimeString);
        bundle.putBoolean(KEY_STATE_TOGGLE, mStateToggle);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mDateString = bundle.getString(KEY_DATE_TEXT);
            mTimeString = bundle.getString(KEY_TIME_TEXT);
            mStateToggle = bundle.getBoolean(KEY_STATE_TOGGLE);
            super.onRestoreInstanceState(bundle.getParcelable(KEY_SUPER_STATE));
        }else {
            super.onRestoreInstanceState(state);
        }

        refresh();
    }
}

