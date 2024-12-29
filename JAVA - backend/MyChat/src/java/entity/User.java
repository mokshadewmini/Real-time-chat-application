package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "user")
public class User implements Serializable{
    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "fname", length = 45, nullable = false)
    private String fname;
    
    @Column(name = "lname",length = 45, nullable = false)
    private String lname;
    
    @Column(name = "mobile",length = 10, nullable = false)
    private String mobile;
    
    @Column(name = "password",length = 70, nullable = false)
    private String password;
    
    @Column(name = "created_date",nullable = false)
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "user_status_id")
    private User_Status user_Status;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the user_Status
     */
    public User_Status getUser_Status() {
        return user_Status;
    }

    /**
     * @param user_Status the user_Status to set
     */
    public void setUser_Status(User_Status user_Status) {
        this.user_Status = user_Status;
    }
    
}
