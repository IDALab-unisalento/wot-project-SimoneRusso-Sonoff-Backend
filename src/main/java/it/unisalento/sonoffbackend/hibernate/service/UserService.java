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
	public User save(User user) throws Exception {
		try {
			return userRepository.save(user);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public User findByUsername(String username) throws Exception{
		try {
			return userRepository.findByUsername(username);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
