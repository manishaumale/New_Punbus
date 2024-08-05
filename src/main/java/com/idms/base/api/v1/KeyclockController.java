package com.idms.base.api.v1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.json.JSONObject;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idms.base.api.v1.model.dto.GroupDto;
import com.idms.base.api.v1.model.dto.ModuleDto;
import com.idms.base.api.v1.model.dto.RefreshTokenDto;
import com.idms.base.api.v1.model.dto.RolesDto;
import com.idms.base.api.v1.model.dto.UserDto;
import com.idms.base.api.v1.model.dto.UserInfoDto;
import com.idms.base.dao.entity.Role;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.repository.RoleRepository;
import com.idms.base.service.UserService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/keycloak")
@Validated
public class KeyclockController {

	@Autowired
	private ModelMapper mapper;

	@Value("${keycloak.auth-server-url}")
	private String keycloakUrl;

	@Value("${keycloak.realm}")
	private String keycloakRealm;

	@Value("${keycloak.resource}")
	private String keycloakClient;

	@Value("${keycloak1.client-sec}")
	private String keycloakClientSecret;

	@Value("${keycloak1.client_id}")
	private String keycloakClientId;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	private UserService userService;

	@PostMapping("/getLogin")
	public ResponseEntity<UserInfoDto> getLogin(@RequestParam("username") String username,
			@RequestParam("password") String password) {
		String token = "";
		String refresh_token = "";
		AccessTokenResponse accessTokenResponse = null;
		StringBuffer response = new StringBuffer();
		UserInfoDto userInfo = new UserInfoDto();
		try {
			Keycloak keycloak = KeycloakBuilder.builder().serverUrl(keycloakUrl).realm(keycloakRealm).username(username)
					.password(password).clientId(keycloakClient).clientSecret(keycloakClientSecret).build();
			accessTokenResponse = keycloak.tokenManager().getAccessToken();
			token = accessTokenResponse.getToken();
			refresh_token = accessTokenResponse.getRefreshToken();
			URL urlForGetRequest = new URL(keycloakUrl + RestConstants.USER_INFO_URL);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			conection.setRequestProperty("Content-Type", "application/json");
			conection.setRequestProperty("Authorization", "Bearer " + token);

			int responseCode = conection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				userInfo = new ObjectMapper().readValue(response.toString(), UserInfoDto.class);
				userInfo.setToken(token);
				userInfo.setRefreshToken(refresh_token);
				Role role = userService.getRoleByCode(userInfo.getRoles().get(0));
				List<ModuleDto> modules = userService.getAllModuleByRole(role.getId());
				userInfo.setModules(modules);
				
			} else {
				System.out.println("GET NOT WORKED");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<UserInfoDto>(userInfo, HttpStatus.OK);
	}
	
	@GetMapping("/refreshToken/{refreshToken}")
	public RefreshTokenDto refreshToken(@PathVariable("refreshToken") String refToken) {
		StringBuffer response = new StringBuffer();
		RefreshTokenDto dto = new RefreshTokenDto();
		JSONObject json = null;
		try {
			
			String url = keycloakUrl + "/realms/IDMSPunjab/protocol/openid-connect/token";
			URL urlForGetRequest = new URL(url);
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("POST");
			conection.setDoOutput(true);
			conection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conection.setRequestProperty( "charset", "utf-8");
			
			String jsonString = "client_id=" +keycloakClient  + "&grant_type=refresh_token&refresh_token=" + refToken + "&client_secret=" + keycloakClientSecret ; 
			byte[] postData       = jsonString.getBytes( StandardCharsets.UTF_8 );
			int    postDataLength = postData.length;
			conection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
			
			try( DataOutputStream wr = new DataOutputStream( conection.getOutputStream())) {
				   wr.write( postData );
			}
			
			int responseCode = conection.getResponseCode();
			String readLine = null;
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
			} else {
				System.out.println("GET NOT WORKED");
			}
			dto = new ObjectMapper().readValue(response.toString(), RefreshTokenDto.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	@PostMapping("/createRole")
	public ResponseEntity<ResponseStatus> createRole(@RequestBody @Valid RolesDto roleDto) {
		String tokenString = "";
		String role1 = this.getRole();
		if (roleDto.getName().equals("")) {
			return new ResponseEntity<ResponseStatus>(
					new ResponseStatus("Role name is mandatory.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
		if(role1.equals("SUPERADMIN")) {
			try {
	
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				KeycloakAuthenticationToken token;
				KeycloakSecurityContext context;
				token = (KeycloakAuthenticationToken) authentication;
				context = token.getAccount().getKeycloakSecurityContext();
				tokenString = context.getTokenString();
	
				ObjectMapper obj = new ObjectMapper();
				String roleDtoJson = obj.writeValueAsString(roleDto);
	
				URL urlForGetRequest = new URL(keycloakUrl + RestConstants.ROLE_CREATE_URL + keycloakClientId + "/roles");
				String readLine = null;
				HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
				conection.setRequestMethod("POST");
				conection.setDoOutput(true);
				conection.setRequestProperty("Content-Type", "application/json");
				conection.setRequestProperty("Authorization", "Bearer " + tokenString);
				conection.setRequestProperty("Content-Length", Integer.toString(roleDtoJson.length()));
	
				try (OutputStream os = conection.getOutputStream()) {
					byte[] input = roleDtoJson.getBytes("utf-8");
					os.write(input, 0, input.length);
				}
	
				int responseCode = conection.getResponseCode();
				if (responseCode == 201 || responseCode == 200) {
					RolesDto dto = getRoleByName(tokenString, roleDto.getName());
					Role role = new Role();
					role.setIds(dto.getId());
					role.setName(roleDto.getName());
					role.setRoleCode(roleDto.getName());
					role.setDescription(roleDto.getDescription());
					roleRepo.save(role);
					return new ResponseEntity<ResponseStatus>(
							new ResponseStatus(conection.getResponseMessage(), HttpStatus.OK), HttpStatus.OK);
				} else {
					return new ResponseEntity<ResponseStatus>(
							new ResponseStatus(conection.getResponseMessage(), HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ResponseStatus>(new ResponseStatus("Something went wrong.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<ResponseStatus>(new ResponseStatus("Not Authorized.", HttpStatus.UNAUTHORIZED),
					HttpStatus.OK);
		}
	}

	@PutMapping("/updateRole")
	public ResponseEntity<ResponseStatus> updateRole(@RequestBody @Valid RolesDto roleDto,
			@RequestParam("oldname") String oldname) {
		String tokenString = "";
		String role1 = this.getRole();
		if (roleDto.getName().equals("")) {
			return new ResponseEntity<ResponseStatus>(
					new ResponseStatus("Role name is mandatory.", HttpStatus.FORBIDDEN), HttpStatus.OK);
		}
		if(role1.equals("SUPERADMIN")) {
			try {
	
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				KeycloakAuthenticationToken token;
				KeycloakSecurityContext context;
				token = (KeycloakAuthenticationToken) authentication;
				context = token.getAccount().getKeycloakSecurityContext();
				tokenString = context.getTokenString();
				URL urlForGetRequest = new URL(
						keycloakUrl + RestConstants.ROLE_CREATE_URL + keycloakClientId + "/roles/" + oldname);
				ObjectMapper obj = new ObjectMapper();
				String roleDtoJson = obj.writeValueAsString(roleDto);
	
				HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
				conection.setRequestMethod("PUT");
				conection.setDoOutput(true);
				conection.setRequestProperty("Content-Type", "application/json");
				conection.setRequestProperty("Authorization", "Bearer " + tokenString);
				conection.setRequestProperty("Content-Length", Integer.toString(roleDtoJson.length()));
	
				try (OutputStream os = conection.getOutputStream()) {
					byte[] input = roleDtoJson.getBytes("utf-8");
					os.write(input, 0, input.length);
				}
	
				int responseCode = conection.getResponseCode();
				if (responseCode == 201 || responseCode == 200 || responseCode == 204) {
					Role role = roleRepo.findByName(oldname);
					role.setName(roleDto.getName());
					role.setDescription(roleDto.getDescription());
					roleRepo.save(role);
					return new ResponseEntity<ResponseStatus>(
							new ResponseStatus(conection.getResponseMessage(), HttpStatus.OK), HttpStatus.OK);
				} else {
					return new ResponseEntity<ResponseStatus>(
							new ResponseStatus(conection.getResponseMessage(), HttpStatus.FORBIDDEN), HttpStatus.OK);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<ResponseStatus>(new ResponseStatus("Something went wrong.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<ResponseStatus>(new ResponseStatus("Not Authorized.", HttpStatus.UNAUTHORIZED),
					HttpStatus.OK);
		}
	}

	@GetMapping("/getRoles")
	public ResponseEntity<List<RolesDto>> getRoles() {
		List<RolesDto> list = new ArrayList<>();
		String role1 = this.getRole();
		if(role1.equals("SUPERADMIN")) {
			try {
				list  = this.userService.getAllRoles().stream().map(role -> this.mapper.map(role, RolesDto.class)).collect(Collectors.toList());
			} catch(Exception e) {
				e.printStackTrace();
				return new ResponseEntity<List<RolesDto>>(list, HttpStatus.FORBIDDEN);
			}
		} else {
			return new ResponseEntity<List<RolesDto>>(list, HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<RolesDto>>(list, HttpStatus.OK);	
	}
	
	@GetMapping("/getUsers/{group}")
	public ResponseEntity<List<UserDto>> getUsers(@PathVariable("group") String group) {
		String tokenString = "";
		String groupId = "";
		StringBuffer response = new StringBuffer();
		StringBuffer userResponse = new StringBuffer();
		List<GroupDto> groups = new ArrayList<>();
		List<UserDto> users = new ArrayList<>();
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			KeycloakAuthenticationToken token;
			KeycloakSecurityContext context;
			token = (KeycloakAuthenticationToken) authentication;
			context = token.getAccount().getKeycloakSecurityContext();
			tokenString = context.getTokenString();
			URL urlForGetRequest = new URL(keycloakUrl + RestConstants.GET_GROUPS_URL);
			
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			conection.setDoOutput(true);
			conection.setRequestProperty("Content-Type", "application/json");
			conection.setRequestProperty("Authorization", "Bearer " + tokenString);
			ObjectMapper obj = new ObjectMapper();
			int responseCode = conection.getResponseCode();
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
				String readLine = null;
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				groups = obj.readValue(response.toString(), new TypeReference<List<GroupDto>>() {});
			}
			
			if(groups.size()>0) {
				for(GroupDto grp : groups) {
					if(grp.getName().equals(group)) {
						groupId = grp.getId();
						break;
					}
				}
			}
			
			if(!groupId.equals("")) {
				URL urlForGetUsers = new URL(keycloakUrl + RestConstants.GET_GROUPS_URL + groupId + "/members" );
				HttpURLConnection userCon = (HttpURLConnection) urlForGetUsers.openConnection();
				userCon.setRequestMethod("GET");
				userCon.setDoOutput(true);
				userCon.setRequestProperty("Content-Type", "application/json");
				userCon.setRequestProperty("Authorization", "Bearer " + tokenString);
				int resCode = userCon.getResponseCode();
				if (resCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(userCon.getInputStream()));
					String readLine = null;
					while ((readLine = in.readLine()) != null) {
						userResponse.append(readLine);
					}
					in.close();
					users = obj.readValue(userResponse.toString(), new TypeReference<List<UserDto>>() {});
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<UserDto>>(users, HttpStatus.OK);
	}
	
	public String getRole() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String role = "";
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<User> optional = userService.findByUserName(authentication.getName());
			if (optional.isPresent()) {
				role = optional.get().getRole().getRoleCode();
			}
		}
		return role;
	}
	
	public RolesDto getRoleByName(String token, String name) {
		RolesDto dto = new RolesDto();
		StringBuffer response = new StringBuffer();
		try {
			URL urlForGetRequest = new URL(keycloakUrl + RestConstants.ROLE_CREATE_URL + keycloakClientId + "/roles/"+name);
			String readLine = null;
			HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
			conection.setRequestMethod("GET");
			conection.setDoOutput(true);
			conection.setRequestProperty("Content-Type", "application/json");
			conection.setRequestProperty("Authorization", "Bearer " + token);
			
			int responseCode = conection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));

				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				dto = new ObjectMapper().readValue(response.toString(), RolesDto.class);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
}
