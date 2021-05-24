package com.example.playbookProjApplicationBackend.Organization;

import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="organizations", uniqueConstraints = {
        @UniqueConstraint(name ="unique_organization_name", columnNames = {"name"}),
        @UniqueConstraint(name ="unique_organization_link", columnNames = {"organization_link"})
})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "state", nullable = false)
    private String state;
    @Column(name = "organization_type", nullable = false)
    private String organizationType;
    @Column(name = "logo_image_location", nullable = false)
    private String logoImageLocation;
    @Column(name = "organization_link", nullable = false)
    private String organizationLink;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Team> teams;

    protected Organization() {
    }

    public Organization(String name, String country,
                        String state, String organizationType,
                        String logoImageLocation, String organizationLink, Set<Team> teams) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.organizationType = organizationType;
        this.logoImageLocation = logoImageLocation;
        this.organizationLink = organizationLink;
        this.teams = teams;
    }

    public Organization(String name, String country, String state, String organizationType, String logoImageLocation, String organizationLink) {
        this.name = name;
        this.country = country;
        this.state = state;
        this.organizationType = organizationType;
        this.logoImageLocation = logoImageLocation;
        this.organizationLink = organizationLink;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", organizationType='" + organizationType + '\'' +
                ", logoImageLocation='" + logoImageLocation + '\'' +
                ", organizationLink='" + organizationLink + '\'' +
                ", teams=" + teams +
                '}';
    }

    public JSONObject toJSONObj(){
        JSONObject orgObj = new JSONObject();
        orgObj.put("organization_id",id);
        orgObj.put("organization_name",name);
        orgObj.put("country",country);
        orgObj.put("state_province",state);
        orgObj.put("organization_type",organizationType);
        orgObj.put("image_location",logoImageLocation);
        orgObj.put("organization_link",organizationLink);
        return orgObj;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getLogoImageLocation() {
        return logoImageLocation;
    }

    public void setLogoImageLocation(String logoImageLocation) {
        this.logoImageLocation = logoImageLocation;
    }

    public String getOrganizationLink() {
        return organizationLink;
    }

    public void setOrganizationLink(String organizationLink) {
        this.organizationLink = organizationLink;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
