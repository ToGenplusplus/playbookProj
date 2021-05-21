package com.example.playbookProjApplicationBackend.Organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends JpaRepository<Organization,Long> {
    @Query(value = "SELECT * FROM organizations o WHERE o.name = :org_name",nativeQuery = true)
    Organization getOrganizationByName(@Param("org_name")String name);
    @Query(value = "SELECT * FROM organizations o WHERE o.organization_link = :org_link",nativeQuery = true)
    Organization getOrganizationByOrgLink(@Param("org_link")String link);
}
