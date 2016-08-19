
package com.combustiongroup.burntout.network.dto.response;

import com.combustiongroup.burntout.network.dto.ResultUserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UserProfileResponse {

    private List<ResultUserProfile> resultUserProfiles = new ArrayList<ResultUserProfile>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The resultUserProfiles
     */
    public List<ResultUserProfile> getResultUserProfiles() {
        return resultUserProfiles;
    }

    /**
     * 
     * @param resultUserProfiles
     *     The resultUserProfiles
     */
    public void setResultUserProfiles(List<ResultUserProfile> resultUserProfiles) {
        this.resultUserProfiles = resultUserProfiles;
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
                "resultUserProfiles=" + resultUserProfiles +
                '}';
    }
}
