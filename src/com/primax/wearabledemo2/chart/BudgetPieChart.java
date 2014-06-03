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

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import com.primax.wearabledemo2.DatePickerFragment;
import com.primax.wearabledemo2.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

/**
 * Budget demo pie chart.
 */
public class BudgetPieChart extends AbstractDemoChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "Budget chart";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "The budget per project for this year (pie chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public View execute(Context context) {
//	  double[] values = new double[] { 12, 14, 11, 10, 100 };
//	  double[] values = new double[] { 20, 50, 5, 10, 20, 15, 20, 60 };
    double[] values = MainActivity.draw_pie;
//    int[] colorsx = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
    int[] colors = new int[] { Color.CYAN, Color.rgb(255, 120, 40), Color.GREEN, Color.rgb(250, 30, 30), Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.rgb(140, 60, 215) };
    DefaultRenderer renderer = buildCategoryRenderer(colors);
    
    renderer.setChartTitle("Activity Pie ( % )");
    renderer.setChartTitleTextSize(40);
    renderer.setLabelsColor(Color.DKGRAY);
    renderer.setLabelsTextSize(25);
    renderer.setLegendTextSize(30);
    int[] mar = new int[] {50, 50, 50, 50} ;
    renderer.setMargins(mar);
    
    renderer.setZoomButtonsVisible(true);
    renderer.setZoomEnabled(true);
    renderer.setDisplayValues(true);
    renderer.setShowLabels(true);
    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
//    r.setColor(Color.CYAN);
    r.setHighlighted(true);
//    r.setGradientStart(0, Color.BLUE);
//    r.setGradientStop(0, Color.GREEN);
//    Intent intent = ChartFactory.getPieChartIntent(context,
//        buildCategoryDataset("Project budget", values), renderer, "Budget");
//    return intent;
    String[] SA = new String[] {"Standing ", "Running ", "Walking ", "Biking ", "Sitting ", "Swimming ", "Driving ", "Sleeping "};
    View view = ChartFactory.getPieChartView(context, buildCategoryDataset("Project budget", values, SA), renderer);
//    return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
//        Type.STACKED);
    return view;
  }
  
  public View execute2(Context context) {
    double[] values = MainActivity.draw_pie2;
    int[] colors = new int[] { Color.CYAN, Color.rgb(255, 120, 40), Color.GREEN, Color.rgb(250, 30, 30), Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.rgb(140, 60, 215) };
    DefaultRenderer renderer = buildCategoryRenderer(colors);
    
    renderer.setChartTitle("Calories Pie ( kcal )");
    renderer.setChartTitleTextSize(40);
    renderer.setLabelsColor(Color.DKGRAY);
    renderer.setLabelsTextSize(25);
    renderer.setLegendTextSize(30);
    int[] mar = new int[] {50, 50, 50, 50} ;
    renderer.setMargins(mar);
    
    renderer.setZoomButtonsVisible(true);
    renderer.setZoomEnabled(true);
    renderer.setDisplayValues(true);
    renderer.setShowLabels(true);
    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
//    r.setColor(Color.CYAN);
    r.setHighlighted(true);
//    r.setGradientStart(0, Color.BLUE);
//    r.setGradientStop(0, Color.GREEN);
//    Intent intent = ChartFactory.getPieChartIntent(context,
//        buildCategoryDataset("Project budget", values), renderer, "Budget");
//    return intent;
    String[] SA = new String[] {"Standing ", "Running ", "Walking ", "Biking ", "Sitting ", "Swimming ", "Driving ", "Sleeping "};
    View view = ChartFactory.getPieChartView(context, buildCategoryDataset("Project budget", values, SA), renderer);
//    return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
//        Type.STACKED);
    return view;
  }

}
