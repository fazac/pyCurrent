package com.stock.pycurrent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * yf_usr
 *
 * @author fzc
 */
@Getter
@Setter
@Entity
@Table(name = "yf_usr")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YfUsr implements Serializable {
    /**
     * sn
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sn")
    private Integer sn;

    /**
     * 名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * emial
     */
    @Column(name = "email")
    private String email;

    /**
     * 图像
     */
    @Column(name = "img")
    private String img;

    /**
     * 唯一id
     */
    @Column(name = "uid")
    private String uid;

    /**
     * 密码,md5加密
     */
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "totp_sk")
    private String totpSk;
    @Column(name = "totp_img")
    private String totpImg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YfUsr yfUsr = (YfUsr) o;
        return Objects.equals(sn, yfUsr.sn) && Objects.equals(name, yfUsr.name) && Objects.equals(phone, yfUsr.phone) && Objects.equals(email, yfUsr.email) && Objects.equals(img, yfUsr.img) && Objects.equals(uid, yfUsr.uid) && Objects.equals(password, yfUsr.password) && Objects.equals(salt, yfUsr.salt) && Objects.equals(totpSk, yfUsr.totpSk) && Objects.equals(totpImg, yfUsr.totpImg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sn, name, phone, email, img, uid, password, salt, totpSk, totpImg);
    }
}