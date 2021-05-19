package com.example.playbookProjApplicationBackend.Organization;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/organization")
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


}
