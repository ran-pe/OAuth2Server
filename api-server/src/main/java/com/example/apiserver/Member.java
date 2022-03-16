package com.example.apiserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(unique = true)
    private String username;

    private String email;

    private String phone;

    private String nick;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    private String remark;

    public Member(String name, String username, String email, String phone, String nick, Date lastLoginDate, String remark) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.nick = nick;
        this.lastLoginDate = lastLoginDate;
        this.remark = remark;
    }
}
