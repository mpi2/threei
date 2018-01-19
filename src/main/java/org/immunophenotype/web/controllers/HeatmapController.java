package org.immunophenotype.web.controllers;


import org.immunophenotype.web.services.HeatmapRow;
import org.immunophenotype.web.services.HeatmapService;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.List;

@Controller
public class HeatmapController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private HeatmapService heatmapService;

    public HeatmapController(HeatmapService heatmapService) {
        Assert.notNull(heatmapService, "Heatmapservice cannot be null");
        this.heatmapService = heatmapService;
    }


    @RequestMapping("/data")
    public String dataController(Model model) {

        //need to get the data from a service so we can replace a data source behind it easily

        try {
            List<HeatmapRow> rows = heatmapService.getHeatmapRows();
            List<String> columnHeaders = heatmapService.getDistinctProcedureNames(rows);
            JSONArray columnHeadersJson=new JSONArray(columnHeaders);

            // JSON object representing the rows in the heatmap
            JSONArray heatmap = new JSONArray();

            for (HeatmapRow row : rows) {

                JSONArray heatmapRow = new JSONArray();
                heatmapRow
                        .put(row.getGene())
                        .put(row.getConstruct());

                // put the numbers in for each column header to insure column numbers same and same order
                for (String key : columnHeaders) {

                    // 4 is the constant for "No data"
                    Integer call = row.getProcedureSignificance().getOrDefault(key, 4);
                    heatmapRow.put(call);
                }
                heatmap.put(heatmapRow);
            }

            model.addAttribute("columnHeaders", columnHeaders);
            model.addAttribute("columnHeadersJson", columnHeadersJson);
            model.addAttribute("heatmap_rows", heatmap);

        } catch (SQLException e) {
            logger.warn("SQL error occurred when retrieving entries for heatmap", e);
        }

        return "data";
    }


}
