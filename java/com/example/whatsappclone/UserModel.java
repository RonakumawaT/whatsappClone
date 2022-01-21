package com.example.whatsappclone;

public class UserModel {

    String profilePic,Name,Email,password,UID,lastMessage,token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel(String profilePic, String Name, String Email, String password, String UID, String lastMessage) {
        this.profilePic = profilePic;
        this.Name = Name;
        this.Email = Email;
        this.password = password;
        this.UID = UID;
        this.lastMessage = lastMessage;
    }

    public UserModel(String Name, String mail, String password) {
        this.Name = Name;
        this.Email = mail;
        this.password = password;
    }

    public UserModel(){

    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getEMail() {
        return Email;
    }

    public void setEMail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
