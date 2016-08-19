
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Statstwo {

    private String reported;
    private String reportee;
    private String ranking;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The reported
     */
    public String getReported() {
        return reported;
    }

    /**
     * 
     * @param reported
     *     The reported
     */
    public void setReported(String reported) {
        this.reported = reported;
    }

    /**
     * 
     * @return
     *     The reportee
     */
    public String getReportee() {
        return reportee;
    }

    /**
     * 
     * @param reportee
     *     The reportee
     */
    public void setReportee(String reportee) {
        this.reportee = reportee;
    }

    /**
     * 
     * @return
     *     The ranking
     */
    public String getRanking() {
        return ranking;
    }

    /**
     * 
     * @param ranking
     *     The ranking
     */
    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
