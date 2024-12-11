package com.stock.pycurrent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * yf_token
 *
 * @author fzc
 */
@Getter
@Setter
@Entity
@Table(name = "yf_token")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YfToken implements Serializable {
    /**
     * sn
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sn")
    private Integer sn;

    /**
     * 用户sn
     */
    @Column(name = "user_sn")
    private Integer userSn;

    /**
     * token
     */
    @Column(name = "token")
    private String token;

    /**
     * 保证设备唯一性的id, 微信openid或android设备的唯一ID
     */
    @Column(name = "uid")
    private String uid;

    /**
     * 申请token的项目类型, DEVICE_WX等
     */
    @Column(name = "device")
    private String device;

    /**
     * token失效时间
     */
    @Column(name = "expire_date")
    private Date expireDate;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * ip地址
     */
    @Column(name = "ip")
    private String ip;

    @Column(name = "token_salt")
    private String tokenSalt;

    /**
     * 微信的unionid,全局唯一id
     */
    @Column(name = "unionid")
    private String unionid;

    /**
     * 三方平台的登陆session, 微信小程序中为session_key
     */
    @Column(name = "third_session")
    private String thirdSession;
    /**
     * 公钥
     */
    @Column(name = "public_key")
    private String publicKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YfToken yfToken = (YfToken) o;
        return Objects.equals(sn, yfToken.sn) && Objects.equals(userSn, yfToken.userSn) && Objects.equals(token, yfToken.token) && Objects.equals(uid, yfToken.uid) && Objects.equals(device, yfToken.device) && Objects.equals(expireDate, yfToken.expireDate) && Objects.equals(updateTime, yfToken.updateTime) && Objects.equals(ip, yfToken.ip) && Objects.equals(tokenSalt, yfToken.tokenSalt) && Objects.equals(unionid, yfToken.unionid) && Objects.equals(thirdSession, yfToken.thirdSession) && Objects.equals(publicKey, yfToken.publicKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sn, userSn, token, uid, device, expireDate, updateTime, ip, tokenSalt, unionid, thirdSession, publicKey);
    }
}