package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name = "chat")
public class Chat  implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int  id;
    
   @ManyToOne
   @JoinColumn(name = "form_user")
   private User  form_user;
   
   @ManyToOne
   @JoinColumn(name = "to_user")
   private User  to_user;
   
   @Column(name = "date_time")
   private Date date_time;
   
    @Column(name = "message")
   private String message;
   
   
   
   @ManyToOne
   @JoinColumn(name = "chat_status_id")
   private Chat_Status chat_status;
   
   public Chat(){}

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
     * @return the form_user
     */
    public User getForm_user() {
        return form_user;
    }

    /**
     * @param form_user the form_user to set
     */
    public void setForm_user(User form_user) {
        this.form_user = form_user;
    }

    /**
     * @return the to_user
     */
    public User getTo_user() {
        return to_user;
    }

    /**
     * @param to_user the to_user to set
     */
    public void setTo_user(User to_user) {
        this.to_user = to_user;
    }

    /**
     * @return the date_time
     */
    public Date getDate_time() {
        return date_time;
    }

    /**
     * @param date_time the date_time to set
     */
    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    /**
     * @return the chat_status
     */
    public Chat_Status getChat_status() {
        return chat_status;
    }

    /**
     * @param chat_status the chat_status to set
     */
    public void setChat_status(Chat_Status chat_status) {
        this.chat_status = chat_status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
   
   
}
