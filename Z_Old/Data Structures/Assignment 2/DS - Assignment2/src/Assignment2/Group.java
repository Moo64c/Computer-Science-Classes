
public class Group {
    public String GroupName;
    public Group Next;
    private UserListNode Head;

    /**
     * Generic constructor.
     */
    public Group(User user, String name) {
        Head = new UserListNode(user);
        GroupName = name;
    }

    /**
     * Returns the first node in the group's UserList.
     */
    public UserListNode getUserList() {
        return Head;
    }

    public String toString() {
        String retVal = GroupName + "\n";
        UserListNode temp = Head;
        while (temp != null) {
            retVal += temp.CurrUser.Name + "\n";
            temp = temp.Next;
        }
        return retVal;
    }

    /**
     * Auxiliary function that adds a user to the UserListNode of the group.
     *
     * @param addedUser the user which is added.
     */
    public void addUser(User addedUser) {
        UserListNode lastUserListNode = Head;
        while (lastUserListNode.Next != null) {
            lastUserListNode = lastUserListNode.Next;
        }
        lastUserListNode.Next = new UserListNode(addedUser);
    }

    /**
     * searches for a user, removes it from the group and returns it.
     *
     * @param removedUser the name of the user to remove.
     * @return a User object of the removed user.
     */
    public User removeUser(String removedUser) {
        UserListNode iteratingUserListNode = Head;

        // edge case: if the user is in the first node,
        if (Head.CurrUser.Name.equals(removedUser)) {
            User ans = Head.CurrUser;
            Head = Head.Next;
            return ans;
        }

        // get the node before the user.
        while ((iteratingUserListNode.Next != null) && (!(iteratingUserListNode.Next.CurrUser.Name.equals(removedUser)))) {
            iteratingUserListNode = iteratingUserListNode.Next;
        }

        // edge case: the user isn't in the list.
        if (iteratingUserListNode.Next == null) {
            return null;
        }

        // return the user and remove it from the list.
        User ans = iteratingUserListNode.Next.CurrUser;
        iteratingUserListNode.Next = iteratingUserListNode.Next.Next;
        return ans;

    }

    /**
     * checks if the group is empty.
     *
     * @return True if the group is empty.
     */
    public boolean isEmpty() {
        return (Head == null);
    }
}
