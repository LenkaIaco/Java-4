package sk.itlearning.wf.main;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import sk.itlearning.wf.rest.client.WeatherDataRestClient;
import sk.itlearning.wf.xml.LocationType;
import sk.itlearning.wf.xml.Temperature;
import sk.itlearning.wf.xml.TimeType;
import sk.itlearning.wf.xml.Weatherdata;

public class Main {

	/**
	 * Creates a basic GUI window with a date-time/forecast-temperature chart
	 */
	public static void main(String[] args) {
		JFrame gui = new JFrame("Temperature Forecast");
		gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gui.setBounds(50, 50, 600, 500);

		XYDataset dataset = createDataSet();
		
	    JFreeChart chart = ChartFactory.createTimeSeriesChart(
	            "Temperature Forecast",
	            "Datetime",
	            "Temperature",
	            dataset);
	    
		ChartPanel chartPanel = new ChartPanel(chart);

		gui.setContentPane(chartPanel);
		gui.setVisible(true);
	}

	/**
	 * Get processed data from API and put it to XYDataset
	 */
	private static XYDataset createDataSet() {
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		TimeSeries series = new TimeSeries("Datetime");
		
		getTemperatureData().forEach((k, v) -> {
			Hour hour = new Hour(Date.from(k.toInstant(ZoneOffset.UTC)));
			series.add(hour, v);
		});
		
		dataset.addSeries(series);

		return dataset;
	}

	/**
	 * 
	 * Get raw data from API and process it to get desired weather details
	 */
	private static Map<LocalDateTime, BigDecimal> getTemperatureData() {

		// initialize data map for processed data
		Map<LocalDateTime, BigDecimal> timeToTemperatureMap = new HashMap<>();

		Weatherdata data = WeatherDataRestClient.getByLatLon("47.08", "17.18");

		// product.get(0) - get only first product; only one product is available
		List<TimeType> forecastForTimeList = data.getProduct().get(0).getTime();

		// sort forecast data by timeFrom
		Collections.sort(forecastForTimeList, (a, b) -> a.getFrom().compare(b.getFrom()));

		/*
		 * Filter forecast data - let those where timeFrom == timeTo
		 * 
		 * Data where timeFrom != timeTo are meant for precipitation volume during time
		 * period
		 * 
		 */
		forecastForTimeList = forecastForTimeList.stream().filter(t -> t.getFrom().equals(t.getTo()))
				.collect(Collectors.toList());

		for (TimeType tt : forecastForTimeList) {
			// only one location per TimeType is always available
			LocationType locationForecast = tt.getLocation().get(0);

			List<JAXBElement<?>> locationData = locationForecast.getGroundCoverAndPressureAndMaximumPrecipitation();

			locationData.forEach(ld -> {
				if (ld.getName().getLocalPart().equals("temperature")) {
					Temperature temperature = (Temperature) ld.getValue();
					timeToTemperatureMap.put(toLocalDateTime(tt.getFrom()), temperature.getValue());
				}
			});
		}

		return timeToTemperatureMap;
	}

	/**
	 * Convert XML date time to LocalDateTime instance  
	 */
	private static LocalDateTime toLocalDateTime(XMLGregorianCalendar xmlDate) {
		Date date = xmlDate.toGregorianCalendar().getTime();
		return LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
	}
	
}
