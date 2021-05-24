package com.example.playbookProjApplicationBackend.Organization;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String updateOrganization(Long org_id, Map<String,Object> orgObj){
        if(!OR.findById(org_id).isPresent()){
            return new ResponseError("organization with id " + org_id + " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        Organization organization = OR.getOne(org_id);
        String name;
        String link;
        if ( orgObj.containsKey("name")){
            if(OR.getOrganizationByName((String) orgObj.get("name")) != null && !((String) orgObj.get("name")).equals(organization.getName())){
                return new ResponseError("organization with name " + orgObj.get("name")+ " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            name = (String) orgObj.get("name");
        }else{
            name = organization.getName();
        }
        if ( orgObj.containsKey("link")){
            if(OR.getOrganizationByName((String) orgObj.get("link")) != null && !((String) orgObj.get("link")).equals(organization.getOrganizationLink())){
                return new ResponseError("organization with link " + orgObj.get("link")+ " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            link = (String) orgObj.get("link");
        }else{
            link = organization.getOrganizationLink();
        }
        try{
           organization.setCountry(orgObj.containsKey("country") ? (String) orgObj.get("country") : organization.getCountry());
           organization.setState(orgObj.containsKey("state") ? (String) orgObj.get("state") : organization.getState());
           organization.setOrganizationType(orgObj.containsKey("org_type")?  (String) orgObj.get("org_type") : organization.getOrganizationType());
           organization.setLogoImageLocation(orgObj.containsKey("img") ? (String) orgObj.get("img") : organization.getLogoImageLocation());
           organization.setName(name);
           organization.setOrganizationLink(link);
           return new ResponseError("Success",HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
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
