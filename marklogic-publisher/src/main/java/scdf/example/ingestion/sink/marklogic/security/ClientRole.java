package scdf.example.ingestion.sink.marklogic.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * Provides the keys to two different database connections,
 * and a method for referring to their properties in build.gradle.
 */
public enum ClientRole {
	
	DOCUMENT_WRITER, DOCUMENT_READER;
	private String getPrefix() {
		switch(this) {
		case DOCUMENT_WRITER:
			return "marklogic.writer";
		case DOCUMENT_READER:
			return "marklogic.guest";
    	default: throw new SecurityException();
		}
	}
	
	/**
	 * Gets the name of the user parameter for this ClientRole.
	 * @return username parameter used in build.gradle to configure the database connection.
	 */
	public String getUserParam() {
		return getPrefix() + ".user";
	}
	
	/**
	 * Gets the name of the password parameter for this ClientRole.
	 * @return username parameter used in build.gradle to configure the database connection.
	 */
	public String getPasswordParam() {
		return getPrefix() + ".password";
	}
	
	/**
	 * Provides the database client role implied by the security context for the spring application.
	 * @return The ClientRole enum value that corresponds to the current logged-in user.
	 */
	public static ClientRole securityContextRole() {
		SecurityContext secContext = SecurityContextHolder.getContext();
		Collection<? extends GrantedAuthority> auths = secContext.getAuthentication().getAuthorities();
		if (auths.contains(new SimpleGrantedAuthority("ROLE_CONTRIBUTORS"))) {
			return DOCUMENT_WRITER;
		}
		else {
			return DOCUMENT_READER;
		}
	}
	
	/**
	 * Provides the username for implied by the security context for the spring application.
	 * @return The username value that corresponds to the current logged-in user.
	 */
	public static String securityContextUserName() {
		SecurityContext secContext = SecurityContextHolder.getContext();
		String userName = secContext.getAuthentication().getName();
		return userName;
	}
}