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
    @PostMapping (path = "/new")
    public String uploadNewOrganization(@RequestBody Map<String,Object> orgObj){
        return OS.uploadNewOrganization(orgObj);
    }


}
