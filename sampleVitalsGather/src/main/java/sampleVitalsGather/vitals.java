package sampleVitalsGather;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import vitalsgatherer.VitalsGather;

public class vitals {

	public static Map<VitalsGather.resultOptions, String> results = new HashMap<VitalsGather.resultOptions, String>();

	public static void main(String[] args) throws XPathExpressionException, IOException, URISyntaxException,
			ParserConfigurationException, SAXException {
		VitalsGather vg = new VitalsGather();
		String fileLocation = "d:\\data\\";
		String fileName = "dataz";
		Date dt = new Date("09/15/2016 18:14:15");
		results = vg.getVitals("demo.perfectomobile.com", "mitchellw@perfectomobile.com", "Spike123!", "", dt, "",
				VitalsGather.availableTimeTypes.started, fileName, fileLocation, true, true);

		/*
		 * results = vg.getVitals("demo.perfectomobile.com",
		 * "evat@perfectomobile.com", "PaSS4MyDemo!", "", dt , "",
		 * VitalsGather.availableTimeTypes.started, fileName, fileLocation,
		 * true);
		 */

		String url = "http://localhost:8083/WebViewerExample/run?__report=vitals.rptdesign&format=html&__title=Performance+Test+Vitals+Gather+"
				+ URLEncoder.encode(dt.toString(), "UTF-8");

		File vfandroid = null;
		File vfios = null;
		boolean foundSuccess = false;
		if (results.get(VitalsGather.resultOptions.statusAndroid).equals("success")) {
			System.out.println(results.get(VitalsGather.resultOptions.jsonAndroid));
			vfandroid = new File(results.get(VitalsGather.resultOptions.fullPathCsvAndroid));
			url = url + "&AndroidCSVFile=" + URLEncoder.encode(vfandroid.getAbsolutePath(), "UTF-8");
			foundSuccess = true;
		}
		if (results.get(VitalsGather.resultOptions.statusIos).equals("success")) {
			System.out.println(results.get(VitalsGather.resultOptions.jsonIos));
			vfios = new File(results.get(VitalsGather.resultOptions.fullPathCsvIos));
			url = url + "&iOSCSVFile=" + URLEncoder.encode(vfios.getAbsolutePath(), "UTF-8");
			foundSuccess = true;
		}

		if (results.get(VitalsGather.resultOptions.statusTransactionAndroid).equals("success")) {
			System.out.println(results.get(VitalsGather.resultOptions.jsonTransactionAndroid));
			vfandroid = new File(results.get(VitalsGather.resultOptions.fullPathTransactionCsvAndroid));
			url = url + "&AndroidTransactionCSVFile=" + URLEncoder.encode(vfandroid.getAbsolutePath(), "UTF-8");
			foundSuccess = true;
		}
		if (results.get(VitalsGather.resultOptions.statusTransactionIos).equals("success")) {
			System.out.println(results.get(VitalsGather.resultOptions.jsonTransactionIos));
			vfios = new File(results.get(VitalsGather.resultOptions.fullPathTransactionCsvIos));
			url = url + "&iOSTransactionCSVFile=" + URLEncoder.encode(vfios.getAbsolutePath(), "UTF-8");
			foundSuccess = true;
		}

		if (foundSuccess) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Desktop.getDesktop().browse(new URI(url));
		}
	}
}
