package com.idms.base.api.v1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.idms.base.api.v1.model.dto.AttributeDto;
import com.idms.base.api.v1.model.dto.CityDto;
import com.idms.base.api.v1.model.dto.CredentialDto;
import com.idms.base.api.v1.model.dto.DepotMasterDto;
import com.idms.base.api.v1.model.dto.RolesDto;
import com.idms.base.api.v1.model.dto.UserDto;
import com.idms.base.dao.entity.DepotMaster;
import com.idms.base.dao.entity.Role;
import com.idms.base.dao.entity.TransportUnitMaster;
import com.idms.base.dao.entity.User;
import com.idms.base.dao.entity.UserGroups;
import com.idms.base.service.BasicMasterService;
import com.idms.base.service.UserService;
import com.idms.base.support.persist.ResponseStatus;
import com.idms.base.support.rest.RestConstants;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/user")
@Validated
public class KeycloakUserController {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserService userService;

	@Autowired
	private BasicMasterService basicService;

	@Value("${keycloak.auth-server-url}")
	private String keycloakUrl;

	@Value("${keycloak1.client_id}")
	private String keycloakClientId;

	@Value("${realm-manage.manage_user_role_id}")
	private String manageUserRoleId;

	@Value("${realm-manage.manage_user_role_name}")
	private String manageUserRoleName;

	@Value("${realm-manage.client_id}")
	private String manageUserRealmId;

	// @GetMapping("/getUsers/{depotCode}")
	// public ResponseEntity<List<UserDto>>
	// getUsersByDepot(@PathVariable(name="depotCode", required=false) String
	// depotCode) {
	// List<UserDto> list = new ArrayList<>();
	// String role = this.getRole();
	// if(role.equals("SUPERADMIN") || role.equals("DEPOTADMIN")) {
	// try {
	// list =
	// this.userService.getUsers(depotCode).stream().map(user->this.mapper.map(user,
	// UserDto.class)).collect(Collectors.toList());
	// } catch(Exception e) {
	// e.printStackTrace();
	// return new ResponseEntity<List<UserDto>>(list,
	// HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// } else {
	// return new ResponseEntity<List<UserDto>>(list, HttpStatus.UNAUTHORIZED);
	// }
	// return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
	// }

	@GetMapping("/getUsers")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> list = new ArrayList<>();
		String role = this.getRole();
		List<Integer> ids = this.getTPUs();
		try {
			if (role.equals("SUPERADMIN")) {
				this.userService.getUsers(null, ids).stream().forEach(user -> {
					if (!user.getRole().getRoleCode().equals("SUPERADMIN")) {
						list.add(this.mapper.map(user, UserDto.class));
					}
				});
			} else if (role.equals("DEPOTADMIN")) {

			} else {
				return new ResponseEntity<List<UserDto>>(list, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<UserDto>>(list, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
	}

	@GetMapping("/getRoles")
	public ResponseEntity<List<RolesDto>> getRoles() {
		List<RolesDto> list = new ArrayList<>();
		String role = this.getRole();
		if (role.equals("SUPERADMIN") || role.equals("DEPOTADMIN")) {
			try {
				this.userService.getAllRoles().stream().forEach(role1 -> {
					if (!role1.getRoleCode().equals("SUPERADMIN")) {
						list.add(this.mapper.map(role1, RolesDto.class));
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return new ResponseEntity<List<RolesDto>>(list, HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<RolesDto>>(list, HttpStatus.OK);
	}

	@GetMapping("/getDepots")
	public ResponseEntity<List<DepotMasterDto>> getDetpots() {
		List<DepotMasterDto> list = new ArrayList<>();
		List<Integer> tpIds = this.getTPUs();
		try {
			list = this.basicService.findAllDepotMasterByActiveStatus(tpIds).stream()
					.map(depot -> this.mapper.map(depot, DepotMasterDto.class)).collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<DepotMasterDto>>(list, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<List<DepotMasterDto>>(list, HttpStatus.OK);
	}

	@PostMapping("/createUser")
	public ResponseEntity<ResponseStatus> createUser(@RequestBody UserDto userDto) {
		String role1 = this.getRole();

		if (role1.equals("SUPERADMIN") || role1.equals("DEPOTADMIN")) {
			try {

				URL urlForCreateUser = new URL(keycloakUrl + RestConstants.USER);
				ObjectMapper obj = new ObjectMapper();
				String tokenString = this.getToken();

				String readLine = null;

				UserDto u = new UserDto();
				u.setUsername(userDto.getUsername());
				u.setEmail(userDto.getEmail());
				u.setEmailVerified(true);
				u.setEnabled(true);

				CredentialDto credentials[] = new CredentialDto[1];
				CredentialDto cred = new CredentialDto();
				cred.setTemporary(false);
				cred.setType("password");
				cred.setValue("admin@123");
				credentials[0] = cred;
				u.setCredentials(credentials);

				Optional<Role> role = userService.getRoleById(userDto.getRole().getId());
				System.out.println(role);
				Optional<DepotMaster> dp = basicService.getDepotById(userDto.getDepot().getId());
				String[] depot = { dp.get().getDepotCode() };
				AttributeDto attrib = new AttributeDto();
				attrib.setDepot(depot);
				u.setAttributes(attrib);
				u.setGroups(userDto.getGroups());

				String userJSON = obj.writeValueAsString(u);

				HttpURLConnection conection = (HttpURLConnection) urlForCreateUser.openConnection();
				conection.setRequestMethod("POST");
				conection.setDoOutput(true);
				conection.setRequestProperty("Content-Type", "application/json");
				conection.setRequestProperty("Authorization", "Bearer " + tokenString);
				conection.setRequestProperty("Content-Length", Integer.toString(userJSON.length()));

				try (OutputStream os = conection.getOutputStream()) {
					byte[] input = userJSON.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				// conection.respo
				int responseCode = conection.getResponseCode();
				StringBuilder response = new StringBuilder();
				if (responseCode == 201 || responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));

					while ((readLine = in.readLine()) != null) {
						response.append(readLine);
					}

					String id = conection.getHeaderField("Location")
							.substring(conection.getHeaderField("Location").lastIndexOf("/") + 1);

					User user = new User();
					user.setId(id);
					DepotMaster depot1 = new DepotMaster();
					depot1.setId(userDto.getDepot().getId());
					user.setDepot(depot1);
					user.setRole(role.get());
					user.setEmail(userDto.getEmail());
					user.setEmailVerified(true);
					user.setFirstName(userDto.getFirstName());
					user.setLastName(userDto.getLastName());
					user.setEnabled(true);
					user.setUsername(userDto.getUsername());
					List<Integer> tpIds = this.getTPUs();
					List<UserGroups> ugList = new ArrayList<>();
					for(Integer i : tpIds) {
						UserGroups ug = new UserGroups();
						TransportUnitMaster tum = new TransportUnitMaster();
						tum.setId(i);
						ug.setUser(user);
						ug.setTransportUnit(tum);
						ugList.add(ug);
						
					}
					user.setTpGroups(ugList);
					user = userService.saveUser(user);
					String status = this.assignRoleToUser(tokenString, user);
					if (status.equals("OK")) {
						return new ResponseEntity<>(new ResponseStatus("User added successfully.", HttpStatus.OK),
								HttpStatus.OK);
					} else {
						userService.deleteUser(user);
						this.deleteUser(tokenString, user);
						return new ResponseEntity<>(new ResponseStatus("User creation failed.", HttpStatus.FORBIDDEN),
								HttpStatus.OK);
					}
				} else {
					return new ResponseEntity<>(new ResponseStatus("User creation failed.", HttpStatus.FORBIDDEN),
							HttpStatus.OK);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(new ResponseStatus("Something went wrong.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(new ResponseStatus("Don't Allowed.", HttpStatus.UNAUTHORIZED), HttpStatus.OK);
		}

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
	
	public List<Integer> getTPUs() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		List<Integer> tpus = new ArrayList<Integer>();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Optional<User> optional = userService.findByUserName(authentication.getName());
			if (optional.isPresent()) {
				for(UserGroups tpu : optional.get().getTpGroups()) {
					tpus.add(tpu.getTransportUnit().getId());
				}
			}
		}
		return tpus;
	}

	private String getToken() {
		String tokenString = "";
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			KeycloakAuthenticationToken token;
			KeycloakSecurityContext context;
			token = (KeycloakAuthenticationToken) authentication;
			context = token.getAccount().getKeycloakSecurityContext();
			tokenString = context.getTokenString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tokenString;
	}

	private String assignRoleToUser(String token, User user) {

		ObjectMapper obj = new ObjectMapper();
		int responseCode;
		int responseCode1;
		try {

			List<RolesDto> list = new ArrayList<>();

			RolesDto roleDto = new RolesDto();
			roleDto.setId(user.getRole().getIds());
			roleDto.setName(user.getRole().getRoleCode());
			list.add(roleDto);

			String roleJson = obj.writeValueAsString(list);
			System.out.println(roleJson);
			URL urlForCreateUser = new URL(keycloakUrl + RestConstants.USER + "/" + user.getId()
					+ "/role-mappings/clients/" + keycloakClientId);
			HttpURLConnection conection = (HttpURLConnection) urlForCreateUser.openConnection();
			conection.setRequestMethod("POST");
			conection.setDoOutput(true);
			conection.setRequestProperty("Content-Type", "application/json");
			conection.setRequestProperty("Authorization", "Bearer " + token);
			conection.setRequestProperty("Content-Length", Integer.toString(roleJson.length()));

			try (OutputStream os = conection.getOutputStream()) {
				byte[] input = roleJson.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			responseCode = conection.getResponseCode();

			if (user.getRole().getRoleCode().equals("DEPOTADMIN")) {
				List<RolesDto> list1 = new ArrayList<>();
				RolesDto roleDto1 = new RolesDto();
				roleDto1.setId(manageUserRoleId);
				roleDto1.setName(manageUserRoleName);
				list1.add(roleDto1);

				String roleJson1 = obj.writeValueAsString(list1);

				URL urlForAddRole = new URL(keycloakUrl + RestConstants.USER + "/" + user.getId()
						+ "/role-mappings/clients/" + manageUserRealmId);
				HttpURLConnection conection1 = (HttpURLConnection) urlForAddRole.openConnection();
				conection1.setRequestMethod("POST");
				conection1.setDoOutput(true);
				conection1.setRequestProperty("Content-Type", "application/json");
				conection1.setRequestProperty("Authorization", "Bearer " + token);
				conection1.setRequestProperty("Content-Length", Integer.toString(roleJson1.length()));

				try (OutputStream os = conection1.getOutputStream()) {
					byte[] input = roleJson1.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				responseCode = conection.getResponseCode();
			}

			if (responseCode == 204) {
				return "OK";
			} else {
				return "FAIL";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "FAIL";
		}
	}

	private String deleteUser(String token, User user) {
		ObjectMapper obj = new ObjectMapper();
		int responseCode;
		String status = "";
		try {
			URL urlForDeleteUser = new URL(keycloakUrl + RestConstants.USER + "/" + user.getId());
			HttpURLConnection conection = (HttpURLConnection) urlForDeleteUser.openConnection();
			conection.setRequestMethod("DELETE");
			conection.setDoOutput(true);
			conection.setRequestProperty("Content-Type", "application/json");
			conection.setRequestProperty("Authorization", "Bearer " + token);
			responseCode = conection.getResponseCode();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	@PutMapping("/enableDisableUser")
	public ResponseEntity<ResponseStatus> enableDisableUser(@RequestBody UserDto userDto) {
		ObjectMapper obj = new ObjectMapper();
		int responseCode;
		String token = this.getToken();
		String role1 = this.getRole();
		if (role1.equals("SUPERADMIN") || role1.equals("DEPOTADMIN")) {
			try {

				Optional<User> user = userService.findByUserName(userDto.getId());
				if (!user.isPresent()) {
					return new ResponseEntity<>(new ResponseStatus("User not found.", HttpStatus.NOT_FOUND),
							HttpStatus.OK);
				}
				User usr = user.get();
				if (userDto.isEnabled()) {
					usr.setStatus(Boolean.TRUE);
					usr.setEnabled(true);
				} else {
					usr.setStatus(Boolean.FALSE);
					usr.setEnabled(false);
				}
				userService.saveUser(user.get());

				userDto.setEmailVerified(true);
				String userJson = obj.writeValueAsString(userDto);

				URL urlForDeleteUser = new URL(keycloakUrl + RestConstants.USER + "/" + userDto.getId());
				HttpURLConnection conection = (HttpURLConnection) urlForDeleteUser.openConnection();
				conection.setRequestMethod("PUT");
				conection.setDoOutput(true);
				conection.setRequestProperty("Content-Type", "application/json");
				conection.setRequestProperty("Authorization", "Bearer " + token);
				conection.setRequestProperty("Content-Length", Integer.toString(userJson.length()));

				try (OutputStream os = conection.getOutputStream()) {
					byte[] input = userJson.getBytes("utf-8");
					os.write(input, 0, input.length);
				}

				responseCode = conection.getResponseCode();
				if (responseCode == 204) {
					return new ResponseEntity<>(new ResponseStatus("User modified successfully.", HttpStatus.OK),
							HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							new ResponseStatus(conection.getResponseMessage(), HttpStatus.FORBIDDEN), HttpStatus.OK);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(new ResponseStatus("Something went wrong.", HttpStatus.FORBIDDEN),
						HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(new ResponseStatus("Not Authorized.", HttpStatus.UNAUTHORIZED), HttpStatus.OK);
		}
	}

}
