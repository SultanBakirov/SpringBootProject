package com.example.services;

import com.example.models.Group;

import java.io.IOException;
import java.util.List;

public interface GroupService {

    List<Group> getAllGroups(Long companyId);

    void addGroup(Long id, Group group);

    Group getGroupById(Long id);

    void updateGroup(Group group, Long id);

    void deleteGroupById(Long id);

    void assignGroup(Long courseId, Long groupId) throws IOException;

}
