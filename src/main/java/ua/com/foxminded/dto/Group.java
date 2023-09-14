package ua.com.foxminded.dto;

public class Group {

    private long groupId;
    private String groupName;

    public Group(long groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
