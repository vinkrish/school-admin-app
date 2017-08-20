package com.shikshitha.admin.calendar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.shikshitha.admin.R;
import com.shikshitha.admin.model.Evnt;
import com.shikshitha.admin.util.DividerItemDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompactCalendarFragment extends Fragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private static final String TAG = "CompactCalendarFragment";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private boolean shouldShow = false;
    private CompactCalendarView compactCalendarView;
    private ActionBar toolbar;

    private Evnts evnts;
    private EventsAdapter adapter;

    LinkedHashMap<String, List<Evnt>> evntsList = new LinkedHashMap<>();

    public static CompactCalendarFragment newInstance(Evnts evnts) {
        CompactCalendarFragment fragment = new CompactCalendarFragment();
        Bundle args = new Bundle();
        if(evnts != null){
            args.putSerializable("evnts", evnts);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        evnts = (Evnts) args.getSerializable("evnts");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar_fragment,container,false);

        ButterKnife.bind(this, v);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        adapter = new EventsAdapter(getActivity(), new ArrayList<Evnt>(0));
        recyclerView.setAdapter(adapter);

        final ImageView showPreviousMonthBut = (ImageView) v.findViewById(R.id.prev_button);
        final ImageView showNextMonthBut = (ImageView) v.findViewById(R.id.next_button);
        final Button showCalendarWithAnimationBut = (Button) v.findViewById(R.id.show_with_animation_calendar);

        compactCalendarView = (CompactCalendarView) v.findViewById(R.id.compactcalendar_view);

        // below allows you to configure color for the current day in the month
        // compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.black));
        // below allows you to configure colors for the current day the user has selected
        // compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.dark_red));
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        loadEvents();

        adapter.setDataSet(evntsList.get(dateFormat.format(new Date())));

        compactCalendarView.invalidate();

        // show days from other months as greyed out days
        // compactCalendarView.displayOtherMonthDays(true);

        //set initial title
        toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                adapter.setDataSet(evntsList.get(dateFormat.format(dateClicked)));
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
                adapter.setDataSet(evntsList.get(dateFormat.format(firstDayOfNewMonth)));
            }
        });

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showPreviousMonth();
                adapter.setDataSet(evntsList.get(dateFormat.format(compactCalendarView.getFirstDayOfCurrentMonth())));
            }
        });

        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.showNextMonth();
                adapter.setDataSet(evntsList.get(dateFormat.format(compactCalendarView.getFirstDayOfCurrentMonth())));
            }
        });


        final View.OnClickListener exposeCalendarListener = getCalendarExposeLis();
        showCalendarWithAnimationBut.setOnClickListener(exposeCalendarListener);

        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });


        // uncomment below to show indicators above small indicator events
         compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        // uncomment below to open onCreate
        //openCalendarOnCreate(v);

        return v;
    }

    @NonNull
    private View.OnClickListener getCalendarShowLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendar();
                    } else {
                        compactCalendarView.hideCalendar();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener getCalendarExposeLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendarWithAnimation();
                    } else {
                        compactCalendarView.hideCalendarWithAnimation();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    private void openCalendarOnCreate(View v) {
        final RelativeLayout layout = (RelativeLayout)v.findViewById(R.id.main_content);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                compactCalendarView.showCalendarWithAnimation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        // Set to current day on resume to set calendar to latest day
        // toolbar.setTitle(dateFormatForMonth.format(new Date()));
    }

    private void loadEvents() {
        addEvnts();
    }

    private void addEvnts() {
        List<Evnt> evntss = evnts.getEvents();
        Set<String> dates = new HashSet<>();
        for(Evnt e: evntss) {
            dates.add(e.getStartDate());
        }

        for(String date: dates) {
            List<Evnt> evntsLis = new ArrayList<>();
            for(Evnt e: evntss) {
                if(e.getStartDate().equals(date)) {
                    evntsLis.add(e);
                }
            }
            evntsList.put(date, evntsLis);
        }
        for (Map.Entry<String, List<Evnt>> entry : evntsList.entrySet()) {
            currentCalender.setTime(getFormattedDate(entry.getKey()));
            setToMidnight(currentCalender);
            List<Event> events = new ArrayList<>();
            for(Evnt ev: entry.getValue()) {
                Event eve = new Event(Color.argb(255, 255, 31, 31), currentCalender.getTimeInMillis(), ev.getEventTitle());
                events.add(eve);
            }
            compactCalendarView.addEvents(events);
        }
    }

    private Date getFormattedDate(String eventDate) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = new Date();
        try {
            date = defaultFormat.parse(eventDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 255, 82, 82), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 255, 82, 82), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 255, 57, 57), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 255, 82, 82), timeInMillis, "Event at " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 255, 57, 57), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 255, 31, 31), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}