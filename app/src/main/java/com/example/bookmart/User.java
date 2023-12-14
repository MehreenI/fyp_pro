package com.example.bookmart;

import com.example.extra.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private int coin;
    private String Country;
    private String Province;
    private String City;
    private String Town;
    private long joinedDate;
    private String profileImg;
    private String phoneNumber;
    private List<String> favPostId;
    private Enums.UserStatus userStatus;

    public User() {
        this.username = "";
        this.firstName = "";
        this.lastName = "";
        this.password = "";
        this.email = "";
        this.coin = 10;
        this.Country = "";
        this.Province = "";
        this.City = "";
        this.Town = "";
        this.joinedDate = 0;
        this.profileImg = "";
        this.phoneNumber = "";
        this.favPostId = new ArrayList<>();
        this.userStatus = Enums.UserStatus.ACTIVE;
    }

    public User(String username, String firstName, String lastName, String password, String email, int coin, String country, String province, String city, String town, long joinedDate, String profileImg, String phoneNumber, List<String> favPostId, Enums.UserStatus userStatus) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.coin = coin;
        this.Country = country;
        this.Province = province;
        this.City = city;
        this.Town = town;
        this.joinedDate = joinedDate;
        this.profileImg = profileImg;
        this.phoneNumber = phoneNumber;
        this.favPostId = favPostId;
        this.userStatus = userStatus;
    }


    // region Method
    public boolean validateUsername(String string){
        return Preconditions.checkNotEmpty(string) &&
                Preconditions.checkNotNull(string);
    }
    public boolean validatePassword(String string){
        return Preconditions.checkNotEmpty(string) &&
                Preconditions.checkNotNull(string) &&
                Preconditions.isStrongPassword(string);
    }
    public boolean validatePhoneNumber(String string){
        return Preconditions.validPakistanNumber(string) &&
                Preconditions.checkNotNull(string) &&
                Preconditions.checkNotEmpty(string);
    }
    // endregion Method


    // region Getter/Setter
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public int getCoin() {return coin;}
    public void setCoin(int coin) {this.coin = coin;}
    public String getCountry() {return Country;}
    public void setCountry(String country) {Country = country;}
    public String getProvince() {return Province;}
    public void setProvince(String province) {Province = province;}
    public String getCity() {return City;}
    public void setCity(String city) {City = city;}
    public String getTown() {return Town;}
    public void setTown(String town) {Town = town;}
    public long getJoinedDate() {
        return joinedDate;
    }
    public void setJoinedDate(long joinedDate) {
        this.joinedDate = joinedDate;
    }
    public String getProfileImg() {
        return profileImg;
    }
    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public List<String> getFavPostId() {
        return favPostId;
    }
    public void setFavPostId(List<String> favPostId) {
        this.favPostId = favPostId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Enums.UserStatus getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(Enums.UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    // endregion Getter/Setter

}
