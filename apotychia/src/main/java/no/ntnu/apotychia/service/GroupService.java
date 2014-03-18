package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Service
public class GroupService {

    @Autowired
    GroupRepository groupRepository;

    public Group getGroupById(Long groupId) {
        return groupRepository.findGroup(groupId);
    }

    public Long addNewGroup(Group group) {
        try {
            Long groupId = groupRepository.insert(group);
            return groupId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUserToGroup(User user, Long groupId) {
        Set<User> members = groupRepository.findMembers(groupId);
        if (!members.contains(user)) {
            groupRepository.addUser(user, groupId);
        }
    }

    public Set<User> getGroupMembersByGroupId(Long groupId) {
        return groupRepository.findMembers(groupId);
    }

    public List<Group> getAllGroups() {
        List<Group> groups = groupRepository.findAllGroups();
        for (Group g : groups) {
            g.addAllMembers(getGroupMembersByGroupId(g.getId()));
        }
        return groups;
    }
}
