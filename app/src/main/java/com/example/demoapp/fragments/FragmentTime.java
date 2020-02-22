package com.example.demoapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demoapp.R;
import com.example.demoapp.api.ShowFragmentListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.joda.time.DateTime;

public class FragmentTime extends Fragment {

        TextView textView;
        TextView textViewLecture;
        private Handler mHandler;
        FloatingActionButton floatingActionGoFragmentGift;
        private ShowFragmentListener showFragmentListener;

        final int[] belltime = {30600, 46800, 50400, 64800};


        @SuppressLint("HandlerLeak")
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_time, container, false);

            floatingActionGoFragmentGift = rootView.findViewById(R.id.floatingActionGoFragmentGift);
            floatingActionGoFragmentGift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragmentListener.showFragment(new FragmentGift());
                }
            });

            textView = rootView.findViewById(R.id.textView);
            textViewLecture = rootView.findViewById(R.id.textViewLecture);

            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    handlerUpdate();
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                }
            };
            mHandler.sendEmptyMessage(0);
            return rootView;
        }


        public void handlerUpdate(){

            DateTime datetime = DateTime.now();

            int currentTime;
            int timeToLecture;

            int timeToLectureHour;
            int timeToLectureMin;
            int timeToLectureSec;

            int hour = datetime.getHourOfDay();
            int min = datetime.getMinuteOfHour();
            int sec = datetime.getSecondOfMinute();

            int belltimeToLecture = 0;

            int Lecture = 0;

            currentTime = hour * 60 * 60 + min * 60 + sec;

            for (int i = 0; i < 4; i++) {

                    switch (i){
                        case 0:
                            textViewLecture.setText("До начала \nрабочей смены:");
                            break;
                        case 1:
                            textViewLecture.setText("До начала \nобеда:");
                            break;
                        case 2:
                            textViewLecture.setText("До конца \nобеда:");
                            break;
                        case 3:
                            textViewLecture.setText("До конца \nрабочей смены:");
                            break;
                    }


                if (belltime[i] > currentTime) {
                    belltimeToLecture = belltime[i];
                    break;
                }

                if(i == 5){
                    textViewLecture.setText("Рабочая смена окончена");
                    textView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.VISIBLE);
                }
            }

            timeToLecture = belltimeToLecture - currentTime;

            timeToLectureHour = timeToLecture / 3600;
            timeToLectureMin = timeToLecture / 60 - timeToLectureHour * 60;
            timeToLectureSec = timeToLecture - timeToLectureHour * 3600 - timeToLectureMin * 60;

            String StringTimeToLectureHour;
            String StringTimeToLectureMin;
            String StringTimeToLectureSec;

            if (timeToLectureHour < 10) {
                StringTimeToLectureHour = "0" + timeToLectureHour + "ч. ";
            } else {
                StringTimeToLectureHour = timeToLectureHour + "ч. ";
            }
            if (timeToLectureMin < 10) {
                StringTimeToLectureMin = "0" + timeToLectureMin + "м. ";
            } else {
                StringTimeToLectureMin = timeToLectureMin + "м. ";
            }
            if (timeToLectureSec < 10) {
                StringTimeToLectureSec = "0" + timeToLectureSec + "с.";
            } else {
                StringTimeToLectureSec = timeToLectureSec + "с.";
            }

            if (StringTimeToLectureHour.equals("00ч. ") && StringTimeToLectureMin.equals("00м. ")) {
                textView.setText(StringTimeToLectureSec);
            } else {
                if (StringTimeToLectureHour.equals("00ч. ")) {
                    textView.setText(StringTimeToLectureMin + StringTimeToLectureSec);
                } else {
                    textView.setText(StringTimeToLectureHour + StringTimeToLectureMin + StringTimeToLectureSec);
                }
            }
        }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ShowFragmentListener) {
            showFragmentListener = (ShowFragmentListener) context;
        }
    }
}
