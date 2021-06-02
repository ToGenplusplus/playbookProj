package com.example.playbookProjApplicationBackend.Organization;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


@Service
public class OrganizationService {

    private OrganizationRepository OR;
    private ResponseError responseError;

    @Autowired
    public OrganizationService(OrganizationRepository OR, ResponseError responseError) {
        this.OR = OR;
        this.responseError = responseError;
    }
//------------------------------------Organization-----------------------
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

        if(!orgObj.containsKey("country") || !orgObj.containsKey("img") || !orgObj.containsKey("name")
                ||!orgObj.containsKey("link") || !orgObj.containsKey("type") || !orgObj.containsKey("state")){
            return sendResponse("invalid request, missing fields", HttpStatus.BAD_REQUEST.value());
        }
        String name = (String) orgObj.get("name");
        String link = (String) orgObj.get("link");
        String country = (String) orgObj.get("country");
        String img = (String) orgObj.get("img");
        String type = (String) orgObj.get("type");
        String state = (String) orgObj.get("state");
        if(OR.getOrganizationByName(name) != null || OR.getOrganizationByOrgLink(link) != null){
            return sendResponse("Organization already exists", HttpStatus.OK.value());
        }
        try{
            Organization organization = new Organization(name,country,state,type,img,link);
            OR.save(organization);
            return sendResponse("Success",HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public String updateOrganization(Long org_id, Map<String,Object> orgObj){
        if(!OR.findById(org_id).isPresent()){
            return sendResponse("organization with id " + org_id + " does not exist", HttpStatus.BAD_REQUEST.value());
        }
        Organization organization = OR.getOne(org_id);
        String name;
        String link;
        if ( orgObj.containsKey("name")){
            String objname = (String) orgObj.get("name");
            if(OR.getOrganizationByName(objname) != null && !(objname).equals(organization.getName())){
                return sendResponse("organization with name " + orgObj.get("name")+ " already exists", HttpStatus.BAD_REQUEST.value());
            }
            name = objname;
        }else{
            name = organization.getName();
        }
        if ( orgObj.containsKey("link")){
            String objlink = (String) orgObj.get("link");
            if(OR.getOrganizationByOrgLink(objlink) != null && !(objlink).equals(organization.getOrganizationLink())){
                return sendResponse("organization with link " + orgObj.get("link")+ " already exists", HttpStatus.BAD_REQUEST.value());
            }
            link = objlink;
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
            return sendResponse("Success",HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    //--------------------------------------Teams------------------------
    public String getTeamsInOrganization(Long org_id){
        try{
            Organization organization = OR.getOne(org_id);
            return sendResponse(jsonifyTeams(organization.getTeams()),HttpStatus.OK.value());

        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    public String getTeamById(Long org_id, Long team_id){
        try{
            Organization organization = OR.getOne(org_id);
            for (Team team : organization.getTeams()){
                if(team.getId() == team_id){
                    return sendResponse(team.toJSONObj(),HttpStatus.OK.value());
                }
            }
            return sendResponse("team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    public String getTeamByName(Long org_id, String team_name){
        try{
            Organization organization = OR.getOne(org_id);
            for (Team team : organization.getTeams()){
                if(team.getName().equals(team_name)){
                    return sendResponse(team.toJSONObj(),HttpStatus.OK.value());
                }
            }
            return sendResponse("team with name " + team_name + " does not exist",HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public String addNewTeam(Map<String,Object> newTeam){
        if(!newTeam.containsKey("org_id") || !newTeam.containsKey("name")){
            return sendResponse("invalid request, missing fields", HttpStatus.BAD_REQUEST.value());
        }
        try{
            Organization organization = OR.getOne((Long) newTeam.get("org_id"));
            Set<Team> orgTeams = organization.getTeams();
            for(Team team: orgTeams){
                if(team.getName().equals((String) newTeam.get("name"))){
                    return sendResponse("Team with name " + team.getName() +  " already exixts for this organiation",HttpStatus.BAD_REQUEST.value());
                }
            }
            Team team = new Team((String) newTeam.get("name"),organization);
            orgTeams.add(team);
            organization.setTeams(orgTeams);
            return sendResponse(team.toJSONObj(),HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String callMethod(Object param, String methodToCall){
        String resp;
        switch (methodToCall){
            case "getAllOrganizations":
                resp = sendResponse(jsonify(OR.findAll()), HttpStatus.OK.value());
                break;
            case "getOrganizationById":
                boolean exists = OR.findById((Long) param).isPresent();
                resp = (!exists) ? sendResponse("Organization with id: " + String.valueOf(param) + " does not exist",404):
                        sendResponse(OR.getOne((Long) param).toJSONObj(),HttpStatus.OK.value());
                break;
            case "getOrganizationByName":
                Organization organization = OR.getOrganizationByName((String) param);
                resp = (organization == null) ? sendResponse("Organization with name: " + String.valueOf(param) + " does not exist",404):
                        sendResponse(organization.toJSONObj(),HttpStatus.OK.value());
                break;
            default:
                resp = sendResponse("invalid request",HttpStatus.BAD_REQUEST.value());
        }
        return resp;
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

    private JSONObject jsonifyTeams(Collection<Team> teams){
        JSONObject teamObj  = new JSONObject();
        JSONArray teamArray = new JSONArray();
        for (Team team: teams){
            teamArray.add(team.toJSONObj());
        }

        teamObj.put("teams",teamArray);
        return teamObj;
    }

    private String sendResponse(Object Message, int errorCode){
        responseError.setMessage(Message);
        responseError.setErrorCode(errorCode);
        return responseError.toJson();
    }
}
