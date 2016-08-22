
package com.combustiongroup.burntout.network.dto;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Stats {

    private String reported;
    private String reportee;
    private String ranking;
    private String my_rank;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The notifier_reported_count
     */
    public String getReported() {
        return reported;
    }

    /**
     * 
     * @param reported
     *     The notifier_reported_count
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
     *     The theranking
     */
    public String getRanking() {
        return ranking;
    }

    /**
     * 
     * @param ranking
     *     The theranking
     */
    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    /**
     * 
     * @return
     *     The my_rank
     */
    public String getMy_rank() {
        return my_rank;
    }

    /**
     * 
     * @param my_rank
     *     The my_rank
     */
    public void setMy_rank(String my_rank) {
        this.my_rank = my_rank;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
