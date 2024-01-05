package cgroup2.cadmycode.user;

public class ViewedItem {

    private int contentItemID;
    private int userID;
    private int viewed;

    public ViewedItem(int contentItemID, int userID, int viewed) {
        this.contentItemID = contentItemID;
        this.userID = userID;
        this.viewed = viewed;
    }

    public int getContentItemID() {
        return contentItemID;
    }

    public int getUserID() {
        return userID;
    }

    public int getViewed() {
        return viewed;
    }
}
