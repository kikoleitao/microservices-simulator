package pt.ulisboa.tecnico.socialsoftware.teastore.microservices.user.aggregate;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;

import pt.ulisboa.tecnico.socialsoftware.ms.domain.aggregate.Aggregate;

import pt.ulisboa.tecnico.socialsoftware.teastore.shared.dtos.UserDto;

@Entity
public abstract class User extends Aggregate {
    @Column
    private String userName;
    @Column
    private String password;
    @Column
    private String realName;
    @Column
    private String email;

    public User() {

    }

    public User(Integer aggregateId, UserDto userDto) {
        super(aggregateId);
        setAggregateType(getClass().getSimpleName());
        setUserName(userDto.getUserName());
        setPassword(userDto.getPassword());
        setRealName(userDto.getRealName());
        setEmail(userDto.getEmail());
    }

    public User(User other) {
        super(other);
        setUserName(other.getUserName());
        setPassword(other.getPassword());
        setRealName(other.getRealName());
        setEmail(other.getEmail());
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public void verifyInvariants() {
        // No invariants defined
    }

}