
public class UserListNode {
    public UserListNode Next;
    public User CurrUser;

    /**
     * Generic constructor.
     */
    public UserListNode(User user) {
        CurrUser = user;
    }

    /**
     * Auxiliary function - seeks the user according to name, in UserListNode.
     *
     * @param name - to search
     * @return the User
     */
    public User findUser(String name) {
        UserListNode searcherUser = this;
        while ((searcherUser != null) && !(searcherUser.CurrUser.Name.equals(name))) {
            searcherUser = searcherUser.Next;
        }
        return searcherUser.CurrUser;
    }

}
