
package com.combustiongroup.burntout.network.dto.response;

import com.combustiongroup.burntout.network.dto.ResultUserPicture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class UploadPictureResponse {

    private List<ResultUserPicture> resultUserPictures = new ArrayList<ResultUserPicture>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The resultUserPictures
     */
    public List<ResultUserPicture> getResultUserPictures() {
        return resultUserPictures;
    }

    /**
     * 
     * @param resultUserPictures
     *     The resultUserPictures
     */
    public void setResultUserPictures(List<ResultUserPicture> resultUserPictures) {
        this.resultUserPictures = resultUserPictures;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
