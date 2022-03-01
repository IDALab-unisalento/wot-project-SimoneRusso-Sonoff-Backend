package it.unisalento.sonoffbackend.hibernate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.unisalento.sonoffbackend.hibernate.dao.UserRepository;
import it.unisalento.sonoffbackend.hibernate.domain.User;
import it.unisalento.sonoffbackend.hibernate.iService.IUserService;

@Service
public class UserService implements IUserService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public User save(User user) throws IllegalArgumentException {
		return userRepository.save(user);
	}

}
