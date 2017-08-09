package com.shikshitha.admin.calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shikshitha.admin.R;
import com.shikshitha.admin.util.DividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsFragment extends Fragment {
    @BindView(R.id.no_events)
    LinearLayout noEvents;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Evnts evnts;

    public static EventsFragment newInstance(Evnts evnts) {
        EventsFragment fragment = new EventsFragment();
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
        if (getArguments() != null) {
            Bundle args = getArguments();
            evnts = (Evnts) args.getSerializable("evnts");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        ButterKnife.bind(this, view);

        if(evnts.getEvents().size() == 0) {
            noEvents.setVisibility(View.VISIBLE);
        } else {
            noEvents.setVisibility(View.INVISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

            EventsAdapter adapter = new EventsAdapter(getActivity(), evnts.getEvents());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

}
