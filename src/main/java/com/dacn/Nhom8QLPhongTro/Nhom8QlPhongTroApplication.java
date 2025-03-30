package com.dacn.Nhom8QLPhongTro;

import com.dacn.Nhom8QLPhongTro.services.FilesStorageService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication
public class Nhom8QlPhongTroApplication implements CommandLineRunner {

	@Resource
	FilesStorageService storageService;
	public static void main(String[] args) {
		SpringApplication.run(Nhom8QlPhongTroApplication.class, args);
	}

	@Override
	public void run(String... args) {
		storageService.init();
	}
	


}
