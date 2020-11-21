package com.amboucheba.soatp2.models;

import java.util.List;

public class MessageList {


    private List<Message> messages;
    private int count;

    public MessageList(List<Message> messages) {
        this.messages = messages;
        this.count = messages.size();
    }

    public MessageList() {
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public String toString(){
        String result = "";

        for (Message message: messages) {
            result = result.concat(message.getId() + " : " + message.getText() + "\n");
        }

        return result;
    }
}
