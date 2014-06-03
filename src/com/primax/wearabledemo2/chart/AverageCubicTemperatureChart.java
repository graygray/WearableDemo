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
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.primax.wearabledemo2.MainActivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.View;

/**
 * Average temperature demo chart.
 */
public class AverageCubicTemperatureChart extends AbstractDemoChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
	  Log.e("gray", "AverageCubicTemperatureChart.java:getName, " + "");
    return "Average temperature";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
	  Log.e("gray", "AverageCubicTemperatureChart.java:getDesc, " + "");
    return "The average temperature in 4 Greek islands (cubic line chart)";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */
  public View execute(Context context) {
	  Log.e("gray", "AverageCubicTemperatureChart.java:execute, " + "");
    String[] titles = new String[] { "Calories ( kcal )"};
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
//    	x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
      x.add(MainActivity.draw_x);
    }
    List<double[]> values = new ArrayList<double[]>();
//    values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2, 13.9 });
//    values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });
//    values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });
//    values.add(MainActivity.draw_y_walking);
//    values.add(MainActivity.draw_y_running);
    values.add(MainActivity.draw_y_calories);
//    values.add(MainActivity.draw_y4);
    int[] colors = new int[] { Color.RED };
    PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "Calories Burned", "Time ( every min )", "Kcal ", 0, MainActivity.MAX_ARRAY_SIZE, 0, MainActivity.draw_yMax_calories,
        Color.WHITE, Color.GRAY);
    
    renderer.setChartTitleTextSize(50);
    renderer.setAxisTitleTextSize(40);
    renderer.setLabelsTextSize(30);
    renderer.setLegendTextSize(40);
    renderer.setPointSize(5);
    int[] mar = new int[] {80, 80, 150, 30} ;
    renderer.setMargins(mar);
    
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
//    renderer.setPanLimits(new double[] { -10, 200, -10, 500 });
//    renderer.setZoomLimits(new double[] { -10, 200, -10, 500 });
    View view = ChartFactory.getCubeLineChartView(context, buildDataset(titles, x, values), 
    		renderer, 0.33f);
//    Intent intent = ChartFactory.getCubicLineChartIntent(context, buildDataset(titles, x, values),
//        renderer, 0.33f, "Average temperature");
    return view;
  }

  public View execute2(Context context) {
    String[] titles = new String[] { "Calories ( kcal )"};
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < MainActivity.MAX_DAY_TO_DRAW; i++) {
    	x.add(new double[] { 1, 2, 3, 4, 5, 6, 7 });
//    	x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });
//      x.add(MainActivity.draw_x);
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(MainActivity.draw_y_calories_perday);
    
    int[] colors = new int[] { Color.RED };
    PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "Calories Burned", "Time ( every min )", "Kcal ", 0, MainActivity.MAX_DAY_TO_DRAW, 0, MainActivity.draw_yMax_calories,
        Color.WHITE, Color.GRAY);
    
    for (int i = 0; i <= MainActivity.MAX_DAY_TO_DRAW; i++) {
    	renderer.addTextLabel(i+1, MainActivity.draw_x_dateArray[i]);
	}
    renderer.setXLabels(0);
    
    renderer.setChartTitleTextSize(50);
    renderer.setAxisTitleTextSize(40);
    renderer.setLabelsTextSize(30);
    renderer.setLegendTextSize(40);
    renderer.setPointSize(5);
    int[] mar = new int[] {80, 80, 150, 30} ;
    renderer.setMargins(mar);
    
//    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
//    renderer.setPanLimits(new double[] { -10, 200, -10, 500 });
//    renderer.setZoomLimits(new double[] { -10, 200, -10, 500 });
    View view = ChartFactory.getCubeLineChartView(context, buildDataset(titles, x, values), 
    		renderer, 0.33f);
//    Intent intent = ChartFactory.getCubicLineChartIntent(context, buildDataset(titles, x, values),
//        renderer, 0.33f, "Average temperature");
    return view;
  }
}
