package ru.logistica.tms.dao;

//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
import java.io.Serializable;

//@Entity
public class User {
    private Serializable userId;
    private String userIdExternal;
    private DataSources dataSourceId;
    private String login;
    private Character salt;
    private String passAndSalt;
    private String userRoleId;
    private String userName;
    private String phoneNumber;
    private String email;
    private String position;
    private Integer pointId;
    private Integer supplierId;

//    @Id
//    @Column(name = "userId")
    public Serializable getUserId() {
        return userId;
    }

    public void setUserId(Serializable userId) {
        this.userId = userId;
    }

//    @Basic
//    @Column(name = "userIdExternal")
    public String getUserIdExternal() {
        return userIdExternal;
    }

    public void setUserIdExternal(String userIdExternal) {
        this.userIdExternal = userIdExternal;
    }

//    @Basic
//    @Column(name = "dataSourceId")
    public DataSources getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(DataSources dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

//    @Basic
//    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

//    @Basic
//    @Column(name = "salt")
    public Character getSalt() {
        return salt;
    }

    public void setSalt(Character salt) {
        this.salt = salt;
    }

//    @Basic
//    @Column(name = "passAndSalt")
    public String getPassAndSalt() {
        return passAndSalt;
    }

    public void setPassAndSalt(String passAndSalt) {
        this.passAndSalt = passAndSalt;
    }

//    @Basic
//    @Column(name = "userRoleId")
    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

//    @Basic
//    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    @Basic
//    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

//    @Basic
//    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    @Basic
//    @Column(name = "position")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

//    @Basic
//    @Column(name = "pointId")
    public Integer getPointId() {
        return pointId;
    }

    public void setPointId(Integer pointId) {
        this.pointId = pointId;
    }
//    @Basic
//    @Column(name = "supplierId")
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (userIdExternal != null ? !userIdExternal.equals(user.userIdExternal) : user.userIdExternal != null) return false;
        if (dataSourceId != user.dataSourceId) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (salt != null ? !salt.equals(user.salt) : user.salt != null) return false;
        if (passAndSalt != null ? !passAndSalt.equals(user.passAndSalt) : user.passAndSalt != null) return false;
        if (userRoleId != null ? !userRoleId.equals(user.userRoleId) : user.userRoleId != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (position != null ? !position.equals(user.position) : user.position != null) return false;
        if (pointId != null ? !pointId.equals(user.pointId) : user.pointId != null) return false;
        if (supplierId != null ? supplierId.equals(user.supplierId) : user.supplierId == null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userIdExternal != null ? userIdExternal.hashCode() : 0);
        result = 31 * result + (dataSourceId != null ? dataSourceId.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (passAndSalt != null ? passAndSalt.hashCode() : 0);
        result = 31 * result + (userRoleId != null ? userRoleId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (pointId != null ? pointId.hashCode() : 0);
        result = 31 * result + (supplierId != null ? supplierId.hashCode() : 0);
        return result;
    }
}
