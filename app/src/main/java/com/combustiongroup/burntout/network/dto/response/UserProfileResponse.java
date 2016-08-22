
package com.combustiongroup.burntout.network.dto.response;

import com.combustiongroup.burntout.network.dto.ResultsUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UserProfileResponse {

    private List<ResultsUserProfile> results = new ArrayList<ResultsUserProfile>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The resultses
     */
    public List<ResultsUserProfile> getResults() {
        return results;
    }

    /**
     * 
     * @param resultses
     *     The resultses
     */
    public void setResults(List<ResultsUserProfile> results) {
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return "UserProfileResponse{" +
                "resultses=" + results +
                '}';
    }
}
