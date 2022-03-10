package com.example.apiserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    private String remark;

    public Member(String name, String username, String remark) {
        this.name = name;
        this.username = username;
        this.remark = remark;
    }
}
