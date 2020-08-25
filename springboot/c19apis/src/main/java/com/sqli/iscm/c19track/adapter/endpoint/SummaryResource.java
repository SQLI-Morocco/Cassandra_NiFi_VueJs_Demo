package com.sqli.iscm.c19track.adapter.endpoint;

import com.sqli.iscm.c19track.domain.core.Region;
import com.sqli.iscm.c19track.domain.core.Summary;
import com.sqli.iscm.c19track.domain.service.SummaryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/data/summary")
public class SummaryResource {
    @Inject
    private SummaryService summaryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Summary getGlobalSummary() {
        return summaryService.getLatestGlobalSummary();
    }

    @GET
    @Path("/regions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Region> getSummaryByRegion() {
        return summaryService.getLatestSummaryByRegion();
    }
}
