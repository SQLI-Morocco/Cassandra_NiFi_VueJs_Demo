package com.sqli.iscm.c19track.adapter.endpoint;

import com.sqli.iscm.c19track.domain.core.Active;
import com.sqli.iscm.c19track.domain.core.Confirmed;
import com.sqli.iscm.c19track.domain.core.DataPoint;
import com.sqli.iscm.c19track.domain.core.Deaths;
import com.sqli.iscm.c19track.domain.core.Recovered;
import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.core.Summary;
import com.sqli.iscm.c19track.domain.service.DataService;
import com.sqli.iscm.c19track.domain.service.SummaryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.sqli.iscm.c19track.app.utils.DateUtil.DATE_FORMAT_GMT;

@Path("/data/points")
public class DataPointResource {

    @Inject
    private DataService dataService;

    @Inject
    private SummaryService summaryService;

    @GET
    @Path("/{region}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataPoint> getDataPointsByRegion(@PathParam("region") final String encodedRegion) throws UnsupportedEncodingException {
        String region = URLDecoder.decode(encodedRegion, "UTF-8");
        final Iterable<DataPoint> dataPoints = dataService.getAllDataPointsByRegion(Region.builder().name(region).build());
        final List<DataPoint> sList = new ArrayList<DataPoint>();
        dataPoints.forEach(sList::add);
        updateWithHourlyData(region, sList);

        return sList;
    }

    @GET
    @Path("/{region}/deaths")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataPoint> getDeathsDataPointsByRegion(@PathParam("region") final String region) {
        final Iterable<DataPoint> dataPoints = dataService.findAllDeathsByRegion(Region.builder().name(region).build());
        final List<DataPoint> sList = new ArrayList<DataPoint>();
        dataPoints.forEach(sList::add);
        return sList;
    }

    @GET
    @Path("/{region}/{combinedKey}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DataPoint> getDataPointsByRegion(@PathParam("region") final String region, @PathParam("combinedKey") final String combinedKey) {
        final Iterable<DataPoint> dataPoints = dataService.getAllDataPointsByRegion(Region.builder().name(region).build());
        final List<DataPoint> sList = new ArrayList<DataPoint>();
        dataPoints.forEach(sList::add);
        return sList;
    }

    private void updateWithHourlyData(String region, List<DataPoint> sList) {
        Summary latestRegionSummary = summaryService.getLatestRegionSummary(region).getSummary();
        DataPoint latestRegionDataPoint = DataPoint.builder()
                .confirmed(Confirmed.builder().value(latestRegionSummary.getConfirmed().getValue()).build())
                .active(Active.builder().value(latestRegionSummary.getActive().getValue()).build())
                .deaths(Deaths.builder().value(latestRegionSummary.getDeaths().getValue()).build())
                .recovered(Recovered.builder().value(latestRegionSummary.getRecovered().getValue()).build())
                .dateRecorded(DATE_FORMAT_GMT.format(latestRegionSummary.getLastUpdate())).build();

        DataPoint firstDataPoint = sList.get(0);
        if (firstDataPoint.getDateRecorded().equalsIgnoreCase(latestRegionDataPoint.getDateRecorded())) {
            firstDataPoint.setActive(latestRegionDataPoint.getActive());
            firstDataPoint.setConfirmed(latestRegionDataPoint.getConfirmed());
            firstDataPoint.setDeaths(latestRegionDataPoint.getDeaths());
            firstDataPoint.setRecovered(latestRegionDataPoint.getRecovered());
        } else {
            sList.add(latestRegionDataPoint);
        }
    }
}
