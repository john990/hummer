package com.rise.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo.holographlibrary.PieGraph;
import com.echo.holographlibrary.PieSlice;
import com.rise.R;

/**
 * Created by kai.wang on 2/22/14.
 */
public class ReportFragment extends Fragment implements BaseFragment{
    private PieGraph pieGraph;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_report,null);
        injectViews(container);
        return container;
    }

    @Override
    public void injectViews(View parentView) {
        pieGraph = (PieGraph) parentView.findViewById(R.id.report_pie_graph);

        PieSlice slice = new PieSlice();
        slice.setColor(Color.parseColor("#99CC00"));
        slice.setValue(2);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#FFBB33"));
        slice.setValue(3);
        pieGraph.addSlice(slice);
        slice = new PieSlice();
        slice.setColor(Color.parseColor("#AA66CC"));
        slice.setValue(8);
        pieGraph.addSlice(slice);

    }
}