package ru.logistica.tms.dao2.userDao;

import org.hibernate.annotations.NaturalId;
import ru.logistica.tms.util.CriptUtils;

import javax.persistence.*;

@Entity
@Table(name = "users", schema = "public", catalog = "postgres")
@Inheritance(strategy=InheritanceType.JOINED)
public class User {
    private Integer userId;
    private String userLogin;
    private String salt;
    private String passAndSalt;
    private String userName;
    private String phoneNumber;
    private String email;
    private String position;
    private UserRole userRole;

    @Id
    @Column(name = "userid", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @NaturalId(mutable = false)
    @Column(name = "userlogin", nullable = false, length = 255, unique = true)
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Basic
    @Column(name = "salt", nullable = false, length = 16)
    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Basic
    @Column(name = "passandsalt", nullable = false, length = 32)
    public String getPassAndSalt() {
        return passAndSalt;
    }

    public void setPassAndSalt(String passAndSalt) {
        this.passAndSalt = passAndSalt;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 255)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "phonenumber", nullable = false, length = 255)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 255)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "position", nullable = false, length = 64)
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name="userroleid")
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    /**
     * take real user password as String and write into salt and passAndSalt
     * @param password
     */
    public void setPassword(String password) {
        String salt = CriptUtils.generateSalt();
        setPassAndSalt(CriptUtils.generatePassAndSalt(password, salt));
        setSalt(salt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != null ? !userId.equals(user.userId) : user.userId != null) return false;
        if (userLogin != null ? !userLogin.equals(user.userLogin) : user.userLogin != null) return false;
        if (salt != null ? !salt.equals(user.salt) : user.salt != null) return false;
        if (passAndSalt != null ? !passAndSalt.equals(user.passAndSalt) : user.passAndSalt != null) return false;
        if (userName != null ? !userName.equals(user.userName) : user.userName != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(user.phoneNumber) : user.phoneNumber != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (position != null ? !position.equals(user.position) : user.position != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (userLogin != null ? userLogin.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (passAndSalt != null ? passAndSalt.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userLogin='" + userLogin + '\'' +
                ", salt='" + salt + '\'' +
                ", passAndSalt='" + passAndSalt + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", position='" + position + '\'' +
                ", userRole=" + userRole +
                '}';
    }
}
