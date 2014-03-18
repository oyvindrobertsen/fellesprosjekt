package no.ntnu.apotychia.group;

import no.ntnu.apotychia.Application;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.GroupService;
import no.ntnu.apotychia.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class GroupServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;
    private User testUser;

    @Before
    public void before() {
        testUser = new User("testUser");
        testUser.setPasswordAndEncode("testPassword");
        testUser.setFirstName("TestFirstName");
        testUser.setLastName("TestLastName");
        testUser.setEmail("TestEmail@test.com");
        userService.addNewUser(testUser);
    }

    @Test
    public void thatGroupsCanBeCreated() {
        Group testGroup = new Group();
        testGroup.setName("TestGroup");
        Long groupId = groupService.addNewGroup(testGroup);
        assertNotNull(groupId);
    }

    @Test
    public void thatUsersCanBeAddedToGroups() {
        Group testGroup = new Group();
        testGroup.setName("TestGroup");
        Long groupId = groupService.addNewGroup(testGroup);
        groupService.addUserToGroup(testUser, groupId);
        assertEquals(1, groupService.getGroupMembersByGroupId(groupId).size());
    }

}
