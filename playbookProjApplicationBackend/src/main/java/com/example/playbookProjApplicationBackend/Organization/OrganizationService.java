package com.example.playbookProjApplicationBackend.Organization;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class OrganizationService {

    private OrganizationRepository OR;

    @Autowired
    public OrganizationService(OrganizationRepository OR) {
        this.OR = OR;
    }

    public String getAllOrganizations(){
        return callMethod(null,"getAllOrganizations");
    }

    public String getOrganizationById(Long org_id){
        return callMethod(org_id,"getOrganizationById");
    }

    public String getOrganizationByName(String name){
        return callMethod(name,"getOrganizationByName");
    }

    private String callMethod(Object param, String methodToCall){
        ResponseError resp;
        switch (methodToCall){
            case "getAllOrganizations":
                resp = new ResponseError(jsonify(OR.findAll()),200);
                break;
            case "getOrganizationById":
                boolean exists = OR.findById((Long) param).isPresent();
                resp = (!exists) ? new ResponseError("Organization with id: " + String.valueOf(param) + " does not exist",404):
                        new ResponseError(OR.getOne((Long) param).toJSONObj(),200);
                break;
            case "getOrganizationByName":
                Organization organization = OR.getOrganizationByName((String) param);
                resp = (organization == null) ? new ResponseError("Organization with name: " + String.valueOf(param) + " does not exist",404):
                        new ResponseError(organization.toJSONObj(),200);
                break;
            default:
                resp = new ResponseError("invalid request",404);
        }
        return resp.toJson();
    }

    private JSONObject jsonify(Collection<Organization> organizations){
        JSONObject orgObj  = new JSONObject();
        JSONArray orgsArray = new JSONArray();
        for (Organization organization: organizations){
            orgsArray.add(organization.toJSONObj());
        }

        orgObj.put("organizations",orgsArray);
        return orgObj;
    }
}
