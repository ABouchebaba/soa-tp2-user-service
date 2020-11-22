package com.amboucheba.soatp2.models;

import java.util.List;

public class UserList {

    private List<User> users;
    private int count;

    public UserList(List<User> users) {
        this.users = users;
        this.count = users.size();
    }

    public UserList() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String toString(){
        String result = "";

        for (User user: users) {
            result = result.concat(user.getUsername() + " -> " + user.getEmail() + "\n");
        }

        return result;
    }

}
