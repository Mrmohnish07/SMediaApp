package com.mb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mb.Entity.User;

public interface UserRepo extends  JpaRepository<User,Long>
{

  public User findByUsername(String username);

}
