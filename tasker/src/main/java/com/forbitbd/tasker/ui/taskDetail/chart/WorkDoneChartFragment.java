package com.forbitbd.tasker.ui.taskDetail.chart;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.forbitbd.androidutils.utils.MyUtil;
import com.forbitbd.tasker.R;
import com.forbitbd.tasker.models.WorkDone;
import com.forbitbd.tasker.ui.taskDetail.BaseDetailFragment;
import com.forbitbd.tasker.utils.DateAxisFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkDoneChartFragment extends BaseDetailFragment implements ChartContract.View {



    private BarChart barChart;
    private ChartPresenter mPresenter;


    public WorkDoneChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChartPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_work_done_chart, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.bar_chart);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.processWorkDoneList(getWorkdoneList());
    }

    private void createChart(List<WorkDone> workDoneList){

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);
        barChart.setExtraOffsets(10, 10, 10, getResources().getDimension(R.dimen.large_padding));

        List<String> xAxisLabels = new ArrayList<>();
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (WorkDone x: workDoneList) {
            yVals1.add(new BarEntry(workDoneList.indexOf(x), (float) x.getAmount()));
            xAxisLabels.add(MyUtil.getStringDate(x.getDate()));
        }

        IAxisValueFormatter xAxisFormatter = new DateAxisFormatter(xAxisLabels);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90f);
        //xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        YAxis leftAxis = barChart.getAxisLeft();
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        // Disable Right Axis
        barChart.getAxisRight().setEnabled(false);



        BarDataSet dataSet = new BarDataSet(yVals1,"Work Done");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(mTfLight);
        data.setBarWidth(0.9f);
        barChart.setData(data);
        barChart.animateXY(1000,1000);
        barChart.invalidate();

    }


    public void update(List<WorkDone> workDoneList){
        mPresenter.processWorkDoneList(workDoneList);
    }

    @Override
    public void showChart(List<WorkDone> workDoneList) {

        createChart(workDoneList);
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        super.onDestroy();
    }
}
