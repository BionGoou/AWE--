package com.awe.model.other;

import com.alibaba.fastjson.annotation.JSONField;
import com.awe.model.entity.SysUserDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private SysUserDO user;

    private String uuid;

    private List<String> permissions;

    public void setUser(SysUserDO user) {
        this.user = user;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public Long getUserId(){
        return this.user.getUserId();
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @JSONField(serialize = false)
    private List<SimpleGrantedAuthority> authorities;

    public LoginUser(SysUserDO user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // 将permissions中的String类型的权限信息封装成SimpleGrantedAuthority对象
        if(!Objects.isNull(authorities)){
            return this.authorities;
        }
        this.authorities = this.permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return this.authorities;

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public SysUserDO getUser() {
        return this.user;
    }
}
