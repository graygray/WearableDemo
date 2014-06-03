/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
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
package com.primax.wearabledemo2.chart;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.primax.wearabledemo2.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * Sales demo bar chart.
 */
public class SalesStackedBarChart extends AbstractDemoChart {
	/**
	 * Returns the chart name.
	 * 
	 * @return the chart name
	 */
	public String getName() {
		return "Sales stacked bar chart";
	}

	/**
	 * Returns the chart description.
	 * 
	 * @return the chart description
	 */
	public String getDesc() {
		return "The monthly sales for the last 2 years (stacked bar chart)";
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public View execute(Context context) {
		String[] titles = new String[] { "Walking ", "Running" };
		List<double[]> values = new ArrayList<double[]>();
		// values.add(new double[] { 14230, 12300, 14240, 15244, 15900, 19200,
		// 22030, 21200, 19500, 15500,
		// 12600, 14000 });
		// values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030,
		// 11200, 9500, 10500,
		// 11600, 13500 });
		values.add(MainActivity.draw_y_walking);
		values.add(MainActivity.draw_y_running);
		int[] colors = new int[] { Color.BLUE, Color.CYAN };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Steps Count", "Time ( every min )",
				"Steps", 0, MainActivity.draw_xMax, 0,
				MainActivity.draw_yMax_stepCount, Color.WHITE, Color.GRAY);

		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);
		// renderer.setPanLimits(new double[] { -10, 200, -10, 500 });
		// renderer.setZoomLimits(new double[] { -10, 200, -10, 500 });

		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		// renderer.setPanEnabled(true, false);
		// renderer.setZoomEnabled(false);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		// return ChartFactory.getBarChartIntent(context,
		// buildBarDataset(titles, values), renderer,
		// Type.STACKED);
		return view;
	}

	public View executeCaloriesView(Context context) {
		String[] titles = new String[] { "Calories ( kcal ) " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_calories);
		int[] colors = new int[] { Color.RED };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Calories Burned", "Time ( every min )",
				"Kcal", 0, MainActivity.MAX_ARRAY_SIZE, 0,
				MainActivity.draw_yMax_calories, Color.WHITE, Color.GRAY);

		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

//		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}
	
	public View executeCaloriesViewPerDay(Context context) {
		String[] titles = new String[] { "Calories ( kcal ) " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_calories_perday);
		int[] colors = new int[] { Color.RED };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Calories Burned", "Date",
				"Kcal", 0, MainActivity.MAX_DAY_TO_DRAW+1, 0,
				MainActivity.draw_yMax_calories+100, Color.WHITE, Color.GRAY);

		for (int i = 0; i < MainActivity.MAX_DAY_TO_DRAW; i++) {
	    	renderer.addTextLabel(i+1, MainActivity.draw_x_dateArray[i]);
		}
	    renderer.setXLabels(0);
	    
		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
//		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}
	
	public View executeWalkingViewPerDay(Context context) {
		String[] titles = new String[] { "Walking " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_walking_perday);
		int[] colors = new int[] { Color.BLUE };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Steps Count", "Date",
				"Steps", 0, MainActivity.MAX_DAY_TO_DRAW+1, 0,
				MainActivity.draw_yMax_walkStepCount+100, Color.WHITE, Color.GRAY);

		for (int i = 0; i < MainActivity.MAX_DAY_TO_DRAW; i++) {
	    	renderer.addTextLabel(i+1, MainActivity.draw_x_dateArray[i]);
		}
	    renderer.setXLabels(0);
	    
		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
//		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}
	
	public View executeRunningViewPerDay(Context context) {
		String[] titles = new String[] { "Running " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_running_perday);
		int[] colors = new int[] { Color.CYAN };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Steps Count", "Date",
				"Steps", 0, MainActivity.MAX_DAY_TO_DRAW+1, 0,
				MainActivity.draw_yMax_runningStepCount+100, Color.WHITE, Color.GRAY);

		for (int i = 0; i < MainActivity.MAX_DAY_TO_DRAW; i++) {
	    	renderer.addTextLabel(i+1, MainActivity.draw_x_dateArray[i]);
		}
	    renderer.setXLabels(0);
	    
		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

		renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
//		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}
	
	public View executeWalkingView(Context context) {
		String[] titles = new String[] { "Walking " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_walking);
		int[] colors = new int[] { Color.BLUE };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Steps Count", "Time ( every min )",
				"Steps", 0, MainActivity.MAX_ARRAY_SIZE, 0,
				MainActivity.draw_yMax_walkStepCount, Color.WHITE, Color.GRAY);

		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}

	public View executeRunningView(Context context) {
		String[] titles = new String[] { "Running " };
		List<double[]> values = new ArrayList<double[]>();
		values.add(MainActivity.draw_y_running);
		int[] colors = new int[] { Color.CYAN };
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		setChartSettings(renderer, "Steps Count", "Time ( every min )",
				"Steps", 0, MainActivity.MAX_ARRAY_SIZE, 0,
				MainActivity.draw_yMax_runningStepCount, Color.WHITE, Color.GRAY);

		renderer.setChartTitleTextSize(50);
		renderer.setAxisTitleTextSize(40);
		renderer.setLabelsTextSize(30);
		renderer.setLegendTextSize(40);
		renderer.setPointSize(8);
		int[] mar = new int[] { 80, 80, 150, 30 };
		renderer.setMargins(mar);

		renderer.setXLabels(12);
		renderer.setYLabels(10);
		renderer.setXLabelsAlign(Align.LEFT);
		renderer.setYLabelsAlign(Align.LEFT);
		renderer.setShowGrid(true);
		renderer.setZoomButtonsVisible(true);
		renderer.setZoomRate(1.1f);
		renderer.setBarSpacing(0.5f);

		View view = ChartFactory.getBarChartView(context,
				buildBarDataset(titles, values), renderer, Type.STACKED);
		return view;
	}

}
