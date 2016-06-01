package ru.logistica.tms.dao.userDao;

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
    @SequenceGenerator(name="users_userid_seq", sequenceName="users_userid_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "users_userid_seq")
    @Column(name = "userid", updatable = false)
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @NaturalId(mutable = true)
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

        return userId != null ? userId.equals(user.userId) : user.userId == null;

    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
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
