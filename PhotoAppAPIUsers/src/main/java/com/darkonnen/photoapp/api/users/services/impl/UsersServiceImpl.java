package com.darkonnen.photoapp.api.users.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.darkonnen.photoapp.api.users.data.AlbumsServiceClient;
import com.darkonnen.photoapp.api.users.data.UserEntity;
import com.darkonnen.photoapp.api.users.data.UsersRepository;
import com.darkonnen.photoapp.api.users.services.UsersService;
import com.darkonnen.photoapp.api.users.shared.UserDto;
import com.darkonnen.photoapp.api.users.ui.models.AlbumResponseModel;

@Service
public class UsersServiceImpl implements UsersService {

	UsersRepository usersRepository;
	BCryptPasswordEncoder bCryptPasswordEncoder;
//	RestTemplate restTemplate;
	AlbumsServiceClient albumServiceClient;
	Environment environment;
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	

	@Autowired
	public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			 Environment environment, AlbumsServiceClient albumServiceClient) { //RestTemplate restTemplate,
		this.usersRepository = usersRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
//		this.restTemplate = restTemplate;
		this.albumServiceClient = albumServiceClient;
		this.environment = environment;
	}

	@Override
	public UserDto createUser(UserDto userDetails) {
		userDetails.setUserId(UUID.randomUUID().toString());
		userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
		usersRepository.save(userEntity);
//		userEntity.setEncryptedPassword("test");

		UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity userEntity = usersRepository.findByEmail(username);

		if (userEntity == null)
			throw new UsernameNotFoundException(username);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true,
				new ArrayList<>());
	}

	@Override
	public UserDto getUserDetailsByEmail(String email) {
		UserEntity userEntity = usersRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new ModelMapper().map(userEntity, UserDto.class);
	}

	@Override
	public UserDto getUserById(String userId) {
		UserEntity userEntity = usersRepository.findByUserId(userId);

		if (userEntity == null)
			throw new UsernameNotFoundException("User not found");
		UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//		String albumsUrl = String.format(environment.getProperty("albums.url"), userId);
//		
////		System.out.println(albumsUrl);
//
//		ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET,
//				null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
//				});
//		List<AlbumResponseModel> albumsList = albumsListResponse.getBody();
		
		logger.info("Before calling albums microservice");
		List<AlbumResponseModel> albumsList = albumServiceClient.getAlbums(userId);
		logger.info("After calling albums microservice");

//		try {
//			albumsList = albumServiceClient.getAlbums(userId);
//		} catch (FeignException e) {
//			// TODO Auto-generated catch block
//			logger.error(e.getLocalizedMessage());
//		}
//		
		userDto.setAlbums(albumsList);
		
		return userDto;
	}

}
