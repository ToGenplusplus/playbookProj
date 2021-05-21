package com.example.playbookProjApplicationBackend.Organization;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


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
    public String uploadNewOrganization(Map<String,Object> orgObj){
        ResponseError resp = null;
        if(!orgObj.containsKey("country") || !orgObj.containsKey("img") || !orgObj.containsKey("name")
                ||!orgObj.containsKey("link") || !orgObj.containsKey("type") || !orgObj.containsKey("state")){
            return new ResponseError("invalid request, missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        String name = (String) orgObj.get("name");
        String link = (String) orgObj.get("link");
        String country = (String) orgObj.get("country");
        String img = (String) orgObj.get("img");
        String type = (String) orgObj.get("type");
        String state = (String) orgObj.get("state");
        if(OR.getOrganizationByName(name) != null || OR.getOrganizationByOrgLink(link) != null){
            return new ResponseError("Organization already exists", HttpStatus.OK.value()).toJson();
        }
        try{
            Organization organization = new Organization(name,country,state,type,img,link);
            OR.save(organization);
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }

    private String callMethod(Object param, String methodToCall){
        ResponseError resp;
        switch (methodToCall){
            case "getAllOrganizations":
                resp = new ResponseError(jsonify(OR.findAll()), HttpStatus.OK.value());
                break;
            case "getOrganizationById":
                boolean exists = OR.findById((Long) param).isPresent();
                resp = (!exists) ? new ResponseError("Organization with id: " + String.valueOf(param) + " does not exist",404):
                        new ResponseError(OR.getOne((Long) param).toJSONObj(),HttpStatus.OK.value());
                break;
            case "getOrganizationByName":
                Organization organization = OR.getOrganizationByName((String) param);
                resp = (organization == null) ? new ResponseError("Organization with name: " + String.valueOf(param) + " does not exist",404):
                        new ResponseError(organization.toJSONObj(),HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("invalid request",HttpStatus.BAD_REQUEST.value());
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
