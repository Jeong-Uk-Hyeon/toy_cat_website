// users테이블 객체 설정
package com.example.capston_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userid;

    @Column(nullable = false)
    private String userpass;

    // Getter, Setter
    public String getUserid() { return userid; }
    public void setUserid(String userid) { this.userid = userid; }

    public String getUserpass() { return userpass; }
    public void setUserpass(String userpass) { this.userpass = userpass; }
}
