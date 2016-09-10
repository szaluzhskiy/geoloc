package ru.ps.geoloc.coord;

import junit.framework.Assert;
import org.junit.Test;
import ru.ps.geoloc.utils.LonLatToMetersConverter;

public class TestLonToLatMetersConverter {

    @Test
    public void lonConvert() {
        int xMin = LonLatToMetersConverter.lon2xCell(0.0);
        int xMax = LonLatToMetersConverter.lon2xCell(90.0);

        //System.out.println(new Double(xMin).intValue() + " " + new Double(xMax).intValue());
        Assert.assertEquals(0, xMin);
        Assert.assertEquals(47, xMax);
    }
    @Test
    public void lanConvert() {
        int yMin = LonLatToMetersConverter.lat2yCell(0.0);
        int yMax = LonLatToMetersConverter.lat2yCell(90.0);

        Assert.assertEquals(0, yMin);
        Assert.assertEquals(1119, yMax);
    }
}
