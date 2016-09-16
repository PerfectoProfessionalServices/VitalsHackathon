package vitalsgatherer;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.junit.Test;

import java.util.Date;

/**
 * Created by mitchellw on 9/16/2016.
 */
public class TestJunitSampler {
    @Test
    public void testJmeterVitalsGatherer() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument("fileLocation", "..\\");
        defaultParameters.addArgument("fileName", "dataz");
        defaultParameters.addArgument("startDate", new Date("09/15/2016 12:14:15").toString());
        defaultParameters.addArgument("perfectoHost","demo.perfectomobile.com");
        defaultParameters.addArgument("perfectoUser", "<YOUR_USER>@perfectomobile.com");
        defaultParameters.addArgument("perfectoPassword", "<YOUR_PASSWORD");
        new JmeterSampler().runTest(new JavaSamplerContext(defaultParameters));
    }
}
