package it.unisalento.sonoffbackend.hibernate.iService;

import it.unisalento.sonoffbackend.hibernate.domain.User;

public interface IUserService {

	User save(User user) throws Exception;

	User findByUsername(String username) throws Exception;

}
