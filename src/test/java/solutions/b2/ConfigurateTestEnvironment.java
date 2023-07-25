package solutions.b2;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.MySQLContainer;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class ConfigurateTestEnvironment implements QuarkusTestResourceLifecycleManager {

	public static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.0")
			.withDatabaseName("autorizador")
			.withUsername("autorizadorUser")
			.withPassword("teste@123");
	
	@Override
	public Map<String, String> start() {
		MYSQL.start();
		
		Map<String, String> properties = new HashMap<>();
	    properties.put("quarkus.datasource.username", "autorizadorUser");
	    properties.put("quarkus.datasource.password", "teste@123");
	    properties.put("quarkus.datasource.jdbc.url",//
	        "jdbc:mysql://"+ MYSQL.getHost() +":" + MYSQL.getFirstMappedPort() + "/autorizador");

	    return properties;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
