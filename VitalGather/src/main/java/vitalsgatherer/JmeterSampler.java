package vitalsgatherer;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JmeterSampler extends AbstractJavaSamplerClient implements Serializable {
		private static final long serialVersionUID = 1L;

		// set up default arguments for the JMeter GUI
		@Override
		public Arguments getDefaultParameters() {
			Arguments defaultParameters = new Arguments();
			defaultParameters.addArgument("fileLocation", "..\\");
			defaultParameters.addArgument("fileName", "dataz");
			defaultParameters.addArgument("startDate", new Date("09/14/2016 18:14:15").toString());
			defaultParameters.addArgument("perfectoHost","demo.perfectomobile.com");
			defaultParameters.addArgument("perfectoUser", "DEFAULT_USER@perfectomobile.com");
			defaultParameters.addArgument("perfectoPassword", "DEFAULT_PASS");
			return defaultParameters;
		}

		public SampleResult runTest(JavaSamplerContext context) {
			// pull parameters
			String fileLocation = context.getParameter( "fileLocation" );
			String fileName = context.getParameter( "fileName" );
			Date startDate = new Date(context.getParameter( "startDate" ));
			String host = context.getParameter("perfectoHost");
			String user = context.getParameter("perfectoUser");
			String password = context.getParameter("perfectoPassword");

			SampleResult result = new SampleResult();
			result.sampleStart(); // start stopwatch

			try {
				VitalsGather vg = new VitalsGather();
				try {
					results = vg.getVitals(host, user, password, "", startDate, "",
                            VitalsGather.availableTimeTypes.started, fileName, fileLocation, true, true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String url = "http://localhost:8083/Birt/run?__report=vitals.rptdesign&format=html&__title=Performance+Test+Vitals+Gather+"
						+ URLEncoder.encode(startDate.toString(), "UTF-8");

				File vfandroid = null;
				File vfios = null;
				if (results.get(VitalsGather.resultOptions.statusAndroid)
						.equals("success")) {
					System.out.println(results.get(VitalsGather.resultOptions.jsonAndroid));
					vfandroid = new File(results.get(VitalsGather.resultOptions.fullPathCsvAndroid));
					url = url + "&AndroidCSVFile=" + URLEncoder.encode(vfandroid.getAbsolutePath(), "UTF-8");
				}
				if (results.get(VitalsGather.resultOptions.statusIos)
						.equals("success")) {
					System.out.println(results.get(VitalsGather.resultOptions.jsonIos));
					vfios = new File(results.get(VitalsGather.resultOptions.fullPathCsvIos));
					url = url + "&iOSCSVFile=" + URLEncoder.encode(vfios.getAbsolutePath(), "UTF-8");
				}

				if (results.get(VitalsGather.resultOptions.statusTransactionAndroid)
						.equals("success")) {
					System.out.println(results.get(VitalsGather.resultOptions.jsonTransactionAndroid));
					vfandroid = new File(results.get(VitalsGather.resultOptions.fullPathTransactionCsvAndroid));
					url = url + "&AndroidTransactionCSVFile=" + URLEncoder.encode(vfandroid.getAbsolutePath(), "UTF-8");
				}
				if (results.get(VitalsGather.resultOptions.statusTransactionIos)
						.equals("success")) {
					System.out.println(results.get(VitalsGather.resultOptions.jsonTransactionIos));
					vfios = new File(results.get(VitalsGather.resultOptions.fullPathTransactionCsvIos));
					url = url + "&iOSTransactionCSVFile=" + URLEncoder.encode(vfios.getAbsolutePath(), "UTF-8");
				}

				Desktop.getDesktop().browse(new URI(url));

				result.sampleEnd(); // stop stopwatch
				result.setSuccessful( true );
				result.setResponseMessage( "Successfully performed action" );
				result.setResponseCodeOK(); // 200 code
			} catch (Exception e) {
				result.sampleEnd(); // stop stopwatch
				result.setSuccessful( false );
				result.setResponseMessage( "Exception: " + e );
				System.err.println(e.getStackTrace());
				// get stack trace as a String to return as document data
				java.io.StringWriter stringWriter = new java.io.StringWriter();
				e.printStackTrace( new java.io.PrintWriter( stringWriter ) );
				result.setResponseData( stringWriter.toString() );
				result.setDataType( org.apache.jmeter.samplers.SampleResult.TEXT );
				result.setResponseCode( "500" );
			}

			return result;
		}
	public static Map<VitalsGather.resultOptions, String> results = new HashMap<VitalsGather.resultOptions, String>();

}
