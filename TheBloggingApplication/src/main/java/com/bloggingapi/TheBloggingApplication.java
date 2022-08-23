package com.bloggingapi;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.bloggingapi.entity.Role;
import com.bloggingapi.repository.RoleRepo;

@SpringBootApplication
public class TheBloggingApplication extends SpringBootServletInitializer implements CommandLineRunner {

	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;

	public static void main(String[] args) {
		System.out.println("Hello World!!");
		ApplicationContext ctx = SpringApplication.run(TheBloggingApplication.class, args);
		
		System.out.println("List of Beans instantiated");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
        	if(beanName.equals("userController") || beanName.equals("categoryController")
        			|| beanName.equals("postController"))
        		System.out.println(beanName);
        }
	}

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TheBloggingApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		
		//System.out.println(this.passwordEncoder.encode("password"));
		
		/*for(String path : ClassPath.getClassPath().split(";")) {
			System.out.println(path);
		}*/

		try {

			Role roleAdmin = new Role("ROLE_ADMIN");
			Role roleNormal = new Role("ROLE_NORMAL");

			if(!roleRepo.existsRoleByRoleName(roleAdmin.getRoleName())) {
				roleRepo.save(roleAdmin);
			}
			if(!roleRepo.existsRoleByRoleName(roleNormal.getRoleName())) {
				roleRepo.save(roleNormal);
			}

		}catch(Exception e) {
			System.out.println(e);
		}

	}
}
