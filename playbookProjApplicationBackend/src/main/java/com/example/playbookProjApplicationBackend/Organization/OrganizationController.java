package com.example.playbookProjApplicationBackend.Organization;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/organizations")
@AllArgsConstructor
public class OrganizationController {

    private OrganizationService OS;

    @GetMapping(path = "/all")
    public String getAllOrganizations(){
        return OS.getAllOrganizations();
    }
    @GetMapping(path = "/byId/{org_id}")
    public String getOrganizationById(@PathVariable("org_id") Long org_id){
        return OS.getOrganizationById(org_id);
    }
    @GetMapping(path = "/byName/{org_name}")
    public String getOrganizationByName(@PathVariable("org_name") String name){
        return OS.getOrganizationByName(name);
    }
    @GetMapping(path = "/team/all/{org_id}")
    public String getTeamsInOrganization(@PathVariable("org_id")Long org_id){return OS.getTeamsInOrganization(org_id); }
    @GetMapping(path = "/team/byId/{org_id}/{team_id}")
    public String getTeamById(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id){return OS.getTeamById(org_id,team_id);}
    @GetMapping(path = "/team/byName/{org_id}/{team_name}")
    public String getTeamByName(@PathVariable("org_id")Long org_id,@PathVariable("team_name")String team_name){return OS.getTeamByName(org_id,team_name); }
    @PostMapping (path = "/new")
    public String uploadNewOrganization(@RequestBody Map<String,Object> orgObj){
        return OS.uploadNewOrganization(orgObj);
    }
    @PutMapping(path = "/update/{org_id}")
    public String updateOrganization(@PathVariable("org_id") Long org_id,@RequestBody Map<String,Object> orgObj){
        return OS.updateOrganization(org_id, orgObj);
    }


}
