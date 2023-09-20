package ua.com.foxminded.repository.interfaces;

import ua.com.foxminded.dto.Group;

import java.sql.SQLException;
import java.util.List;

public interface GroupDao {

     List<Group> getAllGroups() throws SQLException;

     int addNewGroup(Group group) throws SQLException;
}
