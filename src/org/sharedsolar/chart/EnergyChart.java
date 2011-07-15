/**
 * Copyright (C) 2009, 2010 SC 4ViewSoft SRL
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
package org.sharedsolar.chart;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.sharedsolar.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;

public class EnergyChart extends AbstractDemoChart {

	private List<double[]> values;
	private String[] labels;
	
	public String getName() {
		return "Energy - Today";
	}

	public String getDesc() {
		return "Energy Bar Chart for Today";
	}
	
	public EnergyChart(List<double[]> values, String[] labels) {
		this.values = values;
		this.labels = labels;
	}
	
	public Intent execute(Context context) {
		int num = values.get(0).length;
		double max = 0;
		double[] value = values.get(0);
		for (int i = 0; i < value.length; i++) {
			if (value[i] > max)
				max = value[i];
		}
		
		// title
		String[] titles = new String[] {context.getString(R.string.today)};
		
		// color
		int[] colors = new int[] {Color.rgb(119, 155, 195)};
		XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
		
		// label
		renderer.setOrientation(Orientation.HORIZONTAL);
		renderer.setXLabels(0);
		renderer.setYLabels(4);
		renderer.setXLabelsAngle(-45);
		renderer.setDisplayChartValues(true);
		renderer.setTextTypeface("sans-serif", Typeface.NORMAL);
		for (int i = 0; i < num; i++)
			renderer.addTextLabel(i+1, labels[i]);		
		String chartTitleLabel = context.getString(R.string.energy) + " - "
			+ context.getString(R.string.today);
		String chartYLabel = context.getString(R.string.energy) + " ("
			+ context.getString(R.string.wh) + ")";
		renderer.setXLabelsAlign(Align.CENTER);
		
		// settings
		setChartSettings(renderer, chartTitleLabel, "", chartYLabel,
				0, num + 1, 0, max * 1.1, Color.GRAY, Color.LTGRAY);
		return ChartFactory.getBarChartIntent(context,
				buildBarDataset(titles, values), renderer, Type.DEFAULT);
	}
}
