package com.example.playbookProjApplicationBackend.Team;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class TeamRepositoryTest {
    @Autowired
    private TeamRepository underTest;

    @AfterEach
    void tearDown(){underTest.deleteAll();}

    @Test
    void CanAddNewTeam(){
    }

}