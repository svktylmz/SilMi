package tr.com.hacktusdynamics.android.silmi.models;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class MyLab {
    private static final String TAG = MyLab.class.getSimpleName();

    private static MyLab sMyLab;
    private Context mContext;

    private List<Box> mCreateAlarmList;

    //Constructor private
    private MyLab(Context context){
        mContext = context;

        mCreateAlarmList = new ArrayList<>();

        //create10DummyBoxes(); //seed data
    }

    /** Singleton method to create MyLab instance */
    public static MyLab get(Context context){
        if(sMyLab == null)
            sMyLab = new MyLab(context);

        return sMyLab;
    }


    public List<Box> getCreateAlarmList() {
        return mCreateAlarmList;
    }

    public void setCreateAlarmList(List<Box> mCreateAlarmList) {
        this.mCreateAlarmList = mCreateAlarmList;
    }

    public void addCreateAlarmList(Box box){
        mCreateAlarmList.add(box);
    }

    public void removeCreateAlarmList(Box box){
        for(Box b : mCreateAlarmList){
            if(b.getId().equals(b.getId()))
                mCreateAlarmList.remove(box);
        }
    }
}
